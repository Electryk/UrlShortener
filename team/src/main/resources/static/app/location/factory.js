angular.module('urlShortener.gr4.app')
    .factory('locationFactory', locationFactory);

  locationFactory.$inject = ['$http'];

  function locationFactory($http) {

    var factory = {
      getLocationUrl: getLocationUrl
    };

    function getLocationUrl(uri, dateInit, dateEnd) {
      return $http({
        method: 'GET',
        url: 'http://localhost:8080/locations',
        params: {
          id: uri,
          dateInit: dateInit.getTime(),
          dateEnd: dateEnd.getTime()
        }
      });
    };

    return factory;

  };