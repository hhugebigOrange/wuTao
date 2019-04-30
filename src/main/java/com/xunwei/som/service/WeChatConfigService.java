package com.xunwei.som.service;

import com.xunwei.som.pojo.WeChatConfig;

public interface WeChatConfigService {
	
	WeChatConfig selectById(String id);

	int updateById(WeChatConfig id);
}
