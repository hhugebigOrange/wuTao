package com.xunwei.som.mapper;

import com.xunwei.som.pojo.permissions.Permission;
import com.xunwei.som.pojo.permissions.PermissionKey;

public interface PermissionMapper {
	
    int deleteByPrimaryKey(PermissionKey key);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(PermissionKey key);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
    
    /**
     * 根据用户名查找该用户的权限
     * @return
     */
    Permission selectPermissionByuserId(String userId);
    
    /**
     * 根据角色名更新权限
     * @return
     */
    int updatePermissionByRoleId(String roleId,String permissionId);
    
}