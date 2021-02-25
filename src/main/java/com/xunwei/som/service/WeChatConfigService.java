package com.xunwei.som.service;

import com.xunwei.som.pojo.WeChatConfig;
import org.springframework.stereotype.Service;

@Service
public interface WeChatConfigService {
	
	WeChatConfig selectById(String id);

	int updateById(WeChatConfig id);
}
