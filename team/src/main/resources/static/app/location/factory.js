angular.module('urlShortener.gr4.app')
    .factory('locationFactory', locationFactory);

  locationFactory.$inject = ['$http'];

  function locationFactory($http) {

    var factory = {
      getLocationUrl: getLocationUrl
    };

    function getLocationUrl(uri) {
      return $http({
        method: 'GET',
        url: 'http://localhost:8080/locations',
        params: {id: uri}
      });
    };

    return factory;

  };