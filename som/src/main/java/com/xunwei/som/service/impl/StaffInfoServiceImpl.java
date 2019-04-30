package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.base.model.staffInfo;
import com.xunwei.som.mapper.StaffInfoMapper;
import com.xunwei.som.service.StaffInfoService;
import com.xunwei.som.util.SqlTools;

public class StaffInfoServiceImpl implements StaffInfoService{

	
	/**
	 * 新增用户
	 */
	@Override
	public boolean insertStaff(staffInfo staff) {
		SqlSession session = SqlTools.getSession();
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			boolean result = mapper.insert(staff);
			System.out.println(result?"添加成功":"添加失败");
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return false;
	}
  
	
	
	/**
	 * 根据条件查询用户
	 */
	@Override
	public List<staffInfo> getStaffByDynamic(String name, String compName, String post) {
		SqlSession session = SqlTools.getSession();
		staffInfo staff=new staffInfo();
		staff.setName(name);
		staff.setCompName(compName);
		staff.setPost(post);
		StaffInfoMapper mapper = session.getMapper(StaffInfoMapper.class);
		try {
			List<staffInfo> staffs = mapper.selectStaffByDynamic(staff);
			System.out.println(staffs.toString());
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return null;
	}
	
}
