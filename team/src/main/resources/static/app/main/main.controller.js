angular.module('urlShortener.gr4.app')
  .controller('MainCtrl', MainCtrl);

  MainCtrl.$inject = ['urlShortenerFactory'];

  function MainCtrl(urlShortenerFactory) {

    var vm = this;
    vm.url = '';
    vm.shorterUrl = '';
    vm.error;

    vm.getShorterUrl = getShorterUrl;

    function getShorterUrl() {
    	
    	//If vm.url has value
    	if (vm.url) {
    		urlShortenerFactory.createShorterUrl(vm.url)
    			.then(function success(response) {
	          //Success petition
	          vm.error = false;
	          vm.shorterUrl = response.data.uri;

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
     
  };
