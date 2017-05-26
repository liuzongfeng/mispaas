function ordertoscope($scope,$http,id) {
	$scope.createOrder=function(){
		var zTree = $.fn.zTree.getZTreeObj("treeDemo1");
		var ids = onCheckId($http,$scope,zTree);
		var names = onCheckName($http,$scope,zTree);
		var ids=null;
		var names=null;
		console.log($scope.value);
		if($scope.value=="请选择租户"){
			$("#myModal").modal('show');
			return;
		}
		 var passOrde=
		  {
				id: null,
				billNo: null,
				proId: id,
				tenantId:$scope.value,
				status: null,
				crateDate: new Date(),
				approveId: null,
				approveDate: null,
				approveDescibe: null,
   		  };
		$http({
	        method: 'POST',
	        url: tenantSelfinterfaces.Var_createPaasOrder,
	        params:{"tenantId":$scope.value,"ids":ids,"names":names},
	        datatype:"json",
	        data:passOrde
	    }).then(function successCallback(response) {
	    	var page=null;
	    	applictionListController($scope,$http,page);
	        }, function errorCallback(response) {
	    });
	};
	//名称模糊查询应用
	$scope.getapplicationByInstanceName=function(){
		var page=null;
		appListBynameController($scope,$http,page);
		
	};
	//分类查询应用
	$scope.getapplicationBytemplateCategory=function(){
		var page=null;
		appListBynameController($scope,$http,page);
	};
	//分类查询产品
	$scope.gettemplateBytemplateCategory=function(){
		var id=null;
		var page=null;
		templateListController($scope,$http,id,page);
	};
	//名字模糊查产品
	$scope.gettemplatesBytemplateName=function(){
		var id=null;
		var page=null;
		templateListController($scope,$http,id,page);
		
	};
	//应用分页
	$scope.changeorderpage=function($event,page){
		var lis=$("#appfenye").children();
		for(i=0;i<lis.length;i++){
			lis.eq(i).children().css("backgroundColor","white");
			
		}
		$($event.target).css("backgroundColor","#458fe9");
		applictionListController($scope,$http,page);
	};
	//产品分页
	$scope.changetemplatepage=function($event,page){
		var lis=$("#chanpinfenye").children();
		for(i=0;i<lis.length;i++){
			lis.eq(i).children().css("backgroundColor","white");
			
		}
		$($event.target).css("backgroundColor","#458fe9");
		templateListController($scope,$http,id,page);
	};
	//撤销定单
	$scope.repealorder=function(orderId){
		
		$http({
	        method: 'PUT',
	        url: tenantSelfinterfaces.Var_repealOrder,
	        params:{"orderId":orderId},
	        datatype:"json",
	    }).then(function successCallback(response) {
	        }, function errorCallback(response) {
	    });
		var page=null;
		applictionListController($scope,$http,page);
		
	};
	//产品列表菜单样式控制
	$scope.appover=function(){
		$scope.templateShowpng1=false;
		$scope.templateShowpng2=true;
		$scope.appcolor="#458fe9";
	};
	$scope.appleave=function(){
		$scope.templateShowpng1=true;
		$scope.templateShowpng2=false;
		$scope.appcolor="#8e9094";
	};
	//应用菜单样式控制
	$scope.insover=function(){
		$scope.instanceShowpng1=false;
		$scope.instanceShowpng2=true;
		$scope.inscolor="#458fe9";
	};
	$scope.insleave=function(){
		$scope.instanceShowpng1=true;
		$scope.instanceShowpng2=false;
		$scope.inscolor="#8e9094";
	};
};
function transmitOrderId(orderId,$scope,$http){
	var orderId=orderId;
	$scope.changeApplicationUser=function(){
		var zTree = $.fn.zTree.getZTreeObj("treeDemo2");
		var ids = onCheckId($http,$scope,zTree);
		var names = onCheckName($http,$scope,zTree);
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
	        params:{"tenantId":$scope.tenantId,"ids":ids,"orderId":orderId,"names":names},
	        datatype:"json",
	    }).then(function successCallback(response) {
	    	var page=null;
	    	applictionListController($scope,$http,page);
	        }, function errorCallback(response) {
	    });
	};
};