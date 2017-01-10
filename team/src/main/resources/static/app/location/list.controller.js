angular.module('urlShortener.gr4.app')
  .controller('LocationListCtrl', LocationListCtrl);

  LocationListCtrl.$inject = ['$scope', 'locationFactory', 'leafletData'];

  function LocationListCtrl($scope, locationFactory, leafletData) {

    var vm = this;

    vm.pattern = '';
    vm.locations = [];
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

    vm.center = {
      lat: 40.095,
      lng: -3.823,
      zoom: 4
    };

    vm.getLocation = getLocation;
    vm.open = open;
    vm.open2 = open2;
    vm.clickMapBtn = clickMapBtn;
    vm.clickListBtn = clickListBtn;
    vm.clickFindBtn = clickFindBtn;

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

    function clickMapBtn() {
      vm.tableBtn = false;
      vm.mapBtn = true;
      vm.findDiv = false;
      _setMapMarkers();
      _addCircleMarkers();
    };

    function clickListBtn() {
      vm.tableBtn = true;
      vm.mapBtn = false;
      if (vm.locations.length !== 0) {
        vm.findDiv = false;
      } else {
        vm.findDiv = true;
      }
    };

    function clickFindBtn() {
      vm.tableBtn = true;
      vm.mapBtn = false;
      vm.findDiv = true;
    };

    function getLocation() {
      vm.dateInit.setSeconds(00);
      vm.dateEnd.setSeconds(59);
      locationFactory.getLocationUrl(vm.pattern, vm.dateInit, vm.dateEnd)
        .then(function(result) {
          console.log(result.data)
          vm.locations = result.data;
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

    function _setMapMarkers() {
      vm.leaflet.markers = [];

      for (var i = 0; i < vm.locations.length; i++) {
        var position = vm.locations[i];
        if (position.latitude && position.longitude) {
          var marker = {
            lat: position.latitude,
            lng: position.longitude
          };

          vm.leaflet.markers.push(marker);
        }
      }

      _processMarkers();
    };

    function _processMarkers() {
      vm.markers = [];

      for (var i = 0; i < vm.leaflet.markers.length; i++) {
        var marker = vm.leaflet.markers[i];
        var found = false;
        var count = 1;

        for (var j = 0; j < vm.markers.length && !found; j++) {
          if (vm.markers[j].latitude === marker.latitude 
            && vm.markers[j].longitude === marker.longitude) {
            vm.markers[j].count = vm.markers[j].count + 1;
            found = true;
            count = vm.markers[j].count;
          } 
        }

        if (!found && count === 1) {
          marker.count = 1;
          vm.markers.push(marker);
        }
      }
    };

    function open() {
      vm.opened = true;
    };

    function open2() {
      vm.opened2 = true;
    };
  };