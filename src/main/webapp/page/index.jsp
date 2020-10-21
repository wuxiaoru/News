<%--
  Created by IntelliJ IDEA.
  User: stl
  Date: 2018/1/19
  Time: 13:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
    <title>主页</title>
    <%@ include file="pages/global/mylinks.jsp" %>
<body>
    <!--使用$可以直接输出Session里面的共享数据-->
    <h2>Welcome,${user.username}</h2>
    <a href="${ctx}/login" class="layui-btn layui-bg-green">退出登录</a>
</body>
<script>

</script>

</html>
