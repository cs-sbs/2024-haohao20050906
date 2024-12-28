package com.java.main.start;

import com.java.main.pojo.Admin;
import com.java.main.pojo.User;
import com.java.main.common.AdminLoginSuccess;
import com.java.main.common.UserBuyLibrary;
import com.java.main.utils.Connect;
import com.java.main.utils.CodeUtil;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.java.main.utils.Connect.queryAll;


public class AppRun {
    public AppRun() {
        System.out.println("******************图书管理系统**********************");
        System.out.println("*********************欢迎您！***********************");

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new AppRun();
        //用户
        ArrayList<User> list = new ArrayList<User>();
        //管理员
        ArrayList<Admin> UserAdmin = new ArrayList<>();
        //获取用户
        ResultSet rs1 = Connect.queryAll("select  * from user");
        while (rs1.next()) {
            Integer id = rs1.getInt("id");
            String username = rs1.getString("username");
            String password = rs1.getString("password");
            list.add(new User(id, username, password));
        }
        //获取管理员
        ResultSet rs2 = queryAll("select  * from admin");
        while (rs2.next()) {
            Integer id = rs2.getInt("id");
            String username = rs2.getString("username");
            String password = rs2.getString("password");
            UserAdmin.add(new Admin(id, username, password));
        }
        Scanner sc = new Scanner(System.in);
        rs2.close();
        rs1.close();
        while (true) {
            System.out.println("请输入您的选择：1.登录 2注册  3管理管理员登录   4关闭系统");
            String numStr = sc.nextLine();
            try {
                int num = Integer.parseInt(numStr);
                switch (num) {
                    case 1:
                        Login(list);
                        break;
                    case 2:
                        Register(list);
                        break;
                    case 3:
                        AdminLogin(UserAdmin);
                        break;
                    case 4:
                        System.exit(WindowConstants.EXIT_ON_CLOSE);
                        break;
                    default:
                        System.out.println("没有这个选项");
                        break;
                }
            } catch (Exception e) {
                System.out.println("请输入数字，不能输入其它字符");
            }
        }
    }


    /**
     * 管理员登录
     *
     * @param UserAdminList
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static void AdminLogin(ArrayList<Admin> UserAdminList) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入管理员账号");
            String username = sc.nextLine();
            System.out.println("请输入管理员密码");
            String pwd = sc.nextLine();
            int adminIndex = getAdminIndex(UserAdminList, username);
            if (adminIndex >= 0) {
                if (UserAdminList.get(adminIndex).getUsername().equals(username) &&
                        UserAdminList.get(adminIndex).getPassword().equals(pwd)) {
                    System.out.println("登录成功！");
                    new AdminLoginSuccess();
                    break;
                } else {
                    System.out.println("密码输入错误！");
                }
            } else {
                System.out.println("该用户管理员账号不存在！");
            }
        }
    }

    /**
     * 用户注册
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static void Register(ArrayList<User> list) throws SQLException, ClassNotFoundException {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入用户名：");
            String username = sc.next();
            //判断输入的用户名是否符合要求
            boolean flag = flag(username);
            if (flag) {
                //输入的用户名符合
                //判断用户名是否存在
                int index = getIndex(list, username);
                if (index < 0) {
                    //用户名不存在，可以执行注册
                    System.out.println("请输入密码：");
                    String pwd1 = sc.next();
                    boolean pwdFlag = pwdFlag(pwd1);
                    if (pwdFlag) {
                        //符合要求
                        System.out.println("请再次输入密码：");
                        String pwd2 = sc.next();
                        if (pwd2.equals(pwd1)) {
                            //两次密码输入一致
                            String sql = "insert into user(username,password) values('" + username + "','" + pwd2 + "')";
                            Connect.edit(sql);
                            System.out.println("注册成功！");
                            //刷新用户集合中的数据，在次进行查询获取用户数据
                            //获取数据库中的用户
                            ResultSet rs1 = queryAll("select  * from user");
                            while (rs1.next()) {
                                Integer id = rs1.getInt("id");
                                String uname = rs1.getString("username");
                                String password = rs1.getString("password");
                                list.add(new User(id, uname, password));
                            }
                            break;
                        } else {
                            //两次密码输入不一致
                            System.out.println("两次密码输入不一致，请重新输入！");
                        }
                    } else {
                        //不符合要求
                        System.out.println("密码只能由数字和字母组成，最少6位，最多15位");
                    }
                } else {
                    System.out.println("用户名已经存在，请重新输入~");
                }
            } else {
                //输入的用户名不符合
                System.out.println("用户名要求4-10位且全为字母，请重新输入吧");
            }
        }
    }

    /**
     * 用户登录
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static void Login(ArrayList<User> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入用户名：");
            String username = sc.nextLine();
            //判断用户名是否存在
            int index = getIndex(list, username);
            if (index >= 0) {
                //用户名存在，执行下一步输入密码操作
                System.out.println("请输入密码：");
                String password = sc.nextLine();
                if (password.equals(list.get(index).getPassword())) {
                    String codeStr = CodeUtil.getCode();
                    System.out.println("当前验证码为：" + codeStr);
                    System.out.println("请输入验证码：");
                    String code = sc.nextLine();
                    if (code.equalsIgnoreCase(codeStr)) {
                        //密码输入正确
                        System.out.println("登陆成功！");
                        //传递用户登录的用户名
                        new UserBuyLibrary(username).main();
                        break;
                    } else {
                        System.out.println("验证码输入错误，请重新输入");
                    }
                } else {
                    System.out.println("密码输入错误，请重新输入");
                }
            } else {
                //用户名不存在
                System.out.println("用户名未注册，请注册后再来使用~");
                break;
            }
        }
    }

    /**
     * 判断用户在登录或者注册时用户名是否存在，存在返回对应的索引，否则返回-1
     *
     * @param list
     * @param username
     * @return
     */
    public static int getIndex(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            if (username.equals(list.get(i).getUsername())) {
                //存在
                return i;
            }
        }
        //不存在
        return -1;
    }

    /**
     * 判断管理员登录
     *
     * @param list
     * @param username
     * @return
     */
    public static int getAdminIndex(ArrayList<Admin> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            if (username.equals(list.get(i).getUsername())) {
                //存在
                return i;
            }
        }
        //不存在
        return -1;
    }

    /**
     * 用户注册时要求用户名全为字母，最少4位，最多10位
     *
     * @param username 用户名
     * @return 布尔值
     */
    public static boolean flag(String username) {
        //只能为字母，最少4位，最多10位
        String regex = "[a-zA-Z]{4,10}";
        return username.matches(regex);
    }


    /**
     * 用户注册时要求密码只能由数字和字母组成，最少6位，最多15位
     *
     * @param password 用户名
     * @return 布尔值
     */
    public static boolean pwdFlag(String password) {
        //只能为数字和字母，最少6位，最多15位
        String regex = "[a-zA-Z0-9]{6,15}";
        return password.matches(regex);
    }

}
