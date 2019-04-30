package com.xunwei.som.mapper;

import java.util.List;

import com.xunwei.som.pojo.permissions.ParameterSetting;

public interface ParameterSettingMapper {
	
    int deleteByPrimaryKey(String parameterName);

    int insert(ParameterSetting record);

    int insertSelective(ParameterSetting record);

    List<ParameterSetting> selectByPrimaryKey();

    int updateByPrimaryKeySelective(ParameterSetting record);

    int updateByPrimaryKey(ParameterSetting record);
    
}