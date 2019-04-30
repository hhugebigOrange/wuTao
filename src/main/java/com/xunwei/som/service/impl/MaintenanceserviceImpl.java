package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.MaintenancePerformMapper;
import com.xunwei.som.mapper.MaintenanceMapper;
import com.xunwei.som.pojo.MaintenancePerform;
import com.xunwei.som.pojo.Maintenance;
import com.xunwei.som.service.Maintenanceservice;
import com.xunwei.som.util.SqlTools;

public class MaintenanceserviceImpl implements Maintenanceservice {

	/**
	 * 新增保养的方法
	 */
	@Override
	public boolean insertOrder(Maintenance tenance) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			boolean result = mapper.insert(tenance);
			System.out.println(result?"添加成功":"添加失败");
			session.commit();
			session.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return false;
	}
	/**
	 * 查看所有保养计划
	 */
	@Override
	public List<Maintenance> select() {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			List<Maintenance> maintenances=mapper.select();
			session.commit();
			return maintenances;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		
		return null;
	}
	/**
	 * 
	 * 根据服务区域查看相应的保养计划
	 */
	@Override
	public List<Maintenance> selectmaintenance(String custName,String compName,String staffName
			,String machCode,String startDate,String endDate,Integer page,Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			List<Maintenance> result = mapper.selectmaintenance(custName,compName,staffName,machCode,startDate,endDate,page,limit,identifier);
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
	public List<Maintenance> selectByCycle(String Cycle, String responsibleId,String compName,String custName) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			List<Maintenance> result = mapper.selectByCycle(Cycle, responsibleId,compName,custName);
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
	public int updateMaintenance(String contractNo, String machCode, Integer state,String lastTime) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			int result = mapper.updateMaintenance(contractNo, machCode, state,lastTime);
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
	public Maintenance selectOneMaintenance(String contractNo, String machCode) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			Maintenance result = mapper.selectOneMaintenance(contractNo, machCode);
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
	public int insertSelective(MaintenancePerform record) {
		SqlSession session = SqlTools.getSession();
		MaintenancePerformMapper mapper = session.getMapper(MaintenancePerformMapper.class);
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
	@Override
	public List<MaintenancePerform> selectByDynamic(MaintenancePerform record, String startDate, String endDate) {
		SqlSession session = SqlTools.getSession();
		MaintenancePerformMapper mapper = session.getMapper(MaintenancePerformMapper.class);
		try {
			List<MaintenancePerform> result = mapper.selectByDynamic(record, startDate, endDate);
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
	public List<Maintenance> selectByContract(String responsibleId) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			List<Maintenance> result = mapper.selectByContract(responsibleId);
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
	public int updateMaintenance(Maintenance record) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			int result = mapper.upDateMaintenance(record);
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
	public int deleteMaintenance(String machCode) {
		SqlSession session = SqlTools.getSession();
		MaintenanceMapper mapper = session.getMapper(MaintenanceMapper.class);
		try {
			int result = mapper.deleteMaintenance(machCode);
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
