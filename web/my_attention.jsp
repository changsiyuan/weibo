<%-- 
    Document   : my_attention
    Created on : 2014-12-2, 20:56:04
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>my attention users</title>
    </head>
    <body>
        <%
            Integer attentionUserNum = (Integer) session.getAttribute("attentionUserNum");

            String[] userName = new String[attentionUserNum];
            String[] userEmail = new String[attentionUserNum];
            String[] userDescription = new String[attentionUserNum];
            for (int m = 1, i = 0; m <= attentionUserNum; m++, i++) {
                String aUserName = "aUserName" + m;
                String aUserEmail = "aUserEmail" + m;
                String aUserDescription = "aUserDescription" + m;
                userName[i] = (String) session.getAttribute(aUserName);
                userEmail[i] = (String) session.getAttribute(aUserEmail);
                userDescription[i] = (String) session.getAttribute(aUserDescription);
            }
        %>

        <h2>我关注的用户信息如下(用户名，email，用户简介)：</h2><br>
        <%
            for (int i = 0; i < attentionUserNum; i++) {
        %>
        <%=userName[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=userEmail[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=userDescription[i]%><br>
        <%
            }
        %>
    </body>
</html>
