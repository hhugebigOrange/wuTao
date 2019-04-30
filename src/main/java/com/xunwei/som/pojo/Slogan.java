package com.xunwei.som.pojo;

/**
 * 口号
 * @author Administrator
 *
 */
public class Slogan {
    private String slogan;

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan == null ? null : slogan.trim();
    }
}