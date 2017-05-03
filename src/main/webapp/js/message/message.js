angular.module('message', ['ngRoute']).config(
		function($routeProvider, $httpProvider, $locationProvider) {

			$locationProvider.html5Mode(true);

			$routeProvider.when('/textpass', {
				templateUrl : 'textpass.html',
				controller : 'pass',
				controllerAs : 'controller'
			}).otherwise('/');

			$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

		}	
)
.controller('message', function($http) {
	var self = this;
	$http.get('/resource/').then(function(response) {
		self.greeting = response.data;
	});
	$http.get('/myhello').then(function(response) {
		self.greeting1 = response.data;
	});
	
	
});
