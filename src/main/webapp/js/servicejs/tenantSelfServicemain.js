function ordertoscope($scope,$http,id) {
	$scope.gettenantByid=function(tenantId,tenantName){
		$("#userMenuStatuetitle").html(tenantName);
		$scope.newtenantId=tenantId;
	};
	$scope.createOrder=function(){
		var zTree = $.fn.zTree.getZTreeObj("treeDemo1");
		var ids = onCheckId($http,$scope,zTree);
		var names = onCheckName($http,$scope,zTree);
		if($("#userMenuStatuetitle").html()=="请选择租户"){
			$("#myModal").modal('show');
			return;
		}
		 var passOrde=
		  {
				id: null,
				billNo: null,
				proId: id,
				tenantId:$scope.newtenantId,
				status: null,
				crateDate: new Date(),
				approveId: null,
				approveDate: null,
				approveDescibe: null,
   		  };
		$http({
	        method: 'POST',
	        url: tenantSelfinterfaces.Var_createPaasOrder,
	        params:{"tenantId":$scope.newtenantId,"ids":ids,"names":names},
	        datatype:"json",
	        data:passOrde
	    }).then(function successCallback(response) {
	    	var page=null;
	    	appListBynameController($scope,$http,page);
	        }, function errorCallback(response) {
	    });
		 $("#myApplicationListTab").attr("class","active");
         $("#tab_51_3").attr("class","tab-pane fade in active");
         $scope.ListOfGoodsTab=false;
         $scope.DetailsOfGoodsTab=false;
         $("#tab_51_1").attr("class","tab-pane fade");
         $("#tab_51_2").attr("class","tab-pane fade");
	};
	//分类查询产品
	$scope.gettemplateBytemplateCategory=function(templateCategory){
		if(templateCategory=="chanpinguolv"){
			$("#dropdownMenuStatuetitle").html("产品分类过滤");
		}else{
			$("#dropdownMenuStatuetitle").html(templateCategory);
		}
		
		$scope.usetemplateCategory=templateCategory;
		var id=null;
		var page=null;
		templateListController($scope,$http,id,page);
	}
	//分类查询应用
	$scope.getapplicationBytemplateCategory=function(templateCategory){
		if(templateCategory=="yingyongguolv"){
			$("#appMenuStatuetitle").html("应用分类过滤");
		}else{
			$("#appMenuStatuetitle").html(templateCategory);
		}
		$scope.useapptemplateCategory=templateCategory;
		var page=null;
		appListBynameController($scope,$http,page);
	}
	//名称模糊查询应用
	$scope.appKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
        	var page=null;
    		appListBynameController($scope,$http,page);
        }
    };
    
	$scope.getapplicationByInstanceName=function(){
		var page=null;
		appListBynameController($scope,$http,page);
	};
	//名字模糊查产品
	$scope.tepKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
        	var id=null;
    		var page=null;
    		templateListController($scope,$http,id,page);
        }
    };
	$scope.gettemplatesBytemplateName=function(){
		var id=null;
		var page=null;
		templateListController($scope,$http,id,page);
		
	};
	//产品分页
	 changetemplatepage($scope,$http,id);
	//应用分页
	 changeorderpage($scope,$http,id);
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
		var page=$scope.nowpage;
		appListBynameController($scope,$http,page);
	};
	//产品列表菜单样式控制
	$scope.appover=function(){
		$scope.templateShowpng1=false;
		$scope.templateShowpng2=true;
		$scope.appcolor="#458fe9";
		$("#appcolor").css("color","#458fe9");
	};
	$scope.appleave=function(){
		$scope.templateShowpng1=true;
		$scope.templateShowpng2=false;
		$scope.appcolor="#8e9094";
		$("#appcolor").css("color","#8e9094");
	};
	//应用菜单样式控制
	$scope.insover=function(){
		$scope.instanceShowpng1=false;
		$scope.instanceShowpng2=true;
		$scope.inscolor="#458fe9";
		$("#inscolor").css("color","#458fe9");
	};
	$scope.insleave=function(){
		$scope.instanceShowpng1=true;
		$scope.instanceShowpng2=false;
		$scope.inscolor="#8e9094";
		$("#inscolor").css("color","#8e9094");
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
	    	appListBynameController($scope,$http,page);
	        }, function errorCallback(response) {
	    });
		$("#myModal2").modal('show');
		 $("#myApplicationListTab").attr("class","active");
		 $("#myApplicationUsers").attr("class","");
	     $("#tab_51_3").attr("class","tab-pane fade in active");
	     $("#tab_51_4").attr("class","tab-pane fade");
	};
};
//产品分页
function changetemplatepage($scope,$http,id){
	$scope.selPage=1;
	var page=$scope.nowpage;
	$scope.newPages = $scope.pages > 5 ? 5 : $scope.pages; //按钮数超过5个，按5个显示
	$scope.pageList = [];                          //页码集合	
	//分页要repeat的数组
	for (var i = 0; i < $scope.newPages; i++) { //处理按钮数少于5个的页码集合
		$scope.pageList.push(i + 1);
	}
	
	//当前页不能小于1，大于最大
	if (page < 1 || page > $scope.pages) return;
	
	//最多显示分页数5
	if (page > 2) {
	//因为只显示5个页码，大于2页开始分页转换
		var newpageList = [];
		var i = (page - 3);
		if($scope.pages - i < 5){
			i = $scope.pages -5;
		}
		for ( ; i < ((page + 2) > $scope.pages ? $scope.pages : (page + 2)) ; i++) {
			newpageList.push(i + 1);
		}
		$scope.pageList = newpageList;
	}
	//选中页
	$scope.teselectPage = function (page) {
		$scope.selPage = page;     //设置当前页
		page=page
		$scope.isActivePage(page);//选中样式
		var id=null;
		templateListController($scope,$http,id,page);//发起请求
	};
	//设置当前选中页样式
	$scope.isActivePage = function (page) {
		return $scope.selPage == page;
	};
	//上一页
	$scope.tePrevious = function () {
		if($scope.selPage  == 1){
			return;
		}else{
			$scope.teselectPage($scope.selPage - 1);
		}
	}
	//下一页
	$scope.teNext = function () {
		if($scope.selPage == $scope.pages){
			return;
		}else{
			
			$scope.teselectPage($scope.selPage -0+ 1);
		}
	};
	//跳转
	$scope.tepjump = function () {
		var page=$scope.jumppage;
		if($scope.jumppage > $scope.pages||$scope.jumppage <= 0){
			return;
		}else{
			$scope.teselectPage(page);
		}
	};
}
//应用分页
function changeorderpage($scope,$http,id){
	$scope.selPage=1;
	var page=$scope.nowpage;
	$scope.newPages = $scope.pages > 5 ? 5 : $scope.pages; //按钮数超过5个，按5个显示
	$scope.pageList = [];                          //页码集合	
	//分页要repeat的数组
	for (var i = 0; i < $scope.newPages; i++) { //处理按钮数少于5个的页码集合
		$scope.pageList.push(i + 1);
	}
	
	//当前页不能小于1，大于最大
	if (page < 1 || page > $scope.pages) return;
	
	//最多显示分页数5
	if (page > 2) {
	//因为只显示5个页码，大于2页开始分页转换
		var newpageList = [];
		var i = (page - 3);
		if($scope.pages - i < 5){
			i = $scope.pages -5;
		}
		for ( ; i < ((page + 2) > $scope.pages ? $scope.pages : (page + 2)) ; i++) {
			newpageList.push(i + 1);
		}
		$scope.pageList = newpageList;
	}
	//选中页
	$scope.appselectPage = function (page) {
		$scope.selPage = page;     //设置当前页
		$scope.isActivePage(page);//选中样式
		appListBynameController($scope,$http,page);//发起请求
	};
	//设置当前选中页样式
	$scope.isActivePage = function (page) {
		return $scope.selPage == page;
	};
	//上一页
	$scope.appPrevious = function () {
		if($scope.selPage  == 1){
			return;
		}else{
			$scope.appselectPage($scope.selPage - 1);
		}
	}
	//下一页
	$scope.appNext = function () {
		if($scope.selPage == $scope.pages){
			return;
		}else{
			
			$scope.appselectPage($scope.selPage -0+ 1);
		}
	};
	//跳转
	$scope.appjump = function () {
		var page=$scope.jumppage;
		if($scope.jumppage > $scope.pages||$scope.jumppage <= 0){
			return;
		}else{
			$scope.appselectPage(page);
		}
	};
}

