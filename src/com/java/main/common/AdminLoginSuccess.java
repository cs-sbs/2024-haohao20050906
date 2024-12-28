package com.java.main.common;

import java.util.Scanner;

/**
 * 管理员登录成功
 */
public class AdminLoginSuccess {
    public AdminLoginSuccess() {
        Scanner sc = new Scanner(System.in);
        System.out.println("******************请选择***********************");
        System.out.println("******************1-用户管理***********************");
        System.out.println("******************2-图书管理***********************");
        System.out.print("请输入：");
        int num = sc.nextInt();
        try {
            switch (num) {
                case 1:
                    new UserAdmin();
                    break;
                case 2:
                    new LibraryAdmin();
                default:
                    System.out.println("没有这个选择");
                    new AdminLoginSuccess();
                    break;
            }
        } catch (Exception e) {
            System.out.println("请输入数字");
            new AdminLoginSuccess();
        }
    }
}