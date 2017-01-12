'use strict';

var GeoIpModel = require('./model');
var GeoLocationService = require('../geoIp-service/service');

exports.get = get;

/**
 * Get the geolocation asociated to the <ip>
 */
function get(ip) {
  var queryDoc = {
    ip: ip
  };

  //First find the ip in the data base. If the ip stay in the
  //data base -> not request to external service.
  var query = GeoIpModel.findOne(queryDoc);
  return query.exec()
    .then(function(_location){
      if (!_location) {
        //The ip isn't contains in the data base. -> request
        //info to the external service
        return GeoLocationService.getLocationByIp(ip);
      }

      return _location;
    })
    .then(function(geoPosition) {
      var location = _createLocationObj(ip, geoPosition);

      if (!geoPosition._id && location.country !== 'private range') {
        //If the location country isn't private range and not stay in the 
        //data base -> insert the location into the data base.
        _insertLocation(location);
      }

      return location;
    });
};

/**
 * Create a new geolocation.
 */
function _insertLocation(location) {
  var newGeoIp = new GeoIpModel(location);
  return newGeoIp.save();
};

/**
 * Create an object location.
 */
function _createLocationObj(ip, res) {
  var location = {
    ip: ip,
    dateTime: new Date
  };

  if (res.status === 'fail') {
    location.country = 'private range';
  } else {
    location.city = res.city;
    location.country = res.country;
    location.lat = res.lat;
    location.lon = res.lon;
    location.regionName = res.regionName;
    location.org = res.org;
  }
  
  return location;
};