package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.permissions.User;

public interface UserMapper {
	
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByUserId(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    /**
     * 查找所有用户
     * @return
     */
    List<User> selectAllUser();
    
    /**
     * 根据角色查找相应的用户群
     */
    List<User> selectUserByRole(@Param("role")String role,@Param("custName")String custName,@Param("phone")String phone);
    
}