package com.java.main.common;

import com.java.main.pojo.UserBuy;
import com.java.main.pojo.Library;
import com.java.main.utils.Connect;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 用户进行图书购买
 */
public class UserBuyLibrary {
    private static String name;

    public UserBuyLibrary(String name) {
        this.name = name;
        System.out.println("欢迎来到图书商城");
        System.out.println("当前的登录用户:" + name);
    }

    public static void main() throws SQLException, ClassNotFoundException {
        //创建图书集合
        ArrayList<Library> list = new ArrayList<>();
        //用户购买记录
        ArrayList<UserBuy> buyList = new ArrayList<>();
        //连接数据库 将图书信息添加到集合中
        ResultSet rs1 = Connect.queryAll("select * from library");
        while (rs1.next()) {
            Integer id = rs1.getInt("id");
            String libraryName = rs1.getString("name");
            String authorName = rs1.getString("author");
            Double price = rs1.getDouble("price");
            Integer count = rs1.getInt("count");
            //获取上架时间
            Timestamp shelf_time = rs1.getTimestamp("Shelf_time");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(shelf_time);
            list.add(new Library(id, libraryName, authorName, price, count, time));
        }
        rs1.close();
        //获取用户购买记录
        String sql = "select * from buy_table";
        ResultSet rs2 = Connect.queryAll(sql);
        while (rs2.next()) {
            Timestamp buyTime = rs2.getTimestamp("buy_time");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(buyTime);
            buyList.add(new UserBuy(rs2.getString("name"), rs2.getString("library"), time));
        }
        rs2.close();
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入你的选择 1.查看所有图书信息 2.购买书籍 3.查询购买记录 4.关闭系统");
        try {
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    LookForLibraryInfo(list);
                    break;
                case 2:
                    BuyBook(list);
                    break;
                case 3:
                    FindBuy(buyList);
                    break;
                case 4:
                    System.exit(WindowConstants.EXIT_ON_CLOSE);
                    break;
                default:
                    System.out.println("没有这个选项");
                    main();
                    break;
            }
        } catch (Exception e) {
            System.out.println("请输入数字，不能输入其它字符~");
            main();
        }
    }

    //查询当前用户购买的图书信息
    private static void FindBuy(ArrayList<UserBuy> buyList) throws SQLException, ClassNotFoundException {
        System.out.println("购买记录如下所示：");
        int index = getBuyIndex(buyList, name);
        if (index >= 0) {
            buyList.stream().forEach(userBuy -> System.out.println("用户" + userBuy.getName() + "于" + userBuy.getBuyTime() + "购买了" + userBuy.getLibraryName()));
        } else {
            System.out.println("您还没有购买任何书籍");
        }
        main();
    }

    //购买书籍
    private static void BuyBook(ArrayList<Library> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入你要购买的图书名字");
            String libraryName = sc.next();
            int index = getIndex(list, libraryName);
            if (index >= 0) {
                //图书存在
                //判断图书数量是否大于0
                Integer count = list.get(index).getCount();
                if (count > 0) {
                    //图书有剩余容量
                    System.out.println("请输入购买数量：");
                    Integer amount = sc.nextInt();
                    //判断购买数量是否在图书库存范围内
                    if (amount > 0 && amount <= list.get(index).getCount()) {
                        //在库存范围内
                        //获取剩余书籍数量
                        Integer remainingCount = list.get(index).getCount() - amount;
                        //更新数据库
                        String sql = "update library set count='" + remainingCount + "'where name='" + libraryName + "'";
                        Connect.edit(sql);
                        System.out.println("购买成功！");
                        //获取当前购买时间
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String datetime = sdf.format(d);
                        //将用户名和图书名字插入数据库中
                        Connect.edit("insert into buy_table values ('" + name + "','" + libraryName + "','" + datetime + "')");
                        main();
                        break;
                    } else {
                        System.out.println("超出库存范围，请重新输入~");
                    }
                } else {
                    //图书已经卖完了
                    System.out.println("很抱歉，图书已经卖完了");
                    System.out.println("是否继续执行图书购买操作？输入yes/no");
                    String continue1 = "yes";
                    String exit = "no";
                    String choose = sc.next();
                    if (choose.equals(continue1)) {
                        //继续购买书籍
                        BuyBook(list);
                    } else if (choose.equals(exit)) {
                        UserBuyLibrary.main();
                    }
                }
            } else {
                System.out.println("不存在该书籍！请重新输入~");
            }
        }
    }

    //查询书籍
    private static void LookForLibraryInfo(ArrayList<Library> list) throws SQLException, ClassNotFoundException {
        System.out.println("图书信息如下：");
        list.stream().forEach(new Consumer<Library>() {
            @Override
            public void accept(Library library) {
                System.out.println("图书编号：" + library.getId() + " 图书名字： 《" + library.getName() + "》 图书作者：" + library.getAuthor()
                        + " 图书价格：" + library.getPrice() + " 图书库存" + library.getCount() + " 图书上架时间：" + library.getTime());
            }
        });
        main();
    }

    //判断图书名字在集合在是否存在
    public static int getIndex(ArrayList<Library> list, String name) {
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    //判断用户在是否存在在购买集合中
    private static int getBuyIndex(ArrayList<UserBuy> buyList, String name) {
        for (int i = 0; i < buyList.size(); i++) {
            if (name.equals(buyList.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }
}