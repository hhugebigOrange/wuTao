package com.xunwei.som.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.OrderParts;
import com.xunwei.som.pojo.ServiceInfo;
import org.springframework.stereotype.Service;

@Service
public interface ServiceManageService {
	
	/**
	 * 新增工单信息
	 * @param orderInfo
	 * @return
	 */

	public boolean insertOrder(OrderInfo orderInfo);
	
	/**
     * 修改工单号
     * @param woNumber 原工单号
     * @param newWoNumber 新工单号
     * @return
     */
    int updateWoNumber(String woNumber,String newWoNumber);
	
	 /**
     * 获得所有待处理工单的集合
     * @return
     */
    public List<OrderInfo> getOrderByProcessed(String woStatus);
    
    /**
     * 根据工作状获取相应的个数
     * @return
     */
    int getOrderByWoStatus(String woStatus);
    
    /**
     * 根据工单号修改工单状态
     * @return
     */
    int updateWoStatus(String woNumber,String woStatus);
    
    /**
     * 根据工单号修改故障类型
     * @return
     */
    int updateFaultType(String woNumber,String faultType);

    /**
     * 查询所有工单
     * @param 工单状态
     * @return
     */
    List<OrderInfo> selectOrderAll();
    
    /**
     * 根据条件查询相应的工单
     * @param 客户名称
     * @param 机器编码
     * @param 开始日期
     * @param 结束日期
     * @return
     */
    List<OrderInfo> selectOrderByDynamic(String custName,String machCode,String woNumber,String startDate,String endDate,String type,String woSattus);
	
    
    /**
     * 根据条件查询相应的工单2.0
     * @param custName
     * @param serviceArea
     * @param startDate
     * @param endDate
     * @param workState
     * @param faultType
     * @return
     */
    List<OrderInfo> selectOrderByOrder(String custName,String serviceArea,String startDate,@Param("endDate")String endDate,
    		String workState,String[] faultType,String conver,Integer page,Integer limit);
    
    /**
     * 根据工单号查找相应工单
     */
    OrderInfo selectOrderByOrderNum(String orderNum);
    
    /**
     * 根据公司名，获取上一个工单号码
     * @param compPrifix
     * @return
     */
    String selectLastOrderNumber(String compName);
    
    /**
     * 根据工单对象修改工单数据
     * @param record
     * @return
     */
    boolean updateOrder(OrderInfo record);
    
    /**
     * 查询所有待购零件订单
     * @param woStatus
     * @return
     */
    List<OrderInfo> selectOrderByParts(OrderInfo record);
    
    /**
   	 * 根据输入的条件增加相应的报修服务信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(ServiceInfo record);
    
    
    /**
   	 * 根据输入的条件增加相应的零件订购信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(OrderParts record);
    
    /**
   	 * 根据输入的条件查看零件订购信息
   	 * @param priNumber
   	 * @return
   	 */
    OrderParts selectSelective(OrderParts record);
    
    
    /**
   	 * 根据输入的条件增加修改相应的零件订购信息
   	 * @param priNumber
   	 * @return
   	 */
    int updateSelective(OrderParts record);
    
}
