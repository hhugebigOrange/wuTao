package com.xunwei.som.mapper;

import java.util.List;

import com.xunwei.som.pojo.CustomerFeedback;

public interface CustomerFeedbackMapper {
	
    int insert(CustomerFeedback record);

    /**
     * 新增客户评价
     * @param record
     * @return
     */
    int insertSelective(CustomerFeedback record);
    
    /**
     * 查询所有客户反馈
     * @return
     */
    List<CustomerFeedback> selectAllFeedback();
}