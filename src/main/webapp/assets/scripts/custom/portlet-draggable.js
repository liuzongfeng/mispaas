var PortletDraggable = function () {

    return {
        //main function to initiate the module
        init: function () {
            var setTimeId = "";
            if (!jQuery().sortable) {
                return;
            }
            $("#sortable_portlets").sortable({
                connectWith: ".portlet",
                items: ".portlet",
                opacity: 0.8,
                coneHelperSize: true,
                revert: true,
                placeholder: 'sortable-box-placeholder round-all',
                forcePlaceholderSize: true,
                tolerance: "pointer",
                start   : function(event, ui) {
                    //排序的一个列中的元素是当前列中的最后一个元素，则不能排序
                    //alert("Start:" + $(ui.item).siblings().length);
                },
                sort:  function(event, ui) {
                   // alert(ui.offset.left + "," + $(ui.item).width());
                    var width = $(ui.item).width();
                    if( (ui.offset.left + width) > $("body").width()){
                        //alert("3333");
                        $(ui.item).hide();
                    }
                    if(ui.offset.left < 0){
                      // alert("3333");
                        $(ui.item).hide();
                    }
                },
				update :  function(event, ui){
                    var portletList = $(ui.item).parent().children(".portlet");
                    var length = portletList.length;
                    for(var i = 0; i < length; i++){
                        var className = $(portletList[i]).attr("class");
                        if(className.indexOf("col-md-6") >= 0){
                            $(portletList[i + 1]).addClass("col-md-6").addClass("clearNone");
                            break;
                        }
                    }
				},
                stop :  function(event, ui){
                   // alert("STOP");
                    if(setTimeId != ""){
                        clearTimeout(setTimeId);
                    }
                    setTimeId =
                        window.setTimeout(function(){
                            //alert("三秒后保存用户配置!");
                            PortletDraggable.updateUserAppConfig();
                            setTimeId = "";
                        },3000);
                }
            });

            $(".column").disableSelection();
        },
        /**
         *  更新应用门户和应用管理首页用户拖拽配置
         */
        updateUserAppConfig: function(){
                // $(ui.item).attr("data-value");
                return false;
                var appList = [];
                //获取应用的页数
                var pages = $("#sortable_portlets").children("[class*='page']");

                for(var k = 0; k < pages.length; k++){
                    //一页应用排列的列数应用集合(一般为三列)
                    var colSortList = $(pages[k]).children(".column.sortable");
                    //列数
                    var count = colSortList.length;
                    //应用所属的页数
                    var page = k + 1;
                    for(var i = 0; i < count; i++){
                        //一列应用集合
                        var colSort = $(colSortList[i]).children(".portlet.box");
                        for(var j = 0; j < colSort.length; j++){
                            var application = colSort[j];
                            var id = $(application).attr("data-value");
                            var style = $(application).find("a").attr("class");
                            //alert("style:" + style + ",---" + style.replaceAllString("appOffLine", ""));
                            style = style.replaceAllString("appOffLine", "");
                            var rowspan = 12;
                            if($(application).hasClass("col-md-6")){
                                rowspan = 6;
                            } else{
                                rowspan = 12;
                            }
                            var col = i + 1;
                            var appStyle = {"id": id , "style": style, "col": col, "page": page, "rowspan": rowspan};
                            appList.push(appStyle);
                        }
                    }
                }

                var actionUrl = "updateAppConfig";
                Ajax.call({
                    url: actionUrl,
                    p: {
                        appList: appList
                    },
                    f: function (data) {
                       // Notify.success("应用配置更新成功!");
                    }
                });
        }
    };
}();