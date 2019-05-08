package com.xunwei.som.base.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.base.controller.BaseController;
import com.xunwei.som.calendar.CalendarService;
import com.xunwei.som.calendar.impl.CalendarServiceImpl;
import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.permissions.Permission;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.ParameterSettingService;
import com.xunwei.som.service.PermissionService;
import com.xunwei.som.service.SloganService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ParameterSettingServiceImpl;
import com.xunwei.som.service.impl.PermissionServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.SloganServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

import net.sf.json.JSONObject;

/**
 * 登陆控制器
 * 
 * @author Administrator
 *
 */

@Controller
public class UserLoginController extends BaseController {

	private UserService userService = new UserServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private ServiceInfoServiceImpl serviceInfoService = new ServiceInfoServiceImpl();

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private SloganService sloganService = new SloganServiceImpl();

	private PermissionService permissionService = new PermissionServiceImpl();

	private ParameterSettingService parameterSettingService = new ParameterSettingServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();
	
	private CalendarService calendarService = new CalendarServiceImpl();

	@RequestMapping("/weChatIndex")
	public ModelAndView index(ModelAndView modelAndView) throws IOException {
		modelAndView.setViewName("redirect:/user/html/index.html");
		return modelAndView;
	}

	@RequestMapping("/weiChatTest")
	public ModelAndView weiChatTest(ModelAndView modelAndView) throws IOException {
		modelAndView.setViewName("/weChat/index");
		return modelAndView;
	}

	@RequestMapping("/gotofile")
	public ModelAndView gotofile(ModelAndView modelAndView) throws IOException {
		modelAndView.setViewName("redirect:http://solutionyun.com/som/weChat/resSuccess.html");
		return modelAndView;
	}

	@RequestMapping("/")
	public ModelAndView weChatIndex(ModelAndView modelAndView) throws IOException {
		modelAndView.setViewName("redirect:/user/html/index.html");
		return modelAndView;
	}

	@RequestMapping("/accidentScan")
	public ModelAndView accidentScan(ModelAndView modelAndView) throws IOException {
		modelAndView.setViewName("redirect:/weChat/accident-scan.html");
		return modelAndView;
	}

	/**
	 * 进入登陆界面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView goToLoginPage(ModelAndView modelAndView) {
		modelAndView.setViewName("/user/html/login");
		return modelAndView;
	}

	/**
	 * 验证登陆
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/dologin", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> Login(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 如果账号或密码为空，则返回登陆页面
		if (SOMUtils.isNull(username) || SOMUtils.isNull(password)) {
			json.put("code", 1);
			json.put("msg", "用户名或密码不能为空");
			return json;
		}
		// 查找出所有用户
		List<User> users = userService.selectAllUser();
		for (User user : users) {
			// 比对账号和密码,成功跳到首页
			if (username.equals(user.getUserId()) && password.equals(user.getPassword())) {
				String slogan = sloganService.selectSlogan();
				// 获取登陆客户所属的公司名称
				String custName = user.getCustName();
				if (!staffInfoServiceImplnew.getStaffByDynamic("", "", "", username, null, null, null, null)
						.isEmpty()) {
					session.setAttribute("userId", staffInfoServiceImplnew
							.getStaffByDynamic("", "", "", username, null, null, null, null).get(0).getStaffId()); // 登陆用户的员工ID
				}
				Date date = new Date();
				User user2 = new User();
				user2.setUserId(user.getUserId());
				// 登陆成功更新登陆时间
				user2.setLastLoginTime(date);
				userService.updateByPrimaryKeySelective(user2);
				// session域中存放登陆者的权限信息
				UserRole userRole = userService.selectByPrimaryKey(username);
				String userRoles = userRole.getRoleId();
				session.setAttribute("custName", custName); // 登陆用户的公司名
				session.setAttribute("user", username); // 登陆用户的手机号
				session.setAttribute("slogan", slogan); // 登陆用户界面的口号
				session.setAttribute("userRole", userRoles); // 登陆用户角色
				if (userRoles.equals("客户")) {
					List<Contract> contract = customerManage.selectByCust(user.getCustName(), null, null, null, null,
							null, null, null, null);
					if (contract != null && contract.size() > 0) {
						user.setCustName(contract.get(0).getMainService());
					}
				}
				// 判断拥有哪些权限
				json.put("code", 0);
				json.put("user", username);
				json.put("name", user.getUserName());
				json.put("custName",
						(userRoles.equals("总部客服") || userRoles.equals("运维总监")) ? null : user.getCustName());
				json.put("userRole", userRoles);
				json.put("userRole2", SOMUtils.roleToEnglish(userRoles));
				json.put("msg", "登陆成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "账号或密码错误，请重新输入");
		return json;
	}

	/**
	 * 返回登陆权限
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/permissions", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> permissions() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String username = request.getParameter("username");
		Map<String, Object> json = new HashMap<>();
		if (username == null || username.equals("")) {
			json.put("code", 1);
			json.put("msg", "未登录，请先登录");
			return json;
		}
		if (permissionService.selectPermissionByUserId(username) == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该用户不存在");
			return json;
		}
		// 查找出所有用户
		List<User> users = userService.selectAllUser();
		for (User user : users) {
			// 比对账号,找到则找出权限
			if (username.equals(user.getUserId())) {
				Permission permission = permissionService.selectPermissionByUserId(username);
				String[] permissions = permission.getPermissionId().split(";");
				// session域中存放登陆者的权限信息
				UserRole userRole = userService.selectByPrimaryKey(username);
				// 判断拥有哪些权限
				json.put("code", 0);
				json.put("user", username);
				json.put("name", user.getUserName());
				json.put("userRole", userRole.getRoleId());
				json.put("data", SOMUtils.permissions(permissions, SOMUtils.permissionsList()));
				json.put("msg", "返回权限列表成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "账号不存在，请重新输入");
		return json;
	}

	/**
	 * 方法：修改密码，修改成功后返回首页
	 */
	@PostMapping(value = "/upDatePassword", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> upDatePassword(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String userName = request.getParameter("username");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String surePassword = request.getParameter("surePassword");
		// 先判断旧密码是否正确
		if (!oldPassword.equals(userService.selectByUserId(userName).getPassword())) {
			json.put("code", 1);
			json.put("msg", "对不起，原密码不正确，请重新输入");
			return json;
		}
		// 如果旧密码正确，在判断新密码是否相等
		if (!newPassword.equals(surePassword)) {
			json.put("code", 1);
			json.put("msg", "新密码与确认密码不相等");
			return json;
		}
		User user = new User();
		user.setUserId(userName);
		user.setPassword(newPassword);
		// 修改密码成功后，返回首页
		userService.updateByPrimaryKeySelective(user);
		json.put("code", 0);
		json.put("msg", "修改成功");
		return json;
	}

	/**
	 * 方法：修改密码，修改成功后返回首页
	 */
	@PostMapping(value = "/resetPassword", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> resetPassword(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String userName = request.getParameter("userId");
		User user = new User();
		user.setUserId(userName);
		user.setPassword("123456");
		if (userName == null || userName.equals("")) {
			json.put("code", 1);
			json.put("msg", "重置密码是账号不能为空");
			return json;
		}
		// 修改密码成功后，返回首页
		if (userService.updateByPrimaryKeySelective(user) > 0) {
			json.put("code", 0);
			json.put("msg", "重置密码成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "重置密码失败");
		return json;
	}

	/**
	 * 匹配首页
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/homePage")
	public ModelAndView homePage(ModelAndView modelAndView) {
		modelAndView.setViewName("/user/html/main");
		return modelAndView;
	}

	/**
	 * 客户匹配首页
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/client", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> client(ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		Calendar cale = null;
		cale = Calendar.getInstance();
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH) + 1;
		String startDate = year + "-" + month + "-01 00:00:00";
		String endDate = year + "-" + month + "-31 23:59:59";
		// *********************************************************
		String userName = request.getParameter("username");
		String custName = userService.selectByUserId(userName).getCustName();
		if (custName == null) {
			json.put("code", 1);
			json.put("msg", "请登录");
			return json;
		}
		// 客户只能看到自己的合同
		List<Contract> contracts = customerManage.selectByCust(custName, "", "", "", null, null, null, null, null);
		// 获取当月所有工单
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfoByOrder(custName, "", "", startDate,
				endDate);
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		// 总设备数
		List<Device> devices = customerManage.selectByDevice(custName, "", "", "", null, null);
		// 客户资产数
		List<Device> custDevices = customerManage.selectByDevice(custName, "", "", "客户", null, null);
		int serviceDevices = 0;
		if (devices != null && custDevices != null) {
			serviceDevices = devices.size() - custDevices.size()
					- customerManage.selectByDevice(custName, "", "", "客户", null, null).size();
		}
		// 未完成
		int unFinished = 0;
		// 超时工单
		int overOrder = 0;
		// 客户评价
		int customerEvaluation = 0;
		//
		int one = 0; // 1星
		int two = 0; // 2星
		int three = 0; // 3星
		int four = 0; // 4星
		int five = 0; // 5星
		for (ServiceInfo service : serviceInfos) {
			if (!service.getOrderInfo().getWoStatus().equals("已完成")
					&& !service.getOrderInfo().getWoStatus().equals("已关单")
					&& !service.getOrderInfo().getWoStatus().equals("已转单")) {
				unFinished++;
			}
			if (service.getCustScore() != null) {
				customerEvaluation++;
				switch (service.getCustScore()) {
				case 1:
					one++;
					break;
				case 2:
					two++;
					break;
				case 3:
					three++;
					break;
				case 4:
					four++;
					break;
				case 5:
					five++;
					break;
				}
			}
			// 判断是否是超时工单
			if (SOMUtils.isOverTime(service)) {
				overOrder++;
			}
		}
		Contract contract = null;
		if (contracts != null && contracts.size() > 0) {
			contract = contracts.get(0);
			Date fDate = contract.getStartDate();
			Date oDate = contract.getEndDate();
			Date date = new Date();
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.setTime(fDate);
			double day1 = (int) (oDate.getTime() - fDate.getTime()) / (1000 * 3600 * 24); // 合同总期数
			aCalendar.setTime(oDate);
			int day3 = (int) (oDate.getTime() - date.getTime()) / (1000 * 3600 * 24); // 未执行期数
			// 如果当前日期在结束日期后
			if (date.after(fDate)) {
				contract.setContractDeadline(day1); // 合同总期数
				contract.setKpi3(String.valueOf(day1)); // 已经执行期数
				contract.setDueDays(0); // 未执行期数
			} else {
				contract.setContractDeadline(day1); // 合同总期数
				contract.setKpi3(String.valueOf((day3 - day1) / 30)); // 已经执行期数
				contract.setDueDays(day3); // 未执行期数
			}
		}
		json.put("code", 0);
		json.put("方案解决介绍", parameterSettingService.selectByPrimaryKey());
		json.put("合同管理", contract);
		json.put("合同设备数", devices == null ? 0 : devices.size());
		json.put("客户资产", custDevices == null ? 0 : custDevices.size());
		json.put("服务商资产", serviceDevices);
		json.put("本月工单", serviceInfos == null ? 0 : serviceInfos.size());
		json.put("未完成", unFinished);
		json.put("超时工单", overOrder);
		json.put("用户评价", customerEvaluation);
		json.put("one", one);
		json.put("two", two);
		json.put("three", three);
		json.put("four", four);
		json.put("five", five);
		return json;
	}

	/**
	 * 匹配运营总监首页
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/majordomo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> majordomo() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 运营总监可以看到所有客户的合同
		List<Contract> contracts = customerManage.selectByComp("");
		// 查找已到期合同
		List<Contract> timeContracts = customerManage.selectByCust("", "", "1", "", null, null, null, null, null);
		// 查找一年内到期合同
		List<Contract> dueToContracts = customerManage.selectByCust("", "", "", "1", null, null, null, null, null);
		// 运营总监可以看到所有工单
		Calendar cale = null;
		cale = Calendar.getInstance();
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH) + 1;
		String startDate = year + "-" + month + "-01 00:00:00";
		String endDate = year + "-" + month + "-31 23:59:59";
		// 获取当月所有工单
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfoByOrder("", "", "", startDate, endDate);
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		// 未完成
		int unFinished = 0;
		// 超时工单
		int overOrder = 0;
		// 客户投诉
		int complaints = 0;
		// 客户表扬
		int praise = 0;
		// 客户满意度平均分数
		double custScore = 0.0;
		// 客户评价
		int customerEvaluation = 0;
		for (ServiceInfo service : serviceInfos) {
			if (!service.getOrderInfo().getWoStatus().equals("已完成")
					&& !service.getOrderInfo().getWoStatus().equals("已关单")
					&& !service.getOrderInfo().getWoStatus().equals("已转单")) {
				unFinished++;
			}
			if (service.getCustScore() != null && service.getCustScore() <= 3) {
				complaints++;
			} else if (service.getCustScore() != null && service.getCustScore() == 5) {
				praise++;
			}
			if (service.getCustScore() != null) {
				custScore = custScore + service.getCustScore();
				customerEvaluation++;
			}
			if (service.getOrderInfo().getFaultType().equals("事故类")){
				if(SOMUtils.isOverTime(service)) {
					overOrder++;
				}
			}
		}
		json.put("code", 0);
		json.put("合同总数", contracts.size());
		json.put("已到期合同", timeContracts.size());
		json.put("一年内到期合同", dueToContracts.size());
		json.put("本月工单", serviceInfos.size());
		json.put("未完成", unFinished);
		json.put("超时工单", overOrder);
		json.put("用户评价", customerEvaluation);
		json.put("客户投诉", complaints);
		json.put("客户表扬", praise);
		return json;
	}

	/**
	 * 匹配总部客服首页
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/headquartersService", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> headquartersService() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 总部客服可以看到所有工单
		Calendar cale = null;
		cale = Calendar.getInstance();
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH) + 1;
		String startDate = year + "-" + month + "-01 00:00:00";
		String endDate = year + "-" + month + "-31 23:59:59";
		// 本月工单和评价
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfoByOrder("", "", "", startDate, endDate);
		// 待受理
		int unAccept = serviceInfoService.selectServiceInfByWoStatus(null, null, null, null, null, null, "2", null)
				.size();
		// 未完成
		int unFinished = 0;
		// 超时工单
		int overOrder = 0;
		// 客户投诉
		int complaints = 0;
		// 客户表扬
		int praise = 0;
		// 客户评价
		int customerEvaluation = 0;
		for (ServiceInfo service : serviceInfos) {
			if (!service.getOrderInfo().getWoStatus().equals("已完成")
					&& !service.getOrderInfo().getWoStatus().equals("已关单")
					&& !service.getOrderInfo().getWoStatus().equals("已转单")) {
				unFinished++;
			}
			if (service.getCustScore() != null && service.getCustScore() <= 3) {
				complaints++;
			} else if (service.getCustScore() != null && service.getCustScore() == 5) {
				praise++;
			}
			if (service.getCustScore() != null) {
				customerEvaluation++;
			}
			if (service.getOrderInfo().getFaultType().equals("事故类")){
				if(SOMUtils.isOverTime(service)) {
					overOrder++;
				}
			}
		}
		json.put("code", 0);
		json.put("待受理客户事故及需求数量", unAccept);
		json.put("本月工单", serviceInfos.size());
		json.put("未完成", unFinished);
		json.put("超时工单", overOrder);
		json.put("客户评价", customerEvaluation);
		json.put("客户投诉", complaints);
		json.put("客户表扬", praise);
		return json;
	}

	/**
	 * 匹配分公司运维经理首页
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/operations", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> operations(ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端获取登录人账号
		Map<String, Object> json = new HashMap<>();
		// ****************************************************************
		String userName = request.getParameter("username");
		String custName = userService.selectByUserId(userName).getCustName();
		if (custName == null) {
			json.put("code", 1);
			json.put("msg", "请登录");
			return json;
		}
		String identifier=null;
		// 分公司运营总监只能看到属于自己服务区域的合同
		List<Contract> contracts = customerManage.selectByComp(custName);
		// 查找到期合同
		List<Contract> timeContracts = customerManage.selectByCust("", custName, "1", "", null, null, null, null, null);
		// 查找一年内到期合同
		List<Contract> dueToContracts = customerManage.selectByCust("", custName, "", "1", null, null, null, null,
				null);
		// ****************************************************************
		// 分公司运营总监只能看到属于自己服务区域的工单
		Calendar cale = null;
		cale = Calendar.getInstance();
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH) + 1;
		String startDate = year + "-" + month + "-01 00:00:00";
		String endDate = year + "-" + month + "-31 23:59:59";
		// 获取所属分公司的工单号前缀
		String prefix = SOMUtils.orderNumToComp(custName);
		User user = userService.selectByUserId(request.getParameter("username"));
		if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			prefix=null;
			identifier = "1";
		}
		// 获取当月所有工单
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfByWoStatus(prefix, null, startDate, endDate, null, null, null, identifier);
		// 待受理
		int unAccept = serviceInfoService.selectServiceInfByWoStatus(prefix, null, null, null, null, null, "2", identifier)
				.size();
		// 未完成
		int unFinished = 0;
		// 超时工单
		int overOrder = 0;
		// 客户投诉
		int complaints = 0;
		// 客户表扬
		int praise = 0;
		// 客户评价
		int customerEvaluation = 0;
		for (ServiceInfo service : serviceInfos) {
			if (!service.getOrderInfo().getWoStatus().equals("已完成")
					&& !service.getOrderInfo().getWoStatus().equals("已关单")
					&& !service.getOrderInfo().getWoStatus().equals("已转单")) {
				unFinished++;
			}
			if (service.getCustScore() != null && service.getCustScore() <= 3) {
				complaints++;
			} else if (service.getCustScore() != null && service.getCustScore() == 5) {
				praise++;
			}
			if (service.getCustScore() != null) {
				customerEvaluation++;
			}
			if (service.getOrderInfo().getFaultType().equals("事故类")){
				if(SOMUtils.isOverTime(service)) {
					overOrder++;
				}
			}

		}
		json.put("code", 0);
		json.put("待受理客户事故及需求数量", unAccept);
		json.put("合同总数", contracts == null ? 0 : contracts.size());
		json.put("已到期", timeContracts == null ? 0 : timeContracts.size());
		json.put("一年内到期", dueToContracts == null ? 0 : dueToContracts.size());
		json.put("本月工单", serviceInfos.size());
		json.put("未完成", unFinished);
		json.put("超时工单", overOrder);
		json.put("用户评价", customerEvaluation);
		json.put("客户投诉", complaints);
		json.put("客户表扬", praise);
		return json;
	}

	/**
	 * 匹配分公司运维助理首页
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assistant", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> assistant(ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端获取登录人账号
		Map<String, Object> json = new HashMap<>();
		// ****************************************************************
		String userName = request.getParameter("username");
		String custName = userService.selectByUserId(userName).getCustName();
		if (custName == null) {
			json.put("code", 1);
			json.put("msg", "请登录");
			return json;
		}
		// 分公司运营助理只能看到属于自己服务区域的工单
		Calendar cale = null;
		cale = Calendar.getInstance();
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH) + 1;
		String startDate = year + "-" + month + "-01 00:00:00";
		String endDate = year + "-" + month + "-31 23:59:59";
		// 获取所属分公司的工单号前缀
		String prefix = SOMUtils.orderNumToComp(custName);
		User user = userService.selectByUserId(request.getParameter("username"));
		String identifier=null;
		if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			prefix=null;
			identifier = "1";
		}
		// 获取当月所有工单
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfByWoStatus(prefix, null, startDate, endDate, null, null, null, identifier);
		// 未完成
		int unFinished = 0;
		// 超时工单
		int overOrder = 0;
		// 客户投诉
		int complaints = 0;
		// 客户表扬
		int praise = 0;
		// 客户评价
		int customerEvaluation = 0;
		// 零件订购及耗材数量
		int partNumber = serviceInfoService.selectServiceInfByWoStatus(prefix, null, null, null, null, null, "3", identifier).size();
		for (ServiceInfo service : serviceInfos) {
			if (!service.getOrderInfo().getWoStatus().equals("已完成")
					&& !service.getOrderInfo().getWoStatus().equals("已关单")
					&& !service.getOrderInfo().getWoStatus().equals("已转单")) {
				unFinished++;
			}
			if (service.getCustScore() != null && service.getCustScore() <= 3) {
				complaints++;
			} else if (service.getCustScore() != null && service.getCustScore() == 5) {
				praise++;
			}
			if (service.getCustScore() != null) {
				customerEvaluation++;
			}
			if (service.getOrderInfo().getFaultType().equals("事故类")){
				if(SOMUtils.isOverTime(service)) {
					overOrder++;
				}
			}
		}
		json.put("code", 0);
		json.put("本月工单", serviceInfos.size());
		json.put("未完成", unFinished);
		json.put("超时工单", overOrder);
		json.put("用户评价", customerEvaluation);
		json.put("客户投诉", complaints);
		json.put("客户表扬", praise);
		json.put("待定零件和耗材", partNumber);
		return json;
	}

	/**
	 * 匹配分公司运营助理首页:零件及订购数量
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPartAndNumber", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> getPartAndNumber(ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端获取登录人账号
		Map<String, Object> json = new HashMap<>();
		// ****************************************************************
		String userName = request.getParameter("username");
		String custName = userService.selectByUserId(userName).getCustName();
		if (custName == null) {
			json.put("code", 1);
			json.put("msg", "请登录");
			return json;
		}
		// 分公司运营助理只能看到属于自己服务区域的工单
		/*
		 * Calendar cale = null; cale = Calendar.getInstance(); int year =
		 * cale.get(Calendar.YEAR); int month = cale.get(Calendar.MONTH) + 1;
		 * String startDate = year + "-" + month + "-01 00:00:00"; String
		 * endDate = year + "-" + month + "-31 23:59:59";
		 */
		String prefix = SOMUtils.orderNumToComp(custName);
		// 获取当月所有工单
		User user = userService.selectByUserId(userName);
		String identifier=null;
		if (user.getCustName().equals("广州乐派数码科技有限公司") || user.getCustName().equals("系统推进部")
				|| user.getCustName().equals("行业客户部")) {
			prefix=null;
			identifier = "1";
		}
		// 获取所属分公司的工单号前缀
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfByWoStatus(prefix, null, null, null, null, null, "3", identifier);
		List<OrderInfo> orders = new ArrayList<>();
		for (ServiceInfo serviceInfo : serviceInfos) {
			serviceInfo.getOrderInfo().setEnginner(serviceInfo.getStaffInfo().getName());
			orders.add(serviceInfo.getOrderInfo());
		}
		json.put("code", 0);
		json.put("待定零件及耗材", orders);
		return json;
	}

	/**
	 * 根据微信扫一扫状态，告诉前端应该跳转哪个页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getOpenId", produces = "application/json; charset=utf-8")
	public ModelAndView getOpenId(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 从前端接受数据
		String code = request.getParameter("code"); // code
		String machCode = request.getParameter("machCode"); // 机器编码
		String custName = customerManage.selectByCode(machCode).getCustArea(); // 所属客户名称
		Integer custId = custInfoService.selectCusIdByName(custName);
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", code);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		String openid = jsonObject.getString("openid");
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openid)) {
				String lujing = "";
				if (userService.selectByPrimaryKey(user.getUserId()).getRoleId().equals("客户")) {
					lujing = "redirect:/weChat/index.html";
				} else if (userService.selectByPrimaryKey(user.getUserId()).getRoleId().equals("工程师")) {
					lujing = "redirect:/weChat/index.html";
				}
				/*
				 * // 判断拥有哪些权限 List<OrderInfo> orderInfos =
				 * serviceManageServiceImpl.selectOrderByDynamic(null, machCode,
				 * null, null, null, null, null); if (orderInfos == null ||
				 * orderInfos.size() <= 0) { modelAndView.setViewName(lujing);
				 * return modelAndView; } if
				 * (orderInfos.get(0).getWoStatus().equals("已处理")) {
				 * modelAndView.setViewName(lujing); return modelAndView; } if
				 * (orderInfos.get(0).getWoStatus().equals("已关单")) {
				 * modelAndView.setViewName(lujing); return modelAndView; }
				 */
				modelAndView.setViewName(lujing);
				return modelAndView;
			}
		}
		// 如果没有该用户，则跳转至注册页面
		modelAndView
				.setViewName("redirect:http://solutionyun.com/weChat/index.html#/engineer/register?custId=" + custId);
		return modelAndView;
	}

	/**
	 * 判断用户是否登陆，如果有openId则免登陆，否则调至登陆页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/isNeedLogin", produces = "application/json; charset=utf-8")
	public ModelAndView isLogin(ModelAndView modelAndView) throws IOException {
		// 获取openId
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 从前端接受数据
		String code = request.getParameter("code"); // code
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", code);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		String openid = jsonObject.getString("openid");
		for (User user2 : userService.selectAllUser()) {
			if (user2.getOpenId() == null) {
				continue;
			}
			if (user2.getOpenId().equals(openid)) {
				UserRole userRole = userService.selectByPrimaryKey(user2.getUserId());
				switch (userRole.getRoleId()) {
				case "客户":
					modelAndView.setViewName("redirect:http://solutionyun.com/weChat/index.html?openId=" + openid);
					return modelAndView;
				case "工程师":
					modelAndView.setViewName("redirect:http://solutionyun.com/weChat/index.html?openId=" + openid);
					return modelAndView;
				case "技术主管":
					modelAndView.setViewName("redirect:http://solutionyun.com/weChat/index.html?openId=" + openid);
					return modelAndView;
				case "运维助理":
					modelAndView.setViewName("redirect:http://solutionyun.com/weChat/index.html?openId=" + openid);
					return modelAndView;
				default:
					break;
				}
			}
		}
		modelAndView.setViewName("redirect:/weChat/index.html");
		return modelAndView;
	}

	@Test
	public void test() throws IOException {
		String GETOPENID = "http://solutionyun.com/som/dologin?username=18011446524&password=123456";
		String path = GETOPENID;
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		URLConnection httpUrlConn = url.openConnection();
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		System.out.println(buffer.toString());
	}

	/**
	 * 获取OPENID
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/obtainOpenId", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> obtainOpenId(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 从前端接受数据
		String code = request.getParameter("code"); // code
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", code);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		String openid = jsonObject.getString("openid");
		Map<String, Object> json = new HashMap<>();
		json.put("code", 0);
		json.put("openid", openid);
		return json;
	}

	/**
	 * 获取OPENID
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/obtainOpenId2", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> obtainOpenId2(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 从前端接受数据
		String username = request.getParameter("username");
		String code = request.getParameter("code"); // code
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", code);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		String openid = jsonObject.getString("openid");
		Map<String, Object> json = new HashMap<>();
		json.put("code", 0);
		json.put("openid", openid);
		json.put("username", username);
		return json;
	}

	/**
	 * getCode回调地址
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getOpenIdCode", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> getOpenIdCode() throws IOException {
		String a = UserLoginController.doGet(
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%2fobtainOpenId%3fmachCode%3dGZZ0090&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
		Map<String, Object> json = new HashMap<>();
		json.put("code", 0);
		json.put("openId", a);
		return json;
	}

	public static String doGet(String httpurl) {
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br = null;
		String result = null;// 返回结果字符串
		try {
			// 创建远程url连接对象
			URL url = new URL(httpurl);
			// 通过远程url连接对象打开一个连接，强转成httpURLConnection类
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod("GET");
			// 设置连接主机服务器的超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(60000);
			// 发送请求
			connection.connect();
			// 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				// 封装输入流is，并指定字符集
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				// 存放数据
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			connection.disconnect();// 关闭远程连接
		}
		return result;
	}

	/**
	 * 根据微信扫一扫状态，告诉前端应该跳转哪个页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getOpenId2", produces = "application/json; charset=utf-8")
	public ModelAndView getOpenId2(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 从前端接受数据
		String username = request.getParameter("username"); // username
		String code = request.getParameter("code"); // code
		String machCode = request.getParameter("machCode"); // 机器编码
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", code);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		String openid = jsonObject.getString("openid");
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openid)) {
				String lujing = "";
				if (userService.selectByPrimaryKey(user.getUserId()).getRoleId().equals("客户")) {
					lujing = "redirect:/weChat/index.html";
				} else if (userService.selectByPrimaryKey(user.getUserId()).getRoleId().equals("工程师")) {
					lujing = "redirect:/weChat/index.html";
				}
				/*
				 * // 判断拥有哪些权限 List<OrderInfo> orderInfos =
				 * serviceManageServiceImpl.selectOrderByDynamic(null, machCode,
				 * null, null, null, null, null); if (orderInfos == null ||
				 * orderInfos.size() <= 0) { modelAndView.setViewName(lujing);
				 * return modelAndView; } if
				 * (orderInfos.get(0).getWoStatus().equals("已处理")) {
				 * modelAndView.setViewName(lujing); return modelAndView; } if
				 * (orderInfos.get(0).getWoStatus().equals("已关单")) {
				 * modelAndView.setViewName(lujing); return modelAndView; }
				 */
				modelAndView.setViewName(lujing);
				return modelAndView;
			}
		}
		// 如果没有该用户，则跳转至注册页面
		modelAndView.setViewName("redirect:http://solutionyun.com/weChat/index.html#/engineer/register");
		return modelAndView;
	}
	
	/**
	 * 匹配分公司运维助理首页
	 * 
	 * @param modelAndView
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCalendar", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> getCalendar() throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端获取登录人账号
		Map<String, Object> json = new HashMap<>();
		json.put("data", calendarService.selectAllCalendar());
		return json;
	}
	
}
