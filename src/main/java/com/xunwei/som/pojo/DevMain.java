package com.xunwei.som.pojo;

import java.util.Date;

public class DevMain {
    private String machCode;

    private Integer custId;

    private Date mainTime;

    private String mainFrequency;

    private Integer staffId;
    
    private Date arrivalTime;
    
    private String numberFeeback;
    
    private StaffInfo staffInfo;
    
    private Device deviceInfo;
    
    private CustInfo custInfo;
    
    public StaffInfo getStaffInfo() {
		return staffInfo;
	}

	public void setStaffInfo(StaffInfo staffInfo) {
		this.staffInfo = staffInfo;
	}

	public Device getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(Device deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public CustInfo getCustInfo() {
		return custInfo;
	}

	public void setCustInfo(CustInfo custInfo) {
		this.custInfo = custInfo;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getNumberFeeback() {
		return numberFeeback;
	}

	public void setNumberFeeback(String numberFeeback) {
		this.numberFeeback = numberFeeback;
	}

	public String getMachCode() {
        return machCode;
    }

    public void setMachCode(String machCode) {
        this.machCode = machCode == null ? null : machCode.trim();
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Date getMainTime() {
        return mainTime;
    }

    public void setMainTime(Date mainTime) {
        this.mainTime = mainTime;
    }

    public String getMainFrequency() {
        return mainFrequency;
    }

    public void setMainFrequency(String mainFrequency) {
        this.mainFrequency = mainFrequency == null ? null : mainFrequency.trim();
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

	@Override
	public String toString() {
		return "devMain [machCode=" + machCode + ", custId=" + custId + ", mainTime=" + mainTime + ", mainFrequency="
				+ mainFrequency + ", staffId=" + staffId + ", arrivalTime=" + arrivalTime + ", numberFeeback="
				+ numberFeeback + "]";
	}
    
    
}