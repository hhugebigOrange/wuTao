package com.xunwei.som.base.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.ecs.storage.Array;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.calendar.CalendarTool;
import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.pojo.CustomerKpi;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.EngineerKpi;
import com.xunwei.som.pojo.Kpi;
import com.xunwei.som.pojo.Maintenance;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.OrderStatistics;
import com.xunwei.som.pojo.SeatServiceKpi;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.StandardRate;
import com.xunwei.som.pojo.front.DeviceBasic;
import com.xunwei.som.pojo.front.MaintenanceService;
import com.xunwei.som.pojo.permissions.ParameterSetting;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.CustomerManageService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.MaintenanceserviceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Controller
public class KpiController extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private MaintenanceserviceImpl maintenanceserviceImpl = new MaintenanceserviceImpl();

	private CustomerManageService customerManageService = new CustomerManageServiceImpl();

	private UserService userService = new UserServiceImpl();

	private Map<String, Double> workTimes = new HashMap<>();

	// 用于保存每个用户的查询记录
	private Map<String, Object> export = new HashMap<>();

	// 用来存放每次导出的工程师KPI报表
	private List<StandardRate> exportStandardRates;
	// 用来存放工单时效表KPI
	private Map<String, String> orderPrescription = new HashMap<>();
	// 用来存放工单时效表KPI2
	private List<StandardRate> exportStandardRates2;
	// 用来存放每次导出的客户kpi报表
	private List<CustomerKpi> exportCustomerKpi;
	// 用来存放每次导出的分公司kpi报表
	private List<CustomerKpi> exportBranchKpi;
	// 用来存放每次导出的总公司kpi报表
	private List<CustomerKpi> exportHeadOfficeKpi;
	// 用来存放每次导出的坐席客服kpi报表
	private List<SeatServiceKpi> exportSeatServiceKpi;
	// 用来存放每次导出的设备运转正常率报表
	private List<Device> exportDeviceKpi;
	// 用来存放每次导出的运维合同报表
	private List<Contract> exportContractKpi;
	// 用来存放每次导出的设备管理报表
	private List<Device> exportDevicesKpi;
	// 用来存放每次导出的维修报表
	private List<DeviceBasic> exportDeviceBasicKpi;
	// 存放查询出来客户保养报表的结果
	private List<Maintenance> maintenancePerforms;
	// 存放查询出来客户保养报表的结果
	private List<MaintenanceService> exportPerforms;
	// 用来存放工单分析KPI报表查询结果集
	private List<EngineerKpi> orderAnalysis;

	/**
	 * 工程师Kpi报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/engineerKpiSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> engineerKpiSummary1() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String staffName = request.getParameter("staffName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		String identifier = null; // 标识符
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			identifier = "1";
		}
		List<ServiceInfo> engineerKpiServiceInfos = serviceInfoService.selectServiceInfoByengineerKpi(staffName, null,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		for (ServiceInfo serviceInfo : engineerKpiServiceInfos) {
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? 0
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? 0
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getTelRepon(),
							workTime, offWorkTime))); // 工程师响应用时
			engineerKpi.setArrTimeSlot(serviceInfo.getArrTime() == null ? 0
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getArrTime(), workTime, offWorkTime)); // 工程师到达用时
			engineerKpi.setProbSolveSlot(serviceInfo.getProbSolve() == null ? 0
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getProbSolve(), workTime,
							offWorkTime)); // 工程师解决用时
			if (engineerKpi.getResponseTime() != null && engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			if (engineerKpi.getProbSolveSlot() != null
					&& engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			engineerKpi.setSecondService(order.getPartsTypeNumber() == null ? "否" : "是");
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			engineerKpis.add(engineerKpi);
		}
		DecimalFormat df = new DecimalFormat("#.00");
		List<StandardRate> StandardRates = new ArrayList<>();
		for (StaffInfo staffInfo : staffInfoServiceImplnew.selectAllStaff()) {
			if (!(staffInfo.getPost().equals("工程师") || staffInfo.getPost().equals("技术主管"))) {
				continue;
			}
			double responseTimeRate = 0.0;// 响应时间达标率
			double arrTimeRate = 0.0;// 到达时间达标率
			double probSolveRate = 0.0;// 问题解决时间达标率
			Integer orderNum = 0;// 属于该工程师的工单数量
			double custSatisfactionNum = 0; // 有评价的工单
			double custSatisfaction = 0.0; // 客户满意度
			Integer turnOrderNum = 0; // 转单升级数量
			double secondService = 0.0; // 二次上门率
			Integer custSatisfactionPair = 0; // 客户投诉工单数量
			double arrTimeAvg = 0.0; // 到达现场平均用时
			for (EngineerKpi engineerKpi : engineerKpis) {
				// 如果名字相等，则算出各种成功率
				if (staffInfo.getName().equals(engineerKpi.getStaffName())) {
					orderNum++;
					if (engineerKpi.getResponseTimeRate().equals("是")) {
						responseTimeRate++;
					}
					if (engineerKpi.getArrTimeRate().equals("是")) {
						arrTimeRate++;
					}
					// 到达现场总共用时
					arrTimeAvg = arrTimeAvg + engineerKpi.getArrTimeSlot();
					if (engineerKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (engineerKpi.getWoStatus().equals("已转单")) {
						turnOrderNum++;
					}
					if (engineerKpi.getSecondService().equals("是")) {
						secondService++;
					}
					if (engineerKpi.getCustScore() != null) {
						if (engineerKpi.getCustScore() <= 2) {
							custSatisfactionPair++;
						}
						custSatisfactionNum++;
						custSatisfaction = custSatisfaction + engineerKpi.getCustScore();
					}
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				StandardRate StandardRate = new StandardRate();
				StandardRate.setStaffName(staffInfo.getName()); // 工程师姓名
				StandardRate.setResponseTimeRate(SOMUtils.getInt(responseTimeRate / orderNum * 100) + ""); // 响应时间达标率
				StandardRate.setArrTimeRate(SOMUtils.getInt(arrTimeRate / orderNum * 100) + ""); // 到达时间达标率
				StandardRate.setProbSolveRate(SOMUtils.getInt(probSolveRate / orderNum * 100) + ""); // 问题解决时间达标率
				StandardRate.setArrTimeAvg(Double.valueOf(df.format((arrTimeAvg / 60) / orderNum)) + ""); // 到达现场平均用时
				// 保养完成率
				StandardRate.setSecondService(SOMUtils.getInt(secondService / orderNum * 100) + ""); // 二次上门率
				StandardRate.setOrderNum(orderNum + ""); // 工单数量
				StandardRate.setOrderTurnNum(turnOrderNum); // 转单、升级数量
				StandardRate.setCustomerSatisfaction(custSatisfaction == 0.0 ? 0.0 + ""
						: SOMUtils.getIntOneToDouble(custSatisfaction / custSatisfactionNum) + ""); // 客户评价分数
				StandardRate.setCustComplaints(custSatisfactionPair); // 客户投诉
				StandardRates.add(StandardRate);
			}
		}
		exportStandardRates = StandardRates;
		export.put("exportStandardRates" + request.getParameter("username"), exportStandardRates);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, StandardRates, page, limit);
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
		json.put("msg", "工程师kpi数据");
		json.put("count", StandardRates.size());
		json.put("data", page == null || StandardRates == null ? StandardRates : StandardRates.subList(page, limit));
		return json;
	}

	// **************************************************************************************************************

	/**
	 * 客户Kpi报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerKpiSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerKpiSummary() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		String identifier = null; // 标识符
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName("");
			identifier = "1";
		}
		List<ServiceInfo> engineerKpiServiceInfos = serviceInfoService.selectServiceInfoByengineerKpi(null, custName,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		for (ServiceInfo serviceInfo : engineerKpiServiceInfos) {
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getTelRepon(),
							workTime, offWorkTime))); // 工程师响应用时
			engineerKpi.setArrTimeSlot(serviceInfo.getArrTime() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getArrTime(), workTime, offWorkTime)); // 工程师到达用时
			engineerKpi.setProbSolveSlot(serviceInfo.getProbSolve() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getProbSolve(), workTime,
							offWorkTime)); // 工程师解决用时
			if (engineerKpi.getResponseTime() != null && engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			if (engineerKpi.getProbSolveSlot() != null
					&& engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			engineerKpi.setSecondService(order.getPartsTypeNumber() == null ? "否" : "是");
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			engineerKpis.add(engineerKpi);
		}
		List<String> custNames = new ArrayList<>(); // 一次查询中，所有公司的名称
		for (EngineerKpi engineerKpi : engineerKpis) {
			if (!custNames.contains(engineerKpi.getCustName())) {
				custNames.add(engineerKpi.getCustName());
			}
		}
		List<CustomerKpi> customerKpis = new ArrayList<>(); // 客户Kpi
		for (String string : custNames) {
			double responseTimeRate = 0.0;// 响应时间达标率
			double arrTimeRate = 0.0;// 到达时间达标率
			double probSolveRate = 0.0;// 问题解决时间达标率
			Integer orderNum = 0;// 属于该工程师的工单数量
			Integer turnOrderNum = 0; // 转单升级数量
			for (EngineerKpi engineerKpi : engineerKpis) {
				if (engineerKpi.getCustName().equals(string)) {
					orderNum++;
					if (engineerKpi.getResponseTimeRate().equals("是")) {
						responseTimeRate++;
					}
					if (engineerKpi.getArrTimeRate().equals("是")) {
						arrTimeRate++;
					}
					if (engineerKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (engineerKpi.getWoStatus().equals("已转单")) {
						turnOrderNum++;
					}
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				CustomerKpi customerKpi = new CustomerKpi();
				customerKpi.setCustName(string);
				customerKpi.setResponseTimeRate(SOMUtils.getInt(responseTimeRate / orderNum * 100) + "%");
				customerKpi.setArrTimeRate(SOMUtils.getInt(arrTimeRate / orderNum * 100) + "%"); // 到达时间达标率
				customerKpi.setProbSolveRate(SOMUtils.getInt(probSolveRate / orderNum * 100) + "%"); // 问题解决时间达标率
				customerKpis.add(customerKpi);
			}
		}
		exportCustomerKpi = customerKpis;
		export.put("exportCustomerKpi" + request.getParameter("username"), exportCustomerKpi);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, customerKpis, page, limit);
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
		json.put("msg", "客户kpi数据");
		json.put("count", customerKpis.size());
		json.put("data", page == null || customerKpis == null ? customerKpis : customerKpis.subList(page, limit));
		return json;
	}

	// **************************************************************************************************************

	/**
	 * 分公司Kpi报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/branchKpiSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> branchKpiSummary() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		String identifier = null; // 标识符
		if (serviceArea == null) {
			serviceArea = "";
		}
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		List<ServiceInfo> engineerKpiServiceInfos = serviceInfoService.selectServiceInfoByengineerKpi(null, null,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		for (ServiceInfo serviceInfo : engineerKpiServiceInfos) {
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getTelRepon(),
							workTime, offWorkTime))); // 工程师响应用时
			engineerKpi.setArrTimeSlot(serviceInfo.getArrTime() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getArrTime(), workTime, offWorkTime)); // 工程师到达用时
			engineerKpi.setProbSolveSlot(serviceInfo.getProbSolve() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getProbSolve(), workTime,
							offWorkTime)); // 工程师解决用时
			if (engineerKpi.getResponseTime() != null && engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			if (engineerKpi.getProbSolveSlot() != null
					&& engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			engineerKpi.setSecondService(order.getPartsTypeNumber() == null ? "否" : "是");
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			engineerKpis.add(engineerKpi);
		}
		List<String> custNames = new ArrayList<>(); // 一次查询中，所有公司的名称
		for (EngineerKpi engineerKpi : engineerKpis) {
			if (!custNames.contains(engineerKpi.getCustName())) {
				custNames.add(engineerKpi.getCustName());
			}
		}
		double avgResponseTimeRate = 0.0; // 平均响应达标率
		double avgArrTimeRate = 0.0; // 平均到达达标率
		double avgProbTimeRate = 0.0; // 平均解决用时达标率
		List<CustomerKpi> customerKpis = new ArrayList<>(); // 客户Kpi
		for (String string : custNames) {
			double responseTimeRate = 0.0;// 响应时间达标率
			double arrTimeRate = 0.0;// 到达时间达标率
			double probSolveRate = 0.0;// 问题解决时间达标率
			Integer orderNum = 0;// 属于该工程师的工单数量
			double custSatisfactionNum = 0; // 有评价的工单
			double custSatisfaction = 0.0; // 客户满意度
			Integer turnOrderNum = 0; // 转单升级数量
			double secondService = 0.0; // 二次上门率
			Integer custSatisfactionPair = 0; // 客户投诉工单数量
			for (EngineerKpi engineerKpi : engineerKpis) {
				if (engineerKpi.getCustName().equals(string)) {
					orderNum++;
					if (engineerKpi.getResponseTimeRate().equals("是")) {
						responseTimeRate++;
					}
					if (engineerKpi.getArrTimeRate().equals("是")) {
						arrTimeRate++;
					}
					if (engineerKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (engineerKpi.getWoStatus().equals("已转单")) {
						turnOrderNum++;
					}
					if (engineerKpi.getSecondService().equals("是")) {
						secondService++;
					}
					if (engineerKpi.getCustScore() != null) {
						if (engineerKpi.getCustScore() <= 2) {
							custSatisfactionPair++;
						}
						custSatisfactionNum++;
						custSatisfaction = custSatisfaction + engineerKpi.getCustScore();
					}
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				CustomerKpi customerKpi = new CustomerKpi();
				customerKpi.setCustName(string); // 客户名称
				customerKpi.setResponseTimeRate(SOMUtils.getInt((responseTimeRate / orderNum * 100)) + "%"); // 响应时间达标率
				customerKpi.setArrTimeRate(SOMUtils.getInt((arrTimeRate / orderNum * 100)) + "%"); // 到达时间达标率
				customerKpi.setProbSolveRate(SOMUtils.getInt((probSolveRate / orderNum * 100)) + "%"); // 问题解决时间达标率
				customerKpi.setCustScore(custSatisfactionNum == 0 ? 0.0
						: SOMUtils.getIntOneToDouble(custSatisfaction / custSatisfactionNum)); // 客户评价分数
				avgResponseTimeRate = avgResponseTimeRate + SOMUtils.getInt((responseTimeRate / orderNum * 100));
				avgArrTimeRate = avgArrTimeRate + SOMUtils.getInt((arrTimeRate / orderNum * 100));
				avgProbTimeRate = avgProbTimeRate + SOMUtils.getInt((probSolveRate / orderNum * 100));
				customerKpis.add(customerKpi);
			}
		}
		exportBranchKpi = customerKpis;
		export.put("exportBranchKpi" + request.getParameter("username"), exportBranchKpi);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, customerKpis, page, limit);
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
		json.put("msg", "分公司kpi数据");
		json.put("count", customerKpis.size());
		json.put("avgResponseTimeRate",
				customerKpis.size() == 0 ? 0 : SOMUtils.getInt(avgResponseTimeRate / customerKpis.size()));
		json.put("avgArrTimeRate",
				customerKpis.size() == 0 ? 0 : SOMUtils.getInt(avgArrTimeRate / customerKpis.size()));
		json.put("avgProbTimeRate",
				customerKpis.size() == 0 ? 0 : SOMUtils.getInt(avgProbTimeRate / customerKpis.size()));
		json.put("data", page == null || customerKpis == null ? customerKpis : customerKpis.subList(page, limit));
		return json;
	}

	// **************************************************************************************************************

	/**
	 * 总公司Kpi报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/headOffice", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> headOffice() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		String identifier = null; // 标识符
		if (serviceArea == null) {
			serviceArea = "";
		}
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		List<ServiceInfo> engineerKpiServiceInfos = serviceInfoService.selectServiceInfoByengineerKpi(null, null,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		for (ServiceInfo serviceInfo : engineerKpiServiceInfos) {
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getTelRepon(),
							workTime, offWorkTime))); // 工程师响应用时
			engineerKpi.setArrTimeSlot(serviceInfo.getArrTime() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getArrTime(), workTime, offWorkTime)); // 工程师到达用时
			engineerKpi.setProbSolveSlot(serviceInfo.getProbSolve() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getProbSolve(), workTime,
							offWorkTime)); // 工程师解决用时
			if (engineerKpi.getResponseTime() != null && engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			if (engineerKpi.getProbSolveSlot() != null
					&& engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			engineerKpi.setSecondService(order.getPartsTypeNumber() == null ? "否" : "是");
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			engineerKpis.add(engineerKpi);
		}
		List<String> custNames = new ArrayList<>(); // 一次查询中，所有公司的名称
		for (EngineerKpi engineerKpi : engineerKpis) {
			if (!custNames.contains(engineerKpi.getCustName())) {
				custNames.add(engineerKpi.getCustName());
			}
		}
		List<CustomerKpi> customerKpis = new ArrayList<>(); // 客户Kpi
		for (String string : custNames) {
			double responseTimeRate = 0.0;// 响应时间达标率
			double arrTimeRate = 0.0;// 到达时间达标率
			double probSolveRate = 0.0;// 问题解决时间达标率
			Integer orderNum = 0;// 属于该工程师的工单数量
			double custSatisfactionNum = 0; // 有评价的工单
			double custSatisfaction = 0.0; // 客户满意度
			Integer turnOrderNum = 0; // 转单升级数量
			double secondService = 0.0; // 二次上门率
			Integer custSatisfactionPair = 0; // 客户投诉工单数量
			for (EngineerKpi engineerKpi : engineerKpis) {
				if (engineerKpi.getCustName().equals(string)) {
					orderNum++;
					if (engineerKpi.getResponseTimeRate().equals("是")) {
						responseTimeRate++;
					}
					if (engineerKpi.getArrTimeRate().equals("是")) {
						arrTimeRate++;
					}
					if (engineerKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (engineerKpi.getWoStatus().equals("已转单")) {
						turnOrderNum++;
					}
					if (engineerKpi.getSecondService().equals("是")) {
						secondService++;
					}
					if (engineerKpi.getCustScore() != null) {
						if (engineerKpi.getCustScore() <= 2) {
							custSatisfactionPair++;
						}
						custSatisfactionNum++;
						custSatisfaction = custSatisfaction + engineerKpi.getCustScore();
					}
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				CustomerKpi customerKpi = new CustomerKpi();
				customerKpi.setCustName(string); // 客户名称
				customerKpi.setResponseTimeRate(SOMUtils.getInt(responseTimeRate / orderNum * 100) + "%"); // 响应时间达标率
				customerKpi.setArrTimeRate(SOMUtils.getInt(arrTimeRate / orderNum * 100) + "%"); // 到达时间达标率
				customerKpi.setProbSolveRate(SOMUtils.getInt(probSolveRate / orderNum * 100) + "%"); // 问题解决时间达标率
				customerKpi.setCustScore(custSatisfactionNum == 0 ? 0.0
						: SOMUtils.getIntOneToDouble(custSatisfaction / custSatisfactionNum)); // 客户评价分数
				customerKpis.add(customerKpi);
			}
		}
		exportHeadOfficeKpi = customerKpis;
		export.put("exportHeadOfficeKpi" + request.getParameter("username"), exportHeadOfficeKpi);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, customerKpis, page, limit);
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
		json.put("msg", "中公司kpi数据");
		json.put("count", customerKpis.size());
		json.put("data", page == null || customerKpis == null ? customerKpis : customerKpis.subList(page, limit));
		return json;
	}

	// **************************************************************************************************************

	/**
	 * 坐席客服Kpi报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/seatServiceSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> seatServiceSummary() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String staffname = request.getParameter("staffName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		String identifier = null; // 标识符
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName("");
			identifier = "1";
		}
		List<ServiceInfo> engineerKpiServiceInfos = serviceInfoService.selectServiceInfoByengineerKpi(null, null,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		for (ServiceInfo serviceInfo : engineerKpiServiceInfos) {
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getTelRepon(),
							workTime, offWorkTime))); // 工程师响应用时
			engineerKpi.setArrTimeSlot(serviceInfo.getArrTime() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getArrTime(), workTime, offWorkTime)); // 工程师到达用时
			engineerKpi.setProbSolveSlot(serviceInfo.getProbSolve() == null ? null
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getProbSolve(), workTime,
							offWorkTime)); // 工程师解决用时
			if (engineerKpi.getSendTimeSlot() != null && engineerKpi.getSendTimeSlot() <= ParameterSetting.sendTime) {
				engineerKpi.setSendTimeRate("是");
			} else {
				engineerKpi.setSendTimeRate("否");
			}
			if (engineerKpi.getResponseTime() != null && engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			if (engineerKpi.getProbSolveSlot() != null
					&& engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			engineerKpi.setSecondService(order.getPartsTypeNumber() == null ? "否" : "是");
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			engineerKpi.setDistributeMan(order.getDistributeMan());
			engineerKpis.add(engineerKpi);
		}
		if (staffname != null && !staffname.trim().equals("")) {
			for (User user1 : userService.selectAllUser()) {
				if (user1.getUserName().equals(staffname)) {
					staffname = user1.getUserId();
					break;
				}
			}
		}
		List<String> staffName = new ArrayList<>(); // 一次查询中，所有客服的名字
		if (staffname != null && !staffname.trim().equals("")) {
			staffName.add(staffname);
		} else {
			for (EngineerKpi enginnerKpi : engineerKpis) {
				if (!staffName.contains(enginnerKpi.getDistributeMan())) {
					staffName.add(enginnerKpi.getDistributeMan());
				}
			}
		}
		DecimalFormat df = new DecimalFormat("#.00");
		List<SeatServiceKpi> SeatServiceKpis = new ArrayList<>(); // 客户Kpi
		for (String string : staffName) {
			double sendTimeRate = 0.0;// 受理时间达标率
			Integer orderNum = 0;// 属于客服的工单数量
			Integer custSatisfactionPair = 0; // 客户投诉工单数量
			for (EngineerKpi engineerKpi : engineerKpis) {
				if (engineerKpi.getDistributeMan() == null) {
					continue;
				}
				if (engineerKpi.getDistributeMan().equals(string)) {
					orderNum++;
					if (engineerKpi.getSendTimeRate().equals("是")) {
						sendTimeRate++;
					}
					if (engineerKpi.getCustScore() != null) {
						if (engineerKpi.getCustScore() <= 2) {
							custSatisfactionPair++;
						}
					}
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				SeatServiceKpi SeatServiceKpi = new SeatServiceKpi();
				SeatServiceKpi.setStaffName(userService.selectByUserId(string).getUserName());
				SeatServiceKpi.setChiefSendTimeRate(SOMUtils.getInt(sendTimeRate / orderNum * 100) + "%"); //
				SeatServiceKpi.setOrderNum(orderNum);
				SeatServiceKpi.setCustComplaints(custSatisfactionPair);
				SeatServiceKpis.add(SeatServiceKpi);
			}
		}
		exportSeatServiceKpi = SeatServiceKpis;
		export.put("exportSeatServiceKpi" + request.getParameter("username"), exportSeatServiceKpi);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, SeatServiceKpis, page, limit);
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
		json.put("msg", "分公司kpi数据");
		json.put("count", SeatServiceKpis.size());
		json.put("data",
				page == null || SeatServiceKpis == null ? SeatServiceKpis : SeatServiceKpis.subList(page, limit));
		return json;
	}

	// **********************************************************************************************************************************

	/**
	 * 工单分析表KPI
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderAnalysis", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderAnalysis() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String custName = request.getParameter("custName"); // 客户名称
		String serviceArea = request.getParameter("serviceArea"); // 服务区域
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		String c = request.getParameter("c");
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
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		String identifier = null;
		if (serviceArea == null) {
			serviceArea = "";
		}
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		faultType = "123".split(",");
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		double arrive1 = 0;
		double arrive2 = 0;
		double arrive3 = 0;
		double prob1 = 0;
		double prob2 = 0;
		double prob3 = 0;
		// 当前分公司所有完成的工单
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		double d = 0.0;
		List<ServiceInfo> services = serviceInfoService.selectServiceInfoByengineerKpi(null, custName,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		for (ServiceInfo serviceInfo : services) {
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? null
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getSendTime(), serviceInfo.getTelRepon(), workTime,
							offWorkTime))); // 工程师响应用时
			if (serviceInfo.getArrTime() == null) {
				engineerKpi.setArrTimeSlot(null);
			} else {
				d = Double.parseDouble(SOMUtils.getIntOne(
						CalendarTool.getDownTime(order.getSendTime(), serviceInfo.getArrTime(), workTime, offWorkTime)
								/ 60));
				engineerKpi.setArrTimeSlot(d == 0.0 ? 0.0 : d); // 工程师到达用时
			}
			if (serviceInfo.getProbSolve() == null) {
				engineerKpi.setProbSolveSlot(null);
			} else {
				d = Double.parseDouble(SOMUtils.getIntOne(
						CalendarTool.getDownTime(order.getSendTime(), serviceInfo.getProbSolve(), workTime, offWorkTime)
								/ 60));
				engineerKpi.setProbSolveSlot(d == 0.0 ? 0.0 : d);
			}
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setTreatmentMeasure(order.getTreatmentMeasure());
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= 2) {
				arrive1++;
			} else if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() > 2
					&& engineerKpi.getArrTimeSlot() <= 4) {
				arrive2++;
			} else if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() > 4) {
				arrive3++;
			}
			if (engineerKpi.getProbSolveSlot() != null && engineerKpi.getProbSolveSlot() <= 4) {
				prob1++;
			} else if (engineerKpi.getProbSolveSlot() != null && engineerKpi.getProbSolveSlot() > 4
					&& engineerKpi.getProbSolveSlot() <= 8) {
				prob2++;
			} else if (engineerKpi.getProbSolveSlot() != null && engineerKpi.getProbSolveSlot() > 8) {
				prob3++;
			}
			engineerKpis.add(engineerKpi);
		}
		double aTotal = arrive1 + arrive2 + arrive3;
		double pTotal = prob1 + prob2 + prob3;
		orderAnalysis = engineerKpis;
		export.put("orderAnalysis" + request.getParameter("username"), orderAnalysis);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, engineerKpis, page, limit);
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
		json.put("msg", "工单分析数据");
		json.put("count", engineerKpis.size());
		json.put("data", page == null || engineerKpis == null ? engineerKpis : engineerKpis.subList(page, limit));
		if (aTotal > 0 && pTotal > 0) {
			json.put("arrive1", arrive1);
			json.put("arrive2", arrive2);
			json.put("arrive3", arrive3);
			json.put("prob1", prob1);
			json.put("prob2", prob2);
			json.put("prob3", prob3);
		}
		return json;
	}

	// **********************************************************************************************************************************

	/**
	 * 维修报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/repairReport", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> repairReport() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String custName = request.getParameter("custName"); // 客户名称
		String serviceArea = request.getParameter("serviceArea"); // 服务区域
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		String c = request.getParameter("c");
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
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		String identifier = null;
		if (serviceArea == null) {
			serviceArea = "";
		}
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		faultType = "123".split(",");
		// 当前分公司所有完成的工单
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		List<ServiceInfo> services = serviceInfoService.selectServiceInfoByengineerKpi(null, custName,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<DeviceBasic> deviceKpis = new ArrayList<>();
		Map<String, Integer> faultName = new HashMap<>();
		for (ServiceInfo serviceInfo : services) {
			DeviceBasic deviceKpi = new DeviceBasic();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			deviceKpi.setWoNumber(serviceInfo.getWoNumber());
			deviceKpi.setCustName(device.getCustArea());
			deviceKpi.setUnitType(device.getUnitType());
			deviceKpi.setDeviceBrand(device.getDeviceBrand());
			deviceKpi.setEsNumber(device.getEsNumber());
			deviceKpi.setMachCode(device.getMachCode());
			deviceKpi.setServiceArea(SOMUtils.CompToOrderNumTo(order.getWoNumber().substring(0, 2)));
			deviceKpi.setDepartment(device.getDepartment());
			deviceKpi.setLocation(device.getLocation());
			deviceKpi.setRepairTime(order.getRepairTime());
			deviceKpi.setProbTime(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve());
			deviceKpi.setBwReader(device.getBwReader());
			deviceKpi.setCoReader(device.getColorReader());
			deviceKpi.setAccidentType(order.getFaultClass());
			deviceKpi.setFaultNo(order.getFalutNo());
			deviceKpi.setRemark(order.getRemark());
			deviceKpi.setMaintenanceFeedback(order.getTreatmentMeasure());
			if (!faultName.containsKey(order.getFaultClass())) {
				faultName.put(order.getFaultClass(), 1);
			} else {
				faultName.put(order.getFaultClass(), faultName.get(order.getFaultClass()) + 1);
			}
			deviceKpis.add(deviceKpi);
		}
		List<Kpi> kpis = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : faultName.entrySet()) {
			Kpi kpi = new Kpi(entry.getKey(), entry.getValue() + "");
			kpis.add(kpi);
		}
		exportDeviceBasicKpi = deviceKpis;
		export.put("exportDeviceBasicKpi" + request.getParameter("username"), exportDeviceBasicKpi);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, deviceKpis, page, limit);
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
		json.put("msg", "维修报表数据");
		json.put("kpi", kpis);
		json.put("count", deviceKpis.size());
		json.put("data", page == null || deviceKpis == null ? deviceKpis : deviceKpis.subList(page, limit));
		return json;
	}

	// *********************************************************************************************************************

	/**
	 * 设备运转率
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/equipmentOperation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> equipmentOperation() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有开始时间，则默认设置为当月一号
		if ("".equals(startDate) || startDate == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
		} else {
			startDate = startDate + " 00:00:00";
		}
		// 如果查询条件没有结束时间，则默认设置为当前系统时间
		if ("".equals(endDate) || endDate == null) {
			endDate = ExcelUtils.fmtOne.format(new Date());
		} else {
			endDate = endDate + " 23:59:59";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// 按照当前时间的天数来算理论的工作时间
		double workTimie = Double.valueOf(DurationFormatUtils.formatPeriod(ExcelUtils.fmtOne.parse(startDate).getTime(),
				ExcelUtils.fmtOne.parse(endDate).getTime(), "H")) + 1;
		workTimes.put(request.getParameter("username"), workTimie);
		String identifier = null;
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (serviceArea == null) {
			serviceArea = "";
		}
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageWithLimit(request, page, limit);
		if (para[0] == null) {
			// 默认显示第一页数据
			page = 0;
			limit = 20;
		} else {
			page = para[0];
			limit = para[1];
		}
		String[] faultType = { "事故类" };
		List<Device> exportDeviceKpi = customerManageService.selectByDeviceKpi(user.getCustName(), custName, null, null,
				null, identifier);
		List<Device> deviceKpi = customerManageService.selectByDeviceKpi(user.getCustName(), custName, null, page,
				limit, identifier);
		List<ServiceInfo> deviceService = serviceInfoService.selectServiceInfByDynamic(
				SOMUtils.orderNumToComp(serviceArea), custName, startDate, endDate, null, null, null, null, null, null,
				faultType, null, null, null, null, identifier);
		Map<String, Double> downTime = new HashMap<>(); // 停机时间
		// 计算每个设备的停机时间
		for (ServiceInfo serviceInfo : deviceService) {
			double a = 0.0;
			if (serviceInfo.getProbSolve() == null) {
				continue;
			}
			a = Double.valueOf(DurationFormatUtils.formatPeriod(serviceInfo.getOrderInfo().getRepairTime().getTime(),
					serviceInfo.getProbSolve().getTime(), "m"));
			if (downTime.get(serviceInfo.getOrderInfo().getMachCode()) == null) {
				downTime.put(serviceInfo.getOrderInfo().getMachCode(), a);
			} else {
				downTime.put(serviceInfo.getOrderInfo().getMachCode(),
						downTime.get(serviceInfo.getOrderInfo().getMachCode()) + a);
			}
		}
		for (Device device : exportDeviceKpi) {
			double a = 0.0;
			if (downTime.containsKey(device.getMachCode())) {
				a = downTime.get(device.getMachCode());
				device.setDownTime(Double.valueOf(SOMUtils.getIntOne(a / 60)));
				device.setWorkTime(Double.valueOf(SOMUtils.getIntOne((workTimie * 60 - a) / 60)));
				device.setOperationRate(SOMUtils.getInt((device.getWorkTime() / workTimie) * 100) + "%");
			} else {
				device.setDownTime(0.0);
				device.setWorkTime(workTimie);
				device.setOperationRate("100%");
			}
		}
		for (Device device : deviceKpi) {
			double a = 0.0;
			if (downTime.containsKey(device.getMachCode())) {
				a = downTime.get(device.getMachCode());
				device.setDownTime(Double.valueOf(SOMUtils.getIntOne(a / 60)));
				device.setWorkTime(Double.valueOf(SOMUtils.getIntOne((workTimie * 60 - a) / 60)));
				device.setOperationRate(SOMUtils.getInt((device.getWorkTime() / workTimie) * 100) + "%");
			} else {
				device.setDownTime(0.0);
				device.setWorkTime(workTimie);
				device.setOperationRate("100%");
			}
		}
		export.put("exportDeviceKpi" + request.getParameter("username"), exportDeviceKpi);
		json.put("code", 0);
		json.put("msg", "分公司kpi数据");
		json.put("count", exportDeviceKpi.size());
		json.put("data", deviceKpi);
		json.put("设备正常运转率", deviceKpi);
		return json;
	}

	// *************************************************************************************8

	/**
	 * 图表的设备运转率
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/totalEquipmentOperation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> totalEquipmentOperation() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有开始时间，则默认设置为当月一号
		if ("".equals(startDate) || startDate == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
		} else {
			startDate = startDate + " 00:00:00";
		}
		// 如果查询条件没有结束时间，则默认设置为当前系统时间
		if ("".equals(endDate) || endDate == null) {
			endDate = ExcelUtils.fmtOne.format(new Date());
		} else {
			endDate = endDate + " 23:59:59";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// 按照当前时间的天数来算理论的工作时间
		double workTimie = Double.valueOf(DurationFormatUtils.formatPeriod(ExcelUtils.fmtOne.parse(startDate).getTime(),
				ExcelUtils.fmtOne.parse(endDate).getTime(), "H")) + 1;
		String identifier = null;
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (serviceArea == null) {
			serviceArea = "";
		}
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
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
		List<Device> exportDeviceKpi = customerManageService.selectByDeviceKpi(user.getCustName(), custName, null, null,
				null, identifier);
		if (exportDeviceKpi == null || exportDeviceKpi.size() <= 0) {
			json.put("code", 0);
			json.put("设备正常运转率", 0);
			return json;
		}
		double totalDownTime = 0; // 总共的停机时间
		String[] faultType = { "事故类" };
		List<ServiceInfo> deviceService = serviceInfoService.selectServiceInfByDynamic(
				SOMUtils.orderNumToComp(serviceArea), custName, startDate, endDate, null, null, null, null, null, null,
				faultType, null, null, null, null, identifier);
		for (Device device : exportDeviceKpi) {
			if (deviceService == null || deviceService.size() <= 0) {
				device.setDownTime(0.0);
				device.setWorkTime(workTimie);
				device.setOperationRate("100%");
				continue;
			}
			double a = 0.0;
			for (ServiceInfo serviceInfo : deviceService) {
				if (!serviceInfo.getOrderInfo().getMachCode().equals(device.getMachCode())) {
					continue;
				}
				if (serviceInfo.getProbSolve() == null) {
					continue;
				}
				a = a + Double
						.valueOf(DurationFormatUtils.formatPeriod(serviceInfo.getOrderInfo().getRepairTime().getTime(),
								serviceInfo.getProbSolve().getTime(), "m"));
				device.setDownTime(Double.valueOf(SOMUtils.getIntOne(a / 60)));
				device.setWorkTime(Double.valueOf(SOMUtils.getIntOne((workTimie * 60 - a) / 60)));
				device.setOperationRate(SOMUtils.getInt((device.getWorkTime() / workTimie) * 100) + "%");
				totalDownTime = totalDownTime + Double
						.valueOf(DurationFormatUtils.formatPeriod(serviceInfo.getOrderInfo().getRepairTime().getTime(),
								serviceInfo.getProbSolve().getTime(), "m"));
			}
		}
		totalDownTime = totalDownTime / 60;
		json.put("code", 0);
		json.put("设备正常运转率", SOMUtils.getInt((1 - (totalDownTime / (workTimie * exportDeviceKpi.size()))) * 100));
		return json;
	}

	// *************************************************************************************************

	/**
	 * 客户保养
	 * 
	 * @return
	 */
	@RequestMapping(value = "/maintenanceSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> maintenanceSummary() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String custName = request.getParameter("custName");// 客户名称
		String serviceArea = request.getParameter("serviceArea");// 服务区域
		String enginnerName = request.getParameter("enginnerName");// 工程师姓名
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 分页
		String identifier = null;
		// 判断是用户还是公司账号
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			serviceArea = request.getParameter("serviceArea");
		}
		if (serviceArea != null) {
			if (serviceArea.equals("广州乐派数码科技有限公司") || serviceArea.equals("系统推进部") || serviceArea.equals("行业客户部")) {
				serviceArea = request.getParameter("serviceArea");
				identifier = "1";
			}
		}
		// 先查出相应的保养设备
		List<Maintenance> maintenancePerform = maintenanceserviceImpl.selectmaintenance(custName, serviceArea,
				enginnerName, "", "", "", null, null, identifier);
		// 遍历保养执行
		for (Maintenance maintenance : maintenancePerform) {
			maintenance.setMaintenStatus(maintenance.getMaintenanceState() == 0 ? "未完成" : "已完成");
		}
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, maintenancePerform, page, limit);
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
		maintenancePerforms = maintenancePerform;
		export.put("maintenancePerforms" + request.getParameter("username"), maintenancePerforms);
		json.put("code", 0);
		json.put("msg", "客户满意度管理数据");
		json.put("count", maintenancePerform == null ? 0 : maintenancePerform.size());
		if (page == null) {
			json.put("data", maintenancePerform.size() > 20 ? maintenancePerform.subList(0, 20)
					: maintenancePerform.subList(0, maintenancePerform.size() - 1));
		} else {
			json.put("data", maintenancePerform.subList(page, limit));
		}
		return json;
	}

	// **************************************************************************************************************

	/**
	 * 分公司保养
	 * 
	 * @return
	 */
	@RequestMapping(value = "/maintenanceReport", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> maintenanceReport(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受数据
		String staffName = request.getParameter("staffName");
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		Map<String, Object> json = new HashMap<>();
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		String identifier = null;
		// 判断是用户还是公司账号
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			serviceArea = request.getParameter("serviceArea");
		} else if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			identifier = "1";
		}
		// 先查出相应的保养设备
		List<Maintenance> maintenancePerform = maintenanceserviceImpl.selectmaintenance(custName, serviceArea,
				staffName, "", "", "", null, null, identifier);
		List<String> custNames = new ArrayList<>();
		List<Device> Devices = customerManageService.selectByDynamic(null, null, null, null, null, null, null);
		Map<String, Integer> deviceNumbers = new HashMap<>();
		for (Maintenance maintenance : maintenancePerform) {
			if (!custNames.contains(maintenance.getCustName())) {
				custNames.add(maintenance.getCustName());
				deviceNumbers.put(maintenance.getCustName(), 0);
			}
		}
		for (Device device : Devices) {
			if (device.getCustArea() == null) {
				continue;
			}
			if (deviceNumbers.containsKey(device.getCustArea())) {
				deviceNumbers.put(device.getCustArea(), deviceNumbers.get(device.getCustArea()) + 1);
			}
		}

		List<MaintenanceService> MaintenanceServices = new ArrayList<>();
		for (String string : custNames) {
			double planDeviceNumber = 0.0; // 计划保养数量
			double reallyOverDeviceNumber = 0.0; // 实际完成数量
			for (Maintenance maintenance : maintenancePerform) {
				if (maintenance.getCustName().equals(string)) {
					planDeviceNumber++;
					if (maintenance.getMaintenanceState() != 0) {
						reallyOverDeviceNumber++;
					}
				}
			}
			MaintenanceService MaintenanceService = new MaintenanceService();
			MaintenanceService.setCustName(string);
			MaintenanceService.setDeviceNumber(deviceNumbers.get(string));
			MaintenanceService.setPlanDeviceNumber(SOMUtils.getInt(planDeviceNumber));
			MaintenanceService.setReallyOverDeviceNumber(SOMUtils.getInt(reallyOverDeviceNumber));
			MaintenanceService.setNotOverDeviceNumber(SOMUtils.getInt(planDeviceNumber - reallyOverDeviceNumber));
			MaintenanceService.setMaintenanceRate(planDeviceNumber == 0 ? 0 + ""
					: SOMUtils.getInt(reallyOverDeviceNumber / planDeviceNumber * 100) + "%");
			MaintenanceServices.add(MaintenanceService);
		}
		exportPerforms = MaintenanceServices;
		export.put("exportPerforms" + request.getParameter("username"), exportPerforms);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, MaintenanceServices, page, limit);
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
		json.put("msg", "工程师kpi数据");
		json.put("count", MaintenanceServices.size());
		json.put("data", page == null || MaintenanceServices == null ? MaintenanceServices
				: MaintenanceServices.subList(page, limit));
		return json;
	}

	// *************************************************************************************************************

	/**
	 * 设备管理报表
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/equipmentManagementReport", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> equipmentManagementReport() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受查询条件
		Map<String, Object> json = new HashMap<>();
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String contartctNo = request.getParameter("contartctNo");
		Date date = new Date();
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageWithLimit(request, page, limit);
		if (para[0] == null) {
			// 默认显示第一页数据
			page = 0;
			limit = 20;
		} else {
			page = para[0];
			limit = para[1];
		}
		String identifier = null;
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (serviceArea == null) {
			serviceArea = "";
		}
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		exportDevicesKpi = customerManageService.selectByDeviceKpi(user.getCustName(), custName, contartctNo, null,
				null, identifier);
		List<Device> devices = customerManageService.selectByDeviceKpi(user.getCustName(), custName, contartctNo, page,
				limit, identifier);
		for (Device device : devices) {
			if (device.getInstalledTime() == null) {
				device.setDeviceUserYear("NA");
			} else {
				device.setDeviceUserYear(SOMUtils.getIntOne(Double.valueOf(
						DurationFormatUtils.formatPeriod(device.getInstalledTime().getTime(), date.getTime(), "d"))
						/ 365));
			}
		}
		export.put("exportDevicesKpi" + request.getParameter("username"), exportDevicesKpi);
		json.put("code", 0);
		json.put("msg", "设备管理报表");
		json.put("count", exportDevicesKpi.size());
		json.put("data", devices);
		return json;
	}

	// *************************************************************************************************

	/**
	 * 匹配运维合同报表
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/operationalContract", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> operationAndMaintenance() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受查询参数
		Map<String, Object> json = new HashMap<>();
		String contractNo = request.getParameter("contractNo");
		String custName = request.getParameter("custName");
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageWithLimit(request, page, limit);
		if (para[0] == null) {
			// 默认显示第一页数据
			page = 0;
			limit = 20;
		} else {
			page = para[0];
			limit = para[1];
		}
		String identifier = null;
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName("");
			identifier = "1";
		}
		exportContractKpi = customerManageService.selectByKpi(contractNo, custName, user.getCustName(), null, null,
				identifier);
		List<Contract> contracts = customerManageService.selectByKpi(contractNo, custName, user.getCustName(), page,
				limit, identifier);
		// 保存数据
		Date date = new Date();
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
		json.put("code", 0);
		json.put("msg", "合同管理数据");
		json.put("count", exportContractKpi.size());
		json.put("data", contracts);
		return json;
	}

	// ***************************************************************************************************************

	/**
	 * 导出工程师汇总kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/expoetEngineerKpiSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> engineerKpiSummary(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 设置表头
		String[] Titles = { "工单师姓名", "响应时间达标率", "到达时间达标率", "问题解决时间达标率", "二次上门率", "工单数量", "转单/升级数量", "客户满意度", "客户投诉" };
		String picture = request.getParameter("picture");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/enginner.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		List<StandardRate> exportStandardRates = (List<StandardRate>) export
				.get("exportStandardRates" + request.getParameter("username"));
		// 循环将数据写入Excel
		for (StandardRate StandardRate : exportStandardRates) {
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(StandardRate.getStaffName()); // 工程师姓名
			row.createCell(2).setCellValue(StandardRate.getResponseTimeRate() + "%"); // 响应时间达标率
			row.createCell(3).setCellValue(StandardRate.getArrTimeRate() + "%"); // 到达时间达标率
			row.createCell(4).setCellValue(StandardRate.getProbSolveRate() + "%"); // 问题解决时间达标率
			row.createCell(5).setCellValue(StandardRate.getSecondService() + "%"); // 二次上门率
			row.createCell(6).setCellValue(StandardRate.getOrderNum()); // 工单数量
			row.createCell(7).setCellValue(StandardRate.getOrderTurnNum()); // 转单数量
			row.createCell(8).setCellValue(StandardRate.getCustomerSatisfaction()); // 客户满意度
			row.createCell(9).setCellValue(StandardRate.getCustComplaints()); // 客户投诉
		}
		FileOutputStream fileOut = null;
		try {
			if (picture == null || picture.trim().equals("")) {
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/enginner.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/enginner.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(picture.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/enginner.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/enginner.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	// *************************************************************************************************************

	/**
	 * 导出客户汇总kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportCustomerKpiSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportCustomerKpiSummary(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String[] Titles = { "客户名称", "响应时间达标率", "到达时间达标率", "问题解决时间达标率" };
		Map<String, Object> json = new HashMap<>();
		String string = request.getParameter("picture");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/customer.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		List<CustomerKpi> exportCustomerKpi = (List<CustomerKpi>) export
				.get("exportCustomerKpi" + request.getParameter("username"));
		for (CustomerKpi customer : exportCustomerKpi) {
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(customer.getCustName());
			row.createCell(2).setCellValue(customer.getResponseTimeRate());
			row.createCell(3).setCellValue(customer.getArrTimeRate());
			row.createCell(4).setCellValue(customer.getProbSolveRate());
		}
		FileOutputStream fileOut = null;
		try {
			if (string == null || string.trim().equals("")) {
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/customer.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/customer.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(string.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/customer.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/customer.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	// *************************************************************************************************************
	/**
	 * 导出分公司kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportBranchKpi", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportBranchKpi(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 设置表头
		String[] Titles = { "客户名称", "响应时间达标率", "到达用时达标率", "问题解决达标率", "客户满意度" };
		String string = request.getParameter("picture");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/branchKpi.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		// 循环将数据写入Excel
		List<CustomerKpi> exportBranchKpis = (List<CustomerKpi>) export
				.get("exportBranchKpi" + request.getParameter("username"));
		for (CustomerKpi exportBranchKpi : exportBranchKpis) {
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(exportBranchKpi.getCustName());
			row.createCell(2).setCellValue(exportBranchKpi.getResponseTimeRate());
			row.createCell(3).setCellValue(exportBranchKpi.getArrTimeRate());
			row.createCell(4).setCellValue(exportBranchKpi.getProbSolveRate());
			row.createCell(5).setCellValue(exportBranchKpi.getCustScore());
		}
		Map<String, Object> json = new HashMap<>();
		FileOutputStream fileOut = null;
		try {
			if (string == null || string.trim().equals("")) {
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/branchKpi.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/branchKpi.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(string.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/branchKpi.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/branchKpi.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	// *******************************************************************************************************

	/**
	 * 导出总公司kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportHeadOffice", produces = "application/json; charset=utf-8")
	public void exportHeadOffice(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 设置表头
		String[] Titles = { "客户名称", "响应时间达标率", "到达用时达标率", "问题解决达标率", "客户满意度" };
		String tableName = "总公司kpi数据";
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		row = sheet.createRow(1);
		// 循环将数据写入Excel
		List<CustomerKpi> exportHeadOfficeKpi = (List<CustomerKpi>) export
				.get("exportHeadOfficeKpi" + request.getParameter("username"));
		for (CustomerKpi headoffKpi : exportHeadOfficeKpi) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(headoffKpi.getCustName());
			row.createCell(2).setCellValue(headoffKpi.getResponseTimeRate());
			row.createCell(3).setCellValue(headoffKpi.getArrTimeRate());
			row.createCell(4).setCellValue(headoffKpi.getProbSolveRate());
			row.createCell(5).setCellValue(headoffKpi.getCustScore());
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// *******************************************************************************************************

	/**
	 * 导出坐席客服kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportSeatService", produces = "application/json; charset=utf-8")
	public void exportSeatService(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String tableName = "坐席客服KPI报表";
		// 设置表头
		String[] Titles = { "客户姓名", "受理时间达标率", "工单数量", "客户投诉" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		row = sheet.createRow(1);
		// 循环将数据写入Excel
		List<SeatServiceKpi> exportSeatServiceKpi = (List<SeatServiceKpi>) export
				.get("exportSeatServiceKpi" + request.getParameter("username"));
		for (SeatServiceKpi seatServiceKpi : exportSeatServiceKpi) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(seatServiceKpi.getStaffName());
			row.createCell(2).setCellValue(seatServiceKpi.getChiefSendTimeRate());
			row.createCell(3).setCellValue(seatServiceKpi.getOrderNum());
			row.createCell(4).setCellValue(seatServiceKpi.getCustComplaints());
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// *************************************************************************************************************
	/**
	 * 导出工单分析表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportWorkAnalysisTable", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportWorkAnalysisTable(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 设置表头
		String[] Titles = { "工单号码", "客户名称", "服务区域", "工单来源", "设备类型", "机器编码", "服务类型", "服务类别", "服务级别", "报修时间", "派单时间",
				"响应时间", "到达时间", "完成时间", "客服派单用时", "响应用时(min)", "到达现场耗时", "处理用时", "责任工程师", "转单/升级", "后续工程师", "工单状态",
				"处理措施" };
		Map<String, Object> json = new HashMap<>();
		String picture1 = request.getParameter("picture1");
		String picture2 = request.getParameter("picture2");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/orderanalysis.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		List<EngineerKpi> orderAnalysis = (List<EngineerKpi>) export
				.get("orderAnalysis" + request.getParameter("username"));
		// 循环将数据写入Excel
		for (EngineerKpi order : orderAnalysis) {
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(order.getWoNumber());
			row.createCell(2).setCellValue(order.getCustName());
			row.createCell(3).setCellValue(SOMUtils.CompToOrderNumTo(order.getWoNumber().substring(0, 2)));
			row.createCell(4).setCellValue(order.getRepairType());
			row.createCell(5).setCellValue(order.getDevName());
			row.createCell(6).setCellValue(order.getMachCode());
			row.createCell(7).setCellValue(order.getFaultType());
			row.createCell(8).setCellValue(order.getAccidentType());
			row.createCell(9).setCellValue("");
			row.createCell(10).setCellValue(ExcelUtils.fmtOne.format(order.getRepairTime()));
			row.createCell(11).setCellValue(ExcelUtils.fmtOne.format(order.getSendTime()));
			row.createCell(12)
					.setCellValue(order.getTelRepon() == null ? null : ExcelUtils.fmtOne.format(order.getTelRepon()));
			row.createCell(13)
					.setCellValue(order.getArrTime() == null ? null : ExcelUtils.fmtOne.format(order.getArrTime()));
			row.createCell(14)
					.setCellValue(order.getProbSolve() == null ? null : ExcelUtils.fmtOne.format(order.getProbSolve()));
			row.createCell(15).setCellValue(order.getSendTimeSlot());
			if (order.getResponseTime() == null) {
				row.createCell(16).setCellValue("");
			} else {
				row.createCell(16).setCellValue(order.getResponseTime());
			}
			if (order.getArrTimeSlot() == null) {
				row.createCell(17).setCellValue("");
			} else {
				row.createCell(17).setCellValue(order.getArrTimeSlot());
			}

			if (order.getProbSolveSlot() == null) {
				row.createCell(18).setCellValue("");
			} else {
				row.createCell(18).setCellValue(order.getProbSolveSlot());
			}
			row.createCell(19).setCellValue(order.getStaffName());
			row.createCell(20).setCellValue(order.getOrderTurnNum());
			row.createCell(21).setCellValue(order.getStaffName2());
			row.createCell(22).setCellValue(order.getWoStatus());
			row.createCell(23).setCellValue(order.getTreatmentMeasure());
		}
		FileOutputStream fileOut = null;
		try {
			if (picture1 == null || picture1.trim().equals("")) {
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/orderanalysis.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/orderanalysis.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(picture1.substring(22));
				byte[] c = decoder.decodeBuffer(picture2.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFClientAnchor anchor1 = new XSSFClientAnchor(0, 0, 0, 0, (short) 11, 1, (short) 20, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				patriarch.createPicture(anchor1, wb.addPicture(c, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/orderanalysis.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/orderanalysis.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	// **********************************************************************************************************************************

	/**
	 * 导出维修报表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportRepairReport", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportRepairReport(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 设置表头
		String[] Titles = { "服务单号", "客户名称", "设备型号", "设备品牌", "设备序列号", "机器编码", "服务区域", "部门", "设备位置", "报修时间", "完成时间",
				"黑白读数", "彩色读数", "故障现象分类", "处理措施", "故障代码", "故障描述" };
		Map<String, Object> json = new HashMap<>();
		String string = request.getParameter("picture");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/maintenance.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		List<DeviceBasic> exportDeviceBasicKpi = (List<DeviceBasic>) export
				.get("exportDeviceBasicKpi" + request.getParameter("username"));
		// 循环将数据写入Excel
		for (DeviceBasic deviceBasic : exportDeviceBasicKpi) {
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(deviceBasic.getWoNumber());
			row.createCell(2).setCellValue(deviceBasic.getCustName());
			row.createCell(3).setCellValue(deviceBasic.getUnitType());
			row.createCell(4).setCellValue(deviceBasic.getDeviceBrand());
			row.createCell(5).setCellValue(deviceBasic.getEsNumber());
			row.createCell(6).setCellValue(deviceBasic.getMachCode());
			row.createCell(7).setCellValue(deviceBasic.getServiceArea());
			row.createCell(8).setCellValue(deviceBasic.getDepartment());
			row.createCell(9).setCellValue(deviceBasic.getLocation());
			row.createCell(10).setCellValue(ExcelUtils.fmtOne.format(deviceBasic.getRepairTime()));
			row.createCell(11).setCellValue(
					deviceBasic.getProbTime() == null ? null : ExcelUtils.fmtOne.format(deviceBasic.getProbTime()));
			row.createCell(12).setCellValue(deviceBasic.getBwReader());
			row.createCell(13).setCellValue(deviceBasic.getCoReader());
			row.createCell(14).setCellValue(deviceBasic.getAccidentType());
			row.createCell(15).setCellValue(deviceBasic.getMaintenanceFeedback());
			row.createCell(16).setCellValue(deviceBasic.getFaultNo());
			row.createCell(17).setCellValue(deviceBasic.getRemark());
		}
		FileOutputStream fileOut = null;
		try {
			if (string == null || string.trim().equals("")) {
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/maintenance.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/maintenance.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(string.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/maintenance.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/maintenance.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	// *********************************************************************************************************

	/**
	 * 导出设备正常运转率Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportEquipmentOperation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportEquipmentOperation(HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "设备型号", "设备序列号", "机器编码", "运转时间(hr)", "停机时间(hr)", "设备正常运转率(%)" };
		Map<String, Object> json = new HashMap<>();
		String string = request.getParameter("picture");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/operation.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		Double workTime = 0.0;
		// 循环将数据写入Excel
		List<Device> exportDeviceKpi = (List<Device>) export.get("exportDeviceKpi" + request.getParameter("username"));
		for (Device device : exportDeviceKpi) {
			if (workTime == 0.0) {
				if (device.getDownTime() == 0) {
					workTime = device.getWorkTime();
				}
			}
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(device.getCustArea());
			row.createCell(2).setCellValue(device.getServiceArea());
			row.createCell(3).setCellValue(device.getUnitType());
			row.createCell(4).setCellValue(device.getEsNumber());
			row.createCell(5).setCellValue(device.getMachCode());
			row.createCell(6).setCellValue(device.getWorkTime() == null ? workTime + "" : device.getWorkTime() + "");
			row.createCell(7).setCellValue(device.getDownTime() == null ? "0.0" : device.getDownTime() + "");
			row.createCell(8).setCellValue(device.getDownTime() == null ? "100%" : device.getOperationRate());
		}
		FileOutputStream fileOut = null;
		try {
			if (string == null || string.trim().equals("")) {
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/operation.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/operation.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(string.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/operation.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/operation.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	// ****************************************************************************************************************

	/**
	 * 导出保养状态报表Excel
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportMaintenanceReport", produces = "application/json; charset=utf-8")
	public void exportMaintenanceReport(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String tableName = "保养报表状态";
		// 设置表头
		String[] Titles = { "客户名称", "设备数量", "计划保养数量", "实际完成", "未完成", "完成率", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		List<MaintenanceService> exportPerforms = (List<MaintenanceService>) export
				.get("exportPerforms" + request.getParameter("username"));
		for (MaintenanceService maintenance : exportPerforms) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(maintenance.getCustName());
			row.createCell(2).setCellValue(maintenance.getDeviceNumber());
			row.createCell(3).setCellValue(maintenance.getPlanDeviceNumber());
			row.createCell(4).setCellValue(maintenance.getReallyOverDeviceNumber());
			row.createCell(5).setCellValue(maintenance.getNotOverDeviceNumber());
			row.createCell(6).setCellValue(maintenance.getMaintenanceRate());
			row.createCell(7).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// ******************************************************************************************************************
	/**
	 * 导出保养汇总Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportMaintenanceSummary", produces = "application/json; charset=utf-8")
	public void exportMaintenanceSummary(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String tableName = "保养完成报表";
		// 设置表头
		String[] Titles = { "客户名称", "机器编码", "设备类型", "保养周期", "保养状态", "上次保养时间", "黑白读数", "彩色读数", "覆盖率", "耗材余量", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		List<Maintenance> maintenancePerforms = (List<Maintenance>) export
				.get("maintenancePerforms" + request.getParameter("username"));
		// 循环将数据写入Excel
		for (Maintenance maintenanceSummary : maintenancePerforms) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(maintenanceSummary.getCustName());
			row.createCell(2).setCellValue(maintenanceSummary.getMachCode());
			row.createCell(3).setCellValue(maintenanceSummary.getDevice().getDeviceType());
			row.createCell(4).setCellValue(maintenanceSummary.getMainFrequency());
			row.createCell(5).setCellValue(maintenanceSummary.getMaintenStatus());
			row.createCell(6).setCellValue(maintenanceSummary.getLastTime() == null ? null
					: ExcelUtils.fmtOne.format(maintenanceSummary.getLastTime()));
			row.createCell(7).setCellValue(maintenanceSummary.getDevice().getBwReader());
			row.createCell(8).setCellValue(maintenanceSummary.getDevice().getColorReader());
			row.createCell(9).setCellValue(maintenanceSummary.getCoverage());
			row.createCell(10).setCellValue(maintenanceSummary.getMaterialModel());
			row.createCell(11).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// ************************************************************************************************************************************
	/**
	 * 导出设备管理报表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportEquipmentManagement", produces = "application/json; charset=utf-8")
	public void exportEquipmentManagement(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String tableName = "设备管理报表";
		// 设置表头
		String[] Titles = { "机器编码", "设备归属", "固定资产编码", "合同编号", "客户名称", "装机日期", "品牌", "设备类型", "设备型号规格", "设备序列号",
				"设备已使用年数", "设备使用状态", "是否保密设备", "密级", "设备存放位置", "IP地址", "资产管理人", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		Date date = new Date();
		// 循环将数据写入Excel
		List<Device> exportDevicesKpi = (List<Device>) export
				.get("exportDevicesKpi" + request.getParameter("username"));
		for (Device device : exportDevicesKpi) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(device.getMachCode());
			row.createCell(2).setCellValue(device.getAssetAttr());
			row.createCell(3).setCellValue(device.getAssetNumber());
			row.createCell(4).setCellValue(device.getContractNo());
			row.createCell(5).setCellValue(device.getCustArea());
			row.createCell(6).setCellValue(
					device.getInstalledTime() == null ? null : ExcelUtils.fmt.format(device.getInstalledTime()));
			row.createCell(7).setCellValue(device.getDeviceBrand());
			row.createCell(8).setCellValue(device.getDeviceType());
			row.createCell(9).setCellValue(device.getUnitType());
			row.createCell(10).setCellValue(device.getEsNumber());
			if (device.getInstalledTime() == null) {
				row.createCell(11).setCellValue("NA");
			} else {
				row.createCell(11).setCellValue(SOMUtils.getIntOne(Double.valueOf(
						DurationFormatUtils.formatPeriod(device.getInstalledTime().getTime(), date.getTime(), "d"))
						/ 365));
			}
			row.createCell(12).setCellValue(device.getAssetStatus());
			row.createCell(13).setCellValue(device.getSecret());
			row.createCell(14).setCellValue(device.getSecretLevel());
			row.createCell(15).setCellValue(device.getLocation());
			row.createCell(16).setCellValue(device.getIP());
			row.createCell(17).setCellValue(device.getHoldMan());
			row.createCell(18).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// ***************************************************************************************************************************************
	/**
	 * 导出运维合同报表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportOperationalContract", produces = "application/json; charset=utf-8")
	public void exportOperationalContract(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String tableName = "运维合同报表";
		// 保存数据
		Date date = new Date();
		for (Contract contract : exportContractKpi) {
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
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "合同编码", "合同性质", "合同类型", "登记时间", "经办部门", "经办人", "合同保管人", "签约日期", "到期日期",
				"合同期限(月)", "离到期天数" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Contract contract : exportContractKpi) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(contract.getCustName());
			row.createCell(2).setCellValue(contract.getMainService());
			row.createCell(3).setCellValue(contract.getContractNo());
			row.createCell(4).setCellValue(contract.getContractNature());
			row.createCell(5).setCellValue(contract.getContractType());
			row.createCell(6)
					.setCellValue(contract.getRegTime() == null ? null : ExcelUtils.fmt.format(contract.getRegTime()));
			row.createCell(7).setCellValue("");
			row.createCell(8).setCellValue(contract.getAgent());
			row.createCell(9).setCellValue(contract.getContractHoldman());
			row.createCell(10).setCellValue(
					contract.getStartDate() == null ? null : ExcelUtils.fmt.format(contract.getStartDate()));
			row.createCell(11)
					.setCellValue(contract.getEndDate() == null ? null : ExcelUtils.fmt.format(contract.getEndDate()));
			row.createCell(12).setCellValue(contract.getContractDeadline());
			row.createCell(13).setCellValue(contract.getDueDays());
			row.createCell(14).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	/**
	 * 分子公司工单统计
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderStatistics", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderStatistics() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		String identifier = null;
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (serviceArea == null || serviceArea.trim().equals("")) {
			serviceArea = "";
		}
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			identifier = "1";
			user.setCustName(serviceArea);
		}
		String enginnerName = null;
		List<ServiceInfo> service = serviceInfoService.selectServiceInfByWoStatus(
				SOMUtils.orderNumToComp(user.getCustName()), null, startDate, endDate, null, null, null, identifier);
		List<String> enginnerNames = new ArrayList<>();
		for (ServiceInfo serviceInfo : service) {
			if (serviceInfo.getStaffId() == null) {
				continue;
			}
			enginnerName = serviceInfo.getStaffId();
			if (!enginnerNames.contains(enginnerName)) {
				enginnerNames.add(enginnerName);
			}
		}
		int woNumber1 = 0; // 事故类工单数量
		int woNumber2 = 0; // 需求类工单数量
		int woNumber3 = 0; // 事件类工单数量
		int totalTurnOrderNumber = 0; // 转单数量
		int totalNumber = 0; // 所有工单数量
		List<OrderStatistics> orderStatistics = new ArrayList<>();
		for (String string : enginnerNames) {
			int engineerWoNumber1 = 0; // 工程师事故类工单数量
			int engineerWoNumber2 = 0; // 工程师需求类工单数量
			int engineerWoNumber3 = 0; // 工程师事件类工单数量
			int enginnerTotalNumber = 0; // 工程师所有工单数量
			int turnOrderNumber = 0; // 工程师转单数量
			for (ServiceInfo serviceInfo : service) {
				if (serviceInfo.getStaffId() == null) {
					continue;
				}
				if (serviceInfo.getStaffId().equals(string)) {
					totalNumber++;
					enginnerTotalNumber++;
					if (serviceInfo.getOrderInfo().getFaultType().equals("事故类")) {
						woNumber1++;
						engineerWoNumber1++;
					} else if (serviceInfo.getOrderInfo().getFaultType().equals("需求类")) {
						woNumber2++;
						engineerWoNumber2++;
					} else if (serviceInfo.getOrderInfo().getFaultType().equals("事件类")) {
						woNumber3++;
						engineerWoNumber3++;
					}
					if (serviceInfo.getOrderInfo().getWoStatus().equals("已转单")) {
						turnOrderNumber++;
						totalTurnOrderNumber++;
					}
				}
			}
			OrderStatistics orderStatistic = new OrderStatistics(
					staffInfoServiceImplnew.selectStaffByNum(string).getName(), engineerWoNumber1, engineerWoNumber2,
					engineerWoNumber3, enginnerTotalNumber);
			orderStatistic.setTurnOrderNumber(turnOrderNumber);
			orderStatistics.add(orderStatistic);
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, orderStatistics, page, limit);
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
		json.put("msg", "分子公司月度工单统计表");
		json.put("事故类合计", woNumber1);
		json.put("需求类合计", woNumber2);
		json.put("事件类合计", woNumber3);
		json.put("转单合计", totalTurnOrderNumber);
		json.put("所有工单合计", totalNumber);
		json.put("count", orderStatistics.size());
		json.put("data",
				page == null || orderStatistics == null ? orderStatistics : orderStatistics.subList(page, limit));
		return json;
	}

	// ****************************************************************************************************************************

	/**
	 * 工程师工单时效Kpi报表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderPrescription", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderPrescription() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String staffName = request.getParameter("staffName");
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = { "事故类" };
		if (startDate != null && !"".equals(startDate.trim())) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate.trim())) {
			endDate = endDate + " 23:59:59";
		}
		Map<String, Object> json = new HashMap<>();
		// 如果查询条件没有时间，则默认查询当月的
		if (("".equals(startDate) && "".equals(endDate)) || (startDate == null && endDate == null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date());
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00");
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		String identifier = null; // 标识符
		if (serviceArea == null || serviceArea.trim().equals("")) {
			json.put("code2", 1);
			serviceArea = "";
		}
		// 根据情不同角色查找相应的完成工单
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("行业客户部")
				|| user.getCustName().equals("系统推进部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		List<ServiceInfo> engineerKpiServiceInfos = serviceInfoService.selectServiceInfoByengineerKpi(staffName, null,
				SOMUtils.orderNumToComp(user.getCustName()), startDate, endDate, faultType, null, null, identifier);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		OrderInfo order = new OrderInfo();
		Device device = new Device();
		StaffInfo staff = new StaffInfo();
		for (ServiceInfo serviceInfo : engineerKpiServiceInfos) {
			EngineerKpi engineerKpi = new EngineerKpi();
			order = serviceInfo.getOrderInfo();
			device = serviceInfo.getDevice();
			staff = serviceInfo.getStaffInfo();
			if (staff == null) {
				continue;
			}
			serviceInfo.setOrderInfo(order);
			serviceInfo.setDevice(device);
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setCustName(order.getCustName());
			engineerKpi.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			engineerKpi.setRepairType(order.getRepairType());
			engineerKpi.setDevName(device.getDevName());
			engineerKpi.setMachCode(device.getMachCode());
			engineerKpi.setFaultType(order.getFaultType());
			engineerKpi.setAccidentType(order.getAccidentType());
			engineerKpi.setRepairTime(order.getRepairTime()); // 报修时间
			engineerKpi.setSendTime(order.getSendTime() == null ? null : order.getSendTime()); // 派单时间
			engineerKpi.setTelRepon(serviceInfo.getTelRepon() == null ? null : serviceInfo.getTelRepon()); // 响应时间
			engineerKpi.setArrTime(serviceInfo.getArrTime() == null ? null : serviceInfo.getArrTime()); // 到达时间
			engineerKpi.setProbSolve(serviceInfo.getProbSolve() == null ? null : serviceInfo.getProbSolve()); // 完成时间
			engineerKpi.setSendTimeSlot(order.getSendTime() == null ? 0
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), order.getSendTime(), workTime,
							offWorkTime))); // 客服派单用时
			engineerKpi.setResponseTime(serviceInfo.getTelRepon() == null ? 0
					: SOMUtils.getInt(CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getTelRepon(),
							workTime, offWorkTime))); // 工程师响应用时
			engineerKpi.setArrTimeSlot(serviceInfo.getArrTime() == null ? 0
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getArrTime(), workTime, offWorkTime)); // 工程师到达用时
			engineerKpi.setProbSolveSlot(serviceInfo.getProbSolve() == null ? 0
					: CalendarTool.getDownTime(order.getRepairTime(), serviceInfo.getProbSolve(), workTime,
							offWorkTime)); // 工程师解决用时
			if (engineerKpi.getResponseTime() != null && engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			if (engineerKpi.getArrTimeSlot() != null && engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			if (engineerKpi.getProbSolveSlot() != null
					&& engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			engineerKpi.setSecondService(order.getPartsTypeNumber() == null ? "否" : "是");
			engineerKpi.setStaffName(staff.getName()); // 责任工程师
			engineerKpi.setOrderTurnNum(order.getWoStatus().equals("已转单") ? 1 : 0);
			engineerKpi.setStaffName2(device.getReserveEnginner()); // 后备工程师
			engineerKpi.setWoStatus(order.getWoStatus());
			engineerKpi.setMaintenanceFeedback(order.getMaintenanceFeedback());
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			engineerKpis.add(engineerKpi);
		}
		List<StandardRate> StandardRates = new ArrayList<>();
		double avgOrderNumber = 0.0; // 人均单数
		double avgArrTime = 0.0; // 人均到达现场平均工时
		double avrProble = 0.0; // 人均解决问题平均工时
		for (StaffInfo staffInfo : staffInfoServiceImplnew.selectAllStaff()) {
			if (!(staffInfo.getPost().equals("工程师") || staffInfo.getPost().equals("技术主管"))) {
				continue;
			}
			Integer orderNum = 0;// 属于该工程师的工单数量
			double arrTimeAvg = 0.0; // 到达现场总共用时
			double probleAvg = 0.0; // 问题解决总共用时
			for (EngineerKpi engineerKpi : engineerKpis) {
				// 如果名字相等，则算出各种成功率
				if (staffInfo.getName().equals(engineerKpi.getStaffName())) {
					orderNum++;
					avgOrderNumber++;
					arrTimeAvg = arrTimeAvg + engineerKpi.getArrTimeSlot();
					probleAvg = probleAvg + engineerKpi.getProbSolveSlot();
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				StandardRate StandardRate = new StandardRate();
				StandardRate.setStaffName(staffInfo.getName()); // 工程师姓名
				StandardRate.setOrderNum(orderNum + "");
				avgArrTime = avgArrTime + (arrTimeAvg / 60) / orderNum;
				avrProble = avrProble + (probleAvg / 60) / orderNum;
				StandardRate.setArrTimeAvg(SOMUtils.getIntOne((arrTimeAvg / 60) / orderNum)); // 到达现场平均用时
				StandardRate.setProbTimeAvg(SOMUtils.getIntOne((probleAvg / 60) / orderNum)); // 解决问题平均用时
				// 保养完成率
				StandardRates.add(StandardRate);
			}
		}
		exportStandardRates2 = StandardRates;
		export.put("exportStandardRates2" + request.getParameter("username"), exportStandardRates2);
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, StandardRates, page, limit);
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
		json.put("msg", "工程师kpi数据");
		json.put("count", StandardRates.size());
		json.put("avgTime", StandardRates.size() <= 0 ? null : SOMUtils.getIntOne(avgArrTime / StandardRates.size()));
		json.put("proTime", StandardRates.size() <= 0 ? null : SOMUtils.getIntOne(avgArrTime / StandardRates.size()));
		json.put("avgOrderNumber",
				StandardRates.size() <= 0 ? null : SOMUtils.getIntOne(avgOrderNumber / StandardRates.size()));
		json.put("data", page == null || StandardRates == null ? StandardRates : StandardRates.subList(page, limit));
		orderPrescription.put("avgTime",
				StandardRates.size() <= 0 ? null : SOMUtils.getIntOne(avgArrTime / StandardRates.size()));
		orderPrescription.put("proTime",
				StandardRates.size() <= 0 ? null : SOMUtils.getIntOne(avgArrTime / StandardRates.size()));
		orderPrescription.put("avgOrderNumber",
				StandardRates.size() <= 0 ? null : SOMUtils.getIntOne(avgOrderNumber / StandardRates.size()));
		return json;
	}

	/**
	 * 导出工单时效表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportOrderPrescription", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportOrderPrescription(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 设置表头
		String[] Titles = { "工程师姓名", "维修单数", "到达现场平均用时", "问题解决平均用时" };
		Map<String, Object> json = new HashMap<>();
		String picture1 = request.getParameter("picture1");
		String picture2 = request.getParameter("picture2");
		String avgOrderNumber = request.getParameter("avgOrderNumber");
		String avgTime = request.getParameter("avgTime");
		String proTime = request.getParameter("proTime");
		XSSFWorkbook wb = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/orderPrescription.xlsx");
		wb.removeSheetAt(wb.getSheetIndex("Sheet1"));
		wb.removeSheetAt(wb.getSheetIndex("Sheet2"));
		wb.createSheet("Sheet1");
		wb.createSheet("Sheet2");
		XSSFSheet sheet1 = wb.getSheet("Sheet1");
		XSSFRow row = sheet1.createRow(0);
		row.createCell(0).setCellValue("序号");
		int i = 1;
		for (String title : Titles) {
			row.createCell(i).setCellValue(title);
			i++;
		}
		i = 1;
		row = sheet1.createRow(1);
		// 循环将数据写入Excel
		List<StandardRate> exportStandardRates2 = (List<StandardRate>) export
				.get("exportStandardRates2" + request.getParameter("username"));
		for (StandardRate stand : exportStandardRates2) {
			row = sheet1.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(stand.getStaffName());
			row.createCell(2).setCellValue(stand.getOrderNum());
			row.createCell(3).setCellValue(stand.getArrTimeAvg());
			row.createCell(4).setCellValue(stand.getProbTimeAvg());
		}
		row = sheet1.createRow(i);
		row.createCell(2).setCellValue(avgOrderNumber);
		row.createCell(3).setCellValue(avgTime);
		row.createCell(4).setCellValue(proTime);
		FileOutputStream fileOut = null;
		try {
			if (picture1 == null || picture1.trim().equals("")) {
				System.out.println("进入图片循环");
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/orderPrescription.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/orderPrescription.xlsx");
			} else {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(picture1.substring(22));
				byte[] c = decoder.decodeBuffer(picture2.substring(22));
				XSSFSheet sheet2 = wb.getSheet("Sheet2");
				/**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 */
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				XSSFClientAnchor anchor1 = new XSSFClientAnchor(0, 0, 0, 0, (short) 11, 1, (short) 20, 15);
				XSSFDrawing patriarch = sheet2.createDrawingPatriarch();
				patriarch.createPicture(anchor, wb.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				patriarch.createPicture(anchor1, wb.addPicture(c, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
				fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/orderPrescription.xlsx"); // 写入excel文件
				wb.write(fileOut);
				json.put("code", 0);
				json.put("msg", SOMUtils.ipAndPort + "kpi/orderPrescription.xlsx");
			}
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

}
