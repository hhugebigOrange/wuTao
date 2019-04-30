package com.xunwei.som.pojo.front;

import java.util.Date;

public class FailureAnalysisHistory {

private String custName;
	
	private String woNumber;
	
	private String devName;
	
	private String machCode;
	
	private String repairType;
	
	private String esNumber;
	
	private String unitType;
	
	private String location;
	
	private String repairMan;
	
	private String repairPhone;
	
	private Date repairTime;
	
	private String enginnerName;
	
	private String faultType;
	
	private String accidentType;
	
	private String mainTenanceFeedback;
	
	private Date probSolve;
	
	private String treatmentMeasure;
	
	private String treatmentState;
	
	private String faultNo;
	
	public String getFaultNo() {
		return faultNo;
	}

	public void setFaultNo(String faultNo) {
		this.faultNo = faultNo;
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

	public String getMainTenanceFeedback() {
		return mainTenanceFeedback;
	}

	public void setMainTenanceFeedback(String mainTenanceFeedback) {
		this.mainTenanceFeedback = mainTenanceFeedback;
	}

	public Date getProbSolve() {
		return probSolve;
	}

	public void setProbSolve(Date probSolve) {
		this.probSolve = probSolve;
	}

	public String getTreatmentMeasure() {
		return treatmentMeasure;
	}

	public void setTreatmentMeasure(String treatmentMeasure) {
		this.treatmentMeasure = treatmentMeasure;
	}

	public String getTreatmentState() {
		return treatmentState;
	}

	public void setTreatmentState(String treatmentState) {
		this.treatmentState = treatmentState;
	}

	public FailureAnalysisHistory(String custName, String woNumber, String devName, String machCode, String repairType,
			String esNumber, String unitType, String location, String repairMan, String repairPhone, Date repairTime,
			String enginnerName, String faultType, String accidentType, String mainTenanceFeedback, Date probSolve,
			String treatmentMeasure, String treatmentState) {
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
		this.mainTenanceFeedback = mainTenanceFeedback;
		this.probSolve = probSolve;
		this.treatmentMeasure = treatmentMeasure;
		this.treatmentState = treatmentState;
	}

	public FailureAnalysisHistory() {
		super();
	}
	
}
