/**
 * Created by Administrator on 2017/4/26.
 */
var setting = {
		    view: {
		       
		        selectedMulti: false,
		    	showIcon: false,
		    	showLine: false,
		    },
		    check: {
		        enable: true,
		        chkboxType : { "Y" : "ps", "N" : "ps"}
		    },
		    data: {
		        simpleData: {
		            enable: true
		        }
		    },
		    edit: {
		        enable: false,
		    },
		};
/*var zNodes = [
	 { id: 11, pId: 0, name: "父节点11", open: true },
	    { id: 10, pId: 11, name: "父节点10"},
	    { id: 123, pId: 11, name: "叶子节点123" },
	    { id: 666, pId: 11, name: "叶子节点666" },
	    { id: 567, pId: 11, name: "叶子节点567" },
	    { id: 908, pId: 11, name: "叶子节点908" },
	    { id: 569, pId: 908, name: "父节点569" },
	    { id: 888, pId: 908, name: "叶子节点888" },
	    { id: 4567, pId: 888, name: "叶子节点4567" },
	    { id: 143, pId: 888, name: "叶子节点143" },
	    { id: 36, pId: 569, name: "叶子节点36" },
	    { id: 78, pId: 11, name: "父节点13", isParent: true },
	    { id: 689, pId: 0, name: "父节点689" },
	    { id: 3114, pId: 689, name: "父节点3114", open: true },
	    { id: 2016, pId: 689, name: "叶子节点2016" },
	    { id: 2017, pId: 689, name: "叶子节点2017" },
];*/
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
var streeNodes=null;
function createTree1(node,orgtree,modelId){
	var modelnode = [];
	var jk=0;
	for(i=0;i<node.length;i++){
		if(node[i].subserviceId==modelId){
			modelnode[jk]=node[i];
			jk=jk+1;
		}
	}
	var testarry = [];
	streeNodes=modelnode;
	var stree = orgtree.organizationList;
	for(i=0;i<stree.length;i++){
		testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:false,halfCheck:false};
	};
	
	$(document).ready(function () {
	    $.fn.zTree.init($("#treeDemo2"), setting, testarry);
	});
	for(i=0;i<stree.length;i++){
		for(j=0;j<modelnode.length;j++){
			if(stree[i].id==modelnode[j].userOrOrdId){
				var zTree = $.fn.zTree.getZTreeObj("treeDemo2");
					var pnode = zTree.getNodeByParam("id",modelnode[j].userOrOrdId, null);
					zTree.checkNode(pnode, true, true);
				break;
			}
		}
	}
};
///+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
function createTree2(orgtree){
	var testarry = [];
	var stree = orgtree.organizationList;
	for(i=0;i<stree.length;i++){
		testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:false};
	}
	$(document).ready(function () {
	    $.fn.zTree.init($("#treeDemo1"), setting, testarry);
	});
};

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
function createTree3(node,orgtree,modelId){
	
	var modelnode = [];
	var jk=0;
	for(i=0;i<node.length;i++){
		if(node[i].subserviceId==modelId){
			modelnode[jk]=node[i];
			jk=jk+1;
		}
	}
	var testarry = [];
	streeNodes=modelnode;
	var stree = orgtree.organizationList;
	for(i=0;i<stree.length;i++){
		testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:false,halfCheck:false};
		};
	$(document).ready(function () {
	    $.fn.zTree.init($("#treeDemo3"), setting, testarry);
	});
	
	for(i=0;i<stree.length;i++){
		for(j=0;j<modelnode.length;j++){
			if(stree[i].id==modelnode[j].userOrOrdId){
				var zTree = $.fn.zTree.getZTreeObj("treeDemo3");
					var pnode = zTree.getNodeByParam("id",modelnode[j].userOrOrdId, null);
					zTree.checkNode(pnode, true, true);
				break;
			}
			
		}
	}
	
};
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//获取点选的机构ID集合
function onCheckId($http,$scope,zTree) {
	if(zTree==null){
		return null;
	}
        nodes = zTree.getCheckedNodes(true),
        v = "";
    var parentid = "";
    var parentlevel = "";
    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + ",";
        parentid += nodes[i].id + ",";
        parentlevel += nodes[i].menu_level + ",";
    }
    if (v.length > 0) {
        v = v.substring(0, v.length - 1);
        parentid = parentid.substring(0, parentid.length - 1);
        parentlevel = parentlevel.substring(0, parentlevel.length - 1);
        var ids =parentid.split(",");
		return ids;
    }
    	return null;
};
//++++++++++++++++++++++++++++++++++++++++++++++++++++
//获取点选的机构名称集合。
function onCheckName($http,$scope,zTree) {
	if(zTree==null){
		return null;
	}
    nodes = zTree.getCheckedNodes(true),
    v = "";
var parentid = "";
var parentlevel = "";
for (var i = 0, l = nodes.length; i < l; i++) {
    v += nodes[i].name + ",";
    parentid += nodes[i].id + ",";
    parentlevel += nodes[i].menu_level + ",";
}
if (v.length > 0) {
    v = v.substring(0, v.length - 1);
    parentid = parentid.substring(0, parentid.length - 1);
    parentlevel = parentlevel.substring(0, parentlevel.length - 1);
    var names =v.split(",");
	return names;
}
	return null;
};
//=====================================================================================
function addHoverDom(treeId, treeNode) {
var sObj = $("#" + treeNode.tId + "_span");
if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
    + "' title='add node' onfocus='this.blur();'></span>";
sObj.after(addStr);
var btn = $("#addBtn_" + treeNode.tId);
if (btn) btn.bind("click", function () {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.addNodes(treeNode, { id: (100 + newCount), pId: treeNode.id, name: "new node" + (newCount++) });
    return false;
});
};
function removeHoverDom(treeId, treeNode) {
$("#addBtn_" + treeNode.tId).unbind().remove();
};
//========================================================================
//回调函数加载组织机构的用户
function zTreeOnClick(event, treeId, treeNode) {
	var ourl = tenantSelfinterfaces.Var_OthergetuserWithOrg;
	var orgId=treeNode.id.split("/key")[0];
	 $.ajax({
			type:"get",
			url:tenantSelfinterfaces.Var_getOrgWithUser,
			data:"OrgId="+orgId,
			success:function(ret54){//请求成功且响应完整时执行，ret54==响应值(可能是解析后)
				/*if(ret54!=null){}*/
				var stree=ret54.userList;
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				var node = treeObj.getNodeByParam("id",orgId+"/1", null);
				treeObj.removeNode(node);
				var node = treeObj.getNodeByParam("id",orgId+"/2", null);
				treeObj.removeNode(node);
				var testarry = [];
				
				for(i=0;i<stree.length;i++){
					testarry[i]={ id:"/"+stree[i].id, pId: treeNode.id, name: "U/"+stree[i].name, open: true ,checked:false};
					//treeObj.removeChildNodes(treeNode);
				}
				
				for(i=0;i<stree.length;i++){
					var node = treeObj.getNodeByParam("id","/"+stree[i].id, null);
					treeObj.removeNode(node);
				}
				newNodes = treeObj.addNodes(treeNode, testarry);

					if(streeNodes!=null){
						for(j=0;j<streeNodes.length;j++){
							if(streeNodes[j].userOrOrdId.indexOf("/")>= 0){
								var node = treeObj.getNodeByParam("id",streeNodes[j].userOrOrdId, null);
								var zTree = $.fn.zTree.getZTreeObj(treeId);
								zTree.checkNode(node, true, true);
							}else{
								var node = treeObj.getNodeByParam("id",streeNodes[j].userOrOrdId, null);
								var zTree = $.fn.zTree.getZTreeObj(treeId);
								zTree.checkNode(node, true, true);
							}
						}
					}
			},
			dataType:"json"
		  });
};

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
/*//显示菜单
function showMenu() {
    $("#menuContent2").css({ left: "15px", top: "34px" }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}
//隐藏菜单
function hideMenu() {
    $("#menuContent2").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent2" || event.target.id == "txt_ztree" || $(event.target).parents("#menuContent2").length > 0)) {
        hideMenu();
    }
}

//节点点击事件
function onClickNode(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.checkNode(treeNode, !treeNode.checked, null, true);
    return false;
}

//节点选择事件
function onCheck(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
        nodes = zTree.getCheckedNodes(true),
        v = "";
    var parentid = "";
    var parentlevel = "";
    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + ",";
        parentid += nodes[i].id + ",";
        parentlevel += nodes[i].menu_level + ",";
    }
    if (v.length > 0) {
        v = v.substring(0, v.length - 1);
        parentid = parentid.substring(0, parentid.length - 1);
        parentlevel = parentlevel.substring(0, parentlevel.length - 1);
    }
    else {
        return;
    }
}*/
