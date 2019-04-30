package com.xunwei.som.pojo;

import java.util.Date;

public class DeviceChange {
    private String machCode;

    private String changeType;

    private String serviceArea;

    private String location;

    private String department;

    private String serviceLevel;

    private String responsibleEngineer;

    private String responsibleEngineerId;

    private String reserveEnginner;

    private String reserveEnginnerId;

    private String bwReader;

    private String colorReader;

    private Date installedTime;

    private String sercet;

    private String sercetLevel;
    
    private String IP;

    private Date changeTime;

    private String changeMan;

    private String changeManId;
    
    public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getMachCode() {
        return machCode;
    }

    public void setMachCode(String machCode) {
        this.machCode = machCode == null ? null : machCode.trim();
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType == null ? null : changeType.trim();
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea == null ? null : serviceArea.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getResponsibleEngineer() {
        return responsibleEngineer;
    }

    public void setResponsibleEngineer(String responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer == null ? null : responsibleEngineer.trim();
    }

    public String getResponsibleEngineerId() {
        return responsibleEngineerId;
    }

    public void setResponsibleEngineerId(String responsibleEngineerId) {
        this.responsibleEngineerId = responsibleEngineerId;
    }

    public String getReserveEnginner() {
        return reserveEnginner;
    }

    public void setReserveEnginner(String reserveEnginner) {
        this.reserveEnginner = reserveEnginner == null ? null : reserveEnginner.trim();
    }

    public String getReserveEnginnerId() {
        return reserveEnginnerId;
    }

    public void setReserveEnginnerId(String reserveEnginnerId) {
        this.reserveEnginnerId = reserveEnginnerId;
    }

    public String getBwReader() {
        return bwReader;
    }

    public void setBwReader(String bwReader) {
        this.bwReader = bwReader == null ? null : bwReader.trim();
    }

    public String getColorReader() {
        return colorReader;
    }

    public void setColorReader(String colorReader) {
        this.colorReader = colorReader == null ? null : colorReader.trim();
    }

    public Date getInstalledTime() {
        return installedTime;
    }

    public void setInstalledTime(Date installedTime) {
        this.installedTime = installedTime;
    }

    public String getSercet() {
        return sercet;
    }

    public void setSercet(String sercet) {
        this.sercet = sercet == null ? null : sercet.trim();
    }

    public String getSercetLevel() {
        return sercetLevel;
    }

    public void setSercetLevel(String sercetLevel) {
        this.sercetLevel = sercetLevel == null ? null : sercetLevel.trim();
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getChangeMan() {
        return changeMan;
    }

    public void setChangeMan(String changeMan) {
        this.changeMan = changeMan == null ? null : changeMan.trim();
    }

    public String getChangeManId() {
        return changeManId;
    }

    public void setChangeManId(String changeManId) {
        this.changeManId = changeManId;
    }
}