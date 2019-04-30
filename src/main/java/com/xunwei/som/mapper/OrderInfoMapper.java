package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.OrderInfo;

public interface OrderInfoMapper {
	/**
	 * 新增工单
	 * @param record
	 * @return
	 */
    boolean insert(OrderInfo record);
    
    /**
     * 修改工单号
     * @param woNumber 原工单号
     * @param newWoNumber 新工单号
     * @return
     */
    int updateWoNumber(String woNumber,String newWoNumber);
    
    /**
     * 根据工作状获取相应集合
     * @return
     */
    List<OrderInfo> getOrderByProcessed(String woStatus);
    
    /**
     * 根据工作状获取相应的个数
     * @return
     */
    int getOrderByWoStatus(String woStatus);
    
    /**
     * 根据工单号修改相应的工单状态，和对应的时间
     * @return
     */
    int updateWoStatus(@Param("woNumber")String woNumber,@Param("woStatus")String woStatus);
    
    
    /**
     * 根据工单号修改故障类型
     * @return
     */
    int updateFaultType(@Param("woNumber")String woNumber,@Param("faultType")String faultType);

    /**
     * 查询所有工单
     * @param woStatus
     * @return
     */
    List<OrderInfo> selectOrderAll();
    
    /**
     * 查询条件查询
     * @param woStatus
     * @return
     */
    List<OrderInfo> selectOrderByDynamic(@Param("custName")String custName,@Param("machCode")String machCode,@Param("woNumber")String woNumber,
    		@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("type")String type,@Param("woStatus")String woStatus);
    
    
    /**
     * 工单概况查询条件
     */
    List<OrderInfo> selectOrderByOrder(@Param("custName")String custName,@Param("serviceArea")String serviceArea,
    		@Param("startDate")String startDate,@Param("endDate")String endDate,
    		@Param("workState")String workState,@Param("faultType")String[] faultType,@Param("conver")String conver,
    		@Param("page")Integer page,
    		@Param("limit")Integer limit);
    
    /**
     * 根据工单号查找相应工单
     */
    OrderInfo selectOrderByOrderNum(@Param("orderNum")String orderNum);
    
    /**
     * 根据公司名，获取上一个工单号码
     * @param compPrifix
     * @return
     */
    String selectLastOrderNumber(@Param("compName")String compName);
    
    /**
     * 查询所有的工单KPI
     * @return
     */
    List<OrderInfo> selectOrderInfoByKpi();
    
    /**
     * 查询所有的工单KPI
     * @return
     */
    List<OrderInfo> selectOrderInfoBySeat();
    
    /**
     * 根据工单对象修改工单数据
     * @param record
     * @return
     */
    boolean updateOrder(OrderInfo record);
    
    /**
     * 根据工单号删除工单
     * @param record
     * @return
     */
    boolean deleteOrder(String woNumber);
    /**
     * 查询所有待购零件订单
     * @param woStatus
     * @return
     */
    List<OrderInfo> selectOrderByParts(OrderInfo record);
    
    
    
}