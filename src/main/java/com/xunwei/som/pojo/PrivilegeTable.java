package com.xunwei.som.pojo;

public class PrivilegeTable {
    private Integer userId;

    private String userName;

    private String passwd;

    private Integer priNumber;
    
    private String custName;
    
    public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public Integer getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(Integer priNumber) {
        this.priNumber = priNumber;
    }

	@Override
	public String toString() {
		return "privilegeTable [userId=" + userId + ", userName=" + userName + ", passwd=" + passwd + ", priNumber="
				+ priNumber + "]";
	}
    
}