package com.xunwei.som.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;

public interface ServiceInfoService {

	/**
	 * 根据工单号查询相应工单
	 * 
	 * @return
	 */
	List<OrderInfo> selectOrderByNum();

	/**
	 * 根据员工号查询相应员工
	 * 
	 * @return
	 */
	List<StaffInfo> selectStaffByNum();

	List<ServiceInfo> selectAllServiceInfo();

	/**
	 * 查询所有的工单KPI
	 * 
	 * @return
	 */
	List<OrderInfo> selectOrderInfoByKpi();

	/**
	 * 查询坐席的工单KPI
	 * 
	 * @return
	 */
	List<OrderInfo> selectOrderInfoBySeat();

	/**
	 * 根据相应条件查询客服满意度工单
	 * 
	 * @param serviceArea
	 * @param custName
	 * @param startDate
	 * @param endDate
	 * @param workState
	 * @return
	 */
	List<ServiceInfo> selectServiceInfByDynamic(String serviceArea, String custName, String startDate, String endDate,
			String workState, String custScore, String machCode, String custScore3, String custScore5,
			String repairType, String[] faultType, Integer page, 
			Integer limit,
			String ProcessingState,
			String enginnerName,
			String identifier);

	/**
	 * 根据工单号查找相应的服务评价
	 */
	ServiceInfo selectServiceInfByDyWoNumber(String woNumber);

	/**
	 * 根据工单号修改相应的服务评价
	 */
	int update(String woNumber, String custSat, String custEva, Integer custScore);

	/**
	 * 根据相应条件查询客服满意度工单2.0
	 * 
	 * @param serviceArea
	 * @param custName
	 * @param startDate
	 * @param endDate
	 * @param workState
	 * @return
	 */
	List<ServiceInfo> selectServiceInfByDynamic2(String compName, String woNumber, String sendTime);

	/**
	 * 根据工程师KPI条件查询
	 */
	List<ServiceInfo> selectServiceInfoByengineerKpi(String staffId, String custName, String serviceArea,
			String startDate, String endDate, String[] faultType,Integer page,Integer limit,String identifier);

	/**
	 * 根据坐席客服KPI条件查询
	 */
	List<ServiceInfo> selectServiceInfoBySeatServiceKpi(String staffId, String startDate, String endDate,
			String[] faultType);

	/**
	 * 根据工单分析条件查询
	 * 
	 * @return
	 */
	List<ServiceInfo> selectServiceInfoByOrder(String custName, String woNumber, String faultType, String startDate,
			String endDate);

	/**
	 * 根据工单号修改相应的工单状态和对应的状态时间
	 */
	int updateServiceInfo(@Param("woNumber") String woNumber, @Param("woStatus") String woStatus);

	/**
	 * 根据传入的服务评价对象修改相应的数据
	 * 
	 * @param serviceInfo
	 * @return
	 */
	boolean upDateServiceInfo(ServiceInfo serviceInfo);
	
	
	/**
	 * 根据工单号删除工单
	 * @param woNumber
	 * @return
	 */
	boolean deleteServiceInfo(String woNumber);

	/**
	 * 工程师微信查询工单
	 * 
	 * @param woStatus
	 * @return
	 */
	List<ServiceInfo> selectOrderByWeChat(ServiceInfo record);

	/**
	 * 客户微信查询工单
	 * 
	 * @param record
	 * @return
	 */
	List<ServiceInfo> selectOrderByWeChatCustomer(OrderInfo record);
	
	/**
	 * 技术主管查询工单
	 * @param record
	 * @return
	 */
	List<ServiceInfo> selectOrderByWeChatTechnical(ServiceInfo record);
	
	/**
	 * 根据状态返回数据
	 * @param serviceArea
	 * @param custName
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param limit
	 * @param ProcessingState
	 * @param identifier
	 * @return
	 */
	List<ServiceInfo> selectServiceInfByWoStatus(String serviceArea,String custName,String startDate,String endDate,Integer page,Integer limit,String ProcessingState,
    		String identifier);
}
