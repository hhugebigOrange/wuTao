package com.xunwei.som.pojo;

import java.util.Date;

public class Contract implements Comparable<Contract>{
	
	private Integer id;
	
    private String contractNo;

    private String mainService;

    private String childService;

    private String custName;
    
    private Integer custId;

    private Date startDate;

    private Date endDate;

    private Double contractDeadline;

    private String contractType;

    private String signingCompany;

    private String custLinkman;

    private String linkmanPhone;

    private String openingBank;

    private String bankAccount;

    private String taxIden;

    private String address;

    private String assetAscription;

    private Date kpi1;

    private Date kpi2;

    private String kpi3;

    private Date regTime;

    private String handlingDepartment;

    private String agent;

    private String contractHoldman;
    
    private Integer dueDays;
    
    private String contractNature;
    
    public String getContractNature() {
		return contractNature;
	}

	public void setContractNature(String contractNature) {
		this.contractNature = contractNature;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getDueDays() {
		return dueDays;
	}

	public void setDueDays(Integer dueDays) {
		this.dueDays = dueDays;
	}

	public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public String getMainService() {
        return mainService;
    }

    public void setMainService(String mainService) {
        this.mainService = mainService == null ? null : mainService.trim();
    }

    public String getChildService() {
        return childService;
    }

    public void setChildService(String childService) {
        this.childService = childService == null ? null : childService.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getContractDeadline() {
        return contractDeadline;
    }

    public void setContractDeadline(Double contractDeadline) {
        this.contractDeadline = contractDeadline;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType == null ? null : contractType.trim();
    }

    public String getSigningCompany() {
        return signingCompany;
    }

    public void setSigningCompany(String signingCompany) {
        this.signingCompany = signingCompany == null ? null : signingCompany.trim();
    }

    public String getCustLinkman() {
        return custLinkman;
    }

    public void setCustLinkman(String custLinkman) {
        this.custLinkman = custLinkman == null ? null : custLinkman.trim();
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone == null ? null : linkmanPhone.trim();
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank == null ? null : openingBank.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getTaxIden() {
        return taxIden;
    }

    public void setTaxIden(String taxIden) {
        this.taxIden = taxIden == null ? null : taxIden.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAssetAscription() {
        return assetAscription;
    }

    public void setAssetAscription(String assetAscription) {
        this.assetAscription = assetAscription == null ? null : assetAscription.trim();
    }

    public Date getKpi1() {
        return kpi1;
    }

    public void setKpi1(Date kpi1) {
        this.kpi1 = kpi1;
    }

    public Date getKpi2() {
        return kpi2;
    }

    public void setKpi2(Date kpi2) {
        this.kpi2 = kpi2;
    }

    public String getKpi3() {
        return kpi3;
    }

    public void setKpi3(String kpi3) {
        this.kpi3 = kpi3 == null ? null : kpi3.trim();
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getHandlingDepartment() {
        return handlingDepartment;
    }

    public void setHandlingDepartment(String handlingDepartment) {
        this.handlingDepartment = handlingDepartment == null ? null : handlingDepartment.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getContractHoldman() {
        return contractHoldman;
    }

    public void setContractHoldman(String contractHoldman) {
        this.contractHoldman = contractHoldman == null ? null : contractHoldman.trim();
    }

	public Contract(String contractNo, String mainService, String childService, String custName, Date startDate,
			Date endDate, Double contractDeadline, String contractType, String signingCompany, String custLinkman,
			String linkmanPhone, String openingBank, String bankAccount, String taxIden, String address,
			String assetAscription, Date regTime, String handlingDepartment, String agent, String contractHoldman) {
		super();
		this.contractNo = contractNo;
		this.mainService = mainService;
		this.childService = childService;
		this.custName = custName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractDeadline = contractDeadline;
		this.contractType = contractType;
		this.signingCompany = signingCompany;
		this.custLinkman = custLinkman;
		this.linkmanPhone = linkmanPhone;
		this.openingBank = openingBank;
		this.bankAccount = bankAccount;
		this.taxIden = taxIden;
		this.address = address;
		this.assetAscription = assetAscription;
		this.regTime = regTime;
		this.handlingDepartment = handlingDepartment;
		this.agent = agent;
		this.contractHoldman = contractHoldman;
	}

	public Contract(String contractNo, String mainService, String childService, String custName, Date startDate,
			Date endDate, String contractType, String signingCompany, String custLinkman,
			String linkmanPhone, String openingBank, String bankAccount, String taxIden, String address,
			String assetAscription, Date kpi1, Date kpi2, String kpi3, Date regTime, String handlingDepartment,
			String agent, String contractHoldman) {
		super();
		this.contractNo = contractNo;
		this.mainService = mainService;
		this.childService = childService;
		this.custName = custName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractType = contractType;
		this.signingCompany = signingCompany;
		this.custLinkman = custLinkman;
		this.linkmanPhone = linkmanPhone;
		this.openingBank = openingBank;
		this.bankAccount = bankAccount;
		this.taxIden = taxIden;
		this.address = address;
		this.assetAscription = assetAscription;
		this.kpi1 = kpi1;
		this.kpi2 = kpi2;
		this.kpi3 = kpi3;
		this.regTime = regTime;
		this.handlingDepartment = handlingDepartment;
		this.agent = agent;
		this.contractHoldman = contractHoldman;
	}

	public Contract() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Contract [contractNo=" + contractNo + ", mainService=" + mainService + ", childService=" + childService
				+ ", custName=" + custName + ", startDate=" + startDate + ", endDate=" + endDate + ", contractDeadline="
				+ contractDeadline + ", contractType=" + contractType + ", signingCompany=" + signingCompany
				+ ", custLinkman=" + custLinkman + ", linkmanPhone=" + linkmanPhone + ", openingBank=" + openingBank
				+ ", bankAccount=" + bankAccount + ", taxIden=" + taxIden + ", address=" + address
				+ ", assetAscription=" + assetAscription + ", kpi1=" + kpi1 + ", kpi2=" + kpi2 + ", kpi3=" + kpi3
				+ ", regTime=" + regTime + ", handlingDepartment=" + handlingDepartment + ", agent=" + agent
				+ ", contractHoldman=" + contractHoldman + "]";
	}

	@Override
	public int compareTo(Contract o) {
		if(this.dueDays>o.getDueDays()){
			return 1;
		}else{
			return -1;
		}
	}
}