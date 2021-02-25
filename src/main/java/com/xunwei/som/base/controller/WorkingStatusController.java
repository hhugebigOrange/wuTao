package com.xunwei.som.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.service.StaffInfoService;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

/**
 * 工作状态
 * 
 * @author Administrator
 *
 */
@Controller
public class WorkingStatusController extends BaseController {

	@Autowired
	private StaffInfoService staffInfoService;
	
	//用于保存每个用户的查询记录
	private Map<String,Object> export=new HashMap<>();

	/**
	 * 用户每次查询工程师状态后存放结果集
	 */
	private List<StaffInfo> engineers;

	/**
	 * 匹配工程师工作状态页面，默认查询所有。
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/engineerWorkingStatus", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> engineerWorkingStatus(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		int[] persons = new int[4];
		Integer p0, p1, p2;
		Map<String, Object> json = new HashMap<>();
		p0 = 0;
		p1 = 0;
		p2 = 0;
		String post = request.getParameter("post");
		String name = request.getParameter("name");
		String serviceArea = null;
		String identifier = null;
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
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服")
				|| SOMUtils.getCompName(request).get("role").equals("优质运维专员")
				|| SOMUtils.getCompName(request).get("role").equals("运维管理人员"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		if (serviceArea != null) {
			if (serviceArea.equals("广州乐派数码科技有限公司") || serviceArea.equals("系统推进部") || serviceArea.equals("行业客户部")) {
				serviceArea = request.getParameter("branceName");
				identifier = "1";
			}
		}
		engineers = staffInfoService.getStaffByDynamic(name, serviceArea, post, "", null, null, null, identifier);
		List<StaffInfo> engineer = staffInfoService.getStaffByDynamic(name, serviceArea, post, "", page, limit, null, identifier);
		for (StaffInfo staff : engineer) {
			if (staff.getPost().equals("技术主管")) {
				persons[0] = ++p0;
			}
			if (staff.getPost().equals("运维经理")) {
				persons[1] = ++p1;
			}
			if (staff.getPost().equals("工程师")) {
				persons[2] = ++p2;
			}
		}
		export.put(request.getParameter("username")+"engineerWorkingStatus", engineers);
		json.put("code", 0);
		json.put("msg", "在岗状态数据");
		json.put("count", engineers.size());
		json.put("data", engineer);
		return json;
	}

	/**
	 * 匹配员工修改页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/updateEngineer")
	public ModelAndView updateEngineer(ModelAndView modelAndView) {
		String id = request.getParameter("id");
		StaffInfo updateStaff = staffInfoService.selectStaffByNum(id);
		modelAndView.addObject("updateStaff", updateStaff);
		modelAndView.setViewName("/workingState/html/updateEngineer");
		return modelAndView;
	}

	/**
	 * 方法：员工修改
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/doUpdateEngineer", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doUpdateEngineer(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String id = request.getParameter("id");
		String workStatus = request.getParameter("workStatus");
		String remark = request.getParameter("remark");
		if (workStatus == null) {
			json.put("code", 1);
			json.put("msg", "在岗状态不能为空");
			return json;
		}
		StaffInfo updateStaff = new StaffInfo();
		updateStaff.setStaffId(id);
		updateStaff.setWorkCond(workStatus);
		updateStaff.setRemark(remark);
		staffInfoService.updateStaff(updateStaff);
		json.put("code", 0);
		json.put("msg", "修改成功");
		return json;
	}

	/**
	 * 方法：删除员工在岗状态
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/deletaEngineer", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deletaEngineer(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String id = request.getParameter("id");
		StaffInfo updateStaff = new StaffInfo();
		updateStaff.setStaffId(id);
		updateStaff.setDisplay(0);
		staffInfoService.updateStaff(updateStaff);
		json.put("code", 0);
		json.put("msg", "删除成功");
		return json;
	}

	/**
	 * 查询所有工程师
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectAll", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectAll(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		engineers = staffInfoService.getStaffByDynamic("", "", "", "", null, null, null, null);
		json.put("code", 0);
		json.put("data", engineers);
		json.put("msg", "查询成功");
		return json;
	}

	/**
	 * 方法：导出工程是状态Excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportEngineers", produces = "application/json; charset=utf-8")
	public Map<String, Object> exportOrder(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "工程师状态表";
		List<StaffInfo> engineers=(List<StaffInfo>) export.get(request.getParameter("username")+"engineerWorkingStatus");
		// 设置表头
		String[] Titles = { "姓名", "职位", "工作状态", "电话", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (StaffInfo staff : engineers) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(staff.getName());
			row.createCell(2).setCellValue(staff.getPost());
			row.createCell(3).setCellValue(staff.getWorkCond());
			row.createCell(4).setCellValue(staff.getPhone());
			row.createCell(5).setCellValue(staff.getRemark());
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	@RequestMapping("/checkBox")
	public ModelAndView checkBox(ModelAndView modelAndView) {
		modelAndView.setViewName("/serviceManage/html/checkBox");
		return modelAndView;
	}

	@RequestMapping("/checkBoxValue")
	@ResponseBody
	public String checkBoxValue(ModelAndView modelAndView) {
		String checks[] = request.getParameterValues("faultType");
		String a = "";
		for (int i = 0; i < checks.length; i++) {
			if (checks[i] != null && !"".equals(checks[i])) {
				System.out.println(checks[i]);
				a = a + checks[i];
			}
		}
		return a;
	}

	@RequestMapping(value = "/ResolutionImages", produces = "application/json; charset=utf-8")
	public void ResolutionImages() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String page = request.getParameter("page");
		/*
		 * FileInputStream fis = null; response.setContentType("image/gif"); try
		 * { OutputStream out = response.getOutputStream(); File file = new
		 * File("C:/Users/Administrator/Desktop/讯维截面图片1.0V/迅维（PC端）/登录页.png");
		 * fis = new FileInputStream(file); byte[] b = new
		 * byte[fis.available()]; fis.read(b); out.write(b); out.flush(); }
		 * catch (Exception e) { e.printStackTrace(); } finally { if (fis !=
		 * null) { try { fis.close(); } catch (IOException e) {
		 * e.printStackTrace(); } } }
		 */
	}

}
