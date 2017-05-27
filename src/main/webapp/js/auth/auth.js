angular.module('auth', []).factory(
		'auth',

		function($rootScope, $http, $location) {
			enter = function() {
				if ($location.path() != auth.loginPath) {
					auth.path = $location.path();
					if (!auth.authenticated) {
						$location.path(auth.loginPath);
					}
				}					
			}
			
			$rootScope.LogOut_fn = function(){
				var arrayObj = $rootScope.authorities;
				for(var i=0; i<arrayObj.length; i++){
					var userRole = arrayObj[i].authority;
					$rootScope[userRole] = false;              //将该用户所有的权限置为true
				}
				
				$rootScope.LogoOut = false;                    //认证通过，可以登出
				auth.authenticated = false;
				window.location.href="http://192.168.6.165:8080/logout";
			}
			
			login_fn = function(){
				$http({
					  method: 'GET',
					  url: 'http://192.168.6.165:8080/obtainUserInfo'
					}).then(function successCallback(response) {
						$rootScope.userId = response.data.username;       //用户id --退出使用
						$rootScope.token = response.data.password;        //token --退出使用
						
						$rootScope.username =  response.data.name;        //用户名--展示使用
						$rootScope.authorities = response.data.authorities;
						
						var arrayObj  = response.data.authorities;
						for(var i=0; i<arrayObj.length; i++){
							var userRole = arrayObj[i].authority;
							$rootScope[userRole] = true;              //将该用户所有的权限置为true
						}
						
						$rootScope.LogoOut = true;                    //认证通过，可以登出
						
						auth.authenticated = true;
						callback && callback(auth.authenticated);
						$location.path(auth.path==auth.loginPath ? auth.homePath : auth.path);
					}, function errorCallback(response) {
					    // called asynchronously if an error occurs
					    // or server returns response with an error status.
				});
				
			} 
			login_fn();
			
			var auth = {

				authenticated : false,

				loginPath : '/login',
				logoutPath : '/logout',
				homePath : '/',
				path : $location.path(),

				authenticate : function(credentials, callback) {

					/*var headers = credentials && credentials.username ? {
						authorization : "Basic "
								+ btoa(credentials.username + ":"
										+ credentials.password)
					} : {};

					$http.get('user', {
						headers : headers
					}).then(function(response) {
						if (response.data.name) {
							auth.authenticated = true;
						} else {
							auth.authenticated = false;
						}
						callback && callback(auth.authenticated);
						$location.path(auth.path==auth.loginPath ? auth.homePath : auth.path);
					}, function() {
						auth.authenticated = false;
						callback && callback(false);
					});*/
					
					auth.authenticated = true;
					
					
				},
				clear : function() {
					$location.path(auth.loginPath);
					auth.authenticated = false;
					/*$http.post(auth.logoutPath, {}).then(function() {
						//加载该用户的资源权限
						$http.get('/obtainResource',{}).then(function(response){
							for(var i=0; i< response.data.length; i++){
								var grantd = response.data[i].authority;
								
								$rootScope[grantd] = false;
							}
							
						});
						console.log("Logout succeeded");
					}, function() {
						console.log("Logout failed");
					});*/
				},

				init : function(homePath, loginPath, logoutPath) {

					auth.homePath = homePath;
					auth.loginPath = loginPath;
					auth.logoutPath = logoutPath;

					auth.authenticate({}, function(authenticated) {
						if (authenticated) {
							$location.path(auth.path);
						}
					})

					// Guard route changes and switch to login page if unauthenticated
					$rootScope.$on('$routeChangeStart', function() {
						enter();
					});

				}

			};

			return auth;

		});
