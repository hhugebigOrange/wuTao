package com.xunwei.som.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.SloganMapper;
import com.xunwei.som.service.SloganService;
import com.xunwei.som.util.SqlTools;

public class SloganServiceImpl implements SloganService{

	/**
	 * 查找口号
	 */
	@Override
	public String selectSlogan() {
		SqlSession session = SqlTools.getSession();
		SloganMapper mapper = session.getMapper(SloganMapper.class);
		try {
			String result = mapper.selectSlogan();
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
	public int updateSlogan(String slogan) {
		SqlSession session = SqlTools.getSession();
		SloganMapper mapper = session.getMapper(SloganMapper.class);
		try {
			int result = mapper.updateSlogan(slogan);
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
