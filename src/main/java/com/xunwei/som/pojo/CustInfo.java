package com.xunwei.som.pojo;

import java.util.Date;

/**
 * 客户属性类
 * @author Administrator
 *
 */
public class CustInfo {
	
    //客户id
    private Integer custId;

    //客户名称
    private String custName;

    //客户地址
    private String custAddr;

    //客户电话
    private String custCall;

    //联系人
    private String linkman;

    //联系人电话
    private String phone;
    
    //查看用户是否显示
    private String display;
    
    //上班时间
    private Date workTime;
    
    //下班时间
    private Date offWorkTime;
    
    private String signComp;
    
    public String getSignComp() {
		return signComp;
	}

	public void setSignComp(String signComp) {
		this.signComp = signComp;
	}

	public Date getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Date workTime) {
		this.workTime = workTime;
	}

	public Date getOffWorkTime() {
		return offWorkTime;
	}

	public void setOffWorkTime(Date offWorkTime) {
		this.offWorkTime = offWorkTime;
	}

	public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getCustAddr() {
        return custAddr;
    }

    public void setCustAddr(String custAddr) {
        this.custAddr = custAddr == null ? null : custAddr.trim();
    }

    public String getCustCall() {
        return custCall;
    }

    public void setCustCall(String custCall) {
        this.custCall = custCall;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


	public CustInfo(String custName, String custAddr, String linkman, String phone, Date workTime, Date offWorkTime) {
		super();
		this.custName = custName;
		this.custAddr = custAddr;
		this.linkman = linkman;
		this.phone = phone;
		this.workTime = workTime;
		this.offWorkTime = offWorkTime;
	}

	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}

	public CustInfo(Integer custId, String display) {
		super();
		this.custId = custId;
		this.display = display;
	}
	
	@Override
	public String toString() {
		return "CustInfo [custId=" + custId + ", custName=" + custName + ", custAddr=" + custAddr + ", custCall="
				+ custCall + ", linkman=" + linkman + ", phone=" + phone + ", display=" + display + ", workTime="
				+ workTime + ", offWorkTime=" + offWorkTime + ", signComp=" + signComp + "]";
	}

	public CustInfo() {
		super();
	}
	
}