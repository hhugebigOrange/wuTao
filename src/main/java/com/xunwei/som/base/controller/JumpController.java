/*package com.xunwei.som.base.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.front.CustomerSatisfaction;
import com.xunwei.som.pojo.front.OrderManage;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

@Controller
public class JumpController extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private UserService userService = new UserServiceImpl();

	*//**
	 * 工单列表
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/orderList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderList(ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 登陆用户名
		String woStatus = request.getParameter("woStatus"); // 标识符
		String startDate = ExcelUtils.fmtOne.format(SOMUtils.initDateByMonth()); // 当月开始时间
		String endDate = ExcelUtils.fmtOne.format(new Date()); // 当前时间
		// 当前登录用户
		User user = userService.selectByUserId(username);
		UserRole userRole = userService.selectByPrimaryKey(username); // 当前用户的角色
		List<ServiceInfo> serviceInfos = new ArrayList<>();
		List<OrderManage> OrderManages = new ArrayList<>();
		if (userRole.getRoleId().equals("总部客服")) {
			user.setCustName("");
		}
		// 本月工单
		if (woStatus.equals("a")) {
			serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
					user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null, null);
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : serviceInfos) {
				service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
				service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
				if (service.getOrderInfo().getMachCode() != null) {
					service.setDevice(customerManage.selectDeviceById(service.getOrderInfo().getMachCode()));
					a1 = 1;
				} else {
					a1 = 2;
				}
				if (service.getOrderInfo().getPartsTypeNumber() == null) {
					part = "否";
				} else {
					part = "是";
				}
				if (a1 == 1) {
					OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
							service.getWoNumber(), service.getDevice().getDevName(), service.getDevice().getMachCode(),
							service.getOrderInfo().getRepairType(), service.getDevice().getEsNumber(),
							service.getDevice().getUnitType(), service.getDevice().getLocation(),
							service.getOrderInfo().getRepairMan(), service.getOrderInfo().getRepairService(),
							service.getOrderInfo().getRepairTime(),
							service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
							service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
							service.getOrderInfo().getMaintenanceFeedback());
					orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
							: service.getOrderInfo().getPartsTypeNumber().split(";"));
					orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					OrderManages.add(orderManage);
				} else {
					OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
							service.getWoNumber(), null, null, service.getOrderInfo().getRepairType(), null, null, null,
							service.getOrderInfo().getRepairMan(), service.getOrderInfo().getRepairService(),
							service.getOrderInfo().getRepairTime(),
							service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
							service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
							service.getOrderInfo().getMaintenanceFeedback());
					orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
							: service.getOrderInfo().getPartsTypeNumber().split(";"));
					orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					OrderManages.add(orderManage);
				}
			}
		}
		// 本月未完成
		else if (woStatus.equals("b")) {
			serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
					user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null, null);
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : serviceInfos) {
				service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
				service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
				if (service.getOrderInfo().getWoStatus().equals("已完成")
						|| service.getOrderInfo().getWoStatus().equals("已关单")
						|| service.getOrderInfo().getWoStatus().equals("已转单")) {
					continue;
				}
				if (service.getOrderInfo().getMachCode() != null) {
					service.setDevice(customerManage.selectDeviceById(service.getOrderInfo().getMachCode()));
					a1 = 1;
				} else {
					a1 = 2;
				}
				if (service.getOrderInfo().getPartsTypeNumber() == null) {
					part = "否";
				} else {
					part = "是";
				}
				if (a1 == 1) {
					OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
							service.getWoNumber(), service.getDevice().getDevName(), service.getDevice().getMachCode(),
							service.getOrderInfo().getRepairType(), service.getDevice().getEsNumber(),
							service.getDevice().getUnitType(), service.getDevice().getLocation(),
							service.getOrderInfo().getRepairMan(), service.getOrderInfo().getRepairService(),
							service.getOrderInfo().getRepairTime(),
							service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
							service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
							service.getOrderInfo().getMaintenanceFeedback());
					orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
							: service.getOrderInfo().getPartsTypeNumber().split(";"));
					orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					OrderManages.add(orderManage);
				} else {
					OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
							service.getWoNumber(), null, null, service.getOrderInfo().getRepairType(), null, null, null,
							service.getOrderInfo().getRepairMan(), service.getOrderInfo().getRepairService(),
							service.getOrderInfo().getRepairTime(),
							service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
							service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
							service.getOrderInfo().getMaintenanceFeedback());
					orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
							: service.getOrderInfo().getPartsTypeNumber().split(";"));
					orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					OrderManages.add(orderManage);
				}
			}
		} // 本月超时工单
		else if (woStatus.equals("c")) {
			serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
					user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null, null);
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : serviceInfos) {
				service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
				service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
				if (SOMUtils.isOverTime(service)) {
					if (service.getOrderInfo().getMachCode() != null) {
						service.setDevice(customerManage.selectDeviceById(service.getOrderInfo().getMachCode()));
						a1 = 1;
					} else {
						a1 = 2;
					}
					if (service.getOrderInfo().getPartsTypeNumber() == null) {
						part = "否";
					} else {
						part = "是";
					}
					if (a1 == 1) {
						OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
								service.getWoNumber(), service.getDevice().getDevName(),
								service.getDevice().getMachCode(), service.getOrderInfo().getRepairType(),
								service.getDevice().getEsNumber(), service.getDevice().getUnitType(),
								service.getDevice().getLocation(), service.getOrderInfo().getRepairMan(),
								service.getOrderInfo().getRepairService(), service.getOrderInfo().getRepairTime(),
								service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
								service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
								service.getOrderInfo().getMaintenanceFeedback());
						orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
								: service.getOrderInfo().getPartsTypeNumber().split(";"));
						orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
						orderManage.setEnginnerId(service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id"
								: service.getStaffInfo().getStaffId());
						orderManage.setWoState(service.getOrderInfo().getWoStatus());
						orderManage.setDescribe(service.getOrderInfo().getRemark());
						orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
								: service.getOrderInfo().getGetOrderTime());
						orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
						orderManage.setSendTime(service.getOrderInfo().getSendTime() == null ? null
								: service.getOrderInfo().getSendTime());
						OrderManages.add(orderManage);
					} else {
						OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
								service.getWoNumber(), null, null, service.getOrderInfo().getRepairType(), null, null,
								null, service.getOrderInfo().getRepairMan(), service.getOrderInfo().getRepairService(),
								service.getOrderInfo().getRepairTime(),
								service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
								service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
								service.getOrderInfo().getMaintenanceFeedback());
						orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
								: service.getOrderInfo().getPartsTypeNumber().split(";"));
						orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
						orderManage.setEnginnerId(service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id"
								: service.getStaffInfo().getStaffId());
						orderManage.setWoState(service.getOrderInfo().getWoStatus());
						orderManage.setDescribe(service.getOrderInfo().getRemark());
						orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
								: service.getOrderInfo().getGetOrderTime());
						orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
						orderManage.setSendTime(service.getOrderInfo().getSendTime() == null ? null
								: service.getOrderInfo().getSendTime());
						OrderManages.add(orderManage);
					}
				}
			}
		} else if (woStatus.equals("d")) {
			serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
					user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null, null);
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : serviceInfos) {
				service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
				service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
				if (service.getOrderInfo().getWoStatus().equals("待受理")) {
					if (service.getOrderInfo().getMachCode() != null) {
						service.setDevice(customerManage.selectDeviceById(service.getOrderInfo().getMachCode()));
						a1 = 1;
					} else {
						a1 = 2;
					}
					if (service.getOrderInfo().getPartsTypeNumber() == null) {
						part = "否";
					} else {
						part = "是";
					}
					if (a1 == 1) {
						OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
								service.getWoNumber(), service.getDevice().getDevName(),
								service.getDevice().getMachCode(), service.getOrderInfo().getRepairType(),
								service.getDevice().getEsNumber(), service.getDevice().getUnitType(),
								service.getDevice().getLocation(), service.getOrderInfo().getRepairMan(),
								service.getOrderInfo().getRepairService(), service.getOrderInfo().getRepairTime(),
								service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
								service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
								service.getOrderInfo().getMaintenanceFeedback());
						orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
								: service.getOrderInfo().getPartsTypeNumber().split(";"));
						orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
						orderManage.setEnginnerId(service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id"
								: service.getStaffInfo().getStaffId());
						orderManage.setWoState(service.getOrderInfo().getWoStatus());
						orderManage.setDescribe(service.getOrderInfo().getRemark());
						orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
								: service.getOrderInfo().getGetOrderTime());
						orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
						orderManage.setSendTime(service.getOrderInfo().getSendTime() == null ? null
								: service.getOrderInfo().getSendTime());
						OrderManages.add(orderManage);
					} else {
						OrderManage orderManage = new OrderManage(service.getOrderInfo().getCustName(),
								service.getWoNumber(), null, null, service.getOrderInfo().getRepairType(), null, null,
								null, service.getOrderInfo().getRepairMan(), service.getOrderInfo().getRepairService(),
								service.getOrderInfo().getRepairTime(),
								service.getStaffInfo() == null ? "等待指派" : service.getStaffInfo().getName(),
								service.getOrderInfo().getFaultType(), service.getOrderInfo().getAccidentType(), part,
								service.getOrderInfo().getMaintenanceFeedback());
						orderManage.setPartMessage(service.getOrderInfo().getPartsTypeNumber() == null ? null
								: service.getOrderInfo().getPartsTypeNumber().split(";"));
						orderManage.setCustAddr(service.getOrderInfo().getCustAddr());
						orderManage.setEnginnerId(service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id"
								: service.getStaffInfo().getStaffId());
						orderManage.setWoState(service.getOrderInfo().getWoStatus());
						orderManage.setDescribe(service.getOrderInfo().getRemark());
						orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
								: service.getOrderInfo().getGetOrderTime());
						orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
						orderManage.setSendTime(service.getOrderInfo().getSendTime() == null ? null
								: service.getOrderInfo().getSendTime());
						OrderManages.add(orderManage);
					}
				}
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, OrderManages, page, limit);
		if (para[0] == null) {
			page = null;
			limit = null;
		} else if (para[0] == -1) {
			json.put("code", 1);
			json.put("msg", "当前已经是最后一页了");
			return json;
		} else {
			page = para[0];
			limit = para[1];
		}
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "工单管理数据");
		json.put("count", OrderManages == null ? 0 : OrderManages.size());
		json.put("data", page == null || OrderManages == null ? OrderManages : OrderManages.subList(page, limit));
		return json;
	}

	*//**
	 * 客户满意度列表
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/customerSatisfactionList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerSatisfactionList() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 登陆用户名
		String woStatus = request.getParameter("woStatus"); // 标识符
		String startDate = ExcelUtils.fmtOne.format(SOMUtils.initDateByMonth()); // 当月开始时间
		String endDate = ExcelUtils.fmtOne.format(new Date()); // 当前时间
		// 当前登录用户
		User user = userService.selectByUserId(username);
		UserRole userRole = userService.selectByPrimaryKey(username); // 当前用户的角色
		List<ServiceInfo> serviceInfos = new ArrayList<>();
		List<CustomerSatisfaction> CustomerSatisfactions = new ArrayList<>();
		if (userRole.getRoleId().equals("总部客服")) {
			user.setCustName("");
		}
		serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
				user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null, null);
		// 用户评价总数
		if (woStatus.equals("a")) {
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : serviceInfos) {
				if (service.getProbSolve() == null) {
					continue;
				}
				CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
				service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
				service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
				CustomerSatisfaction.setCustName(service.getOrderInfo().getCustName());
				CustomerSatisfaction.setServiceArea(service.getStaffInfo().getCompName());
				CustomerSatisfaction.setWoNumber(service.getWoNumber());
				CustomerSatisfaction.setRepairMan(service.getOrderInfo().getRepairMan());
				CustomerSatisfaction.setRepairPhone(service.getOrderInfo().getRepairService());
				CustomerSatisfaction.setRepairTime(service.getOrderInfo().getRepairTime());
				CustomerSatisfaction.setCompleteTime(service.getProbSolve());
				CustomerSatisfaction.setEnginnerName(service.getStaffInfo().getName());
				CustomerSatisfaction.setCustScore(service.getCustScore());
				CustomerSatisfaction.setCustEva(service.getCustEva());
				CustomerSatisfaction.setRemake(service.getOrderInfo().getRemark());
				CustomerSatisfactions.add(CustomerSatisfaction);
			}
		} // 用户投诉
		else if (woStatus.equals("b")) {
			for (ServiceInfo service : serviceInfos) {
				if (service.getProbSolve() == null) {
					continue;
				}
				if (service.getCustScore() != null && service.getCustScore() <= 3) {
					CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
					service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
					service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
					CustomerSatisfaction.setCustName(service.getOrderInfo().getCustName());
					CustomerSatisfaction.setServiceArea(service.getStaffInfo().getCompName());
					CustomerSatisfaction.setWoNumber(service.getWoNumber());
					CustomerSatisfaction.setRepairMan(service.getOrderInfo().getRepairMan());
					CustomerSatisfaction.setRepairPhone(service.getOrderInfo().getRepairService());
					CustomerSatisfaction.setRepairTime(service.getOrderInfo().getRepairTime());
					CustomerSatisfaction.setCompleteTime(service.getProbSolve());
					CustomerSatisfaction.setEnginnerName(service.getStaffInfo().getName());
					CustomerSatisfaction.setCustScore(service.getCustScore());
					CustomerSatisfaction.setCustEva(service.getCustEva());
					CustomerSatisfaction.setRemake(service.getOrderInfo().getRemark());
					CustomerSatisfactions.add(CustomerSatisfaction);
				}
			}
		} // 用户表扬
		else if (woStatus.equals("c")) {
			for (ServiceInfo service : serviceInfos) {
				if (service.getProbSolve() == null) {
					continue;
				}
				if (service.getCustScore() != null && service.getCustScore() == 5) {
					CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
					service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
					service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
					CustomerSatisfaction.setCustName(service.getOrderInfo().getCustName());
					CustomerSatisfaction.setServiceArea(service.getStaffInfo().getCompName());
					CustomerSatisfaction.setWoNumber(service.getWoNumber());
					CustomerSatisfaction.setRepairMan(service.getOrderInfo().getRepairMan());
					CustomerSatisfaction.setRepairPhone(service.getOrderInfo().getRepairService());
					CustomerSatisfaction.setRepairTime(service.getOrderInfo().getRepairTime());
					CustomerSatisfaction.setCompleteTime(service.getProbSolve());
					CustomerSatisfaction.setEnginnerName(service.getStaffInfo().getName());
					CustomerSatisfaction.setCustScore(service.getCustScore());
					CustomerSatisfaction.setCustEva(service.getCustEva());
					CustomerSatisfaction.setRemake(service.getOrderInfo().getRemark());
					CustomerSatisfactions.add(CustomerSatisfaction);
				}
			}
		} else if (woStatus.equals("1") || woStatus.equals("2") || woStatus.equals("3") || woStatus.equals("4")
				|| woStatus.equals("5")) {
			int score = Integer.valueOf(woStatus);
			for (ServiceInfo service : serviceInfos) {
				if (service.getProbSolve() == null) {
					continue;
				}
				if (service.getCustScore() != null && service.getCustScore() == score) {
					CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
					service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
					service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
					CustomerSatisfaction.setCustName(service.getOrderInfo().getCustName());
					CustomerSatisfaction.setServiceArea(service.getStaffInfo().getCompName());
					CustomerSatisfaction.setWoNumber(service.getWoNumber());
					CustomerSatisfaction.setRepairMan(service.getOrderInfo().getRepairMan());
					CustomerSatisfaction.setRepairPhone(service.getOrderInfo().getRepairService());
					CustomerSatisfaction.setRepairTime(service.getOrderInfo().getRepairTime());
					CustomerSatisfaction.setCompleteTime(service.getProbSolve());
					CustomerSatisfaction.setEnginnerName(service.getStaffInfo().getName());
					CustomerSatisfaction.setCustScore(service.getCustScore());
					CustomerSatisfaction.setCustEva(service.getCustEva());
					CustomerSatisfaction.setRemake(service.getOrderInfo().getRemark());
					CustomerSatisfactions.add(CustomerSatisfaction);
				}
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, CustomerSatisfactions, page, limit);
		if (para[0] == null) {
			page = null;
			limit = null;
		} else if (para[0] == -1) {
			json.put("code", 1);
			json.put("msg", "当前已经是最后一页了");
			return json;
		} else {
			page = para[0];
			limit = para[1];
		}
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "客户满意度管理数据");
		json.put("count", CustomerSatisfactions == null ? 0 : CustomerSatisfactions.size());
		json.put("data", page == null || CustomerSatisfactions == null ? CustomerSatisfactions
				: CustomerSatisfactions.subList(page, limit));
		return json;
	}

	*//**
	 * 合同列表
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/contractManageList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> contractManageList() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 登陆用户名
		String woStatus = request.getParameter("woStatus"); // 标识符
		// 当前登录用户
		User user = userService.selectByUserId(username);
		UserRole userRole = userService.selectByPrimaryKey(username); // 当前用户的角色
		List<Contract> contracts = new ArrayList<>();
		Date date = new Date();
		if (userRole.getRoleId().equals("总部客服")) {
			user.setCustName("");
		}
		if (woStatus.equals("a")) {
			contracts = customerManage.selectByCust(user.getCustName(), SOMUtils.orderNumToComp(user.getCustName()), "",
					"", null, null, null);
			// 保存数据
			for (Contract contract : contracts) {
				// 合同期限
				long a = contract.getEndDate().getTime() - contract.getStartDate().getTime();
				int b = (int) (a / (1000 * 3600 * 24));
				Double ContractDeadline = Double.valueOf(b) / 30;
				contract.setContractDeadline(ContractDeadline);
				// 离到期天数
				long c = contract.getEndDate().getTime() - date.getTime();
				if (c > 0) {
					int d = (int) (c / (1000 * 3600 * 24));
					contract.setDueDays(d);
				} else {
					contract.setDueDays(0);
				}
			}
		} else if (woStatus.equals("b")) {
			contracts = customerManage.selectByCust(user.getCustName(), SOMUtils.orderNumToComp(user.getCustName()),
					"1", "", null, null, null);
			// 保存数据
			for (Contract contract : contracts) {
				// 合同期限
				long a = contract.getEndDate().getTime() - contract.getStartDate().getTime();
				int b = (int) (a / (1000 * 3600 * 24));
				Double ContractDeadline = Double.valueOf(b) / 30;
				contract.setContractDeadline(ContractDeadline);
				// 离到期天数
				long c = contract.getEndDate().getTime() - date.getTime();
				if (c > 0) {
					int d = (int) (c / (1000 * 3600 * 24));
					contract.setDueDays(d);
				} else {
					contract.setDueDays(0);
				}
			}
		} else if (woStatus.equals("c")) {
			contracts = customerManage.selectByCust(user.getCustName(), SOMUtils.orderNumToComp(user.getCustName()),
					"1", "", null, null, null);
			// 保存数据
			for (Contract contract : contracts) {
				// 合同期限
				long a = contract.getEndDate().getTime() - contract.getStartDate().getTime();
				int b = (int) (a / (1000 * 3600 * 24));
				Double ContractDeadline = Double.valueOf(b) / 30;
				contract.setContractDeadline(ContractDeadline);
				// 离到期天数
				long c = contract.getEndDate().getTime() - date.getTime();
				if (c > 0) {
					int d = (int) (c / (1000 * 3600 * 24));
					contract.setDueDays(d);
				} else {
					contract.setDueDays(0);
				}
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, contracts, page, limit);
		if (para[0] == null) {
			page = null;
			limit = null;
		} else if (para[0] == -1) {
			json.put("code", 1);
			json.put("msg", "当前已经是最后一页了");
			return json;
		} else {
			page = para[0];
			limit = para[1];
		}
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "合同管理数据");
		json.put("count", contracts == null ? 0 : contracts.size());
		json.put("data", page == null || contracts == null ? contracts : contracts.subList(page, limit));
		return json;
	}

	*//**
	 * 设备列表
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/equipmentInfoList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> equipmentInfoList() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 登陆用户名
		String woStatus = request.getParameter("woStatus"); // 标识符
		// 当前登录用户
		User user = userService.selectByUserId(username);
		UserRole userRole = userService.selectByPrimaryKey(username); // 当前用户的角色
		List<Device> devices = new ArrayList<>();
		if (userRole.getRoleId().equals("总部客服")) {
			user.setCustName("");
		}
		if (woStatus.equals("a")) {
			devices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(),
					null, null);
		} else if (woStatus.equals("b")) {
			devices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(),
					"客户", null);
		} else if (woStatus.equals("c")) {
		   List<Device>	devicess = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(),
					null, null);
			for (Device device : devicess) {
				if(!device.getAssetAttr().equals("客户")){
					devices.add(device);
				}
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, devices, page, limit);
		if (para[0] == null) {
			page = null;
			limit = null;
		} else if (para[0] == -1) {
			json.put("code", 1);
			json.put("msg", "当前已经是最后一页了");
			return json;
		} else {
			page = para[0];
			limit = para[1];
		}
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "设备管理数据");
		json.put("count", devices == null ? 0 : devices.size());
		json.put("data", page == null || devices == null ? devices : devices.subList(page, limit));
		return json;
	}
}
*/