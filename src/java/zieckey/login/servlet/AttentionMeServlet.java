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
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lenovo
 */
@WebServlet(name = "AttentionMeServlet", urlPatterns = {"/AttentionMeServlet"})
public class AttentionMeServlet extends HttpServlet {

    public AttentionMeServlet() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        String result = "";

        // 获取现在登陆的这个用户的用户名（目的是查询自己的微博）
        String UserName = session.getAttribute("UserName").toString();

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
            
            //查询现在登陆的用户的user_id
            String sqlLoginedUserId = "select user_id from weibo.user where user_name = '" + UserName + "'";
            rs = stmt.executeQuery(sqlLoginedUserId);
            rs.next();
            int loginedUserId = rs.getInt("user_id");
            
            //查询关注我的用户及其详细信息
            String sqlAttentionMe = "select a.user_id, user_name, user_email, user_describe from (select user_id from weibo.attention where attention_user_id = "+loginedUserId+") a join weibo.user b on a.user_id=b.user_id  ";
            rs = stmt.executeQuery(sqlAttentionMe);
            int attentionMeNum = 0; //记录现在登陆的这个用户被关注数量
            while (rs.next()) {
                attentionMeNum++;
            }
            rs.beforeFirst();
            //定义数组存储关注我的用户的信息
            String[] userName = new String[attentionMeNum];
            String[] userEmail = new String[attentionMeNum];
            String[] userDescription = new String[attentionMeNum];
            for (int i = 1, j = 0; i <= attentionMeNum; i++, j++) {
                rs.next();
                userName[j] = rs.getString("user_name"); //所有关注的用户的信息依次存入数组
                userEmail[j] = rs.getString("user_email");
                userDescription[j] = rs.getString("user_describe");
            }

            //便于显示关注我的用户的用户名 email等信息
            for (int j = 0, m = 1; j < attentionMeNum; j++, m++) {
                String attentionMeUserName = "amUserName" + m;
                String attentionMeUserEmail = "amUserEmail" + m;
                String attentionMeUserDescription = "amUserDescription" + m;
                request.getSession().setAttribute(attentionMeUserName, userName[j]);
                request.getSession().setAttribute(attentionMeUserEmail, userEmail[j]);
                request.getSession().setAttribute(attentionMeUserDescription, userDescription[j]);
            }
            //关注我的用户数量传到前端
            request.getSession().setAttribute("attentionMeUserNum", attentionMeNum);

            response.sendRedirect("attention_me.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
