package com.java.main.common;

import com.java.main.pojo.User;
import com.java.main.utils.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 用户管理
 */
public class UserAdmin {
    public UserAdmin() throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        //用户信息
        ArrayList<User> list = new ArrayList<>();
        String sql = "select * from user";
        //连接数据库
        ResultSet rs = Connect.queryAll(sql);
        while (rs.next()) {
            list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
        }
        rs.close();
        System.out.println("请输入你的选择：1查看所有用户信息 2删除用户信息 3修改用户信息 4增加用户信息 5选择用户/图书管理");
        String numStr = sc.nextLine();
        try {
            int num = Integer.parseInt(numStr);
            switch (num) {
                case 1:
                    LookUser(list);
                    break;
                case 2:
                    Delete(list);
                    break;
                case 3:
                    UpdateUser(list);
                    break;
                case 4:
                    InsertUser(list);
                    break;
                case 5:
                    new AdminLoginSuccess();
                    break;
                default:
                    System.out.println("没有这个选项！");
                    new UserAdmin();
                    break;
            }
        } catch (Exception e) {
            System.out.println("请输入数字！");
            new UserAdmin();
        }
    }

    /**
     * 修改用户信息
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void UpdateUser(ArrayList<User> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要修改的用户id：");
            int id = sc.nextInt();
            boolean flag = getResult(list, id);
            if (flag) {
                //输入的id存在
                System.out.println("请输入修改后的用户名：");
                String username = sc.next();
                boolean result1 = flag(username);
                if (result1) {
                    //符合要求
                    System.out.println("请修改后的密码：");
                    String pwd = sc.next();
                    boolean result2 = pwdFlag(pwd);
                    if (result2) {
                        //符合要求
                        String sql = "update user set username='" + username + "',password='" + pwd + "' where id=" + id;
                        Connect.edit(sql);
                        System.out.println("用户信息修改成功！");
                        new UserAdmin();
                        break;
                    } else {
                        //不符合要求
                        System.out.println("密码只能由数字和字母组成，最少6位，最多15位");
                    }
                } else {
                    //不符合要求
                    System.out.println("用户名要求4-10位且全为字母，请重新输入吧");
                }
            } else {
                System.out.println("输入的用户id在数据库中不存在，请重新输入~");
                System.out.println("是否继续执行修改用户信息操作？输入yes/no");
                String continue1 = "yes";
                String exit = "no";
                String choose = sc.next();
                if (choose.equals(continue1)) {
                    //继续执行
                    UpdateUser(list);
                } else if (choose.equals(exit)) {
                    new UserAdmin();
                }
            }
        }
    }

    /**
     * 增加用户信息
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void InsertUser(ArrayList<User> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入用户名：");
            String username = sc.nextLine();
            //判断用户名是否符合要求
            boolean flag1 = flag(username);
            if (flag1) {
                //符合要求
                boolean flag = getUserFlag(list, username);
                if (!flag) {
                    //输入的用户名在数据库不存在，执行增加操作
                    System.out.println("请输入密码：");
                    String pwd = sc.nextLine();
                    String sql = "insert into user(username,password) values('" + username + "','" + pwd + "')";
                    Connect.edit(sql);
                    System.out.println("添加成功！");
                    new UserAdmin();
                    break;
                } else {
                    System.out.println("输入的用户名在数据库中已经存在，请重新输入！");
                    System.out.println("是否继续执行增加用户信息操作？输入yes/no");
                    String continue1 = "yes";
                    String exit = "no";
                    String choose = sc.next();
                    if (choose.equals(continue1)) {
                        //继续执行
                        InsertUser(list);
                    } else if (choose.equals(exit)) {
                        new UserAdmin();
                    }
                }
            } else {
                //不符合要求
                System.out.println("用户名要求4-10位且全为字母，请重新输入吧");
            }

        }
    }

    /**
     * 删除用户信息
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void Delete(ArrayList<User> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入你要删除的用户id");
            int id = sc.nextInt();
            boolean flag = getResult(list, id);
            if (flag) {
                //id存在
                String sql = "delete from user where id=" + id;
                Connect.edit(sql);
                System.out.println("删除成功！");
                new UserAdmin();
                break;
            } else {
                //id不存在
                System.out.println("输入的用户id不存在，请重新输入");
                System.out.println("是否继续执行删除用户信息操作？输入yes/no");
                String continue1 = "yes";
                String exit = "no";
                String choose = sc.next();
                if (choose.equals(continue1)) {
                    //继续执行
                    Delete(list);
                } else if (choose.equals(exit)) {
                    new UserAdmin();
                }
            }
        }

    }

    /**
     * 查看所有用户信息
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void LookUser(ArrayList<User> list) throws SQLException, ClassNotFoundException {
        //遍历用户集合
        System.out.println("用户信息如下");
        list.forEach(new Consumer<User>() {
            @Override
            public void accept(User user) {
                System.out.println(user.getId() + " " + user.getUsername() + " " + user.getPassword());
            }
        });
        new UserAdmin();
    }

    /**
     * 在删除用户信息的时候，判断用户输入的id是否存在
     *
     * @param list
     * @param id
     * @return
     */
    public boolean getResult(ArrayList<User> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                //存在
                return true;
            }
        }
        //不存在
        return false;
    }

    /**
     * 查询输入的用户名是否存在
     *
     * @param list
     * @param username
     * @return
     */
    private boolean getUserFlag(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            if (username.equals(list.get(i).getUsername())) {
                //用户名存在
                return true;
            }
        }
        //不存在
        return false;
    }

    /**
     * 最少4位，最多10位
     *
     * @param username 用户名
     * @return 布尔值
     */
    public boolean flag(String username) {
        //只能为字母，最少4位，最多10位
        String regex = "[a-zA-Z]{4,10}";
        return username.matches(regex);
    }

    /**
     * 最少6位，最多15位
     *
     * @param password 密码
     * @return 布尔值
     */
    public boolean pwdFlag(String password) {
        //只能为数字和字母，最少6位，最多15位
        String regex = "[a-zA-Z0-9]{6,15}";
        return password.matches(regex);
    }
}