/**
 * Created by Administrator on 2017/4/20.
 */
var app = angular.module('listApp', []);
app.controller('ListCtrl', function($scope,$http) {
	//获取用户信息
	$scope.tenantId=null;
	$http({
        method: 'GET',
        url:tenantSelfinterfaces.Var_geUserDetails,
        contentType: "application/json",        
    }).then(function successCallback(response) {
    	$scope.tenantId=response.data.name;
    	$scope.userName=response.data.username;
    });
	$http({
        method: 'GET',
        url: tenantSelfinterfaces.Var_getTemplateCategorys,
	}).then(function successCallback(response) {
    	$scope.templateCategoryList=response.data;
        }, function errorCallback(response) {
    }); 
	var id= null;
	var page=null;
	templateListController($scope,$http,id,page);
	ordertoscope($scope,$http,id);
    $scope.ListOfGoods=true;
    $scope.ListOfGoodsTab=true;
   
   //列表控制
    //产品列表
    $scope.nListOfGoods = function(){
    	$scope.selPage=1;
    	$("#nListOfGoods").attr("class","open");
    	$("#nmyApplicationList").attr("class","");
		var lis=$("#chanpinfenye").children();
		for(i=0;i<lis.length;i++){
			lis.eq(i).children().css("backgroundColor","white");
			
		}
		lis.eq(1).children().css("backgroundColor","#458fe9");
    	var page=null;
    	templateListController($scope,$http,id,page);
    	$("#tab_51_1").attr("class","tab-pane fade in active");
    	$("#ListOfGoodsTab").attr("class","active");
    	var trs = $("tr[class=picon-model]");
    	var trs2= $("tr[class=picon-model2]");
    	for(i=0;i<trs.length;i++){
    		$(trs[i]).attr("class","");
    	}
    	for(i=0;i<trs2.length;i++){
    		
    		$(trs2[i]).attr("class","");
    	}
    };
    //应用列表
    $scope.nmyApplicationList = function(){
    	$scope.selPage=1;
    	
    	$("#nListOfGoods").attr("id");
    	$scope.useapptemplateCategory='';
    	var page=null;
    	appListBynameController($scope,$http,page);
    	$("#nListOfGoods").attr("class","");
    	$("#nmyApplicationList").attr("class","open");
		var lis=$("#appfenye").children();
		for(i=0;i<lis.length;i++){
			lis.eq(i).children().css("backgroundColor","white");
		}
		lis.eq(3).children().css("backgroundColor","#458fe9");
		$("#tab_51_3").attr("class","tab-pane fade in active");
		$("#tab_51_4").attr("class","tab-pane fade");
		$("#tab_51_5").attr("class","tab-pane fade");
		$scope.myApplicationDetails=false;
		$scope.myApplicationUsers=false;
		$("#myApplicationListTab").attr("class","active");
		var trs = $("tr[class=picon-model]");
    	var trs2= $("tr[class=picon-model2]");
    	for(i=0;i<trs.length;i++){
    		$(trs[i]).attr("class","");
    	}
    	for(i=0;i<trs2.length;i++){
    		
    		$(trs2[i]).attr("class","");
    	}
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
            params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":$scope.cunm},
        }).then(function successCallback(response) {
        	$scope.templates=response.data.resultObj;
            }, function errorCallback(response) {
        });
    	$scope.Details=false;
        $scope.DetailsOfGoodsTab=true;
        var lis=$("li");
       for(i=0;i<lis.length;i++){
    	   $(lis[i]).removeClass("active");
       }
        $("#DetailsOfGoodsTab").toggleClass("active");
        $("#tab_51_2").attr("class","tab-pane fade in active");
        $("#tab_51_1").attr("class","tab-pane fade");
    };
    //购买产品
    $scope.BuyGoodsDetails=function(id){
    	$scope.checkuserList=[];
    	$http({
            method: 'POST',
            url: tenantSelfinterfaces.Var_showTempliteList,
            contentType: "application/json",
            params:{"page":1,"id":id,"templateId":$scope.templateId,"templateCategory":$scope.templateCategory2,"counm":$scope.cunm},
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
        	$scope.newtenantId=response.data[0].tenantList[0].id;
        	$("#userMenuStatuetitle").html(response.data[0].tenantList[0].name);
            }, function errorCallback(response) {
        }); 
    	$scope.Details=true;
    	$scope.DetailsOfGoodsTab =true;
        $scope.myApplicationList=false;
        var lis=$("li");
        for(i=0;i<lis.length;i++){
     	   $(lis[i]).removeClass("active");
        }
         $("#DetailsOfGoodsTab").attr("class","active");
         $("#tab_51_2").attr("class","tab-pane fade in active");
         $("#tab_51_1").attr("class","tab-pane fade");
        ordertoscope($scope,$http,id);
        
    };
    $scope.closeGoodsDetails=function(){
        $scope.DetailsOfGoodsTab =false;
        $("#ListOfGoodsTab").attr("class","active");
        $("#tab_51_1").attr("class","tab-pane fade in active");
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
                url: tenantSelfinterfaces.Var_getInstanceAndOrgShip_new,
                contentType: "application/json",
                params:{"orderId":orderId},
            }).then(function successCallback(response) {
            	$scope.Orglist=response.data;
                }, function errorCallback(response) {
            }); 
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_gettenantList,
                contentType: "application/json",
                params:{"userurl":tenantSelfinterfaces.Var_othergetuser+$scope.tenantId,"tenanturl":tenantSelfinterfaces.Var_othergettentant},
            }).then(function successCallback(response) {
            	$scope.tetantList=response.data[0];
            	for(i=0;i<response.data[0].tenantList.length;i++){
            		if(response.data[0].tenantList[i].id==$scope.applicationDetails.tenantId){
            			$scope.suoshuzuhu=response.data[0].tenantList[i].name;
            		}
            	}
            	$scope.newtenantId=response.data[0].tenantList[0].id;
            	$("#userMenuStatuetitle").html(response.data[0].tenantList[0].name);
                }, function errorCallback(response) {
            }); 
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getOrgtree,
                params:{"geturl":tenantSelfinterfaces.Var_othergetOrgtree},
            }).then(function successCallback(response) {
            	$scope.chOrglist=response.data;
            	createTree3($scope.Orglist,$scope.chOrglist,$scope.applicationDetails.paasSubservices[0].id);
            	var subs=$scope.applicationDetails.paasSubservices;
            		var id=subs[0].id;
            	$("#"+id).parent().attr("class","picon-model3");
                }, function errorCallback(response) {
            }); 
        	$http({
    	        method: 'GET',
    	        url: tenantSelfinterfaces.Var_getuserAndOrgShip_new,
    	        params:{"orderId":orderId},
    	        datatype:"json",
    	    }).then(function successCallback(response) {
    	    	$scope.userwithOrglist=response.data;
    	    	var finalluserwithOrglist=[];
    	    	var subs=$scope.applicationDetails.paasSubservices;
            	var id=subs[0].id;
    	    	var j=0;
    	    	for(i=0;i<response.data.length;i++){
    	    		if(response.data[i].subserviceId==id){
    	    			finalluserwithOrglist[j]=response.data[i];
    	    				j=j+1;
    	    		}
    	    	}
    	    	$scope.finalluserwithOrglist=finalluserwithOrglist;
    	        }, function errorCallback(response) {
    	    });
            }, function errorCallback(response) {
        });
        $scope.myApplicationDetails=true;
        $scope.DetailsOfGoodsTab =false;
        $scope.ListOfGoods=false;
        $("#myApplicationDetails").attr("class","active");
        $("#myApplicationListTab").attr("class","");
        $("#tab_51_5").attr("class","tab-pane fade in active");
        $("#tab_51_3").attr("class","tab-pane fade");
    };
    $scope.closeApplicationDetails=function(){
        $scope.myApplicationDetails=false;
        
        $("#myApplicationListTab").attr("class","active");
        $("#tab_51_5").attr("class","tab-pane fade");
        $("#tab_51_3").attr("class","tab-pane fade in active");
    };
    //修改用户群组
    $scope.showApplicationUetails=function(orderId){
    	 $scope.checkuserList=[];
    	$http({
            method: 'GET',
            url: tenantSelfinterfaces.Var_showApplicationDetails,
            contentType: "application/json",
            params:{"orderId":orderId},
        }).then(function successCallback(response) {
        	$scope.chapplicationDetails=response.data;
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getInstanceAndOrgShip_new,
                contentType: "application/json",
                params:{"orderId":orderId},
            }).then(function successCallback(response) {
            	$scope.Orglist=response.data;
                }, function errorCallback(response) {
            });
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_gettenantList,
                contentType: "application/json",
                params:{"userurl":tenantSelfinterfaces.Var_othergetuser+$scope.tenantId,"tenanturl":tenantSelfinterfaces.Var_othergettentant},
            }).then(function successCallback(response) {
            	$scope.tetantList=response.data[0];
            	for(i=0;i<response.data[0].tenantList.length;i++){
            		if(response.data[0].tenantList[i].id==$scope.chapplicationDetails.tenantId){
            			$scope.suoshuzuhu=response.data[0].tenantList[i].name;
            		}
            	}
                }, function errorCallback(response) {
            }); 
        	$http({
                method: 'GET',
                url: tenantSelfinterfaces.Var_getOrgtree,
                params:{"geturl":tenantSelfinterfaces.Var_othergetOrgtree},
            }).then(function successCallback(response) {
            	$scope.chOrglist=response.data;
            	createTree1($scope.Orglist,$scope.chOrglist,$scope.chapplicationDetails.paasSubservices[0].id);
            	var subs=$scope.chapplicationDetails.paasSubservices;
            	var id=subs[0].id;
            	$("#"+id+"i").parent().attr("class","picon-model2");
            }, function errorCallback(response) {
            }); 
        	$http({
    	        method: 'GET',
    	        url: tenantSelfinterfaces.Var_getuserAndOrgShip_new,
    	        params:{"orderId":orderId},
    	        datatype:"json",
    	    }).then(function successCallback(response) {
    	    	$scope.userwithOrglist=response.data;
    	    	var finalluserwithOrglist=[];
    	    	var subs=$scope.chapplicationDetails.paasSubservices;
            	var id=subs[0].id;
    	    	var j=0;
    	    	for(i=0;i<response.data.length;i++){
    	    		if(response.data[i].subserviceId==id){
    	    			finalluserwithOrglist[j]=response.data[i];
    	    				j=j+1;
    	    		}
    	    	}
    	    	$scope.finalluserwithOrglist=finalluserwithOrglist;
    	        }, function errorCallback(response) {
    	    });
            }, function errorCallback(response) {
        });
    	transmitOrderId(orderId,$scope,$http);
        $scope.myApplicationUsers=true;
        $scope.DetailsOfGoodsTab =false;
        $scope.ListOfGoods=false;
        $("#myApplicationListTab").attr("class","");
        $("#myApplicationUsers").attr("class","active");
        $("#tab_51_4").attr("class","tab-pane fade in active");
        $("#tab_51_3").attr("class","tab-pane fade");
    };
   
    $scope.closemyApplicationUsers=function(){
        $scope.myApplicationUsers=false;
        $("#myApplicationListTab").attr("class","active");
        $("#tab_51_3").attr("class","tab-pane fade in active");
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
  //登出
    $scope.logout=function()
    {
    	window.location.href= "http://192.168.6.16:8080/logout";
    }
  //修改每页行数
	$("#everyPage1").change(function(){
		var cunm = $("#everyPage1").attr("value");
		$scope.cunm1 = cunm;
		templateListController($scope,$http,id,page);//发起请求
		});
	$("#everyPage2").change(function(){
		var cunm = $("#everyPage2").attr("value");
		$scope.cunm2 = cunm;
		appListBynameController($scope,$http,page)//发起请求
		});
});
//产品列表接口调用
function templateListController($scope,$http,id,page){
	if($scope.usetemplateCategory=="chanpinguolv"){
		$scope.usetemplateCategory='';
	}
	$http({
        method: 'POST',
        url: tenantSelfinterfaces.Var_showTempliteList,
        
        datatype:"JSONP",
        params:{"page":page,"id":id,"productName":$scope.productName,"templateCategory":$scope.usetemplateCategory,"counm":$scope.cunm1},
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
	if($scope.useapptemplateCategory=="yingyongguolv"||$scope.useapptemplateCategory==null){
		$scope.useapptemplateCategory='';
	}
	var reurl = null;
	if(($scope.instanceName==null||$scope.instanceName=='')&&($scope.useapptemplateCategory==null||$scope.useapptemplateCategory=='')){
		
			reurl=tenantSelfinterfaces.Var_showApplicationList;
	}else{
		reurl=tenantSelfinterfaces.Var_showApplicationListByInstanceName;
	}
	$http({
        method: 'GET',
        url: tenantSelfinterfaces.Var_gettenantList,
        contentType: "application/json",
        params:{"userurl":tenantSelfinterfaces.Var_othergetuser+$scope.tenantId,"tenanturl":tenantSelfinterfaces.Var_othergettentant},
    }).then(function successCallback(response) {
    	$scope.tetantList=response.data[0];
    	$http({
            method: 'POST',
            url: reurl,
            contentType: "application/json",
            params:{"page":page,"instanceName":$scope.instanceName,"templateCategory":$scope.useapptemplateCategory,"counm":$scope.cunm2},
            data:$scope.tetantList
    	}).then(function successCallback(response) {
        	for(i=0;i<response.data.resultObj.length;i++){
        		var newTime = new Date(response.data.resultObj[i].crateDate);
        		response.data.resultObj[i].crateDate = newTime.Format("yyyy-MM-dd hh:mm"); 
        	}
        	$scope.pagenum =response.data.pageStr.split(",");
        	$scope.pageinfo = response.data;
        	$scope.applicationList=response.data.resultObj;
        	$scope.pages=response.data.allpage;
        	$scope.totalSize=response.data.begin;
        	$scope.nowpage=response.data.pagenum;
            }, function errorCallback(response) {
        }); 
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

