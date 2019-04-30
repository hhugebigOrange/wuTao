package com.xunwei.som.mapper;

import com.xunwei.som.pojo.permissions.Role;
import com.xunwei.som.pojo.permissions.RoleKey;

public interface RoleMapper {
    int deleteByPrimaryKey(RoleKey key);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(RoleKey key);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}