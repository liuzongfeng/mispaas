angular.module('appexample', ['ngRoute', 'auth']).controller(
		'appexample',

		function($route,$http, auth,$rootScope,$scope) {
			
			//查询实例
			//发起请求加载模板列表
			selectPage_aa = function (page) {
				//,templateType,templateName
				
				var instanceStatus = $scope.instanceStatus;   //分类模板
				var instanceName = $scope.instanceName;   //名称名称
				$scope.pageSize = 5;                      //临时赋值
				var pageSize = $scope.pageSize;           //每页显示的条数
				$http({
					  method: 'GET',
					  params:{"pageNo":page,"pageSize":pageSize,"instanceStatus":instanceStatus,"instanceName":instanceName},
					  url: 'http://localhost:8080/obtainInstanceList'
					}).then(function successCallback(response) {
						
						$scope.instances = response.data.list;   //要展示的数据
						
						$scope.totalSize = response.data.total;   //共多少条数据
						$scope.pageSize = response.data.pageSize;  //每页条数
						$scope.pages = response.data.pages; //共多少分页数
						
						$scope.newPages = $scope.pages > 5 ? 5 : $scope.pages; //按钮数超过5个，按5个显示
						$scope.pageList = [];                          //页码集合
						$scope.selPage = page;                    //当前页
						
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
						$scope.selectPage = function (page) {
							$scope.selPage = page;     //设置当前页
							$scope.isActivePage(page);//选中样式
							
							selectPage_aa(page); //发起请求
							console.log("选择的页：" + page);
						};
						//设置当前选中页样式
						$scope.isActivePage = function (page) {
							return $scope.selPage == page;
						};
						//上一页
						$scope.Previous = function () {
							if($scope.selPage  == 1){
								alert("第一页");
							}else{
								$scope.selectPage($scope.selPage - 1);
							}
						}
						//下一页
						$scope.Next = function () {
							
							
							if($scope.selPage == $scope.pages){
								alert("最后一页");
							}else{
								
								$scope.selectPage($scope.selPage + 1);
							}
						};
					  }, function errorCallback(response) {
					    // called asynchronously if an error occurs
					    // or server returns response with an error status.
				});
			};
			//
			selectPage_aa(1);//查询列表
			
			/*//加载模板分类
			var loadTemplateCategory = function(){
				
				$http({
					  method: 'GET',
					  url: 'http://localhost:8080/obtainInstanceStatus'
					}).then(function successCallback(response) {
						$scope.instanceStatus = response.data;
						
					}, function errorCallback(response) {
					   
				});
			}
			//发起加载模板分类
			loadTemplateCategory();*/
			
			
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
				var instanceName = $('#instanceName_S').val();
				
				$scope.instanceName = instanceName;
				selectPage_aa(1);//查询列表
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
				//按分类查询
				$("#byInstanceStatus").change(function(){
					var instanceStatus = this.options[this.options.selectedIndex].value;
					$scope.instanceStatus = instanceStatus;
					
					selectPage_aa(1);
				});
				
				//复选框的反选
			    $('#checkbox_1').click(function(){
			    	
			    	$('.checkbox_1').each(function () {  
			    		$(this).prop("checked", !$(this).prop("checked"));  
			    	});
			    	$('#checkbox_1').prop("checked",false);  
			    });



			});
			
		});