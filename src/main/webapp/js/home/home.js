angular.module('home', []).controller('home', function($http,$scope) {
	
	/*var self = this;
	$http.get('/user/').then(function(response) {
		
		self.user = response.data.name;
	});*/
	
	$scope.obtainHelp_fn = function(){
		alert("1111");
		$http.get("http://localhost:8080/obtainHelp").then(
				function(data){
					alert(data);
				});
	}
	
});
