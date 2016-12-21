angular.module('urlShortener.gr4.app')
    .factory('urlShortenerFactory', urlShortenerFactory);

  urlShortenerFactory.$inject = ['$http'];

  function urlShortenerFactory($http) {

    var factory = {
      createShorterUrl: createShorterUrl,
      createJsonInfo: createJsonInfo,
      createHtmlInfo: createHtmlInfo
    };

    function createShorterUrl(url) {
      return $http({
        method: 'POST',
        url: 'http://localhost:8080/link',
	//headers: { 'Accept': 'text/html'},
        params: {url: url}
      });
    };
    function createJsonInfo(hash) {
        return $http({
          method: 'GET',
          url: 'http://localhost:8080/' + hash + "+",
          headers: {
              'Accept': 'application/json'
          }
        });
     };
     function createHtmlInfo(hash) {
         return $http({
           method: 'GET',
           url: 'http://localhost:8080/' + hash + "+",
           headers: {
               'Accept': 'text/html'
           }
         });
      };

    return factory;

  };
