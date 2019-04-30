package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.xunwei.som.pojo.Device;

public interface DeviceMapper {
	
    int deleteByPrimaryKey(String machCode);

    int insert(Device record);

    int insertSelective(Device record) throws DataAccessException;

    Device selectByPrimaryKey(String machCode);

    int updateByPrimaryKeySelective(Device record) throws DataAccessException;

    int updateByPrimaryKey(Device record);
    
    /**
     * 根据条件查询设备
     * @param machineName
     * @return
     */
    List<Device> selectByDynamic(@Param(value="serviceArea")String serviceArea,
    		@Param(value="custArea")String custArea,
    		@Param(value="assetAttr")String assetAttr,
    		@Param(value="machCode")String machCode,
    		@Param(value="page")Integer page,
    		@Param(value="limit")Integer limit,
    		@Param(value="identifier")String identifier);
    
    /**
     * 根据设备报表的条件查询
     * @param machineName
     * @return
     */
    List<Device> selectByKpi(@Param(value="serviceArea")String serviceArea,
    		@Param(value="custArea")String custArea,
    		@Param(value="contartctNo")String contartctNo,
    		@Param(value="page")Integer page,
    		@Param(value="limit")Integer limit,
    		@Param(value="identifier")String identifier);
    
    /**
     * 根据条件查询设备2.0
     * @param machineName
     * @return
     */
    List<Device> selectByDevice(@Param(value="custName")String custName,@Param(value="serviceArea")String serviceArea,@Param(value="unitType")String unitType,@Param(value="assetAttr")String assetAttr,
    		@Param(value="page")Integer page,
    		@Param(value="limit")Integer limit);
    
    /**
     * 根据设备Id查询设备信息
     * @param id
     * @return
     */
    Device selectDeviceById(String id);
    
    /**
     * 根据设备序号查询设备信息
     * @param id
     * @return
     */
    Device selectDeviceByID(Integer id);
    
    /**
     * 根据设备资产属性查询设备信息
     * @param id
     * @return
     */
    List<Device> selectDeviceByAsSetNumber(String asSetNumber);
    
    /**
     * 根据客户名称查找所有该客户名下的设备
     */
    List<Device> selectDeviceNumByCustName(@Param(value="custName")String custName);
    /**
     * 根据客户名称查找所该客户名下的保养设备
     */
    List<Device> selectMaintNumByCustName(@Param(value="custName")String custName);
    
    /**
     * 清空设备的服务属性，只保留彩色读数和黑白读数
     * @param machCode
     * @return
     */
    int cleanDeviceAttribute(@Param(value="machCode")String machCode);
    
    
    /**
     * 修改设备的服务区域
     * @param oldServiceArea
     * @param newServiceArea
     * @return
     */
    int updateDeviceServiceArea(@Param(value="oldServiceArea")String oldServiceArea,@Param(value="newServiceArea")String newServiceArea);
    
    
}