package com.xunwei.som.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.DeviceChange;

public interface CustomerManageService {

	/**
	 * 插入合同方法
	 * @param contrace
	 * @return
	 */
	public int insert(Contract contract);
	
	 /**
     * 根据客户名称修改合同信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Contract record,String custName);
	
	
	/**
	 * 根据公司名字查找名下的合同
	 * @param compName
	 * @return
	 */
	public List<Contract> selectByComp(String compName);
	
	/**
	 * 根据合同编码查询合同
	 */
	public Contract selectByPrimaryKey(String contractNo);
	
	/**
	 * 根据合同序号查询合同
	 */
	public Contract selectByID(Integer ID);
	
    /**
     * 根据客户名称查找相应合同
     * 
     */
	public List<Contract> selectByCust(String custName,String compName,String timeout,String dueTo,
			Integer page,
			Integer limit,String contractNature,String order,String identifier);
	
	
    /**
     * 根据报表条件查询合同
     */
    List<Contract> selectByKpi(String contractNo,String custName,String serviceArea,Integer startDate,Integer endDate,String identifier);
	
	/**
	 * 根据条件统计客户总数
	 */
	public int countCustNumber(String custName);
	
	/**
	 * 插入设备
	 * @param device
	 * @return
	 */
	public int insert(Device device);
	
    /**
     * 根据条件查询设备
     * @param machineName
     * @return
     */
    List<Device> selectByDynamic(String serviceArea,String custArea,String assetAttr,String machCode,Integer page,Integer limit,String identifier);
    
    
    /**
     * 根据条件查询设备2.0
     * @param machineName
     * @return
     */
    List<Device> selectByDevice(String custName,String serviceArea,String unitType,String assetAttr,Integer page,Integer limit);
    
    
    /**
     * 根据设备报表的条件查询
     * @param machineName
     * @return
     */
    List<Device> selectByDeviceKpi(String serviceArea,String custArea,String contartctNo,Integer page,Integer limit,String identifier);
   
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
    Device selectDeviceById(Integer id);
    
    /**
     * 根据设备资产属性查询设备信息
     * @param id
     * @return
     */
    List<Device> selectDeviceByAsSetNumber(String asSetNumber);
  
    /**
     * 根据客户名称查找所有该客户名下的设备
     */
    List<Device> selectDeviceNumByCustName(String custName);
    /**
     * 根据客户名称查找所该客户名下的保养设备
     */
    List<Device> selectMaintNumByCustName(String custName);
    
    /**
     * 修改设备
     * @param device
     * @return
     */
    int updateByPrimaryKeySelective(Device device);
    
    /**
     * 根据工单号删除工单
     * @param record
     * @return
     */
    boolean deleteOrder(String woNumber);
    
    
    /**
     * 增加设备变动记录
     * @param record
     * @return
     */
    int insertSelective(DeviceChange record);
    
    /**
     * 清空设备的服务属性，只保留彩色读数和黑白读数
     * @param machCode
     * @return
     */
    int cleanDeviceAttribute(@Param(value="machCode")String machCode);
    
    Device selectByCode(String machCode);
    
    int updateDeviceServiceArea(@Param(value="oldServiceArea")String oldServiceArea,@Param(value="newServiceArea")String newServiceArea);
}
