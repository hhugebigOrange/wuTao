package com.xunwei.som.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 */
public class LoggerUtils {
    public static final String SESSION_USER = "user";
    public static Logger OMS_Logger = Logger.getLogger("webMonitor");

    public static String outputError(String error){
        return "[失败]["+error+"]";
    }

    public static void printLogger(HttpServletRequest httpServletRequest,StringBuilder stringBuilder){
        stringBuilder.append("[").append(httpServletRequest.getProtocol()).append("]");
        stringBuilder.append("[").append(httpServletRequest.getMethod()).append("]");
        stringBuilder.append("[").append(httpServletRequest.getRequestURI()).append("]");
        stringBuilder.append("[").append(httpServletRequest.getParameterMap().size()).append("]");
        stringBuilder.append("[").append(httpServletRequest.getRemoteAddr()).append("]");
        stringBuilder.append("[").append(httpServletRequest.getRemotePort()).append("]");
        long reqTimestamp = (Long)httpServletRequest.getAttribute("timestamp");
        httpServletRequest.removeAttribute("timestamp");
        long time =  System.currentTimeMillis() - reqTimestamp;
        stringBuilder.append("[").append(time).append("ms]");
        OMS_Logger.debug(stringBuilder);
    }
}
