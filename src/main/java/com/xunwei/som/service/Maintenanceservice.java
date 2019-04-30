package com.xunwei.som.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.MaintenancePerform;
import com.xunwei.som.pojo.Maintenance;

public interface Maintenanceservice {
	/**
	 * 
	 * 新增保养计划
	 */
	boolean insertOrder(Maintenance tenance);

	/**
	 * 查看所有保养计划
	 * 
	 * @return
	 * 
	 */
	List<Maintenance> select();

	/**
	 * 根据服务区域查看相应的保养计划
	 */
	List<Maintenance> selectmaintenance(String custName, String compName, String staffName, String machCode,
			String startDate, String endDate,Integer page,Integer limit,String identifier);

	/**
	 * 根据员工编码和周期，查看相应的保养计划
	 * @param Cycle 设备的保养周期
	 * @param responsibleId 工程师的id
	 * @return
	 */
	List<Maintenance> selectByCycle(String Cycle, String responsibleId,String compName,String custName);

	/**
	 * 修改相应设备的保养状态
	 * @param contractNo   合同编码
	 * @param machCode  机器编码
	 * @param machCode 0-未保养 1-已保养
	 * @return
	 */
	int updateMaintenance(String contractNo, String machCode, Integer state,String lastTime);

	/**
	 * 根据合同号和机器编码，查看指定的某个保养计划
	 * 
	 * @param contractNo  合同编码
	 * @param machCode 机器编码
	 * @return
	 */
	Maintenance selectOneMaintenance(String contractNo, String machCode);
	
	/**
	 * 插入一条保养执行记录
	 * @param record
	 * @return
	 */
	
	int insertSelective(MaintenancePerform record);
	
	/**
     * 根据传入的参数查找相应的保养执行情况
     * @param record  
     * @param startDate  开始时间
     * @param endDate   完成时间
     * @return
     */
    List<MaintenancePerform> selectByDynamic(@Param("record")MaintenancePerform record,@Param("startDate")String startDate,@Param("endDate")String endDate);
	
    
    /**
	 * 根据员工编码查找相应的保养合同
	 * 
	 * @return
	 */
	List<Maintenance> selectByContract(@Param("responsibleId")String responsibleId);
	
	/**
	 * 更新保养计划以及执行
	 * @param record
	 * @return
	 */
	int updateMaintenance(Maintenance record);
	
	/**
	 * 删除保养计划
	 * @param machCode
	 * @return
	 */
	int deleteMaintenance(String machCode);
}
