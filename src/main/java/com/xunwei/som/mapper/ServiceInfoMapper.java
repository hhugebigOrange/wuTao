package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;

public interface ServiceInfoMapper {
	  
    /**
   	 * 增加报修服务信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(ServiceInfo record);
    
    /**
   	 * 根据输入的条件增加相应的报修服务信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(ServiceInfo record);
    
    /**
     * 查找所有工单号查询相应的工单
     */
    List<OrderInfo> selectOrderByNum();
    
    /**
     * 查找所有工单号查询相应的工单
     */
    List<StaffInfo> selectStaffByNum();
    
    /**
     * 查询所有服务满意度里面的工单
     * @return
     */
    List<ServiceInfo> selectAllServiceInfo();
    
    /**
     * 根据条件查询服务满意度里面的工单,最后两个参数为判断条件。
     * @return
     */
    List<ServiceInfo> selectServiceInfByDynamic(@Param("serviceArea")String serviceArea,@Param("custName")String custName,
    		@Param("startDate")String startDate,@Param("endDate")String endDate,
    		@Param("workState")String workState,@Param("custScore")String custScore,@Param("machCode")String machCode
    		,@Param("custScore3")String custScore3,@Param("custScore5")String custScore5,@Param("repairType")String repairType,
    		@Param("faultType")String[] faultType,
    		@Param("page")Integer page,@Param("limit")Integer limit,
    		@Param("ProcessingState")String ProcessingState,
    		@Param("enginnerName")String enginnerName,
    		@Param("identifier")String identifier);
    
    /**
     * 根据条件查询服务满意度里面的工单2.0
     * @return
     */
    List<ServiceInfo> selectServiceInfByDynamic2(@Param("compName")String compName,@Param("woNumber")String woNumber,
    		@Param("sendTime")String sendTime);
    
    
    /**
     * 根据工单号查找相应的服务评价
     */
    ServiceInfo selectServiceInfByDyWoNumber(@Param("woNumber")String woNumber);
    
    /**
     * 根据工单号修改相应的服务评价
     */
    int update(@Param("woNumber")String woNumber,@Param("custSat")String custSat,@Param("custEva")String custEva,@Param("custScore")Integer custScore);
    
    /**
     * 根据工单号修改相应的工单状态和对应的状态时间
     */
    int updateServiceInfo(@Param("woNumber")String woNumber,@Param("woStatus")String woStatus);
    
    /**
     * 根据工程师KPI条件查询
     */
    List<ServiceInfo> selectServiceInfoByengineerKpi(@Param("staffId")String staffId,
    		@Param("custName")String custName,@Param("serviceArea")String serviceArea,
    		@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("faultType")String[] faultType
    		,@Param("page")Integer page,@Param("limit")Integer limit,@Param("identifier")String identifier);
    
    /**
     * 根据坐席客服KPI条件查询
     */
    List<ServiceInfo> selectServiceInfoBySeatServiceKpi(@Param("staffId")String staffId,
    		@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("faultType")String[] faultType);
    
    /**
     * 根据工单分析条件查询
     * @return
     */
    List<ServiceInfo> selectServiceInfoByOrder(@Param("custName")String custName,
    		@Param("woNumber")String woNumber,@Param("faultType")String faultType,
    		@Param("startDate")String startDate,@Param("endDate")String endDate);
    
    /**
     * 根据传入的服务评价对象修改相应的数据
     * @param serviceInfo
     * @return
     */
    boolean upDateServiceInfo(ServiceInfo serviceInfo);
    
    /**
     * 删除服务表单
     * @param serviceInfo
     * @return
     */
    boolean deleteServiceInfo(String woNumber);
    
    /**
     * 工程师微信查询工单
     * @param woStatus
     * @return
     */
    List<ServiceInfo> selectOrderByWeChat(ServiceInfo record);
    
    
    /**
     * 客户微信查询工单
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
     * 根据工单状态返回相应数据
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
    List<ServiceInfo> selectServiceInfByWoStatus(@Param("serviceArea")String serviceArea,@Param("custName")String custName,
    		@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("page")Integer page,@Param("limit")Integer limit,@Param("ProcessingState")String ProcessingState,
    		@Param("identifier")String identifier);
    
}