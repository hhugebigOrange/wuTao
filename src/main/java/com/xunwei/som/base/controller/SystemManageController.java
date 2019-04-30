package com.xunwei.som.base.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.Assessment;
import com.xunwei.som.pojo.CompInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.permissions.ParameterSetting;
import com.xunwei.som.pojo.permissions.RolePermission;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.CompInfoService;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.ParameterSettingService;
import com.xunwei.som.service.SloganService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CompInfoServiceImpl;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ParameterSettingServiceImpl;
import com.xunwei.som.service.impl.SloganServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.SOMUtils;

/**
 * 系统管理
 * 
 * @author Administrator
 *
 */

@Controller
public class SystemManageController extends BaseController {

	private SloganService sloganService = new SloganServiceImpl();

	private CompInfoService compInfoService = new CompInfoServiceImpl();

	private ParameterSettingService parameterSettingService = new ParameterSettingServiceImpl();

	private UserService userService = new UserServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	public List<CompInfo> compInfos;

	/**
	 * 账户管理
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/selectUser", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectUser() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		String role = request.getParameter("role");
		String custName = request.getParameter("custName");
		String phone = request.getParameter("phone");
		Map<String, Object> json = new HashMap<>();
		if (request.getParameter("username") == null || request.getParameter("username").trim().equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 当前登录用户
		List<User> users = new ArrayList<>();
		User currUser = userService.selectByUserId(request.getParameter("username"));
		UserRole userRole = userService.selectByPrimaryKey(request.getParameter("username")); // 当前用户的角色
		if (userRole.getRoleId().equals("总部客服") || userRole.getRoleId().equals("运维总监")) {
			currUser.setCustName("");
			// 查找出所有用户
			users = userService.selectUserByRole(role, custName, phone);
			for (User user : users) {
				user.setRole(userService.selectByPrimaryKey(user.getUserId()).getRoleId());
			}
		} else if (currUser.getCustName().equals("广州乐派数码科技有限公司") || currUser.getCustName().equals("系统推进部")
				|| currUser.getCustName().equals("行业客户部")) {
			// 查找出所有用户
			users = userService.selectUserByRole(role, "广州乐派数码科技有限公司", phone);
			users.addAll(userService.selectUserByRole(role, "系统推进部", phone));
			users.addAll(userService.selectUserByRole(role, "行业客户部", phone));
			for (User user : users) {
				user.setRole(userService.selectByPrimaryKey(user.getUserId()).getRoleId());
			}
		} else {
			users = userService.selectUserByRole(role, currUser.getCustName(), phone);
			for (User user : users) {
				user.setRole(userService.selectByPrimaryKey(user.getUserId()).getRoleId());
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, users, page, limit);
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
		json.put("count", users.size());
		json.put("data", page == null || users == null ? users : users.subList(page, limit));
		return json;
	}

	/**
	 * 匹配修改密码页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/passwordChange")
	public ModelAndView passwordChange(ModelAndView modelAndView) {
		modelAndView.setViewName("/systemManage/html/passwordChange");
		return modelAndView;
	}

	/**
	 * 方法：更新权限
	 */
	@RequestMapping(value = "/upDatePermission", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> upDatePermission() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		Map<String, Object> para = SOMUtils.permissionsList();// 全为false的权限列表
		// 根据当前权限，修改true和false
		String permission = "";
		for (Map.Entry<String, Object> entry : para.entrySet()) {
			String a = request.getParameter(entry.getKey());
			// 判断前端传递有当前参数
			if (a != null && !a.equals("")) {
				if (a.equals("true")) {
					permission = permission + SOMUtils.permissionToNumber(entry.getKey()) + ";";
				}
			}
		}
		// para现在就是某角色的完整权限列表
		String[] permissionList = permission.split(";");
		List<String> one = new ArrayList<>();// 一级菜单
		List<String> two = new ArrayList<>();// 二级菜单
		List<String> three = new ArrayList<>();// 三级菜单
		for (String string : permissionList) {
			if (string.length() == 1) {
				one.add(string);
			} else if (string.length() == 3) {
				two.add(string);
			} else if (string.length() == 5) {
				three.add(string);
			}
		}
		// 判断一级二级菜单之间是否符合上下级关系
		for (String string : two) {
			if (!one.contains(string.substring(0, 1))) {
				json.put("code", 1);
				json.put("msg", "对不起，您选择的权限当中有权限尚未勾选上级，请检查");
				return json;
			}
		}
		// 判断二级三级菜单之间是否符合上下级关系
		for (String string : three) {
			if (!two.contains(string.substring(0, 3))) {
				json.put("code", 1);
				json.put("msg", "对不起，您选择的权限当中有权限尚未勾选上级，请检查");
				return json;
			}
		}
		// 符合规则后，在更新权限。权限字段需要转化为1;1-1;1-2;1-3;1-5;6;6-2;7;7-1;7-1-2;7-3;7-4;7-5;7-6;3;3-2;11;11-3的格式，在更新
		String role = request.getParameter("role");
		if (role == null || role.equals("")) {
			json.put("code", 1);
			json.put("msg", "请选择要分配权限的角色");
			return json;
		}
		RolePermission rolePermission = new RolePermission();
		rolePermission.setRoleId(role);
		rolePermission.setPermissionId(permission);
		int result = userService.updateByPrimaryKeySelective(rolePermission);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "修改权限成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "修改权限失败");
		return json;
	}

	/**
	 * 方法：增加客户账号
	 */
	@RequestMapping(value = "/addCust", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> addCust() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受权限字段
		String role = request.getParameter("role"); // 角色名称
		String compName = request.getParameter("compName"); // 公司名称
		String userName = request.getParameter("userName"); // 客户名称
		String account = request.getParameter("accout"); // 登陆账号
		String password = request.getParameter("password"); // 密码
		Date date = new Date(); // 创建时间
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("角色", role);
		params.put("公司名称", compName);
		params.put("客户名称", userName);
		params.put("登陆账号", account);
		params.put("密码", password);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		if (!role.equals("客户")) {
			json.put("code", 1);
			json.put("msg", "新增用户时，角色只能为客户");
			return json;
		}
		// 判断该账号是否已经注册过
		if (userService.selectByUserId(account) != null) {
			json.put("code", 1);
			json.put("msg", "对不起，该账号已经被注册过了");
			return json;
		}
		// 判断公司名是否存在
		if (custInfoService.selectCusIdByName(compName) == -1) {
			json.put("code", 1);
			json.put("msg", "对不起，该公司名称不存在");
			return json;
		}
		// 创建用户对象
		User user = new User();
		user.setUserId(account);
		user.setUserName(userName);
		user.setPassword(password);
		user.setCustName(compName);
		user.setCreateTimed(date);
		int a = userService.insertSelective(user);
		// 创建用户-权限对象
		UserRole userRole = new UserRole();
		userRole.setRoleId(role);
		userRole.setUserId(account);
		int b = userService.insertSelective(userRole);
		if (a > 0 && b > 0) {
			json.put("code", 0);
			json.put("msg", "新增成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "新增失败");
		return json;
	}

	/**
	 * 方法：增加企业员工方法
	 */
	@RequestMapping(value = "/addCompany", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> addCompany() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受权限字段
		String staffId = request.getParameter("staffId"); // 员工编码
		String password = request.getParameter("password"); // 密码
		Date date = new Date(); // 创建时间
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("员工编码", staffId);
		params.put("密码", password);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		StaffInfo staff = staffInfoServiceImplnew.selectStaffByNum(staffId);
		// 判断该员工编码是否存在
		if (staff == null) {
			json.put("code", 1);
			json.put("msg", "对不起，员工编码不存在，请重新输入");
			return json;
		}
		// 判断该账号是否已经注册过
		if (userService.selectByUserId(staff.getPhone()) != null) {
			json.put("code", 1);
			json.put("msg", "对不起，该账号已经被注册过了");
			return json;
		}
		// 创建用户对象
		User user = new User();
		user.setUserId(staff.getPhone());
		user.setUserName(staff.getName());
		user.setPassword(password);
		user.setCustName(staff.getCompName());
		user.setCreateTimed(date);
		int a = userService.insertSelective(user);
		// 创建用户-权限对象
		UserRole userRole = new UserRole();
		userRole.setRoleId(staff.getPost());
		userRole.setUserId(staff.getPhone());
		int b = userService.insertSelective(userRole);
		if (a > 0 && b > 0) {
			json.put("code", 0);
			json.put("msg", "新增成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "新增失败");
		return json;
	}

	/**
	 * 方法：更新参数列表
	 */
	@RequestMapping("/upParameter")
	@ResponseBody
	public boolean upParameter(ModelAndView modelAndView) {
		// 从前端接受权限字段
		// 参数字段需要转化为运维经理;技术经理;客户经理;客服;工程师;驻现场人员的格式，在更新
		String parameter = request.getParameter("parameter");
		String parameterName = request.getParameter("parameterName");
		ParameterSetting parameterSetting = new ParameterSetting();
		parameterSetting.setParameter(parameter);
		parameterSetting.setParameterName(parameterName);
		int result = parameterSettingService.updateByPrimaryKey(parameterSetting);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 匹配个人信息页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/personalInfo")
	public ModelAndView personalInfo(ModelAndView modelAndView) {
		modelAndView.setViewName("/systemManage/html/personalInfo");
		return modelAndView;
	}

	/**
	 * 匹配推广链接页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/promoteLinks")
	public ModelAndView promoteLinks(ModelAndView modelAndView) {
		modelAndView.setViewName("/systemManage/html/promoteLinks");
		return modelAndView;
	}

	/**
	 * 匹配推广二维码页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/promoteQrCodes")
	public ModelAndView promoteQrCodes(ModelAndView modelAndView) {
		modelAndView.setViewName("/systemManage/html/promoteQrCodes");
		return modelAndView;
	}

	/**
	 * 修改口号
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/changeSlogan")
	public ModelAndView changeSlogan(ModelAndView modelAndView) {
		// 从前端接受数据
		String slogan = request.getParameter("slogan");
		sloganService.updateSlogan(slogan);
		session.setAttribute("slogan", slogan);
		modelAndView.setViewName("/user/html/main");
		return modelAndView;
	}

	/**
	 * 接口：区域管理-查看
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/areaManage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> areaManage(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受数据
		List<CompInfo> compInfos = compInfoService.selectAllComp();
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, compInfos, page, limit);
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
		json.put("msg", "分公司管理数据");
		json.put("count", compInfos == null ? 0 : compInfos.size());
		json.put("data", page == null || compInfos == null ? compInfos : compInfos.subList(page, limit));
		return json;
	}

	/**
	 * 接口：增加服务区域
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/addArea", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> addArea(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受数据
		String serviceArea = request.getParameter("serviceArea"); // 服务区域
		String abbre = request.getParameter("abbre"); // 缩写
		String location = request.getParameter("location"); // 地址
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("服务区域", serviceArea);
		params.put("缩写", abbre);
		params.put("地址", location);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		for (CompInfo comp : compInfoService.selectAllComp()) {
			if (comp.getCompName().equals(serviceArea)) {
				json.put("code", 1);
				json.put("msg", "对不起，该服务区域已经存在");
				return json;
			}
			if (comp.getAbbreviation().equals(abbre.toUpperCase())) {
				json.put("code", 0);
				json.put("msg", "对不起，该服务区域缩写已经存在");
				return json;
			}
		}
		// 创建分公司对象
		CompInfo compInfo = new CompInfo();
		compInfo.setAbbreviation(abbre.toUpperCase());
		compInfo.setCompName(serviceArea);
		compInfo.setCompLocation(location);
		int result = compInfoService.insertSelective(compInfo);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "新增成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "新增失败");
		return json;
	}

	/**
	 * 接口：返回员工信息
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/staffMessage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> staffMessage() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String staffId = request.getParameter("staffId"); // 员工Id
		if (staffId == null || staffId.equals("")) {
			json.put("code", 1);
			json.put("msg", "员工编码不能为空");
			return json;
		}
		// 创建分公司对象
		StaffInfo staffInfo = staffInfoServiceImplnew.selectStaffByNum(staffId);
		if (staffInfo == null) {
			json.put("code", 1);
			json.put("msg", "员工编码不存在，请重新输入");
			return json;
		}
		json.put("code", 0);
		json.put("data", staffInfo);
		return json;
	}

	/**
	 * 接口：修改服务区域
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/updateArea", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> updateArea(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受数据
		Integer compId = Integer.valueOf(request.getParameter("compId")); // 服务区域id
		String serviceArea = request.getParameter("serviceArea"); // 服务区域
		String abbre = request.getParameter("abbre"); // 缩写
		String location = request.getParameter("location"); // 地址
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("服务区域", serviceArea);
		params.put("缩写", abbre);
		params.put("地址", location);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		CompInfo compInfo1 = compInfoService.selectByPrimaryKey(compId);
		// 创建分公司对象
		CompInfo compInfo = new CompInfo();
		compInfo.setAbbreviation(abbre.toUpperCase());
		compInfo.setCompName(serviceArea);
		compInfo.setCompLocation(location);
		compInfo.setCompNumber(compId);
		for (CompInfo comp : compInfoService.selectAllComp()) {
			if (comp.getCompName().equals(serviceArea)) {
				if (!serviceArea.equals(compInfo1.getCompName())) {
					json.put("code", 1);
					json.put("msg", "对不起，该服务区域已经存在");
					return json;
				}
			}
			if (comp.getAbbreviation().equals(abbre.toUpperCase())) {
				if (!abbre.toUpperCase().equals(compInfo1.getAbbreviation())) {
					json.put("code", 0);
					json.put("msg", "对不起，该服务区域缩写已经存在");
					return json;
				}
			}
		}
		int result = compInfoService.updateByPrimaryKeySelective(compInfo);
		if (result > 0) {
			if (!serviceArea.equals(compInfo1.getCompName())) {
				customerManage.updateDeviceServiceArea(compInfo1.getCompName(), serviceArea);
			}
			json.put("code", 0);
			json.put("msg", "修改成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "修改失败");
		return json;
	}

	/**
	 * 接口：删除服务区域
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/deleteArea", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> deleteArea(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受数据
		Integer compId = Integer.valueOf(request.getParameter("compNumber")); // 服务区域Id
		// 创建分公司对象
		CompInfo compInfo = new CompInfo();
		compInfo.setCompNumber(compId);
		compInfo.setDisplay(0);
		int result = compInfoService.updateByPrimaryKeySelective(compInfo);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "删除成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "删除失败");
		return json;
	}

	@RequestMapping(value = "/judgeRole", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> judgeRole() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String role = request.getParameter("username");
		if (role == null || role.equals("")) {
			return SOMUtils.permissionsList();
		}
		RolePermission permission = userService.selectByKey(userService.selectByPrimaryKey(role).getRoleId());
		String[] permissions = permission.getPermissionId().split(";");
		// 判断拥有哪些权限
		json.put("code", 0);
		json.put("role", role);
		json.put("data", SOMUtils.permissions(permissions, SOMUtils.permissionsList()));
		return json;
	}

	/**
	 * 接口：考核目标时间设定
	 * 
	 * @return
	 */
	@RequestMapping(value = "/assessment", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> assessment() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String responseTime = request.getParameter("responseTime"); // 响应时间
		String arriveTime = request.getParameter("arriveTime"); // 到达现场时间
		String solvingTime = request.getParameter("solvingTime"); // 问题解决时间
		String sendTime = request.getParameter("sendTime"); // 客服派单解决时间
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("响应时间", responseTime);
		params.put("到达现场时间", arriveTime);
		params.put("问题解决时间", solvingTime);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		try {
			Double.valueOf(responseTime);
			Double.valueOf(arriveTime);
			Double.valueOf(solvingTime);
			Double.valueOf(sendTime);
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "考核目标时间只能输入数字");
		}
		Assessment assment = new Assessment();
		assment.setAssessmentName("responseTime");
		assment.setAssessmentValue(Integer.valueOf(responseTime));
		userService.updateByPrimaryKeySelective(assment);
		Assessment assment1 = new Assessment();
		assment1.setAssessmentName("arriveTime");
		assment1.setAssessmentValue(Integer.valueOf(arriveTime) * 60);
		userService.updateByPrimaryKeySelective(assment1);
		Assessment assment2 = new Assessment();
		assment2.setAssessmentName("solvingTime");
		assment2.setAssessmentValue(Integer.valueOf(solvingTime) * 60);
		userService.updateByPrimaryKeySelective(assment2);
		Assessment assment3 = new Assessment();
		assment3.setAssessmentName("sendTime");
		assment3.setAssessmentValue(Integer.valueOf(sendTime));
		userService.updateByPrimaryKeySelective(assment3);
		json.put("code", 0);
		json.put("msg", "修改考核目标时间成功");
		return json;
	}

	/**
	 * 接口：新增解决方案
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addSolvePlan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> addSolvePlan() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String title = request.getParameter("title"); // 方案标题
		String content = request.getParameter("content"); // 方案标内容
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("方案标题", title);
		params.put("方案内容", content);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 创建方案解决对象
		ParameterSetting parameterSetting = new ParameterSetting();
		parameterSetting.setParameterName(title);
		parameterSetting.setParameter(content);
		int result = parameterSettingService.insertSelective(parameterSetting);
		if (result > 0) {
			json.put("code", 0);
			json.put("msg", "新增成功");
			return json;
		}
		json.put("code", 0);
		json.put("msg", "新增失败");
		return json;
	}

	/**
	 * 接口：查询考核目标时间
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectAssessment", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectAssessment() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		json.put("code", 0);
		json.put("msg", "查询成功");
		json.put("responseTime", userService.selectAssessmentNameByPrimaryKey("responseTime").getAssessmentValue());
		json.put("arriveTime", userService.selectAssessmentNameByPrimaryKey("arriveTime").getAssessmentValue() / 60);
		json.put("solvingTime", userService.selectAssessmentNameByPrimaryKey("solvingTime").getAssessmentValue() / 60);
		json.put("sendTime", userService.selectAssessmentNameByPrimaryKey("sendTime").getAssessmentValue());
		return json;
	}

}
