<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>员工渠道</title>
    <link href="/assets/css/framework.css" rel="stylesheet">
    <link href="/assets/css/login.css" rel="stylesheet">
</head>
<body>
<div class="login-wrapper">
    <div class="main-box">
        <div class="show-box"></div>
        <div class="form-box">
            <form action="/${sid}/sign/on" method="POST">
            <div class="form">
                <label class="label">员工渠道</label>
                <div class="fields">
                    <div style="padding-left: 20px">
                        <div class="field"><span>账号</span> <input placeholder="请输入" type="text" id="account" name="model['account']"></div>
                        <div class="field"><span>密码</span> <input placeholder="请输入" type="password" id="pwd" name="model['pwd']"></div>
                    </div>
                </div>
                <div class="submit-btn">登 录</div>
            </div>
            </form>
            <div class="copyright">版权所有 &copy; 2016</div>
        </div>
    </div>
</div>
<script src="/assets/js/jquery/jquery-1.11.3.min.js"></script>
<script src="/assets/js/jquery/jquery.form.min.js"></script>
<script src="/assets/js/jFramework.js"></script>
<script>
    $(function(){
        var iframe = window.parent.document.getElementsByTagName("iframe")[0];
        if(iframe){
            window.parent.location.reload();
            return;
        }
        <c:if test="${!empty msg}">
        jFramework.toast.waring('${msg}');
        </c:if>
        $(".submit-btn").click(function(){
            if(!jFramework.utils.isEmpty($("#account").val())&&!jFramework.utils.isEmpty($("#pwd").val())){
                jFramework.loading.show();
                $("form").submit();
            }
        });
        $(".forget-pass").click(function(){
        });
    });
</script>
</body>
</html>
