package com.xunwei.som.calendar;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Calendars {
	//日期
	 @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date calendar;
    //是否是节假日
    private Integer sign;
    
    private String remark;
    
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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