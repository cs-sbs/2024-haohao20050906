package com.java.main.pojo;

/**
 * 图书实体类
 */
public class Library {
    private Integer id;
    private String name;
    private String author;
    private Double price;
    private Integer count;
    private String time;

    public Library() {
    }

    public Library(Integer id, String name, String author, Double price, Integer count, String time) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.count = count;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", time='" + time + '\'' +
                '}';
    }
}
