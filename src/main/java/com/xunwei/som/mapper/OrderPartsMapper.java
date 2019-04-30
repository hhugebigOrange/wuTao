package com.xunwei.som.mapper;

import com.xunwei.som.pojo.OrderParts;

public interface OrderPartsMapper {
    int deleteByPrimaryKey(String woNumber);

    int insert(OrderParts record);

    /**
     * 插入零件订购状态
     * @param record
     * @return
     */
    int insertSelective(OrderParts record);

    OrderParts selectByPrimaryKey(String woNumber);

    /**
     * 根据输入的条件修改工单零件订购状态
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderParts record);

    int updateByPrimaryKey(OrderParts record);
}