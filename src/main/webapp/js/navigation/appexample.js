angular.module('appexample', ['ngRoute', 'auth']).controller(
		'appexample',

		function($route,$http, auth,$rootScope,$scope) {
			
			//查询实例
			//发起请求加载模板列表
			selectPage_aa = function (page) {
				//,templateType,templateName
				
				var instanceStatus = $scope.instanceStatus;   //分类模板
				var instanceName = $scope.instanceName;   //名称名称
				$scope.pageSize = 8;                      //临时赋值
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
			
			//应用实例
			$scope.appExample_fn = function(){
				if($scope.editExample){
					$("#activeExample").attr("class","active");
	                $("#activeCreateEx").attr("class","");
	                $("#activeEditEx").attr("class","");
					$scope.editExample=false;
					
					$scope.appexample_title=true;
					$scope.appExample = true;
					$scope.divPage=true;
				}
                
			}
			//创建实例--title
			$scope.createExample_fn_title = function(){
				$("#activeExample").attr("class","");
                $("#activeCreateEx").attr("class","active");
                $("#activeEditEx").attr("class","");
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
				$("#searchInstanceDetail").hide();   //隐藏提交按钮
				
			}
			
			var autoHeight_fn_s = function(){
				//高度自适应
				//instance高度自适应
                var rightDiv = document.getElementById("rightdiv");
                var ch_t = rightDiv.clientHeight;
                var ifm_left= $("#leftdiv");
                ifm_left.css("height",ch_t+35);
			}
			
			//创建实例
			$scope.createExample_fn = function(){
				
				var tag = window.event.target || window.event.srcElement;
				
				$(tag).parent().prev().prev().children("[type=checkbox]").prop("checked",true);
				
				$scope.editExample_fn();
				
				
				
				$("#activeExample").attr("class","");
                $("#activeCreateEx").attr("class","active");
                $("#activeEditEx").attr("class","");
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
				$("#searchInstanceDetail").hide();   //隐藏提交按钮
				
			}
			
			//取消编辑实例
			$scope.cancleExample_fn = function(){
				
                $("#activeExample").attr("class","active");
				$scope.editAppexample_title = false;
				$scope.createAppexample_title = false;
				$scope.editExample=false;
				$scope.appexample_title=true;
				$scope.appExample = true;
				$scope.divPage=true;
				
			}
            $scope.closeEditExample_fn = function () {
                
                $("#activeExample").attr("class","active");
                $scope.editAppexample_title = false;
                $scope.createAppexample_title = false;
                $scope.editExample=false;
                $scope.appexample_title=true;
                $scope.appExample = true;
                $scope.divPage=true;
            }
            
            $scope.editExample_save_fn = function(){
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
				autoHeight_fn_s();
            }
            
			//编辑实例
			$scope.editExample_fn = function(){
				
				if($('.checkbox_1:checked').length == 0){
					swal("请先选中实例!", "", "warning");
					return;
				}
				if($('.checkbox_1:checked').length > 1){
					swal("不支持多选!", "", "warning");
					$('.checkbox_1').prop("checked",false);
					return;
				}
				
				var aa = 0;
				//判断是否有选中的复选框
				$('.checkbox_1').each(function () {  
		    		var isChecked = $(this).prop("checked"); 
		    		if(isChecked){
		    			
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
						
						
		    			aa = 1;
		    			var instanceId_s = $(this).val();       //获得选中的实例id
		    			for(var i=0 ; i< $scope.instances.length; i++){
		    				var instance = $scope.instances[i];
		    				if(instance.instanceId == instanceId_s){
		    					$scope.Instance_s= instance;
		    					
		    					$scope.subServices = instance.paasTemplate.paasSubservices;
		    				}
		    			}
		    		}
		    		if(aa == 1){//
		    			$('.checkbox_1').prop("checked",false);
					}
		    	});
				
				if(aa == 0){//未选中
					swal("请先选中实例!", "", "warning");
					$scope.closeEditExample_fn();
				}
				autoHeight_fn_s();
			}
			
			
			//提交编辑修改
			$scope.postEditInstance_fn = function(){
				$scope.Instance_s.paasTemplate = $scope.Instance_s.paasTemplate.id;
				var instance_s = $scope.Instance_s;
				
				var req = {
						 method: 'POST',
						 url: 'http://localhost:8080/editInstance',
						 headers: {
						   'Content-Type': "application/json"
						 },
						 data: { instance_s: instance_s}
						}

						$http(req).then(function(response){
							var resultUp = response.data.result;
							if(resultUp == "updateOk"){
								swal("更新实例成功!", "", "success");
								$scope.closeEditExample_fn();
								selectPage_aa(1);//查询列表
								
							}else{
								swal(resultUp, "", "error");
								$scope.closeEditExample_fn();
								selectPage_aa(1);//查询列表
							}
							
						}, function(){});
			}
			
			//
			//查询实例
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
					
				});
		}
			
			
			
			
			$(function() {
				
				//高度自适应
				//instance高度自适应
                var rightDiv = document.getElementById("instanceBody");
                var ch_t = rightDiv.clientHeight;
                var ifm_left= $("#leftdiv");
                ifm_left.css("height",ch_t-20*$scope.pageSize);
				
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

			  //编辑实例按钮控制 --移入
			    $("#editInstanceImg").mouseover(function(){
             		$(this).css("background-color","#458fe9");
             		$(this).css("background-position","0 48px");
             		$(this).css("border-color","#377ed2");
             		$(this).css("color","#FFFFFF");
             		
             		var imgObj = $(this).children()[0];
             		if(null != imgObj){
             			$(imgObj).prop("src","../img/editTemplateChoosed.png");
             		}
             		var spaObj  = $(this).children()[1];
             		if(null != spaObj){
             			$(spaObj).css("color","#FFFFFF");
             		}
             	});
			    // --移出
             	$("#editInstanceImg").mouseout(function(){
             		$(this).css("background-color","");
             		$(this).css("background-position","");
             		$(this).css("border-color","");
             		$(this).css("color","");
             		
             		var imgObj = $(this).children()[0];
             		if(null != imgObj){
             			$(imgObj).prop("src","../img/editTemplateShow.png");
             		}
             		var spaObj  = $(this).children()[1];
             		if(null != spaObj){
             			$(spaObj).css("color","#327CD5");
             		}
             	});

			});
			
		});