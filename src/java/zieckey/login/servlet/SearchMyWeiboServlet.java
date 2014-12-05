package zieckey.login.servlet;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
public class SearchMyWeiboServlet extends HttpServlet implements Servlet {

    public SearchMyWeiboServlet() {

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
            String sqlMyWeibo = "Select * FROM weibo.weibo where user_id= " + userId;
            rs = stmt.executeQuery(sqlMyWeibo); //返回我的微博和发微博的时间

            ArrayList<String[]> weibo = new ArrayList<String[]>();//存储所有微博
            int myWeiboNum = 0;//微博总数
            while (rs.next()) {
                myWeiboNum++;
            }
            rs.beforeFirst();
            for (int j = 1; j <= myWeiboNum; j++) {
                rs.next();
                String[] myWeibo = new String[5];//存储一条微博的五个字段的信息
                myWeibo[0] = rs.getString("weibo_id");
                myWeibo[1] = rs.getString("user_id");
                myWeibo[2] = rs.getString("weibo_content");
                myWeibo[3] = rs.getString("weibo_time");
                myWeibo[4] = rs.getString("comment_number");
                weibo.add(myWeibo);
            }

            //将上面list放入session
            request.getSession().setAttribute("myWeiboList", weibo);

            //便于login_success.jsp页面知道我的微博的数量
            request.getSession().setAttribute("myWeiboNum", myWeiboNum);

            //跳转到my_weibo.jsp显示我的微博
            response.sendRedirect("my_weibo.jsp");
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
