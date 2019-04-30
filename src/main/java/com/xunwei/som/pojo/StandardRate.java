package com.xunwei.som.pojo;

import java.math.BigDecimal;

/**
 * 达标实体类，用来对应各种达标率
 * @author Administrator
 *
 */
public class StandardRate {
     
	private String custName;
	//坐席客服受理时间达标率
	private String chiefSendTimeRate;
	//工程师姓名
	private String staffName;
	//响应时间达标率
	private String responseTimeRate;
	//到达时间达标率
	private String arrTimeRate;
	//到达现场平均用时
	private String arrTimeAvg;
	//解决问题平均用时
	private String probTimeAvg;
	//问题解决达标率
	private String probSolveRate;
	//二次上门率
	private String secondService;
	//设备运转率
	private String equipmentOperationRate;
	//转单数量
	private int orderTurnNum;
	//保养完成率
	private String maintenanceRate;
	//工单数量
	private String orderNum;
	//客户投诉次数
	private int CustComplaints;
	//客户满意度
	private String customerSatisfaction;
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getChiefSendTimeRate() {
		return chiefSendTimeRate;
	}
	public void setChiefSendTimeRate(String chiefSendTimeRate) {
		this.chiefSendTimeRate = chiefSendTimeRate;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getResponseTimeRate() {
		return responseTimeRate;
	}
	public void setResponseTimeRate(String responseTimeRate) {
		this.responseTimeRate = responseTimeRate;
	}
	public String getArrTimeRate() {
		return arrTimeRate;
	}
	public void setArrTimeRate(String arrTimeRate) {
		this.arrTimeRate = arrTimeRate;
	}
	public String getArrTimeAvg() {
		return arrTimeAvg;
	}
	public void setArrTimeAvg(String arrTimeAvg) {
		this.arrTimeAvg = arrTimeAvg;
	}
	public String getProbTimeAvg() {
		return probTimeAvg;
	}
	public void setProbTimeAvg(String probTimeAvg) {
		this.probTimeAvg = probTimeAvg;
	}
	public String getProbSolveRate() {
		return probSolveRate;
	}
	public void setProbSolveRate(String probSolveRate) {
		this.probSolveRate = probSolveRate;
	}
	public String getSecondService() {
		return secondService;
	}
	public void setSecondService(String secondService) {
		this.secondService = secondService;
	}
	public String getEquipmentOperationRate() {
		return equipmentOperationRate;
	}
	public void setEquipmentOperationRate(String equipmentOperationRate) {
		this.equipmentOperationRate = equipmentOperationRate;
	}
	public int getOrderTurnNum() {
		return orderTurnNum;
	}
	public void setOrderTurnNum(int orderTurnNum) {
		this.orderTurnNum = orderTurnNum;
	}
	public String getMaintenanceRate() {
		return maintenanceRate;
	}
	public void setMaintenanceRate(String maintenanceRate) {
		this.maintenanceRate = maintenanceRate;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public int getCustComplaints() {
		return CustComplaints;
	}
	public void setCustComplaints(int custComplaints) {
		CustComplaints = custComplaints;
	}
	public String getCustomerSatisfaction() {
		return customerSatisfaction;
	}
	public void setCustomerSatisfaction(String customerSatisfaction) {
		this.customerSatisfaction = customerSatisfaction;
	}
}
