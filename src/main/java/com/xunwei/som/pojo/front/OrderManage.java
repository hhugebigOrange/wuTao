package com.xunwei.som.pojo.front;

import java.util.Date;
import java.util.List;

public class OrderManage {
	
	private String serviceArea;

	private String custName;
	
	private String custAddr;
	
	private String woNumber;
	
	private String devName;
	
	private String machCode;
	
	private String repairType;
	
	private String esNumber;
	
	private String unitType;
	
	private String department;
	
	private String describe;
	
	private String faultClass;
	
	private String faultNo;
	
	private String location;
	
	private String repairMan;
	
	private String repairPhone;
	
	private String enginnerName;
	
	private String enginnerId;
	
	private String faultType;
	
	private String accidentType;
	
	private String part;
	
	private String serviceType;
	
	private String[] partMessage;
	
	private Picture picture;
	
	private String mainTenanceFeedback;
	
	private String woState;
	
	private String woProgress;
	
	private String turnOrderReson;
	
	private String enginnerPhone;
	
	private List<String> process;
	
	private String Rejected;
	
	private String bwReader;
	
	private String coReader;
	
	private String reserveEngineer;
	
	private String treatmentMeasure;
	
	private Date arrTime;
	
	private String orderReason;
	
	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getOrderReason() {
		return orderReason;
	}

	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}

	public Date getArrTime() {
		return arrTime;
	}

	public void setArrTime(Date arrTime) {
		this.arrTime = arrTime;
	}

	public String getTreatmentMeasure() {
		return treatmentMeasure;
	}

	public void setTreatmentMeasure(String treatmentMeasure) {
		this.treatmentMeasure = treatmentMeasure;
	}

	public String getReserveEngineer() {
		return reserveEngineer;
	}

	public void setReserveEngineer(String reserveEngineer) {
		this.reserveEngineer = reserveEngineer;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	/*@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")*/
	private Date repairTime;
	
	/*@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")*/
	private Date currentTime;
	
	/*@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")*/
	private Date getOrderTime;
	
	/*@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")*/
	private Date probSolveTime;
	
	/*@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")*/
	private Date sendTime;
	
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getGetOrderTime() {
		return getOrderTime;
	}

	public void setGetOrderTime(Date getOrderTime) {
		this.getOrderTime = getOrderTime;
	}

	public Date getProbSolveTime() {
		return probSolveTime;
	}

	public void setProbSolveTime(Date probSolveTime) {
		this.probSolveTime = probSolveTime;
	}

	public String getFaultClass() {
		return faultClass;
	}

	public void setFaultClass(String faultClass) {
		this.faultClass = faultClass;
	}

	public String getRejected() {
		return Rejected;
	}

	public void setRejected(String rejected) {
		Rejected = rejected;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public List<String> getProcess() {
		return process;
	}

	public void setProcess(List<String> process) {
		this.process = process;
	}

	public String getEnginnerPhone() {
		return enginnerPhone;
	}

	public void setEnginnerPhone(String enginnerPhone) {
		this.enginnerPhone = enginnerPhone;
	}

	public String getWoProgress() {
		return woProgress;
	}

	public void setWoProgress(String woProgress) {
		this.woProgress = woProgress;
	}

	public String getTurnOrderReson() {
		return turnOrderReson;
	}

	public void setTurnOrderReson(String turnOrderReson) {
		this.turnOrderReson = turnOrderReson;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getFaultNo() {
		return faultNo;
	}

	public void setFaultNo(String faultNo) {
		this.faultNo = faultNo;
	}

	public String[] getPartMessage() {
		return partMessage;
	}

	public void setPartMessage(String[] partMessage) {
		this.partMessage = partMessage;
	}

	public String getCustAddr() {
		return custAddr;
	}

	public void setCustAddr(String custAddr) {
		this.custAddr = custAddr;
	}

	public String getEnginnerId() {
		return enginnerId;
	}

	public void setEnginnerId(String enginnerId) {
		this.enginnerId = enginnerId;
	}

	public String getWoState() {
		return woState;
	}

	public void setWoState(String woState) {
		this.woState = woState;
	}

	public String getMainTenanceFeedback() {
		return mainTenanceFeedback;
	}

	public void setMainTenanceFeedback(String mainTenanceFeedback) {
		this.mainTenanceFeedback = mainTenanceFeedback;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getWoNumber() {
		return woNumber;
	}

	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getMachCode() {
		return machCode;
	}

	public void setMachCode(String machCode) {
		this.machCode = machCode;
	}

	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public String getEsNumber() {
		return esNumber;
	}

	public void setEsNumber(String esNumber) {
		this.esNumber = esNumber;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getEnginnerName() {
		return enginnerName;
	}

	public void setEnginnerName(String enginnerName) {
		this.enginnerName = enginnerName;
	}

	public String getFaultType() {
		return faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public OrderManage(String custName, String woNumber, String devName, String machCode, String repairType,
			String esNumber, String unitType, String location, String repairMan, String repairPhone, Date repairTime,
			String enginnerName, String faultType, String accidentType, String part,String mainTenanceFeedback) {
		super();
		this.custName = custName;
		this.woNumber = woNumber;
		this.devName = devName;
		this.machCode = machCode;
		this.repairType = repairType;
		this.esNumber = esNumber;
		this.unitType = unitType;
		this.location = location;
		this.repairMan = repairMan;
		this.repairPhone = repairPhone;
		this.repairTime = repairTime;
		this.enginnerName = enginnerName;
		this.faultType = faultType;
		this.accidentType = accidentType;
		this.part = part;
		this.mainTenanceFeedback=mainTenanceFeedback;
	}

	public OrderManage() {
		super();
	}
	
}
