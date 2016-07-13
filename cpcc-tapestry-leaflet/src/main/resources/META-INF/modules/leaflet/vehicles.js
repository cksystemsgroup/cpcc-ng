define([ "leaflet", "t5/core/console" ],

function(leaflet, console)
{
	var module = {};

	/*
	 * Vehicle icon
	 */
	leaflet.VehicleIcon = leaflet.Icon.extend({

		options : {
			shadowUrl : null,
			iconSize : new leaflet.Point(32, 32),
			iconAnchor : new leaflet.Point(16, 16),
			popupAnchor : new leaflet.Point(1, -16),
			className : 'vehicle-icon'
		},

		taskId : '',
		taskState : '',
		vehicleId : '',
		vehicleState : '',
		vehicleHeading : 0,
		vehicleName : '',

		createIcon : function()
		{
			var div = document.createElement('div');

			this.img = this._createImg(this.options.iconUrl);
			div.appendChild(this.img);

			this.vehicleDiv = document.createElement('div');
			div.appendChild(this.vehicleDiv);

			this.taskDiv = document.createElement('div');
			div.appendChild(this.taskDiv);

			this.setVvState({
				geometry : {
					geometries : []
				}
			});

			this._setIconStyles(div, 'icon');
			return div;
		},

		createShadow : function()
		{
			return null;
		},

		setVvState : function(vvs)
		{
			var geometries = vvs.geometry.geometries;

			var html = '';

			if (vvs.properties && vvs.properties.vvsTotal !== undefined)
			{
				html = '<table class="vv-table"><tr class="vv-info"><td class="vv-table-stats-total">'
						+ vvs.properties.vvsTotal + '</td><td class="vv-table-stats-active">'
						+ vvs.properties.vvsActive + '</td><td class="vv-table-stats-migrating">'
						+ vvs.properties.vvsMigrating + '</td><td class="vv-table-stats-inactive">'
						+ (vvs.properties.vvsInterrupted + vvs.properties.vvsDormant) + '</td></tr></table>'
			}

			if (this.taskDiv.innerHTML != html)
			{
				this.taskDiv.innerHTML = html;
			}
		},

		setVehicleState : function(vehicleId, props)
		{
			var vehicleState = props.rvState;
			var vehicleHeading = props.rvHeading;
			var vehicleName = props.rvName;

			if (this.vehicleId != vehicleId || this.vehicleHeading != vehicleHeading || this.vehicleName != vehicleName
					|| this.vehicleState != vehicleState)
			{
				this.vehicleId = vehicleId;
				this.vehicleName = vehicleName;
				this.vehicleHeading = vehicleHeading;
				this.vehicleState = vehicleState;
				// this.vehicleDiv.innerHTML = vehicleName + ' (' + (vehicleId || '??') + ')';
				this.vehicleDiv.innerHTML = vehicleName;
				this.vehicleDiv.className = 'vehicle-info vehicle-info-' + vehicleState;

				var rotation = 270 - vehicleHeading;
				this.img.style.transform = 'rotate(' + rotation + 'deg)';
				this.img.style['-ms-transform'] = 'rotate(' + rotation + 'deg)';
				this.img.style['-webkit-transform'] = 'rotate(' + rotation + 'deg)';
			}
		}
	});

	/*
	 * leaflet.VehicleMarker is used to display generic vehicles on the map.
	 */
	leaflet.VehicleMarker = leaflet.Marker.extend({
		options : {
			baseUrl : 'images',
			icon : new leaflet.VehicleIcon({
				iconUrl : 'images/VehicleGrey_32.png'
			}),
			iconImages : {
				busy : 'VehicleBlack_32.png',
				idle : 'VehicleGreen_32.png',
				offline : 'VehicleGrey_32.png'
			},
			clickable : false,
			className : 'vehicle-marker-icon'
		},

		vehicleState : '',

		setVehicleState : function(id, props)
		{
			if (this.vehicleState != props.rvState)
			{
				this.vehicleState = props.rvState;
				this.options.icon = new leaflet.VehicleIcon({
					iconUrl : this.options.baseUrl + '/' + this.options.iconImages[props.rvState]
				});
				this.setIcon(this.options.icon);
			}

			this.options.icon.setVehicleState(id, props);
		},

		setVvState : function(task, state)
		{
			this.options.icon.setVvState(task, state)
		},
	});

	/*
	 * leaflet.QuadrotorMarker is used to display quadrotor UAVs on the map.
	 */
	leaflet.QuadrotorMarker = leaflet.VehicleMarker.extend({
		options : {
			iconImages : {
				busy : 'QuadrotorBlack_32.png',
				idle : 'QuadrotorGreen_32.png',
				offline : 'QuadrotorGrey_32.png'
			}
		}
	});

	/*
	 * leaflet.ModelAirPlaneMarker is used to display model air-planes on the
	 * map.
	 */
	leaflet.ModelAirPlaneMarker = leaflet.VehicleMarker.extend({
		options : {
			iconImages : {
				busy : 'ModelAirPlaneBlack_32.png',
				idle : 'ModelAirPlaneGreen_32.png',
				offline : 'ModelAirPlaneGrey_32.png'
			}
		}
	});

	/*
	 * leaflet.DrifterMarker is used to display drifter buoys on the map.
	 */
	leaflet.DrifterMarker = leaflet.VehicleMarker.extend({
		options : {
			iconImages : {
				busy : 'DrifterBlack_32.png',
				idle : 'DrifterGreen_32.png',
				offline : 'DrifterGrey_32.png'
			}
		}
	});

	/*
	 * leaflet.BoatMarker is used to display boats on the map.
	 */
	leaflet.BoatMarker = leaflet.VehicleMarker.extend({
		options : {
			iconImages : {
				busy : 'BoatBlack_32.png',
				idle : 'BoatGreen_32.png',
				offline : 'BoatGrey_32.png'
			}
		}
	});

	/*
	 * leaflet.GroundStationMarker is used to display ground stations on the
	 * map.
	 */
	leaflet.GroundStationMarker = leaflet.VehicleMarker.extend({
		options : {
			iconImages : {
				busy : 'GroundStationBlack_32.png',
				idle : 'GroundStationGreen_32.png',
				offline : 'GroundStationGrey_32.png'
			}
		}
	});

	module.vehiceMap = {};
	module.vehiceMap["QUADROCOPTER"] = leaflet.QuadrotorMarker;
	module.vehiceMap["BOAT"] = leaflet.BoatMarker;
	module.vehiceMap["DRIFTER"] = leaflet.DrifterMarker;
	module.vehiceMap["FIXED_WING_AIRCRAFT"] = leaflet.ModelAirPlaneMarker;
	module.vehiceMap["GROUND_STATION"] = leaflet.GroundStationMarker;

	// also: UNMANNED_AERIAL_VEHICLE, UNMANNED_SURFACE_VEHICLE,
	// UNMANNED_UNDERWATER_VEHICLE, MOBILE_PHONE, TABLET

	/*
	 * create a vehicle marker
	 */
	module.createVehicle = function(type, pos)
	{
		var markerClass = module.vehiceMap[type] ? module.vehiceMap[type] : leaflet.VehicleMarker;
		return new markerClass(pos);
	}

	return module;
});