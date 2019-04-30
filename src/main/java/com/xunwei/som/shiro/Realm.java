package com.xunwei.som.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xunwei.som.user.model.OldUser;

/**
 * 
 */
public class Realm extends AuthorizingRealm {
	
	/**
	 * 权限认证
	 */

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		// 获取登录时输入的用户名
		String loginName = (String) principalCollection.fromRealm(getName()).iterator().next();
		return null;
	}

	/**
	 * 登录认证;
	 */
	@SuppressWarnings("resource")
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("mybatis/beans-svc.xml");
		// UsernamePasswordToken对象用来存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		OldUser user = null;
//		userService.getModelByLoginId((String) token.getPrincipal());
		// 查出是否有此用户
//		if (user == null) {
//			throw new UnknownAccountException();// 没有找到账号
//		} else {
//			// 若存在，将此用户存放到登录认证info中
			SecurityUtils.getSubject().getSession().setAttribute("user", user);
			try {
				return new SimpleAuthenticationInfo(user.getName()==null?user.getUname():user.getName(), user.getPassword(), getName());
			} catch (Throwable t) {
				t.printStackTrace();
				throw new AuthenticationException();
			}
//		}
	}

}
