angular.module('urlShortener.gr4.app')
    .factory('userinfoFactory', userinfoFactory);

  userinfoFactory.$inject = ['$http'];

  function userinfoFactory($http) {

    var factory = {
    		getUserInfo: getUserInfo
    };

    function getUserInfo(pattern, dateInit, dateEnd) {
      return $http({
        method: 'GET',
        url: 'http://localhost:8080/userinfo',
        params: {
          pattern: pattern,
          dateInit: dateInit.getTime(),
          dateEnd: dateEnd.getTime()
        }
      });
    };

    return factory;

  };