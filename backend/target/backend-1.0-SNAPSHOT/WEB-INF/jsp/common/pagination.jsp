<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="pages" value="${page.pages}"/>
<c:set var="pageNum" value="${page.pageNum}"/>
<c:if test="${pages>1}"><div class="pagination">
    <form class="paginationForm-${randomId}" method="post"><c:forEach items="${conditions.entrySet()}" var="p"><c:if test="${!empty p and !empty p.value}"><input type="hidden" name="model['${p.key}']" value="${p.value}"></c:if></c:forEach></form>
    <script>
        function pageTo(num){
            var submitForm=function(form){
                jFramework.tab.current.setParams($(form).serializeArray());
                jFramework.tab.current.loadContent();
            };
            var form =$(".paginationForm-${randomId}")[0];
            form.pageNum=num;
            submitForm(form);
        }
    </script>
    <c:choose><c:when test="${pages<=10}">
        <c:forEach begin="1" end="${pages}" var="p" varStatus="pa">
            <a class="<c:if test="${pageNum eq p}">act</c:if>"
               onclick="<c:if test="${pageNum ne p}">pageTo(${p})</c:if>"
               href="javascript:void(0)">${p}</a>
        </c:forEach></c:when><c:otherwise>
        <c:choose>
            <c:when test="${pageNum<=4}">
                <c:forEach begin="1" end="5" var="p" varStatus="pa">
                    <a class="<c:if test="${pageNum eq p}">act</c:if>"
                       onclick="<c:if test="${pageNum ne p}">pageTo(${p})</c:if>"
                       href="javascript:void(0)">${p}</a></c:forEach>
                <i class="split"></i> <i class="split"></i> <i class="split"></i>
                <a class="<c:if test="${pageNum eq pages}">act</c:if>"
                   onclick="<c:if test="${pageNum ne pages}">pageTo(${pages})</c:if>"
                   href="javascript:void(0)">${pages}</a>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${pageNum ge (pages-3)}">
                        <a class="<c:if test="${pageNum eq 1}">act</c:if>"
                           onclick="<c:if test="${pageNum ne 1}">pageTo(1)</c:if>"
                           href="javascript:void(0)">1</a>
                        <i class="split"></i> <i class="split"></i> <i class="split"></i>
                        <c:forEach begin="${pages-4}" end="${pages}" var="p" varStatus="pa">
                            <a class="<c:if test="${pageNum eq p}">act</c:if>"
                               onclick="<c:if test="${pageNum ne p}">pageTo(${p})</c:if>"
                               href="javascript:void(0)">${p}</a></c:forEach>
                    </c:when>
                    <c:otherwise>
                        <a class="<c:if test="${pageNum eq 1}">act</c:if>"
                           onclick="<c:if test="${pageNum ne 1}">pageTo(1)</c:if>"
                           href="javascript:void(0)">1</a>

                        <i class="split"></i> <i class="split"></i> <i class="split"></i>

                        <c:forEach begin="${pageNum-2}" end="${pageNum+2}" var="p" varStatus="pa">
                            <a class="<c:if test="${pageNum eq p}">act</c:if>"
                               onclick="<c:if test="${pageNum ne p}">pageTo(${p})</c:if>"
                               href="javascript:void(0)">${p}</a></c:forEach>
                        <i class="split"></i> <i class="split"></i> <i class="split"></i>
                        <a class="<c:if test="${pageNum eq pages}">act</c:if>"
                           onclick="<c:if test="${pageNum ne pages}">pageTo(${pages})</c:if>"
                           href="javascript:void(0)">${pages}</a>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </c:otherwise></c:choose>
</div>
</c:if>
<script>
    $(function(){
        $(".search-btn-${randomId}").click(function(e){
            e.preventDefault();
            jFramework.tab.current.setParams($("#search-form-${randomId}").serializeArray());
            jFramework.tab.current.loadContent();
        })
    });
</script>
