package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.DevMain;

public interface DevMainMapper {
	 
    /**
   	 * 增加设备保养信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(DevMain record);
    
    /**
   	 * 根据输入的条件增加相应的设备信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(DevMain record);
    
    
    /**
     * 根据条件查询设备执行情况
     * @return
     */
    List<DevMain> selectDevMain(@Param(value="compName")String compName,@Param(value="custName")String custName,@Param(value="engineerName")String engineerName);
    
}