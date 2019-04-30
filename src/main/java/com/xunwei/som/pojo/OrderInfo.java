package com.xunwei.som.pojo;
import java.util.Date;

/**
 * 工单属性类
 * @author Administrator
 *
 */
public class OrderInfo {
	
	
	//工单号,主键，自增 1
    private String woNumber;
    
    //优先级别
    private Integer priority;
    
    //工单类型 9
    private String faultType;

    //设备序列号 2
    private String esNumber;

    //客户名称 3
    private String custName;

    //客户编号
    private Integer custId;

    //设备名称 4
    private String devName;

    //机器编码 5
    private String machCode;

    //报修人 7
    private String repairMan;

    //报修维护电话 8
    private String repairService;

    //客户地址 6
    private String custAddr;

    //处理状态
    private String treatmentState;

    //处理措施 11
    private String treatmentMeasure;

    //客户报修日期
    private Date repairTime;
  
    //客服接单时间
    private Date acceptTime;

    //客服派单时间 12
    private Date sendTime;
    
    //工程师接单时间
    private Date getOrderTime;
    
    //技术主管关单时间
    private Date closeTime;
    
    //转单时间
    private Date turnOrderTime;

    //转单原因
    private String turnOrderReson;
    
    //工单状态 
    private String woStatus;
    
    //工单进度 
    private String woProgress;

    //备注
    private String remark;
    
    //报修类型
    private String repairType;
    
    //故障类型
    private String accidentType;
    
    //服务类别
    private String serviceType;
    
    //MACD类型
    private String macdType;
    
    //维修反馈
    private String maintenanceFeedback;
    
    //故障分类
    private String faultClass;
    
    //故障代码
    private String falutNo;
    
    //零件种类和数量
    private String partsTypeNumber;
    
    //派单人
    private String distributeMan;
    
    //零件状态
    private String partsStatus;
    
    //责任工程师
    private String enginner;
    
    //实际报修人
    private String orderAccount;
    
    public String getOrderAccount() {
		return orderAccount;
	}

	public void setOrderAccount(String orderAccount) {
		this.orderAccount = orderAccount;
	}

	//设备型号
    private String unitType;
    
    public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getEnginner() {
		return enginner;
	}

	public void setEnginner(String enginner) {
		this.enginner = enginner;
	}

	//服务区域
    private String serviceArea;
    
	public String getMacdType() {
		return macdType;
	}

	public void setMacdType(String macdType) {
		this.macdType = macdType;
	}

	public String getTurnOrderReson() {
		return turnOrderReson;
	}

	public void setTurnOrderReson(String turnOrderReson) {
		this.turnOrderReson = turnOrderReson;
	}

	public String getWoProgress() {
		return woProgress;
	}

	public void setWoProgress(String woProgress) {
		this.woProgress = woProgress;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getPartsStatus() {
		return partsStatus;
	}

	public void setPartsStatus(String partsStatus) {
		this.partsStatus = partsStatus;
	}

	public String getPartsTypeNumber() {
		return partsTypeNumber;
	}

	public void setPartsTypeNumber(String partsTypeNumber) {
		this.partsTypeNumber = partsTypeNumber;
	}

	public String getMaintenanceFeedback() {
		return maintenanceFeedback;
	}

	public void setMaintenanceFeedback(String maintenanceFeedback) {
		this.maintenanceFeedback = maintenanceFeedback;
	}

	public String getFaultClass() {
		return faultClass;
	}

	public void setFaultClass(String faultClass) {
		this.faultClass = faultClass;
	}

	public String getFalutNo() {
		return falutNo;
	}

	public void setFalutNo(String falutNo) {
		this.falutNo = falutNo;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Date getTurnOrderTime() {
		return turnOrderTime;
	}

	public void setTurnOrderTime(Date turnOrderTime) {
		this.turnOrderTime = turnOrderTime;
	}

	public Date getGetOrderTime() {
		return getOrderTime;
	}

	public void setGetOrderTime(Date getOrderTime) {
		this.getOrderTime = getOrderTime;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	

    public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(String woNumber) {
        this.woNumber = woNumber;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType == null ? null : faultType.trim();
    }

    public String getEsNumber() {
        return esNumber;
    }

    public void setEsNumber(String esNumber) {
        this.esNumber = esNumber == null ? null : esNumber.trim();
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

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName == null ? null : devName.trim();
    }

    public String getMachCode() {
        return machCode;
    }

    public void setMachCode(String machCode) {
        this.machCode = machCode == null ? null : machCode.trim();
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

    public String getCustAddr() {
        return custAddr;
    }

    public void setCustAddr(String custAddr) {
        this.custAddr = custAddr == null ? null : custAddr.trim();
    }

    public String getTreatmentState() {
        return treatmentState;
    }

    public void setTreatmentState(String treatmentState) {
        this.treatmentState = treatmentState == null ? null : treatmentState.trim();
    }

    public String getTreatmentMeasure() {
        return treatmentMeasure;
    }

    public void setTreatmentMeasure(String treatmentMeasure) {
        this.treatmentMeasure = treatmentMeasure == null ? null : treatmentMeasure.trim();
    }

    public Date getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Date repairTime) {
        this.repairTime = repairTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getWoStatus() {
        return woStatus;
    }

    public void setWoStatus(String woStatus) {
        this.woStatus = woStatus == null ? null : woStatus.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public String getDistributeMan() {
		return distributeMan;
	}

	public void setDistributeMan(String distributeMan) {
		this.distributeMan = distributeMan;
	}

	public OrderInfo() {
		super();
	}
	
	public OrderInfo(String faultType, String esNumber, String custName, Integer custId, String devName,
			String machCode, String repairMan, String repairService, String custAddr, Date sendTime,
			String repairType) {
		super();
		this.faultType = faultType;
		this.esNumber = esNumber;
		this.custName = custName;
		this.custId = custId;
		this.devName = devName;
		this.machCode = machCode;
		this.repairMan = repairMan;
		this.repairService = repairService;
		this.custAddr = custAddr;
		this.sendTime = sendTime;
		this.repairType = repairType;
	}
	
	public OrderInfo(String woNumber,String faultType, String esNumber, String custName, Integer custId, String devName,
			String machCode, String repairMan, String repairService, String custAddr,
			String treatmentMeasure, Date sendTime, String woStatus, String repairType,
			String accidentType) {
		super();
		this.woNumber=woNumber;
		this.faultType = faultType;
		this.esNumber = esNumber;
		this.custName = custName;
		this.custId = custId;
		this.devName = devName;
		this.machCode = machCode;
		this.repairMan = repairMan;
		this.repairService = repairService;
		this.custAddr = custAddr;
		this.treatmentMeasure = treatmentMeasure;
		this.sendTime = sendTime;
		this.woStatus = woStatus;
		this.repairType = repairType;
		this.accidentType = accidentType;
	}

	@Override
	public String toString() {
		return "orderInfo [woNumber=" + woNumber + ", priority=" + priority + ", faultType=" + faultType + ", esNumber="
				+ esNumber + ", custName=" + custName + ", custId=" + custId + ", devName=" + devName + ", machCode="
				+ machCode + ", repairMan=" + repairMan + ", repairService=" + repairService + ", custAddr=" + custAddr
				+ ", treatmentState=" + treatmentState + ", treatmentMeasure=" + treatmentMeasure + ", repairTime="
				+ repairTime + ", sendTime=" + sendTime + ", woStatus=" + woStatus + ", remark=" + remark
				+ ", repairType=" + repairType + "]";
	}
	
}