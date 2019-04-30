package com.xunwei.som.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 *
 */
public class MySessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        //会话创建时触发
//        OMS_Logger.info("会话创建：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        //退出/会话过期时触发
//        User user = (User)session.getAttribute(SESSION_USER);
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        OMS_Logger.info("用户名为：["+user.getId()+"]，在"+sdf.format(new Date())+"登出注销！");
    }

    @Override
    public void onExpiration(Session session) {
        //会话过期时触发
//        User user = (User) session.getAttribute(SESSION_USER);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        OMS_Logger.info("用户名为：["+user.getId()+"]，在" + sdf.format(new Date()) + "会话过期！");
    }
}
