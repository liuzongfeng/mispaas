var hello1 = angular
		.module('hello', [ 'ngRoute', 'auth', 'home', 'message', 'navigation','appexample','apptemplate' ])
		.config(

				function($routeProvider, $httpProvider, $locationProvider) {
					
					$locationProvider.html5Mode(true);
					$routeProvider.when('/', {
						templateUrl : '../js/navigation/appexample.html',
						controller : 'appexample',
						controllerAs : 'controller'
					}).when('/message', {
						templateUrl : '../js/message/message.html',
						controller : 'message',
						controllerAs : 'controller'
					}).when('/login', {
						templateUrl : '../js/navigation/login2.html',
						controller : 'navigation',
						controllerAs : 'controller'
					}).when('/pages/index_lzf.html', {
						templateUrl : '../js/navigation/apptemplate.html',
						controller : 'apptemplate',
						controllerAs : 'controller'
					}).when('/pages/index_lzf2.html', {
						templateUrl : '../js/navigation/appexample.html',
						controller : 'appexample',
						controllerAs : 'controller'
					}).otherwise('/');
					

					$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
					
					
				}).run(function(auth) {
			// Initialize auth module with the home page and login/logout path
			// respectively
			auth.init('/', '/login', '/logout');

		});


