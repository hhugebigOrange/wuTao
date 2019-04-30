package com.xunwei.som.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 耗材管理
 */
@Controller
public class MaterialManageController {

	/**
	 * 匹配耗材管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/materialManage")
	public ModelAndView materialManage(ModelAndView modelAndView){
		modelAndView.setViewName("/materialManage/html/materialManage");
		return modelAndView;
	}
	
}
