package com.xunwei.som.pojo;

import java.util.Date;

public class ContractInfo {
    private String contractCode;

    private Date regTime;

    private String tranDep;

    private String respPerson;

    private String custContract;

    private String contractName;

    private String custCompName;

    private Date contractDate;

    private Date daysDue;

    private String custName;

    private Integer custId;

    private Integer staffId;

    private Double amouOfContract;

    private String contractType;

    private String contractChar;

    private String remark;
    
    private Integer contractDeadline;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getTranDep() {
        return tranDep;
    }

    public void setTranDep(String tranDep) {
        this.tranDep = tranDep == null ? null : tranDep.trim();
    }

    public String getRespPerson() {
        return respPerson;
    }

    public void setRespPerson(String respPerson) {
        this.respPerson = respPerson == null ? null : respPerson.trim();
    }

    public String getCustContract() {
        return custContract;
    }

    public void setCustContract(String custContract) {
        this.custContract = custContract == null ? null : custContract.trim();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getCustCompName() {
        return custCompName;
    }

    public void setCustCompName(String custCompName) {
        this.custCompName = custCompName == null ? null : custCompName.trim();
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getDaysDue() {
        return daysDue;
    }

    public void setDaysDue(Date daysDue) {
        this.daysDue = daysDue;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Double getAmouOfContract() {
        return amouOfContract;
    }

    public void setAmouOfContract(Double amouOfContract) {
        this.amouOfContract = amouOfContract;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType == null ? null : contractType.trim();
    }

    public String getContractChar() {
        return contractChar;
    }

    public void setContractChar(String contractChar) {
        this.contractChar = contractChar == null ? null : contractChar.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public Integer getContractDeadline() {
		return contractDeadline;
	}

	public void setContractDeadline(Integer contractDeadline) {
		this.contractDeadline = contractDeadline;
	}
    
    
}