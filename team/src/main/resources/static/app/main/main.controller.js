angular.module('urlShortener.gr4.app')
  .controller('MainCtrl', MainCtrl);

  MainCtrl.$inject = ['urlShortenerFactory', '$window'];

  function MainCtrl(urlShortenerFactory, $window) {

    var vm = this;
    vm.url = '';
    vm.shorterUrl = '';
    vm.hash = '';
    vm.browserName = '';
    vm.browserVersion = '';
    vm.os = '';
    vm.stringinfo = '';
    vm.error;
    vm.errorJson;

    vm.getShorterUrl = getShorterUrl;
    vm.getJsonInfo = getJsonInfo;
    vm.getHtmlInfo = getHtmlInfo;

    function getShorterUrl() {
    	//If vm.url has value
    	if (vm.url) {
    		urlShortenerFactory.createShorterUrl(vm.url)
    			.then(function success(response) {
	          //Success petition
	          vm.error = false;
	          vm.shorterUrl = response.data.uri;
	          vm.hash = response.data.hash;

	        }, function error(){
	          //Error 
	          vm.error = true;
	          vm.shorterUrl = 'ERROR!';
	        });
    	} else {
    		//vm.url no value => ERROR
    		vm.error = true;
    		vm.shorterUrl = 'ERROR!';
    	}
    };
    
    function getJsonInfo() {
    	//If vm.hash has value
    	if (vm.hash) {
    		urlShortenerFactory.createJsonInfo(vm.hash)
    			.then(function success(response) {
	          //Success petition
	          vm.error = false;
	          vm.browserName = response.data.browserName;
	          vm.browserVersion = response.data.browserVersion;
	          vm.os = response.data.os;
	        }, function error(){
	          //Error 
	          vm.errorJson = true;
	        });
    	} else {
    		//vm.hash no value => ERROR
    		vm.errorJson = true;
    	}
    };
    
    function getHtmlInfo() {
    	urlShortenerFactory.createHtmlInfo(vm.hash);
    };
     
  };
