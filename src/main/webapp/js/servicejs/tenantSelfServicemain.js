function ordertoscope($scope,$http) {
	$scope.createOrder=function(){
		 onCheck($http,$scope);
	};
	//名称模糊查询应用
	$scope.getapplicationByInstanceName=function(){
		applictionListController($scope,$http);
	};
	//分类查询
	$scope.getapplicationBytemplateCategory=function(){
		applictionListController($scope,$http);
	}
};