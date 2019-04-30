package com.xunwei.som.mapper;

import com.xunwei.som.pojo.OpenId;

public interface OpenIdMapper {
	
    int deleteByPrimaryKey(OpenId key);

    int insert(OpenId record);

    int insertSelective(OpenId record);

    OpenId selectByPrimaryKey(OpenId key);

    int updateByPrimaryKeySelective(OpenId record);

    int updateByPrimaryKey(OpenId record);
}