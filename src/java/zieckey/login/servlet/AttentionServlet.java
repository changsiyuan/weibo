package zieckey.login.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
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
 * @author changsiyuan
 */
@WebServlet(name = "AttentionServlet", urlPatterns = {"/AttentionServlet"})
public class AttentionServlet extends HttpServlet {

    public AttentionServlet() {

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
            if (rs.next() == false) {
                //目前登陆的用户没有关注他查询的这个用户

                //查询目前attention关注表中的最大行数
                String sqlMaxAttention = "select attention_id from weibo.attention order by attention_id DESC LIMIT 1";
                rs = stmt.executeQuery(sqlMaxAttention);
                rs.next();
                int maxAttentionNum = rs.getInt("attention_id");
                maxAttentionNum++;

                //添加关注
                String sqlFollowUp = "insert into weibo.attention values(" + maxAttentionNum + "," + loginedUserId + "," + searchUserId + ")";
                stmt.execute(sqlFollowUp);

                //在屏幕上输出加关注成功的字样
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>you follow him successful!</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>you follow him successful</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            } else {
                //目前登陆的用户已经关注他查询的这个用户
                //在屏幕上输出已经关注的字样
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>you can not follow him again</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>you have followed him, you can not follow him again</h1>");
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
