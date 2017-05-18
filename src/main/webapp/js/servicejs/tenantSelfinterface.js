/*var paasrest_dns="http://127.0.0.1:8080";*/
var paasrest_dns="http://192.168.6.16:8080";
tenantSelfinterfaces={
		//创建订单he维护订单租户和组织机构的关系
		Var_createPaasOrder:paasrest_dns+'/passService/createPaasOrder',
		//查询已购买的应用（订单，模板，实例）
		Var_showApplicationList:paasrest_dns+'/passService/showApplicationList',
		//查询应用详情(实例详情)
		Var_showApplicationDetails:paasrest_dns+'/passService/showApplicationDetails',
		//获取应用实例和组织机构的关系
		Var_getInstanceAndOrgShip:paasrest_dns+'/passService/getInstanceAndOrgShip',
		//插入订单组织机构关系
		Var_addInstanceAndOrgShip:paasrest_dns+'/passService/addInstanceAndOrgShip',
		//删除订单组织机构关系
		Var_deleteInstanceAndOrgShip:paasrest_dns+'/passService/deleteInstanceAndOrgShip',
		//查询已发布的产品列表（订单，模板，实例）
		Var_showTempliteList:paasrest_dns+'/passService/showTempliteList',
		//获取模板分类
		Var_getTemplateCategorys:paasrest_dns+'/passService/getTemplateCategorys',
		//撤销定单
		Var_repealOrder:paasrest_dns+'/passService/repealOrder',
		//名字模糊查询产品列表
		Var_showApplicationListByInstanceName:paasrest_dns+'/passService/showApplicationListByInstanceName',
};