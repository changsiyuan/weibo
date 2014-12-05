/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zieckey.login.servlet;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author changsiyuan
 */
public class SubmitWeiboServlet extends HttpServlet implements Servlet {

    public SubmitWeiboServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String result = "";

        // 获取微博内容
        String wContent = request.getParameter("weiboContent");
        if (wContent == "" || wContent == null || wContent.length() > 200) {
            try {
                result = "请重新输入微博（不超过200字符）！";
                request.setAttribute("ErrorWeibo", result);
                response.sendRedirect("login_success.jsp");
            } catch (Exception e) {
            }
        }

        //获取用户名
        String username = (String) request.getSession().getAttribute("UserName");

        // 登记JDBC驱动程序
        try {
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            System.out.println("InstantiationException");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            System.out.println("IllegalAccessException");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            System.out.println("ClassNotFoundException");
        }

        // 连接参数与Access不同
        String url = "jdbc:postgresql://localhost:5432/Twitter";

        // 建立连接
        java.sql.Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(url, "postgres", "postgres");
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            // SQL语句
            String sqlWeiboNum = "select weibo_id from weibo.weibo order by weibo_id DESC LIMIT 1";
            rs = stmt.executeQuery(sqlWeiboNum);// 返回查询结果(现有的微博总条数)
            rs.next();
            int allWeiboNum = rs.getInt("weibo_id");
            allWeiboNum++;

            //查询发微博这个用户的user_id
            String sqlSelectUserId = "select user_id from weibo.user where user_name = '" + username + "'";
            rs = stmt.executeQuery(sqlSelectUserId);
            rs.next();
            int userId = rs.getInt("user_id");

            //将微博存入数据库
            String sqlInsertWeibo = "insert into weibo.weibo values(" + allWeiboNum + ",'" + userId + "','" + wContent + "','11:00:11.101')";
            stmt.execute(sqlInsertWeibo);
            
            response.sendRedirect("login_success.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
