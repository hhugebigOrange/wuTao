package com.xunwei.som.pojo;

import java.util.Date;

public class ServiceInfo {
	
	private OrderInfo orderInfo;
	
	private StaffInfo staffInfo;
	
	private Device device;
	
    private String woNumber;

    private String staffId;

    private Integer arrivalTime;

    private String mainResults;

    private String serviceMode;

    private Date telRepon;

    private Date arrTime;

    private Date probSolve;

    private String docType;

    private Integer custScore;

    private String custComp;

    private String custPrai;
    
    private String custSat;
    
    private String downTime;
    
    private String custEva;
    
    private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCustEva() {
		return custEva;
	}

	public void setCustEva(String custEva) {
		this.custEva = custEva;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(String woNumber) {
        this.woNumber = woNumber;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getMainResults() {
        return mainResults;
    }

    public void setMainResults(String mainResults) {
        this.mainResults = mainResults == null ? null : mainResults.trim();
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode == null ? null : serviceMode.trim();
    }

    public Date getTelRepon() {
        return telRepon;
    }

    public void setTelRepon(Date telRepon) {
        this.telRepon = telRepon;
    }

    public Date getArrTime() {
        return arrTime;
    }

    public void setArrTime(Date arrTime) {
        this.arrTime = arrTime;
    }

    public Date getProbSolve() {
        return probSolve;
    }

    public void setProbSolve(Date probSolve) {
        this.probSolve = probSolve;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType == null ? null : docType.trim();
    }

    public Integer getCustScore() {
        return custScore;
    }

    public void setCustScore(Integer custScore) {
        this.custScore = custScore;
    }

    public String getCustComp() {
        return custComp;
    }

    public void setCustComp(String custComp) {
        this.custComp = custComp == null ? null : custComp.trim();
    }

    public String getCustPrai() {
        return custPrai;
    }

    public void setCustPrai(String custPrai) {
        this.custPrai = custPrai == null ? null : custPrai.trim();
    }

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public StaffInfo getStaffInfo() {
		return staffInfo;
	}

	public void setStaffInfo(StaffInfo staffInfo) {
		this.staffInfo = staffInfo;
	}

	public String getCustSat() {
		return custSat;
	}

	public void setCustSat(String custSat) {
		this.custSat = custSat;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "serviceInfo [orderInfo=" + orderInfo + ", staffInfo=" + staffInfo + ", woNumber=" + woNumber
				+ ", staffId=" + staffId + ", arrivalTime=" + arrivalTime + ", mainResults=" + mainResults
				+ ", serviceMode=" + serviceMode + ", telRepon=" + telRepon + ", arrTime=" + arrTime + ", probSolve="
				+ probSolve + ", docType=" + docType + ", custScore=" + custScore + ", custComp=" + custComp
				+ ", custPrai=" + custPrai + "]";
	}
    
    
    
}