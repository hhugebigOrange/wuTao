package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.PrivilegeTable;

public interface PrivilegeTableMapper {
	/**
	 * 根据主键删除用户权限信息
	 * @param priNumber
	 * @return
	 */
    int deleteByPrimaryKey(Integer userId);
    
    /**
   	 * 增加用户权限信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(PrivilegeTable record);

    /**
   	 * 根据输入的条件增加相应的用户权限信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(PrivilegeTable record);

    /**
     * 根据主键查找用户权限信息
     * @param priNumber
     * @return
     */
    PrivilegeTable selectByPrimaryKey(Integer userId);

    /**
     * 根据输入条件更新用户权限信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKeySelective(PrivilegeTable record);
    
    /**
     * 更新用户权限信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKey(PrivilegeTable record);
    
    /**
     * 查询所有用户
     * @return
     */
    List<PrivilegeTable> selectAllPrivilegeTable();
    
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    PrivilegeTable selectPasswordByUser(@Param("userName")String userName);
    
    
    /**
     * 根据用户名修改密码
     * @param userName
     * @return
     */
    int updatePrivile(@Param("userName")String userName,@Param("password")String password);
    
}