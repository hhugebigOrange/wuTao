package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

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
		}
		return null;
	}
	
	@Test
	public void get() throws Exception{
		List<Machine> user=selectMachineByDynamic("x");
		if(user==null){
			System.out.println("空");
		}else{
			System.out.println("不是空");
		}
		for (Machine machine : user) {
			System.out.println(machine);
		}
	}
	
}
