package com.xunwei.som.base.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Maintenance;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.MaintenanceContract;
import com.xunwei.som.pojo.front.MaintenanceEnginner;
import com.xunwei.som.pojo.front.OrderManage;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.service.CustomerManageService;
import com.xunwei.som.service.ServiceInfoService;
import com.xunwei.som.service.StaffInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.MaintenanceserviceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.SOMUtils;

/**
 * 技术主管
 * 
 * @author Administrator
 *
 */

@Controller
public class WeChatTechnical extends BaseController {

	private MaintenanceserviceImpl maintenanceserviceImpl = new MaintenanceserviceImpl();

	private CustomerManageService customerManageService = new CustomerManageServiceImpl();

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private StaffInfoService staffInfoService = new StaffInfoServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private ServiceInfoService serviceInfoService = new ServiceInfoServiceImpl();

	private UserService userService = new UserServiceImpl();

	/**
	 * 技术主管接口：查看技术主管负责保养的合同
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/TechnicalMaintenanceContract", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalMaintenanceContract() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String openId = request.getParameter("openId"); // openId
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		// 根据openId查询工程师Id
		User user2 = null;
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openId)) {
				user2 = user;
			}
		}
		if (user2 == null) {
			json.put("code", 1);
			json.put("msg", "对应openId的用户不存在");
			return json;
		}
		List<Maintenance> maintenances = new ArrayList<>();
		if (user2.getCustName().equals("广州乐派数码科技有限公司") || user2.getCustName().equals("行业客户部")
				|| user2.getCustName().equals("系统推进部")) {
			maintenances = maintenanceserviceImpl.selectByCycle(null, null, "广州乐派数码科技有限公司", null);
			maintenances.addAll(maintenanceserviceImpl.selectByCycle(null, null, "行业客户部", null));
			maintenances.addAll(maintenanceserviceImpl.selectByCycle(null, null, "系统推进部", null));
		} else {
			maintenances = maintenanceserviceImpl.selectByCycle(null, null, user2.getCustName(), null);
		}
		List<MaintenanceContract> maintenanceContracts = new ArrayList<>();
		List<String> contractCode = new ArrayList<>();
		for (Maintenance maintenance : maintenances) {
			if (contractCode.contains(maintenance.getContractCode())) {
				continue;
			}
			MaintenanceContract maintenanceContract = new MaintenanceContract();
			maintenanceContract.setContractNo(maintenance.getContractCode());
			maintenanceContract.setCustName(maintenance.getCustName());
			contractCode.add(maintenance.getContractCode());
			maintenanceContracts.add(maintenanceContract);
		}
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("count", maintenanceContracts.size());
		json.put("data", maintenanceContracts);
		return json;
	}

	/**
	 * 技术主管接口：查看技术主管近期的需要保养的设备
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/TechnicalMaintenancePlan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalMaintenancePlan(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收参数
		String Cycle = request.getParameter("Cycle");// 查询周期
		String contractNo = request.getParameter("contractNo"); // 合同号
		String isOver = request.getParameter("isOver"); // 是否已经完成
		Map<String, Object> json = new HashMap<>();
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("合同号", contractNo);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		List<Maintenance> maintenances = maintenanceserviceImpl.selectByCycle(Cycle, null, null, contractNo);
		List<Maintenance> needMaintenances = new ArrayList<>();
		// 遍历保养执行
		for (Maintenance maintenance : maintenances) {
			maintenance.setMaintenStatus(maintenance.getMaintenanceState() == 0 ? "未完成" : "已完成");
		}
		List<MaintenanceEnginner> maintenanceEnginners = new ArrayList<>();
		if (Cycle != null && !Cycle.trim().equals("")) {
			if (isOver.equals("已完成")) {
				for (Maintenance maintenance : needMaintenances) {
					if (maintenance.getMaintenanceState() == 1 && maintenance.getMainFrequency().equals(Cycle)) {
						MaintenanceEnginner maintenanceEnginner = new MaintenanceEnginner();
						maintenanceEnginner.setMachCode(maintenance.getMachCode());
						maintenanceEnginner.setDevName(maintenance.getDevice().getDevName());
						maintenanceEnginner.setBwReader(maintenance.getDevice().getBwReader());
						maintenanceEnginner.setColorReader(maintenance.getDevice().getColorReader());
						maintenanceEnginner.setMainFrequency(maintenance.getMainFrequency());
						maintenanceEnginner.setLocaltion(maintenance.getDevice().getLocation());
						maintenanceEnginner.setStatus(maintenance.getMaintenanceState() == 1 ? "已完成" : "未完成");
						maintenanceEnginners.add(maintenanceEnginner);
					}
				}
			} else if (isOver.equals("未完成")) {
				for (Maintenance maintenance : needMaintenances) {
					if (maintenance.getMaintenanceState() == 0 && maintenance.getMainFrequency().equals(Cycle)) {
						MaintenanceEnginner maintenanceEnginner = new MaintenanceEnginner();
						maintenanceEnginner.setMachCode(maintenance.getMachCode());
						maintenanceEnginner.setDevName(maintenance.getDevice().getDevName());
						maintenanceEnginner.setBwReader(maintenance.getDevice().getBwReader());
						maintenanceEnginner.setColorReader(maintenance.getDevice().getColorReader());
						maintenanceEnginner.setMainFrequency(maintenance.getMainFrequency());
						maintenanceEnginner.setLocaltion(maintenance.getDevice().getLocation());
						maintenanceEnginner.setStatus(maintenance.getMaintenanceState() == 1 ? "已完成" : "未完成");
						maintenanceEnginners.add(maintenanceEnginner);
					}

				}
			}
		} else {
			if (isOver.equals("已完成")) {
				for (Maintenance maintenance : needMaintenances) {
					if (maintenance.getMaintenanceState() == 1) {
						MaintenanceEnginner maintenanceEnginner = new MaintenanceEnginner();
						maintenanceEnginner.setMachCode(maintenance.getMachCode());
						maintenanceEnginner.setDevName(maintenance.getDevice().getDevName());
						maintenanceEnginner.setBwReader(maintenance.getDevice().getBwReader());
						maintenanceEnginner.setColorReader(maintenance.getDevice().getColorReader());
						maintenanceEnginner.setMainFrequency(maintenance.getMainFrequency());
						maintenanceEnginner.setLocaltion(maintenance.getDevice().getLocation());
						maintenanceEnginner.setStatus(maintenance.getMaintenanceState() == 1 ? "已完成" : "未完成");
						maintenanceEnginners.add(maintenanceEnginner);
					}
				}
			} else if (isOver.equals("未完成")) {
				for (Maintenance maintenance : needMaintenances) {
					if (maintenance.getMaintenanceState() == 0) {
						MaintenanceEnginner maintenanceEnginner = new MaintenanceEnginner();
						maintenanceEnginner.setMachCode(maintenance.getMachCode());
						maintenanceEnginner.setDevName(maintenance.getDevice().getDevName());
						maintenanceEnginner.setBwReader(maintenance.getDevice().getBwReader());
						maintenanceEnginner.setColorReader(maintenance.getDevice().getColorReader());
						maintenanceEnginner.setMainFrequency(maintenance.getMainFrequency());
						maintenanceEnginner.setLocaltion(maintenance.getDevice().getLocation());
						maintenanceEnginner.setStatus(maintenance.getMaintenanceState() == 1 ? "已完成" : "未完成");
						maintenanceEnginners.add(maintenanceEnginner);
					}

				}
			}
		}
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("count", maintenanceEnginners.size());
		json.put("data", maintenanceEnginners);
		return json;
	}

	/**
	 * 匹配工程师工作状态页面，默认查询所有。
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/TechnicalWorkingStatus", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalWorkingStatus(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String openId = request.getParameter("openId"); // openId
		String staffName = request.getParameter("staffName"); // 员工姓名
		String state = request.getParameter("state"); // 状态
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		// 根据openId查询工程师Id
		User user2 = null;
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openId)) {
				user2 = user;
			}
		}
		if (user2 == null) {
			json.put("code", 1);
			json.put("msg", "对应openId的用户不存在");
			return json;
		}
		List<StaffInfo> engineer = staffInfoService.getStaffByDynamic(staffName, user2.getCustName(), "工程师", null, null,
				null, state, null);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, engineer, page, limit);
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
		json.put("msg", "在岗状态数据");
		json.put("count", engineer == null ? 0 : engineer.size());
		json.put("data", page == null || engineer == null ? engineer : engineer.subList(page, limit));
		return json;
	}

	/**
	 * 技术主管接口:查询工单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/TechnicalNotOverOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalNotOverOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		// 从前端接收参数
		String openId = request.getParameter("openId"); // openid
		String woStatus = request.getParameter("woStatus"); // 工单状态
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
			json.put("msg", "对不起，该openId对应的用户不存在");
			return json;
		}
		StaffInfo staff = null;
		for (StaffInfo staffinfo : staffInfoService.selectAllStaff()) {
			if (staffinfo.getPhone().equals(user.getUserId())) {
				staff = staffinfo;
				break;
			}
		}
		ServiceInfo service = new ServiceInfo();
		service.setStaffId(staff.getStaffId());
		service.setState(woStatus);
		List<ServiceInfo> serviceInfo = new ArrayList<>();
		if (staff.getCompName().equals("系统推进部") || staff.getCompName().equals("广州乐派数码科技有限公司")
				|| staff.getCompName().equals("行业客户部")) {
			service.setCustSat("XT");
			serviceInfo = serviceInfoService.selectOrderByWeChatTechnical(service);
			service.setCustSat("HY");
			serviceInfo.addAll(serviceInfoService.selectOrderByWeChatTechnical(service));
			service.setCustSat("GZ");
			serviceInfo.addAll(serviceInfoService.selectOrderByWeChatTechnical(service));
		} else {
			service.setCustSat(SOMUtils.orderNumToComp(user.getCustName()));
			serviceInfo = serviceInfoService.selectOrderByWeChatTechnical(service);
		}
		List<OrderManage> orderManages = new ArrayList<>();
		Integer a1 = null;
		for (ServiceInfo serviceInfo2 : serviceInfo) {
			serviceInfo2.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(serviceInfo2.getWoNumber()));
			if (serviceInfo2.getOrderInfo().getMachCode() != null) {
				serviceInfo2.setDevice(customerManage.selectDeviceById(serviceInfo2.getOrderInfo().getMachCode()));
				a1 = 1;
			} else {
				a1 = 2;
			}
			String woState = serviceInfo2.getOrderInfo().getWoProgress();
			if (serviceInfo2.getCustScore() != null) {
				switch (serviceInfo2.getCustScore()) {
				case 3:
					woState = "commonly";
					break;
				case 2:
					woState = "dissatisfied";
					break;
				case 4:
					woState = "satisfied";
					break;
				case 5:
					woState = "verySatisfied";
					break;
				case 1:
					woState = "SuperDiscontent";
					break;
				default:
					break;
				}
			}
			serviceInfo2.setStaffInfo(staffInfoService.selectStaffByNum(serviceInfo2.getStaffId()));
			if (a1 == 1) {
				OrderManage orderManage = new OrderManage();
				orderManage.setCustName(serviceInfo2.getOrderInfo().getCustName());
				orderManage.setWoNumber(serviceInfo2.getWoNumber());
				orderManage.setMachCode(serviceInfo2.getOrderInfo().getMachCode());
				orderManage.setDevName(serviceInfo2.getDevice().getDevName());
				orderManage.setUnitType(serviceInfo2.getDevice().getUnitType());
				orderManage.setDepartment(serviceInfo2.getDevice().getDepartment());
				orderManage.setRepairMan(serviceInfo2.getOrderInfo().getRepairMan());
				orderManage.setRepairPhone(serviceInfo2.getOrderInfo().getRepairService());
				orderManage.setLocation(serviceInfo2.getDevice().getLocation());
				orderManage.setFaultType(serviceInfo2.getOrderInfo().getFaultType());
				orderManage.setAccidentType(serviceInfo2.getOrderInfo().getAccidentType());
				orderManage.setFaultClass(serviceInfo2.getOrderInfo().getFaultClass());
				orderManage.setFaultNo(serviceInfo2.getOrderInfo().getFalutNo());
				orderManage.setDescribe(serviceInfo2.getOrderInfo().getRemark());
				orderManage.setMainTenanceFeedback(serviceInfo2.getOrderInfo().getMaintenanceFeedback());
				orderManage.setRepairTime(serviceInfo2.getOrderInfo().getRepairTime());
				orderManage.setWoState(woState);
				orderManage.setTurnOrderReson(serviceInfo2.getOrderInfo().getTurnOrderReson());
				orderManage.setEnginnerName(serviceInfo2.getStaffInfo().getName());
				orderManage.setEnginnerPhone(serviceInfo2.getStaffInfo().getPhone());
				orderManage.setPartMessage(serviceInfo2.getOrderInfo().getPartsTypeNumber() == null ? null
						: serviceInfo2.getOrderInfo().getPartsTypeNumber().split(";"));
				orderManage.setRejected(serviceInfo2.getCustPrai() == null ? null : serviceInfo2.getCustPrai());
				orderManage.setBwReader(serviceInfo2.getDevice().getBwReader());
				orderManage.setCoReader(serviceInfo2.getDevice().getColorReader());
				orderManage.setServiceType(serviceInfo2.getOrderInfo().getServiceType());
				orderManage.setTreatmentMeasure(serviceInfo2.getOrderInfo().getTreatmentMeasure());
				orderManage.setOrderReason(serviceInfo2.getOrderInfo().getTreatmentState());
				orderManages.add(orderManage);
			} else {
				OrderManage orderManage = new OrderManage();
				orderManage.setCustName(serviceInfo2.getOrderInfo().getCustName());
				orderManage.setWoNumber(serviceInfo2.getWoNumber());
				orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo2.getWoNumber().substring(0, 2)));
				orderManage.setRepairMan(serviceInfo2.getOrderInfo().getRepairMan());
				orderManage.setRepairPhone(serviceInfo2.getOrderInfo().getRepairService());
				orderManage.setFaultType(serviceInfo2.getOrderInfo().getFaultType());
				orderManage.setAccidentType(serviceInfo2.getOrderInfo().getAccidentType());
				orderManage.setDescribe(serviceInfo2.getOrderInfo().getRemark());
				orderManage.setRepairTime(serviceInfo2.getOrderInfo().getRepairTime());
				orderManage.setWoState(woState);
				orderManage.setTurnOrderReson(serviceInfo2.getOrderInfo().getTurnOrderReson());
				orderManage.setEnginnerName(serviceInfo2.getStaffInfo().getName());
				orderManage.setEnginnerPhone(serviceInfo2.getStaffInfo().getPhone());
				orderManage.setPartMessage(serviceInfo2.getOrderInfo().getPartsTypeNumber() == null ? null
						: serviceInfo2.getOrderInfo().getPartsTypeNumber().split(";"));
				orderManage.setRejected(serviceInfo2.getCustPrai() == null ? null : serviceInfo2.getCustPrai());
				orderManage.setServiceType(serviceInfo2.getOrderInfo().getServiceType());
				orderManage.setMainTenanceFeedback(serviceInfo2.getOrderInfo().getMaintenanceFeedback());
				orderManage.setTreatmentMeasure(serviceInfo2.getOrderInfo().getTreatmentMeasure());
				orderManage.setOrderReason(serviceInfo2.getOrderInfo().getTreatmentState());
				orderManages.add(orderManage);
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, orderManages, page, limit);
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
		json.put("msg", "查询成功");
		json.put("count", orderManages.size());
		json.put("data", page == null || orderManages == null ? orderManages : orderManages.subList(page, limit));
		return json;
	}

	/**
	 * 关单接口：完结工单。
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/TechnicalCloseOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalCloseOrder() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String woNumber = request.getParameter("woNumber"); // 要完结的工单号
		Date closeTime = new Date(); // 完结工单的时间，取系统当前时间
		String woStatus = "已关单"; // 设置工单状态为已派单
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
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 更新工单信息
		boolean a = serviceManageServiceImpl.updateOrder(orderInfo);
		if (a) {
			json.put("code", 0);
			json.put("msg", "修改成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "修改失败");
		return json;
	}

	/**
	 * 技术主管零件订购:通过。
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/TechnicalPartOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalPartOrder() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String woNumber = request.getParameter("woNumber"); // 要零件通过的工单号
		String woStatus = "已通过"; // 设置工单状态为已派单
		// 创建工单对象接受信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoNumber(woNumber);
		orderInfo.setWoProgress(woStatus);
		Map<String, Object> args = new HashMap<>();
		args.put("工单号码", woNumber);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 更新工单信息
		boolean a = serviceManageServiceImpl.updateOrder(orderInfo);
		if (a) {
			json.put("code", 0);
			json.put("msg", "零件通过审核成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "零件通过审核失败");
		return json;
	}

	/**
	 * 技术主管零件订购:驳回。
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/TechnicalRejectedOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> TechnicalRejectedOrder() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String woNumber = request.getParameter("woNumber"); // 要驳回通过的工单号
		String content = request.getParameter("content"); // 要驳回的理由
		String woStatus = "已驳回"; // 设置工单状态为已派单
		// 创建工单对象接受信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoNumber(woNumber);
		orderInfo.setWoProgress(woStatus);
		Map<String, Object> args = new HashMap<>();
		args.put("工单号码", woNumber);
		args.put("反驳原因", content);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = new ServiceInfo();
		service.setCustPrai(content);
		service.setWoNumber(woNumber);
		// 更新工单信息
		boolean a = serviceManageServiceImpl.updateOrder(orderInfo);
		boolean b = serviceInfoService.upDateServiceInfo(service);
		if (a && b) {
			json.put("code", 0);
			json.put("msg", "零件反驳成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "零件反驳失败");
		return json;
	}

	/**
	 * 故障代码以及分类，默认查询所有。
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/faultClass", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> faultClass(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收故障分类
		Map<String, Object> json = new HashMap<>();
		json.put("图像质量空白", "B01~B99");
		json.put("图像质量问题", "C01~C99");
		json.put("图像位置不正确", "D01~D99");
		json.put("图像再现故障(浓度相关)", "E01~E99");
		json.put("图像再现故障(浓度无关)", "G01~G99");
		json.put("图像异常", "H01~H99");
		json.put("其他的图像质量不良", "K01~K99");
		json.put("纸张传输/处理故障", "L01~L99");
		json.put("原稿传输故障", "N01~N99");
		json.put("异常噪音/气味/外观", "P01~P03");
		json.put("控制器控制故障(图像打印控制器/传真等)", "Q01~Q03");
		json.put("主机控制故障", "R01~R99");
		json.put("软件故障", "S01~S99");
		json.put("机器/产物物理损坏/包装错误", "T01~T04");
		json.put("胶装器故障", "U01~U99");
		json.put("其他", "Z01");
		json.put("code", 0);
		return json;
	}
}
