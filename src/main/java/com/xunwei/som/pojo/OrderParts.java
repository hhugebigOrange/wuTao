package com.xunwei.som.pojo;

/**
 * 工单：零件状态类
 * @author Administrator
 *
 */
public class OrderParts {
    private String woNumber;

    private String woStatus;
    
    private String reason;
    
    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(String woNumber) {
        this.woNumber = woNumber == null ? null : woNumber.trim();
    }

    public String getWoStatus() {
        return woStatus;
    }

    public void setWoStatus(String woStatus) {
        this.woStatus = woStatus == null ? null : woStatus.trim();
    }
}