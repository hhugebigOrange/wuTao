package com.xunwei.som.base.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunwei.som.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.CustomerFeedback;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.OrderManage;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.util.SOMUtils;


/**
 * 微信：客户
 * 
 * @author Administrator
 *
 */

@Controller
public class WeChatCustomer extends BaseController {

	@Autowired
	private ServiceManageService serviceManageService;

	@Autowired
	private ServiceInfoService serviceInfoService;

	@Autowired
	private CustomerManageService customerManageService;

	@Autowired
	private CustomerFeedbackService customerFeedbackService;

	@Autowired
	private StaffInfoService staffInfoService;

	@Autowired
	private UserService userService;

	/**
	 * 客户查找工单
	 * 
	 * @param 工单号
	 */
	@RequestMapping(value = "/customerWeChatOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerWeChatOrder() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String openId = request.getParameter("openId"); // openid
		String woStatus = request.getParameter("woStatus"); // 工单状态
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		// 根据openId查找相应用户
		User user = null;
		for (User user2 : userService.selectAllUser()) {
			if(user2.getOpenId()==null){
				continue;
			}
			if (user2.getOpenId().equals(openId)) {
				user = user2;
				break;
			}
		}
		if (user == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该用户不存在");
			return json;
		}
		OrderInfo order = new OrderInfo();
		order.setOrderAccount(user.getUserId());
		order.setWoStatus(woStatus);
		List<ServiceInfo> serviceInfo = serviceInfoService.selectOrderByWeChatCustomer(order);
		List<OrderManage> orderManages = new ArrayList<>();
		Integer a1 = null;
		for (ServiceInfo serviceInfo2 : serviceInfo) {
			List<String> process = new ArrayList<>();
			serviceInfo2.setOrderInfo(serviceManageService.selectOrderByOrderNum(serviceInfo2.getWoNumber()));
			if (serviceInfo2.getOrderInfo().getMachCode() != null) {
				serviceInfo2.setDevice(customerManageService.selectDeviceById(serviceInfo2.getOrderInfo().getMachCode()));
				a1 = 1;
			} else {
				a1 = 2;
			}
			process.add("待受理");
			if (serviceInfo2.getOrderInfo().getSendTime() != null) {
				process.add("已受理");
			}
			if (serviceInfo2.getOrderInfo().getTurnOrderTime() != null) {
				process.add("已转单");
			}
			if (serviceInfo2.getState() != null) {
				if (serviceInfo2.getState().contains("2")) {
					process.add("已电话响应");
				}
				if (serviceInfo2.getState().contains("4,true")) {
					process.add("已到达现场");
					process.add("处理中");
				}
				if (serviceInfo2.getState().contains("6,true")) {
					process.add("待订件");
				}
				if (serviceInfo2.getState().contains("7")) {
					process.add("已完成");
				}
			}
			OrderManage orderManage = new OrderManage();
			orderManage.setProcess(process);
			switch (process.get(process.size() - 1)) {
			case "已转单":
				orderManage.setCurrentTime(serviceInfo2.getOrderInfo().getTurnOrderTime());
				break;
			case "待受理":
				orderManage.setCurrentTime(serviceInfo2.getOrderInfo().getRepairTime());
				break;
			case "已受理":
				orderManage.setCurrentTime(serviceInfo2.getOrderInfo().getAcceptTime());
				break;
			case "已电话响应":
				orderManage.setCurrentTime(serviceInfo2.getTelRepon());
				break;
			case "处理中":
				orderManage.setCurrentTime(serviceInfo2.getArrTime());
				break;
			case "待订件":
				orderManage.setCurrentTime(serviceInfo2.getArrTime());
				break;
			case "已完成":
				orderManage.setCurrentTime(serviceInfo2.getProbSolve());
				break;
			default:
				break;
			}
			StaffInfo staff=staffInfoService.selectStaffByNum(serviceInfo2.getStaffId()==null?null:serviceInfo2.getStaffId());
			if (a1 == 1) {
				orderManage.setCustName(serviceInfo2.getOrderInfo().getCustName());
				orderManage.setWoNumber(serviceInfo2.getWoNumber());
				orderManage.setMachCode(serviceInfo2.getOrderInfo().getMachCode());
				orderManage.setDevName(serviceInfo2.getDevice().getDevName());
				orderManage.setUnitType(serviceInfo2.getDevice().getUnitType());
				orderManage.setDepartment(serviceInfo2.getDevice().getDepartment());
				orderManage.setRepairMan(serviceInfo2.getOrderInfo().getRepairMan());
				orderManage.setRepairPhone(serviceInfo2.getOrderInfo().getRepairService());
				orderManage.setLocation(serviceInfo2.getDevice().getLocation());
				orderManage.setFaultType(serviceInfo2.getOrderInfo().getFaultType());
				orderManage.setAccidentType(serviceInfo2.getOrderInfo().getAccidentType());
				orderManage.setFaultNo(serviceInfo2.getOrderInfo().getFalutNo());
				orderManage.setDescribe(serviceInfo2.getOrderInfo().getRemark());
				orderManage.setRepairTime(serviceInfo2.getOrderInfo().getRepairTime());
				orderManage.setWoState(process.get(process.size()-1));
				orderManage.setTurnOrderReson(serviceInfo2.getOrderInfo().getTurnOrderReson());
				orderManage.setPartMessage(serviceInfo2.getOrderInfo().getPartsTypeNumber() == null ? null
						: serviceInfo2.getOrderInfo().getPartsTypeNumber().split(";"));
				orderManage.setEnginnerName(staff==null?null:staff.getName());
				orderManage.setEnginnerPhone(staff==null?null:staff.getPhone());
				orderManage.setOrderReason(serviceInfo2.getOrderInfo().getTreatmentState());
				orderManages.add(orderManage);
			} else {
				orderManage.setCustName(serviceInfo2.getOrderInfo().getCustName());
				orderManage.setWoNumber(serviceInfo2.getWoNumber());
				orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo2.getWoNumber().substring(0, 2)));
				orderManage.setRepairMan(serviceInfo2.getOrderInfo().getRepairMan());
				orderManage.setRepairPhone(serviceInfo2.getOrderInfo().getRepairService());
				orderManage.setFaultType(serviceInfo2.getOrderInfo().getFaultType());
				orderManage.setAccidentType(serviceInfo2.getOrderInfo().getAccidentType());
				orderManage.setDescribe(serviceInfo2.getOrderInfo().getRemark());
				orderManage.setRepairTime(serviceInfo2.getOrderInfo().getRepairTime());
				orderManage.setWoState(process.get(process.size()-1));
				orderManage.setTurnOrderReson(serviceInfo2.getOrderInfo().getTurnOrderReson());
				orderManage.setPartMessage(serviceInfo2.getOrderInfo().getPartsTypeNumber() == null ? null
						: serviceInfo2.getOrderInfo().getPartsTypeNumber().split(";"));
				orderManage.setEnginnerName(staff==null?null:staff.getName());
				orderManage.setEnginnerPhone(staff==null?null:staff.getPhone());
				orderManage.setOrderReason(serviceInfo2.getOrderInfo().getTreatmentState());
				orderManages.add(orderManage);
			}
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageAndLimit(request, orderManages, page, limit);
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
		json.put("count", orderManages.size());
		json.put("data", page == null || orderManages == null ? orderManages : orderManages.subList(page, limit));
		return json;
	}

	/**
	 * 客户手机端:新增反馈与建议
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/customerDoAdvice", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> customerDoAdvice(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String openId = request.getParameter("openId"); // openid
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		// 根据openId查找相应用户
		User user = null;
		for (User user2 : userService.selectAllUser()) {
			if (user2.getOpenId().equals(openId)) {
				user = user2;
				break;
			}
		}
		if (user == null) {
			json.put("code", 1);
			json.put("msg", "对不起，该用户不存在");
			return json;
		}
		String custPhone = user.getUserId();
		String content = request.getParameter("content");
		Date now = new Date();
		CustomerFeedback customerFeedback = new CustomerFeedback(user.getCustName(), user.getUserName(), custPhone, now,
				content);
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
	
}
