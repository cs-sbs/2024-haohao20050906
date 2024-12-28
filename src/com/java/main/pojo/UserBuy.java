package com.java.main.pojo;

/**
 * 购买记录实体类
 */
public class UserBuy {
    private String name;
    private String libraryName;
    private String buyTime;

    public UserBuy() {
    }

    public UserBuy(String name, String libraryName, String buyTime) {
        this.name = name;
        this.libraryName = libraryName;
        this.buyTime = buyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    @Override
    public String toString() {
        return "UserBuy{" +
                "name='" + name + '\'' +
                ", libraryName='" + libraryName + '\'' +
                ", buyTime='" + buyTime + '\'' +
                '}';
    }
}
