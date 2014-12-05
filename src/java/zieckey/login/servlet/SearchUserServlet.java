package zieckey.login.servlet;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author changsiyuan
 */
public class SearchUserServlet extends HttpServlet implements Servlet {

    public SearchUserServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        String result = "";

        // 获取现在登陆的这个用户的用户名（目的是查询自己的微博）
        String sUserName = session.getAttribute("UserName").toString();
        if (sUserName == "" || sUserName == null || sUserName.length() > 20) {
            try {
                result = "请输入用户名（不超过20字符）！";
                request.setAttribute("ErrorUserName", result);
                response.sendRedirect("register.html");
            } catch (Exception e) {
            }
        }

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
            String sqlSearchUser = "Select * FROM weibo.user where user_name = '" + sUserName + "'";
            rs = stmt.executeQuery(sqlSearchUser);// 返回查询结果

            //从rs中取出用户信息
            rs.next();
            int userId = rs.getInt("user_id");
            String userName = rs.getString("user_name");
            String userEmail = rs.getString("user_email");
            String userDescription = rs.getString("user_describe");

            //便于在搜索页面显示用户名 email等信息
            request.getSession().setAttribute("UserName1", userName);
            request.getSession().setAttribute("UserEmail", userEmail);
            request.getSession().setAttribute("UserDescription", userDescription);

            //从weibo表中select出搜索的用户的微博的内容和时间
            String sqlSearchWeibo = "Select weibo_content, weibo_time FROM weibo.weibo where user_id=" + userId;
            rs = stmt.executeQuery(sqlSearchWeibo); //返回这个用户的微博和发微博的时间
            int weiboNum = 0;//微博的数量
            while (rs.next()) {
                weiboNum++;
            }
            String[] weiboContent = new String[weiboNum];
            String[] weiboTime = new String[weiboNum];
            int i = 0;
            rs.beforeFirst();//使rs的指针回到第一行之前
            //将微博以及时间存放在数组中
            while (rs.next()) {
                weiboContent[i] = rs.getString("weibo_content");
                weiboTime[i] = rs.getString("weibo_time");
                i++;
            }

            //便于login_success.jsp页面显示微博内容和时间
            for (int j = 0, m = 1; j < weiboNum; j++, m++) {
                String wContent = "wContent" + m;
                String wTime = "wTime" + m;
                request.getSession().setAttribute(wContent, weiboContent[j]);
                request.getSession().setAttribute(wTime, weiboTime[j]);
            }

            //便于login_success.jsp页面知道这个用户微博的数量
            request.getSession().setAttribute("weiboNum", weiboNum);

            //跳转到login_success.jsp页面显示所搜结果
            response.sendRedirect("user_search.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String result = "";

        // 获取待搜索的用户名
        String sUserName = request.getParameter("txtUserName");
        if (sUserName == "" || sUserName == null || sUserName.length() > 20) {
            try {
                result = "请输入用户名（不超过20字符）！";
                request.setAttribute("ErrorUserName", result);
                response.sendRedirect("register.html");
            } catch (Exception e) {
            }
        }

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
            String sqlSearchUser = "Select * FROM weibo.user where user_name = '" + sUserName + "'";  //在这里sUserName是待所搜的用户的用户名
            rs = stmt.executeQuery(sqlSearchUser);// 返回查询结果

            //从rs中取出用户信息
            rs.next();
            int userId=0;
            try{
                userId = rs.getInt("user_id");  
            }catch(Exception e){
                response.sendRedirect("search_user_failure.jsp"); //查询为空（没有此用户）
                throw e;
            }
            userId = rs.getInt("user_id");
            String userName = rs.getString("user_name");
            String userEmail = rs.getString("user_email");
            String userDescription = rs.getString("user_describe");

            //便于在搜索页面显示用户名 email等信息
            request.getSession().setAttribute("UserName1", userName);   //UserName1是待所搜的用户的用户名
            request.getSession().setAttribute("UserEmail", userEmail);
            request.getSession().setAttribute("UserDescription", userDescription);

            //从weibo表中select出搜索的用户的微博的内容和时间
            String sqlSearchWeibo = "Select weibo_content, weibo_time FROM weibo.weibo where user_id=" + userId;
            rs = stmt.executeQuery(sqlSearchWeibo); //返回这个用户的微博和发微博的时间
            int weiboNum = 0;//微博的数量
            while (rs.next()) {
                weiboNum++;
            }
            String[] weiboContent = new String[weiboNum];
            String[] weiboTime = new String[weiboNum];
            int i = 0;
            rs.beforeFirst();//使rs的指针回到第一行之前
            //将微博以及时间存放在数组中
            while (rs.next()) {
                weiboContent[i] = rs.getString("weibo_content");
                weiboTime[i] = rs.getString("weibo_time");
                i++;
            }

            //便于login_success.jsp页面显示微博内容和时间
            for (int j = 0, m = 1; j < weiboNum; j++, m++) {
                String wContent = "wContent" + m;
                String wTime = "wTime" + m;
                request.getSession().setAttribute(wContent, weiboContent[j]);
                request.getSession().setAttribute(wTime, weiboTime[j]);
            }

            //便于login_success.jsp页面知道这个用户微博的数量
            request.getSession().setAttribute("weiboNum", weiboNum);

            //跳转到login_success.jsp页面显示所搜结果
            response.sendRedirect("user_search.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
