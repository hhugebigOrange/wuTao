package com.xunwei.som.mapper;

import com.xunwei.som.pojo.AdminLoginTable;

public interface AdminLoginTableMapper {
	
	/**
	 * 根据主键删除登陆信息
	 * @param priNumber
	 * @return
	 */
    int deleteByPrimaryKey(Integer priNumber);
    
    /**
	 * 增加登陆信息
	 * @param priNumber
	 * @return
	 */
    int insert(AdminLoginTable record);
    
    /**
   	 * 根据输入的条件增加相应的登陆信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(AdminLoginTable record);
    
    /**
     * 根据主键查找登陆信息
     * @param priNumber
     * @return
     */
    AdminLoginTable selectByPrimaryKey(Integer priNumber);

    /**
     * 根据输入条件更新登陆信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKeySelective(AdminLoginTable record);
    
    /**
     * 更新登陆信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKey(AdminLoginTable record);
}