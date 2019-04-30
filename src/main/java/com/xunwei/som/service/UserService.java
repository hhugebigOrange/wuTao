package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.pojo.Assessment;
import com.xunwei.som.pojo.OpenId;
import com.xunwei.som.pojo.permissions.RolePermission;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;

/**
 * 用户接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
     * 查找所有用户
     * @return
     */
    List<User> selectAllUser();
    
    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    User selectByUserId(String userId);
    
    /**
     * 修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);
    
    /**
     * 根据客户或者员工进行筛选
     */
    List<User> selectUserByRole(String role,String custName,String phone);
    
    /**
     * 根据客户id查找所属的角色
     * @param userId
     * @return
     */
    UserRole selectByPrimaryKey(String userId);
    
    /**
     * 增加用户
     * @param record
     * @return
     */
    int insertSelective(User record);
    
    /**
     * 增加用户-权限对应表
     * @param record
     * @return
     */
    int insertSelective(UserRole record);
    
    RolePermission selectByKey(String id);
    
    int updateByPrimaryKeySelective(RolePermission record);
    
    OpenId selectByPrimaryKey(OpenId key);
    
    int deleteByPrimaryKey(String userId);
    
    int deleteUserRolePrimaryKey(String userId);
    
    /**
     * 修改参数设定
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Assessment record);
    
    /**
     * 
     * @param assessmentName
     * @return
     */
    Assessment selectAssessmentNameByPrimaryKey(String assessmentName);
    
    /**
     * 更新角色表
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserRole record);
}
