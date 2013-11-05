var VV = {
    sensor : {},
    task : {},
    types : {},
};

/**
 * LatLng
 */
VV.types.LatLng = function(lat, lng)
{
    this.lat = lat;
    this.lng = lng;
};

VV.types.LatLng.prototype.getLat = function()
{
    return this.lat;
};

VV.types.LatLng.prototype.getLng = function()
{
    return this.lng;
};

/**
 * LatLngAlt
 */
VV.types.LatLngAlt = function(lat, lng, alt)
{
    this.lat = lat;
    this.lng = lng;
    this.alt = alt;
};

VV.types.LatLngAlt.prototype.getLat = function()
{
    return this.lat;
};

VV.types.LatLngAlt.prototype.getLng = function()
{
    return this.lng;
};

VV.types.LatLngAlt.prototype.getAlt = function()
{
    return this.alt;
};

/**
 * Sensor API
 */
VV.sensor.list = function()
{
    return getVvRte().listSensors();
};

VV.sensor.get = function(name)
{
    return getVvRte().getSensor(name);
};

/**
 * Task API
 */
VV.task.execute = function(taskParams, callback)
{
    var helper = {
        sequence : 0,
        valid : false,
        repeat : true,
        sensorValues : [],
    };

    getStdOut().println("execute started  helper=" + JSON.stringify(helper));

    while (helper.repeat)
    {
        getVvRte().executeTask(helper, taskParams);
        getStdOut().println("execute running  helper=" + JSON.stringify(helper));
        if (helper.valid)
        {
            getStdOut().println("valid");
            callback(helper.sensorValues);
            helper.valid = false;
        }
    }

    getStdOut().println("execute finished helper=" + JSON.stringify(helper));
};
