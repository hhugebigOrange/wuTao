package com.xunwei.som.service.impl;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.AssessmentMapper;
import com.xunwei.som.mapper.OpenIdMapper;
import com.xunwei.som.mapper.RolePermissionMapper;
import com.xunwei.som.mapper.UserMapper;
import com.xunwei.som.mapper.UserRoleMapper;
import com.xunwei.som.pojo.Assessment;
import com.xunwei.som.pojo.OpenId;
import com.xunwei.som.pojo.permissions.RolePermission;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.UserService;
import com.xunwei.som.util.SqlTools;

public class UserServiceImpl implements UserService{

	@Override
	public List<User> selectAllUser() {
		SqlSession session = SqlTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<User> users = mapper.selectAllUser();
			session.commit();
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public User selectByUserId(String userId) {
		SqlSession session = SqlTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			User user = mapper.selectByUserId(userId);
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
	public int updateByPrimaryKeySelective(User record) {
		SqlSession session = SqlTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(record);
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
	public List<User> selectUserByRole(String role,String custName,String phone) {
		SqlSession session = SqlTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<User> users = mapper.selectUserByRole(role,custName,phone);
			session.commit();
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public UserRole selectByPrimaryKey(String userId) {
		SqlSession session = SqlTools.getSession();
		UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
		try {
			UserRole userRole = mapper.selectByPrimaryKey(userId);
			session.commit();
			return userRole;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int insertSelective(User record) {
		SqlSession session = SqlTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
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
	public int insertSelective(UserRole record) {
		SqlSession session = SqlTools.getSession();
		UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
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
	public RolePermission selectByKey(String id) {
		SqlSession session = SqlTools.getSession();
		RolePermissionMapper mapper = session.getMapper(RolePermissionMapper.class);
		try {
			RolePermission result = mapper.selectByPrimaryKey(id);
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
	public int updateByPrimaryKeySelective(RolePermission record) {
		SqlSession session = SqlTools.getSession();
		RolePermissionMapper mapper = session.getMapper(RolePermissionMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(record);
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
	public OpenId selectByPrimaryKey(OpenId key) {
		SqlSession session = SqlTools.getSession();
		OpenIdMapper mapper = session.getMapper(OpenIdMapper.class);
		try {
			OpenId result = mapper.selectByPrimaryKey(key);
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
	public int deleteByPrimaryKey(String userId) {
		SqlSession session = SqlTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.deleteByPrimaryKey(userId);
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
	public int deleteUserRolePrimaryKey(String userId) {
		SqlSession session = SqlTools.getSession();
		UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
		try {
			int result = mapper.deleteByPrimaryKey(userId);
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
	public int updateByPrimaryKeySelective(Assessment record) {
		SqlSession session = SqlTools.getSession();
		AssessmentMapper mapper = session.getMapper(AssessmentMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(record);
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
	public Assessment selectAssessmentNameByPrimaryKey(String assessmentName) {
		SqlSession session = SqlTools.getSession();
		AssessmentMapper mapper = session.getMapper(AssessmentMapper.class);
		try {
			Assessment result = mapper.selectByPrimaryKey(assessmentName);
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
	public int updateByPrimaryKeySelective(UserRole record) {
		SqlSession session = SqlTools.getSession();
		UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(record);
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
