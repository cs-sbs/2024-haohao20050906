package com.java.main.common;

import com.java.main.pojo.Library;
import com.java.main.utils.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * 图书管理
 */
public class LibraryAdmin {
    public LibraryAdmin() throws SQLException, ClassNotFoundException {
        //获取图书库存为0的书籍
        ResultSet rs1 = Connect.queryAll("select * from library where count='0'");
        while (rs1.next()) {
            System.out.println("========================图书" + rs1.getString("name") + "的库存为0了，请增加库存！！！" +
                    "========================");
        }
        Scanner sc = new Scanner(System.in);
        //图书集合
        ArrayList<Library> list = new ArrayList<>();
        ResultSet rs2 = Connect.queryAll("select * from library");
        while (rs2.next()) {
            Integer id = rs2.getInt("id");
            String libraryName = rs2.getString("name");
            String authorName = rs2.getString("author");
            Double price = rs2.getDouble("price");
            Integer count = rs2.getInt("count");
            //获取上架时间
            Timestamp shelf_time = rs2.getTimestamp("Shelf_time");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(shelf_time);
            list.add(new Library(id, libraryName, authorName, price, count, time));
        }
        System.out.println("请输入你的选择：1查看所有图书信息 2增加图书 3修改图书库存 4删除图书 5选择用户/图书管理");
        String numStr = sc.nextLine();
        try {
            switch (Integer.parseInt(numStr)) {
                case 1:
                    LookForInfo(list);
                    break;
                case 2:
                    InsertBook(list);
                    break;
                case 3:
                    EditBook(list);
                    break;
                case 4:
                    deleteBook(list);
                    break;
                case 5:
                    new AdminLoginSuccess();
                    break;
                default:
                    System.out.println("没有这个选项~");
                    break;
            }
        } catch (Exception e) {
            System.out.println("请输入数字！不能输入其它字符~");
        }
        rs2.close();
        rs1.close();
    }


    /**
     * 修改图书库存
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void EditBook(ArrayList<Library> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要增加图书库存的图书名字：");
            String name = sc.nextLine();
            int index = getResult(list, name);
            if (index >= 0) {
                //存在
                System.out.println("请输入增加的库存数量：");
                Integer count1 = sc.nextInt();
                Integer count2 = list.get(index).getCount();
                Integer count = count1 + count2;
                String sql = "update library set count='" + count + "' where name='" + name + "'";
                Connect.edit(sql);
                System.out.println("库存添加成功！");
                new LibraryAdmin();
                break;
            } else {
                System.out.println("不存在该图书！请重新输入~");
            }
        }
    }

    /**
     * 增加图书
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void InsertBook(ArrayList<Library> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要增加的图书名：");
        String name = sc.next();
        System.out.println("请输入作者名：");
        String author = sc.next();
        System.out.println("请输入价格：");
        Double price = sc.nextDouble();
        System.out.println("请输入图书数量：");
        Integer count = sc.nextInt();
        //获取上架时间
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(d);
        String sql = " insert into library(name, author, price, count, Shelf_time) values('" + name + "','" + author + "','" +
                price + "','" + count + "','" + time + "')";
        Connect.edit(sql);
        System.out.println("添加成功！");
        new LibraryAdmin();
    }

    /**
     * 查询所有图书信息
     *
     * @param list
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void LookForInfo(ArrayList<Library> list) throws SQLException, ClassNotFoundException {
        System.out.println("图书信息如下：");
        for (Library library : list) {
            System.out.println("图书编号：" + library.getId() + " 图书名字：《" + library.getName() + " 》 图书作者：" + library.getAuthor()
                    + " 图书价格：" + library.getPrice() + " 图书库存" + library.getCount() + " 图书上架时间：" + library.getTime());
        }
        new LibraryAdmin();
    }

    /**
     * 根据图书名删除图书
     *
     * @param list
     */
    public void deleteBook(ArrayList<Library> list) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要删除的图书名字");
            String name = sc.next();
            Integer index = getResult(list, name);
            if (index >= 0) {
                //存在
                try {
                    Connect.edit("delete from library where name='" + name + "'");
                    System.out.println("删除成功");
                    new LibraryAdmin();
                    break;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                //不存在
                System.out.println("要删除的图书名字不存在，请重新输入");
                System.out.println("是否继续根据图书名删除图书操作？输入yes/no");
                String continue1 = "yes";
                String exit = "no";
                String choose = sc.next();
                if (choose.equals(continue1)) {
                    //继续执行
                    deleteBook(list);
                } else if (choose.equals(exit)) {
                    new LibraryAdmin();
                }
            }
        }
    }

    /**
     * 根据图书名判断图书是否存在
     *
     * @param list
     * @param name
     * @return
     */
    public Integer getResult(ArrayList<Library> list, String name) {
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                //存在
                return i;
            }
        }
        return -1;
    }
}