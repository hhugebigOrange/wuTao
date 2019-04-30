package com.xunwei.som.service.impl;


import org.apache.ibatis.session.SqlSession;
import com.xunwei.som.mapper.PermissionMapper;
import com.xunwei.som.pojo.permissions.Permission;
import com.xunwei.som.service.PermissionService;
import com.xunwei.som.util.SqlTools;

public class PermissionServiceImpl implements PermissionService{

	@Override
	public Permission selectPermissionByUserId(String userId) {
		SqlSession session = SqlTools.getSession();
		PermissionMapper mapper = session.getMapper(PermissionMapper.class);
		try {
			Permission Permissions = mapper.selectPermissionByuserId(userId);
			session.commit();
			return Permissions;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updatePermissionByRoleId(String roleId, String permissionId) {
		SqlSession session = SqlTools.getSession();
		PermissionMapper mapper = session.getMapper(PermissionMapper.class);
		try {
			int result = mapper.updatePermissionByRoleId(roleId, permissionId);
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
