angular.module('urlShortener.gr4.app')
  .controller('MainCtrl', MainCtrl);

  MainCtrl.$inject = ['urlShortenerFactory', '$state'];

  function MainCtrl(urlShortenerFactory, $state) {
    var vm = this;
    vm.url = '';
    vm.shorterUrl = '';
    vm.error;

    /*por convencion crear primero el vm, luego las variables y por ultimo las funciones*/

    vm.getShorterUrl = getShorterUrl;

    function getShorterUrl() {

    	//If vm.url has value
    	if (vm.url) {
    		urlShortenerFactory.createShorterUrl(vm.url)
    			.then(function success(response) {
	          //Success petition
	          vm.error = false;
			      console.log(response);
	          vm.shorterUrl = response.data.uri;
            console.log(response.data);

            var isSafe = response.data.safe;
            if(!isSafe)
            {
              $state.go('SafeBrowsing', {
                uri: vm.shorterUrl
              });
            }


	        }, function error(error){
			console.log(error)
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
