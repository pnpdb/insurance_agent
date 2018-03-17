<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div>
    <div class="tool-bar">
        <c:if test="${!empty roleAdd}">
            <button class="btn" onclick="jFramework.modal.show('${roleAdd.name}',{url:'${roleAdd.url}'});">${roleAdd.name}</button>
        </c:if>

        <c:set var="del" value="${!empty roleDel}"/>
        <c:if test="${del}">
            <button class="btn btn-danger">${roleDel.name}</button>
        </c:if>
    </div>
    <div class="search-bar">
        <form method="post" id="search-form-${randomId}">
            <input placeholder="输入名称模糊搜索" value="${condition.name}" name="model['name']">
            <button class="btn search-btn-${randomId}">查询</button>
        </form>
    </div>
    <table class="data-table">
        <thead>
        <tr>
            <c:set var="cols" value="2" />
            <c:if test="${del}">
                <c:set var="cols" value="${cols+1}"/>
                <th class="checkbox-th"><input type="checkbox" class="check-all-${randomId}"></th>
            </c:if>
            <th>名称</th><th>状态</th>
            <c:set var="opt" value="${!empty roleEdit or !empty rolePermissionAssign}"/>
        <c:if test="${opt}">
            <th>操作</th>
            <c:set var="cols" value="${cols+1}"/>
        </c:if>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty page.rows}">
                <tr>
                    <td colspan="${cols}" class="empty-tip">
                        <c:set var="entries" value="${conditions.entrySet()}"/>
                        <c:if test="${!empty entries}">
                            <c:forEach items="${entries}" var="c">
                                <c:if test="${!empty c.value}">
                                    【${c.value}】
                                </c:if>
                            </c:forEach>
                            </c:if>无角色数据
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${page.rows}" var="r">
                    <tr>
                        <c:if test="${del}">
                            <td><input type="checkbox" class="check-one-${randomId}" value="${r.id}"></td>
                        </c:if>
                        <td>${r.name}</td>
                        <td>${r.state eq 'enable'?'可用':'禁用'}</td>
                        <c:if test="${opt}">
                        <td>
                            <c:if test="${!empty roleEdit}">
                                <button class="btn" onclick="jFramework.modal.show('${roleEdit.name}',{url:'${roleEdit.url}?rd=${r.id}'});">${roleEdit.name}</button>
                             </c:if>
                            <c:if test="${!empty rolePermissionAssign}">
                                <button class="btn" onclick="jFramework.modal.show('${rolePermissionAssign.name}',{url:'${rolePermissionAssign.url}?rd=${r.id}'});">${rolePermissionAssign.name}</button>
                            </c:if>
                        </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
    <%@include file="../../common/pagination.jsp"%>
</div>
