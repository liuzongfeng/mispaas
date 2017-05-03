angular.module('apptemplate', ['ngRoute', 'auth']).controller(
		'apptemplate',

		function($route,$http, auth,$rootScope,$scope) {
			
			$scope.appTab= true;
			$scope.apptemplate1_title = true;
			$scope.apptemplate1=true;
			$scope.divPage=true;
			//$scope.editTemplate= false;
			$("#activeTemplate").attr("class","active");
			
			console.log("2222222222222222");
			//应用模板
			$scope.appTemplate_fn = function(){
				alert("应用模板")
                $("#activeTemplate").attr("class","active");
				$("#activeEdit").attr("class","");

				$scope.editTemplate = false;
				//$scope.editTemplate_title=false;
				
				$scope.appTab= true;
				$scope.apptemplate1_title = true;
				$scope.apptemplate1=true;
				$scope.divPage=true;
			}
			//编辑模板
			$scope.editTemplate_fn = function(){
				alert("编辑模板");
                $("#activeTemplate").attr("class","");
                $("#activeEdit").attr("class","active");
				$scope.apptemplate1=false;
				$scope.divPage=false;
				$scope.editTemplate = true;
				$scope.editTemplate_title=true;
			}
            $scope.closeEditTemplate_fn = function () {
                $scope.editTemplate_title=false;
                $scope.editTemplate = false;
                //$scope.editTemplate_title=false;
                $("#activeTemplate").attr("class","active");
                $scope.appTab= true;
                $scope.apptemplate1_title = true;
                $scope.apptemplate1=true;
                $scope.divPage=true;
            }
			//取消编辑
			$scope.cancleTemplate_fn = function(){
				$scope.editTemplate_title=false;
				$scope.editTemplate = false;
				//$scope.editTemplate_title=false;
                $("#activeTemplate").attr("class","active");
				$scope.appTab= true;
				$scope.apptemplate1_title = true;
				$scope.apptemplate1=true;
				$scope.divPage=true;
			}
			//提交编辑模板
			$scope.submitTemplate_fn = function(){
				
				alert("发起提交请求");
			}
			
			//删除模板
			$scope.delTemplate_fn = function(){
				alert("删除模板");
				
				
				swal({   
					title: "操作提示",   
					text: "确定删除?",   
					type: "warning",   
					showCancelButton: true,   
					confirmButtonColor: "#DD6B55",
					cancelButtonText: "取消",//取消按钮文本
					confirmButtonText: "是的，确定删除！",//确定按钮上面的文档
					closeOnConfirm: false 
				}, 
				function(){
					
					alert("请求后端进行删除");
					swal("删除成功!", "该项目已被删除!", "success");
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
			
			//导出模板
			$scope.checkOutTemplate_fn = function(){
				alert("导出模板");
				
				swal({   
					title: "请先选中模板",   
					text: "",   
					type: "warning",   
					showCancelButton: false,   
					closeOnConfirm: false 
				});
			}
			
			
			
			//查询模板
			$scope.searchTemplate_fn = function(){
				alert("发起查询请求");
			}
			
			
			$(function() {
				//模态框隐藏
				$('#myModal').modal('hide');
				//关闭模态框出发事件
				$('#myModal').on('hide.bs.modal',
						    function() {
						        //alert('嘿，我听说您喜欢模态框...');
				});
				
				//复选框的反选
			    $('#checkbox_1').click(function(){
			    	
			    	$('.checkbox_1').each(function () {  
			    		$(this).prop("checked", !$(this).prop("checked"));  
			    	});  
			    });
			});
		});












