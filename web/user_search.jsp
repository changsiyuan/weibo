<%-- 
    Document   : user_search
    Created on : 2014-12-2, 11:34:18
    Author     : changsiyuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>用户搜索结果</title>
    </head>
    <body>
        <%
            //用户信息
            String userName1 = (String) session.getAttribute("UserName1");
            String userEmail = (String) session.getAttribute("UserEmail");
            String userDescription = (String) session.getAttribute("UserDescription");
        
            //微博的数量
            Integer weiboNum = (Integer) session.getAttribute("weiboNum"); //对象只能转化为包装类，不能转为基本类型int
            //将微博内容和微博时间存放在数组中
            String[] weiboContent = new String[weiboNum];
            String[] weiboTime = new String[weiboNum];
            for (int m = 1, i = 0; m <= weiboNum; m++, i++) {
                String wContent = "wContent" + m;
                String wTime = "wTime" + m;
                weiboContent[i] = (String) session.getAttribute(wContent);
                weiboTime[i] = (String) session.getAttribute(wTime);
            }
        %>
        您查询的用户信息如下：<br>
        用户名：<%=userName1%> <br>
        用户email：<%=userEmail%> <br>
        用户自我简介：<%=userDescription%> <br>
        您查询的用户发过的微博以及时间如下：<br>
        <%
            for (int i = 0; i < weiboNum; i++) {
        %>
        <%=weiboContent[i]%>
        <br><%=weiboTime[i]%><br>
        <%
            }
        %>
        
        <a href="login_success.jsp"><button type="button" class="btn-link">返回我的首页</button></a>
        <br><br><br>
        <a href="AttentionServlet">加关注</a>
        <a href="UnfollowServlet">取消关注</a>
    </body>
</html>
