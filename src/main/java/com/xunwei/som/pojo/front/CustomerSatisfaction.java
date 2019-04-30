package com.xunwei.som.pojo.front;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerSatisfaction {

	private String custName;
	
	private String serviceArea;
	
	private String woNumber;
	
	private String repairMan;
	
	private String repairPhone;
	
	@JsonFormat(pattern="YYYY-MM-DD HH:mm:ss")
	private Date repairTime;
	@JsonFormat(pattern="YYYY-MM-DD HH:mm:ss")
	private Date completeTime;
	
	private String enginnerName;
	
	private Integer custScore;
	
	private String custEva;
	
	private String remake;

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getWoNumber() {
		return woNumber;
	}

	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}

	public String getRepairMan() {
		return repairMan;
	}

	public void setRepairMan(String repairMan) {
		this.repairMan = repairMan;
	}

	public String getRepairPhone() {
		return repairPhone;
	}

	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}

	public Date getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getEnginnerName() {
		return enginnerName;
	}

	public void setEnginnerName(String enginnerName) {
		this.enginnerName = enginnerName;
	}

	public Integer getCustScore() {
		return custScore;
	}

	public void setCustScore(Integer custScore) {
		this.custScore = custScore;
	}

	public String getCustEva() {
		return custEva;
	}

	public void setCustEva(String custEva) {
		this.custEva = custEva;
	}

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}

	public CustomerSatisfaction(String custName, String serviceArea, String woNumber, String repairMan,
			String repairPhone, Date repairTime, Date completeTime, String enginnerName, Integer custScore,
			String custEva, String remake) {
		super();
		this.custName = custName;
		this.serviceArea = serviceArea;
		this.woNumber = woNumber;
		this.repairMan = repairMan;
		this.repairPhone = repairPhone;
		this.repairTime = repairTime;
		this.completeTime = completeTime;
		this.enginnerName = enginnerName;
		this.custScore = custScore;
		this.custEva = custEva;
		this.remake = remake;
	}

	public CustomerSatisfaction() {
		super();
	}
	
	
}
