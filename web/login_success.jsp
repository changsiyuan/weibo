<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>My JSP 'login_failure.jsp' starting page</title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <!--
       <link rel="stylesheet" type="text/css" href="styles.css">
        -->
    </head>
    <body>
        <%
            String userName = (String) session.getAttribute("UserName");
        %>
        <div align=center>
            <%=userName%>
            欢迎您，登录成功！
            <a href="index.html"><button type="button" class="btn-link">退出登录</button></a>
        </div>
        <div>
            <form method="post" name="frmLogin" action="SearchUserServlet">
                <br>
                <h4>搜索用户（请输入要所搜的用户名）</h4>
                <input type="text" name="txtUserName" value="username"
                       size="20" maxlength="20"
                       onfocus="if (this.value == 'username')
                                   this.value = '';"><br>
                <input type="submit" name="Submit"　value="搜索" >
            </form>
        </div>
        <br><br><br>
        <div>
            <form method="post" name="frmLogin" action="SubmitWeiboServlet">
                <h4>我要发微博（请在下面输入微博）</h4>
                <textarea class="form-control" rows="3" value="发微博" name="weiboContent" onfocus="if (this.value == '发微博')
                            this.value = '';"></textarea><br>
                <input type="submit" name="Submit"　value="发微博" />
                <a href="SearchMyWeiboServlet">查看我的微博</a>
            </form>
        </div>
        <br><br><br>
        想要查看我关注的人？请单击下面按钮：<br>
        <a href="MyAttentionServlet"><button type="button" class="btn btn-success">我关注的人</button><br></a>
        <a href="MyAttentionWeiboServlet"><button type="button" class="btn btn-success">我关注的人的微博</button><br></a> 
        想要查看关注我的人？请单击下面按钮：<br>
        <a href="AttentionMeServlet"><button type="button" class="btn btn-success">关注我的人</button><br></a>
        <br><br><br>
        单击查询最热微博：<br>
        <a href="MostPopularWeiboServlet"><button type="button" class="btn btn-success">最热微博浏览</button><br></a>
    </body>
</html>
