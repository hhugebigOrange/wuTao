package com.xunwei.som.service;


import com.xunwei.som.pojo.permissions.Permission;
import org.springframework.stereotype.Service;

/**
 * 权限接口
 * @author Administrator
 *
 */
@Service
public interface PermissionService {

	/**
     * 根据用户名id查找该用户的权限
     * @return
     */
    Permission selectPermissionByUserId(String userId);
    
    /**
     * 根据角色名更新权限
     * @return
     */
    int updatePermissionByRoleId(String roleId,String permissionId);
	
}
