var hello1 = angular
		.module('hello', [ 'ngRoute', 'auth', 'home', 'message', 'navigation','appexample','apptemplate' ])
		.config(

				function($routeProvider, $httpProvider, $locationProvider) {
					
					$locationProvider.html5Mode(true);
					$routeProvider.when('/', {
						templateUrl : '../paas_core/js/navigation/appexample.html',
						controller : 'appexample',
						controllerAs : 'controller'
					}).when('/message', {
						templateUrl : '../paas_core/js/message/message.html',
						controller : 'message',
						controllerAs : 'controller'
					}).when('/login', {
						templateUrl : '../paas_core/js/navigation/login2.html',
						controller : 'navigation',
						controllerAs : 'controller'
					}).when('/apptemplate2', {
						templateUrl : '../paas_core/js/navigation/apptemplate.html',
						controller : 'apptemplate',
						controllerAs : 'controller'
					}).when('/appexample2', {
						templateUrl : '../paas_core/js/navigation/appexample.html',
						controller : 'appexample',
						controllerAs : 'controller'
					}).otherwise('/');
					

					$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
					
					
				}).run(function(auth) {
			// Initialize auth module with the home page and login/logout path
			// respectively
			auth.init('/', '/login', '/logout');

		});


