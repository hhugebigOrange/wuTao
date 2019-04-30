package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.xunwei.som.mapper.PrivilegeTableMapper;
import com.xunwei.som.pojo.PrivilegeTable;
import com.xunwei.som.service.PrivilegeService;
import com.xunwei.som.util.SqlTools;

public class PrivilegeServiceImpl implements PrivilegeService{

	@Override
	public List<PrivilegeTable> selectAllPrivilege() {
		SqlSession session = SqlTools.getSession();
		PrivilegeTableMapper mapper = session.getMapper(PrivilegeTableMapper.class);
		try {
			List<PrivilegeTable> privileges = mapper.selectAllPrivilegeTable();
			session.commit();
			return privileges;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public PrivilegeTable selectPasswordByUser(String userName) {
		SqlSession session = SqlTools.getSession();
		PrivilegeTableMapper mapper = session.getMapper(PrivilegeTableMapper.class);
		try {
			PrivilegeTable privi=mapper.selectPasswordByUser(userName);
			session.commit();
			return privi;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updatePrivile(String userName, String password) {
		SqlSession session = SqlTools.getSession();
		PrivilegeTableMapper mapper = session.getMapper(PrivilegeTableMapper.class);
		try {
			int result = mapper.updatePrivile(userName, password);
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
	
	@Test
	public void updatePrivile() {
		SqlSession session = SqlTools.getSession();
		PrivilegeTableMapper mapper = session.getMapper(PrivilegeTableMapper.class);
		try {
			PrivilegeTable privi=mapper.selectPasswordByUser("admin");
			System.out.println(privi);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
	}
}
