package com.xunwei.som.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 验证是否登陆拦截器
 * 
 * @version 2018年8月22日下午5:57:17
 * @author zhuwenbin
 */
public class LoginInterceptor implements HandlerInterceptor {

	// 可以在拦截器中注入service、dao
	// @Autowired
	// private UserService userService;

	/**
	 * preHandle表示在执行目标方法前执行
	 * 
	 * 返回值的意义是：true-放行，继续执行目标方法或者其他拦截器；false-停止执行，无任何响应
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println(">>>>>正在执行preHandle方法");
		// 获取session对象
		HttpSession session = request.getSession();
		// 取出用户信息
		Object object = session.getAttribute("user");
		// 判读用户信息是否为null，如果为null证明没有登陆，跳转到登陆页面
		if (object == null) {
			System.out.println(">>>>>用户未登陆，跳转到登陆页面");
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}

		return true;
	}

	/**
	 * postHandle表示在执行目标方法后但未响应时执行
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println(">>>>>正在执行postHandle方法");
	}

	/**
	 * afterCompletion表示在执行目标方法并响应后执行
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println(">>>>>正在执行afterCompletion方法");
	}

}
