package com.java.main.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * 验证码工具类
 */
public class CodeUtil {
    public static String getCode() {
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char) ('a' + i));
        }
        for (int i = 0; i < 26; i++) {
            list.add((char) ('A' + i));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = new Random().nextInt(list.size());
            sb.append(list.get(index));
        }
        for (int i = 0; i < 2; i++) {
            int number = new Random().nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }
}
