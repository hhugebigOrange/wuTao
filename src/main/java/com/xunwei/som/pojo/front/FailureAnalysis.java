package com.xunwei.som.pojo.front;

import java.util.Date;

public class FailureAnalysis {

	private String custName;
	
	private String servicrArea;
	
	private String machCode;
	
	private String devName;
	
	private String faultType;
	
	 private String downTime;//停机时间
	 
	 private Date repairTime; //创建时间

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getServicrArea() {
		return servicrArea;
	}

	public void setServicrArea(String servicrArea) {
		this.servicrArea = servicrArea;
	}

	public String getMachCode() {
		return machCode;
	}

	public void setMachCode(String machCode) {
		this.machCode = machCode;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getFaultType() {
		return faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public Date getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}

	public FailureAnalysis(String custName, String servicrArea, String machCode, String devName, String faultType,
			String downTime, Date repairTime) {
		super();
		this.custName = custName;
		this.servicrArea = servicrArea;
		this.machCode = machCode;
		this.devName = devName;
		this.faultType = faultType;
		this.downTime = downTime;
		this.repairTime = repairTime;
	}

	public FailureAnalysis() {
		super();
	}
	 
}
