package com.xunwei.som.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.WeChatConfigMapper;
import com.xunwei.som.pojo.WeChatConfig;
import com.xunwei.som.service.WeChatConfigService;
import com.xunwei.som.util.SqlTools;

public class WeChatConfigServiceImpl implements WeChatConfigService{

	@Override
	public WeChatConfig selectById(String id) {
		SqlSession session = SqlTools.getSession();
		WeChatConfigMapper mapper = session.getMapper(WeChatConfigMapper.class);
		try {
			WeChatConfig result = mapper.selectById(id);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updateById(WeChatConfig id) {
		SqlSession session = SqlTools.getSession();
		WeChatConfigMapper mapper = session.getMapper(WeChatConfigMapper.class);
		try {
			int result = mapper.updateById(id);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return -1;
	}
	
}
