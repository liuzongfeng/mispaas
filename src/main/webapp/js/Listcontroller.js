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
app.controller('ListCtrl', function($scope) {
    $scope.ListOfGoods=true;
    $scope.ListOfGoodsTab=true;
   //列表控制
    //产品列表
    $scope.nListOfGoods = function(){
        $scope.ListOfGoodsTab=true;
        $scope.myApplicationListTab=false;
        $scope.theBillListTab =false;
        $scope.ListOfGoods=true;
        $scope.myApplicationList=false;
        $scope.theBillList=false;
        $scope.myApplicationDetails=false;
        $scope.DetailsOfGoodsTab =false;
        $scope.myApplicationUsers=false;
        $('#myTab a[href="#ListOfGoodsTab"]').tab('show')
    };
    //应用列表
    $scope.nmyApplicationList = function(){
        $scope.myApplicationListTab=true;
        $scope.theBillListTab =false;
        $scope.ListOfGoodsTab=false;
        $scope.myApplicationList=true;
        $scope.ListOfGoods=false;
        $scope.theBillList=false;
        $scope.DetailsOfGoodsTab =false;
        $('#myTab a[href="#myApplicationListTab"]').tab('show')
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
    $scope.showGoodsDetails=function(){
        $scope.DetailsOfGoodsTab =true;
        //$scope.ListOfGoods=false;
        $scope.myApplicationList=false;
        $('#myTab a[href="#DetailsOfGoodsTab"]').tab('show')
    };
    $scope.closeGoodsDetails=function(){
        $scope.DetailsOfGoodsTab =false;
        $('#myTab a[href="#ListOfGoodsTab"]').tab('show')
    };
    //应用详情
    $scope.showApplicationDetails=function(){
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
    $scope.showApplicationUetails=function(){
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
});
