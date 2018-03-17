<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>代理-员工渠道</title>
    <link href="/assets/css/framework.css" rel="stylesheet">
    <link href="/assets/css/main.css" rel="stylesheet">
    <script src="/assets/js/jquery/jquery-1.11.3.min.js"></script>
    <script src="/assets/js/jFramework.js"></script>
    <script src="/assets/js/jFramework.menu.js"></script>
    <script src="/assets/js/jFramework.tab.js"></script>
</head>
<body>
<div class="main">
    <div class="main-left">
        <div class="user-info table">
            <div class="info">
                <img src="/assets/images/user.svg">
                <span>${admin.name}</span>
            </div>
            <form method="post" action="/admin/login/out" style="display: none" name="outForm"></form>
            <div class="login-out" onclick="document.outForm.submit()" title="退出登录">
                <i class="icon icon-login-out"></i>
            </div>
        </div>
        <div class="menu-box">
        </div>
    </div>
    <div class="main-right">
        <div class="tab-head table">
            <div class="line"></div>
            <div class="tab-container table">
                <div class="scroll scroll-left">
                    <i class="icon icon-arrow-left"></i>
                </div>
                <div class="scroll scroll-right">
                    <i class="icon icon-arrow-right"></i>
                </div>
            </div>
            <div class="refresh-tab" title="刷新当前页"><i class="icon icon-refresh"></i></div>
        </div>
        <div class="tab-content-container" >
        </div>
    </div>
</div>
<script>
    $(function(){
        var menus = ${treeMenu};
        /*for(var i=1;i<9;i++){
            var item = {text:"menu_"+i,id:i};
            if(i==6||i==4){
                var children = [];
                for(var j=1;j<i;j++){
                    var child = {text:"menu_"+i+"-"+j,id:"sub"+i+j};
                    if(j==5){
                        var subChildren=[];
                        for(var n=1;n<j;n++){
                            subChildren.push({text:"menu_"+i+"-"+j+"-"+n,id:"ssub"+i+j+n});
                        }
                        child.children=subChildren;
                    }
                    children.push(child);
                }
                item.children=children;
            }
            menus.push(item);
        }*/
        jFramework.menu.load(menus);
        $(".refresh-tab").click(function(){
            jFramework.tab.refresh();
        });
    });
</script>
</body>
</html>
