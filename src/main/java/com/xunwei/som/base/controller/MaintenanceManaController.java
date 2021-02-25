package com.xunwei.som.base.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunwei.som.service.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.MaintenancePerform;
import com.xunwei.som.pojo.Maintenance;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.MaintenanceContract;
import com.xunwei.som.pojo.front.MaintenanceEnginner;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

/**
 * 保养管理模块
 * 
 * @author Administrator
 *
 */
@Controller
public class MaintenanceManaController extends BaseController {

	@Autowired
	private Maintenanceservice maintenanceservice;

	@Autowired
	private CustomerManageService customerManageService;

	@Autowired
	private StaffInfoService staffInfoService;

	@Autowired
	private UserService userService;

	// 存放查询出来的保养报表的结果
	private List<Maintenance> maintenances;

	// 用于保存每个用户的查询记录
	private Map<String, Object> export = new HashMap<>();

	// 存放查询出来的保养执行的结果
	private List<Maintenance> maintenancePerforms = new ArrayList<>();

	/**
	 * 匹配保养执行页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/maintenancePerformer", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> maintenancePerformer(ModelAndView modelAndView) {
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
		if (serviceArea == null || serviceArea.equals("")) {
			serviceArea = "";
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		// 判断是用户还是公司账号
		if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
			user.setCustName("");
		} else if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (userRole.getRoleId().equals("优质运维专员") || userRole.getRoleId().equals("运维管理人员")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		// 先查出相应的保养设备
		maintenancePerforms = maintenanceservice.selectmaintenance(custName, user.getCustName(), enginnerName, "",
				"", "", null, null, identifier);
		List<Maintenance> maintenances = maintenanceservice.selectmaintenance(custName, user.getCustName(),
				enginnerName, "", "", "", page, limit, identifier);
		// 遍历保养执行
		for (Maintenance maintenance : maintenances) {
			maintenance.setMaintenStatus(maintenance.getMaintenanceState() == 0 ? "未完成" : "已完成");
		}
		export.put(request.getParameter("username") + "maintenancePerformer", maintenancePerforms);
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("count", maintenancePerforms.size());
		json.put("data", maintenances);
		return json;
	}

	/**
	 * 定时方法，每5分钟刷新保养执行的状态
	 */
	@Scheduled(fixedRate = 1000 * 60 * 5)
	public void autoRefreshMaintenancePerform() {
		List<Maintenance> maintenances = maintenanceservice.selectmaintenance(null, null, null, null, null, null,
				null, null, null);
		Calendar currentTime = Calendar.getInstance();// 当前时间的年月日：时分秒
		currentTime.setTime(new Date());
		Calendar maintenanceTime = Calendar.getInstance();// 每个保养计划里面的时间
		for (Maintenance maintenance : maintenances) {
			// 如果周期是月
			if (maintenance.getMainFrequency().equals("月")) {
				// 如果最近维修时间没有
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是在当月，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (maintenanceTime.get(Calendar.MONTH) == currentTime.get(Calendar.MONTH)) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
			// 如果周期是月
			if (maintenance.getMainFrequency().equals("双月")) {
				// 如果最近维修时间没有
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是超过两个月，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (currentTime.get(Calendar.MONTH)-maintenanceTime.get(Calendar.MONTH)<3) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
			// 如果周期是季度
			if (maintenance.getMainFrequency().equals("季度")) {
				// 如果最近维修时间没有
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是在季度，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (SOMUtils.isWhichQuarter(maintenanceTime.get(Calendar.MONTH) + 1) == SOMUtils
						.isWhichQuarter(currentTime.get(Calendar.MONTH) + 1)) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
			// 如果周期是年
			if (maintenance.getMainFrequency().equals("年")) {
				// 如果最近维修的时间为空
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是在季度，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (maintenanceTime.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR)) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
			// 如果周期是年
			if (maintenance.getMainFrequency().equals("半年")) {
				// 如果最近维修的时间为空
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是在季度，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (currentTime.get(Calendar.DAY_OF_YEAR) - maintenanceTime.get(Calendar.DAY_OF_YEAR) <= 183) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
			// 如果周期是周
			if (maintenance.getMainFrequency().equals("周")) {
				// 如果最近维修的时间为空
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是在季度，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (maintenanceTime.get(Calendar.WEEK_OF_YEAR) == currentTime.get(Calendar.WEEK_OF_YEAR)) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
			// 如果周期是日
			if (maintenance.getMainFrequency().equals("日")) {
				// 如果最近维修的时间为空
				if (maintenance.getLastTime() == null) {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
				maintenanceTime.setTime(maintenance.getLastTime());
				// 判断最近维修的时间是否是在季度，如果不是，则添加进当前的集合中，顺便修改保养状态
				if (maintenanceTime.get(Calendar.DAY_OF_YEAR) == currentTime.get(Calendar.DAY_OF_YEAR)) {
					maintenance.setMaintenanceState(1);
					maintenanceservice.updateMaintenance(maintenance);
					continue;
				} else {
					maintenanceservice.updateMaintenance(maintenance.getContractCode(), maintenance.getMachCode(),
							0, "");
					continue;
				}
			}
		}
	}

	/**
	 * 匹配保养计划页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/maintenancePlan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> maintenancePlan(ModelAndView modelAndView) {
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 分页
		String identifier = null;
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
		if (serviceArea == null || serviceArea.equals("")) {
			serviceArea = "";
		}
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		// 判断是用户还是公司账号
		if (userRole.getRoleId().equals("客户")) {
			custName = user.getCustName();
			user.setCustName("");
		} else if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (userRole.getRoleId().equals("优质运维专员") || userRole.getRoleId().equals("运维管理人员")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		maintenances = maintenanceservice.selectmaintenance(custName, user.getCustName(), "", "", "", "", null,
				null, identifier);
		List<Maintenance> maintenance = maintenanceservice.selectmaintenance(custName, user.getCustName(), "", "",
				"", "", page, limit, identifier);
		// 分页
		export.put(request.getParameter("username") + "maintenancePlan", maintenances);
		json.put("code", 0);
		json.put("msg", "保养计划数据");
		json.put("count", maintenances.size());
		json.put("data", maintenance);
		return json;
	}

	/**
	 * 根据机器编码返回相应数据
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/maintenancePlanByMachCode", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> maintenancePlanByMachCode(ModelAndView modelAndView) {
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 前端获取参数
		String machCode = request.getParameter("machCode");
		Device device = customerManageService.selectByCode(machCode);
		if (device == null || device.equals("")) {
			json.put("code", 1);
			json.put("msg", "查询失败，该机器编码不存在");
			return json;
		}
		device.setCustAddress(device.getCustArea());
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("data", device);
		return json;
	}

	/**
	 * 匹配新增保养计划页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/newMaintenance")
	public ModelAndView newMaintenance(ModelAndView modelAndView) {
		modelAndView.setViewName("/maintenanceManage/html/newMaintenance");
		return modelAndView;
	}

	/**
	 * 增加保养方法，成功后返回保养管理页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addMaintenance", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAddManager(ModelAndView modelAndView, HttpServletRequest request)
			throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 1.从前端接收机器编码
		String machCode = request.getParameter("machCode");
		// 保养频率
		String mainFrequency = request.getParameter("mainFrequency");
		if (machCode == null || machCode.equals("")) {
			json.put("code", 1);
			json.put("msg", "机器编码不能为空");
			return json;
		}
		if (mainFrequency == null || mainFrequency.equals("")) {
			json.put("code", 1);
			json.put("msg", "保养频率不能为空");
			return json;
		}
		if (customerManageService.selectByCode(machCode) == null) {
			json.put("code", 1);
			json.put("msg", "对不起，没有该机器编码，请重新输入");
			return json;
		}
		if (customerManageService.selectByCode(machCode).getContractNo() == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器还没有绑定合同，不能进行保养");
			return json;
		}
		if (maintenanceservice.selectOneMaintenance(null, machCode) != null) {
			json.put("code", 1);
			json.put("msg", "该机器已经添加过保养计划，请输入还未添加保养计划的机器");
			return json;
		}
		// 2.根据机器编码找出相应的合同信息，并查询锁相应参数
		Device device = customerManageService.selectByCode(machCode);
		if (customerManageService.selectByPrimaryKey(device.getContractNo()) == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器对应的合同尚未录入系统，请添加该设备对应的合同信息");
			return json;
		}
		// 合同编码
		String contractCode = device.getContractNo();
		// 客户名称
		String custName = device.getCustArea();
		// 服务区域
		String compName = device.getServiceArea();
		// 联系人
		String repairMan = device.getCustLinkman();
		// 联系电话
		String repairService = device.getLinkmanPhone();
		// 保养工程师Id
		String responsibleName = request.getParameter("responsibleName");
		// 后备工程师Id
		String reserveEnginnerName = request.getParameter("reserveEnginnerName");
		// 责任工程师
		StaffInfo responsible = null;
		// 后备工程师
		StaffInfo reserveEnginner = null;
		for (StaffInfo staff : staffInfoService.selectAllStaff()) {
			if (staff.getName().equals(responsibleName)) {
				responsible = staff;
			}
			if (staff.getName().equals(reserveEnginnerName)) {
				reserveEnginner = staff;
			}
		}
		if (responsible == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该责任工程师不存在");
			return json;
		}
		if (reserveEnginner == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该后备工程师不存在");
			return json;
		}
		// 新增保养计划
		Maintenance tenance = new Maintenance(contractCode, custName, repairMan, repairService, mainFrequency,
				responsible.getName(), responsible.getStaffId(), reserveEnginner.getName(),
				reserveEnginner.getStaffId(), compName, machCode, null, null, null, 0, null, null, new Date());
		boolean result = maintenanceservice.insertOrder(tenance);
		if (result) {
			json.put("code", 0);
			json.put("msg", "添加成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "添加失败");
		return json;
	}

	/**
	 * 删除保养计划
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/deleteMaintenance", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deleteMaintenance(HttpServletRequest request) throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 1.从前端接收机器编码
		String machCode = request.getParameter("machCode");
		if (machCode == null || machCode.equals("")) {
			json.put("code", 1);
			json.put("msg", "机器编码不能为空");
			return json;
		}
		if (maintenanceservice.deleteMaintenance(machCode) > 0) {
			json.put("code", 1);
			json.put("msg", "删除成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "删除失败");
			return json;
		}
	}

	/**
	 * 修改保养计划
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/updateMaintenance", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> updateMaintenance(HttpServletRequest request) throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 1.从前端接收机器编码
		String machCode = request.getParameter("machCode");
		// 保养频率
		String mainFrequency = request.getParameter("mainFrequency");
		if (machCode == null || machCode.equals("")) {
			json.put("code", 1);
			json.put("msg", "机器编码不能为空");
			return json;
		}
		if (mainFrequency == null || mainFrequency.equals("")) {
			json.put("code", 1);
			json.put("msg", "保养频率不能为空");
			return json;
		}
		// 2.根据机器编码找出相应的合同信息，并查询锁相应参数
		Device device = customerManageService.selectByCode(machCode);
		// 合同编码
		String contractCode = device.getContractNo();
		// 客户名称
		String custName = device.getCustArea();
		// 服务区域
		String compName = device.getServiceArea();
		// 联系人
		String repairMan = device.getCustLinkman();
		// 联系电话
		String repairService = device.getLinkmanPhone();
		// 保养工程师Id
		String responsibleName = request.getParameter("responsibleName");
		// 后备工程师Id
		String reserveEnginnerName = request.getParameter("reserveEnginnerName");
		// 责任工程师
		StaffInfo responsible = null;
		// 后备工程师
		StaffInfo reserveEnginner = null;
		for (StaffInfo staff : staffInfoService.selectAllStaff()) {
			if (staff.getName().equals(responsibleName)) {
				responsible = staff;
			}
			if (staff.getName().equals(reserveEnginnerName)) {
				reserveEnginner = staff;
			}
		}
		if (responsible == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该责任工程师不存在");
			return json;
		}
		if (reserveEnginner == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该后备工程师不存在");
			return json;
		}
		// 新增保养计划
		Maintenance tenance = new Maintenance(contractCode, custName, repairMan, repairService, mainFrequency,
				responsible.getName(), responsible.getStaffId(), reserveEnginner.getName(),
				reserveEnginner.getStaffId(), compName, machCode, null, null, null, 0, null, null, new Date());
		int result = maintenanceservice.updateMaintenance(tenance);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "修改成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "修改失败");
		return json;
	}

	/**
	 * 查看所有保养计划
	 */
	@RequestMapping("/selectMaintenace")
	public Map<String, Object> slectMaintenance(ModelAndView modelAndView) {
		maintenances = maintenanceservice.select();
		Map<String, Object> json = new HashMap<>();
		modelAndView.addObject("maintenances", maintenances);
		modelAndView.setViewName("/maintenanceManage/html/maintenancePlan");
		json.put("code", 1);
		json.put("msg", "资产属性数据");
		json.put("count", maintenances.size());
		json.put("data", maintenances);
		return json;
	}

	/**
	 * 根据服务区域查看保养计划
	 */
	/*
	 * @RequestMapping("/selcetmaintenances") public ModelAndView
	 * selcetmaintenances(ModelAndView modelAndView, HttpServletRequest
	 * request){ String compName=request.getParameter("compName");
	 * if(compName==null || "".equals(compName.trim())){ compName=""; }
	 * maintenances =maintenanceservice.selectmaintenance(compName);
	 * modelAndView.addObject("maintenances", maintenances);
	 * modelAndView.setViewName("/maintenanceManage/html/maintenancePlan");
	 * return modelAndView;
	 * 
	 * }
	 */

	/**
	 * 导出保养计划
	 * 
	 * @param model
	 * @param request
	 * @param respose
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportmaintenanceMapper", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportmaintenanceMapper(ModelAndView model, HttpServletRequest request,
			HttpServletResponse respose) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		List<Maintenance> maintenances = (List<Maintenance>) export
				.get(request.getParameter("username") + "maintenancePlan");
		String tableName = "保养计划";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "合同号码", "机器编码", "联系人", "联系电话", "保养频率", "责任工程师", "后备工程师" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(respose, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Maintenance dev : maintenances) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(dev.getCustName());
			row.createCell(2).setCellValue(dev.getCompName());
			row.createCell(3).setCellValue(dev.getContractCode());
			row.createCell(4).setCellValue(dev.getMachCode());
			row.createCell(5).setCellValue(dev.getRepairMan());
			row.createCell(6).setCellValue(dev.getRepairService());
			row.createCell(7).setCellValue(dev.getMainFrequency());
			row.createCell(8).setCellValue(dev.getResponsibleEngineer());
			row.createCell(9).setCellValue(dev.getReservEngineer());
		}
		ExcelUtils.download(respose, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * 导出保养执行
	 * 
	 * @param model
	 * @param request
	 * @param respose
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportmaintenancePerformer", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportmaintenancePerformer(ModelAndView model, HttpServletRequest request,
			HttpServletResponse respose) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "保养执行";
		List<Maintenance> maintenancePerforms = (List<Maintenance>) export
				.get(request.getParameter("username") + "maintenancePerformer");
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "合同号码", "机器编码", "保养完成时间", "保养频率", "保养执行状态", "覆盖率(%)", "黑白读数", "彩色读数",
				"耗材型号", "耗材数量", "责任工程师", "后备工程师", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(respose, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Maintenance maintenance : maintenancePerforms) {
			maintenance.setDevice(customerManageService.selectDeviceById(maintenance.getMachCode()));
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(maintenance.getCustName());
			row.createCell(2).setCellValue(maintenance.getCompName());
			row.createCell(3).setCellValue(maintenance.getContractCode());
			row.createCell(4).setCellValue(maintenance.getMachCode());
			row.createCell(5).setCellValue(
					maintenance.getLastTime() == null ? "" : ExcelUtils.fmtOne.format(maintenance.getLastTime()));
			row.createCell(6).setCellValue(maintenance.getMainFrequency());
			row.createCell(7).setCellValue(maintenance.getMaintenanceState() == 0 ? "未完成" : "已完成");
			row.createCell(8).setCellValue(maintenance.getCoverage() == null ? "" : maintenance.getCoverage() + "%");
			row.createCell(9).setCellValue(maintenance.getDevice().getBwReader());
			row.createCell(10).setCellValue(maintenance.getDevice().getColorReader());
			row.createCell(11).setCellValue(maintenance.getMaterialModel());
			row.createCell(12).setCellValue(maintenance.getMaterialNumber());
			row.createCell(13).setCellValue(maintenance.getResponsibleEngineer());
			row.createCell(14).setCellValue(maintenance.getReservEngineer());
			row.createCell(15).setCellValue("");
		}
		ExcelUtils.download(respose, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
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
			post = "日";
			break;
		case "2":
			post = "周";
			break;
		case "3":
			post = "月";
			break;
		case "4":
			post = "季度";
			break;
		case "5":
			post = "半年";
			break;
		case "6":
			post = "年";
			break;
		}
		return post;
	}

	/**
	 * 工程师接口：查看工程师负责保养的合同
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/EngMaintenanceContract", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngMaintenanceContract() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String openId = request.getParameter("openId"); // openId
		// 根据openId查询工程师Id
		String staffId = "";
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openId)) {
				staffId = user.getUserId();
			}
		}
		for (StaffInfo staff : staffInfoService.selectAllStaff()) {
			if (staff.getPhone().equals(staffId)) {
				staffId = staff.getStaffId();
			}
		}
		if (staffId == null || staffId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不存在，请重新输入");
			return json;
		}
		List<Maintenance> maintenances = maintenanceservice.selectByContract(staffId);
		List<MaintenanceContract> maintenanceContracts = new ArrayList<>();
		for (Maintenance maintenance : maintenances) {
			MaintenanceContract maintenanceContract = new MaintenanceContract();
			maintenanceContract.setContractNo(maintenance.getContractCode());
			maintenanceContract.setCustName(maintenance.getCustName());
			maintenanceContracts.add(maintenanceContract);
		}
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("count", maintenanceContracts.size());
		json.put("data", maintenanceContracts);
		return json;
	}

	/**
	 * 工程师接口：查看工程师近期的需要保养的设备
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/EngMaintenancePlan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngMaintenancePlan(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收参数
		String Cycle = request.getParameter("Cycle");// 查询周期
		String openId = request.getParameter("openId"); // openId
		String contractNo = request.getParameter("contractNo"); // 合同号
		String isOver = request.getParameter("isOver"); // 是否完成
		Map<String, Object> json = new HashMap<>();
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", openId);
		params.put("合同号", contractNo);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		Date date = new Date();// 获取当前年月日
		// 先查出相应的保养设备
		String staffId = "";
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openId)) {
				staffId = user.getUserId();
			}
		}
		for (StaffInfo staff : staffInfoService.selectAllStaff()) {
			if (staff.getPhone().equals(staffId)) {
				staffId = staff.getStaffId();
			}
		}
		if (staffId == null || staffId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不存在，请重新输入");
			return json;
		}
		List<Maintenance> maintenances = maintenanceservice.selectByCycle(Cycle, staffId, null, contractNo);
		for (Maintenance maintenance : maintenances) {
			maintenance.setMaintenStatus(maintenance.getMaintenanceState() == 0 ? "未完成" : "已完成");
		}
		List<MaintenanceEnginner> maintenanceEnginners = new ArrayList<>();
		if (Cycle != null && !Cycle.trim().equals("")) {
			if (isOver.equals("已完成")) {
				for (Maintenance maintenance : maintenances) {
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
				for (Maintenance maintenance : maintenances) {
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
				for (Maintenance maintenance : maintenances) {
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
				for (Maintenance maintenance : maintenances) {
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
	 * 工程师接口：查看工程师名下的保养设备，以及保养状态
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/EngResponMaintenancePlan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngResponMaintenancePlan(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收参数
		String responsibleId = request.getParameter("responsibleId");// 工程师id
		Map<String, Object> json = new HashMap<>();
		maintenances = maintenanceservice.selectByCycle("", responsibleId, "", "");
		json.put("code", 0);
		json.put("msg", "需要保养的设备信息");
		json.put("count", maintenances.size());
		json.put("data", maintenances);
		return json;
	}

	/**
	 * 工程师接口：查看具体的某个保养设备的信息
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/EngOneMaintenancePlan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngOneMaintenancePlan() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		Map<String, Object> json = new HashMap<>();
		String contractNo = request.getParameter("contractNo");
		String machCode = request.getParameter("machCode");
		Maintenance maintenance = maintenanceservice.selectOneMaintenance(contractNo, machCode);
		json.put("code", 0);
		json.put("msg", "保养的设备信息");
		json.put("count", 1);
		json.put("data", maintenance);
		return json;
	}

	/**
	 * 工程师接口：提交保养执行
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/EngMaintenancePerform", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngMaintenancePerform() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		Map<String, Object> json = new HashMap<>();
		String machCode = request.getParameter("machCode");// 机器编码
		String bwReader = request.getParameter("bwReader");// 黑白读数
		String coReader = request.getParameter("coReader");// 彩色读数
		String materialModel = request.getParameter("materialModel");// 耗材型号
		String coverage = request.getParameter("coverage"); // 覆盖率
		Date date = new Date();
		String materialNumber = request.getParameter("materialNumber"); // 耗材数量
		// 进行非空判断
		// 判断输入参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("机器编码", machCode);
		args.put("黑白读数", bwReader);
		args.put("彩色读数", coReader);
		args.put("耗材型号", materialModel);
		args.put("耗材数量", materialNumber);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 查出相应的合同编码
		Device device = customerManageService.selectByCode(machCode);
		if (device == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器编码不存在");
			return json;
		}
		if (device.getContractNo() == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器尚未签订合同，无法进行保养");
			return json;
		}
		// 查找出相应的保养
		Maintenance maintenan = maintenanceservice.selectOneMaintenance(null, machCode);
		if (maintenan == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器尚未添加到保养计划，无法进行保养");
			return json;
		}
		device.setMachCode(machCode.toUpperCase());
		device.setBwReader(bwReader);
		device.setColorReader(coReader);
		customerManageService.updateByPrimaryKeySelective(device);
		Maintenance maintenance = new Maintenance();
		maintenance.setMachCode(machCode);
		maintenance.setMaintenanceState(1);
		maintenance.setMaterialModel(materialModel);
		maintenance.setMaterialNumber(materialNumber);
		maintenance.setCoverage(coverage);
		maintenance.setLastTime(date);
		int result = maintenanceservice.updateMaintenance(maintenance);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "保养执行提交成功");
			json.put("count", 1);
			json.put("data", maintenance);
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "保养执行提交失败");
			json.put("count", 1);
			json.put("data", "保养执行提交失败");
			return json;
		}
	}

	/**
	 * 工程师接口：查看保养执行情况
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/EngSetMaintenancePerform", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngSetMaintenancePerform() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		Map<String, Object> json = new HashMap<>();
		MaintenancePerform maintenancePerform = new MaintenancePerform();
		String responsibleId = request.getParameter("responsibleId");
		String contractCode = request.getParameter("contractCode");
		String machCode = request.getParameter("machCode");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		maintenancePerform.setResponsibleId(responsibleId);
		maintenancePerform.setMachCode(machCode);
		maintenancePerform.setContractCode(contractCode);
		List<MaintenancePerform> result = maintenanceservice.selectByDynamic(maintenancePerform, startDate,
				endDate);
		json.put("code", 0);
		json.put("msg", "保养执行情况");
		json.put("count", result.size());
		json.put("data", result);
		return json;
	}

	/*
	 * @ResponseBody public Map<String, Object> test() { // 连接本地的 Redis 服务
	 * Map<String, Object> json = new HashMap<>(); Jedis jedis = new
	 * Jedis("localhost"); List<CustInfo> custInfos =
	 * custInfo.selectCustByBaseInfo(null, null, null, null, null); // 设置 redis
	 * 字符串数据 jedis.set("runoobkey", JSONArray.fromObject(custInfos).toString());
	 * // 获取存储的数据并输出 String a = jedis.get("runoobkey"); List<CustInfo>
	 * custInfoss = JSONArray.toList(JSONArray.fromObject(a), CustInfo.class);
	 * for (CustInfo custInfo : custInfoss) { System.out.println(custInfo); }
	 * json.put("data", custInfoss); return json;
	 * }y.fromObject(custInfos).toString()); // 获取存储的数据并输出 String a =
	 * jedis.get("runoobkey"); List<CustInfo> custInfoss =
	 * JSONArray.toList(JSONArray.fromObject(a), CustInfo.class); for (CustInfo
	 * custInfo : custInfoss) { System.out.println(custInfo); } json.put("data",
	 * custInfoss); return json; }
	 */
}
