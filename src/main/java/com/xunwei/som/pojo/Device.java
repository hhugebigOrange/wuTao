package com.xunwei.som.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Device {
	
	private Integer Id;
	
    private String machCode;

    private String devName;

    private String deviceBrand;

    private String unitType;

    private String deviceBound;

    private String deviceType;

    private String outputSpec;

    private String esNumber;

    private String custArea;
    
    private String custAddress;

    private String serviceArea;

    private String department;

    private String serviceLevel;

    private String custLinkman;

    private String linkmanPhone;

    private String responsibleEngineer;
    
    private String responsibleEngineerID;

    private String reserveEnginner;
    
    private String reserveEnginnerID;

    private String bwReader;

    private String colorReader;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date installedTime;

    private String assetAttr;

    private String assetNumber;
    
    private String changeType;
    
    private String location;
    //停机时间
    private double downTime;
    
    //运转时间
    private double workTime;
    
    //合同编码
    private String contractNo;
    //保管部门
    private String holdDepartment;
    //保管人
    private String holdMan;
    //是否涉密
    private String secret;
    //涉密等级
    private String secretLevel;
    //资产类别
    private String assetClass;
    //规格型号
    private String specifications;
    //设备正常运转率
    private String operationRate;
    //设备使用年数
    private String deviceUserYear;
    
    private String IP;
    
    private String assetStatus;

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getDeviceUserYear() {
		return deviceUserYear;
	}

	public void setDeviceUserYear(String deviceUserYear) {
		this.deviceUserYear = deviceUserYear;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
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

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
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
    
    public String getOperationRate() {
		return operationRate;
	}

	public void setOperationRate(String operationRate) {
		this.operationRate = operationRate;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}

	public void setHoldMan(String holdMan) {
		this.holdMan = holdMan;
	}

	public String getHoldDepartment() {
		return holdDepartment;
	}

	public void setHoldDepartment(String holdDepartment) {
		this.holdDepartment = holdDepartment;
	}

	public String getHoldMan() {
		return holdMan;
	}

	public void setHoleMan(String holdMan) {
		this.holdMan = holdMan;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Double getDownTime() {
		return downTime;
	}

	public void setDownTime(double downTime) {
		this.downTime = downTime;
	}

	public Double getWorkTime() {
		return workTime;
	}

	public void setWorkTime(double workTime) {
		this.workTime = workTime;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMachCode() {
        return machCode;
    }

    public void setMachCode(String machCode) {
        this.machCode = machCode == null ? null : machCode.trim();
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName == null ? null : devName.trim();
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand == null ? null : deviceBrand.trim();
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType == null ? null : unitType.trim();
    }

    public String getDeviceBound() {
        return deviceBound;
    }

    public void setDeviceBound(String deviceBound) {
        this.deviceBound = deviceBound == null ? null : deviceBound.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getOutputSpec() {
        return outputSpec;
    }

    public void setOutputSpec(String outputSpec) {
        this.outputSpec = outputSpec == null ? null : outputSpec.trim();
    }

    public String getEsNumber() {
        return esNumber;
    }

    public void setEsNumber(String esNumber) {
        this.esNumber = esNumber == null ? null : esNumber.trim();
    }

    public String getCustArea() {
        return custArea;
    }

    public void setCustArea(String custArea) {
        this.custArea = custArea == null ? null : custArea.trim();
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea == null ? null : serviceArea.trim();
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

    public String getResponsibleEngineer() {
        return responsibleEngineer;
    }

    public void setResponsibleEngineer(String responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer == null ? null : responsibleEngineer.trim();
    }

    public String getReserveEnginner() {
        return reserveEnginner;
    }

    public void setReserveEnginner(String reserveEnginner) {
        this.reserveEnginner = reserveEnginner == null ? null : reserveEnginner.trim();
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

    public String getAssetAttr() {
        return assetAttr;
    }

    public void setAssetAttr(String assetAttr) {
        this.assetAttr = assetAttr == null ? null : assetAttr.trim();
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber == null ? null : assetNumber.trim();
    }

	public Device(String machCode, String devName, String deviceBrand, String unitType, String deviceBound,
			String deviceType, String outputSpec, String esNumber, String custArea, String serviceArea,
			String department, String serviceLevel, String custLinkman, String linkmanPhone,
			String responsibleEngineer, String reserveEnginner, String bwReader, String colorReader, Date installedTime,
			String assetAttr, String assetNumber) {
		super();
		this.machCode = machCode;
		this.devName = devName;
		this.deviceBrand = deviceBrand;
		this.unitType = unitType;
		this.deviceBound = deviceBound;
		this.deviceType = deviceType;
		this.outputSpec = outputSpec;
		this.esNumber = esNumber;
		this.custArea = custArea;
		this.serviceArea = serviceArea;
		this.department = department;
		this.serviceLevel = serviceLevel;
		this.custLinkman = custLinkman;
		this.linkmanPhone = linkmanPhone;
		this.responsibleEngineer = responsibleEngineer;
		this.reserveEnginner = reserveEnginner;
		this.bwReader = bwReader;
		this.colorReader = colorReader;
		this.installedTime = installedTime;
		this.assetAttr = assetAttr;
		this.assetNumber = assetNumber;
	}

	public Device() {
		super();
	}
	
	public Device(String machCode, String devName, String deviceBrand, String unitType, String deviceBound,
			String deviceType, String outputSpec, String esNumber, String custArea, String serviceArea,
			String department, String serviceLevel, String custLinkman, String linkmanPhone,
			String responsibleEngineer, String reserveEnginner, String bwReader, String colorReader, Date installedTime,
			String assetAttr, String assetNumber, String changeType, String location, Double downTime, Double workTime,
			String contractNo, String holdDepartment, String holdMan, String secret, String secretLevel,
			String assetClass, String specifications, String operationRate) {
		super();
		this.machCode = machCode;
		this.devName = devName;
		this.deviceBrand = deviceBrand;
		this.unitType = unitType;
		this.deviceBound = deviceBound;
		this.deviceType = deviceType;
		this.outputSpec = outputSpec;
		this.esNumber = esNumber;
		this.custArea = custArea;
		this.serviceArea = serviceArea;
		this.department = department;
		this.serviceLevel = serviceLevel;
		this.custLinkman = custLinkman;
		this.linkmanPhone = linkmanPhone;
		this.responsibleEngineer = responsibleEngineer;
		this.reserveEnginner = reserveEnginner;
		this.bwReader = bwReader;
		this.colorReader = colorReader;
		this.installedTime = installedTime;
		this.assetAttr = assetAttr;
		this.assetNumber = assetNumber;
		this.changeType = changeType;
		this.location = location;
		this.downTime = downTime;
		this.workTime = workTime;
		this.contractNo = contractNo;
		this.holdDepartment = holdDepartment;
		this.holdMan = holdMan;
		this.secret = secret;
		this.secretLevel = secretLevel;
		this.assetClass = assetClass;
		this.specifications = specifications;
		this.operationRate = operationRate;
	}
	
	

	@Override
	public String toString() {
		return "机器编码=" + machCode + ", 设备名称=" + devName + ", 设备品牌=" + deviceBrand + ", 设备型号="
				+ unitType + ", 设备幅面=" + deviceBound + ", 设备类型=" + deviceType + ", 输出规格="
				+ outputSpec + ", 设备序列号=" + esNumber + ", 客户区域=" + custArea + ", 服务区域=" + serviceArea
				+ ", 部门=" + department + ", 服务级别=" + serviceLevel + ", 客户联系人=" + custLinkman
				+ ", 客户联络电话=" + linkmanPhone + ", 责任工程师=" + responsibleEngineer
				+ ", 后备工程师=" + reserveEnginner + ", 初始黑白读数=" + bwReader + ", 初始彩色读数=" + colorReader
				+ ", 装机时间=" + installedTime + ", 资产属性=" + assetAttr + ", 资产编码=" + assetNumber
				+ ", 变动类型=" + changeType + ", 位置=" + location + "]";
	}
	  
}