package com.xunwei.som.pojo;

import java.util.Date;

public class Maintenance {
	
	//合同编码
    private String contractCode;
    //客户名称
    private String custName;
    //联系人
    private String repairMan;
    //联系电话
    private String repairService;
    //保养频率
    private String mainFrequency;
    //责任工程师
    private String responsibleEngineer;
    //责任工程师Id
    private String responsibleId;
    //后备工程师
    private String reservEngineer;
    //后备工程师ID
    private String reservEngineerId;
    //服务区域
    private String compName;
    //机器编码
    private String machCode;
    //设备名称
    private String devName;
    //其他外键信息
    private Device device;
    //最近一次维修时间
    private Date lastTime;
    //备注
    private String remark;
    //保养状态 0-未保养 1-已保养
    private Integer maintenanceState;
    //保养是否完成
    private String maintenStatus;
    //保养完成时间
  	private Date completionTime;
  	//黑白读数
  	private String bwReader;
  	//彩色读数
  	private String coReader;
  	
  	private String unitType;
  	
  	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
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

	private String materialModel; //耗材型号
  	
  	private String materialNumber; //耗材数量
  	
  	private String coverage; //覆盖率
  	
    public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getMaterialModel() {
		return materialModel;
	}

	public void setMaterialModel(String materialModel) {
		this.materialModel = materialModel;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getReservEngineerId() {
		return reservEngineerId;
	}

	public void setReservEngineerId(String reservEngineerId) {
		this.reservEngineerId = reservEngineerId;
	}

	public String getMaintenStatus() {
		return maintenStatus;
	}

	public void setMaintenStatus(String maintenStatus) {
		this.maintenStatus = maintenStatus;
	}

	public String getResponsibleEngineer() {
		return responsibleEngineer;
	}

	public void setResponsibleEngineer(String responsibleEngineer) {
		this.responsibleEngineer = responsibleEngineer;
	}

	public String getResponsibleId() {
		return responsibleId;
	}

	public void setResponsibleId(String responsibleId) {
		this.responsibleId = responsibleId;
	}

	public Integer getMaintenanceState() {
		return maintenanceState;
	}

	public void setMaintenanceState(Integer maintenanceState) {
		this.maintenanceState = maintenanceState;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private Date createDate;
    
    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getReservEngineer() {
		return reservEngineer;
	}

	public void setReservEngineer(String reservEngineer) {
		this.reservEngineer = reservEngineer;
	}

	public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getRepairMan() {
        return repairMan;
    }

    public void setRepairMan(String repairMan) {
        this.repairMan = repairMan == null ? null : repairMan.trim();
    }

    public String getRepairService() {
        return repairService;
    }

    public void setRepairService(String repairService) {
        this.repairService = repairService;
    }

    public String getMainFrequency() {
        return mainFrequency;
    }

    public void setMainFrequency(String mainFrequency) {
        this.mainFrequency = mainFrequency == null ? null : mainFrequency.trim();
    }

	public String getMachCode() {
		return machCode;
	}

	public void setMachCode(String machCode) {
		this.machCode = machCode;
	}
	
	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}
	
	public Maintenance(String contractCode, String custName, String repairMan, String repairService,
			String mainFrequency, String responsibleEngineer, String responsibleId, String reservEngineer,
			String reserveEnginnerId, String compName, String machCode, Device device, Date lastTime, String remark,
			Integer maintenanceState, String maintenStatus, Date completionTime, Date createDate) {
		super();
		this.contractCode = contractCode;
		this.custName = custName;
		this.repairMan = repairMan;
		this.repairService = repairService;
		this.mainFrequency = mainFrequency;
		this.responsibleEngineer = responsibleEngineer;
		this.responsibleId = responsibleId;
		this.reservEngineer = reservEngineer;
		this.reservEngineerId = reserveEnginnerId;
		this.compName = compName;
		this.machCode = machCode;
		this.device = device;
		this.lastTime = lastTime;
		this.remark = remark;
		this.maintenanceState = maintenanceState;
		this.maintenStatus = maintenStatus;
		this.completionTime = completionTime;
		this.createDate = createDate;
	}

	public Maintenance() {
		super();
	}

	@Override
	public String toString() {
		return "Maintenance [contractCode=" + contractCode + ", custName=" + custName + ", repairMan=" + repairMan
				+ ", repairService=" + repairService + ", mainFrequency=" + mainFrequency + ", responsibleEngineer="
				+ responsibleEngineer + ", responsibleId=" + responsibleId + ", reservEngineer=" + reservEngineer
				+ ", reservEngineerId=" + reservEngineerId + ", compName=" + compName + ", machCode=" + machCode
				+ ", device=" + device + ", lastTime=" + lastTime + ", remark=" + remark + ", maintenanceState="
				+ maintenanceState + ", maintenStatus=" + maintenStatus + ", completionTime=" + completionTime
				+ ", materialModel=" + materialModel + ", materialNumber=" + materialNumber + ", coverage=" + coverage
				+ ", createDate=" + createDate + "]";
	}
	
}