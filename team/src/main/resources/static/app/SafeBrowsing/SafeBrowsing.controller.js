angular.module('urlShortener.gr4.app')
  .controller('SafeBrowsingCtrl', SafeBrowsingCtrl);

  SafeBrowsingCtrl.$inject = ['urlShortenerFactory', '$stateParams'];

  function SafeBrowsingCtrl(urlShortenerFactory, $stateParams) {
    var vm = this;
    vm.url = $stateParams.uri; /*aqui guarda la url que escribimos*/
  };
