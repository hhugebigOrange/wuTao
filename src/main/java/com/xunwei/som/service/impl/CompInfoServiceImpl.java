package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.CompInfoMapper;
import com.xunwei.som.pojo.CompInfo;
import com.xunwei.som.service.CompInfoService;
import com.xunwei.som.util.SqlTools;

public class CompInfoServiceImpl implements CompInfoService{

	@Override
	public List<CompInfo> selectAllComp() {
		SqlSession session = SqlTools.getSession();
		CompInfoMapper mapper = session.getMapper(CompInfoMapper.class);
		try {
			List<CompInfo> compInfos= mapper.selectAllComp();
			session.commit();
			return compInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(CompInfo record) {
		SqlSession session = SqlTools.getSession();
		CompInfoMapper mapper = session.getMapper(CompInfoMapper.class);
		try {
			int result= mapper.updateByPrimaryKeySelective(record);
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
	public int insertSelective(CompInfo record) {
		SqlSession session = SqlTools.getSession();
		CompInfoMapper mapper = session.getMapper(CompInfoMapper.class);
		try {
			int result= mapper.insertSelective(record);
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
	public CompInfo selectByPrimaryKey(Integer compNumber) {
		SqlSession session = SqlTools.getSession();
		CompInfoMapper mapper = session.getMapper(CompInfoMapper.class);
		try {
			CompInfo result= mapper.selectByPrimaryKey(compNumber);
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
}
