<%-- 
    Document   : register_success
    Created on : 2014-12-1, 11:33:18
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>register success</title>
    </head>
    <body>
        <%
            String userName = (String) session.getAttribute("UserName");
            String userEmail = (String) session.getAttribute("UserEmail");
            String userDescription = (String) session.getAttribute("UserDescription");
        %>
        <div align=center>
            <%=userName%>
            恭喜！您已经成功注册账户！
            您的电子邮箱为：
            <%=userEmail%>
            您的个人简介为：
            <%=userDescription%>
            <br>
            <a href="login_success.jsp"><button type="button" class="btn btn-success">进入我的首页</button><br></a>
        </div>
    </body>
</html>
