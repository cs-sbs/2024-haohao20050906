package com.java.main.utils;

import java.sql.*;

/**
 * 数据库连接工具类
 */
public class Connect {
    /**
     * 查询表中的所有数据
     *
     * @param sql
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ResultSet queryAll(String sql) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection rt = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lib_dtb", "root", "haohao0906");
        Statement st = rt.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return rs;
    }

    /**
     * 添加数据，删除数据，修改数据
     *
     * @param sql
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void edit(String sql) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection rt = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lib_dtb", "root", "haohao0906" );
        Statement st = rt.createStatement();
        st.executeUpdate(sql);
    }


}
