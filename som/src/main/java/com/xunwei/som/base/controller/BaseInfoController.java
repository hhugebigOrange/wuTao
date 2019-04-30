package com.xunwei.som.base.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.base.model.staffInfo;
import com.xunwei.som.service.impl.StaffInfoServiceImpl;

@Controller
public class BaseInfoController extends BaseController {

	
	
	private StaffInfoServiceImpl staffInfoServiceImplnew =new StaffInfoServiceImpl();
	
	/**
	 * 匹配客户信息页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping({ "/customerInfo" })
	public ModelAndView customerInfo(ModelAndView modelAndView) {
		modelAndView.setViewName("/baseinfo/html/customerInfo");
		return modelAndView;
	}

	/**
	 * 匹配用户管理页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping({ "/userManageInfo" })
	public ModelAndView userManageInfo(ModelAndView modelAndView) {
		modelAndView.setViewName("/baseinfo/html/userManageInfo");
		return modelAndView;
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
	 * 增加用户方法，成功后返回用户信息页面
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping({ "/doAddManager" })
	public ModelAndView doAddManager(ModelAndView modelAndView, HttpServletRequest request) throws ParseException {
		// 接受前端页面传递过来的信息
		String staffName = request.getParameter("staffName");
		String comperName = request.getParameter("comperName");
		String post = roleToPost(request.getParameter("role"));
		String phoneName = request.getParameter("phoneName");
		String createdate = request.getParameter("createData");
		System.out.println(phoneName);
		// 将createData转换为Data类型
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date createDate = format.parse(createdate);
		staffInfo staff = new staffInfo(staffName, phoneName, comperName, post, createDate);
	    System.out.println(staff);
		boolean result=staffInfoServiceImplnew.insertStaff(staff);
		System.out.println(result?"添加成功":"添加失败");
		modelAndView.setViewName("/baseinfo/html/userManageInfo");
		return modelAndView;
	}

	
	/**
	 * 前台代码转换为对应的岗位
	 * @param role
	 * @return
	 */
	public String roleToPost(String role){
		String post=null;
	  switch(role){
	  case "1":
		  post="运维经理";
		  break;
	  case "2":
		  post="客服主管";
		  break;
	  case "3":
		  post="客服调度员";
		  break;
	  case "4":
		  post="技术主管";
		  break;
	  case "5":
		  post="工程师";
		  break;
	  case "6":
		  post="驻现场人员";
		  break;
	  case "7":
		  post="客户助理";
		  break;
	  }
	  return post;
  }

}
