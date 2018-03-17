/**
 * Created by gecha on 2016/12/29.
 */
(function (window, $, jf) {
    var tabs = [];
    var Tab = function (menu) {
        this.menu = menu;
        this.id = menu.id;
        this.titleDom = tabBuild(this);
        this.contentDom = tabContent(this.menu);
        this.active();
        this.loadContent();
    };
    var tabBuild = function (_this) {
        var menu = _this.menu;
        var tabTitle = $("<div/>").addClass("tab").attr("id", "tab-" + menu.id).attr("data-id", menu.id);
        var tabText = $("<div/>").addClass("tab-text").append('<span>' + menu.text + '</span>');
        var tabClose = $('<i class="icon close"></i>');
        tabText.click(function () {
            _this.active();
        });
        tabClose.click(function () {
            if (jf.tab.current != _this) {
                return;
            }
            var removeTab = function (tab) {
                tab.titleDom.remove();
                tab.contentDom.remove();
            };
            traverseArray(tabs, function (tab, i) {
                if (_this.id == tab.id) {
                    removeTab(tab);
                    tabs.remove(i);
                    var next = i;
                    if (next >= tabs.length) {
                        next = tabs.length - 1;
                    }
                    next = tabs[next];
                    next && next.active();
                    return false;
                } else {
                    return true;
                }
            });
            jf.menu.disActive(_this.id);
            scrollTabBind();
        });
        tabTitle.append(tabText.append(tabClose));
        $(jf.tab.titleContainer).append(tabTitle);
        scrollTabBind(tabTitle);
        return tabTitle;
    };
    var scrollTabBind = function (tab) {
        var tabContainer = $(jf.tab.titleContainer);
        var width = tabContainer.width();
        var tabContainerDom = tabContainer[0];
        var scrollWith = tabContainerDom.scrollWidth;
        if (scrollWith > width) {
            var gap = scrollWith-width;
            var scroll = function (dom) {
                var scrollGap = 40;
                var leftHide=false,rightHide=false;
                var toLeft = $(jf.tab.titleContainer + " > .scroll-left");
                toLeft.css("left",$(".main-left").width());
                var toRight = $(jf.tab.titleContainer + " > .scroll-right");
                if(tab){
                    dom.scrollLeft(tab[0].offsetLeft);
                }
                var x = dom.scrollLeft();
                if (x == 0) {
                    leftHide=true;
                    toLeft.css("visibility", "hidden");
                }else if(x>=gap){
                    toRight.css("visibility", "hidden");
                    rightHide=true;
                }

                toLeft.unbind("click").click(function () {
                    var x = dom.scrollLeft();
                    if(rightHide){
                        toRight.css("visibility", "visible");
                        rightHide=false;
                    }
                    if (x == 0) {
                        $(this).css("visibility", "hidden");
                        leftHide=true;
                    }
                    dom.scrollLeft(x - scrollGap);
                });
                toRight.unbind("click").click(function () {
                    var x = dom.scrollLeft();
                    if(leftHide){
                        toLeft.css("visibility", "visible");
                        leftHide=false;
                    }
                    if(gap <= x){
                        $(this).css("visibility", "hidden");
                        rightHide=true;
                    }
                    dom.scrollLeft(x + scrollGap);
                });
            };
            $(jf.tab.titleContainer + " > .scroll").css("visibility", "visible");
            scroll(tabContainer);
        }else{
            $(jf.tab.titleContainer + " > .scroll").css("visibility", "hidden");
        }
    };
    var tabContent = function (menu) {
        var content = $("<div/>").addClass("tab-content").attr("id", "tab-content-" + menu.id);
        $(jf.tab.contentContainer).append(content);
        return content;
    };
    var traverseArray = function (array, fun) {
        for (var j = 0; j < array.length; j++) {
            var item = array[j];
            if (!fun(item, j)) {
                return false;
            }
        }
        return true;
    };
    var tabActive = function (_this) {
        if (jf.tab.current && _this == jf.tab.current) {
            return;
        }
        jf.menu.active(_this.id);
        var tab = jf.tab.current;
        if (tab) {
            tab.titleDom.removeClass("active");
            tab.contentDom.hide();
        }
        _this.titleDom.addClass("active");
        _this.contentDom.show();
        jf.tab.current = _this;
    };
    var loadTabContent = function (_this) {
        jf.ajax(_this.menu.url, {
            params: _this.params,
            onSuccess: function (r) {
                _this.contentDom.html(r);
            }
        });
    };
    Tab.prototype = {
        active: function () {
            tabActive(this);
        },
        loadContent: function () {
            loadTabContent(this);
        },
        setParams:function(paramsArray){
            var params = this.params||{};
            if(paramsArray&&paramsArray.length>0){
                for(var i=0;i<paramsArray.length;i++){
                    var item = paramsArray[i];
                    params[item.name] = item.value;
                }
            }
            this.params=params;
        }
    };
    var newTab = function (menu) {
        traverseArray(tabs, function (tab) {
            if (tab.id == menu.id) {
                tab.active();
                return false;
            }
            return true;
        }) && tabs.push(new Tab(menu));
    };
    $(window).resize(function(){
        scrollTabBind();
    });
    jf.tab = {
        titleContainer: ".tab-container",
        contentContainer: ".tab-content-container",
        activeTab: function (menu) {
            newTab(menu);
        },
        refresh: function () {
            if (this.current) {
                this.current.loadContent();
            }
        }
    };
})(window, jQuery, jFramework);