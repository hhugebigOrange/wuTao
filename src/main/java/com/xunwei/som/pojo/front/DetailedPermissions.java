package com.xunwei.som.pojo.front;

public class DetailedPermissions {

	public boolean assistant; //运维助理首页
	public boolean client;    //客户端登陆后首页
	public boolean majordomo; // 运维助理首页
	public boolean operations; // 运维助理首页
	public boolean serviceHome; // 运维助理首页
	public boolean workerOrder; // 工单概况
	public boolean contract; // 合同概况
	public boolean atWork; // 在岗状态
	public boolean satisfaction; // 客户满意度
	public boolean custmorMsg; // 客户信息
	public boolean equipment; // 设备概况
	public boolean calendar; // 工作日历
	public boolean userManage; // 员工信息
	public boolean contractMsg; // 合同管理
	public boolean contractSatis; // 客户满意度管理
	public boolean workMsg; // 工单管理
	public boolean workMsgAdd; // 新增工单
	public boolean faultAnalysis; // 故障分析
	public boolean equipmentManage; // 设备管理
	public boolean assets; // 资产管理
	public boolean plan; // 保养计划
	public boolean implement; // 保养执行
	public boolean engineer; // 工程师KPI报表
	public boolean customerMsgr; // 客户KPI报表
	public boolean branchOffice; // 分公司KPI
	public boolean headquarters; // 总公司运维KPI
	public boolean service; // 坐席客服KPI
	public boolean orderAnalysis; // 工单分析表
	public boolean repair; // 维修报表
	public boolean operationRate; // 设备运转正常率
	public boolean maintain; // 保养报表
	public boolean equipmentReq; // 设备管理报表
	public boolean contractForm; // 运维合同报表
	public boolean engineerWork; // 在岗状态
	public boolean area; // 区域管理
	public boolean jurisdiction; // 权限分配
	public boolean pc; // PC端权限分配
	public boolean moble; // 移动端权限分配
	public boolean account; // 账户管理
	public boolean amend; // 密码修改
	public boolean parameter; // 参数设定
	
	
	
	
	
	
	
	public DetailedPermissions(boolean assistant, boolean client, boolean majordomo, boolean operations,
			boolean serviceHome, boolean workerOrder, boolean contract, boolean atWork, boolean satisfaction,
			boolean custmorMsg, boolean equipment, boolean calendar, boolean userManage, boolean contractMsg,
			boolean contractSatis, boolean workMsg, boolean workMsgAdd, boolean faultAnalysis, boolean equipmentManage,
			boolean assets, boolean plan, boolean implement, boolean engineer, boolean customerMsgr,
			boolean branchOffice, boolean headquarters, boolean service, boolean orderAnalysis, boolean repair,
			boolean operationRate, boolean maintain, boolean equipmentReq, boolean contractForm, boolean engineerWork,
			boolean area, boolean jurisdiction, boolean pc, boolean moble, boolean account, boolean amend,
			boolean parameter) {
		super();
		this.assistant = assistant;
		this.client = client;
		this.majordomo = majordomo;
		this.operations = operations;
		this.serviceHome = serviceHome;
		this.workerOrder = workerOrder;
		this.contract = contract;
		this.atWork = atWork;
		this.satisfaction = satisfaction;
		this.custmorMsg = custmorMsg;
		this.equipment = equipment;
		this.calendar = calendar;
		this.userManage = userManage;
		this.contractMsg = contractMsg;
		this.contractSatis = contractSatis;
		this.workMsg = workMsg;
		this.workMsgAdd = workMsgAdd;
		this.faultAnalysis = faultAnalysis;
		this.equipmentManage = equipmentManage;
		this.assets = assets;
		this.plan = plan;
		this.implement = implement;
		this.engineer = engineer;
		this.customerMsgr = customerMsgr;
		this.branchOffice = branchOffice;
		this.headquarters = headquarters;
		this.service = service;
		this.orderAnalysis = orderAnalysis;
		this.repair = repair;
		this.operationRate = operationRate;
		this.maintain = maintain;
		this.equipmentReq = equipmentReq;
		this.contractForm = contractForm;
		this.engineerWork = engineerWork;
		this.area = area;
		this.jurisdiction = jurisdiction;
		this.pc = pc;
		this.moble = moble;
		this.account = account;
		this.amend = amend;
		this.parameter = parameter;
	}
	
	public DetailedPermissions() {
		super();
	}
	
}
