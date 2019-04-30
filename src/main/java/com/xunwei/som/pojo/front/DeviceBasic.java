package com.xunwei.som.pojo.front;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DeviceBasic {
	
	private String woNumber;
	
	private String serviceArea;
	
	private String deviceType;
	
	private String deviceBrand;
	
	private String esNumber;

	private String assetAttr;

	private String devName;

	private Integer devNumber;

	private String assetNumber;

	private String machCode;

	private String assetClass;

	private String specifications;

	private String unitType;

	private String ip;

	private String responsibleEngineerID;

	private String reserveEnginnerID;

	private String department;

	private String location;

	private String bwReader;

	private String coReader;

	private String sercet;

	private String sercetLevel;

	private String serviceLevel;
	
	private String accidentType;
	
	private String faultNo;
	
	private String remark;
	
	private String  maintenanceFeedback;
	
	private String custName;

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date installedTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date repairTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date probTime;
	
	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getMaintenanceFeedback() {
		return maintenanceFeedback;
	}

	public void setMaintenanceFeedback(String maintenanceFeedback) {
		this.maintenanceFeedback = maintenanceFeedback;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAccidentType() {
		return accidentType;
	}

	public String getFaultNo() {
		return faultNo;
	}



	public void setFaultNo(String faultNo) {
		this.faultNo = faultNo;
	}



	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getEsNumber() {
		return esNumber;
	}

	public void setEsNumber(String esNumber) {
		this.esNumber = esNumber;
	}

	public String getDeviceBrand() {
		return deviceBrand;
	}

	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}

	public String getWoNumber() {
		return woNumber;
	}

	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}

	public Date getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}

	public Date getProbTime() {
		return probTime;
	}

	public void setProbTime(Date probTime) {
		this.probTime = probTime;
	}

	public String getSercet() {
		return sercet;
	}

	public void setSercet(String sercet) {
		this.sercet = sercet;
	}

	public String getSercetLevel() {
		return sercetLevel;
	}

	public void setSercetLevel(String sercetLevel) {
		this.sercetLevel = sercetLevel;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBwReader() {
		return bwReader;
	}

	public void setBwReader(String bwReader) {
		this.bwReader = bwReader;
	}

	public String getCoReader() {
		return coReader;
	}

	public void setCoReader(String coReader) {
		this.coReader = coReader;
	}

	public Date getInstalledTime() {
		return installedTime;
	}

	public void setInstalledTime(Date installedTime) {
		this.installedTime = installedTime;
	}

	public String getResponsibleEngineerID() {
		return responsibleEngineerID;
	}

	public void setResponsibleEngineerID(String responsibleEngineerID) {
		this.responsibleEngineerID = responsibleEngineerID;
	}

	public String getReserveEnginnerID() {
		return reserveEnginnerID;
	}

	public void setReserveEnginnerID(String reserveEnginnerID) {
		this.reserveEnginnerID = reserveEnginnerID;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getAssetAttr() {
		return assetAttr;
	}

	public void setAssetAttr(String assetAttr) {
		this.assetAttr = assetAttr;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public Integer getDevNumber() {
		return devNumber;
	}

	public void setDevNumber(Integer devNumber) {
		this.devNumber = devNumber;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getMachCode() {
		return machCode;
	}

	public void setMachCode(String machCode) {
		this.machCode = machCode;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public DeviceBasic(String assetAttr, String devName, Integer devNumber, String assetNumber, String machCode,
			String assetClass, String specifications) {
		super();
		this.assetAttr = assetAttr;
		this.devName = devName;
		this.devNumber = devNumber;
		this.assetNumber = assetNumber;
		this.machCode = machCode;
		this.assetClass = assetClass;
		this.specifications = specifications;
	}

	public DeviceBasic() {
		super();
	}

}
