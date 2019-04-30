package com.xunwei.som.pojo;

import java.util.Date;

public class DeviceInfo {
    private String machCode;

    private String esNumber;

    private String unitType;

    private String devName;

    private String manufa;

    private String devStatus;

    private String eqLocation;

    private Date insDate;

    private Integer serYear;

    private Integer compNumber;

    private String assetAttr;

    private String remark;

    public String getMachCode() {
        return machCode;
    }

    public void setMachCode(String machCode) {
        this.machCode = machCode == null ? null : machCode.trim();
    }

    public String getEsNumber() {
        return esNumber;
    }

    public void setEsNumber(String esNumber) {
        this.esNumber = esNumber == null ? null : esNumber.trim();
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType == null ? null : unitType.trim();
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName == null ? null : devName.trim();
    }

    public String getManufa() {
        return manufa;
    }

    public void setManufa(String manufa) {
        this.manufa = manufa == null ? null : manufa.trim();
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus == null ? null : devStatus.trim();
    }

    public String getEqLocation() {
        return eqLocation;
    }

    public void setEqLocation(String eqLocation) {
        this.eqLocation = eqLocation == null ? null : eqLocation.trim();
    }

    public Date getInsDate() {
        return insDate;
    }

    public void setInsDate(Date insDate) {
        this.insDate = insDate;
    }

    public Integer getSerYear() {
        return serYear;
    }

    public void setSerYear(Integer serYear) {
        this.serYear = serYear;
    }

    public Integer getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(Integer compNumber) {
        this.compNumber = compNumber;
    }

    public String getAssetAttr() {
        return assetAttr;
    }

    public void setAssetAttr(String assetAttr) {
        this.assetAttr = assetAttr == null ? null : assetAttr.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}