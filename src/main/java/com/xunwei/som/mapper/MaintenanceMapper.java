package com.xunwei.som.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.Maintenance;
public interface MaintenanceMapper {
	
	/**
	 *新增保养计划 
	 */
	
	boolean insert(Maintenance record);
	/**
	 * 查看保养计划
	 * @return
	 */
	List<Maintenance> select();
	
	/**
	 * 根据员工编码和周期，查看相应的保养计划
	 * @param Cycle 设备的保养周期
	 * @param responsibleId 工程师的id
	 * @param compName 分公司名
	 * @param custName 客户名字
	 * @return
	 */
	List<Maintenance> selectByCycle(@Param("Cycle")String Cycle,@Param("responsibleId")String responsibleId,
			@Param("compName")String compName,@Param("custName")String custName);
	
	
	/**
	 * 根据合同号和机器编码，查看指定的某个保养计划
	 * @param contractNo 合同编码
	 * @param machCode   机器编码
	 * @return
	 */
	Maintenance selectOneMaintenance(@Param("contractNo")String contractNo,@Param("machCode")String machCode);
	
	
	/**
	 * 修改相应设备的保养状态
	 * @param contractNo 合同编码
	 * @param machCode  机器编码
	 * @param machCode  0-未保养  1-已保养  2-需跟进
	 * @return
	 */
	int updateMaintenance(@Param("contractNo")String contractNo,
			@Param("machCode")String machCode,@Param("state")Integer state,@Param("lastTime")String lastTime);
	
	/**
	 * 根据服务区域来查询保养计划
	 * @param custName
	 * @param machCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Maintenance> selectmaintenance(@Param("custName")String custName,
			@Param("compName")String compName,@Param("staffName")String staffName,
			@Param("machCode")String machCode,@Param("startDate")String startDate,
			@Param("endDate")String endDate,
			@Param("page")Integer page,
			@Param("limit")Integer limit,
			@Param("identifier")String identifier);
	
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
	int upDateMaintenance(Maintenance record);
	
	int deleteMaintenance(String machCode);
	
}