package com.xunwei.som.mapper;

import com.xunwei.som.pojo.DeviceChange;

public interface DeviceChangeMapper {
	
    int insert(DeviceChange record);

    int insertSelective(DeviceChange record);
}