angular.module('urlShortener.gr4.app')
  .controller('LocationListCtrl', LocationListCtrl);

  LocationListCtrl.$inject = ['locationFactory'];

  function LocationListCtrl(locationFactory) {

    var vm = this;

    vm.uri = '';

    vm.getLocation = getLocation;

    function getLocation() {
      locationFactory.getLocationUrl(vm.uri)
        .then(function(result) {
          console.log(result.data)
        })
    };
     
  };