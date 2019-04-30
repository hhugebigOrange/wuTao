package com.xunwei.som.mapper;

import com.xunwei.som.pojo.AssetInfo;

public interface AssetInfoMapper {
	/**
	 * 根据主键删除资产信息
	 * @param priNumber
	 * @return
	 */
    int deleteByPrimaryKey(Integer assetNumber);
    
    /**
   	 * 增加资产信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(AssetInfo record);
    
    /**
   	 * 根据输入的条件增加相应的资产信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(AssetInfo record);
    
    /**
     * 根据主键查找资产信息
     * @param priNumber
     * @return
     */
    AssetInfo selectByPrimaryKey(Integer assetNumber);

    /**
     * 根据输入条件更新资产信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKeySelective(AssetInfo record);
    
    /**
     * 更新资产信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKey(AssetInfo record);
}