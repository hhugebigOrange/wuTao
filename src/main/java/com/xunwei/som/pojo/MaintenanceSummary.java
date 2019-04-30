package com.xunwei.som.pojo;

/**
 * 保养报表汇总
 * @author Administrator
 *
 */
public class MaintenanceSummary {

	//客户名称
	private String custName;
	//客户名下合同所有设备数量
	private int devNumber;
	//计划保养数量
	private int planDevNum;
	//实际完成数量
	private int complNum;
	//未完成
	private int noCompNum;
	//完成率
	private double completionRate;
	//工程师姓名
	private String staffName;
	
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	//备注
	private String remark;
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public int getDevNumber() {
		return devNumber;
	}
	public void setDevNumber(int devNumber) {
		this.devNumber = devNumber;
	}
	public int getPlanDevNum() {
		return planDevNum;
	}
	public void setPlanDevNum(int planDevNum) {
		this.planDevNum = planDevNum;
	}
	public int getComplNum() {
		return complNum;
	}
	public void setComplNum(int complNum) {
		this.complNum = complNum;
	}
	public int getNoCompNum() {
		return noCompNum;
	}
	public void setNoCompNum(int noCompNum) {
		this.noCompNum = noCompNum;
	}
	public double getCompletionRate() {
		return completionRate;
	}
	public void setCompletionRate(double completionRate) {
		this.completionRate = completionRate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public MaintenanceSummary(String custName, int devNumber, int planDevNum, int complNum, int noCompNum,
			double completionRate, String staffName, String remark) {
		super();
		this.custName = custName;
		this.devNumber = devNumber;
		this.planDevNum = planDevNum;
		this.complNum = complNum;
		this.noCompNum = noCompNum;
		this.completionRate = completionRate;
		this.staffName = staffName;
		this.remark = remark;
	}
	public MaintenanceSummary() {
		super();
	}
	
	
}
