angular.module('urlShortener.gr4.app')
  .controller('UserInfoCtrl', UserInfoCtrl);

  UserInfoCtrl.$inject = ['$http', '$scope', 'userinfoFactory', 'leafletData'];

  function UserInfoCtrl($http, $scope, userinfoFactory, leafletData) {

    var vm = this;

    vm.pattern = '';
    vm.clicks = [];
    vm.opened = false;
    vm.opened2 = false;
    vm.showFilterInit = false;
    vm.showFilterEnd = false;
    vm.tableBtn = true;
    vm.mapBtn = false;
    vm.findDiv = true;
    vm.dateInit = new Date(0);
    vm.dateEnd = new Date();
    vm.leaflet = {};
    vm.markers = [];
    vm.circleMap;
    numUsuarios = 0;

    vm.center = {
      lat: 40.095,
      lng: -3.823,
      zoom: 4
    };

    vm.getInfo = getInfo;
    vm.open = open;
    vm.open2 = open2;
    vm.clickFindBtn = clickFindBtn;
    
    $http.get('/getUniqueUsers')
    .success(function(data) {
        $scope.numUsuarios = data;
        console.log(data);
    })
    .error(function(data) {
        console.log('Error: ' + data);
    });

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
 
    function clickFindBtn() {
      vm.tableBtn = true;
      vm.mapBtn = false;
      vm.findDiv = true;
    };

    function getInfo() {
      vm.dateInit.setSeconds(00);
      vm.dateEnd.setSeconds(59);
      userinfoFactory.getUserInfo(vm.pattern, vm.dateInit, vm.dateEnd)
        .then(function(result) {
          console.log(result.data)
          vm.clicks = result.data;
        })
    };

    function _addCircleMarkers() {
      leafletData.getMap('map')
        .then(function(map) {
          if (vm.circleMap) {
            map.removeLayer(vm.circleMap);
          }

          for (var i = 0; i < vm.markers.length; i++) {
            var marker = vm.markers[i];
            vm.circleMap = L.circle([marker.lat, marker.lng], marker.count * 100);
            vm.circleMap.addTo(map);
          }
        })
        .catch(function(err){
          console.log(err)
        });
    };  


    function open() {
      vm.opened = true;
    };

    function open2() {
      vm.opened2 = true;
    };
  };