function ordertoscope($scope,$http,id) {
	$scope.createOrder=function(){
		 var zTree = $.fn.zTree.getZTreeObj("treeDemo1");
		var ids = onCheck($http,$scope,zTree);
		 var passOrde=
		  {
				id: null,
				billNo: null,
				proId: id,
				tenantId: 1,
				status: null,
				crateDate: new Date(),
				approveId: null,
				approveDate: null,
				approveDescibe: null,
   		  };
		$http({
	        method: 'POST',
	        url: tenantSelfinterfaces.Var_createPaasOrder,
	        params:{"tenantId":1,"ids":ids},
	        datatype:"json",
	        data:passOrde
	    }).then(function successCallback(response) {
	    	applictionListController($scope,$http);
	        }, function errorCallback(response) {
	    });
	};
	//名称模糊查询应用
	$scope.getapplicationByInstanceName=function(){
		alert("名称模糊查询应用");
		applictionListController($scope,$http);
	};
	//分类查询应用
	$scope.getapplicationBytemplateCategory=function(){
		alert("分类查询应用");
		applictionListController($scope,$http);
	};
	//分类查询产品
	$scope.gettemplateBytemplateCategory=function(){
		var id=null;
		alert("分类查询产品");
		templateListController($scope,$http,id);
	};
	//名字模糊查产品
	$scope.gettemplatesBytemplateId=function(){
		var id=null;
		alert("名字模糊查产品");
		templateListController($scope,$http,id);
	};
	
};

function transmitOrderId(orderId,$scope,$http){
	var orderId=orderId;
	$scope.changeApplicationUser=function(){
		var zTree = $.fn.zTree.getZTreeObj("treeDemo2");
		var ids = onCheck($http,$scope,zTree);
		alert(ids);
		alert(orderId);
		//删除原来的组织关系数据
		$http({
	        method: 'DElETE',
	        url: tenantSelfinterfaces.Var_deleteInstanceAndOrgShip,
	        params:{"orderId":orderId},
	        datatype:"json",
	    }).then(function successCallback(response) {
	        }, function errorCallback(response) {
	    });
		//插入新的组织关系
		$http({
	        method: 'POST',
	        url: tenantSelfinterfaces.Var_addInstanceAndOrgShip,
	        params:{"tenantId":1,"ids":ids,"orderId":orderId},
	        datatype:"json",
	    }).then(function successCallback(response) {
	    	applictionListController($scope,$http);
	        }, function errorCallback(response) {
	    });
	};
};