<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .permission-tree{
        padding: 5px 0;
    }
    .permission-tree label{
        display: block;
    }
    .permission-tree label input{
        margin-top: 0;
        vertical-align: bottom;
    }
    .permission-tree ul{
        list-style: none;
        padding: 0;
        margin: 0;
    }
    .permission-tree li > ul{
        padding-left: 16px;
    }
    .permission-tree li{
        padding: 5px 0;
        border-bottom: 1px solid #eeeeee;
    }
    .permission-tree li:last-child{
        border-bottom:0;
    }
</style>
<div class="body">
    <form class="add form" action="/admin/role/data/opt/permissionAssign/save.json">
        <%@include file="../permission/permissionsTree.jsp"%>
        <input type="hidden" name="model['roleId']" value="${roleId}">
    </form>
</div>
<div class="foot">
    <button class="btn btn-primary save">保存</button>
</div>
<script>
    $(function () {
        $(".btn.save").click(function () {
            var btn = $(this);
            var add=[],remove=[];
            $("input.not-has:checked").each(function(){
                add.push($(this).val());
            });
            $("input.has").each(function(){
                var _this = $(this);
                if(!_this.prop("checked")){
                    remove.push(_this.val());
                }
            });
        });
    });
</script>
