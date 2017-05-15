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
	templateListController($scope,$http,id);
    $scope.ListOfGoods=true;
    $scope.ListOfGoodsTab=true;
   //列表控制
    //产品列表
    $scope.nListOfGoods = function(id){
    	templateListController($scope,$http,id);
    };
    //应用列表
    $scope.nmyApplicationList = function(){
    	applictionListController($scope,$http);
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
        $('#myTab a[href="#theBillListTab"]').tab('show')
    };
   //详情弹出控制
    //产品详情
    $scope.showGoodsDetails=function(id){
    	$http({
            method: 'POST',
            url: 'http://192.168.6.16:8080/passService/showTempliteList',
            contentType: "application/json",
            params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":''},
        }).then(function successCallback(response) {
        	$scope.templates=response.data;
            }, function errorCallback(response) {
        }); 
    	$scope.Details=false;
        $scope.DetailsOfGoodsTab =true;
        $scope.myApplicationList=false;
        $('#myTab a[href="#DetailsOfGoodsTab"]').tab('show')
    };
    $scope.BuyGoodsDetails=function(id){
    	$http({
            method: 'POST',
            url: 'http://192.168.6.16:8080/passService/showTempliteList',
            contentType: "application/json",
            params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":''},
        }).then(function successCallback(response) {
        	$scope.templates=response.data;
        	createTree2();
            }, function errorCallback(response) {
        }); 
    	$scope.Details=true;
    	$scope.DetailsOfGoodsTab =true;
        $scope.myApplicationList=false;
        $('#myTab a[href="#DetailsOfGoodsTab"]').tab('show')
    };
    $scope.closeGoodsDetails=function(){
        $scope.DetailsOfGoodsTab =false;
        $('#myTab a[href="#ListOfGoodsTab"]').tab('show')
    };
    //应用详情
    $scope.showApplicationDetails=function(orderId){
    	$http({
            method: 'GET',
            url: 'http://192.168.6.16:8080/passService/showApplicationDetails',
            contentType: "application/json",
            params:{"orderId":orderId},
        }).then(function successCallback(response) {
        	$scope.applicationDetails=response.data;
        	$http({
                method: 'GET',
                url: 'http://192.168.6.16:8080/passService/getInstanceAndOrgShip',
                contentType: "application/json",
                params:{"orderId":orderId},
            }).then(function successCallback(response) {
            	$scope.Orglist=response.data;
                }, function errorCallback(response) {
            }); 
            }, function errorCallback(response) {
        }); 
        $scope.myApplicationDetails=true;
        $scope.DetailsOfGoodsTab =false;
        $scope.ListOfGoods=false;
       // $scope.myApplicationList=false;
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
            url: 'http://192.168.6.16:8080/passService/showApplicationDetails',
            contentType: "application/json",
            params:{"orderId":orderId},
        }).then(function successCallback(response) {
        	$scope.applicationDetails=response.data;
        	$http({
                method: 'GET',
                url: 'http://192.168.6.16:8080/passService/getInstanceAndOrgShip',
                contentType: "application/json",
                params:{"orderId":orderId},
            }).then(function successCallback(response) {
            	$scope.Orglist=response.data;
            	console.log($scope.Orglist);
            	createTree1($scope.Orglist);
                }, function errorCallback(response) {
            }); 
            }, function errorCallback(response) {
        }); 
    	transmitOrderId(orderId,$scope,$http);
        $scope.myApplicationUsers=true;
        $scope.DetailsOfGoodsTab =false;
        $scope.ListOfGoods=false;
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
    ordertoscope($scope,$http);
});
//应用列表接口调用
function applictionListController($scope,$http){
	
	$http({
        method: 'POST',
        url: 'http://192.168.6.16:8080/passService/showApplicationList',
        contentType: "application/json",
        params:{"page":1,"tenantId":1,"instanceName":$scope.instanceName,"templateCategory":$scope.templateCategory,"counm":''},
    }).then(function successCallback(response) {
    	for(i=0;i<response.data.length;i++){
    		var newTime = new Date(response.data[i].crateDate);
    		response.data[i].crateDate = newTime.Format("yyyy-MM-dd"); 
    	}
    	$scope.applicationList=response.data;
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
//产品列表接口调用
function templateListController($scope,$http,id){
	
	$http({
        method: 'POST',
        url: 'http://192.168.6.16:8080/passService/showTempliteList',
        contentType: "application/json",
        params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":''},
    }).then(function successCallback(response) {
    	$scope.templateList=response.data;
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
