package com.xunwei.som.mapper;

import com.xunwei.som.pojo.DeviceInfo;

public interface DeviceInfoMapper {
	/**
	 * 根据主键删除设备信息
	 * @param priNumber
	 * @return
	 */
    int deleteByPrimaryKey(String machCode);
    
    /**
   	 * 增加设备信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(DeviceInfo record);
    
    /**
   	 * 根据输入的条件增加相应的设备信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(DeviceInfo record);
    
    /**
     * 根据主键查找设备信息
     * @param priNumber
     * @return
     */
    DeviceInfo selectByPrimaryKey(String machCode);

    /**
     * 根据输入条件更新设备信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKeySelective(DeviceInfo record);
    
    /**
     * 更新设备信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKey(DeviceInfo record);
}