package com.xunwei.som.mapper;

import com.xunwei.som.pojo.Assessment;

public interface AssessmentMapper {
	
    int deleteByPrimaryKey(String assessmentName);

    int insert(Assessment record);

    int insertSelective(Assessment record);

    Assessment selectByPrimaryKey(String assessmentName);

    int updateByPrimaryKeySelective(Assessment record);

    int updateByPrimaryKey(Assessment record);
}