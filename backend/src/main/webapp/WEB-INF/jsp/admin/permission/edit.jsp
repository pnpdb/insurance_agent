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
    }
    .form-left{
        width:30%;
        max-width: 30%;
        max-height:100%;
        overflow: auto;
        vertical-align: top;
        background-color: #f5f5dc;
    }
    .form-right{
        vertical-align: top;
    }
    .body{
        width:550px;
    }
    .select-head{
        background-color: #cdcdb6;
        padding: 5px;
    }
</style>
<div class="body">
    <form class="add form table" action="/admin/permission/data/opt/${type}/save.json">
        <div class="form-left table-cell">
            <div class="select-head">设置父级权限</div>
            <%@include file="permissionsTree.jsp"%>
        </div>
        <div class="form-right table-cell">
            <c:if test="${!empty model}">
                <input type="hidden" name="model['id']" value="${model.id}">
            </c:if>
            <div class="form-field">
                <label>名称</label>
                <input type="text" class="input-field" required name="model['name']" placeholder="权限名称"
                       value="${model.name}">
            </div>
            <div class="form-field">
                <label>代码</label>
                <input type="text" class="input-field" name="model['code']" placeholder="权限代码"
                       value="${model.code}">
            </div>
            <div class="form-field">
                <label>状态</label>
                <select name="model['state']" class="select-field">
                    <option value="enable" ${model.state eq 'enable'?'selected':''}>可用</option>
                    <option value="disabled" ${model.state eq 'disabled'?'selected':''}>不可用</option>
                </select>
            </div>
            <div class="form-field">
                <label>等级</label>
                <input type="text" class="input-field" name="model['level']">
            </div>
            <div class="form-field">
                <label>连接类型</label>
                <select name="model['type']" class="select-field">
                    <option value="page" ${model.type eq 'page'?'selected':''} >页面</option>
                    <option value="modal" ${model.type eq 'modal'?'selected':''}>弹窗</option>
                </select>
            </div>
            <div class="form-field">
                <label>连接地址</label>
                <input type="text" name="model['url']" class="input-field" placeholder="连接地址" value="${model.url}">
            </div>
            <div class="form-field">
                <label>连接地址校验</label>
            <textarea name="model['urlPattern']" class="textarea-field"
                      placeholder="连接地址校验，回车分割">${model.urlPattern}</textarea>
            </div>
        </div>

    </form>
</div>
<div class="foot">
    <button class="btn btn-primary save">保存</button>
</div>
<script>
    $(function () {
        $(".btn.save").click(function () {
            var btn = $(this);
        });
    });
</script>
