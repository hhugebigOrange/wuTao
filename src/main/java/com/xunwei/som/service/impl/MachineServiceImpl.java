package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.base.model.Machine;
import com.xunwei.som.mapper.MachineMapper;
import com.xunwei.som.service.MachineService;
import com.xunwei.som.util.SqlTools;

public class MachineServiceImpl implements MachineService{

	/**
	 * 查询所有用户
	 */
	@Override
	public List<Machine> selectAllMachine() throws Exception {
		SqlSession session = SqlTools.getSession();
		MachineMapper mapper = session.getMapper(MachineMapper.class);
		try {
			List<Machine> user = mapper.selectAllMachine();
			System.out.println(user.toString());
			session.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<Machine> selectMachineByDynamic(String machineName) throws Exception {
		SqlSession session = SqlTools.getSession();
		MachineMapper mapper = session.getMapper(MachineMapper.class);
		try {
			List<Machine> user = mapper.selectMachineByDynamic(machineName);
			System.out.println(user.toString());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}
	
}
