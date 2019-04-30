package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.StaffInfoMapper;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.service.StaffInfoService;
import com.xunwei.som.util.SqlTools;

public class StaffInfoServiceImpl implements StaffInfoService {

	/**
	 * 新增用户
	 */
	@Override
	public boolean insertStaff(StaffInfo staff) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			boolean result = mapper.insert(staff);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * 根据条件查询用户
	 */
	@Override
	public List<StaffInfo> getStaffByDynamic(String name, String compName, String post, String phone, Integer page,
			Integer limit,String workCond,String identifier) {
		SqlSession session = SqlTools.getSession();
		StaffInfo staff = new StaffInfo();
		staff.setName(name);
		staff.setCompName(compName);
		staff.setPost(post);
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			List<StaffInfo> staffs = mapper.selectStaffByDynamic(name, compName, post, phone, page, limit,workCond,identifier);
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 根据职工的公司名查询所属公司ID
	 */

	@Override
	public Integer selectCompIdByName(String name) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			Integer result = mapper.selectCompIdByName(name);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return -1;
	}

	/**
	 * 查找所有用户
	 */
	@Override
	public List<StaffInfo> selectAllStaff() {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			List<StaffInfo> staffs = mapper.selectAllStaff();
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public StaffInfo selectStaffByNum(String staffNo) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			StaffInfo staff = mapper.selectStaffByNum(staffNo);
			session.commit();
			return staff;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 根据传进来的用户修改相应数据
	 */
	@Override
	public int updateStaff(StaffInfo staffInfo) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			int result = mapper.update(staffInfo);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return -1;
	}

	@Override
	public int updateDateByStaffId(String staffId) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			int result = mapper.updateDateByStaffId(staffId);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return -1;
	}

	@Override
	public List<StaffInfo> selectStaffByOrder() {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			List<StaffInfo> staffInfos = mapper.selectStaffByOrder();
			session.commit();
			return staffInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<StaffInfo> selectStaffBySeat() {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			List<StaffInfo> staffInfos = mapper.selectStaffBySeat();
			session.commit();
			return staffInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public int deleteStaffById(String staffId) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			int result = mapper.deleteStaffById(staffId);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		return -1;
	}
}
