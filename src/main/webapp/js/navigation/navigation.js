angular.module('navigation', ['ngRoute', 'auth']).controller(
		'navigation',

		function($route,$http, auth,$rootScope,$scope) {
			
			var self = this;

			self.credentials = {};
			
			self.tab = function(route) {
				return $route.current && route === $route.current.controller;
			};

			self.authenticated = function() {
				return auth.authenticated;
			}

			self.login = function() {
				
				auth.authenticate(self.credentials, function(authenticated) {
					if (authenticated) {
						console.log("Login succeeded")
						self.error = false;
						$rootScope.authenticated = true;//
						//加载该用户的资源权限
						/*$http.get('/obtainResource',{}).then(function(response){
							for(var i=0; i< response.data.length; i++){
								var grantd = response.data[i].authority;
								
								$rootScope[grantd] = true;
							}
							
						});*/
						
					} else {
						console.log("Login failed")
						self.error = true;
						$rootScope.authenticated = false;
					}
				})
			};

			self.logout = auth.clear;
			
			//处理左侧树
			$scope.leftTreeShow = true;
			$scope.test1 = function(){
				alert("处理左侧树");
				
				if($scope.leftTreeShow){
					$scope.leftTreeShow = false;
				}else{
					$scope.leftTreeShow = true;
				}
				
			}
			

		});
