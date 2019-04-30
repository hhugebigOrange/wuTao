package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.MaintenancePerform;

public interface MaintenancePerformMapper {
	
    int insert(MaintenancePerform record);

    int insertSelective(MaintenancePerform record);
    
    /**
     * 根据传入的参数查找相应的保养执行情况
     * @param record  
     * @param startDate  开始时间
     * @param endDate   完成时间
     * @return
     */
    List<MaintenancePerform> selectByDynamic(@Param("record")MaintenancePerform record,@Param("startDate")String startDate,@Param("endDate")String endDate);
    
    
    
}