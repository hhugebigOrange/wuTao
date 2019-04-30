package com.xunwei.som.service.impl;


import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.ParameterSettingMapper;
import com.xunwei.som.pojo.permissions.ParameterSetting;
import com.xunwei.som.service.ParameterSettingService;
import com.xunwei.som.util.SqlTools;

public class ParameterSettingServiceImpl implements ParameterSettingService{

	@Override
	public List<ParameterSetting> selectByPrimaryKey() {
		SqlSession session = SqlTools.getSession();
		ParameterSettingMapper mapper = session.getMapper(ParameterSettingMapper.class);
		try {
			List<ParameterSetting> parameterSettings = mapper.selectByPrimaryKey();
			session.commit();
			return parameterSettings;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updateByPrimaryKey(ParameterSetting record) {
		SqlSession session = SqlTools.getSession();
		ParameterSettingMapper mapper = session.getMapper(ParameterSettingMapper.class);
		try {
			int result = mapper.updateByPrimaryKey(record);
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

	@Override
	public int insertSelective(ParameterSetting record) {
		SqlSession session = SqlTools.getSession();
		ParameterSettingMapper mapper = session.getMapper(ParameterSettingMapper.class);
		try {
			int result = mapper.insertSelective(record);
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
