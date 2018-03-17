/**
 * Created by gecha on 2016/12/29.
 */
(function(window,$,jf){
    var menuRoot=function(){
        return $("<ul/>");
    };
    var menuList=function(css,menu){
        var li = $("<li/>").addClass(css);
        menu.text=menu.name||menu.text;
        li.attr("data-id",menu.id).attr("id","menu-"+menu.id).attr("data-url",menu.url);
        li.append('<div class="text"><i class="icon"></i> '+menu.text+'</div>');
        return li;
    };
    var menuNormal=function(menu){
        return menuList("normal",menu);
    };
    var menuExpansion=function(menu){
        return menuList("collapse",menu);
    };
    var menuEventBind=function(root){
        root.find("li > div.text").click(function(){
            var _this = $(this).parent();
            var url = _this.attr("data-url");
            if(_this.hasClass("normal")||url){
                if(_this.hasClass("active")){
                    return;
                }
                var id = _this.attr("data-id");
                var text = _this.find("div.text").text();
                root.find("li.active").removeClass("active");
                _this.addClass("active");
                jf.tab.activeTab({id:id,text:text,url:url});
            }else if(_this.hasClass("expansion")){
                _this.find("> ul").slideUp("fast",function(){
                    _this.removeClass("expansion").addClass("collapse");
                });
            }else if(_this.hasClass("collapse")){
                _this.find("> ul").slideDown("fast",function(){
                    _this.removeClass("collapse").addClass("expansion");
                });
            }
        });
    };
    var menuBuilder=function(root,menu){
        var data = menu.children;
        if(data&&data.length>0){
            var menuDom = menuExpansion(menu);
            var childRoot = menuRoot();
            childRoot.hide();
            root.append(menuDom.append(childRoot));
            for(var j=0;j<data.length;j++){
                var menuChild = data[j];
                menuBuilder(childRoot,menuChild);
            }
        }else{
            root.append(menuNormal(menu));
        }
    };
    var activeMenu=function(_this,id){
        var am = $("#menu-"+id);
        if(am.hasClass("active")){
            return;
        }
        _this.root.find("li.active").removeClass("active");
        am.addClass("active");
    };
    var buildMenuHTML=function(_this,data){
        var root =_this.root= menuRoot();
        for(var n=0;n<data.length;n++){
            menuBuilder(root,data[n]);
        }
        menuEventBind(root);
        return root;
    };
    jf.menu={
        container:".menu-box",
        load:function(data){
            var dom = buildMenuHTML(this,data);
            $(this.container).html(dom);
        },
        active:function(id){
            activeMenu(this,id);
        },
        disActive:function(id){
            var am = $("#menu-"+id);
            if(am.hasClass("active")){
                am.removeClass("active");
            }
        }
    };
})(window,jQuery,jFramework);