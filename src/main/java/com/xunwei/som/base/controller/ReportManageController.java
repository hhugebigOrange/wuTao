/*package com.xunwei.som.base.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.xunwei.som.calendar.CalendarTool;
import com.xunwei.som.pojo.BranchKpi;
import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.CustomerKpi;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.EngineerKpi;
import com.xunwei.som.pojo.HeadOfficeKpi;
import com.xunwei.som.pojo.MaintenancePerform;
import com.xunwei.som.pojo.MaintenanceSummary;
import com.xunwei.som.pojo.SeatServiceKpi;
import com.xunwei.som.pojo.StandardRate;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.pojo.Maintenance;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.permissions.ParameterSetting;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.CustomerManageService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.MaintenanceserviceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;
import sun.misc.BASE64Decoder;

*//**
 * KPI管理
 * 
 * @author Administrator
 *
 *//*

@Controller
public class ReportManageController extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private MaintenanceserviceImpl maintenanceserviceImpl = new MaintenanceserviceImpl();

	private CustomerManageService customerManageService = new CustomerManageServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();
	// 用来存放每次查询的客户满意度结果集
	private List<ServiceInfo> serviceInfos;
	// 用来存放每次查询设备管理报表的结果集
	private List<Device> devices;
	// 用来存放每次查询设备保养状态的结果集
	private List<Maintenance> maintenances;
	// 用来存放每次查询合同报表的结果集
	private List<Contract> contracts;
	// 用来存放每次查询保养汇总的结果集
	private List<MaintenanceSummary> maintenanceSummarys = new ArrayList<>();
	// 用来存放汇总达标率结果集
	private List<StandardRate> StandardRates = new ArrayList<>();

	private List<EngineerKpi> engineerKpis = new ArrayList<>();

	private List<CustomerKpi> CustomerKpis = new ArrayList<>();

	private List<BranchKpi> BranchKpis = new ArrayList<>();

	private List<HeadOfficeKpi> HeadOfficeKpis = new ArrayList<>();

	private List<SeatServiceKpi> SeatServiceKpis = new ArrayList<>();

	private String sDate;

	private String eDate;

	// 工程师kpi↓***************************************************************************************************8

	*//**
	 * 工程师Kpi接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/engineerKpi", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> engineerKpi() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String staffId = request.getParameter("staffId");
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
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		// 先查询出所有已完成的服务工单
		List<ServiceInfo> newServiceInfo = serviceInfoService.selectServiceInfoByengineerKpi(staffId, "", "", startDate,
				endDate, faultType);
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : newServiceInfo) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		for (ServiceInfo serviceInfo : newServiceInfo) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setStaffName(serviceInfo.getStaffInfo().getName());
			engineerKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			engineerKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			engineerKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应用时
			engineerKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			// 响应时间是否达标
			if (engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			engineerKpi.setArrTime(serviceInfo.getArrTime());
			// 到达现场用时
			engineerKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			engineerKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			engineerKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			// 二次上门率
			engineerKpi.setOrderNum(1);
			engineerKpi.setOrderTurnNum(0);
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			// 客户投诉
			// 客户表扬
			// 年度满意
			engineerKpis.add(engineerKpi);
		}
		this.engineerKpis = engineerKpis;
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
		json.put("msg", "工程师kpi数据");
		json.put("count", engineerKpis.size());
		json.put("data", page == null || engineerKpis == null ? engineerKpis : engineerKpis.subList(page, limit));
		return json;
	}
	
	*//**
	 * 工程师汇总KPI接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/engineerKpiSummary", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> engineerKpiSummary() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收数据
		String staffId = request.getParameter("staffId");
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
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		List<StaffInfo> staffInfos = staffInfoServiceImplnew.selectStaffByOrder();
		// 先查询出所有已完成的服务工单
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi(staffId, "", "", startDate,
				endDate, faultType);
		// 循环给orderInfo和staffInfo赋值
		List<EngineerKpi> engineerKpis = new ArrayList<>();
		List<StandardRate> StandardRates = new ArrayList<>();
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 对每个工程师的kpi指标赋值
			EngineerKpi engineerKpi = new EngineerKpi();
			engineerKpi.setWoNumber(serviceInfo.getWoNumber());
			engineerKpi.setStaffName(serviceInfo.getStaffInfo().getName());
			engineerKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			engineerKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			engineerKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应用时
			engineerKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			// 响应时间是否达标
			if (engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				engineerKpi.setResponseTimeRate("是");
			} else {
				engineerKpi.setResponseTimeRate("否");
			}
			engineerKpi.setArrTime(serviceInfo.getArrTime());
			// 到达现场用时
			engineerKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				engineerKpi.setArrTimeRate("是");
			} else {
				engineerKpi.setArrTimeRate("否");
			}
			engineerKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			engineerKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				engineerKpi.setProbSolveRate("是");
			} else {
				engineerKpi.setProbSolveRate("否");
			}
			// 二次上门率
			engineerKpi.setOrderNum(1);
			engineerKpi.setOrderTurnNum(0);
			engineerKpi.setCustScore(serviceInfo.getCustScore());
			// 客户投诉
			// 客户表扬
			// 年度满意
			engineerKpis.add(engineerKpi);
		}
		for (StaffInfo staffInfo : staffInfos) {
			double responseTimeRate = 0.0;// 响应时间达标率
			double arrTimeRate = 0.0;// 到达时间达标率
			double probSolveRate = 0.0;// 问题解决时间达标率
			double orderNum = 0.0;// 属于该工程师的工单数量
			double custSatisfaction = 0.0; // 客户满意度
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
					if (engineerKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (engineerKpi.getCustScore() != 0.0) {
						custSatisfaction = custSatisfaction + engineerKpi.getCustScore();
					}
				}
			}
			if (orderNum == 0) {
				continue;
			} else {
				StandardRate StandardRate = new StandardRate();
				StandardRate.setStaffName(staffInfo.getName());
				System.out.println("响应时间达标率：" + responseTimeRate + "总数量:" + orderNum);
				StandardRate.setResponseTimeRate(responseTimeRate / orderNum * 100);
				StandardRate.setArrTimeRate(arrTimeRate / orderNum * 100);
				StandardRate.setProbSolveRate(probSolveRate / orderNum * 100);
				StandardRate.setSecondService(0);
				StandardRate.setOrderTurnNum(0);
				StandardRate.setMaintenanceRate(0);
				StandardRate.setOrderNum(orderNum);
				StandardRate.setCustComplaints(0);
				StandardRate.setCustomerSatisfaction(custSatisfaction / orderNum);
				StandardRates.add(StandardRate);
			}
		}
		this.StandardRates = StandardRates;
		json.put("code", 0);
		json.put("msg", "工程师汇总kpi数据");
		json.put("count", StandardRates.size());
		json.put("data", StandardRates);
		return json;
	}

	*//**
	 * 导出工程师kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping(value = "/exportEngineerKpi", produces = "application/json; charset=utf-8")
	@ResponseBody
	public void exportEngineerKpi(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "工程师KPI报表";
		// 设置表头
		String[] Titles = { "工单号码", "工程师", "受理地点(时间点)", "派单时间", "响应时间(时间点)", "响应时间(时间段)", "响应时间达标率", "到达时间(时间点)",
				"到达用时(时间段)", "到达用时达标率", "问题解决时间(时间点)", "问题解决用时(时间段)", "问题解决达标率", "二次上门率", "工单数量", "转单数量", "客户评价分数",
				"客户投诉(次)", "客户表扬(次)", "年度客户满意度调查结果" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (EngineerKpi engineerKpi : engineerKpis) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(engineerKpi.getWoNumber());
			row.createCell(2).setCellValue(engineerKpi.getStaffName());
			row.createCell(3).setCellValue(ExcelUtils.fmtOne.format(engineerKpi.getOrderTime()));
			row.createCell(4).setCellValue(ExcelUtils.fmtOne.format(engineerKpi.getSendTime()));
			row.createCell(5).setCellValue(ExcelUtils.fmtOne.format(engineerKpi.getTelRepon()));
			row.createCell(6).setCellValue(engineerKpi.getResponseTime());
			row.createCell(7).setCellValue(engineerKpi.getResponseTimeRate());
			row.createCell(8).setCellValue(ExcelUtils.fmtOne.format(engineerKpi.getArrTime()));
			row.createCell(9).setCellValue(engineerKpi.getArrTimeSlot());
			row.createCell(10).setCellValue(engineerKpi.getArrTimeRate());
			row.createCell(11).setCellValue(ExcelUtils.fmtOne.format(engineerKpi.getProbSolve()));
			row.createCell(12).setCellValue(engineerKpi.getProbSolveSlot());
			row.createCell(13).setCellValue(engineerKpi.getProbSolveRate());
			row.createCell(14).setCellValue("");
			row.createCell(15).setCellValue(engineerKpi.getOrderNum());
			row.createCell(16).setCellValue(engineerKpi.getOrderTurnNum());
			row.createCell(17).setCellValue(engineerKpi.getCustScore());
			row.createCell(18).setCellValue("");
			row.createCell(19).setCellValue("");
			row.createCell(20).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	*//**
	 * 导出工程师汇总kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/expoetEngineerKpiSummary")
	public void engineerKpiSummary(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "工程师汇总KPI报表";
		// 设置表头
		String[] Titles = { "工单师姓名", "相应时间达标率", "到达时间达标率", "问题解决时间达标率", "二次上门率", "转单数量", "保养完成率", "工单数量", "客户投诉",
				"客户满意度" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (StandardRate StandardRate : StandardRates) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(StandardRate.getStaffName());
			row.createCell(2).setCellValue(StandardRate.getResponseTimeRate() + "%");
			row.createCell(3).setCellValue(StandardRate.getArrTimeRate() + "%");
			row.createCell(4).setCellValue(StandardRate.getProbSolveRate() + "%");
			row.createCell(5).setCellValue(StandardRate.getSecondService() + "%");
			row.createCell(6).setCellValue(StandardRate.getOrderTurnNum());
			row.createCell(7).setCellValue(StandardRate.getMaintenanceRate());
			row.createCell(8).setCellValue(StandardRate.getOrderNum());
			row.createCell(9).setCellValue(StandardRate.getCustComplaints());
			row.createCell(10).setCellValue(StandardRate.getCustomerSatisfaction());
		}

		ExcelUtils.download(res, wb, tableName);
	}

	// 客户kpi↓**************************************************************************************************

	*//**
	 * 客户Kpi接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/customerKpi")
	@ResponseBody
	public Map<String, Object> customerKpi(ModelAndView modelAndView) throws Exception {
		// 从前端接收数据
		if (CustomerKpis != null) {
			CustomerKpis.clear();
		}
		String custName = request.getParameter("custName");
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
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi("", custName, "", startDate, endDate,
				faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给客户KPI赋值
			CustomerKpi CustomerKpi = new CustomerKpi();
			CustomerKpi.setCustName(serviceInfo.getOrderInfo().getCustName());
			CustomerKpi.setWoNumber(serviceInfo.getWoNumber());
			CustomerKpi.setMachCode(serviceInfo.getOrderInfo().getMachCode());
			CustomerKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			CustomerKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			CustomerKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应用时
			CustomerKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			if (CustomerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				CustomerKpi.setResponseTimeRate("是");
			} else {
				CustomerKpi.setResponseTimeRate("否");
			}
			CustomerKpi.setArrTime(serviceInfo.getArrTime());
			// 到达现场用时
			CustomerKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (CustomerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				CustomerKpi.setArrTimeRate("是");
			} else {
				CustomerKpi.setArrTimeRate("否");
			}
			CustomerKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			CustomerKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (CustomerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				CustomerKpi.setProbSolveRate("是");
			} else {
				CustomerKpi.setProbSolveRate("否");
			}
			// 二次上门率
			CustomerKpi.setCustScore(serviceInfo.getCustScore());
			// 客户投诉
			// 客户表扬
			// 年度满意
			CustomerKpis.add(CustomerKpi);
		}
		modelAndView.addObject("CustomerKpis", CustomerKpis);
		json.put("code", 0);
		json.put("msg", "客户kpi数据");
		json.put("count", CustomerKpis.size());
		json.put("data", CustomerKpis);
		return json;
	}

	*//**
	 * 客户汇总KPI接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/customerKpiSummary")
	@ResponseBody
	public Map<String, Object> customerKpiSummary(ModelAndView modelAndView) throws Exception {
		if (StandardRates != null) {
			StandardRates.clear();
		}
		// 从前端接收数据
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = request.getParameterValues("faultType");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		List<OrderInfo> orderInfos = serviceInfoService.selectOrderInfoByKpi();
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi("", custName, "", startDate, endDate,
				faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			service.getOrderInfo().getCustName();
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给客户KPI赋值
			CustomerKpi CustomerKpi = new CustomerKpi();
			CustomerKpi.setCustName(serviceInfo.getOrderInfo().getCustName());
			CustomerKpi.setWoNumber(serviceInfo.getWoNumber());
			CustomerKpi.setMachCode(serviceInfo.getOrderInfo().getMachCode());
			CustomerKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			CustomerKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			CustomerKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应用时
			CustomerKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			if (CustomerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				CustomerKpi.setResponseTimeRate("是");
			} else {
				CustomerKpi.setResponseTimeRate("否");
			}
			CustomerKpi.setArrTime(serviceInfo.getArrTime());
			// 到达现场用时
			CustomerKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (CustomerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				CustomerKpi.setArrTimeRate("是");
			} else {
				CustomerKpi.setArrTimeRate("否");
			}
			CustomerKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			CustomerKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (CustomerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				CustomerKpi.setProbSolveRate("是");
			} else {
				CustomerKpi.setProbSolveRate("否");
			}
			// 二次上门率
			CustomerKpi.setCustScore(serviceInfo.getCustScore());
			// 客户投诉
			// 客户表扬
			// 年度满意
			CustomerKpis.add(CustomerKpi);
		}
		for (OrderInfo orderInfo : orderInfos) {
			double responseTimeRate = 0;// 响应时间达标率
			double arrTimeRate = 0;// 到达时间达标率
			double probSolveRate = 0;// 问题解决时间达标率
			double orderNum = 0;// 属于该公司的工单数量
			double custSatisfaction = 0.0; // 客户满意度
			for (CustomerKpi CustomerKpi : CustomerKpis) {
				// 如果名字相等，则算出各种成功率
				if (orderInfo.getCustName().equals(CustomerKpi.getCustName())) {
					orderNum++;
					if (CustomerKpi.getResponseTimeRate().equals("是")) {
						responseTimeRate++;
					}
					if (CustomerKpi.getArrTimeRate().equals("是")) {
						arrTimeRate++;
					}
					if (CustomerKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (CustomerKpi.getCustScore() != 0.0) {
						custSatisfaction = custSatisfaction + CustomerKpi.getCustScore();
					}
				}
			}
			// 创建查询的保养执行对象，用于查询
			MaintenancePerform maintenancePerform = new MaintenancePerform();
			maintenancePerform.setCustName(orderInfo.getCustName());
			maintenancePerform.setMainFrequency("月");
			// 查看当月以月为单位，某个客户保养过的设备
			double a = maintenanceserviceImpl.selectByDynamic(maintenancePerform, startDate, endDate).size();
			// 以月为单位，某个客户需要保养的设备次数
			double b = maintenanceserviceImpl.selectByCycle("月", null, "", orderInfo.getCustName()).size();
			StandardRate StandardRate = new StandardRate();
			StandardRate.setCustName(orderInfo.getCustName());
			StandardRate.setResponseTimeRate(responseTimeRate / orderNum * 101);
			StandardRate.setArrTimeRate(arrTimeRate / orderNum * 101);
			StandardRate.setProbSolveRate(probSolveRate / orderNum * 101);
			if (b == 0.0) {
				StandardRate.setMaintenanceRate(0.0);
			} else {
				StandardRate.setMaintenanceRate(a / b * 101);
			}
			StandardRate.setCustComplaints(0);
			StandardRate.setCustomerSatisfaction(custSatisfaction / orderNum);
			StandardRates.add(StandardRate);
			session.setAttribute("StandardRates", StandardRates);
		}
		json.put("code", 0);
		json.put("msg", "客户汇总kpi数据");
		json.put("count", StandardRates.size());
		json.put("data", StandardRates);
		return json;
	}

	*//**
	 * 匹配客户KPI报表图形页面
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/customerKpiReport")
	public ModelAndView KpiReport(ModelAndView modelAndView) {
		modelAndView.setViewName("/reportManage/html/customerKpiReport");
		return modelAndView;
	}

	*//**
	 * 导出客户kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportCustomerKpi")
	public void exportCustomerKpi(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "工程师KPI报表";
		// 设置表头
		String[] Titles = { "客户名称", "工单号码", "机器编码", "受理地点(时间点)", "派单时间", "响应时间(时间点)", "响应时间(时间段)", "响应时间达标率",
				"到达时间(时间点)", "到达用时(时间段)", "到达用时达标率", "问题解决时间(时间点)", "问题解决用时(时间段)", "问题解决达标率", "二次上门率", "客户评价分数",
				"客户投诉(次)", "客户表扬(次)", "年度客户满意度调查结果" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (CustomerKpi CustomerKpi : CustomerKpis) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(CustomerKpi.getCustName());
			row.createCell(2).setCellValue(CustomerKpi.getWoNumber());
			row.createCell(3).setCellValue(CustomerKpi.getMachCode());
			row.createCell(4).setCellValue(ExcelUtils.fmtOne.format(CustomerKpi.getOrderTime()));
			row.createCell(5).setCellValue(ExcelUtils.fmtOne.format(CustomerKpi.getSendTime()));
			row.createCell(6).setCellValue(ExcelUtils.fmtOne.format(CustomerKpi.getTelRepon()));
			row.createCell(7).setCellValue(CustomerKpi.getResponseTime());
			row.createCell(8).setCellValue(CustomerKpi.getResponseTimeRate());
			row.createCell(9).setCellValue(ExcelUtils.fmtOne.format(CustomerKpi.getArrTime()));
			row.createCell(10).setCellValue(CustomerKpi.getArrTimeSlot());
			row.createCell(11).setCellValue(CustomerKpi.getArrTimeRate());
			row.createCell(12).setCellValue(ExcelUtils.fmtOne.format(CustomerKpi.getProbSolve()));
			row.createCell(13).setCellValue(CustomerKpi.getProbSolveSlot());
			row.createCell(14).setCellValue(CustomerKpi.getProbSolveRate());
			row.createCell(15).setCellValue("");
			row.createCell(16).setCellValue(CustomerKpi.getCustScore());
			row.createCell(17).setCellValue("");
			row.createCell(18).setCellValue("");
			row.createCell(19).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	*//**
	 * 导出客户汇总kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportCustomerKpiSummary")
	public void exportCustomerKpiSummary(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "客户汇总KPI报表";
		// 设置表头
		String[] Titles = { "客户名称", "响应时间达标率", "到达时间达标率", "问题解决时间达标率", "保养完成率", "客户投诉", "客户满意度" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (StandardRate StandardRate : StandardRates) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(StandardRate.getCustName());
			row.createCell(2).setCellValue(StandardRate.getResponseTimeRate() + "%");
			row.createCell(3).setCellValue(StandardRate.getArrTimeRate() + "%");
			row.createCell(4).setCellValue(StandardRate.getProbSolveRate() + "%");
			row.createCell(5).setCellValue(StandardRate.getMaintenanceRate());
			row.createCell(6).setCellValue(StandardRate.getCustComplaints());
			row.createCell(7).setCellValue(StandardRate.getCustomerSatisfaction());
		}
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 声明第一个sheet名称
		HSSFSheet sheet1 = wb.createSheet("客户汇总kpi柱状图");
		BASE64Decoder decoder = new BASE64Decoder();
		List<byte[]> images = new ArrayList<>();
		images.add(decoder.decodeBuffer(request.getParameter("d").substring(22)));
		// 设置宽高
		sheet1.setDefaultRowHeight((short) (350 * 30 / 25));
		sheet1.setColumnWidth((int) (400 * 1990 / 140), 0);
		// 将获取到的base64 编码转换成图片，画到excel中
		HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
		HSSFClientAnchor anchor = null;
		int index = 0;
		for (byte[] image : images) {
			anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (8 * (index % 3)), ((index / 3) * 18),
					(short) (7 + 8 * (index % 3)), 16 + ((index / 3) * 18));
			patriarch.createPicture(anchor, wb.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG));
			index++;
		}

		ExcelUtils.download(res, wb, tableName);
	}

	@RequestMapping("/kpi")
	public ModelAndView Kpi(ModelAndView modelAndView) {
		modelAndView.setViewName("/reportManage/html/kpi");
		return modelAndView;
	}

	// 分公司KPI****************************************************************************************************

	*//**
	 * 分公司Kpi接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/branchKpi")
	@ResponseBody
	public Map<String, Object> branchKpi(ModelAndView modelAndView) throws Exception {
		// 从前端接收数据
		if (BranchKpis == null) {
			BranchKpis.clear();
		}
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String compName = (String) session.getAttribute("compName");
		String[] faultType = request.getParameterValues("faultType");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi("", custName,
				SOMUtils.orderNumToComp(compName), startDate, endDate, faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给分公司KPI赋值
			BranchKpi BranchKpi = new BranchKpi();
			BranchKpi.setServiceArea(compName);
			BranchKpi.setCustName(serviceInfo.getOrderInfo().getCustName());
			BranchKpi.setWoNumber(serviceInfo.getWoNumber());
			BranchKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			BranchKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			BranchKpi.setChiefSendTime(serviceInfo.getOrderInfo().getAcceptTime());
			// 坐席受理用时
			BranchKpi.setChiefSendTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getRepairTime(),
					serviceInfo.getOrderInfo().getSendTime(), workTime, offWorkTime));
			if (BranchKpi.getChiefSendTimeSlot() <= ParameterSetting.telRepon) {
				BranchKpi.setChiefSendTimeRate("是");
			} else {
				BranchKpi.setChiefSendTimeRate("否");
			}
			BranchKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应时间段
			BranchKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			if (BranchKpi.getResponseTime() <= ParameterSetting.telRepon) {
				BranchKpi.setResponseTimeRate("是");
			} else {
				BranchKpi.setResponseTimeRate("否");
			}
			BranchKpi.setArrTime(serviceInfo.getArrTime());
			BranchKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (BranchKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				BranchKpi.setArrTimeRate("是");
			} else {
				BranchKpi.setArrTimeRate("否");
			}
			BranchKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			BranchKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (BranchKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				BranchKpi.setProbSolveRate("是");
			} else {
				BranchKpi.setProbSolveRate("否");
			}
			// 二次上门率
			BranchKpi.setCustScore(serviceInfo.getCustScore());
			BranchKpi.setOrderNum(1);
			BranchKpi.setOrderTurnNum(0);
			// 客户投诉
			// 客户表扬
			// 年度满意
			BranchKpis.add(BranchKpi);
		}
		json.put("code", 0);
		json.put("msg", "分公司kpi数据");
		json.put("count", BranchKpis.size());
		json.put("data", BranchKpis);
		return json;
	}

	*//**
	 * 分公司汇总KPI接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/branchKpiSummary")
	@ResponseBody
	public Map<String, Object> branchKpiSummary(ModelAndView modelAndView) throws Exception {
		// 从前端接收数据
		if (StandardRates == null) {
			StandardRates.clear();
		}
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String compName = (String) session.getAttribute("compName");
		String[] faultType = request.getParameterValues("faultType");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		List<OrderInfo> orderInfos = serviceInfoService.selectOrderInfoByKpi();
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi("", "", SOMUtils.orderNumToComp(compName),
				startDate, endDate, faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			service.getOrderInfo().getCustName();
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给分公司KPI赋值
			BranchKpi BranchKpi = new BranchKpi();
			BranchKpi.setServiceArea(compName);
			BranchKpi.setCustName(serviceInfo.getOrderInfo().getCustName());
			BranchKpi.setWoNumber(serviceInfo.getWoNumber());
			BranchKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			BranchKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			BranchKpi.setChiefSendTime(serviceInfo.getOrderInfo().getAcceptTime());
			// 坐席受理用时
			BranchKpi.setChiefSendTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getRepairTime(),
					serviceInfo.getOrderInfo().getSendTime(), workTime, offWorkTime));
			if (BranchKpi.getChiefSendTimeSlot() <= ParameterSetting.telRepon) {
				BranchKpi.setChiefSendTimeRate("是");
			} else {
				BranchKpi.setChiefSendTimeRate("否");
			}
			BranchKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应时间段
			BranchKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			if (BranchKpi.getResponseTime() <= ParameterSetting.telRepon) {
				BranchKpi.setResponseTimeRate("是");
			} else {
				BranchKpi.setResponseTimeRate("否");
			}
			BranchKpi.setArrTime(serviceInfo.getArrTime());
			BranchKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (BranchKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				BranchKpi.setArrTimeRate("是");
			} else {
				BranchKpi.setArrTimeRate("否");
			}
			BranchKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			BranchKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (BranchKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				BranchKpi.setProbSolveRate("是");
			} else {
				BranchKpi.setProbSolveRate("否");
			}
			// 二次上门率
			BranchKpi.setCustScore(serviceInfo.getCustScore());
			BranchKpi.setOrderNum(1);
			BranchKpi.setOrderTurnNum(0);
			// 客户投诉
			// 客户表扬
			// 年度满意
			BranchKpis.add(BranchKpi);
		}
		for (OrderInfo orderInfo : orderInfos) {
			double responseTimeRate = 0;// 响应时间达标率
			double arrTimeRate = 0;// 到达时间达标率
			double probSolveRate = 0;// 问题解决时间达标率
			double orderNum = 0;// 属于该公司的工单数量
			double custSatisfaction = 0.0; // 客户满意度
			for (BranchKpi BranchKpi : BranchKpis) {
				// 如果名字相等，则算出各种成功率
				if (orderInfo.getCustName().equals(BranchKpi.getCustName())) {
					orderNum++;
					if (BranchKpi.getResponseTimeRate().equals("是")) {
						responseTimeRate++;
					}
					if (BranchKpi.getArrTimeRate().equals("是")) {
						arrTimeRate++;
					}
					if (BranchKpi.getProbSolveRate().equals("是")) {
						probSolveRate++;
					}
					if (BranchKpi.getCustScore() != 0.0) {
						custSatisfaction = custSatisfaction + BranchKpi.getCustScore();
					}
				}
			}
			if (orderNum == 0) {
				continue;
			}
			MaintenancePerform maintenancePerform = new MaintenancePerform();
			maintenancePerform.setCustName(orderInfo.getCustName());
			maintenancePerform.setMainFrequency("月");
			// 查看当月以月为单位，某个客户保养过的设备
			double a = maintenanceserviceImpl.selectByDynamic(maintenancePerform, startDate, endDate).size();
			// 以月为单位，某个客户需要保养的设备次数
			double b = maintenanceserviceImpl.selectByCycle("月", null, "", orderInfo.getCustName()).size();
			StandardRate StandardRate = new StandardRate();
			StandardRate.setCustName(orderInfo.getCustName());
			StandardRate.setResponseTimeRate(responseTimeRate / orderNum * 101);
			StandardRate.setArrTimeRate(arrTimeRate / orderNum * 101);
			StandardRate.setProbSolveRate(probSolveRate / orderNum * 101);
			StandardRate.setMaintenanceRate(a / b * 101);
			StandardRate.setCustComplaints(0);
			StandardRate.setCustomerSatisfaction(custSatisfaction / orderNum);
			StandardRates.add(StandardRate);
		}
		session.setAttribute("StandardRates", StandardRates);
		json.put("code", 0);
		json.put("msg", "分公司汇总kpi数据");
		json.put("count", StandardRates.size());
		json.put("data", StandardRates);
		return json;
	}

	*//**
	 * 匹配分公司KPI报表图形页面
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/branchKpiReport")
	public ModelAndView branchKpiReport(ModelAndView modelAndView) {
		modelAndView.setViewName("/reportManage/html/branchKpiReport");
		return modelAndView;
	}

	*//**
	 * 导出分公司kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportBranchKpi")
	public void exportBranchKpi(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "分公司KPI报表";
		// 设置表头
		String[] Titles = { "服务区域", "客户名称", "工单号码", "受理时间(时间点)", "派单时间", "坐席受理时间(时间段)", "受理时间达标率", "响应时间(时间点)",
				"响应时间(时间段)", "响应时间达标率", "到达时间(时间点)", "到达用时(时间段)", "到达用时达标率", "问题解决时间(时间点)", "问题解决用时(时间段)", "问题解决达标率",
				"二次上门率", "工单数量", "转单数量", "客户评价分数", "客户投诉(次)", "客户表扬(次)", "年度客户满意度调查结果" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (BranchKpi BranchKpi : BranchKpis) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(BranchKpi.getServiceArea());
			row.createCell(2).setCellValue(BranchKpi.getCustName());
			row.createCell(3).setCellValue(BranchKpi.getWoNumber());
			row.createCell(4).setCellValue(ExcelUtils.fmtOne.format(BranchKpi.getOrderTime()));
			row.createCell(5).setCellValue(ExcelUtils.fmtOne.format(BranchKpi.getSendTime()));
			row.createCell(6).setCellValue(BranchKpi.getChiefSendTimeSlot());
			row.createCell(7).setCellValue(BranchKpi.getChiefSendTimeRate());
			row.createCell(8).setCellValue(ExcelUtils.fmtOne.format(BranchKpi.getTelRepon()));
			row.createCell(9).setCellValue(BranchKpi.getResponseTime());
			row.createCell(10).setCellValue(BranchKpi.getResponseTimeRate());
			row.createCell(11).setCellValue(ExcelUtils.fmtOne.format(BranchKpi.getArrTime()));
			row.createCell(12).setCellValue(BranchKpi.getArrTimeSlot());
			row.createCell(13).setCellValue(BranchKpi.getArrTimeRate());
			row.createCell(14).setCellValue(ExcelUtils.fmtOne.format(BranchKpi.getProbSolve()));
			row.createCell(15).setCellValue(BranchKpi.getProbSolveSlot());
			row.createCell(16).setCellValue(BranchKpi.getProbSolveRate());
			row.createCell(17).setCellValue("");
			row.createCell(18).setCellValue(BranchKpi.getOrderNum());
			row.createCell(19).setCellValue(BranchKpi.getOrderTurnNum());
			row.createCell(20).setCellValue(BranchKpi.getCustScore());
			row.createCell(21).setCellValue("");
			row.createCell(22).setCellValue("");
			row.createCell(23).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	*//**
	 * 导出分公司汇总kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportBranchKpiSummary")
	public void exportBranchKpiSummary(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "分公司汇总KPI报表";
		// 设置表头
		String[] Titles = { "客户名称", "响应时间达标率", "到达时间达标率", "问题解决时间达标率", "保养完成率", "客户投诉", "客户满意度" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (StandardRate StandardRate : StandardRates) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(StandardRate.getCustName());
			row.createCell(2).setCellValue(StandardRate.getResponseTimeRate() + "%");
			row.createCell(3).setCellValue(StandardRate.getArrTimeRate() + "%");
			row.createCell(4).setCellValue(StandardRate.getProbSolveRate() + "%");
			row.createCell(5).setCellValue(StandardRate.getMaintenanceRate());
			row.createCell(6).setCellValue(StandardRate.getCustComplaints());
			row.createCell(7).setCellValue(StandardRate.getCustomerSatisfaction());
		}
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 声明第一个sheet名称
		HSSFSheet sheet1 = wb.createSheet("分公司汇总kpi柱状图");
		BASE64Decoder decoder = new BASE64Decoder();
		List<byte[]> images = new ArrayList<>();
		images.add(decoder.decodeBuffer(request.getParameter("d").substring(22)));
		// 设置宽高
		sheet1.setDefaultRowHeight((short) (350 * 30 / 25));
		sheet1.setColumnWidth((int) (400 * 1990 / 140), 0);
		// 将获取到的base64 编码转换成图片，画到excel中
		HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
		HSSFClientAnchor anchor = null;
		int index = 0;
		for (byte[] image : images) {
			anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (8 * (index % 3)), ((index / 3) * 18),
					(short) (7 + 8 * (index % 3)), 16 + ((index / 3) * 18));
			patriarch.createPicture(anchor, wb.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG));
			index++;
		}

		ExcelUtils.download(res, wb, tableName);
	}

	// 总公司运维KPI****************************************************************************************************

	*//**
	 * 总公司Kpi接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/headOffice")
	@ResponseBody
	public Map<String, Object> headOffice(ModelAndView modelAndView) throws Exception {
		// 从前端接收数据
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = request.getParameterValues("faultType");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi("", custName, "", startDate, endDate,
				faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给分公司KPI赋值
			HeadOfficeKpi HeadOfficeKpi = new HeadOfficeKpi();
			HeadOfficeKpi.setServiceArea(serviceInfo.getStaffInfo().getCompName());
			HeadOfficeKpi.setCustName(serviceInfo.getOrderInfo().getCustName());
			HeadOfficeKpi.setWoNumber(serviceInfo.getWoNumber());
			HeadOfficeKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			HeadOfficeKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			HeadOfficeKpi.setChiefSendTime(serviceInfo.getOrderInfo().getAcceptTime());
			// 坐席受理用时
			HeadOfficeKpi.setChiefSendTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getRepairTime(),
					serviceInfo.getOrderInfo().getSendTime(), workTime, offWorkTime));
			if (HeadOfficeKpi.getChiefSendTimeSlot() <= ParameterSetting.telRepon) {
				HeadOfficeKpi.setChiefSendTimeRate("是");
			} else {
				HeadOfficeKpi.setChiefSendTimeRate("否");
			}
			HeadOfficeKpi.setTelRepon(serviceInfo.getTelRepon());
			// 响应时间段
			HeadOfficeKpi.setResponseTime(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime));
			if (HeadOfficeKpi.getResponseTime() <= ParameterSetting.telRepon) {
				HeadOfficeKpi.setResponseTimeRate("是");
			} else {
				HeadOfficeKpi.setResponseTimeRate("否");
			}
			HeadOfficeKpi.setArrTime(serviceInfo.getArrTime());
			HeadOfficeKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (HeadOfficeKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				HeadOfficeKpi.setArrTimeRate("是");
			} else {
				HeadOfficeKpi.setArrTimeRate("否");
			}
			HeadOfficeKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			HeadOfficeKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (HeadOfficeKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				HeadOfficeKpi.setProbSolveRate("是");
			} else {
				HeadOfficeKpi.setProbSolveRate("否");
			}
			// 二次上门率
			HeadOfficeKpi.setCustScore(serviceInfo.getCustScore());
			HeadOfficeKpi.setOrderNum(1);
			HeadOfficeKpi.setOrderTurnNum(0);
			// 客户投诉
			// 客户表扬
			// 年度满意
			HeadOfficeKpis.add(HeadOfficeKpi);
		}
		json.put("code", 0);
		json.put("msg", "总公司kpi数据");
		json.put("count", HeadOfficeKpis.size());
		json.put("data", HeadOfficeKpis);
		return json;
	}

	*//**
	 * 导出总公司kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportHeadOffice")
	public void exportHeadOffice(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "总公司KPI报表";
		// 设置表头
		String[] Titles = { "服务区域", "客户名称", "工单号码", "受理时间(时间点)", "派单时间", "坐席受理时间(时间段)", "受理时间达标率", "响应时间(时间点)",
				"响应时间(时间段)", "响应时间达标率", "到达时间(时间点)", "到达用时(时间段)", "到达用时达标率", "问题解决时间(时间点)", "问题解决用时(时间段)", "问题解决达标率",
				"二次上门率", "工单数量", "转单数量", "客户评价分数", "客户投诉(次)", "客户表扬(次)", "年度客户满意度调查结果" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (HeadOfficeKpi HeadOfficeKpi : HeadOfficeKpis) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(HeadOfficeKpi.getServiceArea());
			row.createCell(2).setCellValue(HeadOfficeKpi.getCustName());
			row.createCell(3).setCellValue(HeadOfficeKpi.getWoNumber());
			row.createCell(4).setCellValue(ExcelUtils.fmtOne.format(HeadOfficeKpi.getOrderTime()));
			row.createCell(5).setCellValue(ExcelUtils.fmtOne.format(HeadOfficeKpi.getSendTime()));
			row.createCell(6).setCellValue(HeadOfficeKpi.getChiefSendTimeSlot());
			row.createCell(7).setCellValue(HeadOfficeKpi.getChiefSendTimeRate());
			row.createCell(8).setCellValue(ExcelUtils.fmtOne.format(HeadOfficeKpi.getTelRepon()));
			row.createCell(9).setCellValue(HeadOfficeKpi.getResponseTime());
			row.createCell(10).setCellValue(HeadOfficeKpi.getResponseTimeRate());
			row.createCell(11).setCellValue(ExcelUtils.fmtOne.format(HeadOfficeKpi.getArrTime()));
			row.createCell(12).setCellValue(HeadOfficeKpi.getArrTimeSlot());
			row.createCell(13).setCellValue(HeadOfficeKpi.getArrTimeRate());
			row.createCell(14).setCellValue(ExcelUtils.fmtOne.format(HeadOfficeKpi.getProbSolve()));
			row.createCell(15).setCellValue(HeadOfficeKpi.getProbSolveSlot());
			row.createCell(16).setCellValue(HeadOfficeKpi.getProbSolveRate());
			row.createCell(17).setCellValue("");
			row.createCell(18).setCellValue(HeadOfficeKpi.getOrderNum());
			row.createCell(19).setCellValue(HeadOfficeKpi.getOrderTurnNum());
			row.createCell(20).setCellValue(HeadOfficeKpi.getCustScore());
			row.createCell(21).setCellValue("");
			row.createCell(22).setCellValue("");
			row.createCell(23).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// 坐席客服KPI****************************************************************************************************

	*//**
	 * 坐席客服Kpi接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/seatService")
	@ResponseBody
	public Map<String, Object> seatService(ModelAndView modelAndView) throws Exception {
		// 从前端接收数据
		StandardRates.clear();
		SeatServiceKpis.clear();
		String staffId = request.getParameter("staffId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = request.getParameterValues("faultType");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoBySeatServiceKpi(staffId, startDate, endDate, faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getOrderInfo().getDistributeMan()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给客服KPI赋值
			SeatServiceKpi SeatServiceKpi = new SeatServiceKpi();
			SeatServiceKpi.setStaffId(serviceInfo.getStaffInfo().getStaffId());
			SeatServiceKpi.setServiceArea(serviceInfo.getStaffInfo().getCompName());
			SeatServiceKpi.setStaffName(serviceInfo.getStaffInfo().getName());
			SeatServiceKpi.setWoNumber(serviceInfo.getOrderInfo().getWoNumber());
			SeatServiceKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			SeatServiceKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			SeatServiceKpi.setChiefSendTime(serviceInfo.getOrderInfo().getAcceptTime());
			// 坐席受理时间段
			SeatServiceKpi.setChiefSendTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getRepairTime(),
					serviceInfo.getOrderInfo().getSendTime(), workTime, offWorkTime));
			if (SeatServiceKpi.getChiefSendTimeSlot() <= ParameterSetting.telRepon) {
				SeatServiceKpi.setChiefSendTimeRate("是");
			} else {
				SeatServiceKpi.setChiefSendTimeRate("否");
			}
			SeatServiceKpi.setOrderNum(1);
			SeatServiceKpis.add(SeatServiceKpi);
		}
		json.put("code", 0);
		json.put("msg", "坐席客服kpi数据");
		json.put("count", SeatServiceKpis.size());
		json.put("data", SeatServiceKpis);
		return json;
	}

	*//**
	 * 坐席客服汇总Kpi接口
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/seatServiceSummary")
	@ResponseBody
	public Map<String, Object> seatServiceSummary(ModelAndView modelAndView) throws Exception {
		// 从前端接收数据
		StandardRates.clear();
		SeatServiceKpis.clear();
		String staffId = request.getParameter("staffId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = request.getParameterValues("faultType");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		List<OrderInfo> orderInfos = serviceInfoService.selectOrderInfoBySeat();
		// 先查询出所有已完成的服务工单
		serviceInfos = serviceInfoService.selectServiceInfoBySeatServiceKpi(staffId, startDate, endDate, faultType);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getOrderInfo().getDistributeMan()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null).get(0);
			Date workTime = custInfo.getWorkTime();
			Date offWorkTime = custInfo.getOffWorkTime();
			// 循环给客服KPI赋值
			SeatServiceKpi SeatServiceKpi = new SeatServiceKpi();
			SeatServiceKpi.setStaffId(serviceInfo.getStaffInfo().getStaffId());
			SeatServiceKpi.setServiceArea(serviceInfo.getStaffInfo().getCompName());
			SeatServiceKpi.setStaffName(serviceInfo.getStaffInfo().getName());
			SeatServiceKpi.setWoNumber(serviceInfo.getOrderInfo().getWoNumber());
			SeatServiceKpi.setOrderTime(serviceInfo.getOrderInfo().getGetOrderTime());
			SeatServiceKpi.setSendTime(serviceInfo.getOrderInfo().getSendTime());
			SeatServiceKpi.setChiefSendTime(serviceInfo.getOrderInfo().getAcceptTime());
			// 坐席受理时间段
			SeatServiceKpi.setChiefSendTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getRepairTime(),
					serviceInfo.getOrderInfo().getSendTime(), workTime, offWorkTime));
			if (SeatServiceKpi.getChiefSendTimeSlot() <= ParameterSetting.telRepon) {
				SeatServiceKpi.setChiefSendTimeRate("是");
			} else {
				SeatServiceKpi.setChiefSendTimeRate("否");
			}
			SeatServiceKpi.setOrderNum(1);
			SeatServiceKpis.add(SeatServiceKpi);
		}
		for (OrderInfo orderInfo : orderInfos) {
			double chiefSendTimeRate = 0;// 坐席客服响应时间达标率
			double arrTimeRate = 0;// 到达时间达标率
			double probSolveRate = 0;// 问题解决时间达标率
			double orderNum = 0;// 属于该坐席的工单数量
			for (SeatServiceKpi SeatServiceKpi : SeatServiceKpis) {
				// 如果名字相等，则算出各种成功率
				if (orderInfo.getDistributeMan() == SeatServiceKpi.getStaffId()) {
					orderNum++;
					if (SeatServiceKpi.getChiefSendTimeRate().equals("是")) {
						chiefSendTimeRate++;
					}
				}
			}
			MaintenancePerform maintenancePerform = new MaintenancePerform();
			maintenancePerform.setCustName(orderInfo.getCustName());
			maintenancePerform.setMainFrequency("月");
			// 查看当月以月为单位，某个客户保养过的设备
			double a = maintenanceserviceImpl.selectByDynamic(maintenancePerform, startDate, endDate).size();
			// 以月为单位，某个客户需要保养的设备次数
			double b = maintenanceserviceImpl.selectByCycle("月", null, "", orderInfo.getCustName()).size();
			StandardRate StandardRate = new StandardRate();
			System.out.println("订单数量为：" + orderNum);
			StandardRate.setStaffName(staffInfoServiceImplnew.selectStaffByNum(orderInfo.getDistributeMan()).getName());
			StandardRate.setChiefSendTimeRate(chiefSendTimeRate / orderNum * 101);
			StandardRate.setMaintenanceRate(a / b * 101);
			StandardRate.setOrderNum(orderNum);
			StandardRate.setCustComplaints(0);
			StandardRates.add(StandardRate);
		}
		session.setAttribute("StandardRates", StandardRates);
		json.put("code", 0);
		json.put("msg", "坐席客服汇总kpi数据");
		json.put("count", StandardRates.size());
		json.put("data", StandardRates);
		return json;
	}

	*//**
	 * 导出坐席客服kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportSeatService")
	public void exportSeatService(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "坐席客服KPI报表";
		// 设置表头
		String[] Titles = { "员工编号", "服务区域", "客服名称", "工单号码", "受理时间", "派单时间", "坐席受理时间(时间段)", "受理时间达标率", "工单数量", "转单数量",
				"客户投诉(次)", "年度客户满意度调查结果" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (SeatServiceKpi SeatServiceKpi : SeatServiceKpis) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(SeatServiceKpi.getStaffId());
			row.createCell(2).setCellValue(SeatServiceKpi.getServiceArea());
			row.createCell(3).setCellValue(SeatServiceKpi.getStaffName());
			row.createCell(4).setCellValue(SeatServiceKpi.getWoNumber());
			row.createCell(5).setCellValue(ExcelUtils.fmtOne.format(SeatServiceKpi.getOrderTime()));
			row.createCell(6).setCellValue(ExcelUtils.fmtOne.format(SeatServiceKpi.getSendTime()));
			row.createCell(7).setCellValue(SeatServiceKpi.getChiefSendTimeSlot());
			row.createCell(8).setCellValue(SeatServiceKpi.getChiefSendTimeRate());
			row.createCell(9).setCellValue(SeatServiceKpi.getOrderNum());
			row.createCell(10).setCellValue(SeatServiceKpi.getCustComplaints());
			row.createCell(11).setCellValue(SeatServiceKpi.getCustPraise());
			row.createCell(12).setCellValue(SeatServiceKpi.getResOfOneYear());
		}
		ExcelUtils.download(res, wb, tableName);
	}

	*//**
	 * 导出坐席客服汇总kpiExcel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportSeatServiceSummary")
	public void exportSeatServiceSummary(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "客服汇总KPI报表";
		// 设置表头
		String[] Titles = { "客服姓名", "受理时间达标率", "保养完成率", "完成数量", "客户投诉" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (StandardRate StandardRate : StandardRates) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(StandardRate.getStaffName());
			row.createCell(2).setCellValue(StandardRate.getChiefSendTimeRate());
			row.createCell(3).setCellValue(StandardRate.getMaintenanceRate());
			row.createCell(4).setCellValue(StandardRate.getOrderNum());
			row.createCell(5).setCellValue(StandardRate.getCustComplaints());
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// 工单分析表↓***********************************************************************************************************8

	*//**
	 * 工单分析表接口
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/workAnalysisTable")
	@ResponseBody
	public Map<String, Object> workAnalysisTable(ModelAndView modelAndView) {
		// 从前端接收筛选条件
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		Map<String, Object> json = new HashMap<>();
		String woNumber;
		if (serviceArea == null || "".equals(serviceArea)) {
			woNumber = "";
		} else {
			woNumber = SOMUtils.orderNumToComp(serviceArea);
		}
		String faultType = request.getParameter("faultType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		serviceInfos = serviceInfoService.selectServiceInfoByOrder(custName, woNumber, faultType, startDate, endDate);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		json.put("code", 0);
		json.put("msg", "工单分析kpi数据");
		json.put("count", serviceInfos.size());
		json.put("data", serviceInfos);
		return json;
	}

	*//**
	 * 工单分析表：到达现场时间占比图标KPI接口
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/workAnalysisArrTime")
	@ResponseBody
	public Map<String, Integer> workAnalysisArrTime(ModelAndView modelAndView) {
		Map<String, Integer> percentage = new HashMap<>();
		percentage.put("1小时以内", 0);
		percentage.put("1~2小时", 0);
		percentage.put("2~3小时", 0);
		percentage.put("3~4小时", 0);
		percentage.put("超过4小时", 0);
		int i = 0, j = 0, k = 0, l = 0, m = 0;
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getArrivalTime() == null) {
				continue;
			}
			int ArrivalTime = serviceInfo.getArrivalTime();
			if (ArrivalTime <= 60) {
				percentage.put("1小时以内", ++i);
			} else if (ArrivalTime > 60 && ArrivalTime <= 120) {
				percentage.put("1~2小时", ++j);
			} else if (ArrivalTime > 120 && ArrivalTime <= 180) {
				percentage.put("2~3小时", ++k);
			} else if (ArrivalTime > 180 && ArrivalTime >= 240) {
				percentage.put("3~4小时", ++l);
			} else {
				percentage.put("超过4小时", ++m);
			}
		}
		return percentage;
	}

	*//**
	 * 工单分析表：问题解决时间占比KPI接口
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/workAnalysisProSol")
	@ResponseBody
	public Map<String, Double> workAnalysisProSol(ModelAndView modelAndView) {
		Map<String, Double> percentage = new HashMap<>();
		percentage.put("1个工作日内", 0.0);
		percentage.put("1~2个工作日", 0.0);
		percentage.put("2个工作日以上", 0.0);
		percentage.put("4小时内完成", 0.0);
		double i = 0.0, j = 0.0, k = 0.0, l = 0.0, m = 0.0;
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getArrivalTime() == null) {
				continue;
			}
			int ArrivalTime = serviceInfo.getArrivalTime();
			if (ArrivalTime > 240 && ArrivalTime <= 480) {
				percentage.put("1个工作日内", ++i);
			} else if (ArrivalTime > 480 && ArrivalTime <= 960) {
				percentage.put("1~2个工作日", ++j);
			} else if (ArrivalTime > 960) {
				percentage.put("2个工作日以上", ++k);
			} else {
				percentage.put("4小时内完成", ++l);
			}
		}
		return percentage;
	}

	*//**
	 * 工单分析表：工程师到达现场平均用时柱状图
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/workAnalysisHistogram")
	public ModelAndView workAnalysisHistogram(ModelAndView modelAndView) {
		Map<String, Double> percentage = new HashMap<>();
		Map<String, Double> p1 = new HashMap<>();
		percentage.put("1小时以内", 0.0);
		percentage.put("1~2小时", 0.0);
		percentage.put("2~3小时", 0.0);
		percentage.put("3~4小时", 0.0);
		percentage.put("超过4小时", 0.0);
		// 统计到达现场时间占比
		double i = 0.0, j = 0.0, k = 0.0, l = 0.0, m = 0.0;
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getArrivalTime() == null) {
				continue;
			}
			int ArrivalTime = serviceInfo.getArrivalTime();
			if (ArrivalTime <= 60) {
				percentage.put("1小时以内", ++i);
			} else if (ArrivalTime > 60 && ArrivalTime <= 120) {
				percentage.put("1~2小时", ++j);
			} else if (ArrivalTime > 120 && ArrivalTime <= 180) {
				percentage.put("2~3小时", ++k);
			} else if (ArrivalTime > 180 && ArrivalTime >= 240) {
				percentage.put("3~4小时", ++l);
			} else {
				percentage.put("超过4小时", ++m);
			}
		}
		// 问题解决时间占比
		percentage.put("1个工作日内", 0.0);
		percentage.put("1~2个工作日", 0.0);
		percentage.put("2个工作日以上", 0.0);
		percentage.put("4小时内完成", 0.0);
		double i2 = 0.0, j2 = 0.0, k2 = 0.0, l2 = 0.0;
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getArrivalTime() == null) {
				continue;
			}
			int ArrivalTime = serviceInfo.getArrivalTime();
			if (ArrivalTime > 240 && ArrivalTime <= 480) {
				percentage.put("1个工作日内", ++i2);
			} else if (ArrivalTime > 480 && ArrivalTime <= 960) {
				percentage.put("1~2个工作日", ++j2);
			} else if (ArrivalTime > 960) {
				percentage.put("2个工作日以上", ++k2);
			} else {
				percentage.put("4小时内完成", ++l2);
			}
		}
		// 先找出所有的工程师
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getArrivalTime() == null) {
				continue;
			}
			p1.put(serviceInfo.getStaffInfo().getName(), 0.0);
		}
		// x轴，工程师名字
		String[] x1 = new String[serviceInfos.size()];
		// y轴，工程师平均用时
		double[] y1 = new double[serviceInfos.size()];
		int jkl = 0;
		for (Map.Entry<String, Double> entry : p1.entrySet()) {
			int i3 = 0;
			for (ServiceInfo serviceInfo : serviceInfos) {
				if (serviceInfo.getArrivalTime() == null) {
					continue;
				}
				// 如果名字相同，则总用时在原基础上增加用时
				if (entry.getKey().equals(serviceInfo.getStaffInfo().getName())) {
					i3++;
					p1.put(entry.getKey(), serviceInfo.getArrivalTime() + p1.get(entry.getKey()));
				}
			}
			// 平均用时=总用时/单的数量,保留两位小数
			if (i3 == 0) {
				continue;
			}
			BigDecimal bg = new BigDecimal(p1.get(entry.getKey()) / i3);
			Double arrTime = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			x1[jkl] = entry.getKey();
			y1[jkl] = arrTime;
			jkl++;
		}

		// x轴，工程师名字
		String[] x = new String[jkl];
		// y轴，工程师平均用时
		double[] y = new double[jkl];
		for (int num = 0; num < jkl; num++) {
			x[num] = x1[num];
			y[num] = y1[num];
		}
		String a = Arrays.toString(x);
		String b = Arrays.toString(y);
		modelAndView.addObject("percentages", percentage);
		session.setAttribute("x", a.substring(1, a.length() - 1));
		session.setAttribute("y", b.substring(1, b.length() - 1));
		modelAndView.setViewName("/reportManage/html/WorkAnalysisKpiTable");
		return modelAndView;
	}

	*//**
	 * 导出工单分析表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportWorkAnalysisTable")
	public void exportWorkAnalysisTable(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "工单分析表";
		// 设置表头
		String[] Titles = { "工单号码", "客户名称", "服务区域", "设备名称", "机器编码", "服务类型", "服务类别", "服务级别", "报修时间", "受理时间段", "派单时间",
				"电话响应时间", "到达现场时间", "到达现场耗时", "问题解决时间", "工单状态", "维修结果", };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (ServiceInfo serviceInfo : serviceInfos) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(serviceInfo.getOrderInfo().getWoNumber());
			row.createCell(2).setCellValue(serviceInfo.getOrderInfo().getCustName());
			row.createCell(3)
					.setCellValue(SOMUtils.CompToOrderNumTo(serviceInfo.getOrderInfo().getWoNumber().substring(0, 2)));
			row.createCell(4).setCellValue(serviceInfo.getOrderInfo().getDevName());
			row.createCell(5).setCellValue(serviceInfo.getOrderInfo().getMachCode());
			row.createCell(6).setCellValue(serviceInfo.getOrderInfo().getFaultType());
			row.createCell(7).setCellValue(serviceInfo.getOrderInfo().getAccidentType());
			row.createCell(8).setCellValue(serviceInfo.getOrderInfo().getPriority());
			row.createCell(9).setCellValue(ExcelUtils.fmtOne.format(serviceInfo.getOrderInfo().getRepairTime()));
			row.createCell(10).setCellValue("");
			row.createCell(11).setCellValue(ExcelUtils.fmtOne.format(serviceInfo.getOrderInfo().getSendTime()));
			row.createCell(12).setCellValue(
					serviceInfo.getTelRepon() == null ? "" : ExcelUtils.fmtOne.format(serviceInfo.getTelRepon()));
			row.createCell(13).setCellValue(
					serviceInfo.getArrTime() == null ? "" : ExcelUtils.fmtOne.format(serviceInfo.getArrTime()));
			row.createCell(14).setCellValue(serviceInfo.getArrivalTime() == null ? 0 : serviceInfo.getArrivalTime());
			row.createCell(15).setCellValue(
					serviceInfo.getProbSolve() == null ? "" : ExcelUtils.fmtOne.format(serviceInfo.getProbSolve()));
			row.createCell(16).setCellValue(serviceInfo.getOrderInfo().getWoStatus());
			row.createCell(17).setCellValue(serviceInfo.getMainResults() == null ? "" : serviceInfo.getMainResults());
		}
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 声明第一个sheet名称
		HSSFSheet sheet1 = wb.createSheet("工单分析显示图表");
		BASE64Decoder decoder = new BASE64Decoder();
		List<byte[]> images = new ArrayList<>();
		images.add(decoder.decodeBuffer(request.getParameter("f1").substring(22)));
		images.add(decoder.decodeBuffer(request.getParameter("f2").substring(22)));
		images.add(decoder.decodeBuffer(request.getParameter("f3").substring(22)));
		// 设置宽高
		sheet1.setDefaultRowHeight((short) (350 * 30 / 25));
		sheet1.setColumnWidth((int) (400 * 1990 / 140), 0);
		// 将获取到的base64 编码转换成图片，画到excel中
		HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
		HSSFClientAnchor anchor = null;
		int index = 0;
		for (byte[] image : images) {
			anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (8 * (index % 3)), ((index / 3) * 18),
					(short) (7 + 8 * (index % 3)), 16 + ((index / 3) * 18));
			patriarch.createPicture(anchor, wb.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG));
			index++;
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// 维修报表↓*************************************************************************************************
	*//**
	 * 匹配维修报表页面
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/repairReport")
	@ResponseBody
	public Map<String, Object> repairReport(ModelAndView modelAndView) {
		String faultType = request.getParameter("faultType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		serviceInfos = serviceInfoService.selectServiceInfoByOrder("", "", faultType, startDate, endDate);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			service.setDevice(customerManageService.selectDeviceById(service.getOrderInfo().getMachCode()));
		}
		json.put("code", 0);
		json.put("msg", "维修报表数据");
		json.put("count", serviceInfos.size());
		json.put("data", serviceInfos);
		return json;
	}

	*//**
	 * 故障汇总饼图
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	
	 * @RequestMapping("/repairPie")
	 * 
	 * @ResponseBody public Map<String, Integer> repairPie(ModelAndView
	 * modelAndView) { Map<String, Integer> percentage = new HashMap<>();
	 * percentage.put("事件类", 0); percentage.put("事故类", 0); percentage.put("需求类",
	 * 0); int i = 0, j = 0, l = 0; for (serviceInfo serviceInfo : serviceInfos)
	 * { if (serviceInfo.getOrderInfo().getFaultType().equals("事件类")) {
	 * percentage.put("事件类", ++i); } if
	 * (serviceInfo.getOrderInfo().getFaultType().equals("事故类")) {
	 * percentage.put("事故类", ++j); } if
	 * (serviceInfo.getOrderInfo().getFaultType().equals("需求类")) {
	 * percentage.put("需求类", ++l); } } modelAndView.addObject("percentage",
	 * percentage); return percentage; }
	 

	*//**
	 * 匹配维修报表饼图
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/repairPie")
	public ModelAndView repairPie(ModelAndView modelAndView) {
		Map<String, Integer> percentage = new HashMap<>();
		percentage.put("事件类", 0);
		percentage.put("事故类", 0);
		percentage.put("需求类", 0);
		int i = 0, j = 0, l = 0;
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getOrderInfo().getFaultType().equals("事件类")) {
				percentage.put("事件类", ++i);
			}
			if (serviceInfo.getOrderInfo().getFaultType().equals("事故类")) {
				percentage.put("事故类", ++j);
			}
			if (serviceInfo.getOrderInfo().getFaultType().equals("需求类")) {
				percentage.put("需求类", ++l);
			}
		}
		modelAndView.addObject("percentage", percentage);
		modelAndView.setViewName("/reportManage/html/RepairKpiReport");
		return modelAndView;
	}

	*//**
	 * 导出维修报表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportRepairReport")
	public void exportRepairReport(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "维修报表";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "工单号码", "机器编码", "设备型号", "设备品牌", "客户地址", "报修时间", "完成时间", "故障类型", "故障描述",
				"处理描述", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (ServiceInfo serviceInfo : serviceInfos) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(serviceInfo.getOrderInfo().getCustName());
			row.createCell(2)
					.setCellValue(SOMUtils.CompToOrderNumTo(serviceInfo.getOrderInfo().getWoNumber().substring(0, 2)));
			row.createCell(3).setCellValue(serviceInfo.getOrderInfo().getWoNumber());
			row.createCell(4).setCellValue(serviceInfo.getDevice().getMachCode());
			row.createCell(5).setCellValue(serviceInfo.getDevice().getUnitType());
			row.createCell(6).setCellValue(serviceInfo.getDevice().getDeviceBrand());
			row.createCell(7).setCellValue(serviceInfo.getOrderInfo().getCustAddr());
			row.createCell(8).setCellValue(ExcelUtils.fmtOne.format(serviceInfo.getOrderInfo().getRepairTime()));
			row.createCell(9).setCellValue(
					serviceInfo.getProbSolve() == null ? "" : ExcelUtils.fmtOne.format(serviceInfo.getProbSolve()));
			row.createCell(10).setCellValue(serviceInfo.getOrderInfo().getFaultType());
			row.createCell(11).setCellValue(serviceInfo.getOrderInfo().getAccidentType() == null ? ""
					: serviceInfo.getOrderInfo().getAccidentType());
			row.createCell(12).setCellValue("");
			row.createCell(13).setCellValue(serviceInfo.getMainResults());
			row.createCell(14).setCellValue("");
		}

		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 声明第一个sheet名称
		HSSFSheet sheet1 = wb.createSheet("维修报表饼图");
		BASE64Decoder decoder = new BASE64Decoder();
		List<byte[]> images = new ArrayList<>();
		images.add(decoder.decodeBuffer(request.getParameter("d").substring(22)));
		// 设置宽高
		sheet1.setDefaultRowHeight((short) (350 * 30 / 25));
		sheet1.setColumnWidth((int) (400 * 1990 / 140), 0);
		// 将获取到的base64 编码转换成图片，画到excel中
		HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
		HSSFClientAnchor anchor = null;
		int index = 0;
		for (byte[] image : images) {
			anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (8 * (index % 3)), ((index / 3) * 18),
					(short) (7 + 8 * (index % 3)), 16 + ((index / 3) * 18));
			patriarch.createPicture(anchor, wb.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG));
			index++;
		}

		ExcelUtils.download(res, wb, tableName);
	}

	// 设备运转正常率↓************************************************************************************************

	*//**
	 * 匹配设备运转率接口
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/equipmentOperation")
	@ResponseBody
	public Map<String, Object> equipmentOperation(ModelAndView modelAndView) throws Exception {
		if (sDate != null) {
			sDate = "";
		}
		if (eDate != null) {
			eDate = "";
		}
		String c = "08:30:00";
		String d = "17:30:00";
		Date workTime = ExcelUtils.fmtHms.parse(c);
		Date offWorkTime = ExcelUtils.fmtHms.parse(d);
		String custName = request.getParameter("custName");
		String serviceArea = request.getParameter("serviceArea");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate + " 00:00:00";
		eDate = endDate + " 23:59:59";
		// 如果查询条件没有开始时间，则默认设置为当月一号
		if ("".equals(startDate) || startDate == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = ExcelUtils.fmtOne.format(calendar.getTime());// 当月开始时间
			sDate = startDate;
		}
		// 如果查询条件没有结束时间，则默认设置为当前系统时间
		if ("".equals(endDate) || endDate == null) {
			endDate = ExcelUtils.fmtOne.format(new Date());
			eDate = endDate;
		}
		System.out.println("开始时间为：" + sDate);
		// 查出所有的设备记录
		devices = customerManageService.selectByDevice(custName, serviceArea, "", "", null, null);
		// 查出选定日期内所有的报修记录单
		serviceInfos = serviceInfoService.selectServiceInfoByOrder("", "", "", sDate, eDate);
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
			service.setDevice(customerManageService.selectDeviceById(service.getOrderInfo().getMachCode()));
		}
		// 依次判断每个机器是否当月有报修记录
		for (Device device : devices) {
			// 先设置设备理论的工作时间
			Double oryOfWorkTime = CalendarTool.getDownTime(ExcelUtils.fmtOne.parse(sDate),
					ExcelUtils.fmtOne.parse(eDate), workTime, offWorkTime);
			device.setWorkTime(oryOfWorkTime);
			Double downTime = 0.0;
			for (ServiceInfo service : serviceInfos) {
				// 先计算单月有报修记录的时间
				// 如果机器的编码与该工单相同，则计算停机时间
				if (device.getMachCode().equals(service.getOrderInfo().getMachCode())) {
					if (service.getProbSolve() == null) {
						downTime = downTime + CalendarTool.getDownTime(service.getOrderInfo().getRepairTime(),
								ExcelUtils.fmtOne.parse(eDate), workTime, offWorkTime);
						device.setDownTime(downTime);
						continue;
					}

					// 如果结束时间是在选定的结束时间之前，则可以直接套用公式计算
					if (service.getProbSolve().before(ExcelUtils.fmtOne.parse(eDate))) {
						downTime = downTime + CalendarTool.getDownTime(service.getOrderInfo().getRepairTime(),
								service.getProbSolve(), workTime, offWorkTime);
						device.setDownTime(downTime);
						continue;
					} else {
						downTime = downTime + CalendarTool.getDownTime(service.getOrderInfo().getRepairTime(),
								ExcelUtils.fmtOne.parse(eDate), workTime, offWorkTime);
						device.setDownTime(downTime);
						continue;
					}
				} else {
					device.setDownTime(downTime);
				}
			}
			BigDecimal b1 = new BigDecimal(Double.toString(oryOfWorkTime));
			BigDecimal b2 = new BigDecimal(Double.toString(downTime));
			BigDecimal b3 = b1.subtract(b2);
			device.setOperationRate(b3.divide(b1, 2, RoundingMode.HALF_UP).doubleValue());
		}
		json.put("code", 0);
		json.put("msg", "设备运转率kpi数据");
		json.put("count", serviceInfos.size());
		json.put("data", serviceInfos);
		return json;
	}

	*//**
	 * 导出设备正常运转率Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportEquipmentOperation")
	public void exportEquipmentOperation(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "设备运转正常率";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "设备名称", "机器编码", "设备型号", "设备序列号", "运转时间(hr)", "停机时间(hr)", "设备正常运转率(%)" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (Device device : devices) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(device.getCustArea());
			row.createCell(2).setCellValue(device.getServiceArea());
			row.createCell(3).setCellValue(device.getDevName());
			row.createCell(4).setCellValue(device.getMachCode());
			row.createCell(5).setCellValue(device.getUnitType());
			row.createCell(6).setCellValue(device.getEsNumber());
			row.createCell(7).setCellValue(device.getWorkTime());
			row.createCell(8).setCellValue(device.getDownTime());
			row.createCell(9).setCellValue(device.getOperationRate() * 100 + "%");
		}
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 声明第一个sheet名称
		HSSFSheet sheet1 = wb.createSheet("平均运转率饼图");
		BASE64Decoder decoder = new BASE64Decoder();
		List<byte[]> images = new ArrayList<>();
		images.add(decoder.decodeBuffer(request.getParameter("d").substring(22)));
		// 设置宽高
		sheet1.setDefaultRowHeight((short) (350 * 30 / 25));
		sheet1.setColumnWidth((int) (400 * 1990 / 140), 0);
		// 将获取到的base64 编码转换成图片，画到excel中
		HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
		HSSFClientAnchor anchor = null;
		int index = 0;
		for (byte[] image : images) {
			anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (8 * (index % 3)), ((index / 3) * 18),
					(short) (7 + 8 * (index % 3)), 16 + ((index / 3) * 18));
			patriarch.createPicture(anchor, wb.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG));
			index++;
		}
		ExcelUtils.download(res, wb, tableName);
	}

	*//**
	 * 客户设备运转率汇总接口
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/equipmentOperationSummary")
	@ResponseBody
	public Map<String, Object> equipmentOperationSummary(ModelAndView modelAndView) throws Exception {
		Map<String, Object> json = new HashMap<>();
		String custName = "";
		for (Device device : devices) {
			custName = device.getCustArea();
			break;
		}
		Double workTime = 0.0;
		Double downTime = 0.0;
		// 按照客户名称，统计所有的设备总时间和总停机时间
		for (Device device : devices) {
			if (device.getCustArea().equals(custName)) {
				workTime = workTime + device.getWorkTime();
				downTime = downTime + device.getDownTime();
			}
		}
		BigDecimal b = new BigDecimal((workTime - downTime) / workTime);
		Double operationRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		modelAndView.addObject("custName", custName);
		modelAndView.addObject("operationRate", operationRate);
		json.put("code", 0);
		json.put("msg", "设备运转率汇总kpi数据");
		json.put("count", 1);
		json.put("data", custName + (workTime - downTime) / workTime);
		return json;
	}

	@RequestMapping("/repairKpiReport")
	public ModelAndView repairKpiReport(ModelAndView modelAndView) {
		String custName = "";
		for (Device device : devices) {
			custName = device.getCustArea();
			break;
		}
		Double workTime = 0.0;
		Double downTime = 0.0;
		// 按照客户名称，统计所有的设备总时间和总停机时间
		for (Device device : devices) {
			if (device.getCustArea().equals(custName)) {
				workTime = workTime + device.getWorkTime();
				downTime = downTime + device.getDownTime();
			}
		}
		BigDecimal b = new BigDecimal((workTime - downTime) / workTime);
		Double operationRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		modelAndView.addObject("custName", custName);
		modelAndView.addObject("operationRate", operationRate * 100);
		modelAndView.setViewName("/reportManage/html/equipmentOperation");
		return modelAndView;
	}

	// 保养报表↓*******************************************************************************************

	*//**
	 * 保养报表:保养状态接口
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/maintenanceReport")
	@ResponseBody
	public Map<String, Object> maintenanceReport(ModelAndView modelAndView) {
		// 从前端接受数据
		String responsibleId = request.getParameter("responsibleId");
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String Cycle = request.getParameter("Cycle");
		Map<String, Object> json = new HashMap<>();
		maintenances = maintenanceserviceImpl.selectByCycle(Cycle, responsibleId, serviceArea, custName);
		for (Maintenance maintenance : maintenances) {
			maintenance.setDevice(customerManageService.selectDeviceById(maintenance.getMachCode()));
		}
		json.put("code", 0);
		json.put("msg", "保养状态kpi数据");
		json.put("count", maintenances.size());
		json.put("data", maintenances);
		return json;
	}

	*//**
	 * 保养报表:保养汇总接口
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/maintenanceSummary")
	@ResponseBody
	public Map<String, Object> maintenanceSummary(ModelAndView modelAndView) {
		if (maintenanceSummarys != null) {
			maintenanceSummarys.clear();
		}
		// 从前端接受数据
		String staffName = request.getParameter("staffName");
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> json = new HashMap<>();
		sDate = startDate;
		eDate = endDate;
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
			sDate = startDate;
			eDate = endDate;
		}
		Set<String> custNames = new HashSet<>();// 用来存放客户名称
		maintenances = maintenanceserviceImpl.selectmaintenance(custName, serviceArea, staffName, "", startDate,
				endDate);
		for (Maintenance maintenance : maintenances) {
			maintenance.setDevice(customerManageService.selectDeviceById(maintenance.getMachCode()));
			// 还欠缺完成时间，可以在这里设置
			custNames.add(maintenance.getCustName());
		}
		for (String string : custNames) {
			// 创建查询的保养执行对象，用于查询
			MaintenancePerform maintenancePerform = new MaintenancePerform();
			maintenancePerform.setCustName(string);
			maintenancePerform.setMainFrequency("月");
			// 统计每个客户保养的数量，完成的数量，未完成的数量
			MaintenanceSummary maintenanceSummary = new MaintenanceSummary();
			maintenanceSummary.setCustName(string);
			maintenanceSummary.setDevNumber(customerManageService.selectDeviceNumByCustName(string).size());// 客户名下的总数量
			maintenanceSummary.setPlanDevNum(maintenanceserviceImpl.selectByCycle("月", null, "", string).size());// 客户名下的保养设备数量
			maintenanceSummary
					.setComplNum(maintenanceserviceImpl.selectByDynamic(maintenancePerform, startDate, endDate).size());
			maintenanceSummary.setNoCompNum(maintenanceSummary.getPlanDevNum() - maintenanceSummary.getComplNum());
			maintenanceSummary.setCompletionRate(maintenanceSummary.getComplNum() / maintenanceSummary.getPlanDevNum());
			maintenanceSummarys.add(maintenanceSummary);
		}
		json.put("code", 0);
		json.put("msg", "保养汇总kpi数据");
		json.put("count", maintenanceSummarys.size());
		json.put("data", maintenanceSummarys);
		return json;
	}

	*//**
	 * 导出保养状态报表Excel
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportMaintenanceReport")
	public void exportMaintenanceReport(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "保养报表状态";
		// 设置表头
		String[] Titles = { "机器编码", "设备名称", "保养周期", "保养状态", "完成日期", "黑白读数", "彩色读数", "耗材存量", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (Maintenance maintenance : maintenances) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(maintenance.getMachCode());
			row.createCell(2).setCellValue(maintenance.getDevice().getDevName());
			row.createCell(3).setCellValue(maintenance.getMainFrequency());
			row.createCell(4).setCellValue("");
			row.createCell(5).setCellValue("");
			row.createCell(6).setCellValue(maintenance.getDevice().getBwReader());
			row.createCell(7).setCellValue(maintenance.getDevice().getColorReader());
			row.createCell(8).setCellValue("");
			row.createCell(9).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	*//**
	 * 导出保养汇总Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportMaintenanceSummary")
	public void exportMaintenanceSummary(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "保养完成报表";
		// 设置表头
		String[] Titles = { "客户名称", "设备数量", "计划保养数量", "实际完成", "未完成", "完成率", "工程师姓名", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (MaintenanceSummary maintenanceSummary : maintenanceSummarys) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(maintenanceSummary.getCustName());
			row.createCell(2).setCellValue(maintenanceSummary.getDevNumber());
			row.createCell(3).setCellValue(maintenanceSummary.getPlanDevNum());
			row.createCell(4).setCellValue("");
			row.createCell(5).setCellValue("");
			row.createCell(6).setCellValue("");
			row.createCell(7).setCellValue("");
			row.createCell(8).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// 设备管理报表↓************************************************************************************
	*//**
	 * 匹配设备管理报表接口
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/equipmentManagementReport")
	@ResponseBody
	public Map<String, Object> equipmentManagementReport(ModelAndView modelAndView) {
		// 从前端接受查询条件
		String serviceArea = request.getParameter("serviceArea");
		String custArea = request.getParameter("custArea");
		String contartctNo = request.getParameter("contartctNo");
		devices = customerManageService.selectByKpi(serviceArea, custArea, contartctNo);
		Map<String, Object> json = new HashMap<>();
		json.put("code", 0);
		json.put("msg", "设备管理报表接口");
		json.put("count", devices.size());
		json.put("data", devices);
		return json;
	}

	*//**
	 * 导出设备管理报表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportEquipmentManagement")
	public void exportEquipmentManagement(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "设备管理报表";
		// 设置表头
		String[] Titles = { "服务区域", "客户名称", "合同号码", "资产归属", "资产管理人", "设备名称", "机器编码", "设备类型", "设备序列号", "设备使用年数",
				"设备使用状态", "设备位置", "装机日期", "品牌", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (Device device : devices) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(device.getServiceArea());
			row.createCell(2).setCellValue(device.getCustArea());
			row.createCell(3).setCellValue(device.getContractNo());
			row.createCell(4).setCellValue(device.getAssetAttr());
			row.createCell(5).setCellValue(device.getReserveEnginner());
			row.createCell(6).setCellValue(device.getDevName());
			row.createCell(7).setCellValue(device.getMachCode());
			row.createCell(8).setCellValue(device.getDeviceType());
			row.createCell(9).setCellValue(device.getEsNumber());
			row.createCell(10).setCellValue("");
			row.createCell(11).setCellValue("");
			row.createCell(12).setCellValue(device.getLocation());
			row.createCell(13).setCellValue("");
			row.createCell(14).setCellValue(device.getDeviceBrand());
			row.createCell(15).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	// 运维合同报表↓**************************************************************************

	*//**
	 * 匹配运维合同报表
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/operationalContract")
	@ResponseBody
	public Map<String, Object> operationAndMaintenance(ModelAndView modelAndView) {
		// 从前端接受查询参数
		String contractNo = request.getParameter("contractNo");
		String custName = request.getParameter("custName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> json = new HashMap<>();
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate + " 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		sDate = startDate;
		eDate = endDate;
		contracts = customerManageService.selectByKpi(contractNo, custName, startDate, endDate);
		json.put("code", 0);
		json.put("msg", "运维合同kpi数据");
		json.put("count", contracts.size());
		json.put("data", contracts);
		return json;
	}

	*//**
	 * 导出运维合同报表Excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 *//*
	@RequestMapping("/exportOperationalContract")
	public void exportOperationalContract(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String tableName = "运维合同报表";
		// 设置表头
		String[] Titles = { "客户名称", "服务区域", "合同名称", "合同编码", "合同类型", "合同性质", "登记时间", "经办部门", "经办人", "合同保管人", "签约日期",
				"到期日期", "离到期天数", "期限(月)", "合同约定到期提醒天数", "合同到期预警", "外包所服务客户", "合同标的金额", "合同执行情况", "合同终止情况", "提前终止日",
				"提前终止情况说明", "移交归档日期", "接交人", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("开始时间:");
		row.createCell(1).setCellValue(sDate);
		row.createCell(2).setCellValue("结束时间:");
		row.createCell(3).setCellValue(eDate);
		// 循环将数据写入Excel
		for (Contract contract : contracts) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(contract.getCustName());
			row.createCell(2).setCellValue(contract.getChildService());
			row.createCell(3).setCellValue("");
			row.createCell(4).setCellValue(contract.getContractNo());
			row.createCell(5).setCellValue(contract.getContractType());
			row.createCell(6).setCellValue("");
			row.createCell(7).setCellValue(ExcelUtils.fmt.format(contract.getRegTime()));
			row.createCell(8).setCellValue(contract.getHandlingDepartment());
			row.createCell(9).setCellValue(contract.getAgent());
			row.createCell(10).setCellValue(contract.getContractHoldman());
			row.createCell(11).setCellValue(ExcelUtils.fmt.format(contract.getStartDate()));
			row.createCell(12).setCellValue(ExcelUtils.fmt.format(contract.getEndDate()));
			row.createCell(13).setCellValue("");
			row.createCell(14).setCellValue(contract.getContractDeadline());
			row.createCell(15).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
	}

	@Test
	public void test() throws IOException {
		File file = new File("D:/思贝克");
		String[] list = file.list();
		for (String string : list) {
			File oldfile = new File(string);
			if (!oldfile.isFile()) {
				oldfile.mkdirs();
			}
		}
		
		 * BufferedInputStream is=new BufferedInputStream(new
		 * FileInputStream(file)); BufferedOutputStream os=new
		 * BufferedOutputStream(new FileOutputStream("D:/小胖传奇.exe")); byte[]
		 * bt=new byte[1024]; int index=0; while((index=is.read(bt))!=-1){
		 * os.write(bt, 0, index); os.flush(); } os.close(); is.close();
		 
	}

	*//**
	 * 匹配运维合同报表
	 * 
	 * @param modelAndView
	 * @return
	 *//*
	@RequestMapping("/tupian")
	@ResponseBody
	public String tupian(ModelAndView modelAndView) {
		// 从前端接受查询参数
		String tupian = request.getParameter("d");
		System.out.println("图片编码为：" + tupian);
		return tupian;
	}

}
*/