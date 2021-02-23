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
import java.util.Calendar;
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
		} else if (userRole.getRoleId().equals("优质运维专员") || userRole.getRoleId().equals("运维管理人员")) {
			user.setCustName("");
		}
		else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
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
		String assetAscription=request.getParameter("assetAscription");
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
		} else if (userRole.getRoleId().equals("优质运维专员") || userRole.getRoleId().equals("运维管理人员")) {
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
							identifier,assetAscription);
					selectContracts = customerManage.selectByCust(user.getCustName(), null, "", "", page, limit, null,
							order, identifier,assetAscription);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null,
							order, identifier,assetAscription);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null,
							order, identifier,assetAscription);
				} else {
					contracts = customerManage.selectByCust(null, user.getCustName(), "", "", null, null, null, order,
							identifier,assetAscription);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "", "", page, limit, null,
							order, identifier,assetAscription);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null,
							order, identifier,assetAscription);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null,
							order, identifier,assetAscription);
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
							identifier,assetAscription);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "", "", page, limit, null,
							order, identifier,assetAscription);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null,
							order, identifier,assetAscription);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null,
							order, identifier,assetAscription);
				} else {
					contracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null, order,
							identifier,assetAscription);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", page, limit, null,
							order, identifier,assetAscription);
					// 查找到期合同
					timeContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null,
							order, identifier,assetAscription);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null,
							order, identifier,assetAscription);
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
							identifier,assetAscription);
					selectContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", page, limit, null,
							order, identifier,assetAscription);
					timeContracts = customerManage.selectByCust(user.getCustName(), null, "1", "", null, null, null,
							order, identifier,assetAscription);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(user.getCustName(), null, "", "1", null, null, null,
							order, identifier,assetAscription);
				} else {
					contracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null, order,
							identifier,assetAscription);
					selectContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", page, limit, null,
							order, identifier,assetAscription);
					timeContracts = customerManage.selectByCust(null, user.getCustName(), "1", "", null, null, null,
							order, identifier,assetAscription);
					// 查找一年内到期合同
					dueToContracts = customerManage.selectByCust(null, user.getCustName(), "", "1", null, null, null,
							order, identifier,assetAscription);

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
					order, identifier,assetAscription);
			contracts = customerManage.selectByCust(custName, serviceArea, "", "", null, null, contractNature, order,
					identifier,assetAscription);
			// 查找到期合同
			timeContracts = customerManage.selectByCust(custName, serviceArea, "1", "", null, null, null, order,
					identifier,assetAscription);
			// 查找一年内到期合同
			dueToContracts = customerManage.selectByCust(custName, serviceArea, "", "1", null, null, null, order,
					identifier,assetAscription);
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
				"离到期天数", "执行状态","备注" };
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
			row.createCell(12).setCellValue(contract.getAssetAscription());
			row.createCell(13).setCellValue("");
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
		} else if (userRole.getRoleId().equals("优质运维专员") || userRole.getRoleId().equals("运维管理人员")) {
			user.setCustName("");
		}
		else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
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
		String assetAscription = request.getParameter("assetAscription"); // 执行状态
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
		contract.setAssetAscription(assetAscription);
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

	/**
	 * 初始化图片
	 * @throws IOException
	 */
	@RequestMapping(value = "/initPicture", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> initPicture() throws IOException {
		Map<String, Object> json = new HashMap<>();
		List<Device> devices = customerManage.selectByDynamic(null, null, null, null, null, null, null);
		for (Device device : devices) {
			String content = "https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%2fgetOpenId%3fmachCode%3d"
					+ device.getMachCode()
					+ "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
			File file = new File("/root/tomcat/apache-tomcat-8.0.53/webapps/som/qrcode/" + device.getMachCode() + ".png");
			if (!file.exists()) {
				file.createNewFile();
			}
			ZXingUtils.qrCode(content, "/root/tomcat/apache-tomcat-8.0.53/webapps/som/qrcode/" + device.getMachCode() + ".png");
		}
		json.put("code", 0);
		json.put("msg", "初始化图片成功");
		return json;
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
