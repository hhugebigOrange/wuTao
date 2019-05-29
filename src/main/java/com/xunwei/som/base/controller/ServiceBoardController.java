package com.xunwei.som.base.controller;

import com.xunwei.som.base.model.Machine;
import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.CustomerFeedback;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.CustomerSatisfaction;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.CustomerFeedbackService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerFeedbackServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.MachineServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 服务看板
 * 
 * @author Administrator
 *
 */

@Controller
public class ServiceBoardController extends BaseController {

	private MachineServiceImpl machineServiceImpl = new MachineServiceImpl();

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();

	private UserService userService = new UserServiceImpl();

	private CustomerFeedbackService customerFeedbackService = new CustomerFeedbackServiceImpl();

	private List<OrderInfo> orderss;

	private List<Device> devicess;

	private List<Contract> contractss;

	private List<StaffInfo> staffss;

	private List<CustomerSatisfaction> customerSatisfactionss;

	// 用来存放每次查询的客户满意度结果集
	private List<ServiceInfo> serviceInfos;

	// 工单概况↓——————————————————————————————————————————————————————————————————————————————————————————————————

	/**
	 * 匹配工单概况页面，默认查询当天的工单
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/orderSituation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderSituation(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String workState = request.getParameter("workState");
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		String c = request.getParameter("c");
		String f = "";
		if (workState == null || workState.equals("")) {
			workState = null;
		}
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
		String conver = request.getParameter("conver");
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 判断是用户还是公司账号
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		List<OrderInfo> orders = serviceManageServiceImpl.selectOrderByOrder(custName,
				serviceArea == null ? null : SOMUtils.orderNumToComp(serviceArea), startDate, endDate, workState,
				faultType, conver, null, null);
		for (OrderInfo OrderInfo : orders) {
			OrderInfo.setServiceArea(SOMUtils.CompToOrderNumTo(OrderInfo.getWoNumber().substring(0, 2)));
			ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(OrderInfo.getWoNumber());
			if (service.getStaffId() != null) {
				OrderInfo.setEnginner(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()).getName());
			}
			if (OrderInfo.getMachCode() != null) {
				OrderInfo.setUnitType(customerManage.selectByCode(OrderInfo.getMachCode()).getUnitType());
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, orders, page, limit);
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
		orderss = orders;
		int noOver = 0;
		int complete = 0;
		for (OrderInfo order : orders) {
			if (order.getWoStatus().equals("已处理") || order.getWoStatus().equals("已关单")) {
				complete++;
			} else {
				noOver++;
			}
		}
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "工单概况数据");
		json.put("count", orders.size());
		json.put("count2", noOver);
		json.put("count3", complete);
		json.put("data", page == null || orders == null ? orders : orders.subList(page, limit));
		return json;
	}

	/**
	 * 方法：导出工单概况excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportOrderSituation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportOrderSituation(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "工单概况表";
		// 设置表头
		String[] Titles = { "客户名称", "工单号码", "客户地址", "报修人姓名", "联系电话", "设备名称", "机器编码", "设备序列号", "故障类型", "处理状态", "处理措施",
				"备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (OrderInfo order : orderss) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(order.getCustName());
			row.createCell(2).setCellValue(order.getWoNumber());
			row.createCell(3).setCellValue(order.getCustAddr());
			row.createCell(4).setCellValue(order.getRepairMan());
			row.createCell(5).setCellValue(order.getRepairService());
			row.createCell(6).setCellValue(order.getDevName());
			row.createCell(7).setCellValue(order.getMachCode());
			row.createCell(8).setCellValue(order.getEsNumber());
			row.createCell(9).setCellValue(order.getFaultType());
			row.createCell(10).setCellValue(order.getWoStatus());
			row.createCell(11).setCellValue(order.getTreatmentMeasure());
			row.createCell(12).setCellValue(order.getRemark());
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	// 设备概况↓——————————————————————————————————————————————————————————————————————————————————————————————————

	/**
	 * 匹配设备概况页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/equipmentSituation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> equipmentSituation(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端获取参数
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		String unitType = request.getParameter("unitType");
		String assetAttr = request.getParameter("assetAttr");
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 判断是用户还是公司账号
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		// 用来存放每次查询的设备结果集
		List<Device> devices = customerManage.selectByDevice(custName, serviceArea, unitType, assetAttr, null, null);
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
		session.setAttribute("devices", devices);
		devicess = devices;
		json.put("code", 0);
		json.put("msg", "设备概况数据");
		json.put("count", devices.size());
		json.put("data", page == null || devices == null ? devices : devices.subList(page, limit));
		return json;
	}

	/**
	 * 方法：导出设备概况信息excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportSituationEquipment", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportSituationEquipment(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String tableName = "设备概况表";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "设备名称", "机器编码", "设备型号", "设备位置", "厂家", "安装日期", "服务年限", "设备归属", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Device dev : devicess) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(dev.getCustArea());
			row.createCell(2).setCellValue(dev.getServiceArea());
			row.createCell(3).setCellValue(dev.getDevName());
			row.createCell(4).setCellValue(dev.getMachCode());
			row.createCell(5).setCellValue(dev.getUnitType());
			row.createCell(6).setCellValue(dev.getLocation());
			row.createCell(7).setCellValue(dev.getDeviceBrand());
			row.createCell(8)
					.setCellValue(dev.getInstalledTime() == null ? "" : ExcelUtils.fmt.format(dev.getInstalledTime()));
			row.createCell(9).setCellValue("");
			row.createCell(10).setCellValue(dev.getAssetAttr());
			row.createCell(11).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	// 合同概况↓——————————————————————————————————————————————————————————————————————————————————————————————————

	/**
	 * 匹配合同概况页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/contractSituation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> contractSituation(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		Date date = new Date();
		// 从前端获取参数
		String childService = request.getParameter("childService");
		String custName = null;
		String[] params = new String[4];
		// 判断是属于客户还是子公司，若两者都不是，则为本部
		if (request.getSession().getAttribute("custName") != null) {
			params[0] = (String) request.getSession().getAttribute("custName");
		} else {
			params[0] = "";
		}
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 判断是用户还是公司账号
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			childService = (String) SOMUtils.getCompName(request).get("compname");
		}
		// 用来存放每次查询的合同结果集
		List<Contract> contracts = customerManage.selectByCust(custName, childService, null, null, null, null,null,null,null,null);
		// 查找到期合同
		List<Contract> timeContracts = customerManage.selectByCust(custName, childService, "1", "", null, null,null,null,null,null);
		// 查找一年内到期合同
		List<Contract> dueToContracts = customerManage.selectByCust(custName, childService, "", "1", null, null,null,null,null,null);
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
		// 查询相应的合同
		session.setAttribute("contracts", contracts);
		contractss = contracts;
		json.put("code", 0);
		json.put("msg", "合同概况数据");
		json.put("count", contracts == null ? 0 : contracts.size());
		json.put("count2", dueToContracts == null ? 0 : dueToContracts.size());
		json.put("count3", timeContracts == null ? 0 : timeContracts.size());
		json.put("count4", 0);
		json.put("data", page == null || contracts == null ? contracts : contracts.subList(page, limit));
		return json;
	}

	/**
	 * 方法：导出合同概况信息excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportContractSituation", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportContractSituation(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String tableName = "合同概况表";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "合同名称", "合同编码", "合同类型", "合同性质", "登记时间", "经办部门", "经办人", "合同保管人", "签约日期",
				"到期日期", "离到期天数", "期限(月)", "合同约定到期提醒天数", "合同到期预警", "外包所服务客户", "提前终止日", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Contract contract : contractss) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(contract.getCustName());
			row.createCell(2).setCellValue(contract.getMainService());
			row.createCell(3).setCellValue("");
			row.createCell(4).setCellValue(contract.getContractNo());
			row.createCell(5).setCellValue(contract.getContractType());
			row.createCell(6).setCellValue("");
			row.createCell(7)
					.setCellValue(contract.getRegTime() == null ? "" : ExcelUtils.fmt.format(contract.getRegTime()));
			row.createCell(8).setCellValue(ExcelUtils.fmt.format(contract.getEndDate()));
			row.createCell(9).setCellValue(contract.getDueDays());
			row.createCell(10).setCellValue(contract.getContractDeadline());
			row.createCell(11).setCellValue(contract.getDueDays());
			row.createCell(12).setCellValue(contract.getSigningCompany());
			row.createCell(13).setCellValue("");
			row.createCell(14).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	// 在岗人数概况↓——————————————————————————————————————————————————————————————————————————————————————————————————

	/**
	 * 匹配在岗人数页面，默认查询
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/onTheJob", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> onTheJob(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 存放各部门人数
		Integer p0, p1, p2, p3, p4, p5;
		int[] persons = new int[6];
		p0 = 0;
		p1 = 0;
		p2 = 0;
		p3 = 0;
		p4 = 0;
		p5 = 0;
		String serviceArea = request.getParameter("serviceArea");
		String role = request.getParameter("post");
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 判断是用户还是公司账号
		if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		// 根据条件找出所有
		// 用来存放每次查询的岗位结果集
		List<StaffInfo> staffs = staffInfoServiceImplnew.getStaffByDynamic("", serviceArea, role, "", null, null, null,null);
		// 根据传入的参数，设定page和Limit的值
		Integer[] para = SOMUtils.pageAndLimit(request, staffs, page, limit);
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
		for (StaffInfo staff : staffs) {
			if (staff.getPost().equals("运维经理")) {
				persons[0] = ++p0;
			}
			if (staff.getPost().equals("客服主管")) {
				persons[1] = ++p1;
			}
			if (staff.getPost().equals("技术主管")) {
				persons[2] = ++p2;
			}
			if (staff.getPost().equals("工程师")) {
				persons[3] = ++p3;
			}
			if (staff.getPost().equals("驻现场人员")) {
				persons[4] = ++p4;
			}
			if (staff.getPost().equals("客服助理")) {
				persons[5] = ++p5;
			}
		}
		session.setAttribute("staffs", staffs);
		staffss = staffs;
		json.put("code", 0);
		json.put("msg", "在岗人数数据");
		json.put("count", staffs.size());
		json.put("data", staffs == null || page == null ? staffs : staffs.subList(page, limit));
		json.put("count2", persons[5]);
		json.put("count3", persons[3]);
		json.put("count4", persons[1]);
		json.put("count5", persons[2]);
		json.put("count6", persons[0]);
		json.put("count7", persons[4]);
		return json;
	}

	/**
	 * 导出在岗员工概况表EXCEL
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportOnTheJob", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportOnTheJob(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String tableName = "员工概况表";
		// 设置表头
		String[] Titles = { "服务区域", "姓名", "联系电话", "岗位", "工作状态", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (StaffInfo staff : staffss) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(staff.getCompName());
			row.createCell(2).setCellValue(staff.getName());
			row.createCell(3).setCellValue(staff.getPhone());
			row.createCell(4).setCellValue(staff.getPost());
			row.createCell(5).setCellValue(staff.getWorkCond());
			row.createCell(6).setCellValue(staff.getRemark());
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	// 客户满意度概况↓——————————————————————————————————————————————————————————————————————————————————————————————————

	/**
	 * 匹配客户满意度页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/customerSatisfaction", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerSatisfaction(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String custSat = request.getParameter("custSat");
		String three = request.getParameter("three");
		String five = request.getParameter("five");
		String repairType = request.getParameter("repairType");
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		// 判断是否登录
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 判断是用户还是公司账号
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		List<CustomerSatisfaction> CustomerSatisfactions = new ArrayList<>();
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfByDynamic(
				serviceArea == null ? null : SOMUtils.orderNumToComp(serviceArea), custName, startDate, endDate,
				custSat, "", "", three, five, repairType, null, null, null,null,null,null);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			if (service.getProbSolve() == null) {
				continue;
			}
			CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			CustomerSatisfaction.setCustName(service.getOrderInfo().getCustName());
			CustomerSatisfaction.setServiceArea(SOMUtils.CompToOrderNumTo(service.getWoNumber().substring(0, 2)));
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
		customerSatisfactionss = CustomerSatisfactions;
		// 返回结果集
		json.put("code", 0);
		json.put("msg", "客户满意度数据");
		json.put("count", CustomerSatisfactions.size());
		json.put("data", page == null || CustomerSatisfactions == null ? CustomerSatisfactions
				: CustomerSatisfactions.subList(page, limit));
		return json;
	}

	/**
	 * 导出客户满意度Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportCustSatisfaction", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportCustSatisfaction(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String tableName = "客户满意度概况表";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "工单号码", "报修人姓名", "联系电话", "报修时间", "完成时间", "维修工程师", "实际得分", "投诉与建议内容", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (CustomerSatisfaction serviceInfo : customerSatisfactionss) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(serviceInfo.getCustName());
			row.createCell(2).setCellValue(serviceInfo.getServiceArea());
			row.createCell(3).setCellValue(serviceInfo.getWoNumber());
			row.createCell(4).setCellValue(serviceInfo.getRepairMan());
			row.createCell(5).setCellValue(serviceInfo.getRepairPhone());
			row.createCell(6).setCellValue(ExcelUtils.fmtOne.format(serviceInfo.getRepairTime()));
			row.createCell(7).setCellValue(serviceInfo.getCompleteTime() == null ? ""
					: ExcelUtils.fmtOne.format(serviceInfo.getCompleteTime()));
			row.createCell(8).setCellValue(serviceInfo.getEnginnerName());
			if (serviceInfo.getCustScore() == null) {
				row.createCell(9).setCellValue("");
				row.createCell(10).setCellValue("");
			} else {
				row.createCell(9).setCellValue(serviceInfo.getCustScore());
				row.createCell(10).setCellValue(serviceInfo.getCustEva());
			}
			row.createCell(11).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * ***********************************************************************************************
	 */

	/**
	 * 搜索名字
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = { "/SearchWorkList" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object Login(ModelAndView modelAndView) {
		String viewName = "redirect:/serviceboard/html/serviceboard";
		String customerName = this.request.getParameter("customerName");
		if ((SOMUtils.isNull(customerName)) || (SOMUtils.isNull(customerName))) {
			modelAndView.setViewName(viewName);
			return modelAndView;
		}
		try {
			List<Machine> Machines = this.machineServiceImpl.selectMachineByDynamic(customerName);
			for (Machine machine : Machines) {
				System.out.println(machine);
			}
			return Machines;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}

	/**
	 * 匹配浮动窗页面，并查询出所有待处理的工单
	 * 
	 * @return
	 */
	@RequestMapping("/floatingWindow")
	public ModelAndView goToFloatingWindow(ModelAndView modelAndView) {
		List<OrderInfo> orderInfos = serviceManageServiceImpl.getOrderByProcessed("未处理");
		modelAndView.addObject("orderNumber", orderInfos.size());
		modelAndView.addObject("orderInfos", orderInfos);
		modelAndView.setViewName("/serviceboard/html/floatingWindow");
		return modelAndView;
	}

	/**
	 * 派单方法：派单后将工单的状态改为处理中
	 * 
	 * @param modelAndView
	 * @param request
	 * @param model
	 * @return
	 */
	/*
	 * @RequestMapping("/sendOrder") public ModelAndView sendOrder(ModelAndView
	 * modelAndView, HttpServletRequest request) { String woNumber =
	 * request.getParameter("woNumber");
	 * serviceManageServiceImpl.updateWoStatus(woNumber, "处理中");
	 * modelAndView.setViewName("redirect:/floatingWindow"); return
	 * modelAndView; }
	 */

	/**
	 * 匹配服务需求页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/business")
	public ModelAndView business(ModelAndView modelAndView) {
		modelAndView.setViewName("/serviceboard/html/business");
		return modelAndView;
	}

	/**
	 * 匹配故障报修页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/faultRepair")
	public ModelAndView faultRepair(ModelAndView modelAndView) {
		modelAndView.setViewName("/serviceboard/html/faultRepair");
		return modelAndView;
	}

	/**
	 * 匹配反馈与建议页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/advice")
	public ModelAndView advice(ModelAndView modelAndView) {
		modelAndView.setViewName("/serviceboard/html/advice");
		return modelAndView;
	}

	/**
	 * 接口:新增反馈与建议
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/doAdvice", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAdvice(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String id = request.getParameter("username");// 登陆用户名
		// 判断该手机号是和openId是否注册过
		String custName = "";
		String custPhone="";
		for (User user : userService.selectAllUser()) {
			if (user.getUserId().equals(id)) {
				custName = user.getCustName();
				id=user.getUserName();
				custPhone=user.getUserId();
			}
		}
		if(custName.equals("")){
			json.put("code", 1);
			json.put("msg", "对不起，只有客户有权限进行反馈");
			return json;
		}
		String content = request.getParameter("content");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("反馈与建议内容", content);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		Date now = new Date();
		CustomerFeedback customerFeedback = new CustomerFeedback(custName, id, custPhone, now, content);
		int result = customerFeedbackService.insertSelective(customerFeedback);
		if (result > 0) {
			json.put("code", 0);
			json.put("data", result);
			json.put("msg", "反馈成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("data", result);
			json.put("msg", "反馈失败");
			return json;
		}
	}


	/**
	 * 方法:查看所有的反馈与建议
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/selectAllAdvice", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectAllAdvice(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		List<CustomerFeedback> CustomerFeedbacks = customerFeedbackService.selectAllFeedback();
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, CustomerFeedbacks, page, limit);
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
		json.put("msg", "客户反馈与建议");
		json.put("count", CustomerFeedbacks.size());
		json.put("data",
				page == null || CustomerFeedbacks == null ? CustomerFeedbacks : CustomerFeedbacks.subList(page, limit));
		return json;
	}
}
