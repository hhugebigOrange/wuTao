package com.xunwei.som.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerFeedback {
	
    private String custName;

    private String id;

    private String custPhone;

   @JsonFormat(pattern="yyyy-MM-dd")
    private Date feedbackTime;
    
    private String content;
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone == null ? null : custPhone.trim();
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

	public CustomerFeedback(String custName, String id, String custPhone, Date feedbackTime, String content) {
		super();
		this.custName = custName;
		this.id = id;
		this.custPhone = custPhone;
		this.feedbackTime = feedbackTime;
		this.content = content;
	}
	
	public CustomerFeedback() {
		super();
	}
    
}