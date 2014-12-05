/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zieckey.login.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lenovo
 */
@WebServlet(name = "UnfollowServlet", urlPatterns = {"/UnfollowServlet"})
public class UnfollowServlet extends HttpServlet {

    public UnfollowServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String result = "";

        HttpSession session = request.getSession();
        String userNameSearch = session.getAttribute("UserName1").toString(); //得到现在所搜的用户的用户名
        String userName = session.getAttribute("UserName").toString(); //得到现在登陆的用户的用户名

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

            //查询已经登录的这个用户的user_id
            String sqlLoginedUserId = "select user_id from weibo.user where user_name = '" + userName + "'";
            rs = stmt.executeQuery(sqlLoginedUserId);
            rs.next();
            int loginedUserId = rs.getInt("user_id");
            //查询被搜索用户的user_id
            String sqlSearchUserId = "select user_id from weibo.user where user_name = '" + userNameSearch + "'";
            rs = stmt.executeQuery(sqlSearchUserId);
            rs.next();
            int searchUserId = rs.getInt("user_id");

            //查询搜索的这个用户是否已经被现在登陆的用户关注
            String sqlSearchUserAttention = "select user_id from weibo.attention where user_id = " + loginedUserId + " and attention_user_id=" + searchUserId;
            rs = stmt.executeQuery(sqlSearchUserAttention);
            if (rs.next() == true) {
                //目前登陆的用户已经关注他查询的这个用户

                //解除关注
                String sqlUnFollow = "delete from weibo.attention where user_id = " + loginedUserId + " and attention_user_id = " + searchUserId;
                stmt.execute(sqlUnFollow);

                //在屏幕上输出取消关注的字样
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>you unfollow him successful!</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>you unfollow him successful!</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            } else {
                //目前登陆的用户没有关注他查询的这个用户
                //在屏幕上输出没有关注的字样
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>you can not unfollow him!</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>you have not followed him, so you can not unfollow him!</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
