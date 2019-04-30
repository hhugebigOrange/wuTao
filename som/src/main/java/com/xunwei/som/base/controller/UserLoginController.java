package com.xunwei.som.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.base.controller.BaseController;
import com.xunwei.som.util.SOMUtils;

@Controller
public class UserLoginController extends BaseController{

	/**
	 * 进入登陆界面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView goToLoginPage(ModelAndView modelAndView){
		modelAndView.setViewName("/user/html/login");
		return modelAndView;
	}
	
	/**
	 * 验证登陆
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/dologin", method = { RequestMethod.POST })
	public Object Login(ModelAndView modelAndView) {
		String viewName = "redirect:/login";
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//如果账号或密码为空，则返回登陆页面
		if (SOMUtils.isNull(username) || SOMUtils.isNull(password)) {
			modelAndView.addObject("msg", "用户名或密码不能为空");
			modelAndView.setViewName(viewName);
			return modelAndView;
		}
		//比对账号和密码,成功跳到首页
	    if(username.equals("admin") && password.equals("123456")){
	    	System.out.println(username + password);
	    	viewName="redirect:/homePage";
	    	session.setAttribute("user", username);
	    	modelAndView.setViewName(viewName);
	    	return modelAndView;
	    }
	    modelAndView.addObject("msg", "用户名或密码错误");
	    modelAndView.setViewName(viewName);
	    return modelAndView;
	}
	
	/**
	 * 匹配首页
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/homePage")
	public ModelAndView homePage(ModelAndView modelAndView){
		modelAndView.setViewName("/user/html/main");
		return modelAndView;
	}
}
