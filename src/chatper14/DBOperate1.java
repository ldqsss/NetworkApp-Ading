package chatper14;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/12/14 15:58
 * @Author: Ading
 * @file_des:
 */

import java.sql.*;

public class DBOperate1 {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {
            // 此处需要根据 mysq5 版本修改
            Class.forName("com.mysql.jdbc.Driver");
            String dbUrl = "jdbc:mysql://202.116.195.71:3306/mypeopledb?characterEncoding=utf8&useSSL=false";
//指定用户名和密码
            String dbUser="student";
            String dbPwd="student";


            connection = java.sql.DriverManager.getConnection(dbUrl,dbUser,dbPwd);
//            String sql = "select * from people2 where name like ? and age=?";
//
//            stmt.setObject(1, "%");
//            stmt.setObject(2,"%");
//            System.out.println(sql);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next())
//            {
//                System.out.print(rs.getString(1)+"\t");
//                System.out.print(rs.getString(2)+"\t");
//                System.out.print(rs.getInt(3)+"\t");
//                System.out.print(rs.getString(4)+"\n");
//            }
            System.out.println("------------------------------------");

            String sql1 = "insert into PEOPLES2(IP,NO,NAME,AGE,CLASS) values(?,?,?,?,?)";
            PreparedStatement stmt ;

            stmt = connection.prepareStatement(sql1);
            stmt.setObject(1,"xxx");
            stmt.setObject(2,"20191002883");
            stmt.setObject(3,"xxx");
            stmt.setObject(4, 20);
            stmt.setObject(5,"软件工程1901");
            int i = stmt.executeUpdate();
            System.out.println(i);
//            System.out.printf(String.valueOf(stmt.executeUpdate()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
