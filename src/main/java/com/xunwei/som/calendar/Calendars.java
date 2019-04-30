package com.xunwei.som.calendar;

import java.util.Date;

public class Calendars {
	//日期
    private Date calendar;
    //是否是节假日
    private Integer sign;

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }
}