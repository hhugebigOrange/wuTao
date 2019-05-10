package com.xunwei.som.base.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.calendar.CalendarTool;
import com.xunwei.som.pojo.EngineerKpi;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.permissions.ParameterSetting;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.ServiceInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SMS;
import com.xunwei.som.util.SOMUtils;

@Controller
public class HomePageController extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private ServiceInfoService serviceInfoService = new ServiceInfoServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();

	private UserService userService = new UserServiceImpl();
	
	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	/**
	 * 匹配首页,浮动窗显示待处理工单个数
	 */
	@RequestMapping("/home")
	public ModelAndView homePage(ModelAndView modelAndView) {
		int orderNumber = serviceManageServiceImpl.getOrderByWoStatus("未处理");
		modelAndView.addObject("orderNumber", orderNumber);
		modelAndView.setViewName("/homePage/html/home");
		return modelAndView;
	}

	/**
	 * 匹配首页,浮动窗显示待处理工单个数
	 */
	@RequestMapping("/weChat")
	public ModelAndView weChat(ModelAndView modelAndView) {
		modelAndView.setViewName("/user/html/index");
		return modelAndView;
	}

	/* 工程师接单流程控制↓ */
	/**
	 * 第零步：直接转单
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/startTurnOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> startTurnOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String turnOrderReson = request.getParameter("turnOrderReson"); // 转单原因
		// 参数非空判断
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("转单原因", turnOrderReson);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 判断是否已经转单过
		// 找出之前的工单
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，您要转单的工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		/*
		 * if (!service.getOrderInfo().getWoStatus().equals("已受理")) {
		 * json.put("code", 1); json.put("msg", "对不起，此工单已经被转单过一次，不能再次转单");
		 * return json; }
		 */
		if (woNumber.length() > 14) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经被转单过一次，不能再次转单");
			return json;
		}
		if (serviceManageServiceImpl.selectOrderByOrderNum(woNumber + "-1") != null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经转单，转单后的工单号为" + woNumber + "-1");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 当前工单完结，显示为已转单
		serviceManageServiceImpl.updateWoStatus(woNumber, "已转单");
		OrderInfo oldOrderinfo = new OrderInfo();
		oldOrderinfo.setWoNumber(woNumber);
		oldOrderinfo.setTurnOrderReson(turnOrderReson);
		oldOrderinfo.setWoStatus("已转单");
		oldOrderinfo.setWoProgress("已转单");
		serviceManageServiceImpl.updateOrder(oldOrderinfo);
		// 生成新的工单
		// 如果没有机器编码，则新生成的为没有机器编码的工单
		if (service.getOrderInfo().getMachCode() == null || service.getOrderInfo().getMachCode().equals("")) {
			// 执行插入方法
			OrderInfo orderinfo = new OrderInfo();
			orderinfo.setWoNumber(service.getOrderInfo().getWoNumber() + "-1");
			orderinfo.setPriority(service.getOrderInfo().getPriority());
			orderinfo.setFaultType(service.getOrderInfo().getFaultType());
			orderinfo.setCustName(service.getOrderInfo().getCustName());
			orderinfo.setCustId(service.getOrderInfo().getCustId());
			orderinfo.setRepairMan(service.getOrderInfo().getRepairMan());
			orderinfo.setRepairService(service.getOrderInfo().getRepairService());
			orderinfo.setCustAddr(service.getOrderInfo().getCustAddr());
			orderinfo.setRepairTime(service.getOrderInfo().getRepairTime());
			orderinfo.setAccidentType(service.getOrderInfo().getAccidentType());
			orderinfo.setServiceType(service.getOrderInfo().getServiceType());
			orderinfo.setRepairType(service.getOrderInfo().getRepairType());
			orderinfo.setRemark(service.getOrderInfo().getRemark());
			orderinfo.setTurnOrderReson(turnOrderReson);
			orderinfo.setDistributeMan(service.getOrderInfo().getDistributeMan());
			orderinfo.setOrderAccount(service.getOrderInfo().getOrderAccount());
			boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
			if (result) {
				// 如果工单增加成功，则在增加服务评价
				ServiceInfo serviceInfo = new ServiceInfo();
				serviceInfo.setWoNumber(orderinfo.getWoNumber());
				json.put("code", 0);
				json.put("msg", "转单成功");
				return json;
			}
		} else {
			// 执行插入方法
			OrderInfo orderinfo = new OrderInfo();
			orderinfo.setWoNumber(service.getOrderInfo().getWoNumber() + "-1");
			orderinfo.setFaultType(service.getOrderInfo().getFaultType());
			orderinfo.setPriority(service.getOrderInfo().getPriority());
			orderinfo.setEsNumber(service.getOrderInfo().getEsNumber());
			orderinfo.setCustName(service.getOrderInfo().getCustName());
			orderinfo.setCustId(service.getOrderInfo().getCustId());
			orderinfo.setDevName(service.getOrderInfo().getDevName());
			orderinfo.setMachCode(service.getOrderInfo().getMachCode());
			orderinfo.setRepairMan(service.getOrderInfo().getRepairMan());
			orderinfo.setRepairService(service.getOrderInfo().getRepairService());
			orderinfo.setCustAddr(service.getOrderInfo().getCustAddr());
			orderinfo.setRepairTime(service.getOrderInfo().getRepairTime());
			orderinfo.setAccidentType(service.getOrderInfo().getAccidentType());
			orderinfo.setServiceType(service.getOrderInfo().getServiceType());
			orderinfo.setRepairType(service.getOrderInfo().getRepairType());
			orderinfo.setRemark(service.getOrderInfo().getRemark());
			orderinfo.setTurnOrderReson(turnOrderReson);
			orderinfo.setDistributeMan(service.getOrderInfo().getDistributeMan());
			orderinfo.setOrderAccount(service.getOrderInfo().getOrderAccount());
			service.setDevice(customerManage.selectByCode(orderinfo.getMachCode()));
			boolean result = serviceManageServiceImpl.insertOrder(orderinfo);
			if (result) {
				// 如果工单增加成功，则在增加服务评价
				ServiceInfo serviceInfo = new ServiceInfo();
				serviceInfo.setWoNumber(orderinfo.getWoNumber());
				serviceInfo.setStaffId(service.getDevice().getResponsibleEngineerID());
				serviceManageServiceImpl.insertSelective(serviceInfo);
				json.put("code", 0);
				json.put("msg", "转单成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "转单失败，请联系客服");
		return json;
	}

	/**
	 * 第一步：工程师接单
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/acceptOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> acceptOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber");
		// 参数非空判断
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		String woStatus = "已接单";
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断该工单的状态
		if (service.getState() != null && !service.getState().equals("")) {
			json.put("code", 1);
			json.put("msg", "该工单已经被接受了，请不要重复接受");
			return json;
		}
		// 更新工程师的接单时间
		int a = serviceManageServiceImpl.updateWoStatus(woNumber, woStatus);
		if (a > 0) {
			// 接单成功，更新工单的流程状态，并发送短信到报修人联系电话里
			OrderInfo order = new OrderInfo();
			order.setWoNumber(woNumber);
			order.setWoProgress(woStatus);
			serviceManageServiceImpl.updateOrder(order);
			service.setState("1,true");
			serviceInfoService.upDateServiceInfo(service);
			//如果是事故单，则发送短信
			if(service.getOrderInfo().getFaultType().equals("事故类")){
				SMS.senMessage(SMS.SERVICE_ACCEPTANCE, service.getOrderInfo().getRepairService(),
						service.getOrderInfo().getRepairMan(), woNumber);
			}
			json.put("code", 0);
			json.put("msg", "接单成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "接单失败，请联系客服");
		return json;
	}

	/**
	 * 第二步：致电客户，点击确认时调用该接口
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/phoneOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> phoneOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		// 参数非空判断
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断是否接单
		if (service.getState() == null || !service.getState().contains("1")) {
			json.put("code", 1);
			json.put("msg", "对不起，请先接单再致电客户");
			return json;
		}
		// 判断是否已经致电过
		if (service.getState() != null && service.getState().contains("2")) {
			json.put("code", 1);
			json.put("msg", "您已操作过致电流程，请进行下一流程");
			return json;
		}
		// 记录电话响应时间
		service.setTelRepon(new Date());
		service.setState(service.getState() + ",2,true");
		// 更新工程师状态
		if (serviceInfoService.upDateServiceInfo(service)) {
			// 接单成功，更新工单的流程状态
			OrderInfo order = new OrderInfo();
			order.setWoNumber(woNumber);
			order.setWoStatus("处理中");
			order.setWoProgress("已致电，请选择是否解决");
			serviceManageServiceImpl.updateOrder(order);
			json.put("code", 0);
			json.put("msg", "致电客户成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "致电客户过程中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第三步：判断工程师致电客户后是否解决了问题
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/phoneOrderIsOver", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> phoneOrderIsOver() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String isOver = request.getParameter("isOver"); // 是否解决
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("是否电话解决", isOver);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断是否进行上一个流程
		if (service.getState() == null || !service.getState().contains("2")) {
			json.put("code", 1);
			json.put("msg", "对不起，选择是否电话解决前，请先致电客户");
			return json;
		}
		// 只有打过电话的工单，才能弹出进行是否电话解决的选择
		if (service.getState() != null && !service.getState().contains("2,true")) {
			json.put("code", 1);
			json.put("msg", "对不起，您选择了不致电客户，请直接到达现场处理");
			return json;
		}
		// 判断是否已经做过了该步骤
		if (service.getState() != null && service.getState().contains("3")) {
			json.put("code", 1);
			json.put("msg", "您已选择了是或者否，请不要重复选择");
			return json;
		}
		// 当工程师选择已经电话解决时
		if (isOver.equals("1")) {
			// 更新状态，记录到达现场时间和问题解决时间
			service.setState(service.getState() + ",3,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				// 接单成功，更新工单的流程状态
				OrderInfo order = new OrderInfo();
				order.setWoNumber(woNumber);
				order.setWoStatus("处理中");
				order.setWoProgress("电话已解决，请填写反馈");
				serviceManageServiceImpl.updateOrder(order);
				json.put("code", 0);
				json.put("msg", "电话解决了问题");
				json.put("show", false);
				return json;
			}
		} else {
			service.setState(service.getState() + ",3,false");
			if (serviceInfoService.upDateServiceInfo(service)) {
				// 接单成功，更新工单的流程状态
				OrderInfo order = new OrderInfo();
				order.setWoNumber(woNumber);
				order.setWoStatus("处理中");
				order.setWoProgress("电话未解决，请到达现场");
				serviceManageServiceImpl.updateOrder(order);
				json.put("code", 0);
				json.put("msg", "电话未能解决问题，准备到达现场");
				json.put("show", false);
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "电话解决过程中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第四步：到达现场 事故类，需要扫码
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/arriveOrderAccident", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> arriveOrderAccident() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String machCode = request.getParameter("machCode"); // 机器编码
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("机器编码", machCode);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断工单是否需要扫码
		if (!service.getOrderInfo().getFaultType().equals("事故类")) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单不是事故类，不能扫码到达现场");
			return json;
		}
		// 判断该工单是否已经选择了电话处理完成
		if (service.getState() != null && service.getState().contains("3,true")) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经电话处理完成");
			return json;
		}
		// 判断该工单是否已经走过该流程
		if (service.getState() != null && service.getState().contains("4")) {
			json.put("code", 1);
			json.put("msg", "对不起，您已经到达过现场，请不要重复扫码");
			return json;
		}
		// 判断扫描的二维码是否跟工单里的一致
		if (!service.getOrderInfo().getMachCode().equals(machCode)) {
			json.put("code", 1);
			json.put("msg", "对不起，该机器编码与工单里的机器编码不匹配");
			return json;
		}
		// 判断是否致电客户，如果已经致电，不用设置电话响应时间
		if (service.getState().contains("2")) {
			service.setArrTime(new Date());
			service.setWoNumber(woNumber);
			service.setState(service.getState() + ",4,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				// 接单成功，更新工单的流程状态
				OrderInfo order = new OrderInfo();
				order.setWoNumber(woNumber);
				order.setWoStatus("处理中");
				order.setWoProgress("已到达现场");
				serviceManageServiceImpl.updateOrder(order);
				json.put("code", 0);
				json.put("msg", "扫码到达现场成功");
				return json;
			}
		} else {
			// 判断是否致电客户，如果没有致电，则电话响应时间与到达现场时间一致
			service.setArrTime(new Date());
			service.setWoNumber(woNumber);
			service.setState(service.getState() + ",4,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				// 接单成功，更新工单的流程状态
				OrderInfo order = new OrderInfo();
				order.setWoNumber(woNumber);
				order.setWoStatus("处理中");
				order.setWoProgress("已到达现场");
				serviceManageServiceImpl.updateOrder(order);
				json.put("code", 0);
				json.put("msg", "扫码到达现场成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "到达现场过程中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第四步：到达现场 事故类，不需扫码
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/arriveOrderOther", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> arriveOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断工单是否需要扫码
		if (service.getOrderInfo().getFaultType().equals("事故类")) {
			json.put("code", 1);
			json.put("msg", "对不起，工单不是事故类，需要扫码到达现场");
			return json;
		}
		// 判断该工单是否已经选择了电话处理完成
		if (service.getState() != null && service.getState().contains("3,true")) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经电话处理完成");
			return json;
		}
		// 判断该工单是否已经走过该流程
		if (service.getState() != null && service.getState().contains("4")) {
			json.put("code", 1);
			json.put("msg", "对不起，您已经到达过现场，请不要重复扫码");
			return json;
		}
		// 判断是否致电客户，如果已经致电，不用设置电话响应时间
		if (service.getState().contains("2")) {
			service.setArrTime(new Date());
			service.setWoNumber(woNumber);
			service.setState(service.getState() + ",4,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				// 接单成功，更新工单的流程状态
				OrderInfo order = new OrderInfo();
				order.setWoNumber(woNumber);
				order.setWoStatus("处理中");
				order.setWoProgress("已到达现场，未反馈");
				serviceManageServiceImpl.updateOrder(order);
				json.put("code", 0);
				json.put("msg", "到达现场成功");
				return json;
			}
		} else {
			// 判断是否致电客户，如果没有致电，则电话响应时间与到达现场时间一致
			service.setArrTime(new Date());
			service.setWoNumber(woNumber);
			service.setState(service.getState() + ",4,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				// 接单成功，更新工单的流程状态
				OrderInfo order = new OrderInfo();
				order.setWoNumber(woNumber);
				order.setWoStatus("处理中");
				order.setWoProgress("已到达现场，未反馈");
				serviceManageServiceImpl.updateOrder(order);
				json.put("code", 0);
				json.put("msg", "到达现场成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "到达现场过程中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第五步：点击转单升级时调用接口
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/turnOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> turnOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String turnOrderReson = request.getParameter("turnOrderReson"); // 转单原因
		// 进行非空判断
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("转单原因", turnOrderReson);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		if (woNumber.length() > 14) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经被转单过一次，不能再次转单");
			return json;
		}
		/*
		 * if (!service.getOrderInfo().getWoStatus().equals("已受理")) {
		 * json.put("code", 1); json.put("msg", "对不起，此工单已经被转单过一次，不能再次转单");
		 * return json; }
		 */
		// 过来肯定是14位
		if (serviceManageServiceImpl.selectOrderByOrderNum(woNumber + "-1") != null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单已经转单，转单后的工单号为" + woNumber + "-1");
			return json;
		}
		// 判断工单是否需要扫码
		if (!service.getOrderInfo().getFaultType().equals("事故类")) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单不是事故类，不能转单升级");
			return json;
		}
		// 判断是否进行上一个流程
		if (service.getState() == null || !service.getState().contains("4")) {
			json.put("code", 1);
			json.put("msg", "对不起，请先扫码确认到达现场");
			return json;
		}
		// 判断该工单是否已经走过该流程
		if (service.getState() != null && service.getState().contains("5")) {
			json.put("code", 1);
			json.put("msg", "对不起，此工单已经转单，请不要重复操作");
			return json;
		}
		// 当前工单关单，并设置状态为已转单
		service.setState(service.getState() + ",5,true");
		serviceInfoService.upDateServiceInfo(service);
		// 更新工单转单时间
		serviceManageServiceImpl.updateWoStatus(woNumber, "已转单");
		// 转单成功，更新工单的流程状态
		OrderInfo order = new OrderInfo();
		order.setWoNumber(woNumber);
		order.setTurnOrderReson(turnOrderReson);
		order.setWoStatus("已转单");
		order.setWoProgress("已转单");
		serviceManageServiceImpl.updateOrder(order);
		// 生成新订单，并指派给技术主管
		OrderInfo orderinfo = new OrderInfo();
		orderinfo.setWoNumber(woNumber + "-1");
		orderinfo.setFaultType(service.getOrderInfo().getFaultType());
		orderinfo.setEsNumber(service.getOrderInfo().getEsNumber());
		orderinfo.setCustName(service.getOrderInfo().getCustName());
		orderinfo.setCustId(service.getOrderInfo().getCustId());
		orderinfo.setDevName(service.getOrderInfo().getDevName());
		orderinfo.setMachCode(service.getOrderInfo().getMachCode());
		orderinfo.setRepairMan(service.getOrderInfo().getRepairMan());
		orderinfo.setRepairService(service.getOrderInfo().getRepairService());
		orderinfo.setCustAddr(service.getOrderInfo().getCustAddr());
		orderinfo.setRepairTime(service.getOrderInfo().getRepairTime());
		orderinfo.setAccidentType(service.getOrderInfo().getAccidentType());
		orderinfo.setServiceType(service.getOrderInfo().getServiceType());
		orderinfo.setRepairType(service.getOrderInfo().getRepairType());
		orderinfo.setRemark(service.getOrderInfo().getRemark());
		orderinfo.setSendTime(service.getOrderInfo().getSendTime()); // 发送派单时间
		orderinfo.setTurnOrderReson(turnOrderReson);
		orderinfo.setDistributeMan(service.getOrderInfo().getDistributeMan());
		orderinfo.setWoStatus("已接单");
		orderinfo.setWoProgress("已接单");
		orderinfo.setOrderAccount(service.getOrderInfo().getOrderAccount());
		serviceManageServiceImpl.insertOrder(orderinfo);
		// 生成相应的服务评价
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setWoNumber(woNumber + "-1");
		serviceInfo.setState("1,true");
		List<StaffInfo> StaffInfo = staffInfoServiceImplnew.selectAllStaff();
		String compName = SOMUtils.CompToOrderNumTo(woNumber.substring(0, 2));
		for (StaffInfo staffInfo : StaffInfo) {
			if (staffInfo.getPost().equals("技术主管") && staffInfo.getCompName().equals(compName)) {
				serviceInfo.setStaffId(staffInfo.getStaffId());
				break;
			}
		}
		// 更新服务评价
		if (serviceManageServiceImpl.insertSelective(serviceInfo) > 0) {
			json.put("code", 0);
			json.put("msg", "转单成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "转单失败，请联系管理员");
		return json;
	}

	/**
	 * 第六步：点击零件订购时
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/partsOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> partsOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String partsTypeNumber0 = request.getParameter("partsTypeNumber[0][model]"); // 零件种类
		String partsTypeNumber1 = request.getParameter("partsTypeNumber[0][number]"); // 数量
		String partsTypeNumber2 = request.getParameter("partsTypeNumber[1][model]"); // 零件种类
		String partsTypeNumber3 = request.getParameter("partsTypeNumber[1][number]"); // 数量
		String partsTypeNumber4 = request.getParameter("partsTypeNumber[2][model]"); // 零件种类
		String partsTypeNumber5 = request.getParameter("partsTypeNumber[2][number]"); // 数量
		String partsTypeNumber6 = request.getParameter("partsTypeNumber[3][model]"); // 零件种类
		String partsTypeNumber7 = request.getParameter("partsTypeNumber[3][number]"); // 数量
		String partsTypeNumber8 = request.getParameter("partsTypeNumber[4][model]"); // 零件种类
		String partsTypeNumber9 = request.getParameter("partsTypeNumber[4][number]"); // 数量
		String orderReason=request.getParameter("orderReason"); //零件订购原因
		// 进行非空判断
		String partsTypeNumber = partsTypeNumber0 + partsTypeNumber1 + partsTypeNumber2 + partsTypeNumber3
				+ partsTypeNumber4 + partsTypeNumber5 + partsTypeNumber6 + partsTypeNumber7 + partsTypeNumber8
				+ partsTypeNumber9;
		// 将传递过来的值存放到Map集合里面
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("零件种类:数量", partsTypeNumber);
		params.put("零件订购原因", orderReason);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断工单是否需要扫码
		if (!service.getOrderInfo().getFaultType().equals("事故类")) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单不是事故类，不能零件订购");
			return json;
		}
		// 判断是否进行上一个流程
		if (service.getState() == null || !service.getState().contains("4")) {
			json.put("code", 1);
			json.put("msg", "对不起，请先确认到达现场");
			return json;
		}
		String[] partsTypeNumberList = { partsTypeNumber0, partsTypeNumber1, partsTypeNumber2, partsTypeNumber3,
				partsTypeNumber4, partsTypeNumber5, partsTypeNumber6, partsTypeNumber7, partsTypeNumber8,
				partsTypeNumber9 };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < partsTypeNumberList.length; i++) {
			if (partsTypeNumberList[i] == null || partsTypeNumberList[i].equals("")) {
				continue;
			}
			sb.append(partsTypeNumberList[i]);
			if (i % 2 == 0) {
				sb.append(" ");
			} else if (i % 2 != 0) {
				sb.append(";");
			}
		}
		// 更新零件订购信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoNumber(woNumber);
		orderInfo.setTreatmentState(orderReason);
		orderInfo.setPartsTypeNumber(sb.toString());
		orderInfo.setWoStatus("待订件，等待二次上门");
		orderInfo.setWoProgress("待审批");
		if (serviceManageServiceImpl.updateOrder(orderInfo)) {
			service.setState(service.getState() + ",6,true");
			serviceInfoService.upDateServiceInfo(service);
			json.put("code", 0);
			json.put("msg", "零件订购申请提交成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "零件订购申请过程中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第七步：完成反馈，事故单
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/overOrderAccident", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> overOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String faultClass = request.getParameter("faultClass"); // 故障现象分类
		String faultNo = request.getParameter("faultNo"); // 故障分类代码
		String bwReader = request.getParameter("bwReader"); // 黑白读数
		String coReader = request.getParameter("coReader"); // 彩色读数
		String treatmentMeasure = request.getParameter("treatmentMeasure"); // 处理措施
		String maintenanceFeedback = request.getParameter("maintenanceFeedback"); // 故障描述
		// 参数非空判断
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("故障现象分类", faultClass);
		params.put("故障分类代码", faultNo);
		params.put("黑白读数", bwReader);
		params.put("彩色读数", coReader);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		if (service.getState() != null && service.getState().contains("7")) {
			json.put("code", 1);
			json.put("msg", "对不起，此工单已经解决");
			return json;
		}
		// 判断openId所属的角色是哪个
		String openId = request.getParameter("openId"); // openId
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		String role = "";
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openId)) {
				role = userService.selectByPrimaryKey(user.getUserId()).getRoleId();
			}
		}
		if (role.equals("")) {
			json.put("code", 1);
			json.put("msg", "对不起，改openId对应的用户不存在");
			return json;
		}
		if (role.equals("工程师")) {
			// 更新工单信息
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setWoNumber(woNumber);
			orderInfo.setFaultClass(faultClass);
			orderInfo.setFalutNo(faultNo);
			orderInfo.setTreatmentMeasure(treatmentMeasure);
			orderInfo.setMaintenanceFeedback(maintenanceFeedback);
			orderInfo.setWoStatus("已完成");
			orderInfo.setWoProgress("已完成");
			serviceManageServiceImpl.updateOrder(orderInfo);
			Device device=new Device();
			device.setMachCode(service.getOrderInfo().getMachCode());
			device.setBwReader(bwReader);
			device.setColorReader(coReader);
			customerManage.updateByPrimaryKeySelective(device);
			// 更新服务信息
			service.setProbSolve(new Date());
			service.setState(service.getState() + ",7,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				SMS.senMessage(SMS.COMPLETION, service.getOrderInfo().getRepairService(),
						service.getOrderInfo().getRepairMan(), woNumber);
				json.put("code", 0);
				json.put("msg", "服务反馈成功");
				return json;
			}
		} else if (role.equals("技术主管") || role.equals("运维经理")) {
			// 更新工单信息
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setWoNumber(woNumber);
			orderInfo.setFaultClass(faultClass);
			orderInfo.setFalutNo(faultNo);
			orderInfo.setTreatmentMeasure(treatmentMeasure);
			orderInfo.setMaintenanceFeedback(maintenanceFeedback);
			orderInfo.setWoStatus("已关单");
			orderInfo.setWoProgress("已关单");
			serviceManageServiceImpl.updateOrder(orderInfo);
			Device device=new Device();
			device.setMachCode(service.getOrderInfo().getMachCode());
			device.setBwReader(bwReader);
			device.setColorReader(coReader);
			customerManage.updateByPrimaryKeySelective(device);
			// 更新服务信息
			service.setProbSolve(new Date());
			service.setState(service.getState() + ",7,true");
			if (serviceInfoService.upDateServiceInfo(service)) {
				SMS.senMessage(SMS.COMPLETION, service.getOrderInfo().getRepairService(),
						service.getOrderInfo().getRepairMan(), woNumber);
				json.put("code", 0);
				json.put("msg", "服务反馈成功");
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "服务反馈中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第七步：完成反馈，事件单、需求单
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/overOrderOther", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> overOrderOther() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		String treatmentMeasure = request.getParameter("treatmentMeasure"); // 处理措施
		// 参数非空判断
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("工单号", woNumber);
		params.put("处理措施", treatmentMeasure);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		if (service == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该工单号不存在");
			return json;
		}
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 判断是否进行上一个流程
		/*
		 * if (service.getState() == null || !service.getState().contains("4"))
		 * { json.put("code", 1); json.put("msg", "对不起，请先确认到达现场"); return json;
		 * }
		 */
		// 判断该工单是否已经走过该流程
		if (service.getState() != null && service.getState().contains("7")) {
			json.put("code", 1);
			json.put("msg", "对不起，此工单已经解决");
			return json;
		}
		// 更新工单信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoNumber(woNumber);
		orderInfo.setTreatmentMeasure(treatmentMeasure);
		orderInfo.setWoStatus("已完成");
		orderInfo.setWoProgress("已完成");
		serviceManageServiceImpl.updateOrder(orderInfo);
		// 更新服务信息
		service.setProbSolve(new Date());
		service.setState(service.getState() + ",7,true");
		if (serviceInfoService.upDateServiceInfo(service)) {
			//如果是需求单，给客户发短信通知
			if(service.getOrderInfo().getFaultType().equals("需求类")){
				SMS.senMessage(SMS.COMPLETION, service.getOrderInfo().getRepairService(),
						service.getOrderInfo().getRepairMan(), woNumber);
			}
			json.put("code", 0);
			json.put("msg", "服务反馈成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "服务反馈中出现异常，请联系客服");
		return json;
	}

	/**
	 * 第七步：根据工单号判断当前工单下一步应该跳转哪个页面
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/orderState", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderState() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber"); // 工单号
		ServiceInfo service = serviceInfoService.selectServiceInfByDyWoNumber(woNumber);
		service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(woNumber));
		// 如果步骤为空，跳转接单页面
		if (service.getState() == null || service.getState().equals("")) {
			json.put("code", 0);
			json.put("msg", "接单页面");
			return json;
		}
		// 判断用户是否电话响应了，没点则跳转电话响应页面
		if (service.getState() != null && !service.getState().contains("2")) {
			json.put("code", 0);
			json.put("msg", "电话响应页面");
			return json;
		}
		// 如果用户点了电话响应，则跳转是否电话就
		if (service.getState() != null && !service.getState().contains("3") && service.getState().contains("2,true")) {
			json.put("code", 0);
			json.put("msg", "电话响应页面");
			return json;
		}
		// 更新服务信息
		service.setProbSolve(new Date());
		service.setState(",7,true");
		if (serviceInfoService.upDateServiceInfo(service)) {
			json.put("code", 0);
			json.put("msg", "服务反馈成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "服务反馈中出现异常，请联系客服");
		return json;
	}

	/**
	 * 接口：修改工单反馈
	 * 
	 * @param 工单号
	 * @param 更改过后的状态
	 * @return
	 */
	@RequestMapping(value = "/upDateOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> upDateOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String woNumber = request.getParameter("woNumber");
		String maintenanceFeedback = request.getParameter("maintenanceFeedback");
		String faultClass = request.getParameter("faultClass");
		String falutNo = request.getParameter("falutNo");
		// 建立order对象
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setWoNumber(woNumber);
		orderInfo.setMaintenanceFeedback(maintenanceFeedback);
		orderInfo.setFaultClass(faultClass);
		orderInfo.setFalutNo(falutNo);
		// 调用修改工单状态的方法
		boolean result = serviceManageServiceImpl.updateOrder(orderInfo);
		if (result) {
			json.put("code", 0);
			json.put("data", "修改成功");
			json.put("msg", "修改成功");
			return json;
		} else {
			json.put("code", 1);
			json.put("data", "修改失败");
			json.put("msg", "修改失败");
			return json;
		}
	}

	/**
	 * 接口：查看待购零件订单
	 * 
	 * @param 工单号
	 * @param 更改过后的状态
	 * @return
	 */
	@RequestMapping(value = "/selectOrderByParts", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectOrderByParts() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String custName = request.getParameter("custName");
		// 建立order对象
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCustName(custName);
		// 调用修改工单状态的方法
		List<OrderInfo> result = serviceManageServiceImpl.selectOrderByParts(orderInfo);
		json.put("code", 0);
		json.put("data", result);
		json.put("msg", "修改成功");
		return json;
	}

	/**
	 * 接口：查看超时工单
	 * 
	 * @param 工单号
	 * @param 更改过后的状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectOrderByOverTime", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> selectOrderByOverTime() throws Exception {
		// 从前端接收数据
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] faultType = request.getParameterValues("faultType");
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
		// 先查询出所有已完成的服务工单
		List<ServiceInfo> serviceInfos = serviceInfoService.selectServiceInfoByengineerKpi("", "", "", startDate,
				endDate, faultType,null,null,null);
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
		// 循环给orderInfo和staffInfo赋值
		for (ServiceInfo service : serviceInfos) {
			service.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(service.getWoNumber()));
			service.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(service.getStaffId()));
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getProbSolve() == null || "".equals(serviceInfo.getProbSolve())) {
				continue;
			}
			boolean a = false;// 响应是否超时
			boolean b = false;// 到达现场是否超时
			boolean c = false;// 解决问题是否超时
			// 拿到客户的上下班时间
			CustInfo custInfo = custInfoService
					.selectCustByBaseInfo(serviceInfo.getOrderInfo().getCustName(),null, null, null,null).get(0);
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
			engineerKpi.setResponseTime(SOMUtils.getInt(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getTelRepon(), workTime, offWorkTime)));
			// 响应时间是否达标
			if (engineerKpi.getResponseTime() <= ParameterSetting.telRepon) {
				a = true;
			}
			engineerKpi.setArrTime(serviceInfo.getArrTime());
			// 到达现场用时
			engineerKpi.setArrTimeSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getArrTime(), workTime, offWorkTime));
			if (engineerKpi.getArrTimeSlot() <= ParameterSetting.arrTime) {
				b = true;
			}
			engineerKpi.setProbSolve(serviceInfo.getProbSolve());
			// 问题解决用时
			engineerKpi.setProbSolveSlot(CalendarTool.getDownTime(serviceInfo.getOrderInfo().getGetOrderTime(),
					serviceInfo.getProbSolve(), workTime, offWorkTime));
			if (engineerKpi.getProbSolveSlot() <= ParameterSetting.probSolve) {
				c = true;
			}

			if (a && b && c) {
				orderInfos.add(serviceInfo.getOrderInfo());
			}
		}
		json.put("code", 0);
		json.put("data", orderInfos);
		json.put("msg", "修改成功");
		return json;
	}
	
}
