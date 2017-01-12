'use strict';

var Promise = require('bluebird');
var request =  Promise.promisifyAll(require('request'));

exports.getLocationByIp = getLocationByIp;

/**
 * This function connect with the external server and return the response.
 * If the response status is < 200 or >= 400 this function return a JSON
 * object with status = fail. And if during the request to the server has an
 * error, this function return a JSON object with status = fail.
 */
function getLocationByIp(ip) {
  return request.getAsync('http://ip-api.com/json/' + ip)
    .then(function(res, err) {
    	//Check if the status code is correct
      if (res.statusCode >= 200 && res.statusCode < 400) {
      	//Status code correct -> server online
      	return JSON.parse(res.body);
      } else {
      	//Status code not correct -> return a msg with status = 'fail'
      	return {
      		status: 'fail'
      	};
      }
    })
    .catch(function() {
    	//ERROR -> server not online! return a msg with status = 'fail'
    	return {
    		status: 'fail'
    	};
    });
};