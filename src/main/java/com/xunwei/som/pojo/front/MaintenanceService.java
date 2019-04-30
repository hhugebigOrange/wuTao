package com.xunwei.som.pojo.front;


public class MaintenanceService {

	private String custName;
	
	private int deviceNumber;
	
	private int planDeviceNumber; //计划保养数量
	
	private int reallyOverDeviceNumber;  //实际完成数量
	
	private int notOverDeviceNumber; //未完成数量
	
	private String maintenanceRate;  //保养完成率
	
	private String remark; //备注

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public int getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(int deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public int getPlanDeviceNumber() {
		return planDeviceNumber;
	}

	public void setPlanDeviceNumber(int planDeviceNumber) {
		this.planDeviceNumber = planDeviceNumber;
	}

	public int getReallyOverDeviceNumber() {
		return reallyOverDeviceNumber;
	}

	public void setReallyOverDeviceNumber(int reallyOverDeviceNumber) {
		this.reallyOverDeviceNumber = reallyOverDeviceNumber;
	}

	public int getNotOverDeviceNumber() {
		return notOverDeviceNumber;
	}

	public void setNotOverDeviceNumber(int notOverDeviceNumber) {
		this.notOverDeviceNumber = notOverDeviceNumber;
	}

	public String getMaintenanceRate() {
		return maintenanceRate;
	}

	public void setMaintenanceRate(String maintenanceRate) {
		this.maintenanceRate = maintenanceRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "MaintenanceService [custName=" + custName + ", deviceNumber=" + deviceNumber + ", planDeviceNumber="
				+ planDeviceNumber + ", reallyOverDeviceNumber=" + reallyOverDeviceNumber + ", notOverDeviceNumber="
				+ notOverDeviceNumber + ", maintenanceRate=" + maintenanceRate + ", remark=" + remark + "]";
	}
}
