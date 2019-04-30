package com.xunwei.som.mapper;

import com.xunwei.som.pojo.permissions.UserRole;

public interface UserRoleMapper {
	
    int deleteByPrimaryKey(String userId);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}