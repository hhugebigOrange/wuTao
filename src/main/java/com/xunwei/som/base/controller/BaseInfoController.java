package com.xunwei.som.base.controller;

import java.io.IOException;
import java.text.ParseException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.CustomerManageService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

/**
 * 基本信息
 * 
 * @author Administrator
 *
 */
@Controller
@Transactional
public class BaseInfoController extends BaseController {

	/**
	 * 用于每次查询后，保存查询后的结果
	 */
	private List<StaffInfo> staffInfos;

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private CustomerManageService customerManageService = new CustomerManageServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();
	// 用于每次查询后，保存查询后的客户信息
	private List<CustInfo> custInfos;
	//用于保存每个用户的查询记录
	private Map<String,Object> export=new HashMap<>();

	private UserService userService = new UserServiceImpl();

	/**
	 * 匹配客户信息页面，默认查询所有
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/customerInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerInfo() {
		response.setHeader("Access-Control-Allow-Origin", "*");
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
		if(serviceArea==null || serviceArea.equals("")){
			serviceArea="";
		}
		// 当前登录用户
		String identifier = null;
		User user = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			user.setCustName(serviceArea);
		} else if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			user.setCustName(serviceArea);
			identifier = "1";
		}
		List<CustInfo> custInfo = custInfoService.selectCustByBaseInfo(custName, user.getCustName(), page, limit,
				identifier);
		custInfos = custInfoService.selectCustByBaseInfo(custName, user.getCustName(), null, null, identifier);
		export.put(request.getParameter("username")+"customerInfo", custInfos);
		json.put("code", 0);
		json.put("msg", "客户信息");
		json.put("data", custInfo);
		json.put("count", custInfos.size());
		return json;
	}

	/**
	 * 接口：返回相应客户id的信息
	 * 
	 */
	@RequestMapping(value = "/returnCustInfo", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> returnCustInfo() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收参数
		Integer custId = Integer.valueOf(request.getParameter("custId"));
		CustInfo custInfo = custInfoService.selectCustById(custId);
		Map<String, Object> json = new HashMap<>();
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("data", custInfo);
		return json;
	}

	/**
	 * 方法：新增客户信息,成功返回true
	 * 
	 * @throws ParseException
	 */
	@RequestMapping(value = "/insertCust", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> insertCust() throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String custName = request.getParameter("custName"); // 客户名称
		String custAddr = request.getParameter("custAddr"); // 客户地址
		String linkMan = request.getParameter("linkMan"); // 联系人
		String phone = request.getParameter("phone"); // 联系人电话
		String workTime1 = request.getParameter("workTime"); // 上班时间
		String offWorkTime1 = request.getParameter("offWorkTime"); // 下班时间
		// 当前登录用户
		User user = userService.selectByUserId(request.getParameter("username"));
		Date workTime = null;
		Date offWorkTime = null;
		// 判断输入参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("客户名称", custName);
		args.put("地址", custAddr);
		args.put("联系人", linkMan);
		args.put("联系人电话", phone);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 上下班时间不能一个为空，另一个不为空
		if (workTime1 == null && offWorkTime1 != null) {
			json.put("code", 1);
			json.put("msg", "请输入上班时间");
			return json;
		}
		// 上下班时间不能一个为空，另一个不为空
		if (workTime1 != null && offWorkTime1 == null) {
			json.put("code", 1);
			json.put("msg", "请输入下班时间");
			return json;
		}
		// 如果上班时间下班时间都为空
		if ((workTime1 == null || workTime1.trim().equals(""))
				&& (offWorkTime1 == null || offWorkTime1.trim().equals(""))) {
			workTime = ExcelUtils.fmtHms.parse("08:30:00");
			offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
		} else if (workTime1 != null && offWorkTime1 != null) {
			try {
				workTime = ExcelUtils.fmtHms.parse(workTime1);
			} catch (ParseException e) {
				json.put("code", 1);
				json.put("msg", "上班时间格式不正确，正确格式为08:30:00");
				return json;
			}
			try {
				offWorkTime = ExcelUtils.fmtHms.parse(offWorkTime1);
			} catch (Exception e) {
				json.put("code", 1);
				json.put("msg", "下班时间格式不正确，正确格式为08:30:00");
				return json;
			}
		}
		// 若参数都不为空，则执行增加方法
		CustInfo custInfo = new CustInfo(custName, custAddr, linkMan, phone, workTime, offWorkTime);
		custInfo.setSignComp(user.getCustName());
		int result = custInfoService.insert(custInfo);
		// 判断是否增加成功
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "新增成功");
			json.put("data", custInfo);
			return json;
		}
		json.put("code", 1);
		json.put("msg", "新增失败");
		json.put("data", "新增客户信息失败");
		return json;
	}

	/**
	 * 方法：修改客户信息,成功返回true
	 */
	@RequestMapping(value = "/updateCust", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> updateCust() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		try {
			// 从前端接收参数
			String custName = request.getParameter("custName"); // 客户名称
			String custAddr = request.getParameter("custAddr"); // 客户地址
			String linkMan = request.getParameter("linkMan"); // 联系人
			String phone = request.getParameter("phone"); // 联系电话
			Integer custId = Integer.valueOf(request.getParameter("custId")); // 客户公司Id
			Date workTime = null;// 上班时间
			Date offWorkTime = null;// 上班时间
			if (request.getParameter("workTime") != null && !"".equals(request.getParameter("workTime"))) {
				workTime = ExcelUtils.fmtHms.parse(request.getParameter("workTime"));
			} else {
				workTime = ExcelUtils.fmtHms.parse("08:30:00");
			}
			if (request.getParameter("offWorkTime") != null && !"".equals(request.getParameter("offWorkTime"))) {
				offWorkTime = ExcelUtils.fmtHms.parse(request.getParameter("offWorkTime"));
			} else {
				offWorkTime = ExcelUtils.fmtHms.parse("17:30:00");
			}
			// 判断输入参数是否有空
			Map<String, Object> args = new HashMap<>();
			args.put("客户名称", custName);
			args.put("客户地址", custAddr);
			args.put("联系人", linkMan);
			args.put("联系人电话", phone);
			String param = SOMUtils.paramsIsNull(args);
			if (!param.equals("")) {
				json.put("code", 1);
				json.put("msg", param + "不能为空");
				return json;
			}
			CustInfo custInfo = new CustInfo(custName, custAddr, linkMan, phone, workTime, offWorkTime);
			custInfo.setCustId(custId);
			// 获取修改之前公司信息
			CustInfo oldCust = custInfoService.selectCustById(custId);
			int result = custInfoService.update(custInfo);
			// 如果修改成功，则更新合同里的客户名称信息
			if (result > 0) {
				Contract contract = new Contract();
				contract.setCustName(custName);
				contract.setAddress(custAddr);
				contract.setCustLinkman(linkMan);
				contract.setLinkmanPhone(phone);
				int result2 = customerManageService.updateByPrimaryKeySelective(contract, oldCust.getCustName());
				//同步设备信息里面的客户信息
				for (Device device : customerManageService.selectByDevice(oldCust.getCustName(), null, null, null, null, null)) {
					device.setCustArea(custName);
					customerManageService.updateByPrimaryKeySelective(device);
				}
				//同步账号管理里面的客户信息
				for (User user : userService.selectAllUser()) {
					if(user.getCustName().equals(oldCust.getCustName())){
						user.setCustName(custName);
						userService.updateByPrimaryKeySelective(user);
					}
				}
				if (result2 > 0) {
					json.put("code", 0);
					json.put("msg", "修改客户信息成功");
					json.put("data", custInfo);
					return json;
				}
			}
			json.put("code", 1);
			json.put("msg", "修改客户信息失败");
			json.put("data", "修改客户信息失败");
			return json;
		} catch (ParseException e) {
			// 判断参数中的时间格式是否正确
			json.put("code", 1);
			json.put("msg", "上下班时间格式不正确，正确输入格式为：08:30:30");
			return json;
		}
	}

	/**
	 * 方法：删除客户信息,成功返回true(数据库里实际并未删除，只是修改了显示参数)
	 */
	@RequestMapping(value = "/deleteCust", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deleteCust() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		Integer custId = Integer.valueOf(request.getParameter("custId"));
		CustInfo custInfo = new CustInfo(custId, "0");
		int result = custInfoService.update(custInfo);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "删除成功");
			json.put("data", custInfo);
			return json;
		}
		json.put("code", 1);
		json.put("msg", "删除失败");
		json.put("data", "删除客户信息失败");
		return json;
	}

	/**
	 * 方法：导出客户信息Excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportCustomerInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportOrderCustomerInfo(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String username=request.getParameter("username");
		List<CustInfo> custInfos=(List<CustInfo>) export.get(username+"customerInfo");
		String tableName = "客户信息表";
		// 设置表头
		String[] Titles = { "客户名称", "客户地址", "联系人", "联系电话", "上班时间", "下班时间" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (CustInfo custInfo : custInfos) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(custInfo.getCustName());
			row.createCell(2).setCellValue(custInfo.getCustAddr());
			row.createCell(3).setCellValue(custInfo.getLinkman());
			row.createCell(4).setCellValue(custInfo.getPhone());
			row.createCell(5).setCellValue(
					custInfo.getWorkTime() == null ? "无" : ExcelUtils.fmtHms.format(custInfo.getWorkTime()));
			row.createCell(6).setCellValue(
					custInfo.getOffWorkTime() == null ? "无" : ExcelUtils.fmtHms.format(custInfo.getOffWorkTime()));
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	// *****************************************************************************************8
	/**
	 * 匹配员工管理页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/userManageInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> userManageInfo(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String name = request.getParameter("name");
		String branceName = request.getParameter("branceName");
		String role = request.getParameter("role");
		// 判断是否登录
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
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
		String identifier = null;
		// 判断是用户还是公司账号
		if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			branceName = (String) SOMUtils.getCompName(request).get("compname");
		}
		if (branceName != null) {
			if (branceName.equals("广州乐派数码科技有限公司") || branceName.equals("系统推进部") || branceName.equals("行业客户部")) {
				branceName = request.getParameter("branceName");
				identifier = "1";
			}
		}
		List<StaffInfo> staffInfo = staffInfoServiceImplnew.getStaffByDynamic(name, branceName, role, "", page, limit,
				null, identifier);
		staffInfos = staffInfoServiceImplnew.getStaffByDynamic(name, branceName, role, "", null, null, null,
				identifier);
		export.put(request.getParameter("username")+"userManageInfo", staffInfos);
		json.put("code", 0);
		json.put("msg", "员工数据");
		json.put("data", staffInfo);
		json.put("count", staffInfos.size());
		return json;
	}

	/**
	 * 增加员工方法，成功后返回true
	 * 
	 * @return
	 */
	@RequestMapping(value = "/doAddManager", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAddManager() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String staffId1 = request.getParameter("staffId"); // 员工编号
		String staffName = request.getParameter("staffName"); // 员工姓名
		String serviceArea = request.getParameter("serviceArea"); // 所属分公司名称
		String post = request.getParameter("role"); // 角色
		String phoneNumber = request.getParameter("phoneNumber"); // 电话
		String secret = request.getParameter("secret"); // 是否涉密
		String secretLevel = request.getParameter("secretLevel"); // 涉密等级
		String secretClass = request.getParameter("secretClass"); // 具体涉密等级
		String remark = request.getParameter("remark"); // 备注
		// 判断输入参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("员工编号", staffId1);
		args.put("姓名", staffName);
		args.put("服务区域", serviceArea);
		args.put("角色", post);
		args.put("电话", phoneNumber);
		args.put("是否涉密", secret);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		String staffId = staffId1.toUpperCase();
		// 判断员工编号和手机号是否重复
		for (StaffInfo staff : staffInfoServiceImplnew.selectAllStaff()) {
			if (staff.getName().equals(staffName)) {
				json.put("code", 1);
				json.put("msg", "对不起，该员工姓名已经存在");
				return json;
			}
			if (staff.getStaffId().equals(staffId)) {
				json.put("code", 1);
				json.put("msg", "对不起，该员工编号已经有了");
				return json;
			}
			if (staff.getPhone().equals(phoneNumber)) {
				json.put("code", 1);
				json.put("msg", "对不起，该员工手机号已经注册过");
				return json;
			}

		}
		if (secret != null && secret.equals("true")) {
			secret = "是";
		} else {
			secret = "否";
		}
		if (secret.equals("是")) {
			if (secretLevel == null || secretLevel.equals("")) {
				json.put("code", 1);
				json.put("msg", "涉密等级不能为空");
				return json;
			}
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
		} else if (secret.equals("否")) {
			secretLevel = "";
		}
		if (staffInfoServiceImplnew.selectCompIdByName(serviceArea) == 0) {
			json.put("code", 1);
			json.put("msg", "对不起，分公司名称不存在，请重新输入");
			return json;
		}
		// 获得传递过来公司名对应的公司ID
		int comperNumber = staffInfoServiceImplnew.selectCompIdByName(serviceArea);
		Date createDate = new Date();
		StaffInfo staff = new StaffInfo(staffName, phoneNumber, comperNumber, serviceArea, post, createDate);
		staff.setSecret(secret);
		staff.setSecretLevel(secretLevel);
		staff.setSecretClass(secretClass);
		staff.setStaffId(staffId);
		staff.setRemark(remark);
		// 如果参数不为空，则调用增加方法
		boolean result = staffInfoServiceImplnew.insertStaff(staff);
		if (result) {
			// 判断该账号是否已经注册过
			if (userService.selectByUserId(phoneNumber) != null) {
				json.put("code", 1);
				json.put("msg", "对不起，该账号已经被注册过了");
				return json;
			}
			// 创建用户对象
			User user = new User();
			user.setUserId(phoneNumber);
			user.setUserName(staffName);
			user.setPassword("123456");
			user.setCustName(serviceArea);
			user.setCreateTimed(new Date());
			int a = userService.insertSelective(user);
			// 创建用户-权限对象
			UserRole userRole = new UserRole();
			userRole.setRoleId(post);
			userRole.setUserId(phoneNumber);
			int b = userService.insertSelective(userRole);
			if (a > 0 && b > 0) {
				json.put("code", 0);
				json.put("msg", "新增成功");
				return json;
			}
			json.put("code", 0);
			json.put("msg", "增加成功");
			json.put("data", staff);
			return json;
		}
		json.put("code", 1);
		json.put("msg", "增加失败");
		return json;
	}

	/**
	 * 接口：返回相应客户id的信息
	 */
	@RequestMapping(value = "/returnStaff", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> returnStaff() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String staffId = request.getParameter("staffId");
		StaffInfo staffInfo = staffInfoServiceImplnew.selectStaffByNum(staffId);
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("data", staffInfo);
		return json;
	}

	/**
	 * 方法：修改员工信息,成功返回true
	 * 
	 */
	@RequestMapping(value = "/updateStaff", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> updateStaff() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 接受前端页面传递过来的信息
		String staffId = request.getParameter("staffId");
		String staffName = request.getParameter("staffName");
		String serviceArea = request.getParameter("serviceArea");
		String secret = request.getParameter("secret");
		String secretLevel = request.getParameter("secretLevel");
		String remark = request.getParameter("secretLevel");
		// 获得传递过来公司名对应的公司ID
		Integer comperNumber = staffInfoServiceImplnew.selectCompIdByName(serviceArea);
		String post = request.getParameter("role");
		String phoneNumber = request.getParameter("phoneNumber");
		// 判断是否存在空值
		Map<String, Object> para = new HashMap<>();
		para.put("员工编号", staffId);
		para.put("员工姓名", staffName);
		para.put("分公司名称", serviceArea);
		para.put("是否涉密", secret);
		para.put("岗位", post);
		para.put("电话号码", phoneNumber);
		String param = SOMUtils.isNull(para);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (secret != null && secret.equals("true")) {
			secret = "是";
		} else {
			secret = "否";
		}
		if (secret.equals("是")) {
			if (secretLevel == null || secretLevel.equals("")) {
				json.put("code", 1);
				json.put("msg", "涉密等级不能为空");
				return json;
			}
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
			secretLevel = null;
		}
		StaffInfo oldStaff = staffInfoServiceImplnew.selectStaffByNum(staffId); // 原员工信息
		// 判断电话是否注册过
		for (StaffInfo staff : staffInfoServiceImplnew.selectAllStaff()) {
			if (staff.getStaffId().equals(staffId)) {
				if (!staffId.equals(oldStaff.getStaffId())) {
					json.put("code", 1);
					json.put("msg", "对不起，该员工编号已经存在");
					return json;
				}
			}
			if (staff.getName().equals(staffName)) {
				if (!staffName.equals(oldStaff.getName())) {
					json.put("code", 1);
					json.put("msg", "对不起，该员工姓名已经存在");
					return json;
				}
			}
			if (staff.getPhone().equals(phoneNumber)) {
				if (!phoneNumber.equals(oldStaff.getPhone())) {
					json.put("code", 1);
					json.put("msg", "对不起，您修改的手机号已经有了");
					return json;
				}
			}
		}
		// 判断是否修改了员工的手机号和岗位
		// 员工对应的用户信息
		User user = userService.selectByUserId(oldStaff.getPhone());
		user.setUserId(phoneNumber);
		user.setUserName(staffName);
		user.setCustName(serviceArea);
		// 员工对应的角色信息
		UserRole userRole = userService.selectByPrimaryKey(oldStaff.getPhone());
		userRole.setUserId(phoneNumber);
		userRole.setRoleId(post);
		// 将createData转换为Data类型
		StaffInfo staff = new StaffInfo(staffName, phoneNumber, comperNumber, serviceArea, post, null);
		if (secret.equals("否")) {
			staff.setStaffId(oldStaff.getStaffId());
			staff.setSecret(secret);
			staff.setRemark(remark);
		} else {
			staff.setStaffId(oldStaff.getStaffId());
			staff.setSecret(secret);
			staff.setSecretLevel(secretLevel);
			staff.setRemark(remark);
		}
		int result = staffInfoServiceImplnew.updateStaff(staff); // 修改基本信息
		userService.updateByPrimaryKeySelective(user);     // 修改对应用户信息
		userService.updateByPrimaryKeySelective(userRole); // 修改对应角色信息
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "修改成功");
			json.put("data", staff);
			return json;
		}
		json.put("code", 1);
		json.put("msg", "修改失败");
		return json;
	}
	
	/**
	 * 方法：删除用户信息,成功返回true(数据库里实际并未删除，只是修改了显示参数)
	 */
	@RequestMapping(value = "/deleteStaff", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deleteStaff() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String staffId = request.getParameter("staffId");
		String userId = staffInfoServiceImplnew.selectStaffByNum(staffId).getPhone();
		// 先删除用户
		staffInfoServiceImplnew.deleteStaffById(staffId);
		// 在删除账户
		userService.deleteByPrimaryKey(userId);
		// 在删除账户-角色，对应关系
		userService.deleteUserRolePrimaryKey(userId);
		json.put("code", 0);
		json.put("msg", "删除成功");
		return json;
	}

	// ****************************************************************************

	/**
	 * 匹配增加客户页面
	 * 
	 * @param modelAndView
	 * @return
	 */

	@RequestMapping({ "/addCustomer" })
	public ModelAndView addCustomer(ModelAndView modelAndView) {
		modelAndView.setViewName("/baseinfo/html/addCustomer");
		return modelAndView;
	}

	/**
	 * 匹配增加用户页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping({ "/addManagerRole" })
	public ModelAndView addManagerRole(ModelAndView modelAndView) {
		modelAndView.setViewName("/baseinfo/html/addManagerRole");
		return modelAndView;
	}
	// ****************************************************************************

	/**
	 * 增加客户方法，成功后返回客户信息页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping({ "/doAddCustomer" })
	public ModelAndView doAddCustomer(ModelAndView modelAndView) {
		modelAndView.setViewName("/baseinfo/html/customerInfo");
		return modelAndView;
	}

	/**
	 * 查询员工
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/selectAllStaff", produces = "application/json; charset=utf-8")
	@ResponseBody
	public ModelAndView selectAllStaff(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		modelAndView.addObject("staffInfos", staffInfos);
		modelAndView.setViewName("/baseinfo/html/userManageInfo");
		return modelAndView;
	}

	/**
	 * 方法：导出员工信息Excel
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportStaff", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportOrder(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "用户表";
		List<StaffInfo> staffInfos=(List<StaffInfo>) export.get(request.getParameter("username")+"userManageInfo");
		// 设置表头
		String[] Titles = { "员工编号", "服务区域", "姓名", "电话", "角色", "是否涉密", "涉密等级", "创建时间", "备注" };
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.exportOrder(res, Titles, tableName);
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 1;
		// 循环将数据写入Excel
		for (StaffInfo staff : staffInfos) {
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++);
			row.createCell(1).setCellValue(staff.getStaffId());
			row.createCell(2).setCellValue(staff.getCompName());
			row.createCell(3).setCellValue(staff.getName());
			row.createCell(4).setCellValue(staff.getPhone());
			row.createCell(5).setCellValue(staff.getPost());
			row.createCell(6).setCellValue(staff.getSecret());
			row.createCell(7).setCellValue(staff.getSecretLevel());
			row.createCell(8).setCellValue(ExcelUtils.fmt.format(staff.getCreateDate()));
			row.createCell(9).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * 前台代码转换为对应的岗位
	 * 
	 * @param role
	 * @return
	 */
	public String roleToPost(String role) {
		String post = null;
		switch (role) {
		case "1":
			post = "运维经理";
			break;
		case "2":
			post = "客服主管";
			break;
		case "3":
			post = "客服调度员";
			break;
		case "4":
			post = "技术主管";
			break;
		case "5":
			post = "工程师";
			break;
		case "6":
			post = "驻现场人员";
			break;
		case "7":
			post = "客户助理";
			break;
		}
		return post;
	}

	// 工作日历↓****************************************************************************

	/**
	 * 匹配工作日历页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/workingCalendar", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> workingCalendar() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端获取数据
		String compName = request.getParameter("serviceArea");
		String name = request.getParameter("engineerName");
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服"))) {
			compName = (String) SOMUtils.getCompName(request).get("compname");
		}
		// 根据前端传递的条件筛选对应用户,此处岗位限定为工程师
		List<StaffInfo> staffInfos = staffInfoServiceImplnew.getStaffByDynamic(name, compName, "工程师", "", null, null,
				null, null);
		// 循环遍历工程师的结束时间是否到期，若到期，则将开始时间，结束时间，事由原因清空
		Date date = new Date();// 获取当前时间
		for (StaffInfo staffInfo : staffInfos) {
			if (staffInfo.getEndDate() != null) {
				if (date.after(staffInfo.getEndDate())) {
					staffInfoServiceImplnew.updateDateByStaffId(staffInfo.getStaffId());
				}
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, staffInfos, page, limit);
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
		json.put("msg", "工作日历数据");
		json.put("count", staffInfos == null ? 0 : staffInfos.size());
		json.put("data", page == null || staffInfos == null ? staffInfos : staffInfos.subList(page, limit));
		return json;
	}

	/**
	 * 匹配增加行程页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/addWorkingCalendar")
	public ModelAndView addWorkingCalendar(ModelAndView modelAndView) {
		modelAndView.setViewName("/baseinfo/html/addWorkingCalendar");
		return modelAndView;
	}

	/**
	 * 方法：增加行程页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/doAddWorkingCalendar", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> doAddWorkingCalendar() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String jobState = request.getParameter("jobState"); // 在岗状态
		String staffCode = request.getParameter("staffId"); // 员工编号
		// 先判断是否录入员工编号
		if (request.getParameter("staffId") == null || "".equals(request.getParameter("staffId"))) {
			json.put("code", 1);
			json.put("msg", "对不起，员工编号不能为空");
			return json;
		}
		// 在判断填写的员工编号是否存在
		StaffInfo staff = staffInfoServiceImplnew.selectStaffByNum(staffCode);
		if (staff == null) {
			json.put("code", 1);
			json.put("msg", "对不起，员工编号不存在");
			return json;
		}
		// 判断输入的员工是否是工程师
		if (!"工程师".equals(staff.getPost())) {
			json.put("code", 1);
			json.put("msg", "对不起,该员工不是工程师");
			return json;
		}
		// 在判断其他数据是否填写
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String reson = request.getParameter("reson");
		if (endDate == null || startDate == null || reson == null || "".equals(startDate) || "".equals(endDate)
				|| "".equals(reson)) {
			json.put("code", 1);
			json.put("msg", "对不起,您尚有条件未填写");
			return json;
		}
		try {
			StaffInfo staffInfo = new StaffInfo();
			staffInfo.setStaffId(staffCode);
			staffInfo.setWorkCond(jobState);
			staffInfo.setStartDate(ExcelUtils.fmt.parse(startDate));
			staffInfo.setEndDate(ExcelUtils.fmt.parse(endDate));
			staffInfo.setReson(reson);
			staffInfoServiceImplnew.updateStaff(staffInfo);
			json.put("code", 0);
			json.put("msg", "新增行程成功");
			return json;
		} catch (ParseException e) {
			json.put("code", 1);
			json.put("msg", "日期格式错误，请重新输入");
			return json;
		}
	}

	/**
	 * 方法：查找客户
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/selectCustByName", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectCustByName() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String custName = request.getParameter("custName"); // 客户名称
		if (custName == null || custName.equals("")) {
			json.put("code", 1);
			json.put("msg", "客户名称不能为空");
			return json;
		}
		CustInfo cust = custInfoService.selectCustById(custInfoService.selectCusIdByName(custName));
		if (cust == null) {
			json.put("code", 1);
			json.put("msg", "该客户名称不存在，请重新输入");
			return json;
		}
		json.put("code", 1);
		json.put("data", cust);
		return json;
	}
}
