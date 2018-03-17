<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="body">
    <form class="add form" action="/admin/role/data/opt/${type}/save.json">
            <c:if test="${!empty model}">
                <input type="hidden" name="model['id']" value="${model.id}">
            </c:if>
            <div class="form-field">
                <label>名称</label>
                <input type="text" class="input-field" required name="model['name']" placeholder="权限名称"
                       value="${model.name}">
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
