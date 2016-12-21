angular.module('urlShortener.gr4.app')
  .controller('LocationListCtrl', LocationListCtrl);

  LocationListCtrl.$inject = ['locationFactory'];

  function LocationListCtrl(locationFactory) {

    var vm = this;

    vm.pattern = '';
    vm.locations = [];
    vm.opened = false;
    vm.opened2 = false;
    vm.dateInit = new Date();
    vm.dateEnd = new Date();

    vm.getLocation = getLocation;
    vm.open = open;
    vm.open2 = open2;

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