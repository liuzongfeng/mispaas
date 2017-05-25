/**
 * Created by Administrator on 2017/4/26.
 */
var setting = {
		    view: {
		       
		        selectedMulti: false,
		    	showIcon: false,
		    	showLine: false,
		    	/*fontCss : {color:"red"}*/
		    },
		    check: {
		        enable: true,
		        chkboxType : { "Y" : "s", "N" : "ps"}
		    },
		    data: {
		        simpleData: {
		            enable: true
		        }
		    },
		    edit: {
		        enable: false,
		    }
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
function createTree1(node,orgtree){
	var testarry = [];
	var stree = orgtree.organizationList;
	for(i=0;i<stree.length;i++){
		testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:false};
		for(j=0;j<node.length;j++){
			if(stree[i].id==node[j].orgId){
				testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:true};
				break;
			}
		}	
	}
	$(document).ready(function () {
	    $.fn.zTree.init($("#treeDemo2"), setting, testarry);
	});
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
function createTree3(node,orgtree){
	var testarry = [];
	var stree = orgtree.organizationList;
	for(i=0;i<stree.length;i++){
		testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:false, "chkDisabled":true};
		for(j=0;j<node.length;j++){
			if(stree[i].id==node[j].orgId){
				testarry[i]={ id:stree[i].id, pId: stree[i].parentId, name: stree[i].name, open: true ,checked:true};
				break;
			}
		}	
	}
	$(document).ready(function () {
	    $.fn.zTree.init($("#treeDemo3"), setting, testarry);
	});
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
