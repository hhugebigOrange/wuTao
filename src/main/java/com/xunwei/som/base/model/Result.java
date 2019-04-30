package com.xunwei.som.base.model;

import java.io.Serializable;

/**
 * 响应结果
 * @author linzg
 *
 */
@SuppressWarnings("serial")
public class Result implements Serializable {
    private int code = 0;
    private String message;

    public Result(){}

    public Result(int code,String message){
        this.code = code;
        this.message = message;
    }

    //region getter and setter

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    //endregion
}
