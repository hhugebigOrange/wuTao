package com.xunwei.som.pojo;

import java.util.Date;

/**
 * 总公司Kpi
 * @author Administrator
 *
 */
public class HeadOfficeKpi {
	// 服务区域
	private String serviceArea;
	// 客户名称
	private String custName;
	// 工单号码
	private String woNumber;
	// 受理时间
	private Date orderTime;
	// 派单时间
	private Date sendTime;
	// 坐席受理时间
	private Date chiefSendTime;
	// 坐席受理用时
	private double chiefSendTimeSlot;
	// 坐席受理时间达标率
	private String chiefSendTimeRate;
	// 响应时间
	private Date telRepon;
	// 响应时间段
	private double responseTime;
	// 响应时间达标率
	private String responseTimeRate;
	// 到达时间
	private Date arrTime;
	// 到达用时
	private double arrTimeSlot;
	// 到达时间达标率
	private String arrTimeRate;
	// 问题解决时间
	private Date probSolve;
	// 问题解决用时
	private double probSolveSlot;
	// 问题解决达标率
	private String probSolveRate;
	// 二次上门率
	private String secondService;
	// 工单数量
	private int orderNum;
	// 转单数量
	private int orderTurnNum;
	// 客户评价分数
	private int custScore;
	// 客户投诉次数
	private int CustComplaints;
	// 客户表扬次数
	private int CustPraise;
	// 年度客户满意度调查结果
	private String resOfOneYear;

	public double getChiefSendTimeSlot() {
		return chiefSendTimeSlot;
	}
	public void setChiefSendTimeSlot(double chiefSendTimeSlot) {
		this.chiefSendTimeSlot = chiefSendTimeSlot;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getWoNumber() {
		return woNumber;
	}
	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getChiefSendTime() {
		return chiefSendTime;
	}
	public void setChiefSendTime(Date chiefSendTime) {
		this.chiefSendTime = chiefSendTime;
	}
	public String getChiefSendTimeRate() {
		return chiefSendTimeRate;
	}
	public void setChiefSendTimeRate(String chiefSendTimeRate) {
		this.chiefSendTimeRate = chiefSendTimeRate;
	}
	public Date getTelRepon() {
		return telRepon;
	}
	public void setTelRepon(Date telRepon) {
		this.telRepon = telRepon;
	}
	public double getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseTimeRate() {
		return responseTimeRate;
	}
	public void setResponseTimeRate(String responseTimeRate) {
		this.responseTimeRate = responseTimeRate;
	}
	public Date getArrTime() {
		return arrTime;
	}
	public void setArrTime(Date arrTime) {
		this.arrTime = arrTime;
	}
	public double getArrTimeSlot() {
		return arrTimeSlot;
	}
	public void setArrTimeSlot(double arrTimeSlot) {
		this.arrTimeSlot = arrTimeSlot;
	}
	public String getArrTimeRate() {
		return arrTimeRate;
	}
	public void setArrTimeRate(String arrTimeRate) {
		this.arrTimeRate = arrTimeRate;
	}
	public Date getProbSolve() {
		return probSolve;
	}
	public void setProbSolve(Date probSolve) {
		this.probSolve = probSolve;
	}
	public double getProbSolveSlot() {
		return probSolveSlot;
	}
	public void setProbSolveSlot(double probSolveSlot) {
		this.probSolveSlot = probSolveSlot;
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
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getOrderTurnNum() {
		return orderTurnNum;
	}
	public void setOrderTurnNum(int orderTurnNum) {
		this.orderTurnNum = orderTurnNum;
	}
	public int getCustScore() {
		return custScore;
	}
	public void setCustScore(int custScore) {
		this.custScore = custScore;
	}
	public int getCustComplaints() {
		return CustComplaints;
	}
	public void setCustComplaints(int custComplaints) {
		CustComplaints = custComplaints;
	}
	public int getCustPraise() {
		return CustPraise;
	}
	public void setCustPraise(int custPraise) {
		CustPraise = custPraise;
	}
	public String getResOfOneYear() {
		return resOfOneYear;
	}
	public void setResOfOneYear(String resOfOneYear) {
		this.resOfOneYear = resOfOneYear;
	}
}
