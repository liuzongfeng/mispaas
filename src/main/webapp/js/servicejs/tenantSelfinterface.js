/*var paasrest_dns="http://127.0.0.1:8080";*/
var paasrest_dns="http://192.168.6.16:8080";
var cas_ipPort="http://100.0.10.100:8080";
tenantSelfinterfaces={
		//创建订单he维护订单租户和组织机构的关系
		Var_createPaasOrder:paasrest_dns+'/passService/createPaasOrder',
		Var_createPaasOrderNew:paasrest_dns+'/passService/createPaasOrderNew',
		
		//查询已购买的应用（订单，模板，实例）
		Var_showApplicationList:paasrest_dns+'/passService/showApplicationList',
		//查询应用详情(实例详情)
		Var_showApplicationDetails:paasrest_dns+'/passService/showApplicationDetails',
		//获取应用实例和组织机构的关系
		Var_getInstanceAndOrgShip:paasrest_dns+'/passService/getInstanceAndOrgShip',
		//插入订单组织机构关系
		Var_addInstanceAndOrgShip:paasrest_dns+'/passService/addInstanceAndOrgShip',
		Var_addInstanceAndOrgShip_New:paasrest_dns+'/passService/addInstanceAndOrgShip_New',
		//删除订单组织机构关系
		Var_deleteInstanceAndOrgShip:paasrest_dns+'/passService/deleteInstanceAndOrgShip',
		Var_deleteInstanceAndUserOrg:paasrest_dns+'/passService/deleteInstanceAndUserOrg',
		//查询已发布的产品列表（订单，模板，实例）
		Var_showTempliteList:paasrest_dns+'/passService/showTempliteList',
		//获取模板分类
		Var_getTemplateCategorys:paasrest_dns+'/passService/getTemplateCategorys',
		//撤销定单
		Var_repealOrder:paasrest_dns+'/passService/repealOrder',
		//名字模糊查询产品列表
		Var_showApplicationListByInstanceName:paasrest_dns+'/passService/showApplicationListByInstanceName',
		//获取组织机构
		Var_getOrgtree:paasrest_dns+'/passService/getOrgtree',
		//第三方会获取组织机构接口
		Var_othergetOrgtree:cas_ipPort+'/usermanager/api/authorization/organizations?pageStart=1&pageSize=1000',
		//获取某用户的租户身份群
		Var_gettenantList:paasrest_dns+'/passService/gettenantList',
		//第三方接口获取租户信息
		Var_othergettentant:cas_ipPort+'/usermanager/api/authorization/tenants/',
		//第三方获取用户信息接口
		Var_othergetuser:cas_ipPort+'/usermanager/api/authorization/users/',
		Var_OthergetuserWithOrg:cas_ipPort+'/usermanager/api/authorization/organization/users',
		//初始页面获取用户信息
		Var_geUserDetails:paasrest_dns+'/obtainUserInfo',
		//获取组织结构的用户
		Var_getOrgWithUser:paasrest_dns+'/passService/getOrgWithUser',
};