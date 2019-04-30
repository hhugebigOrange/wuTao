package com.xunwei.som.mapper;

import java.util.List;

import com.xunwei.som.pojo.CompInfo;

public interface CompInfoMapper {
	/**
	 * 根据主键删除公司信息
	 * @param priNumber
	 * @return
	 */
    int deleteByPrimaryKey(Integer compNumber);
    
    /**
   	 * 增加公司信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(CompInfo record);
    
    /**
   	 * 根据输入的条件增加相应的公司信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(CompInfo record);
    
    /**
     * 根据主键查找公司信息
     * @param priNumber
     * @return
     */
    CompInfo selectByPrimaryKey(Integer compNumber);

    /**
     * 根据输入条件更新公司信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKeySelective(CompInfo record);

    /**
     * 更新公司信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKey(CompInfo record);
    
    /**
     * 查找所有分公司信息
     * @return
     */
    List<CompInfo> selectAllComp();
}