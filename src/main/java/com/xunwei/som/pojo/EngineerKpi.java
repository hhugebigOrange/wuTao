package com.xunwei.som.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 工程师kpi
 * @author Administrator
 *
 */
public class EngineerKpi {

	//工单编码
	private String woNumber;
	//客户名称
	private String custName;
	//服务区域
	private String serviceArea;
	//工单来源
	private String repairType;
	//设备名称
	private String devName;
	//机器编码
	private String machCode;
	//服务类型
	private String faultType;
	//服务类别
	private String accidentType;
	//报修时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date repairTime;
	//工程师名称
	private String staffName;
	//后备工程师
	private String staffName2;
	//工程师受理时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date orderTime;
	//派单时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date sendTime;
	//派单时间用时
	private Integer sendTimeSlot;
	//派单时间达标率
	private String sendTimeRate;
	//响应时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date telRepon;
	//响应时间段
	private Integer responseTime;
	//响应时间达标率
	private String responseTimeRate;
	//到达时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date arrTime;
	//到达用时
	private Double arrTimeSlot;
	//到达时间达标率
	private String arrTimeRate;
	//问题解决时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date probSolve;
	//问题解决用时
	private Double probSolveSlot;
	//问题解决达标率
	private String probSolveRate;
	//二次上门率
	private String secondService;
	//工单数量
	private double orderNum;
	//转单数量
	private double orderTurnNum;
	//客户评价分数
	private Integer custScore;
	//客户投诉次数
	private double CustComplaints;
	//客户表扬次数
	private double CustPraise;
	//年度客户满意度调查结果
	private String resOfOneYear;
	//工单状态
	private String woStatus;
	//维修结果
	private String maintenanceFeedback;
	//处理措施
	private String treatmentMeasure;
	 //派单人
    private String distributeMan;
    
	public String getTreatmentMeasure() {
		return treatmentMeasure;
	}
	public void setTreatmentMeasure(String treatmentMeasure) {
		this.treatmentMeasure = treatmentMeasure;
	}
	public String getSendTimeRate() {
		return sendTimeRate;
	}
	public void setSendTimeRate(String sendTimeRate) {
		this.sendTimeRate = sendTimeRate;
	}
	public String getDistributeMan() {
		return distributeMan;
	}
	public void setDistributeMan(String distributeMan) {
		this.distributeMan = distributeMan;
	}
	public String getMaintenanceFeedback() {
		return maintenanceFeedback;
	}
	public void setMaintenanceFeedback(String maintenanceFeedback) {
		this.maintenanceFeedback = maintenanceFeedback;
	}
	public String getStaffName2() {
		return staffName2;
	}
	public void setStaffName2(String staffName2) {
		this.staffName2 = staffName2;
	}
	public String getWoStatus() {
		return woStatus;
	}
	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}
	public Date getRepairTime() {
		return repairTime;
	}
	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getMachCode() {
		return machCode;
	}
	public void setMachCode(String machCode) {
		this.machCode = machCode;
	}
	public String getFaultType() {
		return faultType;
	}
	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
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
	public Integer getSendTimeSlot() {
		return sendTimeSlot;
	}
	public void setSendTimeSlot(Integer sendTimeSlot) {
		this.sendTimeSlot = sendTimeSlot;
	}
	public String getWoNumber() {
		return woNumber;
	}
	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getTelRepon() {
		return telRepon;
	}
	public void setTelRepon(Date telRepon) {
		this.telRepon = telRepon;
	}
	public Integer getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Integer responseTime) {
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
	public Double getArrTimeSlot() {
		return arrTimeSlot;
	}
	public void setArrTimeSlot(Double arrTimeSlot) {
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
	public Double getProbSolveSlot() {
		return probSolveSlot;
	}
	public void setProbSolveSlot(Double probSolveSlot) {
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
	public double getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(double orderNum) {
		this.orderNum = orderNum;
	}
	public double getOrderTurnNum() {
		return orderTurnNum;
	}
	public void setOrderTurnNum(double orderTurnNum) {
		this.orderTurnNum = orderTurnNum;
	}
	public Integer getCustScore() {
		return custScore;
	}
	public void setCustScore(Integer custScore) {
		this.custScore = custScore;
	}
	public double getCustComplaints() {
		return CustComplaints;
	}
	public void setCustComplaints(double custComplaints) {
		CustComplaints = custComplaints;
	}
	public double getCustPraise() {
		return CustPraise;
	}
	public void setCustPraise(double custPraise) {
		CustPraise = custPraise;
	}
	public String getResOfOneYear() {
		return resOfOneYear;
	}
	public void setResOfOneYear(String resOfOneYear) {
		this.resOfOneYear = resOfOneYear;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public EngineerKpi(String woNumber, String staffName, Date orderTime,Date sendTime, Date telRepon, Integer responseTime,
			String responseTimeRate, Date arrTime, Double arrTimeSlot, String arrTimeRate, Date probSolve,
			Double probSolveSlot, String probSolveRate, String secondService, int orderNum, int orderTurnNum,
			int custScore, int custComplaints, int custPraise, String resOfOneYear) {
		super();
		this.woNumber = woNumber;
		this.staffName = staffName;
		this.orderTime= orderTime;
		this.sendTime = sendTime;
		this.telRepon = telRepon;
		this.responseTime = responseTime;
		this.responseTimeRate = responseTimeRate;
		this.arrTime = arrTime;
		this.arrTimeSlot = arrTimeSlot;
		this.arrTimeRate = arrTimeRate;
		this.probSolve = probSolve;
		this.probSolveSlot = probSolveSlot;
		this.probSolveRate = probSolveRate;
		this.secondService = secondService;
		this.orderNum = orderNum;
		this.orderTurnNum = orderTurnNum;
		this.custScore = custScore;
		CustComplaints = custComplaints;
		CustPraise = custPraise;
		this.resOfOneYear = resOfOneYear;
	}
	public EngineerKpi() {
		super();
	}
}
