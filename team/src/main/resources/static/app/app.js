angular.module('urlShortener.gr4.app', ['ui.router', 'ui.bootstrap', 'leaflet-directive'])
    .config(configStates);

configStates.$inject = ['$stateProvider', '$urlRouterProvider'];
function configStates($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('main', {
        url: '/main',
        controller: 'MainCtrl as mainVM',
        templateUrl: 'app/main/main.html'
      })
      .state('location', {
        url: '/location',
        controller: 'LocationListCtrl as listVM',
        templateUrl: 'app/location/list.html'
      })
      .state('SafeBrowsing', {
        url: '/SafeBrowsing',
        params: {
          uri: null
        },
        controller: 'SafeBrowsingCtrl as SafeBrowsingVM',
        templateUrl: 'app/SafeBrowsing/SafeBrowsing.html'
      });

     $urlRouterProvider.otherwise('main');

};
