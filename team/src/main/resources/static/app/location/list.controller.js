angular.module('urlShortener.gr4.app')
  .controller('LocationListCtrl', LocationListCtrl);

  LocationListCtrl.$inject = ['$scope', 'locationFactory'];

  function LocationListCtrl($scope, locationFactory) {

    var vm = this;

    vm.pattern = '';
    vm.locations = [];
    vm.opened = false;
    vm.opened2 = false;
    vm.showFilterInit = false;
    vm.showFilterEnd = false;
    vm.dateInit = new Date(0);
    vm.dateEnd = new Date();

    vm.getLocation = getLocation;
    vm.open = open;
    vm.open2 = open2;

    $scope.$watch(function() {
      return vm.showFilterInit;
    }, function(newValue, oldValue) {
      if (vm.showFilterInit) {
        vm.dateInit = new Date();
      } else {
        vm.dateInit = new Date(0);
      }
    });

    $scope.$watch(function() {
      return vm.showFilterEnd;
    }, function(newValue, oldValue) {
      if (vm.showFilterEnd) {
        vm.dateEnd = new Date();
      } else {
        vm.dateEnd = new Date();
      }
    });

    function getLocation() {
      vm.dateInit.setSeconds(00);
      vm.dateEnd.setSeconds(59);
      locationFactory.getLocationUrl(vm.pattern, vm.dateInit, vm.dateEnd)
        .then(function(result) {
          console.log(result.data)
          vm.locations = result.data;
        })
    };  

    function open() {
      vm.opened = true;
    };

    function open2() {
      vm.opened2 = true;
    };
  };