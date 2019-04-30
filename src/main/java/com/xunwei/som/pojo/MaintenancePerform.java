package com.xunwei.som.pojo;

import java.util.Date;

public class MaintenancePerform {
    private String custName;

    private String compName;

    private String contractCode;

    private String machCode;

    private Date completeTime;

    private String mainFrequency;

    private Integer maintenanceState;

    private String bwReader;

    private String coReader;

    private String materialModel;

    private Integer materialNumber;

    private String responsibleEngineer;

    private String responsibleId;

    private String reserveEnginner;

    private String remark;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName == null ? null : compName.trim();
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getMachCode() {
        return machCode;
    }

    public void setMachCode(String machCode) {
        this.machCode = machCode == null ? null : machCode.trim();
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getMainFrequency() {
        return mainFrequency;
    }

    public void setMainFrequency(String mainFrequency) {
        this.mainFrequency = mainFrequency == null ? null : mainFrequency.trim();
    }

    public Integer getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(Integer maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public String getBwReader() {
        return bwReader;
    }

    public void setBwReader(String bwReader) {
        this.bwReader = bwReader == null ? null : bwReader.trim();
    }

    public String getCoReader() {
        return coReader;
    }

    public void setCoReader(String coReader) {
        this.coReader = coReader == null ? null : coReader.trim();
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel == null ? null : materialModel.trim();
    }

    public Integer getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(Integer materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getResponsibleEngineer() {
        return responsibleEngineer;
    }

    public void setResponsibleEngineer(String responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer == null ? null : responsibleEngineer.trim();
    }

    public String getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(String responsibleId) {
        this.responsibleId = responsibleId;
    }

    public String getReserveEnginner() {
        return reserveEnginner;
    }

    public void setReserveEnginner(String reserveEnginner) {
        this.reserveEnginner = reserveEnginner == null ? null : reserveEnginner.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}