package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.DevMainMapper;
import com.xunwei.som.pojo.DevMain;
import com.xunwei.som.service.DevMainService;
import com.xunwei.som.util.SqlTools;

public class DevMainServiceImpl implements DevMainService{
	
	@Override
	public List<DevMain> selectDevMain(String compName,String custName,String engineerName) {
		SqlSession session = SqlTools.getSession();
		DevMainMapper mapper = session.getMapper(DevMainMapper.class);
		try {
			List<DevMain> devMains = mapper.selectDevMain(compName,custName,engineerName);
			session.commit();
			return devMains;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}
	
}
