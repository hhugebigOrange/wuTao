package com.xunwei.som.pojo;

import java.util.Date;

/**
 * 坐席客服Kpi
 * @author Administrator
 *
 */
public class SeatServiceKpi {

	    //员工编号
	    private String staffId;
	    // 服务区域
		private String serviceArea;
		// 客服名称
		private String staffName;
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
		// 工单数量
		private int orderNum;
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
		public String getStaffId() {
			return staffId;
		}
		public void setStaffId(String staffId) {
			this.staffId = staffId;
		}
		public String getServiceArea() {
			return serviceArea;
		}
		public void setServiceArea(String serviceArea) {
			this.serviceArea = serviceArea;
		}
		public String getStaffName() {
			return staffName;
		}
		public void setStaffName(String staffName) {
			this.staffName = staffName;
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
		public int getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(int orderNum) {
			this.orderNum = orderNum;
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
	    
}
