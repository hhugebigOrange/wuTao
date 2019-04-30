package com.xunwei.som.mapper;

import com.xunwei.som.pojo.WeChatConfig;

public interface WeChatConfigMapper {
	
	WeChatConfig selectById(String id);
	
	int updateById(WeChatConfig id);
	
    int insert(WeChatConfig record);

    int insertSelective(WeChatConfig record);
}