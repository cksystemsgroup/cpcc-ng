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
			this.setVehicleState('0', 'none');
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

			if (geometries.length > 0)
			{
				html = html + '<table class="vv-table">';

				for (var k = 0; k < geometries.length; ++k)
				{
					html = html + '<tr class="vv-info vv-info-' + geometries[k].properties.state + '"><td>'
							+ geometries[k].id + '</td><td>' + geometries[k].properties.name + '</td></tr>';
				}

				html = html + "</table>";
			}

			if (this.taskDiv.innerHTML != html)
			{
				this.taskDiv.innerHTML = html;
			}
		},

		setVehicleState : function(vehicleId, vehicleState, vehicleHeading, vehicleName)
		{
			if (this.vehicleId != vehicleId || this.vehicleHeading != vehicleHeading || this.vehicleName != vehicleName
					|| this.vehicleState != vehicleState)
			{
				this.vehicleId = vehicleId;
				this.vehicleName = vehicleName;
				this.vehicleHeading = vehicleHeading;
				this.vehicleState = vehicleState;
				this.vehicleDiv.innerHTML = 'v: ' + vehicleName + ' (' + (vehicleId || '??') + ') <br/>h: '
						+ Number(vehicleHeading).toFixed();
				this.vehicleDiv.className = 'vehicle-info vehicle-info-' + vehicleState;

				this.img.style.transform = 'rotate(' + (270 - vehicleHeading) + 'deg)';
				this.img.style['-ms-transform'] = 'rotate(' + (270 - vehicleHeading) + 'deg)';
				this.img.style['-webkit-transform'] = 'rotate(' + (270 - vehicleHeading) + 'deg)';
			}
		}
	});

	/*
	 * leaflet.VehicleMarker is used to display generic vehicles on the map.
	 */
	leaflet.VehicleMarker = leaflet.Marker.extend({
		options : {
			baseUrl : 'images',
			icon : new leaflet.Icon.Default(),

			iconImages : {
				busy : 'VehicleBlack_32.png',
				idle : 'VehicleGreen_32.png',
				offline : 'VehicleGrey_32.png'
			},
			clickable : false,
			className : 'vehicle-marker-icon'
		},

		vehicleState : '',

		setVehicleState : function(id, state, heading, name)
		{
			if (!this.icons.busy)
			{
				this.icons.busy = new leaflet.VehicleIcon({
					iconUrl : this.options.baseUrl + '/' + this.options.iconImages.busy
				});

				this.icons.idle = new leaflet.VehicleIcon({
					iconUrl : this.options.baseUrl + '/' + this.options.iconImages.idle
				});

				this.icons.offline = new leaflet.VehicleIcon({
					iconUrl : this.options.baseUrl + '/' + this.options.iconImages.offline
				});
			}

			if (this.vehicleState != state)
			{
				this.vehicleState = state;
				this.setIcon(this.icons[state]);
			}

			this.options.icon.setVehicleState(id, state, heading, name);

			return this;
		},

		setVvState : function(task, state)
		{
			this.options.icon.setVvState(task, state)
			return this;
		},
	});

	/*
	 * leaflet.QuadrotorMarker is used to display quadrotor UAVs on the map.
	 */
	leaflet.QuadrotorMarker = leaflet.VehicleMarker.extend({
		icons : {},
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
		icons : {},
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
		icons : {},
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
		icons : {},
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
		icons : {},
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