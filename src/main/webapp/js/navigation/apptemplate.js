angular.module('apptemplate', ['ngRoute', 'auth']).controller(
		'apptemplate', 

		function($route,$http, auth,$rootScope,$scope) {
			
			//发起请求加载模板列表
			selectPage_aa = function (page) {
				
				var templateCategory = $scope.templateCategory;   //分类模板
				var templateName = $scope.templateName;   //名称名称
				$scope.pageSize = 5;                      //临时赋值
				var pageSize = $scope.pageSize;           //每页显示的条数
				$http({
					  method: 'GET',
					  params:{"pageNo":page,"pageSize":pageSize,"templateCategory":templateCategory,"templateName":templateName},
					  url: 'http://localhost:8080/obtainTemplateList'
					}).then(function successCallback(response) {
						
						$scope.templates = response.data.list;   //要展示的数据
						
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
			
			
			//加载模板分类
			var loadTemplateCategory = function(){
				
				$http({
					  method: 'GET',
					  url: 'http://localhost:8080/obtainTemplateCategory'
					}).then(function successCallback(response) {
						$scope.categorys = response.data;
						
					}, function errorCallback(response) {
					    // called asynchronously if an error occurs
					    // or server returns response with an error status.
				});
			}
			//发起加载模板分类
			loadTemplateCategory();
			
			
			
			
			$scope.appTab= true;
			$scope.apptemplate1_title = true;
			$scope.apptemplate1=true;
			$scope.divPage=true;
			//$scope.editTemplate= false;
			$("#activeTemplate").attr("class","active");
			
			
			//导入模板
			$scope.importTemplate_fn = function(){
				var formData = new FormData($('#uploadForm')[0]);
				formData.append("file1",$('#uploadForm')[0]);
				formData.append("file1",$('#uploadForm')[1]);
				$.ajax({
				    url: 'http://localhost:8080/testUploadFile',
				    type: 'POST',
				    cache: false,
				    data: formData,
				    processData: false,
				    contentType: false
				}).done(function(res) { 
					if("uploadOK" == res){
						
						swal({   
							title:"导入成功",
							text: "是否继续导入?",   
							type: "success",   
							showCancelButton: true,   
							confirmButtonColor: "#DD6B55",
							cancelButtonText: "否",//取消按钮文本
							confirmButtonText: "是",//确定按钮上面的文档
							closeOnConfirm: false 
						}, 
						function(isUploadMore){
							if(isUploadMore){
								swal.close();
								selectPage_aa(1);
							}else{
								//1.将模态框进行隐藏
								$("#file1").val("");
								$("#myModal").modal('hide');
								//2.发起查询模板列表的请求
								swal.close();
								selectPage_aa(1);
								
							}
						});
					}else{
						swal({   
							title:"导入失败",
							text: res,   
							type: "error",   
							closeOnConfirm: true 
						}, 
						function(){
								//1.将模态框进行隐藏
								$("#file1").val("");
								$("#myModal").modal('hide');
								selectPage_aa(1);
						});
						
						
					}
				});
			}
			
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
				
				if($('.checkbox_1:checked').length == 0){
					swal("请先选中模板!", "", "warning");
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
		    			$("#activeTemplate").attr("class","");
		                $("#activeEdit").attr("class","active");
		 				$scope.apptemplate1=false;
		 				$scope.divPage=false;
		 				$scope.editTemplate = true;
		 				$scope.editTemplate_title=true;
		    			aa = 1;
		    			var templateId = $(this).val();       //获得选中的模板id
		    			
		    			for(var i=0 ; i< $scope.templates.length; i++){
		    				var template = $scope.templates[i];
		    				if(template.id == templateId){
		    					$scope.Template = template;
		    					//根据模板id,发起请求加载模块信息
		    					$scope.subServices = template.paasSubservices;
		    					/*$http({
		    						  method: 'GET',
		    						  params:{"templateId":templateId},
		    						  url: 'http://localhost:8080/obtainSubServiceByTemplateId'
		    						}).then(function successCallback(response) {
		    							$scope.subServices = response.data;
		    							
		    						}, function errorCallback(response) {
		    						    // called asynchronously if an error occurs
		    						    // or server returns response with an error status.
		    					});
		    					*/
		    				}
		    			}
		    			
		    			//不支持多选：将其他的复选框置为false
		    		}
		    		if(aa == 1){//
		    			$('.checkbox_1').prop("checked",false);
					}
		    	});
				
				if(aa == 0){//未选中
					swal("请先选中模板!", "", "warning");
					$scope.closeEditTemplate_fn();
				}
				
				
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
				
				//alert("发起提交请求");
			}
			
			//删除模板
			$scope.delTemplate_fn = function(){
				//alert("删除模板");
				
				$('.checkbox_1').each(function () {  
		    		var isChecked = $(this).prop("checked"); 
		    		if(isChecked){//有选中对应的模板
		    			var templateId = $(this).val();
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
						function(isConfirm){
							
							//alert("请求后端进行删除");
							//swal("删除成功!", "该项目已被删除!", "success");
							if(isConfirm){ //确认删除
								//发起请求根据模板id,删除模板：需要后台判断该模板是否有实例
								$http({
		    						  method: 'GET',
		    						  params:{"templateId":templateId},
		    						  url: 'http://localhost:8080/deleteTemplateByTemplateId'
		    						}).then(function successCallback(response) {
		    							
		    							
		    							if(response.data.ok == "deleteok"){
		    								//swal("删除成功!", "该模板已被删除!", "success");
		    								swal({   
		    									title:"删除成功!",
		    									text: "该模板已被删除!",   
		    									type: "success",   
		    									closeOnConfirm: true 
		    								}, 
		    								function(){
		    									swal.close();
		    									selectPage_aa(1);
		    								});
		    							}else{
		    								//swal("删除失败!", response.data, "error");
		    								swal({   
		    									title:"删除失败!",
		    									text: response.data.error,   
		    									type: "error",   
		    									closeOnConfirm: true 
		    								}, 
		    								function(){
		    									swal.close();
		    									selectPage_aa(1);	
		    								});
		    							}
		    							
		    						}, function errorCallback(response) {
		    						    
		    					});
							}
						});
		    		}else{//未选中模板
		    			
		    		}
				});
				
				
		}
			
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
				var templateName = $('#templateName_S').val();
				$scope.templateName = templateName;
				selectPage_aa(1);//查询列表
			}
			
			
			
			$(function() {
				//按分类查询
				$("#byCategory").change(function(){
					var templateCategory = this.options[this.options.selectedIndex].value;
					$scope.templateCategory = templateCategory;
					
					selectPage_aa(1);
				});
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
			    	$('#checkbox_1').prop("checked",false); 
			    });
			});
		});












