package com.xunwei.som.base.controller;
import java.util.function.Consumer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.xunwei.som.base.model.Result;
import com.xunwei.som.base.model.ValueResult;
import com.xunwei.som.user.model.OldUser;
import com.xunwei.som.util.SOMUtils;

public class BaseController {
	protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected Cookie[] cookies;
    protected String contextPath;
    protected static final String SESSION_USER = "user";
    public static Logger OMS_Logger = Logger.getLogger("webMonitor");
    protected static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("mybatis/beans-svc.xml");

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		this.cookies = request.getCookies();
		this.contextPath = request.getContextPath();
	}
	
    /**
     * 获取当前的用户信息
     * @return
     */
	public OldUser getUser() {
		Subject currentUser = SecurityUtils.getSubject();
		OldUser user = null;
		if (currentUser.isAuthenticated()) {
			user = (OldUser) currentUser.getSession().getAttribute(SESSION_USER);
		}
		if (!SOMUtils.isNull(user)) {
			return user;
		} else {
			return null;
		}
	}
    
	protected void handleException(Throwable ex, Result er) {
		if (ex instanceof UnknownAccountException) {
			er.setCode(1);//
			er.setMessage("登录失败！不存在该用户或用户已被删除！");
		} else if (ex instanceof IncorrectCredentialsException) {
			er.setCode(2);//
			er.setMessage("登录失败！用户名或密码不正确！");
		} else {
			er.setCode(99);
			er.setMessage("系统异常！");
		}
	}

	 protected <T> ValueResult<T> exeFunc(Consumer<ValueResult<T>> normalAction) {
        return exeFunc(normalAction, null);
    }

    protected <T> ValueResult<T> exeFunc(Consumer<ValueResult<T>> normalAction, Consumer<Throwable> errorAction) {
        return exeFunc(normalAction, errorAction, null);
    }

	protected <T> ValueResult<T> exeFunc(Consumer<ValueResult<T>> normalAction, Consumer<Throwable> errorAction,
			Consumer<ValueResult<T>> finallyFunc) {
		ValueResult<T> er = new ValueResult<>();
		// 1.数据验证
		try {
			// 2.身份认证
			// 3.使用缓存
			normalAction.accept(er);
		} catch (Throwable ex) {
			handleException(ex, er);// 转换成code和message
			// 写详细日志
			if (errorAction != null)
				errorAction.accept(ex);
		} finally {
			if (finallyFunc != null)
				finallyFunc.accept(er);

		}
		return er;
		// 4.通用日志请用Aop
	}

    protected Result exeAction(Runnable normalAction) {
        return exeAction(normalAction, null);
    }

    protected Result exeAction(Runnable normalAction, Consumer<Throwable> errorAction) {
        return exeAction(normalAction, errorAction, null);
    }

	protected Result exeAction(Runnable normalAction, Consumer<Throwable> errorAction, Consumer<Result> finallyAction) {
		Result er = new Result();
		// 1.数据验证
		try {
			// 2.身份认证
			// 3.使用缓存
			normalAction.run();
		} catch (Throwable ex) {
			handleException(ex, er);// 转换成code和message
			// 写详细日志
			if (errorAction != null)
				errorAction.accept(ex);
		} finally {
			if (finallyAction != null)
				finallyAction.accept(er);
		}
		return er;
		// 4.通用日志请用Aop
	}
}
