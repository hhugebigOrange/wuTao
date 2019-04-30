package com.xunwei.som.pojo.permissions;

public class ParameterSetting {
	
    private String parameterName;

    private String parameter;
    
    public static double sendTime=30;
    //到达现场用时，分钟
    public static double arrTime=240;
    //问题解决时间，分钟
    public static double probSolve=600;
    //电话响应时间，分钟
    public static double telRepon=30;
    
    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName == null ? null : parameterName.trim();
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }
}