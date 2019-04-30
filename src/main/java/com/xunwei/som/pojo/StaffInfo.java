package com.xunwei.som.pojo;

import java.util.Date;

public class StaffInfo {
	
	/**
	 * 员工编码
	 */
    private String staffId;  
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 员工电话
     */
    private String phone;
    /**
     * 员工公司编码
     */
    private Integer compNumber;
    /**
     * 员工公司名字
     */
    private String compName;
    /**
     * 员工岗位
     */
    private String post;
    /**
     * 工作状态
     */
    private String workCond;
    /**
     * 员工是否离职
     */
    private String status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 员工入职日期
     */
    private Date createDate;
    
    private Date startDate;
    
    private Date endDate;
    
    private String reson;
    //涉密资质
    private String secret;
    //涉密等级
    private String secretLevel;
    
    private String secretClass;
    
	public String getSecretClass() {
		return secretClass;
	}

	public void setSecretClass(String secretClass) {
		this.secretClass = secretClass;
	}

	//是否在前端页面显示
    private Integer display;
    
    public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(Integer compNumber) {
        this.compNumber = compNumber;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName == null ? null : compName.trim();
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    public String getWorkCond() {
        return workCond;
    }

    public void setWorkCond(String workCond) {
        this.workCond = workCond == null ? null : workCond.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
	public StaffInfo(String name, String phone, Integer compNumber, String compName, String post, Date createDate) {
		super();
		this.name = name;
		this.phone = phone;
		this.compNumber = compNumber;
		this.compName = compName;
		this.post = post;
		this.createDate = createDate;
	}

	public StaffInfo() {
		super();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReson() {
		return reson;
	}

	public void setReson(String reson) {
		this.reson = reson;
	}

	@Override
	public String toString() {
		return "staffInfo [staffId=" + staffId + ", name=" + name + ", phone=" + phone + ", compNumber=" + compNumber
				+ ", compName=" + compName + ", post=" + post + ", workCond=" + workCond + ", status=" + status
				+ ", remark=" + remark + ", createDate=" + createDate + "]";
	}
    
    
}