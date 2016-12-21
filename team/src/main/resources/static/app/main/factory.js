angular.module('urlShortener.gr4.app')
    .factory('urlShortenerFactory', urlShortenerFactory);

  urlShortenerFactory.$inject = ['$http'];

  function urlShortenerFactory($http) {

    var factory = {
      createShorterUrl: createShorterUrl
    };

    function createShorterUrl(url) {
      return $http({
        method: 'POST',
        url: 'http://localhost:8080/link',
	//headers: { 'Accept': 'text/html'},
        params: {url: url}
      });
    };

    return factory;

  };
