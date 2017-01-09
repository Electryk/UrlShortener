angular.module('urlShortener.gr4.app')
  .controller('LocationMapCtrl', LocationMapCtrl);

  LocationMapCtrl.$inject = ['$scope', 'locationFactory', 'leafletData'];

  function LocationMapCtrl($scope, locationFactory, leafletData) {

    var vm = this;

    vm.center = {
      lat: 40.095,
      lng: -3.823,
      zoom: 4
    };

  };