/**
 * Created by Administrator on 2017/4/20.
 */
/*window.onload = function() {
    //点击菜单页面控制
    $("#huiyishishouquan").click(function() {
        $("#ListOfGoods").removeAttr("hidden", "false");
        $("#shouquanguanliebiao").attr("hidden", "true");
    });
}*/
var app = angular.module('listApp', []);
app.controller('ListCtrl', function($scope,$http) {
	var id= null;
	var page=null;
	templateListController($scope,$http,id,page);
	ordertoscope($scope,$http,id);
    $scope.ListOfGoods=true;
    $scope.ListOfGoodsTab=true;
   
   //列表控制
    //产品列表
    $scope.nListOfGoods = function(){
    	$scope.chanpincaidan1=false;
		$scope.chanpincaidan2=true;
		$scope.appcolor="#458fe9";
		$scope.yingyongcaidan1=true;
		$scope.yingyongcaidan2=false;
		$scope.inscolor="#8e9094";
		var lis=$("#chanpinfenye").children();
		for(i=0;i<lis.length;i++){
			lis.eq(i).children().css("backgroundColor","white");
			
		}
		lis.eq(1).children().css("backgroundColor","#458fe9");
    	var page=null;
    	templateListController($scope,$http,id,page);
    	
    	  $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
              $(e.target).css("border-top","2px solid #3f8dce");
              // 获取前一个激活的标签页的名称
            $(e.relatedTarget).css("border-top","1px solid #dcdedd");
          });
    };
    //应用列表
    $scope.nmyApplicationList = function(){
    	$scope.yingyongcaidan1=false;
		$scope.yingyongcaidan2=true;
		$scope.chanpincaidan1=true;
		$scope.chanpincaidan2=false;
		$scope.inscolor="#458fe9";
		$scope.appcolor="#8e9094";
    	var page=null;
    	appListBynameController($scope,$http,page);
		var lis=$("#appfenye").children();
		for(i=0;i<lis.length;i++){
			lis.eq(i).children().css("backgroundColor","white");
		}
		lis.eq(3).children().css("backgroundColor","#458fe9");	
		
		  $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
	            $(e.target).css("border-top","2px solid #3f8dce");
	            // 获取前一个激活的标签页的名称
	          $(e.relatedTarget).css("border-top","1px solid #dcdedd");
	        });
	    };
    //账单列表
    $scope.ntheBillList = function(){
        $scope.theBillListTab =true;
        $scope.myApplicationListTab=false;
        $scope.ListOfGoodsTab=false;
        $scope.theBillList =true;
        $scope.ListOfGoods=false;
        $scope.myApplicationList=false;
        $scope.DetailsOfGoodsTab =false;
        $scope.myApplicationDetails=false;
        $scope.myApplicationUsers=false;
        
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(e.target).css("border-top","2px solid #3f8dce");
            // 获取前一个激活的标签页的名称
          $(e.relatedTarget).css("border-top","1px solid #dcdedd");
        });
        $('#myTab a[href="#theBillListTab"]').tab('show')
    };
   //详情弹出控制
    //产品详情
    $scope.showGoodsDetails=function(id){
    	$http({
            method: 'POST',
            url: tenantSelfinterfaces.Var_showTempliteList,
            contentType: "application/json",
            params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":''},
        }).then(function successCallback(response) {
        	$scope.templates=response.data.resultObj;
            }, function errorCallback(response) {
        });
    	$scope.Details=false;
        $scope.DetailsOfGoodsTab =true;
        $scope.myApplicationList=false;
        
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(e.target).css("border-top","2px solid #3f8dce");
            // 获取前一个激活的标签页的名称
          $(e.relatedTarget).css("border-top","1px solid #dcdedd");
        });
        $('#myTab a[href="#DetailsOfGoodsTab"]').tab('show')
    };
    //购买产品
    $scope.BuyGoodsDetails=function(id){
    	$http({
            method: 'POST',
            url: tenantSelfinterfaces.Var_showTempliteList,
            contentType: "application/json",
            params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":''},
        }).then(function successCallback(response) {
        	$scope.templates=response.data.resultObj;
        	/*createTree3();*/
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getOrgtree,
                params:{"geturl":tenantSelfinterfaces.Var_othergetOrgtree},
            }).then(function successCallback(response) {
            	$scope.chOrglist=response.data;
            	createTree2($scope.chOrglist);
                }, function errorCallback(response) {
            }); 
            }, function errorCallback(response) {
        });
    	$http({
            method: 'GET',
            url: tenantSelfinterfaces.Var_gettenantList,
            contentType: "application/json",
            params:{"userurl":tenantSelfinterfaces.Var_othergetuser+$scope.tenantId,"tenanturl":tenantSelfinterfaces.Var_othergettentant},
        }).then(function successCallback(response) {
        	$scope.tetantList=response.data[0];
            }, function errorCallback(response) {
        }); 
    	$scope.Details=true;
    	$scope.DetailsOfGoodsTab =true;
        $scope.myApplicationList=false;
        
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(e.target).css("border-top","2px solid #3f8dce");
            // 获取前一个激活的标签页的名称
          $(e.relatedTarget).css("border-top","1px solid #dcdedd");
        });
        $('#myTab a[href="#DetailsOfGoodsTab"]').tab('show');
        ordertoscope($scope,$http,id);
    };
    $scope.closeGoodsDetails=function(){
        $scope.DetailsOfGoodsTab =false;
        $('#myTab a[href="#ListOfGoodsTab"]').tab('show')
    };
    //应用详情
    $scope.showApplicationDetails=function(orderId){
    	$http({
            method: 'GET',
            url: tenantSelfinterfaces.Var_showApplicationDetails,
            contentType: "application/json",
            params:{"orderId":orderId},
        }).then(function successCallback(response) {
        	$scope.applicationDetails=response.data;
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getInstanceAndOrgShip,
                contentType: "application/json",
                params:{"orderId":orderId},
            }).then(function successCallback(response) {
            	$scope.Orglist=response.data;
                }, function errorCallback(response) {
            }); 
        	
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getOrgtree,
                params:{"geturl":tenantSelfinterfaces.Var_othergetOrgtree},
            }).then(function successCallback(response) {
            	$scope.chOrglist=response.data;
            	createTree3($scope.Orglist,$scope.chOrglist);
                }, function errorCallback(response) {
            }); 
        	
            }, function errorCallback(response) {
        }); 
        $scope.myApplicationDetails=true;
        $scope.DetailsOfGoodsTab =false;
        $scope.ListOfGoods=false;
       // $scope.myApplicationList=false;
        
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(e.target).css("border-top","2px solid #3f8dce");
            // 获取前一个激活的标签页的名称
          $(e.relatedTarget).css("border-top","1px solid #dcdedd");
        });
        $('#myTab a[href="#myApplicationDetails"]').tab('show')
    };
    $scope.closeApplicationDetails=function(){
        $scope.myApplicationDetails=false;
        $('#myTab a[href="#myApplicationListTab"]').tab('show')
    };
    //修改用户群组
    $scope.showApplicationUetails=function(orderId){
    	$http({
            method: 'GET',
            url: tenantSelfinterfaces.Var_showApplicationDetails,
            contentType: "application/json",
            params:{"orderId":orderId},
        }).then(function successCallback(response) {
        	$scope.chapplicationDetails=response.data;
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getInstanceAndOrgShip,
                contentType: "application/json",
                params:{"orderId":orderId},
            }).then(function successCallback(response) {
            	$scope.Orglist=response.data;
                }, function errorCallback(response) {
            });
        	
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getOrgtree,
                params:{"geturl":tenantSelfinterfaces.Var_othergetOrgtree},
            }).then(function successCallback(response) {
            	$scope.chOrglist=response.data;
            	createTree1($scope.Orglist,$scope.chOrglist);
                }, function errorCallback(response) {
            }); 
        	
            }, function errorCallback(response) {
        }); 
    	transmitOrderId(orderId,$scope,$http);
        $scope.myApplicationUsers=true;
        $scope.DetailsOfGoodsTab =false;
        $scope.ListOfGoods=false;
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(e.target).css("border-top","2px solid #3f8dce");
            // 获取前一个激活的标签页的名称
          $(e.relatedTarget).css("border-top","1px solid #dcdedd");
        });
        $('#myTab a[href="#myApplicationUsers"]').tab('show')
    };
    $scope.closemyApplicationUsers=function(){
        $scope.myApplicationUsers=false;
        $('#myTab a[href="#myApplicationListTab"]').tab('show')
    };
    //购物车
    $scope.showShoppingCar=function(){
        $('#ProductDetails').modal('hide');
        $('#ApplicationDetails').modal('hide');
        $('#ShoppingCarModal').modal('show');
    };

    //日期插件
    $('#startDate').datetimepicker({
        todayhighlight: true,
        format: "yyyy-mm-dd",
        autoclose: true,
        minView: "month",
        language: 'zh-CN',
        pickdate:true
    });
    $('#startDate').focus(function(){
        $('#startDate').datetimepicker('show');
    });
    
});
//产品列表接口调用
function templateListController($scope,$http,id,page){
	$http({
        method: 'GET',
        url: tenantSelfinterfaces.Var_getTemplateCategorys,
	}).then(function successCallback(response) {
    	$scope.templateCategoryList=response.data;
        }, function errorCallback(response) {
    }); 
	$http({
        method: 'POST',
        url: tenantSelfinterfaces.Var_showTempliteList,
        
        datatype:"JSONP",
        params:{"page":page,"id":id,"productName":$scope.productName,"templateCategory":$scope.templateCategory2,"counm":''},
    }).then(function successCallback(response) {
    	$scope.tepagenum =response.data.pageStr.split(",");
    	$scope.tepageinfo = response.data;
    	$scope.templateList=response.data.resultObj;
    	$scope.pages=response.data.allpage;
    	$scope.totalSize=response.data.begin;
    	$scope.nowpage=response.data.pagenum;
        }, function errorCallback(response) {
    }); 
	 $scope.ListOfGoodsTab=true;
     $scope.myApplicationListTab=false;
     $scope.theBillListTab =false;
     $scope.ListOfGoods=true;
     $scope.myApplicationList=false;
     $scope.theBillList=false;
     $scope.myApplicationDetails=false;
     $scope.DetailsOfGoodsTab =false;
     $scope.myApplicationUsers=false;
     $('#myTab a[href="#ListOfGoodsTab"]').tab('show');
};
//综合应用列表接口
function appListBynameController($scope,$http,page){
	var reurl = null;
	if(($scope.instanceName==null||$scope.instanceName=='')&&($scope.templateCategory==null||$scope.templateCategory=='')){
		
			reurl=tenantSelfinterfaces.Var_showApplicationList;
	}else{
		reurl=tenantSelfinterfaces.Var_showApplicationListByInstanceName;
	}
	$http({
        method: 'POST',
        url: reurl,
        contentType: "application/json",
        params:{"page":page,"tenantId":$scope.tenantId,"instanceName":$scope.instanceName,"templateCategory":$scope.templateCategory,"counm":''},
    }).then(function successCallback(response) {
    	for(i=0;i<response.data.resultObj.length;i++){
    		var newTime = new Date(response.data.resultObj[i].crateDate);
    		response.data.resultObj[i].crateDate = newTime.Format("yyyy-MM-dd hh:mm"); 
    	}
    	$scope.pagenum =response.data.pageStr.split(",");
    	$scope.pageinfo = response.data;
    	console.log(response.data);
    	$scope.applicationList=response.data.resultObj;
    	$scope.pages=response.data.allpage;
    	$scope.totalSize=response.data.begin;
    	$scope.nowpage=response.data.pagenum;
        }, function errorCallback(response) {
    }); 
	    $scope.myApplicationListTab=true;
        $scope.theBillListTab =false;
        $scope.ListOfGoodsTab=false;
        $scope.myApplicationList=true;
        $scope.ListOfGoods=false;
        $scope.theBillList=false;
        $scope.DetailsOfGoodsTab =false;
        $('#myTab a[href="#myApplicationListTab"]').tab('show')
};
//日期格式控件
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
