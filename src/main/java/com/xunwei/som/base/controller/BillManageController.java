package com.xunwei.som.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 账单管理
 */
@Controller
public class BillManageController {

	/**
	 * 匹配账单管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/billManagement")
	public ModelAndView billManage(ModelAndView modelAndView){
		modelAndView.setViewName("/billManage/html/billManage");
		return modelAndView;
	}
}
