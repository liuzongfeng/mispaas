angular.module('appexample', ['ngRoute', 'auth']).controller(
		'appexample',

		function($route,$http, auth,$rootScope,$scope) {
			
			$scope.appTab= true;
			$scope.appexample_title=true;
			$scope.appExample = true;
			$scope.divPage=true;
			$("#activeExample").attr("class","active");
			console.log("111111111111111111111111111111");
			
			//应用实例
			$scope.appExample_fn = function(){
                $("#activeExample").attr("class","active");
                $("#activeCreateEx").attr("class","");
                $("#activeEditEx").attr("class","");
				$scope.editExample=false;
				
				$scope.appexample_title=true;
				$scope.appExample = true;
				$scope.divPage=true;
			}
			//创建实例
			$scope.createExample_fn = function(){
				alert("创建实例");
                $("#activeExample").attr("class","");
                $("#activeCreateEx").attr("class","active");
                $("#activeEditEx").attr("class","");
               // $scope.editAppexample_title = false;
				$scope.appExample = false;
				$scope.divPage=false;
				
				$scope.createAppexample_title = true;
				$scope.editExample=true;
                $scope.editAppexample_title = false;
                if(!$scope.editAppexample_title){
                    $("#activeEditEx").css("display","none");
				}
				if($scope.createAppexample_title){
                    $("#activeCreateEx").css("display","");
				}
			}
			//取消编辑实例
			$scope.cancleExample_fn = function(){
				alert("取消编辑实例");
                $("#activeExample").attr("class","active");
				$scope.editAppexample_title = false;
				$scope.createAppexample_title = false;
				$scope.editExample=false;
				$scope.appexample_title=true;
				$scope.appExample = true;
				$scope.divPage=true;
				
			}
            $scope.closeEditExample_fn = function () {
                alert("取消编辑实例");
                $("#activeExample").attr("class","active");
                $scope.editAppexample_title = false;
                $scope.createAppexample_title = false;
                $scope.editExample=false;
                $scope.appexample_title=true;
                $scope.appExample = true;
                $scope.divPage=true;
            }
			//编辑实例
			$scope.editExample_fn = function(){
				alert("编辑实例");
                $("#activeExample").attr("class","");
                $("#activeCreateEx").attr("class","");
                $("#activeEditEx").attr("class","active");
				$scope.appExample = false;
               // $scope.createAppexample_title = false;
				$scope.divPage=false;
				
				$scope.editAppexample_title = true;
				$scope.editExample=true;
                $scope.createAppexample_title = false;
                if(!$scope.createAppexample_title){
                    $("#activeCreateEx").css("display","none");
				}
				if($scope.editAppexample_title){
                    $("#activeEditEx").css("display","");
				}
			}
			
			//
			//查询模实例
			$scope.searchExample_fn = function(){
				alert("发起查询请求");
			}
			//
			//删除实例
			$scope.delExample_fn = function(){
				alert("删除实例");
				
				
				swal({   
					title: "确定删除?",   
					text: "",   
					type: "warning",   
					showCancelButton: true,   
					confirmButtonColor: "#DD6B55",
					cancelButtonText: "取消",//取消按钮文本
					confirmButtonText: "是的，确定删除！",//确定按钮上面的文档
					closeOnConfirm: false 
				}, 
				function(){
					
					alert("请求后端进行删除");
					swal("删除成功!", "已被删除!", "success");
					/*if("" != projectId){
						projectAjaxBO.deleteProjectBByNumber(projectId,showManagerProject);
					}*/
				});
		}
			//返回后的函数
		/*function showManagerProject(result){
			if("1" == result){
				swal("删除成功!", "该项目已被删除!", "success");
				setTimeout('tiaozhuanyemian()',1000);
			}else if("0" == result){
				swal("删除失败!", "请联系管理员!", "error");
			}else if("2" == result){
				swal("系统异常!", "请联系管理员!", "error"); 
			}else{
				swal("系统异常!", "请联系管理员!", "error");
			}
		}*/
			
			
			
			$(function() {
				//复选框的反选
			    $('#checkbox_1').click(function(){
			    	
			    	$('.checkbox_1').each(function () {  
			    		$(this).prop("checked", !$(this).prop("checked"));  
			    	});  
			    });



			});
			
		});