package com.xunwei.som.base.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.OrderParts;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.FailureAnalysis;
import com.xunwei.som.pojo.front.FailureAnalysisHistory;
import com.xunwei.som.pojo.front.OrderManage;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SMS;
import com.xunwei.som.util.SOMUtils;

import sun.misc.BASE64Decoder;

/**
 * 服务管理
 * 
 * @author Administrator
 *
 */

@Controller
public class ServiceManageController extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private CustInfoService custInfo = new CustInfoServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private UserService userService = new UserServiceImpl();

	// 用来存放每次查询的客户满意度结果集
	private List<ServiceInfo> serviceInfos = new ArrayList<>();

	// 用于保存每个用户的查询记录
	private Map<String, Object> export = new HashMap<>();

	/**
	 * 用于每次查询后，保存查询后的结果
	 */
	private List<OrderInfo> orderInfos;

	/**
	 * 匹配工单管理,查询所有工单
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/orderManage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderManage() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端获取参数
		String custName = request.getParameter("custName");
		String machCode = request.getParameter("machCode");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String rapairType = request.getParameter("rapairType");
		String processingState = request.getParameter("ProcessingState");
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		String c = request.getParameter("c");
		String woStatus = request.getParameter("woStatus"); // 标识符
		String enginnerName = request.getParameter("enginnerName"); // 工程师姓名
		String serviceArea = request.getParameter("serviceArea"); // 服务区域
		Date date = new Date();
		String f = "";
		String[] faultType = null;
		if (a != null && !a.equals("")) {
			f = "事故类,";
		}
		if (b != null && !b.equals("")) {
			f = f + "事件类,";
		}
		if (c != null && !c.equals("")) {
			f = f + "需求类,";
		}
		if (!f.equals("")) {
			faultType = f.split(",");
		}
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate + " 00:00:00";
		} else {
			startDate = ExcelUtils.fmt.format(DateUtils.addDays(date, -30)) + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		} else {
			endDate = ExcelUtils.fmt.format(date) + " 23:59:59";
		}
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		List<ServiceInfo> selectServiceInfos = new ArrayList<>();
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageWithLimit(request, page, limit);
		if (para[0] == null) {
			// 默认显示第一页数据
			page = 0;
			limit = 10;
		} else {
			page = para[0];
			limit = para[1];
		}
		// 当前登录用户
		String identifier = null;
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		List<OrderManage> OrderManages = new ArrayList<>();
		if (serviceArea == null || serviceArea.equals("")) {
			serviceArea = "";
		}
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		// 本月工单
		if (woStatus != null && !woStatus.trim().equals("") && woStatus.equals("a")) {
			startDate = ExcelUtils.fmtOne.format(SOMUtils.initDateByMonth()); // 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date()); // 当前时间
			if (userRole.getRoleId().equals("客户")) {
				serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
						user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null,
						null, enginnerName, identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByDynamic(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), startDate, endDate, null, null,
						null, null, null, null, null, page, limit, null, enginnerName, identifier);
			} else {
				serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
						"", startDate, endDate, null, null, null, null, null, null, null, null, null, null,
						enginnerName, identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByDynamic(
						SOMUtils.orderNumToComp(user.getCustName()), "", startDate, endDate, null, null, null, null,
						null, null, null, page, limit, null, enginnerName, identifier);
			}
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : selectServiceInfos) {
				if (service.getOrderInfo().getMachCode() != null) {
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setReserveEngineer(service.getDevice().getReserveEnginner() == null ? null
							: service.getDevice().getReserveEnginner());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
					OrderManages.add(orderManage);
				}
			}
		}
		// 所有未完成
		else if (woStatus != null && !woStatus.trim().equals("") && woStatus.equals("b")) {
			if (userRole.getRoleId().equals("客户")) {
				serviceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), startDate, endDate, null, null,
						"1", identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), startDate, endDate, page,
						limit, "1", identifier);
			} else {
				serviceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), "", startDate, endDate, null, null, "1",
						identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), "", startDate, endDate, page, limit, "1",
						identifier);
			}
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : selectServiceInfos) {
				if (service.getOrderInfo().getMachCode() != null) {
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setReserveEngineer(service.getDevice().getReserveEnginner() == null ? null
							: service.getDevice().getReserveEnginner());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
					OrderManages.add(orderManage);
				}
			}

		} // 本月超时工单
		else if (woStatus != null && !woStatus.trim().equals("") && woStatus.equals("c")) {
			startDate = ExcelUtils.fmtOne.format(SOMUtils.initDateByMonth()); // 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date()); // 当前时间
			String[] fault = { "事故类" };
			if (userRole.getRoleId().equals("客户")) {
				selectServiceInfos = serviceInfoService.selectServiceInfByDynamic(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), startDate, endDate, null, null,
						null, null, null, null, fault, null, null, null, enginnerName, identifier);
			} else {
				selectServiceInfos = serviceInfoService.selectServiceInfByDynamic(
						SOMUtils.orderNumToComp(user.getCustName()), "", startDate, endDate, null, null, null, null,
						null, null, fault, null, null, null, enginnerName, identifier);
			}
			String part = "";
			Integer a1 = null;
			List<ServiceInfo> serviceInfoss = new ArrayList<>();
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : selectServiceInfos) {
				if (SOMUtils.isOverTime(service)) {
					serviceInfoss.add(service);
					if (service.getOrderInfo().getMachCode() != null) {
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
						orderManage.setServiceType(service.getOrderInfo().getServiceType());
						orderManage.setDescribe(service.getOrderInfo().getRemark());
						orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
								: service.getOrderInfo().getGetOrderTime());
						orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
						orderManage.setSendTime(service.getOrderInfo().getSendTime() == null ? null
								: service.getOrderInfo().getSendTime());
						orderManage.setReserveEngineer(service.getDevice().getReserveEnginner() == null ? null
								: service.getDevice().getReserveEnginner());
						orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
						orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
						orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
						orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
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
						orderManage.setServiceType(service.getOrderInfo().getServiceType());
						orderManage.setDescribe(service.getOrderInfo().getRemark());
						orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
								: service.getOrderInfo().getGetOrderTime());
						orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
						orderManage.setSendTime(service.getOrderInfo().getSendTime() == null ? null
								: service.getOrderInfo().getSendTime());
						orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
						orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
						orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
						orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
						OrderManages.add(orderManage);
					}
				}
			}
			serviceInfos = serviceInfoss;
			limit = page + limit;
			// 如果开始坐标都比条目数大，则默认显示前10条，不足10条全部显示
			if (page > OrderManages.size()) {
				if (OrderManages.size() >= 10) {
					OrderManages = OrderManages.subList(0, 10);
				}
			} // 如果结束坐标比条目数大，则结束下标就为结束下标。
			else if (limit > OrderManages.size()) {
				limit = OrderManages.size();
				OrderManages = OrderManages.subList(page, limit);
			} else {
				OrderManages = OrderManages.subList(page, limit);
			}
		} else if (woStatus != null && !woStatus.trim().equals("") && woStatus.equals("d")) {
			if (userRole.getRoleId().equals("客户")) {
				serviceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), null, null, null, null, "2",
						identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), null, null, page, limit, "2",
						identifier);
			} else {
				serviceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), "", null, null, null, null, "2", identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByWoStatus(
						SOMUtils.orderNumToComp(user.getCustName()), "", null, null, page, limit, "2", identifier);
			}
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : selectServiceInfos) {
				if (service.getOrderInfo().getMachCode() != null) {
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setReserveEngineer(service.getDevice().getReserveEnginner() == null ? null
							: service.getDevice().getReserveEnginner());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
					OrderManages.add(orderManage);
				}
			}
		} else {
			if (userRole.getRoleId().equals("客户")) {
				serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
						user.getCustName(), startDate, endDate, "", "", machCode, "", "", rapairType, faultType, null,
						null, processingState, enginnerName, identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByDynamic(
						SOMUtils.orderNumToComp(user.getCustName()), user.getCustName(), startDate, endDate, "", "",
						machCode, "", "", rapairType, faultType, page, limit, processingState, enginnerName,
						identifier);
			} else {
				serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
						custName, startDate, endDate, "", "", machCode, "", "", rapairType, faultType, null, null,
						processingState, enginnerName, identifier);
				selectServiceInfos = serviceInfoService.selectServiceInfByDynamic(
						SOMUtils.orderNumToComp(user.getCustName()), custName, startDate, endDate, "", "", machCode, "",
						"", rapairType, faultType, page, limit, processingState, enginnerName, identifier);
			}
			String part = "";
			Integer a1 = null;
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : selectServiceInfos) {
				if (service.getOrderInfo().getMachCode() != null) {
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setReserveEngineer(service.getDevice().getReserveEnginner() == null ? null
							: service.getDevice().getReserveEnginner());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
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
					orderManage.setServiceType(service.getOrderInfo().getServiceType());
					orderManage.setEnginnerId(
							service.getStaffInfo() == null ? "该工单尚未指派,无工程师Id" : service.getStaffInfo().getStaffId());
					orderManage.setWoState(service.getOrderInfo().getWoStatus());
					orderManage.setDescribe(service.getOrderInfo().getRemark());
					orderManage.setGetOrderTime(service.getOrderInfo().getGetOrderTime() == null ? null
							: service.getOrderInfo().getGetOrderTime());
					orderManage.setProbSolveTime(service.getProbSolve() == null ? null : service.getProbSolve());
					orderManage.setSendTime(
							service.getOrderInfo().getSendTime() == null ? null : service.getOrderInfo().getSendTime());
					orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
					orderManage.setTreatmentMeasure(service.getOrderInfo().getTreatmentMeasure());
					orderManage.setArrTime(service.getArrTime() == null ? null : service.getArrTime());
					orderManage.setPicture(SOMUtils.picture(service.getWoNumber()));
					OrderManages.add(orderManage);
				}
			}
		}
		export.put(request.getParameter("username") + "orderManage", serviceInfos);
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "工单管理数据");
		json.put("data", OrderManages);
		json.put("count", serviceInfos.size());
		return json;
	}

	@RequestMapping("/getOrer")
	@ResponseBody
	public List<ServiceInfo> getOrder() {
		// 从前端接受工单号
		String woNumber = request.getParameter("woNumber");
		List<ServiceInfo> serviceInfo = serviceInfoService.selectServiceInfByDynamic2("", woNumber, "");
		for (ServiceInfo service : serviceInfo) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		return serviceInfo;
	}

	/**
	 * 匹配故障分析页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/failureAnalysis", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> failureAnalysis(ModelAndView modelAndView) throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		String machCode = request.getParameter("machCode");
		String identifier = null;
		// 判断是用户还是公司账号
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		if (serviceArea != null) {
			if (serviceArea.equals("广州乐派数码科技有限公司") || serviceArea.equals("系统推进部") || serviceArea.equals("行业客户部")) {
				serviceArea = request.getParameter("branceName");
				identifier = "1";
			}
		}
		// 先查询出所有已完成的服务工单
		List<ServiceInfo> serviceInfo = serviceInfoService.selectServiceInfByDynamic(
				serviceArea == null ? null : SOMUtils.orderNumToComp(serviceArea), custName, "", "", "", "", machCode,
				"", "", "", null, null, null, null, null, identifier);
		List<ServiceInfo> serviceInfos = new ArrayList<>();
		List<FailureAnalysis> FailureAnalysiss = new ArrayList<>();
		int day = 0;
		String downtime = "";
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfo) {
			// 判断是否有机器编码
			if (service.getOrderInfo().getMachCode() != null) {
				// 先判断是否解决，只有解决了才设置停机时间
				if (service.getProbSolve() != null) {
					day = (int) ((service.getProbSolve().getTime() - service.getOrderInfo().getSendTime().getTime())
							/ (60 * 60 * 1000));
					downtime = String.valueOf(day);
					FailureAnalysis failureAnalysi = new FailureAnalysis(service.getOrderInfo().getCustName(),
							service.getStaffInfo().getCompName(), service.getOrderInfo().getMachCode(),
							service.getDevice().getDevName(), service.getOrderInfo().getFaultType(), downtime,
							service.getOrderInfo().getRepairTime());
					FailureAnalysiss.add(failureAnalysi);
					service.setDownTime(downtime);
					serviceInfos.add(service);
				}
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, FailureAnalysiss, page, limit);
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
		export.put(request.getParameter("username") + "failureAnalysis", serviceInfos);
		json.put("code", 0);
		json.put("msg", "故障分析数据");
		json.put("count", FailureAnalysiss.size());
		json.put("data",
				page == null || FailureAnalysiss == null ? FailureAnalysiss : FailureAnalysiss.subList(page, limit));
		return json;
	}

	/**
	 * 查看匹配故障履历
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/faultRecord", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> faultRecord(ModelAndView modelAndView) throws ParseException {
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String machCode = request.getParameter("machCode");
		if (machCode == null || machCode.equals("")) {
			json.put("code", 1);
			json.put("msg", "查看历史时，机器编码不能为空");
			return json;
		}
		// 先查询出所有已完成的服务工单
		List<ServiceInfo> serviceInfo = serviceInfoService.selectServiceInfByDynamic("", "", "", "", "", "", machCode,
				"", "", "", null, null, null, null, null, null);
		if (serviceInfo == null || serviceInfo.size() == 0) {
			json.put("code", 1);
			json.put("msg", "该机器编码没有历史报修记录");
			return json;
		}
		List<FailureAnalysisHistory> FailureAnalysisHistorys = new ArrayList<>();
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfo) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			service.setDevice(customerManage.selectDeviceById(service.getOrderInfo().getMachCode()));
			// 先判断是否解决，只有解决了才设置停机时间
			if (service.getProbSolve() != null) {
				FailureAnalysisHistory failureAnalysisHistory = new FailureAnalysisHistory(
						service.getOrderInfo().getCustName(), service.getWoNumber(), service.getDevice().getDevName(),
						service.getDevice().getMachCode(), service.getOrderInfo().getRepairType(),
						service.getDevice().getEsNumber(), service.getDevice().getUnitType(),
						service.getDevice().getLocation(), service.getOrderInfo().getRepairMan(),
						service.getOrderInfo().getRepairService(), service.getOrderInfo().getRepairTime(),
						service.getStaffInfo().getName(), service.getOrderInfo().getFaultType(),
						service.getOrderInfo().getAccidentType(), service.getOrderInfo().getMaintenanceFeedback(),
						service.getProbSolve(), service.getOrderInfo().getTreatmentMeasure(),
						service.getOrderInfo().getTreatmentState());
				failureAnalysisHistory.setFaultNo(service.getOrderInfo().getFalutNo());
				FailureAnalysisHistorys.add(failureAnalysisHistory);
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, FailureAnalysisHistorys, page, limit);
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
		json.put("code", 0);
		json.put("msg", "故障履历历史");
		json.put("count", FailureAnalysisHistorys.size());
		json.put("data", page == null || FailureAnalysisHistorys == null ? FailureAnalysisHistorys
				: FailureAnalysisHistorys.subList(page, limit));
		json.put("machCode", machCode);
		return json;
	}

	/**
	 * 匹配故障分析修改页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/updateFaiure")
	public ModelAndView updateFaiure(ModelAndView modelAndView) {
		// 从前端接收参数
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("/serviceManage/html/updateFailure");
		return modelAndView;
	}

	/**
	 * 方法：修改工单故障类型
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/doUpdateFaiure")
	public ModelAndView doUpdateFaiure(ModelAndView modelAndView) {
		// 从前端接收参数
		String workState = request.getParameter("workState");
		String id = request.getParameter("id");
		serviceManageServiceImpl.updateFaultType(id, workState);
		modelAndView.setViewName("redirect:/failureAnalysis");
		return modelAndView;
	}

	/**
	 * 
	 */
	/**
	 * 方法：导出故障分析页面
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportFaiure", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportFaiure(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "故障分析表";
		List<ServiceInfo> serviceInfos = (List<ServiceInfo>) export
				.get(request.getParameter("username") + "failureAnalysis");
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "机器编码", "设备名称", "故障类型", "停机时间(分钟)", "创建时间" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (ServiceInfo serviceInfo : serviceInfos) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(serviceInfo.getOrderInfo().getCustName());
			row.createCell(2).setCellValue(serviceInfo.getStaffInfo().getCompName());
			row.createCell(3).setCellValue(serviceInfo.getOrderInfo().getMachCode());
			row.createCell(4).setCellValue(serviceInfo.getOrderInfo().getDevName());
			row.createCell(5).setCellValue(serviceInfo.getOrderInfo().getFaultType());
			row.createCell(6).setCellValue(serviceInfo.getDownTime());
			row.createCell(7).setCellValue(ExcelUtils.fmtOne.format(serviceInfo.getOrderInfo().getSendTime()));
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * 方法：自动生成相应工单号
	 * 
	 */
	public String newOrderNo(String compName) {
		synchronized (this) {
			// 自动生成工单号：例如BA201810220001
			// 前缀
			String prefix = SOMUtils.orderNumToComp(compName);
			System.out.println("前缀为：" + prefix);
			// 找出前一个工单号
			String lastOrderNumber = serviceManageServiceImpl.selectLastOrderNumber(prefix);
			// 获取当前系统日期，比对上一个工单号日期，若不是同一天，则工单号直接为0001，若为同一天，则在上一个工单号上+1
			Date now = new Date();
			String a = ExcelUtils.fmt.format(now);
			String[] a2 = a.split("-");
			String orderNumber = a2[0] + a2[1] + a2[2];// 转换为没有"-"的日期格式
			// 如果lastOrderNumber为空，则证明这是该前缀的第一个工单
			if (lastOrderNumber == null || lastOrderNumber.equals("")) {
				orderNumber = prefix + orderNumber + "0001";
				return orderNumber;
			}
			// 如果日期相等，在最后的位数上+1
			if (orderNumber.equals(lastOrderNumber.substring(2, 10))) {
				int a1 = Integer.parseInt(lastOrderNumber.substring(10)) + 1;
				String suffix = String.format("%4d", a1).replace(" ", "0");
				orderNumber = prefix + orderNumber + suffix;
			} else {
				orderNumber = prefix + orderNumber + "0001";
			}
			return orderNumber;
		}

	}

	/**
	 * 工程师扫一扫报修
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/enginnerScannerOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> enginnerScannerOrder() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		// 机器编码
		String machCode = request.getParameter("machCode");
		if (machCode == null || machCode.trim().equals("")) {
			json.put("code", 1);
			json.put("msg", "机器编不能为空，重新输入");
			return json;
		}
		machCode = machCode.toUpperCase();
		// 根据设备号查询相应信息
		Device device = customerManage.selectByCode(machCode);
		if (device == null) {
			json.put("code", 1);
			json.put("msg", "相应机器编码的设备不存在，请检查并重新输入");
			return json;
		}
		if (device.getContractNo() == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器尚未绑定合同，不能报修");
			return json;
		}
		if (device.getAssetStatus() != null && device.getAssetStatus().equals("报废")) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器已报废，不能报修");
			return json;
		}
		List<OrderInfo> xixi = serviceManageServiceImpl.selectOrderByDynamic(null, machCode, null, null, null, null,
				null);
		for (OrderInfo orderInfo2 : xixi) {
			if (!orderInfo2.getWoStatus().equals("已完成") && !orderInfo2.getWoStatus().equals("已关单")
					&& !orderInfo2.getWoStatus().equals("已转单")) {
				json.put("code", 1);
				json.put("msg", "该设备已报修，请勿重复报修");
				return json;
			}
		}
		// 0.工单号
		String woNumber = newOrderNo(device.getServiceArea());
		// 10.设备名称
		String devName = device.getDevName();
		// 1.设备序列号
		String esNumber = device.getEsNumber();
		// 2.客户名称
		String custName = device.getCustArea();
		// 4.客户地址
		String custAddr = custInfo.selectCustByBaseInfo(device.getCustArea(), null, null, null, null).get(0)
				.getCustAddr();
		// 5.报修人
		String repairMan = request.getParameter("repairMan");
		// 6.报修人电话
		String repairService = request.getParameter("repairService");
		// 7.服务类型
		String faultType = request.getParameter("faultType");
		// 8.服务类别
		String accidentType = request.getParameter("accidentType");
		// 需求描述
		String remark = request.getParameter("remark");
		// 其他
		String other = request.getParameter("other");
		// 根据传递过来的客户名称，找到相应的客户id.
		int custId = custInfo.selectCusIdByName(custName);
		// 判断输入的参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("机器编码", machCode);
		args.put("联系人", repairMan);
		args.put("联系号码", repairService);
		args.put("服务类型", faultType);
		args.put("服务类别", accidentType);
		args.put("需求描述", remark);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (remark.equals("其它")) {
			if (other == null || other.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "请填写其他故障描述");
				return json;
			} else {
				remark = other;
			}
		}
		String picture01 = request.getParameter("picture01");
		String picture02 = request.getParameter("picture02");
		String picture03 = request.getParameter("picture03");
		String[] picture = { picture01, picture02, picture03 };
		String repairType = "网络";
		// 执行插入方法
		OrderInfo orderinfo = new OrderInfo();
		orderinfo.setWoNumber(woNumber);
		orderinfo.setFaultType(faultType);
		orderinfo.setEsNumber(esNumber);
		orderinfo.setCustName(custName);
		orderinfo.setCustId(custId);
		orderinfo.setDevName(devName);
		orderinfo.setMachCode(machCode);
		orderinfo.setRepairMan(repairMan);
		orderinfo.setRepairService(repairService);
		orderinfo.setCustAddr(custAddr);
		orderinfo.setAccidentType(accidentType);
		orderinfo.setRepairType(repairType);
		orderinfo.setRemark(remark);
		orderinfo.setWoStatus("待受理");
		orderinfo.setOrderAccount(repairService); // 实际派单账号
		boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
		if (result) {
			if (!SOMUtils.arrayIsNull(picture)) {
				// 保存图片
				String strPath = SOMUtils.pictureAddr + woNumber;
				File file = new File(strPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				int a = 1;
				for (String string : picture) {
					if (string != null && !string.equals("")) {
						String number = a + "";
						BASE64Decoder decoder = new BASE64Decoder();
						byte[] b = decoder.decodeBuffer(string.substring(23));
						OutputStream out = new FileOutputStream(strPath + "/" + woNumber + "-" + number + ".png");
						out.write(b);
						out.flush();
						out.close();
						a++;
					}
				}
			}
			// 如果工单增加成功，则在增加服务评价
			ServiceInfo serviceInfo = new ServiceInfo();
			// 获取默认责任工程师名称
			serviceInfo.setWoNumber(woNumber);
			serviceInfo.setStaffId(device.getResponsibleEngineerID());
			serviceManageServiceImpl.insertSelective(serviceInfo);
			json.put("code", 0);
			json.put("data", orderinfo);
			json.put("msg", "报修成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "报修失败");
			return json;
		}
	}

	/**
	 * 客户扫一扫报修
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/customerScannerOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerScannerOrder() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		// 机器编码
		String machCode = request.getParameter("machCode");
		if (machCode == null || machCode.trim().equals("")) {
			json.put("code", 1);
			json.put("msg", "机器编码不能为空，重新输入");
			return json;
		}
		machCode = machCode.toUpperCase();
		String openId = request.getParameter("openId");
		String picture01 = request.getParameter("picture01");
		String picture02 = request.getParameter("picture02");
		String picture03 = request.getParameter("picture03");
		String[] picture = { picture01, picture02, picture03 };
		// 根据设备号查询相应信息
		Device device = customerManage.selectByCode(machCode);
		// 根据openId查找相应用户
		User user = null;
		for (User user2 : userService.selectAllUser()) {
			if (user2.getOpenId() == null) {
				continue;
			}
			if (user2.getOpenId().equals(openId)) {
				user = user2;
				break;
			}
		}
		if (device == null) {
			json.put("code", 1);
			json.put("msg", "相应机器编码的设备不存在，请检查并重新输入");
			return json;
		}
		if (device.getContractNo() == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器尚未绑定合同，不能报修");
			return json;
		}
		if (device.getAssetStatus() != null && device.getAssetStatus().equals("报废")) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器已报废，不能报修");
			return json;
		}
		List<OrderInfo> xixi = serviceManageServiceImpl.selectOrderByDynamic(null, machCode, null, null, null, null,
				null);
		for (OrderInfo orderInfo2 : xixi) {
			if (!orderInfo2.getWoStatus().equals("已完成") && !orderInfo2.getWoStatus().equals("已关单")
					&& !orderInfo2.getWoStatus().equals("已转单")) {
				json.put("code", 1);
				json.put("msg", "该设备已报修，请勿重复报修");
				return json;
			}
		}
		// 0.工单号
		String woNumber = newOrderNo(device.getServiceArea());
		// 10.设备名称
		String devName = device.getDevName();
		// 1.设备序列号
		String esNumber = device.getEsNumber();
		// 2.客户名称
		String custName = device.getCustArea();
		// 4.客户地址
		String custAddr = custInfo.selectCustByBaseInfo(device.getCustArea(), null, null, null, null).get(0)
				.getCustAddr();
		// 5.报修人
		String repairMan = request.getParameter("repairMan");
		// 6.报修人电话
		String repairService = request.getParameter("repairService");
		// 7.服务类型
		String faultType = "事故类";
		// 8.服务类别
		String accidentType = request.getParameter("accidentType");
		// 9.服务需求
		String remark = request.getParameter("remark");
		// 其他
		String other = request.getParameter("other");
		// 根据传递过来的客户名称，找到相应的客户id.
		int custId = custInfo.selectCusIdByName(custName);
		// 判断输入的参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("机器编码", machCode);
		args.put("联系人", repairMan);
		args.put("联系号码", repairService);
		args.put("服务类别", accidentType);
		args.put("服务需求", remark);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (remark.equals("其它")) {
			if (other == null || other.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "请填写其他故障描述");
				return json;
			} else {
				remark = other;
			}
		}
		String repairType = "网络";
		// 执行插入方法
		OrderInfo orderinfo = new OrderInfo();
		orderinfo.setWoNumber(woNumber);
		orderinfo.setFaultType(faultType);
		orderinfo.setEsNumber(esNumber);
		orderinfo.setCustName(custName);
		orderinfo.setCustId(custId);
		orderinfo.setDevName(devName);
		orderinfo.setMachCode(machCode);
		orderinfo.setRepairMan(repairMan);
		orderinfo.setRepairService(repairService);
		orderinfo.setCustAddr(custAddr);
		orderinfo.setAccidentType(accidentType);
		orderinfo.setRepairType(repairType);
		orderinfo.setRemark(remark);
		orderinfo.setWoStatus("待受理");
		orderinfo.setOrderAccount(user.getUserId());
		boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
		if (result) {
			if (!SOMUtils.arrayIsNull(picture)) {
				// 保存图片
				String strPath = SOMUtils.pictureAddr + woNumber;
				File file = new File(strPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				int a = 1;
				for (String string : picture) {
					if (string != null && !string.equals("")) {
						String number = a + "";
						BASE64Decoder decoder = new BASE64Decoder();
						byte[] b = decoder.decodeBuffer(string.substring(23));
						OutputStream out = new FileOutputStream(strPath + "/" + woNumber + "-" + number + ".png");
						out.write(b);
						out.flush();
						out.close();
						a++;
					}
				}
			}
			// 如果工单增加成功，则在增加服务评价
			ServiceInfo serviceInfo = new ServiceInfo();
			// 获取默认责任工程师名称
			serviceInfo.setWoNumber(woNumber);
			serviceInfo.setStaffId(device.getResponsibleEngineerID());
			serviceManageServiceImpl.insertSelective(serviceInfo);
			json.put("code", 0);
			json.put("data", orderinfo);
			json.put("msg", "报修成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "报修失败");
			return json;
		}
	}

	/**
	 * 微信客户服务需求
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/customerNoMachCodeOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerNoMachCodeOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String openId = request.getParameter("openId"); // openid
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		// 根据openId查找相应用户
		User user = null;
		for (User user2 : userService.selectAllUser()) {
			if (user2.getOpenId() == null) {
				continue;
			}
			if (user2.getOpenId().equals(openId)) {
				user = user2;
				break;
			}
		}
		if (user == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该用户不存在");
			return json;
		}
		// 接受前端页面传递过来的信息
		// 客户名称
		String custName = user.getCustName();
		// 服务区域
		String serviceArea = request.getParameter("serviceArea");
		// 根据客户名称查找客户ID
		Integer custId = custInfo.selectCusIdByName(custName);
		if (custId == -1) {
			json.put("code", 1);
			json.put("msg", "您输入的客户名称不存在，请检查并重新输入");
			return json;
		}
		// 根据客户ID查询相应信息
		CustInfo cust = custInfo.selectCustById(custId);
		// 0.工单号
		String woNumber = newOrderNo(serviceArea);
		// 4.客户地址
		String custAddr = cust.getCustAddr();
		// 5.报修人
		String repairMan = request.getParameter("repairMan");
		// 6.报修人电话
		String repairService = request.getParameter("repairService");
		// 服务类别
		String accidentType = request.getParameter("accidentType");
		// 8.服务类型
		String faultType = request.getParameter("faultType");
		String repairType = "";
		// 9.需求描述
		String remark = request.getParameter("remark");
		// 其他
		String other = request.getParameter("other");
		// 报修时间
		Date repairTime = new Date();
		Map<String, Object> args = new HashMap<>();
		args.put("服务区域", serviceArea);
		args.put("报修人", repairMan);
		args.put("报修人联系电话", repairService);
		args.put("服务类别", accidentType);
		/* args.put("报修时间", repairTime); */
		args.put("客户名称", custName);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (remark.equals("其他")) {
			if (other == null || other.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "请填写其他故障描述");
				return json;
			} else {
				remark = other;
			}
		}
		// 转换故障类型
		switch (accidentType) {
		case "耗材补充":
			faultType = "需求类";
			break;
		case "安装实施":
			faultType = "需求类";
			break;
		case "客户培训":
			faultType = "需求类";
			break;
		case "设备变动":
			faultType = "需求类";
			break;
		case "保养":
			faultType = "事件类";
			break;
		case "送货":
			faultType = "事件类";
			break;
		case "抄表":
			faultType = "事件类";
			break;
		case "送文件":
			faultType = "事件类";
			break;
		case "其他":
			faultType = "事件类";
			break;
		default:
			break;
		}
		// 耗材型号
		String materialModel = request.getParameter("materialModel");
		// 耗材数量
		String materialNumber = request.getParameter("materialNumber");
		// 服务类别详细信息
		String serviceType = null;
		if (materialModel != null && !materialModel.trim().equals("") && materialNumber != null
				&& !materialNumber.trim().equals("")) {
			serviceType = materialModel + ":" + materialNumber;
		}
		// MACD类型
		String macdType = request.getParameter("macdType");
		// 判断报修类型
		if (userService.selectByPrimaryKey(user.getUserId()).getRoleId().equals("客户")) {
			repairType = "网络";
		} else {
			repairType = "电话";
		}
		// 执行插入方法
		OrderInfo orderinfo = new OrderInfo();
		orderinfo.setWoNumber(woNumber);
		orderinfo.setFaultType(faultType);
		orderinfo.setCustName(custName);
		orderinfo.setCustId(custId);
		orderinfo.setRepairMan(repairMan);
		orderinfo.setRepairService(repairService);
		orderinfo.setCustAddr(custAddr);
		orderinfo.setRepairTime(repairTime);
		orderinfo.setAccidentType(accidentType);
		orderinfo.setRepairType(repairType);
		orderinfo.setRemark(remark);
		orderinfo.setServiceType(serviceType);
		orderinfo.setMacdType(macdType);
		orderinfo.setWoStatus("待受理");
		orderinfo.setOrderAccount(user.getUserId());
		boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
		if (result) {
			// 如果工单增加成功，则在增加服务评价
			ServiceInfo serviceInfo = new ServiceInfo();
			serviceInfo.setWoNumber(woNumber);
			serviceManageServiceImpl.insertSelective(serviceInfo);
			json.put("code", 0);
			json.put("data", orderinfo);
			json.put("msg", "报修成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "服务需求新增失败");
			return json;
		}
	}

	/**
	 * -----------------------------------------------------------------------
	 */

	/**
	 * 增加工单接口，能报修的设备肯定是签了合同的设备。
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/doNewOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAddManager() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		// 机器编码
		String machCode = request.getParameter("machCode");
		if (machCode == null || machCode.trim().equals("")) {
			json.put("code", 1);
			json.put("msg", "机器编码不能为空，重新输入");
			return json;
		}
		machCode = machCode.toUpperCase();
		// 根据设备号查询相应信息
		Device device = customerManage.selectByCode(machCode);
		if (device == null) {
			json.put("code", 1);
			json.put("msg", "相应机器编码的设备不存在，请检查并重新输入");
			return json;
		}
		if (device.getContractNo() == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器尚未绑定合同，不能报修");
			return json;
		}
		if (device.getAssetStatus() != null && device.getAssetStatus().equals("报废")) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器已报废，不能报修");
			return json;
		}
		List<OrderInfo> xixi = serviceManageServiceImpl.selectOrderByDynamic(null, machCode, null, null, null, null,
				null);
		for (OrderInfo orderInfo2 : xixi) {
			if (!orderInfo2.getWoStatus().equals("已完成") && !orderInfo2.getWoStatus().equals("已关单")
					&& !orderInfo2.getWoStatus().equals("已转单")) {
				json.put("code", 1);
				json.put("msg", "该设备已报修，请勿重复报修");
				return json;
			}
		}
		// 0.工单号
		String woNumber = newOrderNo(device.getServiceArea());
		// 10.设备名称
		String devName = device.getDevName();
		// 1.设备序列号
		String esNumber = device.getEsNumber();
		// 2.客户名称
		String custName = device.getCustArea();
		// 4.客户地址
		if (custInfo.selectCustByBaseInfo(device.getCustArea(), null, null, null, null) == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器对应的客户信息不存在");
		}
		String custAddr = custInfo.selectCustByBaseInfo(device.getCustArea(), null, null, null, null).get(0)
				.getCustAddr();
		// 5.报修人
		String repairMan = request.getParameter("repairMan"); // 报修人
		// 6.报修人电话
		String repairService = request.getParameter("repairService"); // 报修电话
		// 7.报修类型
		String repairType = ""; // 服务类型
		// 服务类别
		String accidentType = request.getParameter("accidentType"); // 服务类别
		// 耗材型号
		String materialModel = request.getParameter("materialModel");
		// 耗材数量
		String materialNumber = request.getParameter("materialNumber");
		// 服务类别详细信息
		String serviceType = null;
		if (materialModel != null && !materialModel.trim().equals("") && materialNumber != null
				&& !materialNumber.trim().equals("")) {
			serviceType = materialModel + ":" + materialNumber;
		}

		// 8.故障类型
		String faultType = "事故类";
		// 9.获取备注
		String remark = request.getParameter("describe");
		// 报修时间
		/* Date repairTime = new Date(); */
		// 当前登录人账号
		String account = request.getParameter("username");
		// 根据传递过来的客户名称，找到相应的客户id.
		int custId = custInfo.selectCusIdByName(custName);
		// 判断输入的参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("机器编码", machCode);
		args.put("报修人", repairMan);
		args.put("报修人联系电话", repairService);
		args.put("服务类别", accidentType);
		/* args.put("报修时间", repairTime); */
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 转换故障类型
		if (faultType.equals("a")) {
			faultType = "事故类";
		} else if (faultType.equals("b")) {
			faultType = "事件类";
		} else if (faultType.equals("c")) {
			faultType = "需求类";
		}
		// 转换服务类别
		if (accidentType.equals("a")) {
			accidentType = "硬件故障";
		} else if (accidentType.equals("b")) {
			accidentType = "软件故障";
		} else if (accidentType.equals("c")) {
			accidentType = "耗材断供";
		}
		// 转换故障类型
		switch (faultType) {
		case "硬件故障":
			faultType = "事故类";
			break;
		case "软件故障":
			faultType = "事故类";
			break;
		case "耗材断供":
			faultType = "事故类";
			break;
		case "耗材补充":
			faultType = "需求类";
			break;
		case "安装实施":
			faultType = "需求类";
			break;
		case "客户培训":
			faultType = "需求类";
			break;
		case "保养":
			faultType = "事件类";
			break;
		default:
			break;
		}
		// 判断报修类型
		if (userService.selectByPrimaryKey(account).getRoleId().equals("客户")) {
			repairType = "网络";
		} else {
			repairType = "电话";
		}
		// 执行插入方法
		OrderInfo orderinfo = new OrderInfo();
		orderinfo.setWoNumber(woNumber);
		orderinfo.setFaultType(faultType);
		orderinfo.setEsNumber(esNumber);
		orderinfo.setCustName(custName);
		orderinfo.setCustId(custId);
		orderinfo.setDevName(devName);
		orderinfo.setMachCode(machCode);
		orderinfo.setRepairMan(repairMan);
		orderinfo.setRepairService(repairService);
		orderinfo.setCustAddr(custAddr);
		/* orderinfo.setRepairTime(repairTime); */
		orderinfo.setAccidentType(accidentType);
		orderinfo.setServiceType(serviceType);
		orderinfo.setRepairType(repairType);
		orderinfo.setRemark(remark);
		orderinfo.setWoStatus("待受理");
		orderinfo.setOrderAccount(repairService);
		boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
		if (result) {
			// 如果工单增加成功，则在增加服务评价
			ServiceInfo serviceInfo = new ServiceInfo();
			// 获取默认责任工程师名称
			serviceInfo.setWoNumber(woNumber);
			serviceInfo.setStaffId(device.getResponsibleEngineerID());
			serviceManageServiceImpl.insertSelective(serviceInfo);
			json.put("code", 0);
			json.put("data", orderinfo);
			json.put("msg", "添加成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "添加失败");
			return json;
		}
	}

	/**
	 * 增加工单接口，无机器编码的工单
	 * 
	 * @param modelAndView
	 * @throws ParseException
	 */
	@RequestMapping(value = "/noMachCodedoNewOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> noMachCodedoNewOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		// 客户名称
		String custName = request.getParameter("custName");
		if (custName == null || custName.equals("")) {
			json.put("code", 1);
			json.put("msg", "客户名称不能为空");
			return json;
		}
		// 服务区域
		String serviceArea = request.getParameter("serviceArea");
		// 根据客户名称查找客户ID
		Integer custId = custInfo.selectCusIdByName(custName);
		if (custId == -1) {
			json.put("code", 1);
			json.put("msg", "您输入的客户名称不存在，请检查并重新输入");
			return json;
		}
		// 根据客户ID查询相应信息
		CustInfo cust = custInfo.selectCustById(custId);
		// 0.工单号
		String woNumber = newOrderNo(serviceArea);
		// 4.客户地址
		String custAddr = cust.getCustAddr();
		// 5.报修人
		String repairMan = request.getParameter("repairMan");
		// 6.报修人电话
		String repairService = request.getParameter("repairService");
		// 7.报修类型
		String repairType = "";
		// 服务类别
		String accidentType = request.getParameter("accidentType");
		// 服务类别2
		String accidentType2 = request.getParameter("accidentType2");
		// 耗材型号
		String materialModel = request.getParameter("materialModel");
		// 耗材数量
		String materialNumber = request.getParameter("materialNumber");
		// MACD
		String macd = request.getParameter("macd");
		// 服务类别详细信息
		String serviceType = null;
		if (macd != null && !macd.trim().equals("")) {
			if (macd.equals("a")) {
				serviceType = "加机";
			} else if (macd.equals("a")) {
				serviceType = "变更";
			} else if (macd.equals("c")) {
				serviceType = "撤除";
			} else if (macd.equals("d")) {
				serviceType = "移动";
			}
		}
		if (materialModel != null && materialNumber != null && !materialModel.trim().equals("")
				&& !materialNumber.trim().equals("")) {
			serviceType = materialModel + "：" + materialNumber;
		}
		// 8.故障类型
		String faultType = request.getParameter("faultType");
		// 9.获取备注
		String remark = request.getParameter("describe");
		// 报修时间
		Date repairTime = new Date();
		// 当前登录人账号
		String account = request.getParameter("username");
		// 判断输入的参数是否有空
		if (accidentType == null && accidentType2 == null) {
			json.put("code", 1);
			json.put("msg", "服务类别不能为空");
			return json;
		}
		Map<String, Object> args = new HashMap<>();
		args.put("服务区域", serviceArea);
		args.put("报修人", repairMan);
		args.put("报修人联系电话", repairService);
		args.put("服务类型", faultType);
		/* args.put("报修时间", repairTime); */
		args.put("客户名称", custName);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 转换故障类型
		if (faultType.equals("a")) {
			faultType = "事故类";
		} else if (faultType.equals("b")) {
			faultType = "事件类";
		} else if (faultType.equals("c")) {
			faultType = "需求类";
		}
		// 转换服务类别
		if (faultType.equals("事件类")) {
			if (accidentType.equals("a")) {
				accidentType = "保养";
			} else if (accidentType.equals("b")) {
				accidentType = "送货";
			} else if (accidentType.equals("c")) {
				accidentType = "抄表";
			} else if (accidentType.equals("d")) {
				accidentType = "送文件";
			} else if (accidentType.equals("e")) {
				accidentType = "其他";
			} else {
				json.put("code", 1);
				json.put("msg", "服务类别不能为空");
				return json;
			}
		} else if (faultType.equals("需求类")) {
			if (accidentType2 == null || accidentType2.equals("")) {
				json.put("code", 1);
				json.put("msg", "服务类别不能为空");
				return json;
			} else if (accidentType2.equals("a")) {
				accidentType = "MACD";
			} else if (accidentType2.equals("b")) {
				accidentType = "耗材补充";
			} else if (accidentType2.equals("c")) {
				accidentType = "安装实施";
			} else if (accidentType2.equals("d")) {
				accidentType = "客户培训";
			}
		}
		// 转换故障类型
		switch (faultType) {
		case "硬件故障":
			faultType = "事故类";
			break;
		case "软件故障":
			faultType = "事故类";
			break;
		case "耗材断供":
			faultType = "事故类";
			break;
		case "耗材补充":
			faultType = "需求类";
			break;
		case "安装实施":
			faultType = "需求类";
			break;
		case "客户培训":
			faultType = "需求类";
			break;
		case "保养":
			faultType = "事件类";
			break;
		default:
			break;
		}
		if (accidentType.equals("MACD")) {
			if (macd == null || "".equals(macd.trim())) {
				json.put("code", 1);
				json.put("msg", "请选择设备变动类型");
				return json;
			}
		}
		if (accidentType.equals("耗材补充")) {
			if (materialModel == null || "".equals(materialModel.trim())) {
				json.put("code", 1);
				json.put("msg", "请输入耗材型号");
				return json;
			}
			if (materialNumber == null || "".equals(materialNumber.trim())) {
				json.put("code", 1);
				json.put("msg", "请输入耗材数量");
				return json;
			}
		}
		// 判断报修类型
		if (userService.selectByPrimaryKey(account).getRoleId().equals("CUST")) {
			repairType = "网络";
		} else if (faultType.equals("事件类")) {
			repairType = "主动服务";
		} else {
			repairType = "电话";
		}

		// 执行插入方法
		OrderInfo orderinfo = new OrderInfo();
		orderinfo.setWoNumber(woNumber);
		orderinfo.setFaultType(faultType);
		orderinfo.setCustName(custName);
		orderinfo.setCustId(custId);
		orderinfo.setRepairMan(repairMan);
		orderinfo.setRepairService(repairService);
		orderinfo.setCustAddr(custAddr);
		orderinfo.setRepairTime(repairTime);
		orderinfo.setAccidentType(accidentType);
		orderinfo.setServiceType(serviceType);
		orderinfo.setRepairType(repairType);
		orderinfo.setRemark(remark);
		orderinfo.setWoStatus("待受理");
		orderinfo.setOrderAccount(repairService);
		boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
		if (result) {
			// 如果工单增加成功，则在增加服务评价
			ServiceInfo serviceInfo = new ServiceInfo();
			serviceInfo.setWoNumber(woNumber);
			serviceManageServiceImpl.insertSelective(serviceInfo);
			json.put("code", 0);
			json.put("data", orderinfo);
			json.put("msg", "添加成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "添加失败");
			return json;
		}
	}

	/**
	 * 修改订单：增加零件信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/doAddParts", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> upDateOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String partsTypeNumber = request.getParameter("partsTypeNumber"); // 零件型号和相应数量
		String woNumber = request.getParameter("woNumber"); // 工单号
		String woStatus = "待订件，等待二次上门"; // 工单当前状态
		// 执行修改方法
		OrderInfo order = new OrderInfo();
		order.setWoNumber(woNumber);
		order.setPartsTypeNumber(partsTypeNumber);
		order.setWoStatus(woStatus);
		boolean a = serviceManageServiceImpl.updateOrder(order);
		if (a) {
			// 增加成功，将该条待购零件记录放到工单零件记录表
			OrderParts orderParts = new OrderParts();
			orderParts.setWoNumber(woNumber);
			orderParts.setWoStatus("工程师订件");
			if (serviceManageServiceImpl.selectSelective(orderParts) == null) {
				// 没有则将该条工单新增进去
				serviceManageServiceImpl.insertSelective(orderParts);
				json.put("code", 0);
				json.put("msg", "新增零件成功");
				return json;
			} else {
				// 有的话更新该工单零件记录状态
				serviceManageServiceImpl.updateSelective(orderParts);
				json.put("code", 0);
				json.put("msg", "新增零件成功");
				return json;
			}
		} else {
			json.put("code", 1);
			json.put("msg", "新增零件失败");
			return json;
		}
	}

	/**
	 * 接口：技术主管确认是否订购零件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sureAddParts", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> sureAddParts() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String woNumber = request.getParameter("woNumber"); // 工单号
		String confirm = request.getParameter("confirm"); // 技术主管审批状态
		String reason = request.getParameter("reason"); // 技术主管驳回原因
		// 执行修改方法
		OrderParts record = new OrderParts();
		record.setWoNumber(woNumber);
		record.setReason(reason);
		// 如果审批通过，则修改工单的零件状态，发送到运维助理处
		if (confirm.equals("是")) {
			String woStatus = "确认订件"; // 工单的零件当前审批状态
			record.setWoStatus(woStatus);
			int a = serviceManageServiceImpl.updateSelective(record);
			if (a > 0) {
				json.put("code", 0);
				json.put("msg", "技术主管确认订件成功");
				json.put("data", "");
				json.put("count", 0);
				return json;
			} else {
				json.put("code", 1);
				json.put("msg", "技术主管确认订件失败");
				json.put("data", "");
				json.put("count", 0);
				return json;
			}
		} else {
			// 驳回，填写驳回原因
			String woStatus = "驳回"; // 工单的零件当前审批状态
			record.setWoStatus(woStatus);
			record.setReason(reason);
			serviceManageServiceImpl.updateSelective(record);
			json.put("code", 0);
			json.put("msg", "驳回成功");
			json.put("data", "");
			json.put("count", 0);
			return json;
		}
	}

	/**
	 * 接口：运维助理确认零件是否回来
	 * 
	 * @return
	 */
	@RequestMapping(value = "/repoAddParts", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> repoAddParts() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String woNumber = request.getParameter("woNumber"); // 工单号
		String woStatus = "零件已订购"; // 工单的零件当前审批状态
		// 执行修改方法
		OrderParts record = new OrderParts();
		record.setWoNumber(woNumber);
		record.setWoStatus(woStatus);
		int a = serviceManageServiceImpl.updateSelective(record);
		if (a > 0) {
			json.put("code", 0);
			json.put("msg", "确认成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "确认失败");
			return json;
		}
	}

	/**
	 * 派单接口：给工程师指派工单。
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/sendOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> sendOrder() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String woNumber = request.getParameter("woNumber"); // 指派工单的工单号
		String engineerID = request.getParameter("engineerID"); // 指定工程师姓名
		String account = request.getParameter("username"); // 指派人账号
		StaffInfo enginner = null; // 受指派工程师对象
		for (StaffInfo staff : staffInfoServiceImplnew.selectAllStaff()) {
			if (staff.getName().equals(engineerID)) {
				enginner = staff;
			}
		}
		if (account == null || "".equals(account.trim())) {
			json.put("code", 1);
			json.put("msg", "请先登陆");
			return json;
		}
		if (enginner == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工程师姓名不存在，请检查并重新输入");
			return json;
		}
		if (!(enginner.getPost().equals("工程师") || enginner.getPost().equals("技术主管")
				|| enginner.getPost().equals("运维经理"))) {
			json.put("code", 1);
			json.put("msg", "对不起，该员工不是工程师，请重新输入");
			return json;
		}
		UserRole role = userService.selectByPrimaryKey(account);
		String faultType = serviceManageServiceImpl.selectOrderByOrderNum(woNumber).getFaultType();
		if (faultType.equals("事故类")) {
			if (role.getRoleId().equals("运维助理")) {
				json.put("code", 1);
				json.put("msg", "对不起，运维助理不能受理事故类");
				return json;
			}
		}
		Date sendTime = new Date(); // 点击派单的时间，取系统当前时间
		String woStatus = "已受理"; // 设置工单状态为已受理
		// 创建工单对象接受信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoStatus(woStatus);
		orderInfo.setDistributeMan(account);
		orderInfo.setWoProgress(woStatus);
		orderInfo.setSendTime(sendTime);
		orderInfo.setWoNumber(woNumber);
		// 创建服务对象接受信息
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setWoNumber(woNumber);
		serviceInfo.setStaffId(enginner.getStaffId());
		// 更新工单信息
		boolean a = serviceManageServiceImpl.updateOrder(orderInfo);
		// 更新服务信息
		boolean b = serviceInfoService.upDateServiceInfo(serviceInfo);
		if (a && b) {
			SMS.senMessage(SMS.ENGINNER_ACCEPTANCE, enginner.getPhone(), enginner.getName(), woNumber);
			json.put("code", 0);
			json.put("msg", "派单成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "派单失败，请联系管理员");
		return json;
	}

	/**
	 * 派单接口：重新指派工单
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/sendOrderOnce", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> sendOrderOnce() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String woNumber = request.getParameter("woNumber"); // 指派工单的工单号
		if (woNumber == null || woNumber.equals("")) {
			json.put("code", 1);
			json.put("msg", "对不起，重新指派时工单号不能为空");
			return json;
		}
		// 找出之前的工单
		OrderInfo order = serviceManageServiceImpl.selectOrderByOrderNum(woNumber);
		if (order == null) {
			json.put("code", 1);
			json.put("msg", "对不起，您要重新指派的工单号不存在");
			return json;
		}
		if (order.getWoNumber().length() > 14) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经被重新指派过一次，不能再次重新指派");
			return json;
		}
		String engineerID = request.getParameter("engineerID");
		String account = request.getParameter("username"); // 指派人账号
		// 当前工单完结
		serviceManageServiceImpl.updateWoStatus(serviceManageServiceImpl.selectOrderByOrderNum(woNumber).getWoNumber(),
				"已关单");
		OrderInfo order1 = new OrderInfo();
		order1.setWoNumber(woNumber);
		order1.setWoProgress("已关单");
		serviceManageServiceImpl.updateOrder(order1);
		// 生成新的工单
		StaffInfo enginner = staffInfoServiceImplnew.selectStaffByNum(engineerID); // 受指派工程师对象
		String staffId = null; // 指派人员工id
		for (StaffInfo staff : staffInfoServiceImplnew.selectAllStaff()) {
			if (staff.getPhone().equals(account)) {
				staffId = staff.getStaffId();
				break;
			}
		}
		// 如果没有机器编码，则新生成的为没有机器编码的工单
		if (order.getMachCode() == null || order.getMachCode().equals("")) {
			// 执行插入方法
			OrderInfo orderinfo = new OrderInfo();
			orderinfo.setWoNumber(order.getWoNumber() + "-1");
			orderinfo.setPriority(order.getPriority());
			orderinfo.setFaultType(order.getFaultType());
			orderinfo.setCustName(order.getCustName());
			orderinfo.setCustId(order.getCustId());
			orderinfo.setRepairMan(order.getRepairMan());
			orderinfo.setRepairService(order.getRepairService());
			orderinfo.setCustAddr(order.getCustAddr());
			orderinfo.setRepairTime(order.getRepairTime());
			orderinfo.setAccidentType(order.getAccidentType());
			orderinfo.setServiceType(order.getServiceType());
			orderinfo.setRepairType(order.getRepairType());
			orderinfo.setRemark(order.getRemark());
			orderinfo.setWoStatus("已受理");
			orderinfo.setWoProgress("已受理");
			orderinfo.setDistributeMan(staffId);
			orderinfo.setSendTime(new Date());
			boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
			if (result) {
				// 如果工单增加成功，则在增加服务评价
				ServiceInfo serviceInfo = new ServiceInfo();
				serviceInfo.setWoNumber(orderinfo.getWoNumber());
				serviceManageServiceImpl.insertSelective(serviceInfo);
				// 发送短信到相应工程师的手机上
				SMS.senMessage(SMS.ENGINNER_ACCEPTANCE, enginner.getPhone(), enginner.getName(),
						orderinfo.getWoNumber());
				json.put("code", 0);
				json.put("msg", "重新指派成功");
				return json;
			}
		} else {
			// 执行插入方法
			OrderInfo orderinfo = new OrderInfo();
			orderinfo.setWoNumber(order.getWoNumber() + "-1");
			orderinfo.setFaultType(order.getFaultType());
			orderinfo.setPriority(order.getPriority());
			orderinfo.setEsNumber(order.getEsNumber());
			orderinfo.setCustName(order.getCustName());
			orderinfo.setCustId(order.getCustId());
			orderinfo.setDevName(order.getDevName());
			orderinfo.setMachCode(order.getMachCode());
			orderinfo.setRepairMan(order.getRepairMan());
			orderinfo.setRepairService(order.getRepairService());
			orderinfo.setCustAddr(order.getCustAddr());
			orderinfo.setRepairTime(order.getRepairTime());
			orderinfo.setAccidentType(order.getAccidentType());
			orderinfo.setServiceType(order.getServiceType());
			orderinfo.setRepairType(order.getRepairType());
			orderinfo.setRemark(order.getRemark());
			orderinfo.setWoStatus("已受理");
			orderinfo.setWoProgress("已受理");
			orderinfo.setDistributeMan(staffId);
			orderinfo.setSendTime(new Date());
			boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
			if (result) {
				// 如果工单增加成功，则在增加服务评价
				ServiceInfo serviceInfo = new ServiceInfo();
				serviceInfo.setWoNumber(orderinfo.getWoNumber());
				serviceInfo.setStaffId(enginner.getStaffId());
				serviceManageServiceImpl.insertSelective(serviceInfo);
				// 发送短信到相应工程师的手机上
				SMS.senMessage(SMS.ENGINNER_ACCEPTANCE, enginner.getPhone(), enginner.getName(),
						orderinfo.getWoNumber());
				json.put("code", 0);
				json.put("msg", "重新指派成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "修改失败");
		return json;
	}

	/**
	 * 关单接口：完结工单。
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/closeOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> closeOrder() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String woNumber = request.getParameter("woNumber"); // 要完结的工单号
		String username = request.getParameter("username"); // 当前完结人的账号
		Date closeTime = new Date(); // 完结工单的时间，取系统当前时间
		String woStatus = "已关单"; // 设置工单状态为已关单
		// 创建工单对象接受信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoStatus(woStatus);
		orderInfo.setCloseTime(closeTime);
		orderInfo.setWoNumber(woNumber);
		orderInfo.setWoProgress(woStatus);
		Map<String, Object> args = new HashMap<>();
		args.put("工单号码", woNumber);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 0);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (userService.selectByPrimaryKey(username).getRoleId().equals("运维助理")) {
			if (serviceManageServiceImpl.selectOrderByOrderNum(woNumber).getFaultType().equals("事故类")) {
				json.put("code", 1);
				json.put("msg", "对不起，运维助理不能关闭事故类工单");
				return json;
			}
			if (serviceManageServiceImpl.selectOrderByOrderNum(woNumber).getWoStatus().equals("已转单")) {
				json.put("code", 1);
				json.put("msg", "对不起，转单的工单无需关闭");
				return json;
			}
		}
		// 更新工单信息
		boolean a = serviceManageServiceImpl.updateOrder(orderInfo);
		if (a) {
			json.put("code", 0);
			json.put("msg", "关单成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "关单失败");
		return json;
	}

	/**
	 * 方法：根据条件查询工单
	 */
	@RequestMapping(value = "/searchOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> searchOrder(ModelAndView modelAndView, HttpServletRequest request) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String woNumber = request.getParameter("woNumber");
		String custName = request.getParameter("custName");
		String machCode = request.getParameter("machCode");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String type = request.getParameter("type");
		String woStatus = request.getParameter("woStatus");
		// 如果查询条件没有时间，则默认查询两月内的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			endDate = ExcelUtils.fmtOne.format(new Date());
			calendar.add(Calendar.MONTH, -2);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
		}
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		orderInfos = serviceManageServiceImpl.selectOrderByDynamic(custName, machCode, woNumber, startDate, endDate,
				type, woStatus);
		json.put("code", 0);
		json.put("msg", "工单管理数据");
		json.put("count", orderInfos.size());
		json.put("data", orderInfos);
		return json;
	}

	/**
	 * 方法：导出excel工单
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportOrder(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		List<ServiceInfo> serviceInfos = (List<ServiceInfo>) export
				.get(request.getParameter("username") + "orderManage");
		String tableName = "工单表";
		// 设置表头
		String[] Titles = { "客户名称", "工单号码", "设备名称", "机器编码", "工单来源", "设备序列号", "设备型号", "设备位置", "报修人/联系人", "联系电话", "报修时间",
				"派单时间", "工程师接单时间", "工程师到达时间", "完成时间", "指定工程师", "服务类型", "服务类别", "故障描述", "处理措施", "处理状态", "是否有零件订购" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			service.setDevice(customerManage.selectByCode(service.getOrderInfo().getMachCode()));
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(service.getOrderInfo().getCustName());
			row.createCell(2).setCellValue(service.getOrderInfo().getWoNumber());
			row.createCell(3).setCellValue(service.getDevice() == null ? "" : service.getDevice().getDevName());
			row.createCell(4).setCellValue(service.getDevice() == null ? "" : service.getDevice().getMachCode());
			row.createCell(5).setCellValue(service.getOrderInfo().getRepairType());
			row.createCell(6).setCellValue(service.getDevice() == null ? "" : service.getDevice().getEsNumber());
			row.createCell(7).setCellValue(service.getDevice() == null ? "" : service.getDevice().getUnitType());
			row.createCell(8).setCellValue(service.getDevice() == null ? "" : service.getDevice().getLocation());
			row.createCell(9).setCellValue(service.getOrderInfo().getRepairMan());
			row.createCell(10).setCellValue(service.getOrderInfo().getRepairService());
			row.createCell(11).setCellValue(service.getOrderInfo().getRepairTime() == null ? null
					: ExcelUtils.fmtOne.format(service.getOrderInfo().getRepairTime()));
			row.createCell(12).setCellValue(service.getOrderInfo().getSendTime() == null ? null
					: ExcelUtils.fmtOne.format(service.getOrderInfo().getSendTime()));
			row.createCell(13).setCellValue(service.getOrderInfo().getGetOrderTime() == null ? null
					: ExcelUtils.fmtOne.format(service.getOrderInfo().getGetOrderTime()));
			row.createCell(14)
					.setCellValue(service.getArrTime() == null ? null : ExcelUtils.fmtOne.format(service.getArrTime()));
			row.createCell(15).setCellValue(
					service.getProbSolve() == null ? null : ExcelUtils.fmtOne.format(service.getProbSolve()));
			row.createCell(16).setCellValue(service.getStaffInfo() == null ? "" : service.getStaffInfo().getName());
			row.createCell(17).setCellValue(service.getOrderInfo().getFaultType());
			row.createCell(18).setCellValue(service.getOrderInfo().getAccidentType());
			row.createCell(19).setCellValue(service.getOrderInfo().getRemark());
			row.createCell(20).setCellValue(service.getOrderInfo().getTreatmentMeasure());
			row.createCell(21).setCellValue(service.getOrderInfo().getWoStatus());
			row.createCell(22).setCellValue(service.getOrderInfo().getPartsTypeNumber() == null ? "否" : "是");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	@RequestMapping(value = "/copy", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> copy(HttpServletRequest request, HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		// 如果工单号为空，则提示工单号不能为空
		if (woNumber == null || woNumber.equals("")) {
			json.put("code", 1);
			json.put("msg", "工单号不能为空");
			return json;
		}
		OrderInfo order = serviceManageServiceImpl.selectOrderByOrderNum(woNumber);

		if (order == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在，无法导出工单");
			return json;
		}
		if (!order.getFaultType().equals("事故类")) {
			json.put("code", 1);
			json.put("msg", "对不起，事故类无法导出工单");
			return json;
		}
		Device device = customerManage.selectByCode(order.getMachCode());
		ServiceInfo serviceInfo = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		HSSFWorkbook wb = ExcelUtils.copyExcel(SOMUtils.qrAddr + "orderTemplate1.xls");
		HSSFSheet sheet = wb.getSheet("Sheet1");
		HSSFRow row = sheet.getRow(2);
		// 工单号码
		row.getCell(4).setCellValue("工单号码：" + order.getWoNumber());
		// 服务类型
		row = sheet.getRow(8);
		row.getCell(1).setCellValue(order.getFaultType());
		// 故障及需求类型
		row.getCell(3).setCellValue(order.getAccidentType());
		// 派单时间
		row.getCell(5).setCellValue(ExcelUtils.fmtOne.format(order.getSendTime()));
		// 客服
		row.getCell(7).setCellValue(userService.selectByUserId(order.getDistributeMan()).getUserName());
		// 合同单号
		row = sheet.getRow(10);
		row.getCell(1).setCellValue(device.getContractNo());
		// 机器编码
		row.getCell(4).setCellValue(order.getMachCode());
		// 客户名称
		row = sheet.getRow(11);
		row.getCell(1).setCellValue(order.getCustName());
		// 客户地址
		row.getCell(4).setCellValue(order.getCustAddr());
		// 客户联系人
		row = sheet.getRow(12);
		row.getCell(1).setCellValue(order.getRepairMan());
		// 设备厂家
		row.getCell(4).setCellValue(device.getDeviceBrand());
		// 客户联系人电话
		row = sheet.getRow(13);
		row.getCell(1).setCellValue(order.getRepairService());
		// 设备型号
		row.getCell(4).setCellValue(device.getUnitType());
		// 设备序列号
		row = sheet.getRow(14);
		row.getCell(1).setCellValue(device.getEsNumber());
		// 负责工程师
		row.getCell(4)
				.setCellValue(staffInfoServiceImplnew
						.selectStaffByNum(
								serviceInfoService.selectServiceInfByDynamic2("", woNumber, "").get(0).getStaffId())
						.getName());
		// 到达时间、完成时间
		row = sheet.getRow(16);
		row.getCell(4).setCellValue(
				serviceInfo.getArrTime() == null ? null : ExcelUtils.fmtOne.format(serviceInfo.getArrTime()));
		row.getCell(7).setCellValue(
				serviceInfo.getProbSolve() == null ? null : ExcelUtils.fmtOne.format(serviceInfo.getProbSolve()));
		// 服务区域
		row = sheet.getRow(22);
		row.getCell(4).setCellValue(device.getServiceArea());
		ExcelUtils.download(res, wb, " " + woNumber);
		json.put("code", 0);
		json.put("msg", "导出工单成功");
		return json;
	}

	/**
	 * 前台代码转换为对应的类型
	 * 
	 * @param role
	 * @return
	 */
	public String typeToName(String type) {
		String post = null;
		switch (type) {
		case "1":
			post = "微信报修";
			break;
		case "2":
			post = "PC端报修";
			break;
		case "3":
			post = "电话报修";
			break;
		case "4":
			post = "事故类";
			break;
		case "5":
			post = "服务类";
			break;
		case "6":
			post = "需求类";
			break;
		}
		return post;
	}

	/**
	 * 设备接口：查看工单图片，返回工单图片所在路径
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/woNumberPicture", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> woNumberPicture(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 查看登陆人是否有权限
		// 从前端接受机器编码
		String woNumber = request.getParameter("woNumber");
		if (woNumber == null || woNumber.equals("")) {
			json.put("code", 1);
			json.put("msg", "请输入要查看图片的工单号");
			return json;
		}
		if (serviceManageServiceImpl.selectOrderByOrderNum(woNumber) == null) {
			json.put("code", 1);
			json.put("msg", "该工单不存在");
			return json;
		}
		List<User> users = new ArrayList<>();
		File file = null;
		String a = null;
		for (int i = 1; i <= 3; i++) {
			a = SOMUtils.pictureAddr + woNumber + "/" + woNumber + "-" + i + ".png";
			file = new File(a);
			if (i == 1 && !file.exists()) {
				json.put("msg", "对不起，该工单暂无图片");
				return json;
			}
			if (file.exists()) {
				User user = new User();
				user.setCustName("http://" + SOMUtils.pictureAddr1 + woNumber + "/" + woNumber + "-" + i + ".png");
				users.add(user);
			}
		}
		json.put("data", users);
		return json;
	}

}
