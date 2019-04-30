package com.xunwei.som.base.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.calendar.CalendarTool;
import com.xunwei.som.pojo.AssetNumber;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.CustomerSatisfaction;
import com.xunwei.som.pojo.front.DeviceBasic;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.AsAetNumberService;
import com.xunwei.som.service.CompInfoService;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.AsAetNumberServiceImpl;
import com.xunwei.som.service.impl.CompInfoServiceImpl;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;
import com.xunwei.som.util.WeChatUtils;
import com.xunwei.som.util.ZXingUtils;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * 客户管理模块
 * 
 * @author
 *
 */

@Controller
@Transactional(rollbackFor = Exception.class)
public class CustomerManageController extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private AsAetNumberService asSetNumberService = new AsAetNumberServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();

	private UserService userService = new UserServiceImpl();

	private CompInfoService compInfoService = new CompInfoServiceImpl();

	// 用于保存每个用户的查询记录
	private Map<String, Object> export = new HashMap<>();
	// 用来存放每次查询的设备结果集
	private List<Device> devices = new ArrayList<>();
	// 用来存放每次查询的合同结果集
	private List<Contract> contracts;
	// 用来存放每次查询的客户满意度结果集
	private List<ServiceInfo> serviceInfos;
	// 客户满意度管理***************************************************************************

	/**
	 * 匹配客户满意度管理页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/customerSatisfactionManager", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerSatFactionManager(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受数据
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String custSat = request.getParameter("custSat");
		String woStatus = request.getParameter("woStatus");
		List<CustomerSatisfaction> CustomerSatisfactions = new ArrayList<>();
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
		// 当前登录用户
		String identifier = null;
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName("");
			identifier = "1";
		}
		if (woStatus != null && !"".equals(woStatus.trim())) {
			startDate = ExcelUtils.fmtOne.format(SOMUtils.initDateByMonth()); // 当月开始时间
			endDate = ExcelUtils.fmtOne.format(new Date()); // 当前时间
			if (userRole.getRoleId().equals("客户")) {
				serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
						user.getCustName(), startDate, endDate, null, null, null, null, null, null, null, null, null,
						null, null, identifier);
			} else {
				serviceInfos = serviceInfoService.selectServiceInfByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
						null, startDate, endDate, null, null, null, null, null, null, null, null, null, null, null,
						identifier);
			}
			// 用户评价总数
			if (woStatus.equals("a")) {
				// 循环给orderInfo和staffInfo赋值
				for (ServiceInfo service : serviceInfos) {
					if (service.getCustScore() == null) {
						continue;
					}
					CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
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
		} else {
			// 判断是用户还是公司账号
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
			serviceInfos = serviceInfoService.selectServiceInfByDynamic(
					serviceArea == null ? null : SOMUtils.orderNumToComp(serviceArea), custName, startDate, endDate, "",
					custSat, "", "", "", "", null, null, null, null, null, identifier);
			List<ServiceInfo> ServiceInfo = serviceInfoService.selectServiceInfByDynamic(
					serviceArea == null ? null : SOMUtils.orderNumToComp(serviceArea), custName, startDate, endDate, "",
					custSat, "", "", "", "", null, null, null, null, null, identifier);
			// 循环给orderInfo和staffInfo赋值
			for (ServiceInfo service : ServiceInfo) {
				if (service.getProbSolve() == null) {
					continue;
				}
				CustomerSatisfaction CustomerSatisfaction = new CustomerSatisfaction();
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
		export.put(request.getParameter("username") + "customerSatisfactionManager", CustomerSatisfactions);
		json.put("code", 0);
		json.put("msg", "客户满意度管理数据");
		json.put("count", CustomerSatisfactions == null ? 0 : CustomerSatisfactions.size());
		json.put("data", page == null || CustomerSatisfactions == null ? CustomerSatisfactions
				: CustomerSatisfactions.subList(page, limit));
		return json;
	}

	/**
	 * 匹配客户满意度评价页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/addCustSatisfa")
	public ModelAndView addCustomerSatisfaction(ModelAndView modelAndView) {
		modelAndView.setViewName("/serviceboard/html/addCustSatisfa");
		return modelAndView;
	}

	/**
	 * 方法：客户满意度评价
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/doAddCustSatisfa", produces = "application/json; charset=utf-8")
	@ResponseBody
	public JSONObject doAddCustomerSatisfaction(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		JSONObject jsons = null;
		String woNumber = request.getParameter("woNumber");
		Integer custScore = Integer.valueOf(request.getParameter("custScore"));
		String custEva = request.getParameter("custEva");
		String custSat;
		String WoProgress = "";
		if (custScore <= 3) {
			custSat = "不满意";
		} else if (custScore >= 5) {
			custSat = "非常满意";
		} else {
			custSat = "满意";
		}
		switch (custScore) {
		case 1:
			WoProgress = "非常不满意";
			break;
		case 2:
			WoProgress = "不满意";
			break;
		case 3:
			WoProgress = "一般";
			break;
		case 4:
			WoProgress = "满意";
			break;
		case 5:
			WoProgress = "非常满意";
			break;
		default:
			break;
		}
		OrderInfo order = new OrderInfo();
		order.setWoNumber(woNumber);
		order.setWoProgress(WoProgress);
		serviceManageServiceImpl.updateOrder(order);
		// 1.判断工单号是否存在
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		// 如果工单号不存在，返回评价页面
		if (service == null) {
			json.put("code", 1);
			json.put("data", "该工单号不存在");
			json.put("msg", "评价失败");
			jsons = JSONObject.fromObject(json);
			return jsons;
		}
		// 如果工单号存在，但已经评价过，则提示该订单已评价
		if (service.getCustScore() != null) {
			json.put("code", 1);
			json.put("data", "该工单号已经评价过");
			json.put("msg", "评价失败");
			jsons = JSONObject.fromObject(json);
			return jsons;
		}
		serviceInfoService.update(woNumber, custSat, custEva, custScore);
		json.put("code", 0);
		json.put("data", "评价成功");
		json.put("msg", "评价成功");
		jsons = JSONObject.fromObject(json);
		return jsons;
	}

	/**
	 * 每小时执行一次，将满48个小时未评价的工单默认设置为4星
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60) // 服务器每小时执行一次。
	public void autoSetCustScore() throws Exception {
		OrderInfo order = new OrderInfo();
		order.setWoStatus("1");
		Date date = new Date();
		long a;
		for (ServiceInfo serviceInfo : serviceInfoService.selectOrderByWeChatCustomer(order)) {
			if (serviceInfo.getProbSolve() != null) {
				if (serviceInfo.getCustScore() == null) {
					a = date.getTime() - serviceInfo.getProbSolve().getTime();
					if (a >= 1000 * 60 * 60 * 48) {
						serviceInfo.setCustScore(4);
						serviceInfoService.upDateServiceInfo(serviceInfo);
					}
				}
			}
		}
	}

	/**
	 * 每小时执行一次，初始化服务区域列表
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60) // 服务器每小时执行一次。
	public void autoSetSetviceArea() throws Exception {
		SOMUtils.compInfos = compInfoService.selectAllComp();
	}

	/**
	 * 导出客户满意度Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportSatFactionManager", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportSatFactionManager(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		List<CustomerSatisfaction> serviceInfos = (List<CustomerSatisfaction>) export
				.get(request.getParameter("username") + "customerSatisfactionManager");
		String tableName = "客户满意度表";
		// 设置表头
		String[] Titles = { "客户名称", "工单号码", "服务区域", "报修人", "报修电话", "维修工程师", "星评得分", "评价内容" };
		System.out.println("执行了导出方法");
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (CustomerSatisfaction serviceInfo : serviceInfos) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(serviceInfo.getCustName());
			row.createCell(2).setCellValue(serviceInfo.getWoNumber());
			row.createCell(3).setCellValue(SOMUtils.CompToOrderNumTo(serviceInfo.getWoNumber().substring(0, 2)));
			row.createCell(4).setCellValue(serviceInfo.getRepairMan());
			row.createCell(5).setCellValue(serviceInfo.getRepairPhone());
			row.createCell(6).setCellValue(serviceInfo.getEnginnerName());
			if (serviceInfo.getCustScore() == null) {
				row.createCell(7).setCellValue("");
			} else {
				row.createCell(7).setCellValue(serviceInfo.getCustScore());
			}
			row.createCell(8).setCellValue(serviceInfo.getCustEva());
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	// 合同管理↓*********************************************************************************
	/**
	 * 匹配合同管理页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/contractManage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> contractManage(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端获取参数
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String contractNature = request.getParameter("contractNature"); // 合同性质
		String woStatus = request.getParameter("woStatus"); // 标识符
		String order = request.getParameter("order"); // 标识符
		List<Contract> timeContracts = new ArrayList<Contract>();
		List<Contract> dueToContracts = new ArrayList<Contract>();
		List<Contract> selectContracts = new ArrayList<Contract>();
		Date date = new Date();
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		if (order != null && order.equals("asc")) {
			order = "1";
		} else if (order != null && order.equals("desc")) {
			order = "2";
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
		// 当前登录用户
		String identifier = null;
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName("");
			identifier = "1";
		}
		if (woStatus != null && !"".equals(woStatus.trim())) {
			if (woStatus.equals("a")) {
				if (userRole.getRoleId().equals("客户")) {
					contracts = customerManage.selectByCust(user.getCustName(), null, "", "", null, null, null, order,
							identifier);
					selectContracts = customerManage.selectByCust(user.getCustName(), null, "", "", page, limit, null,
							order, identifier);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null,
							order, identifier);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null,
							order, identifier);
				} else {
					contracts = customerManage.selectByCust(null, user.getCustName(), "", "", null, null, null, order,
							identifier);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "", "", page, limit, null,
							order, identifier);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null,
							order, identifier);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null,
							order, identifier);
				}
				// 保存数据
				for (Contract contract : selectContracts) {
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
				if (userRole.getRoleId().equals("客户")) {
					contracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null, order,
							identifier);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "", "", page, limit, null,
							order, identifier);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null,
							order, identifier);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null,
							order, identifier);
				} else {
					contracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null, order,
							identifier);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", page, limit, null,
							order, identifier);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null,
							order, identifier);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null,
							order, identifier);
				}
				// 保存数据
				for (Contract contract : selectContracts) {
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
				// 查找到期合同
				timeContracts = contracts;
			} else if (woStatus.equals("c")) {
				if (userRole.getRoleId().equals("客户")) {
					contracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null, order,
							identifier);
					selectContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", page, limit, null,
							order, identifier);
					timeContracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null,
							order, identifier);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null,
							order, identifier);
				} else {
					contracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null, order,
							identifier);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", page, limit, null,
							order, identifier);
					timeContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null,
							order, identifier);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null,
							order, identifier);

				}
				// 保存数据
				for (Contract contract : selectContracts) {
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
				dueToContracts = contracts;
			}
		} else {
			// 判断是用户还是公司账号
			if (SOMUtils.getCompName(request).get("role").equals("客户")) {
				custName = (String) SOMUtils.getCompName(request).get("compname");
			} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
					|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
				serviceArea = (String) SOMUtils.getCompName(request).get("compname");
			}
			if (serviceArea != null) {
				if (serviceArea.equals("广州乐派数码科技有限公司") || serviceArea.equals("系统推进部") || serviceArea.equals("行业客户部")) {
					serviceArea = request.getParameter("serviceArea");
					identifier = "1";
				}
			}
			// 查询相应的合同
			selectContracts = customerManage.selectByCust(custName, serviceArea, "", "", page, limit, contractNature,
					order, identifier);
			contracts = customerManage.selectByCust(custName, serviceArea, "", "", null, null, contractNature, order,
					identifier);
			// 查找到期合同
			timeContracts = customerManage.selectByCust(custName, serviceArea, "1", "", null, null, null, order,
					identifier);
			// 查找一年内到期合同
			dueToContracts = customerManage.selectByCust(custName, serviceArea, "", "1", null, null, null, order,
					identifier);
			// 保存数据
			for (Contract contract : selectContracts) {
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
		export.put(request.getParameter("username") + "contractManage", contracts);
		json.put("code", 0);
		json.put("msg", "合同管理数据");
		json.put("count", contracts.size());
		json.put("data", selectContracts);
		json.put("count2", dueToContracts == null ? 0 : dueToContracts.size());
		json.put("count3", timeContracts == null ? 0 : timeContracts.size());
		json.put("count4", 0);
		return json;
	}

	/**
	 * 方法：导出合同管理页面Excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportContractManage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportContractManage(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "合同管理表";
		List<Contract> contracts = (List<Contract>) export.get(request.getParameter("username") + "contractManage");
		// 设置表头
		String[] Titles = { "客户名称", "主服务区域", "分服务区域", "合同编码", "合同类型", "合同性质", "签约日期", "到期时间", "登记时间", "合同期限(月)",
				"离到期天数", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Contract contract : contracts) {
			// 合同期限
			Date date = new Date();
			long a = contract.getEndDate().getTime() - contract.getStartDate().getTime();
			int b = (int) (a / (1000 * 3600 * 24));
			Integer ContractDeadline = Integer.valueOf(b) / 30;
			// 离到期天数
			long c = contract.getEndDate().getTime() - date.getTime();
			if (c > 0) {
				int d = (int) (c / (1000 * 3600 * 24));
				contract.setDueDays(d);
			} else {
				contract.setDueDays(0);
			}
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(contract.getCustName());
			row.createCell(2).setCellValue(contract.getMainService());
			row.createCell(3).setCellValue(contract.getChildService());
			row.createCell(4).setCellValue(contract.getContractNo());
			row.createCell(5).setCellValue(contract.getContractType());
			row.createCell(6).setCellValue(contract.getContractNature());
			row.createCell(7).setCellValue(ExcelUtils.fmt.format(contract.getStartDate()));
			row.createCell(8).setCellValue(ExcelUtils.fmt.format(contract.getEndDate()));
			row.createCell(9)
					.setCellValue(contract.getRegTime() == null ? "" : ExcelUtils.fmt.format(contract.getRegTime()));
			row.createCell(10).setCellValue(ContractDeadline);
			row.createCell(11).setCellValue(contract.getDueDays());
			row.createCell(12).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * 匹配新增合同页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/addContract")
	public ModelAndView addContract(ModelAndView modelAndView) {
		modelAndView.setViewName("/customerManage/html/addContract");
		return modelAndView;
	}

	/**
	 * 匹配设备信息页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/equipmentInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> equipmentInfo(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		if (devices != null) {
			devices.clear();
		}
		// 从前端接受参数
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String assetAscription = request.getParameter("assetAscription");
		String machCode = request.getParameter("machCode");
		// 判断是用户还是公司账号
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		String woStatus = request.getParameter("woStatus"); // 标识符
		List<Device> selectDevices = new ArrayList<>(); // 返回给前台的设备数据
		// i用于记录有多少签约设备
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
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName("");
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName("");
			identifier = "1";
		}
		if (woStatus != null && !"".equals(woStatus.trim())) {
			if (woStatus.equals("a")) {
				if (userRole.getRoleId().equals("客户")) {
					devices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), null, null, null, null, identifier);
					selectDevices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), null, null, page, limit, identifier);
				} else {
					devices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()), null, null,
							null, null, null, identifier);
					selectDevices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), null, null, page, limit, identifier);
				}
			} else if (woStatus.equals("b")) {
				if (userRole.getRoleId().equals("客户")) {
					devices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), "客户", null, null, null, identifier);
					selectDevices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), null, null, page, limit, identifier);
				} else {
					devices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()), null, "客户",
							null, null, null, identifier);
					selectDevices = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), null, null, page, limit, identifier);
				}
			} else if (woStatus.equals("c")) {
				List<Device> devicess = new ArrayList<>();
				if (userRole.getRoleId().equals("客户")) {
					devicess = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()),
							user.getCustName(), null, null, null, null, identifier);
				} else {
					devicess = customerManage.selectByDynamic(SOMUtils.orderNumToComp(user.getCustName()), null, null,
							null, null, null, identifier);
				}
				int a = 1;
				for (Device device : devicess) {
					if (!device.getAssetAttr().equals("客户")) {
						if (a <= 10) {
							selectDevices.add(device);
							a++;
						}
						devices.add(device);
					}
				}
			}
		} else {
			if (SOMUtils.getCompName(request).get("role").equals("客户")) {
				custName = (String) SOMUtils.getCompName(request).get("compname");
			} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
					|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
				serviceArea = (String) SOMUtils.getCompName(request).get("compname");
			}
			if (serviceArea != null) {
				if (serviceArea.equals("广州乐派数码科技有限公司") || serviceArea.equals("系统推进部") || serviceArea.equals("行业客户部")) {
					serviceArea = request.getParameter("serviceArea");
					identifier = "1";
				}
			}
			devices = customerManage.selectByDynamic(serviceArea, custName, assetAscription, machCode, null, null,
					identifier);
			selectDevices = customerManage.selectByDynamic(serviceArea, custName, assetAscription, machCode, page,
					limit, identifier);
		}
		export.put(request.getParameter("username") + "equipmentInfo", devices);
		json.put("code", 0);
		json.put("msg", "设备管理数据");
		json.put("data", selectDevices);
		json.put("count", devices.size());
		return json;
	}

	/**
	 * 匹配设备新增页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/addEquipment")
	public ModelAndView addEquipment(ModelAndView modelAndView) {
		modelAndView.setViewName("/customerManage/html/addEquipment");
		return modelAndView;
	}

	/**
	 * 设备接口：查看二维码界面 ：返回二维码所在的项目路径
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/qrCode")
	@ResponseBody
	public String quipmentQrCode(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		// 从前端接受机器编码
		String machCode = request.getParameter("machCode");
		if (machCode == null || machCode.equals("")) {
			return "机器编码不能为空";
		}
		if (customerManage.selectByCode(machCode) == null) {
			return "该机器不存在";
		}
		String a = SOMUtils.ipAndPort + machCode + ".png";
		return a;
	}

	/**
	 * 设备接口：导出二维码
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downQrCode", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> downQrCode(ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受机器编码
		String machCode = request.getParameter("machCode");
		ZXingUtils.downloadFile(SOMUtils.qrAddr + machCode + ".png", machCode + ".png", response);
		json.put("code", 0);
		json.put("msg", "下载成功");
		return json;
	}

	/**
	 * 设备接口：批量导出二维码
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downQrCodeAll", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> downQrCodeAll(ModelAndView modelAndView) throws Exception {
		String downloadFilename = "设备二维码.zip";// 文件的名称
		downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");// 转换中文否则可能会产生乱码
		Map<String, Object> json = new HashMap<>();
		response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
		ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

		// 从前端接收数据
		String a = request.getParameter("machCodeArr");
		if (a == null || a.equals("")) {
			json.put("code", 1);
			json.put("msg", "请选择要导出的机器编码");
			return json;
		}
		String[] files = request.getParameter("machCodeArr").substring(1, a.length() - 1).split(",");
		for (int i = 0; i < files.length; i++) {
			URL url = new URL("http://47.106.110.201/som/qrcode/" + files[i] + ".png");
			zos.putNextEntry(new ZipEntry(files[i] + ".jpg"));
			// FileInputStream fis = new FileInputStream(new File(files[i]));
			InputStream fis = url.openConnection().getInputStream();
			byte[] buffer = new byte[1024];
			int r = 0;
			while ((r = fis.read(buffer)) != -1) {
				zos.write(buffer, 0, r);
			}
			fis.close();
		}
		zos.flush();
		zos.close();
		json.put("code", 0);
		json.put("msg", "批量成功");
		return json;
		/*
		 * response.setHeader("Access-Control-Allow-Origin", "*"); byte[] buffer
		 * = new byte[1024]; String strZipName = SOMUtils.qrAddr1 +
		 * "qrCode.zip";// 生成二维码文件 Map<String, Object> json = new HashMap<>();
		 * // 从前端接收数据 String a = request.getParameter("machCodeArr"); if (a ==
		 * null || a.equals("")) { json.put("code", 1); json.put("msg",
		 * "请选择要导出的机器编码"); return json; } String[] machCode =
		 * request.getParameter("machCodeArr").substring(1, a.length() -
		 * 1).split(","); System.out.println(machCode); File[] file1 = new
		 * File[machCode.length]; for (int i = 0; i < file1.length; i++) {
		 * file1[i] = new File(SOMUtils.qrAddr1 + machCode[i] + ".png"); }
		 * ZipOutputStream out = new ZipOutputStream(new
		 * FileOutputStream(strZipName)); // 需要同时下载的两个文件result.txt ，source.txt
		 * for (int i = 0; i < file1.length; i++) { FileInputStream fis = new
		 * FileInputStream(file1[i]); out.putNextEntry(new
		 * ZipEntry(file1[i].getName())); int len; // 读入需要下载的文件的内容，打包到zip文件
		 * while ((len = fis.read(buffer)) > 0) { out.write(buffer, 0, len); }
		 * out.closeEntry(); fis.close(); } out.close(); json.put("code", 0);
		 * json.put("msg", SOMUtils.qrAddr1 + "qrCode.zip"); return json;
		 */
	}

	/**
	 * 匹配设备变动页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/equipmentChange")
	public ModelAndView equipmentChange(ModelAndView modelAndView) {
		modelAndView.setViewName("/customerManage/html/equipmentChange");
		return modelAndView;
	}

	/**
	 * ——————————————————————————————————————————————————————————————————————————————————————————
	 * 
	 * @throws ParseException
	 */

	/**
	 * 方法：添加合同
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/doAddContract", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAddContract(ModelAndView modelAndView) throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 转换String类型为Date
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取前端参数
		// 客户信息
		String custName = request.getParameter("custName"); // 客户名称
		String custAddr = request.getParameter("custAddr"); // 客户地址
		String signingCompany = request.getParameter("signingCompany"); // 签约公司
		String custLinkman = request.getParameter("custLinkman"); // 客户联系人
		String linkmanPhone = request.getParameter("linkmanPhone"); // 客户联系人电话
		// 合同信息
		String mainService = request.getParameter("mainService"); // 主服务区
		String childService = request.getParameter("childService"); // 分服务区
		String contractNo = request.getParameter("contractNo"); // 合同编码
		String regTime = request.getParameter("regTime"); // 登记时间
		String handlingDepartment = request.getParameter("handlingDepartment"); // 经办部门
		String agent = request.getParameter("agent"); // 经办人
		String contractNature = request.getParameter("contractNature"); // 合同性质
		String contractHoldman = request.getParameter("contractHoldman"); // 合同保管人
		String startDate = request.getParameter("startDate"); // 合同开始日期
		String endDate = request.getParameter("endDate"); // 合同结束日期
		String contractType = request.getParameter("contractType"); // 合同类型
		String openingBank = request.getParameter("openingBank"); // 开户行
		String bankAccount = request.getParameter("bankAccount"); // 账号
		String taxIden = request.getParameter("taxIden"); // 纳税人识别号
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("客户名称", custName);
		params.put("客户地址", custAddr);
		params.put("签约公司", signingCompany);
		params.put("客户联系人", custLinkman);
		params.put("联系手机号", linkmanPhone);
		params.put("主服务区", mainService);
		params.put("分服务区", childService);
		params.put("合同编码", contractNo);
		params.put("合同性质", contractNature);
		params.put("签约日期", startDate);
		params.put("到期日期", endDate);
		// 判断是否存在空值
		String msg = SOMUtils.isNull(params);
		if (!msg.equals("")) {
			json.put("code", 1);
			json.put("msg", msg + "不能为空");
			return json;
		}
		// 先判断合同编码是否重复。
		if (customerManage.selectByPrimaryKey(contractNo) != null) {
			json.put("code", 1);
			json.put("msg", "对不起，您输入的合同编码已经存在");
			return json;
		}
		Date regTime1 = null;
		Date startDate1 = null;
		Date endDate1 = null;
		if (regTime != null && !"".equals(regTime)) {
			try {
				// 都满足条件后，新增，首先转换时间与数据格式
				regTime1 = format.parse(request.getParameter("regTime"));
			} catch (ParseException e) {
				json.put("code", 1);
				json.put("msg", "登记时间格式错误，请重新输入");
				return json;
			}
		}
		try {
			startDate1 = format.parse(request.getParameter("startDate"));
		} catch (ParseException e) {
			json.put("code", 1);
			json.put("msg", "开始时间格式错误，请重新输入");
			return json;
		}
		try {
			endDate1 = format.parse(request.getParameter("endDate"));
		} catch (ParseException e) {
			json.put("code", 1);
			json.put("msg", "结束时间格式错误，请重新输入");
			return json;
		}
		if (!(endDate1.after(startDate1))) {
			json.put("code", 1);
			json.put("msg", "对不起，开始时间不能再结束时间之前");
			return json;
		}
		if (custInfoService.selectCusIdByName(custName) <= 0) {
			json.put("code", 1);
			json.put("msg", "对不起，该客户不存在或尚未签订合同");
			return json;
		}
		// 创建合同对象，进行新增
		Contract contract = new Contract();
		contract.setCustName(custName);
		contract.setAddress(custAddr);
		contract.setSigningCompany(signingCompany);
		contract.setCustLinkman(custLinkman);
		contract.setLinkmanPhone(linkmanPhone);
		contract.setMainService(mainService);
		contract.setChildService(childService);
		contract.setContractNo(contractNo);
		contract.setRegTime(regTime1);
		contract.setHandlingDepartment(handlingDepartment);
		contract.setAgent(agent);
		contract.setContractHoldman(contractHoldman);
		contract.setStartDate(startDate1);
		contract.setEndDate(endDate1);
		contract.setContractType(contractType);
		contract.setOpeningBank(openingBank);
		contract.setBankAccount(bankAccount);
		contract.setTaxIden(taxIden);
		contract.setContractNature(contractNature);
		int result = customerManage.insert(contract);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "添加成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "添加失败");
		return json;

	}

	/**
	 * 方法：修改合同基本信息
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/changeContract", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> changeContract() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 转换String类型为Date
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取前端参数
		String ID = request.getParameter("ID"); // 合同ID
		// 客户信息
		String custName = request.getParameter("custName"); // 客户名称
		String custAddr = request.getParameter("custAddr"); // 客户地址
		String signingCompany = request.getParameter("signingCompany"); // 签约公司
		String custLinkman = request.getParameter("custLinkman"); // 客户联系人
		String linkmanPhone = request.getParameter("linkmanPhone"); // 客户联系人电话
		// 合同信息
		String mainService = request.getParameter("mainService"); // 主服务区
		String childService = request.getParameter("childService"); // 分服务区
		String contractNo = request.getParameter("contractNo"); // 合同编码
		String regTime = request.getParameter("regTime"); // 登记时间
		String handlingDepartment = request.getParameter("handlingDepartment"); // 经办部门
		String agent = request.getParameter("agent"); // 经办人
		String contractHoldman = request.getParameter("contractHoldman"); // 合同保管人
		String contractNature = request.getParameter("contractNature"); // 合同性质
		String startDate = request.getParameter("startDate"); // 合同开始日期
		String endDate = request.getParameter("endDate"); // 合同结束日期
		String contractType = request.getParameter("contractType"); // 合同类型
		String openingBank = request.getParameter("openingBank"); // 开户行
		String bankAccount = request.getParameter("bankAccount"); // 账号
		String taxIden = request.getParameter("taxIden"); // 纳税人识别号
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("客户名称", custName);
		params.put("客户地址", custAddr);
		params.put("签约公司", signingCompany);
		params.put("客户联系人", custLinkman);
		params.put("联系手机号", linkmanPhone);
		params.put("主服务区域", mainService);
		params.put("分服务区域", childService);
		params.put("合同编码", contractNo);
		params.put("合同性质", contractNature);
		params.put("签约日期", startDate);
		params.put("到期日期", endDate);
		// 先找出该序号之前的合同
		Contract oldContract = customerManage.selectByID(Integer.valueOf(ID));
		/*
		 * List<Device> a = customerManage.selectByDeviceKpi("", "",
		 * oldContract.getContractNo(), null, null, null);
		 */
		// 如果老合同下有绑定设备，则提示不能修改
		/*
		 * if (a != null && a.size() > 0) { json.put("code", 1); json.put("msg",
		 * "对不起，该合同已经绑定设备，不能修改"); return json; }
		 */
		// 判断是否存在空值
		String msg = SOMUtils.isNull(params);
		if (!msg.equals("")) {
			json.put("code", 1);
			json.put("msg", msg + "不能为空");
			return json;
		}
		// 修改后的合同编码不能与其他合同冲突
		if (customerManage.selectByPrimaryKey(contractNo) != null && !oldContract.getContractNo().equals(contractNo)) {
			json.put("code", 1);
			json.put("msg", "对不起，您输入的合同编码已经存在");
			return json;
		}
		Date regTime1 = null;
		Date startDate1 = null;
		Date endDate1 = null;
		if (regTime != null && !"".equals(regTime)) {
			try {
				// 都满足条件后，新增，首先转换时间与数据格式
				regTime1 = format.parse(request.getParameter("regTime"));
			} catch (ParseException e) {
				json.put("code", 1);
				json.put("msg", "登记时间格式错误，请重新输入");
				return json;
			}
		}
		try {
			startDate1 = format.parse(request.getParameter("startDate"));
		} catch (ParseException e) {
			json.put("code", 1);
			json.put("msg", "开始日期格式错误，请重新输入");
			return json;
		}
		try {
			endDate1 = format.parse(request.getParameter("endDate"));
		} catch (ParseException e) {
			json.put("code", 1);
			json.put("msg", "结束日期格式错误，请重新输入");
			return json;
		}
		// 创建合同对象，进行修改
		Contract contract = new Contract();
		contract.setCustName(custName);
		contract.setSigningCompany(signingCompany);
		contract.setCustLinkman(custLinkman);
		contract.setLinkmanPhone(linkmanPhone);
		contract.setMainService(mainService);
		contract.setChildService(childService);
		contract.setContractNo(contractNo);
		contract.setRegTime(regTime1);
		contract.setHandlingDepartment(handlingDepartment);
		contract.setAgent(agent);
		contract.setContractHoldman(contractHoldman);
		contract.setStartDate(startDate1);
		contract.setEndDate(endDate1);
		contract.setContractType(contractType);
		contract.setOpeningBank(openingBank);
		contract.setBankAccount(bankAccount);
		contract.setTaxIden(taxIden);
		contract.setId(Integer.valueOf(ID));
		contract.setContractNature(contractNature);
		try {
			int result = customerManage.updateByPrimaryKeySelective(contract, null);
			for (Device device : customerManage.selectByDeviceKpi(null, null, oldContract.getContractNo(), null, null,
					null)) {
				device.setContractNo(contractNo);
				customerManage.updateByPrimaryKeySelective(device);
			}
			if (result > 0) {
				json.put("code", 0);
				json.put("msg", "修改成功");
				return json;
			}
			json.put("code", 1);
			json.put("msg", "修改失败");
			return json;
		} catch (DataAccessException e) {
			json.put("code", 1);
			json.put("msg", "你输入的合同编码已经存在，请重新输入");
			return json;
		}
	}

	/**
	 * 方法：查找设备
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/selectDevice", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectDevice() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收数据
		String machCode = request.getParameter("machCode"); // 机器编码
		Device device = customerManage.selectByCode(machCode);
		DeviceBasic deviceBasic = null;
		if (device != null) {
			String assetClass = device.getAssetClass();
			if (assetClass == null || assetClass.equals("")) {
				assetClass = "无";
			}
			String specifications = device.getSpecifications();
			if (specifications == null || specifications.equals("")) {
				specifications = "无";
			}
			deviceBasic = new DeviceBasic(device.getAssetAttr(), device.getDevName(), 1, device.getAssetNumber(),
					device.getMachCode(), assetClass, specifications);
			deviceBasic.setUnitType(device.getUnitType());
			deviceBasic.setResponsibleEngineerID(device.getResponsibleEngineerID());
			deviceBasic.setReserveEnginnerID(device.getReserveEnginnerID());
			deviceBasic.setDepartment(device.getHoldDepartment());
			deviceBasic.setLocation(device.getLocation());
			deviceBasic.setIp(device.getIP());
			deviceBasic.setBwReader(device.getBwReader());
			deviceBasic.setCoReader(device.getColorReader());
			deviceBasic.setInstalledTime(device.getInstalledTime() == null ? null : device.getInstalledTime());
			deviceBasic.setServiceLevel(device.getServiceLevel());
			deviceBasic.setSercet(device.getSecret());
			deviceBasic.setSercetLevel(device.getSecretLevel());
			deviceBasic.setDeviceType(device.getDeviceType());
			json.put("code", 0);
			json.put("msg", "查询成功");
			json.put("data", deviceBasic);
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "查询失败，相应的机器编码不存在");
			return json;
		}
	}

	/**
	 * 方法：设备绑定合同
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/deviceBindingContract", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deviceBindingContract() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 获取前端参数
		// 客户信息
		String contractNo = request.getParameter("contractNo"); // 合同编号
		String machCode = request.getParameter("machCode"); // 机器编码
		// 判断合同号是否存在
		Contract contract = customerManage.selectByPrimaryKey(contractNo);
		if (contract == null) {
			json.put("code", 1);
			json.put("msg", "合同号不存在，请检查并重新输入");
			return json;
		}
		// 查找相应合同
		// 先判断机器是否有绑定合同，如果没有合同，才可以绑定
		if (customerManage.selectDeviceById(machCode).getContractNo() == null
				|| customerManage.selectDeviceById(machCode).getContractNo().equals("")) {
			// 在增加机器的服务属性
			String secret = request.getParameter("secret"); // 是否涉密
			String secretLevel = request.getParameter("secretLevel"); // 涉密等级
			String responsibleEngineerID = request.getParameter("responsibleEngineerID");
			String reserveEnginnerID = request.getParameter("reserveEnginnerID");
			String department = request.getParameter("department"); // 部门
			String location = request.getParameter("location"); // 设备位置
			String serviceLevel = request.getParameter("serviceLevel"); // 服务级别
			String IP = request.getParameter("IP"); // 设备Ip
			String bwReader = request.getParameter("bwReader"); // 黑白读数
			String colorReader = request.getParameter("colorReader"); // 彩色读数
			String installedTime1 = request.getParameter("installedTime"); // 装机时间
			// 将传递过来的值存放到Map集合里面
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("涉密资质", secret);
			params.put("责任工程师", responsibleEngineerID);
			params.put("后备工程师", reserveEnginnerID);
			params.put("部门", department);
			params.put("保管部门", department);
			params.put("设备位置", location);
			params.put("黑白读数", bwReader);
			params.put("彩色读数", colorReader);
			// 判断其他栏目是否存在空值
			String param = SOMUtils.paramsIsNull(params);
			if (!param.equals("")) {
				json.put("code", 1);
				json.put("msg", param + "不能为空");
				return json;
			}
			Date installedTime = null;
			if (installedTime1 != null && !installedTime1.trim().equals("")) {
				try {
					installedTime = ExcelUtils.fmt.parse(installedTime1); // 安装日期
				} catch (ParseException e) {
					json.put("code", 1);
					json.put("msg", "装机日期输入格式不正确，请按照：2018-1-1的格式输入");
					return json;
				}
			}
			StaffInfo responsibleEngineer = null;
			StaffInfo reserveEnginner = null;
			for (StaffInfo staff : staffInfoServiceImplnew.selectAllStaff()) {

				if (staff.getName().equals(responsibleEngineerID)) {
					responsibleEngineer = staff;
				}
				if (staff.getName().equals(reserveEnginnerID)) {
					reserveEnginner = staff;
				}
				if (responsibleEngineer != null && reserveEnginner != null) {
					break;
				}
			}
			// 将传递过来的责任工程师ID和后备工程师ID转换为姓名
			if (responsibleEngineer == null) {
				json.put("code", 1);
				json.put("msg", "对不起，您输入的责任工程师姓名不存在，请重新输入");
				return json;
			}
			if (reserveEnginner == null) {
				json.put("code", 1);
				json.put("msg", "对不起，您输入的后备工程师姓名不存在，请重新输入");
				return json;
			}
			if (!(responsibleEngineer.getPost().equals("工程师") || responsibleEngineer.getPost().equals("技术主管"))) {
				json.put("code", 1);
				json.put("msg", "对不起，" + responsibleEngineer.getName() + "不是工程师或技术主管，请重新输入");
				return json;
			}
			if (!(reserveEnginner.getPost().equals("工程师") || reserveEnginner.getPost().equals("技术主管"))) {
				json.put("code", 1);
				json.put("msg", "对不起，" + reserveEnginner.getName() + "不是工程师或技术主管，请重新输入");
				return json;
			}
			// 判断涉密栏是否统一
			if (secret.equals("true")) {
				if (secretLevel.equals("") || secretLevel == null) {
					json.put("code", 1);
					json.put("msg", "当选择涉密时，涉密等级不能为空");
					return json;
				}
			}
			if (secret.equals("false")) {
				if (secretLevel != null && !secretLevel.equals("")) {
					secretLevel = null;
				}
			}
			// 执行绑定方法
			Device device = new Device();
			device.setContractNo(contractNo);
			device.setMachCode(machCode.toUpperCase());
			device.setLocation(location);
			device.setCustArea(contract.getCustName());
			device.setServiceArea(contract.getMainService());
			device.setDepartment(department);
			device.setServiceLevel(serviceLevel);
			device.setResponsibleEngineer(responsibleEngineer.getName());
			device.setResponsibleEngineerID(responsibleEngineer.getStaffId());
			device.setReserveEnginner(reserveEnginner.getName());
			device.setReserveEnginnerID(reserveEnginner.getStaffId());
			device.setBwReader(bwReader);
			device.setColorReader(colorReader);
			device.setInstalledTime(installedTime);
			device.setSecret(secret);
			device.setSecretLevel(secretLevel);
			device.setIP(IP);
			// 执行变动方法
			int result = customerManage.updateByPrimaryKeySelective(device);
			if (result > 0) {
				json.put("code", 0);
				json.put("msg", "绑定成功");
				return json;
			}
			json.put("code", 1);
			json.put("msg", "绑定失败");
			return json;
		} else {
			json.put("code", 1);
			json.put("msg", "您选择的设备已经绑定了合同，请重新输入设备");
			return json;
		}
	}

	/**
	 * 方法：设备基础信息修改
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = "/deviceChange", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deviceChange() throws ParseException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 获取前端参数
		// 资产信息
		String assetAttr = request.getParameter("assetAttr"); // 资产属性
		String assetNumber = request.getParameter("assetNumber"); // 资产编码
		String holdDepartment = request.getParameter("holdDepartment"); // 保管部门
		String holdMan = request.getParameter("holdMan"); // 保管人
		String assetClass = request.getParameter("assetClass"); // 资产类别
		String specifications = request.getParameter("specifications"); // 规格型号
		// 设备信息
		Integer ID = Integer.valueOf(request.getParameter("ID")); // 设备序号
		String machCode = request.getParameter("machCode"); // 机器编码
		String deviceType = request.getParameter("deviceType"); // 设备类型
		String unitType = request.getParameter("unitType"); // 设备型号
		String esNumber = request.getParameter("esNumber"); // 设备序列号
		String deviceBrand = request.getParameter("deviceBrand"); // 设备品牌
		String outputSpec = request.getParameter("outputSpec"); // 输出规格
		String deviceBound = request.getParameter("deviceBound"); // 设备幅面
		String assetStatus = request.getParameter("assetStatus"); // 资产状态
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("资产属性", assetAttr);
		params.put("机器编码", machCode);
		params.put("设备类型", deviceType);
		params.put("设备型号", unitType);
		params.put("设备品牌", deviceBrand);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (assetAttr.equals("迅维")) {
			if (holdDepartment == null || holdDepartment.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "保管部门不能为空");
				return json;
			}
			if (holdMan == null || holdMan.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "保管人不能为空");
				return json;
			}
			if (assetNumber == null || assetNumber.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "资产编码不能为空");
				return json;
			}
			if (assetStatus == null || assetStatus.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "资产状态不能为空");
				return json;
			}
		}
		// 先查找出修改之前的设备基础信息
		Device oldDevice = customerManage.selectDeviceById(Integer.valueOf(ID));
		Device newDevice = new Device();
		newDevice.setId(ID);
		newDevice.setAssetAttr(assetAttr);
		newDevice.setAssetNumber(assetNumber);
		newDevice.setHoldDepartment(holdDepartment);
		newDevice.setHoldMan(holdMan);
		newDevice.setAssetClass(assetClass);
		newDevice.setSpecifications(specifications);
		newDevice.setDevName(deviceType);
		newDevice.setMachCode(machCode.toUpperCase());
		newDevice.setDeviceType(deviceType);
		newDevice.setUnitType(unitType);
		newDevice.setEsNumber(esNumber);
		newDevice.setDeviceBrand(deviceBrand);
		newDevice.setOutputSpec(outputSpec);
		newDevice.setDeviceBound(deviceBound);
		newDevice.setAssetStatus(assetStatus);
		newDevice.setId(ID);
		int a = -1;
		try {
			a = customerManage.updateByPrimaryKeySelective(newDevice);
			// 资产编码没变
			if (oldDevice.getAssetNumber().equals(assetNumber)) {
				// 如果是迅维变为客户
				if (oldDevice.getAssetAttr().equals("迅维") && assetAttr.equals("客户")) {
					asSetNumberService.deleteAsset(oldDevice.getAssetNumber());
				} else if (oldDevice.getAssetAttr().equals("客户") && assetAttr.equals("迅维")) {
					AssetNumber assetNumbe = new AssetNumber();
					assetNumbe.setAssetNumber(assetNumber);
					asSetNumberService.insert(assetNumbe);
				} else if (oldDevice.getAssetAttr().equals("迅维") && assetAttr.equals("迅维")) {
					asSetNumberService.updateAsset(oldDevice.getAssetNumber(), assetNumber);
				}
			} else {
				// 如果是迅维变为客户
				if (oldDevice.getAssetAttr().equals("迅维") && assetAttr.equals("客户")) {
					asSetNumberService.deleteAsset(oldDevice.getAssetNumber());
				} else if (oldDevice.getAssetAttr().equals("客户") && assetAttr.equals("迅维")) {
					AssetNumber assetNumbe = new AssetNumber();
					assetNumbe.setAssetNumber(assetNumber);
					asSetNumberService.insert(assetNumbe);
				} else if (oldDevice.getAssetAttr().equals("迅维") && assetAttr.equals("迅维")) {
					asSetNumberService.updateAsset(oldDevice.getAssetNumber(), assetNumber);
				}
			}
			if (a > 0) {
				json.put("code", 0);
				json.put("msg", "修改成功");
				return json;
			}
			json.put("code", 0);
			json.put("msg", "修改失败");
			return json;
		} catch (DataAccessException e) {
			json.put("code", 1);
			json.put("msg", "你输入的机器编码或资产编码已经存在，请重新输入");
			return json;
		}
	}

	/**
	 * 方法：增加设备
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = "/doAddEquipment", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAddEquipment() throws DataAccessException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 获取前端参数
		// 资产信息
		String username = request.getParameter("username"); // 登陆人账号
		String assetAttr = request.getParameter("assetAttr"); // 资产属性
		String assetNumber = request.getParameter("assetNumber"); // 资产编码
		String holdDepartment = request.getParameter("holdDepartment"); // 保管部门
		String holdMan = request.getParameter("holdMan"); // 保管人
		String assetClass = request.getParameter("assetClass"); // 资产类别
		String specifications = request.getParameter("specifications"); // 规格型号
		// 设备信息
		String machCode = request.getParameter("machCode"); // 机器编码
		String deviceType = request.getParameter("deviceType"); // 设备类型
		String unitType = request.getParameter("unitType"); // 设备型号
		String esNumber = request.getParameter("esNumber"); // 设备序列号
		String deviceBrand = request.getParameter("deviceBrand"); // 设备品牌
		String outputSpec = request.getParameter("outputSpec"); // 输出规格
		String deviceBound = request.getParameter("deviceBound"); // 设备幅面
		// 判断是否有空值
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("资产属性", assetAttr);
		params.put("机器编码", machCode);
		params.put("设备类型", deviceType);
		params.put("设备型号", unitType);
		params.put("设备品牌", deviceBrand);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (assetAttr.equals("迅维")) {
			if (holdDepartment == null || holdDepartment.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "保管部门不能为空");
				return json;
			}
			if (holdMan == null || holdMan.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "保管人不能为空");
				return json;
			}
			if (assetNumber == null || assetNumber.trim().equals("")) {
				json.put("code", 1);
				json.put("msg", "资产编码不能为空");
				return json;
			}
		}
		// 设立正则表达式
		String regEx = "[A-Z]{3}\\d{4}";
		// 深圳分公司正则表达式
		String szRegEx = "[A-Z]{3}\\d{6}";
		// 当前登录用户
		User user = userService.selectByUserId(username);
		if (user.getCustName().equals("深圳分公司")) {
			// 编译正则表达式
			Pattern pattern = Pattern.compile(szRegEx);
			Matcher matcher = pattern.matcher(machCode);
			// 字符串是否与正则表达式相匹配
			boolean rs = matcher.matches();
			if (!rs) {
				json.put("code", 1);
				json.put("msg", "机器编码格式错误，正确示例为FTK000001，且英文字母需要大写");
				return json;
			}
		} else {
			// 编译正则表达式
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(machCode);
			// 字符串是否与正则表达式相匹配
			boolean rs = matcher.matches();
			if (!rs) {
				json.put("code", 1);
				json.put("msg", "机器编码格式错误，正确示例为GZZ0001，且英文字母需要大写");
				return json;
			}
		}
		// 判断机器编码是否已经存在
		if (customerManage.selectDeviceById(machCode) != null) {
			json.put("code", 1);
			json.put("msg", "您输入的机器编码已经存在，请重新输入");
			return json;
		}
		if (assetNumber != null && !assetNumber.trim().equals("")) {
			if (!customerManage.selectDeviceByAsSetNumber(assetNumber).isEmpty()) {
				json.put("code", 1);
				json.put("msg", "资产编码不能重复");
				return json;
			}
		}
		// 执行添加方法
		Device device = new Device();
		device.setAssetAttr(assetAttr);
		device.setAssetNumber(assetNumber);
		device.setHoldDepartment(holdDepartment);
		device.setHoldMan(holdMan);
		device.setAssetClass(assetClass);
		device.setSpecifications(specifications);
		device.setDevName(deviceType);
		device.setMachCode(machCode);
		device.setDeviceBrand(deviceBrand);
		device.setUnitType(unitType);
		device.setDeviceBound(deviceBound);
		device.setDeviceType(deviceType);
		device.setOutputSpec(outputSpec);
		device.setEsNumber(esNumber);
		device.setServiceArea(user.getCustName());
		int result = customerManage.insert(device);
		System.out.println(result > 0 ? "添加成功" : "添加失败");
		// 如果资产是迅维，则生成一条资产记录到数据库
		if (assetAttr.equals("迅维")) {
			device.setAssetStatus("使用中");
			customerManage.updateByPrimaryKeySelective(device);
			AssetNumber asset = new AssetNumber();
			asset.setAssetNumber(assetNumber);
			asSetNumberService.insert(asset);
		}
		// 生成二维码，保存到项目目录中
		String content = "https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%2fgetOpenId%3fmachCode%3d"
				+ device.getMachCode()
				+ "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
		File file = new File(SOMUtils.qrAddr + device.getMachCode() + ".png");
		if (!file.exists()) {
			file.createNewFile();
		}
		ZXingUtils.qrCode(content, SOMUtils.qrAddr + device.getMachCode() + ".png");
		json.put("code", 0);
		json.put("msg", "新增成功");
		return json;
	}

	/**
	 * 方法：变动设备
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/changeEquipment", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> changeEquipment() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 获取用户登录名
		if (username == null || username.equals("")) {
			json.put("code", 1);
			json.put("msg", "登录名不能为空");
			return json;
		}
		// 从前端接收该机器的机器编码
		String machCode = request.getParameter("machCode"); // 机器编码
		// 接受修改参数
		String changeType = request.getParameter("changeType"); // 变动类型
		String location = request.getParameter("location"); // 设备位置
		String department = request.getParameter("department"); // 部门
		String serviceLevel = request.getParameter("serviceLevel"); // 服务级别
		String responsibleEngineerID = request.getParameter("responsibleEngineer");
		String reserveEnginnerID = request.getParameter("reserveEnginner");
		String IP = request.getParameter("IP"); // 设备IP
		String linkman = request.getParameter("linkman"); // 联系人
		String linkmanTel = request.getParameter("linkmanTel"); // 联系人电话
		String bwReader = request.getParameter("bwReader"); // 黑白读数
		String colorReader = request.getParameter("colorReader"); // 彩色读数
		String installedTime2 = request.getParameter("installedTime"); // 安装日期
		String secret = request.getParameter("secret"); // 是否涉密
		String secretLevel = request.getParameter("secretLevel"); // 涉密等级
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("是否涉密", secret);
		params.put("变动类型", changeType);
		params.put("责任工程师姓名", responsibleEngineerID);
		params.put("后备工程师姓名", reserveEnginnerID);
		params.put("保管部门", department);
		params.put("设备位置", location);
		params.put("装机日期", installedTime2);
		// 判断涉密栏是否统一
		// 判断其他栏目是否存在空值
		// 找出登录人信息
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (customerManage.selectByCode(machCode) == null) {
			json.put("code", 1);
			json.put("msg", "您输入的机器编码不存在");
			return json;
		}
		if (secret.equals("true")) {
			if (secretLevel == null || secretLevel.equals("")) {
				json.put("code", 1);
				json.put("msg", "涉密等级不能为空");
				return json;
			}
		}
		if (secret.equals("false")) {
			if (secretLevel != null && !secretLevel.equals("")) {
				json.put("code", 1);
				json.put("msg", "当选择不涉密时，无需勾选涉密等级");
				return json;
			}
		}
		if (secret != null && secret.equals("true")) {
			secret = "是";
			if (secretLevel != null && secretLevel.equals("1")) {
				secretLevel = "秘密";
			} else if (secretLevel != null && secretLevel.equals("2")) {
				secretLevel = "机密";
			} else if (secretLevel != null && secretLevel.equals("3")) {
				secretLevel = "绝密";
			} else {
				json.put("code", 1);
				json.put("msg", "涉密等级不能为空");
				return json;
			}
		} else {
			secret = "否";
		}
		StaffInfo responsibleEngineer = null;
		StaffInfo reserveEnginner = null;
		// 将传递过来的责任工程师ID和后备工程师ID转换为姓名
		for (StaffInfo staff : staffInfoServiceImplnew.selectAllStaff()) {
			if (staff.getName().equals(responsibleEngineerID)) {
				responsibleEngineer = staff;
			}
			if (staff.getName().equals(reserveEnginnerID)) {
				reserveEnginner = staff;
			}
			if (reserveEnginner != null && responsibleEngineer != null) {
				continue;
			}
		}

		if (responsibleEngineer == null) {
			json.put("code", 1);
			json.put("msg", "对不起，您输入的责任工程师姓名不存在，请重新输入");
			return json;
		}
		if (reserveEnginner == null) {
			json.put("code", 1);
			json.put("msg", "对不起，您输入的后备工程师姓名不存在，请重新输入");
			return json;
		}
		if (!(responsibleEngineer.getPost().equals("工程师") || responsibleEngineer.getPost().equals("技术主管"))) {
			json.put("code", 1);
			json.put("msg", "对不起，员工" + responsibleEngineer.getName() + "不是工程师或技术主管，请重新输入");
			return json;
		}
		if (!(reserveEnginner.getPost().equals("工程师") || reserveEnginner.getPost().equals("技术主管"))) {
			json.put("code", 1);
			json.put("msg", "对不起，员工" + reserveEnginner.getName() + "不是工程师或技术主管，请重新输入");
			return json;
		}
		Date installedTime = null;
		try {
			installedTime = ExcelUtils.fmt.parse(installedTime2);
		} catch (ParseException e) {
			json.put("code", 1);
			json.put("msg", "装机日期输入格式不正确，请按照2018-1-1输入");
			return json;
		}
		// 执行添加方法
		Device device = new Device();
		// 如果变动类型为撤除，则清空设备的服务属性，只保留初始黑白读数和初始彩色读数
		if (changeType.equals("撤除")) {
			// 清空服务属性
			if (customerManage.cleanDeviceAttribute(machCode) > 0) {
				// 设置初始黑白读数和彩色读数
				device.setMachCode(machCode);
				device.setBwReader(bwReader);
				device.setColorReader(colorReader);
				customerManage.updateByPrimaryKeySelective(device);
				/* customerManage.insertSelective(deviceChange); */
				json.put("code", 0);
				json.put("msg", "变动成功");
				return json;
			}
		} else {
			device.setChangeType(changeType);
			device.setMachCode(machCode);
			device.setLocation(location);
			device.setDepartment(department);
			device.setServiceLevel(serviceLevel);
			device.setCustLinkman(linkman);
			device.setLinkmanPhone(linkmanTel);
			device.setResponsibleEngineer(responsibleEngineer.getName());
			device.setResponsibleEngineerID(responsibleEngineer.getStaffId());
			device.setReserveEnginner(reserveEnginner.getName());
			device.setReserveEnginnerID(reserveEnginner.getStaffId());
			device.setBwReader(bwReader);
			device.setColorReader(colorReader);
			device.setInstalledTime(installedTime);
			device.setSecret(secret);
			device.setSecretLevel(secretLevel);
			device.setIP(IP);
			// 执行变动方法
			int result = customerManage.updateByPrimaryKeySelective(device);
			if (result > 0) {
				json.put("code", 0);
				json.put("msg", "变动成功");
				return json;
			} else {
				json.put("code", 1);
				json.put("msg", "变动失败");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "变动失败");
		return json;
	}

	/**
	 * 方法：删除工单
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/deleteOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deleteOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 获取用户登录名
		if (username == null || username.equals("")) {
			json.put("code", 1);
			json.put("msg", "登录名不能为空");
			return json;
		}
		String woNumber = request.getParameter("woNumber"); // 获取工单号
		boolean a = serviceInfoService.deleteServiceInfo(woNumber);
		boolean b = customerManage.deleteOrder(woNumber);
		if (a && b) {
			// 删除该工单对应的图片
			File file = new File(SOMUtils.pictureAddr + woNumber);
			if (file.exists()) {
				SOMUtils.deleteDir(SOMUtils.pictureAddr + woNumber);
			}
			json.put("code", 0);
			json.put("msg", "删除成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "删除失败");
		return json;
	}

	/**
	 * 方法：撤除工单,恢复到受理状态
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/resetOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> resetOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 获取用户登录名
		if (username == null || username.equals("")) {
			json.put("code", 1);
			json.put("msg", "登录名不能为空");
			return json;
		}
		String woNumber = request.getParameter("woNumber"); // 获取工单号
		OrderInfo orderInfo = serviceManageServiceImpl.selectOrderByOrderNum(woNumber);
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (orderInfo == null) {
			json.put("code", 0);
			json.put("msg", "对不起，要撤回的工单不存在");
			return json;
		}
		serviceInfoService.deleteServiceInfo(woNumber);
		customerManage.deleteOrder(woNumber);
		OrderInfo orderinfo = new OrderInfo();
		if (orderInfo.getMachCode() != null) {
			// 执行插入方法
			orderinfo.setWoNumber(woNumber);
			orderinfo.setFaultType(orderInfo.getFaultType());
			orderinfo.setEsNumber(orderInfo.getEsNumber());
			orderinfo.setCustName(orderInfo.getCustName());
			orderinfo.setCustId(orderInfo.getCustId());
			orderinfo.setDevName(orderInfo.getDevName());
			orderinfo.setMachCode(orderInfo.getMachCode());
			orderinfo.setRepairMan(orderInfo.getRepairMan());
			orderinfo.setRepairService(orderInfo.getRepairService());
			orderinfo.setCustAddr(orderInfo.getCustAddr());
			orderinfo.setRepairTime(orderInfo.getRepairTime());
			orderinfo.setAccidentType(orderInfo.getAccidentType());
			orderinfo.setServiceType(orderInfo.getServiceType());
			orderinfo.setRepairType(orderInfo.getRepairType());
			orderinfo.setRemark(orderInfo.getRemark());
			orderinfo.setWoStatus("待受理");
			orderinfo.setWoProgress("待受理");
			orderinfo.setOrderAccount(orderInfo.getOrderAccount());
			boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
			if (result) {
				// 如果工单增加成功，则在增加服务评价
				ServiceInfo serviceInfo = new ServiceInfo();
				// 获取默认责任工程师名称
				serviceInfo.setWoNumber(woNumber);
				serviceInfo.setStaffId(service.getStaffId());
				serviceManageServiceImpl.insertSelective(serviceInfo);
				json.put("code", 0);
				json.put("data", orderinfo);
				json.put("msg", "撤除成功");
				return json;
			} else {
				json.put("code", 1);
				json.put("msg", "撤除失败");
				return json;
			}
		} else {
			orderinfo.setWoNumber(woNumber);
			orderinfo.setFaultType(orderInfo.getFaultType());
			orderinfo.setCustName(orderInfo.getCustName());
			orderinfo.setCustId(orderInfo.getCustId());
			orderinfo.setRepairMan(orderInfo.getRepairMan());
			orderinfo.setRepairService(orderInfo.getRepairService());
			orderinfo.setCustAddr(orderInfo.getCustAddr());
			orderinfo.setRepairTime(orderInfo.getRepairTime());
			orderinfo.setAccidentType(orderInfo.getAccidentType());
			orderinfo.setServiceType(orderInfo.getServiceType());
			orderinfo.setRepairType(orderInfo.getRepairType());
			orderinfo.setRemark(orderInfo.getRemark());
			orderinfo.setWoStatus("待受理");
			orderinfo.setWoProgress("待受理");
			orderinfo.setOrderAccount(orderInfo.getOrderAccount());
			boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
			if (result) {
				// 如果工单增加成功，则在增加服务评价
				ServiceInfo serviceInfo = new ServiceInfo();
				serviceInfo.setWoNumber(woNumber);
				serviceManageServiceImpl.insertSelective(serviceInfo);
				json.put("code", 0);
				json.put("data", orderinfo);
				json.put("msg", "撤除成功");
				return json;
			} else {
				json.put("code", 1);
				json.put("msg", "撤除失败");
				return json;
			}
		}
	}

	/**
	 * 方法：修改工单
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/updateOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> updateOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username"); // 获取用户登录名
		if (username == null || username.equals("")) {
			json.put("code", 1);
			json.put("msg", "登录名不能为空");
			return json;
		}
		String woNumber = request.getParameter("woNumber"); // 获取工单号
		OrderInfo orderInfo = serviceManageServiceImpl.selectOrderByOrderNum(woNumber);
		if (orderInfo == null) {
			json.put("code", 1);
			json.put("msg", "对不起，要修改的工单不存在");
			return json;
		}
		if (orderInfo.getMachCode() != null) {
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
			List<OrderInfo> xixi = serviceManageServiceImpl.selectOrderByDynamic(null, machCode, null, null, null, null,
					null);
			for (OrderInfo orderInfo2 : xixi) {
				if (!orderInfo2.getWoStatus().equals("已关单") && !orderInfo2.getWoStatus().equals("已转单")
						&& !orderInfo2.getWoStatus().equals("已完成")) {
					if (!machCode.equals(orderInfo.getMachCode())) {
						json.put("code", 1);
						json.put("msg", "该设备已报修，正在处理中，请勿重复报修");
						return json;
					}
				}
			}
			// 10.设备名称
			String devName = device.getDevName();
			// 1.设备序列号
			String esNumber = device.getEsNumber();
			// 2.客户名称
			String custName = device.getCustArea();
			// 4.客户地址
			if (custInfoService.selectCustByBaseInfo(device.getCustArea(), null, null, null, null) == null) {
				json.put("code", 1);
				json.put("msg", "对不起，该机器对应的客户信息不存在");
			}
			String custAddr = custInfoService.selectCustByBaseInfo(device.getCustArea(), null, null, null, null).get(0)
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
			if (accidentType.equals("耗材断供")) {
				if (materialModel != null && !materialModel.trim().equals("") && materialNumber != null
						&& !materialNumber.trim().equals("")) {
					serviceType = materialModel + ":" + materialNumber;
				}
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
			int custId = custInfoService.selectCusIdByName(custName);
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
			/*
			 * // 转换服务类别 if (accidentType.equals("a")) { accidentType = "硬件故障";
			 * } else if (accidentType.equals("b")) { accidentType = "软件故障"; }
			 * else if (accidentType.equals("c")) { accidentType = "耗材断供"; }
			 */
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
			} else if (faultType.equals("事件类")) {
				repairType = "主动服务";
			} else {
				repairType = "电话";
			}
			OrderInfo upOrder = new OrderInfo();
			upOrder.setWoNumber(woNumber);
			upOrder.setFaultType(faultType);
			upOrder.setEsNumber(esNumber);
			upOrder.setCustName(custName);
			upOrder.setCustId(custId);
			upOrder.setDevName(devName);
			upOrder.setMachCode(machCode);
			upOrder.setRepairMan(repairMan);
			upOrder.setRepairService(repairService);
			upOrder.setCustAddr(custAddr);
			upOrder.setAccidentType(accidentType);
			upOrder.setServiceType(serviceType);
			upOrder.setRepairType(repairType);
			upOrder.setRemark(remark);
			if (serviceManageServiceImpl.updateOrder(upOrder)) {
				json.put("code", 0);
				json.put("msg", "修改工单成功");
				return json;
			} else {
				json.put("code", 1);
				json.put("msg", "修改工单失败");
				return json;
			}
		} else {
			String custName = request.getParameter("custName");
			if (custName == null || custName.equals("")) {
				json.put("code", 1);
				json.put("msg", "客户名称不能为空");
				return json;
			}
			// 服务区域
			String serviceArea = request.getParameter("serviceArea");
			// 根据客户名称查找客户ID
			Integer custId = custInfoService.selectCusIdByName(custName);
			if (custId == -1) {
				json.put("code", 1);
				json.put("msg", "您输入的客户名称不存在，请检查并重新输入");
				return json;
			}
			// 根据客户ID查询相应信息
			CustInfo cust = custInfoService.selectCustById(custId);
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
			// 耗材型号
			String materialModel = request.getParameter("materialModel");
			// 耗材数量
			String materialNumber = request.getParameter("materialNumber");
			// MACD
			String macd = request.getParameter("macd");
			// 服务类别详细信息
			String serviceType = null;
			if (accidentType.equals("耗材断供")) {
				if (materialModel != null && !materialModel.trim().equals("") && materialNumber != null
						&& !materialNumber.trim().equals("")) {
					serviceType = materialModel + ":" + materialNumber;
				}
			}
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
			} else {
				repairType = "电话";
			}
			OrderInfo upOrder = new OrderInfo();
			upOrder.setWoNumber(woNumber);
			upOrder.setFaultType(faultType);
			upOrder.setCustName(custName);
			upOrder.setCustId(custId);
			upOrder.setRepairMan(repairMan);
			upOrder.setRepairService(repairService);
			upOrder.setCustAddr(custAddr);
			upOrder.setRepairTime(repairTime);
			upOrder.setAccidentType(accidentType);
			upOrder.setServiceType(serviceType);
			upOrder.setRepairType(repairType);
			upOrder.setRemark(remark);
			if (serviceManageServiceImpl.updateOrder(upOrder)) {
				json.put("code", 0);
				json.put("msg", "修改工单成功");
				return json;
			} else {
				json.put("code", 1);
				json.put("msg", "修改工单失败");
				return json;
			}
		}
	}

	/**
	 * 方法：导出设备信息
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportEquipment", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportEquipment(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		List<Device> devices = (List<Device>) export.get(request.getParameter("username") + "equipmentInfo");
		String tableName = "设备表";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "合同编码", "设备类型", "机器编码", "设备序列号", "设备型号", "设备位置", "设备品牌", "设备IP", "安装日期",
				"资产属性", "资产状态", "资产编码", "是否涉密设备" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Device dev : devices) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(dev.getCustArea());
			row.createCell(2).setCellValue(dev.getServiceArea());
			row.createCell(3).setCellValue(dev.getContractNo());
			row.createCell(4).setCellValue(dev.getDeviceType());
			row.createCell(5).setCellValue(dev.getMachCode());
			row.createCell(6).setCellValue(dev.getEsNumber());
			row.createCell(7).setCellValue(dev.getUnitType());
			row.createCell(8).setCellValue(dev.getLocation());
			row.createCell(9).setCellValue(dev.getDeviceBrand());
			row.createCell(10).setCellValue(dev.getIP());
			row.createCell(11).setCellValue(
					dev.getInstalledTime() == null ? null : ExcelUtils.fmt.format(dev.getInstalledTime()));
			row.createCell(12).setCellValue(dev.getAssetAttr());
			row.createCell(13).setCellValue(dev.getAssetStatus());
			row.createCell(14).setCellValue(dev.getAssetNumber());
			row.createCell(15).setCellValue(dev.getSecret());
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * 方法：通过excel批量导入设备信息
	 * 
	 * @param excelPath(excel文件所在路径)
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/importDevice", produces = "application/json; charset=utf-8")
	@ResponseBody
	@Transactional
	public Map<String, Object> exportDevice()
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收文件路径
		String excelPath = request.getParameter("excelPath");
		File file = new File(excelPath);
		// 创建输入流，读取Excel
		InputStream is = new FileInputStream(file);
		// jxl提供的Workbook类
		Workbook wb = WorkbookFactory.create(is);
		// 只有一个sheet,直接处理
		// 创建一个Sheet对象
		Sheet sheet = wb.getSheetAt(0);
		// 总行数
		int trLength = sheet.getLastRowNum();
		Row row = sheet.getRow(0);
		// 得到所有的列数
		int tdLength = row.getLastCellNum();
		// 越过第一行 它是列名称
		for (int i = 1; i <= trLength; i++) {
			String[] object = new String[tdLength];
			Device device = new Device();
			Row row1 = sheet.getRow(i);
			for (int j = 0; j < tdLength; j++) {
				Cell cell1 = row1.getCell(j);
				if (cell1 != null) {
					cell1.setCellType(Cell.CELL_TYPE_STRING);
					object[j] = cell1.getStringCellValue();
				}
			}
			device.setAssetAttr(object[0]);
			device.setHoldDepartment(object[1]);
			device.setHoleMan(object[2]);
			device.setAssetNumber(object[3]);
			device.setAssetClass(object[4]);
			device.setSpecifications(object[5]);
			device.setDevName(object[6]);
			device.setMachCode(object[7]);
			device.setDeviceType(object[8]);
			device.setUnitType(object[9]);
			device.setEsNumber(object[10]);
			device.setDeviceBrand(object[11]);
			device.setOutputSpec(object[12]);
			device.setDeviceBound(object[13]);
			device.setSecret(object[14]);
			device.setSecretLevel(object[15]);
			device.setResponsibleEngineer(object[16]);
			device.setReserveEnginner(object[17]);
			device.setDepartment(object[18]);
			device.setLocation(object[19]);
			device.setServiceLevel(String.valueOf(object[20]));
			device.setBwReader(object[21]);
			device.setColorReader(object[22]);
			device.setInstalledTime(ExcelUtils.fmt.parse(object[23]));
			int result = customerManage.insert(device);
			if (result <= 0) {
				json.put("code", 1);
				json.put("msg", "添加失败");
				return json;
			}
			if (device.getAssetAttr().equals("迅维") && result > 0) {
				AssetNumber assetNumber = new AssetNumber();
				assetNumber.setAssetNumber(device.getAssetNumber());
				boolean result1 = asSetNumberService.insert(assetNumber);
				if (!result1) {
					json.put("code", 1);
					json.put("msg", "添加失败");
					return json;
				}
			}
		}
		json.put("code", 0);
		json.put("msg", "添加成功");
		return json;
	}

	@Test
	public void test() throws IOException {
		List<Device> devices = customerManage.selectByDynamic(null, null, null, null, null, null, null);
		for (Device device : devices) {
			String content = "https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%2fgetOpenId%3fmachCode%3d"
					+ device.getMachCode()
					+ "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
			File file = new File("E:/som/src/main/webapp/qrcode/" + device.getMachCode() + ".png");
			if (!file.exists()) {
				file.createNewFile();
			}
			ZXingUtils.qrCode(content, "E:/som/src/main/webapp/qrcode/" + device.getMachCode() + ".png");
		}
	}

	@Test
	public void excel() {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;// 图片
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			String string = "data:image/png;base64,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";
			byte[] b = decoder.decodeBuffer(string.substring(22));
			XSSFWorkbook wb1 = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "kpi/operation.xlsx");
			XSSFSheet sheet1 = wb1.getSheet("Sheet1");
			XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
			XSSFClientAnchor anchor1 = new XSSFClientAnchor(0, 0, 0, 0, (short) 11, 1, (short) 20, 15);
			XSSFClientAnchor anchor2 = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 17, (short) 10, 30);
			XSSFDrawing patriarch = sheet1.createDrawingPatriarch();
			patriarch.createPicture(anchor, wb1.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
			patriarch.createPicture(anchor1, wb1.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
			patriarch.createPicture(anchor2, wb1.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
			fileOut = new FileOutputStream(SOMUtils.qrAddr + "kpi/operation.xlsx"); // 写入excel文件
			wb1.write(fileOut);
			/*
			 * OutputStream out = new FileOutputStream(
			 * "C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/tupian.png");
			 * out.write(b); out.flush(); out.close();
			 */
			/*
			 * // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
			 * ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			 * // 将图片读到BufferedImage bufferImg = ImageIO.read(new File(
			 * "C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/tupian.png"));
			 * // 将图片写入流中 ImageIO.write(bufferImg, "png", byteArrayOut); //
			 * 创建一个工作薄 HSSFWorkbook wb = new HSSFWorkbook(); // 创建一个sheet
			 * HSSFSheet sheet = wb.createSheet("out put excel"); //
			 * 利用HSSFPatriarch将图片写入EXCEL HSSFPatriarch patriarch =
			 * sheet.createDrawingPatriarch();
			 *//**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 *//*
				 * // 图片一导出到单元格B2中 HSSFClientAnchor anchor = new
				 * HSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				 * // 插入图片 patriarch.createPicture(anchor, wb.addPicture(b,
				 * HSSFWorkbook.PICTURE_TYPE_JPEG)); // 生成的excel文件地址 fileOut =
				 * new FileOutputStream(
				 * "C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/xixi.xls")
				 * ; // 写入excel文件 wb.write(fileOut);
				 */
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
	}

	@RequestMapping(value = "/exportCustomerKpiSummary1", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportCustomerKpiSummary(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;// 图片
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			String string = request.getParameter("picture");
			byte[] b = decoder.decodeBuffer(string.substring(22));
			/*
			 * OutputStream out = new FileOutputStream(
			 * "C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/tupian.png");
			 * out.write(b); out.flush(); out.close();
			 */
			XSSFWorkbook wb1 = ExcelUtils.copyExcel2007(SOMUtils.qrAddr + "picture.xlsx");
			wb1.createSheet("Sheet2");
			wb1.removeSheetAt(wb1.getSheetIndex("Sheet1"));
			wb1.setSheetName(wb1.getSheetIndex("Sheet2"), "Sheet1");
			XSSFSheet sheet1 = wb1.getSheet("Sheet1");
			XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
			XSSFDrawing patriarch = sheet1.createDrawingPatriarch();
			patriarch.createPicture(anchor, wb1.addPicture(b, XSSFWorkbook.PICTURE_TYPE_JPEG)); //
			fileOut = new FileOutputStream(SOMUtils.qrAddr + "picture.xlsx"); // 写入excel文件
			wb1.write(fileOut);
			json.put("code", 0);
			json.put("msg", SOMUtils.ipAndPort + "picture.xlsx");
			/*
			 * File file=new File(
			 * "C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/tupian.png");
			 * FileInputStream fis = new FileInputStream(file);
			 * ByteArrayOutputStream bos = new ByteArrayOutputStream(); byte[] c
			 * = new byte[1024]; int len = -1; while((len = fis.read(c)) != -1)
			 * { bos.write(c, 0, len); } byte[] fileByte = bos.toByteArray(); //
			 * 将图片写入流中 ImageIO.write(bufferImg, "png", byteArrayOut); //
			 * 创建一个sheet //
			 *//**
				 * 该构造函数有8个参数
				 * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
				 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
				 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
				 * 
				 *//*
				 * // 图片一导出到单元格B2中 XSSFClientAnchor anchor =new
				 * XSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 10, 15);
				 * // 插入图片 patriarch.createPicture(anchor,
				 * wb1.addPicture(fileByte, HSSFWorkbook.PICTURE_TYPE_JPEG)); //
				 * 生成的excel文件地址 fileOut = new FileOutputStream(
				 * "C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/xixi.xlsx"
				 * ); // 写入excel文件 wb1.write(fileOut);
				 */
			/* ExcelUtils.download(res, wb, tableName); */
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

	@RequestMapping("/xixi")
	@ResponseBody
	public Double test123() throws Exception {
		Date s = ExcelUtils.fmtOne.parse("2019-04-28 13:30:00");
		Date e = ExcelUtils.fmtOne.parse("2019-04-29 14:30:00");
		return CalendarTool.getDownTime(s, e, s, e);

		// 日历集合
		/*
		 * List<Calendars> Calendars = calendarService.selectAllCalendar(); //
		 * 假期集合 List<String> weekDays=new ArrayList<>(); // 补班集合 List<String>
		 * needWordDays=new ArrayList<>(); for (Calendars calendar : Calendars)
		 * { if (calendar.getSign() == 1) {
		 * weekDays.add(ExcelUtils.fmt.format(calendar.getCalendar())); }else{
		 * needWordDays.add(ExcelUtils.fmt.format(calendar.getCalendar())); } }
		 * System.out.println(weekDays.toString());
		 */
	}

	/**
	 * 方法：导出设备信息
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportEquipment1", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportEquipment1(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		List<Device> devices = customerManage.selectByDevice(null, "深圳分公司", null, null, null, null);
		String tableName = "设备表";
		// 设置表头
		String[] Titles = { "二维码内容", "机器编码" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Device dev : devices) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			String content = "https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%2fgetOpenId%3fmachCode%3d"
					+ dev.getMachCode()
					+ "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(content);
			row.createCell(2).setCellValue(dev.getMachCode());
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

}
