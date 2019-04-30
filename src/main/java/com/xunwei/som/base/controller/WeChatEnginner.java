package com.xunwei.som.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xunwei.som.pojo.OpenId;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.pojo.front.OrderManage;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.service.ServiceInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustomerManageServiceImpl;
import com.xunwei.som.service.impl.ServiceInfoServiceImpl;
import com.xunwei.som.service.impl.ServiceManageServiceImpl;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.SOMUtils;

/**
 * 微信：工程师接口
 * 
 * @author Administrator
 *
 */
@Controller
public class WeChatEnginner extends BaseController {

	private ServiceManageServiceImpl serviceManageServiceImpl = new ServiceManageServiceImpl();

	private ServiceInfoService serviceInfoService = new ServiceInfoServiceImpl();

	private StaffInfoServiceImpl staffInfoServiceImplnew = new StaffInfoServiceImpl();

	private CustomerManageServiceImpl customerManage = new CustomerManageServiceImpl();

	private UserService userService = new UserServiceImpl();

	/**
	 * 工程师接口：查看工单
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/enginnerOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> EngSetMaintenancePerform() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String openId = request.getParameter("openId"); // openid
		String woStatus = request.getParameter("woStatus"); // 工单状态
		if (openId == null || openId.equals("")) {
			json.put("code", 1);
			json.put("msg", "openId不能为空");
			return json;
		}
		OpenId open = new OpenId();
		open.setOpenid(openId);
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
			json.put("msg", "对不起，该openId对应的用户不存在");
			return json;
		}
		StaffInfo staff = null;
		for (StaffInfo staffinfo : staffInfoServiceImplnew.selectAllStaff()) {
			if (staffinfo.getPhone().equals(user.getUserId())) {
				staff = staffinfo;
				break;
			}
		}
		ServiceInfo service = new ServiceInfo();
		service.setStaffId(staff.getStaffId());
		service.setState(woStatus);
		List<ServiceInfo> serviceInfo = serviceInfoService.selectOrderByWeChat(service);
		List<OrderManage> orderManages = new ArrayList<>();
		Integer a1 = null;
		for (ServiceInfo serviceInfo2 : serviceInfo) {
			serviceInfo2.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(serviceInfo2.getWoNumber()));
			if (serviceInfo2.getOrderInfo().getMachCode() != null) {
				serviceInfo2.setDevice(customerManage.selectDeviceById(serviceInfo2.getOrderInfo().getMachCode()));
				a1 = 1;
			} else {
				a1 = 2;
			}
			String woState = serviceInfo2.getOrderInfo().getWoProgress();
			if(serviceInfo2.getCustScore()!=null){
				switch (serviceInfo2.getCustScore()) {
				case 3:
					woState = "commonly";
					break;
				case 2:
					woState = "dissatisfied";
					break;
				case 4:
					woState = "satisfied";
					break;
				case 5:
					woState = "verySatisfied";
					break;
				case 1:
					woState = "SuperDiscontent";
					break;
				default:
					break;
				}
			}
			serviceInfo2.setStaffInfo(staffInfoServiceImplnew.selectStaffByNum(staff.getStaffId()));
			if (a1 == 1) {
				OrderManage orderManage = new OrderManage();
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
				orderManage.setWoState(woState);
				orderManage.setTurnOrderReson(serviceInfo2.getOrderInfo().getTurnOrderReson());
				orderManage.setEnginnerName(serviceInfo2.getStaffInfo().getName());
				orderManage.setEnginnerPhone(serviceInfo2.getStaffInfo().getPhone());
				orderManage.setPartMessage(serviceInfo2.getOrderInfo().getPartsTypeNumber() == null ? null
						: serviceInfo2.getOrderInfo().getPartsTypeNumber().split(";"));
				orderManage.setRejected(serviceInfo2.getCustPrai()==null?null:serviceInfo2.getCustPrai());
				orderManage.setServiceType(serviceInfo2.getOrderInfo().getServiceType());
				orderManage.setOrderReason(serviceInfo2.getOrderInfo().getTreatmentState());
				orderManages.add(orderManage);
			} else {
				OrderManage orderManage = new OrderManage();
				orderManage.setCustName(serviceInfo2.getOrderInfo().getCustName());
				orderManage.setWoNumber(serviceInfo2.getWoNumber());
				orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo2.getWoNumber().substring(0, 2)));
				orderManage.setRepairMan(serviceInfo2.getOrderInfo().getRepairMan());
				orderManage.setRepairPhone(serviceInfo2.getOrderInfo().getRepairService());
				orderManage.setFaultType(serviceInfo2.getOrderInfo().getFaultType());
				orderManage.setAccidentType(serviceInfo2.getOrderInfo().getAccidentType());
				orderManage.setDescribe(serviceInfo2.getOrderInfo().getRemark());
				orderManage.setRepairTime(serviceInfo2.getOrderInfo().getRepairTime());
				orderManage.setWoState(woState);
				orderManage.setTurnOrderReson(serviceInfo2.getOrderInfo().getTurnOrderReson());
				orderManage.setEnginnerName(serviceInfo2.getStaffInfo().getName());
				orderManage.setEnginnerPhone(serviceInfo2.getStaffInfo().getPhone());
				orderManage.setPartMessage(serviceInfo2.getOrderInfo().getPartsTypeNumber() == null ? null
						: serviceInfo2.getOrderInfo().getPartsTypeNumber().split(";"));
				orderManage.setRejected(serviceInfo2.getCustPrai()==null?null:serviceInfo2.getCustPrai());
				orderManage.setServiceType(serviceInfo2.getOrderInfo().getServiceType());
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
	 * 工程师接口：查看工单详情
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/orderDetails", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> orderDetails() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接受参数
		Map<String, Object> json = new HashMap<>();
		String woNumber = request.getParameter("woNumber"); // 工单状态
		if (woNumber == null || woNumber.equals("")) {
			json.put("code", 1);
			json.put("msg", "工单号不能为空");
			return json;
		}
		List<ServiceInfo> serviceInfo = serviceInfoService.selectServiceInfByDynamic2(null, woNumber, null);
		List<OrderManage> orderManages = new ArrayList<>();
		Integer a1 = null;
		for (ServiceInfo serviceInfo2 : serviceInfo) {
			serviceInfo2.setOrderInfo(serviceManageServiceImpl.selectOrderByOrderNum(serviceInfo2.getWoNumber()));
			if (serviceInfo2.getOrderInfo().getMachCode() != null) {
				serviceInfo2.setDevice(customerManage.selectDeviceById(serviceInfo2.getOrderInfo().getMachCode()));
				a1 = 1;
			} else {
				a1 = 2;
			}
			if (a1 == 1) {
				OrderManage orderManage = new OrderManage();
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
				orderManage.setOrderReason(serviceInfo2.getOrderInfo().getTreatmentState());
				orderManages.add(orderManage);
			} else {
				OrderManage orderManage = new OrderManage();
				orderManage.setCustName(serviceInfo2.getOrderInfo().getCustName());
				orderManage.setWoNumber(serviceInfo2.getWoNumber());
				orderManage.setServiceArea(SOMUtils.CompToOrderNumTo(serviceInfo2.getWoNumber().substring(0, 2)));
				orderManage.setRepairMan(serviceInfo2.getOrderInfo().getRepairMan());
				orderManage.setRepairPhone(serviceInfo2.getOrderInfo().getRepairService());
				orderManage.setFaultType(serviceInfo2.getOrderInfo().getFaultType());
				orderManage.setAccidentType(serviceInfo2.getOrderInfo().getAccidentType());
				orderManage.setDescribe(serviceInfo2.getOrderInfo().getRemark());
				orderManage.setRepairTime(serviceInfo2.getOrderInfo().getRepairTime());
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
}
