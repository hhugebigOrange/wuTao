package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.AssetNumber;

public interface AssetNumberMapper {
	
	 /**
   	 * 增加资产盘点信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(AssetNumber record);
    
    /**
   	 * 根据输入的条件增加相应的资产盘点信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(AssetNumber record);
    
    /**
   	 * 根据输入的条件修改相应的资产盘点信息
   	 * @param priNumber
   	 * @return
   	 */
    int updateSelective(AssetNumber record);
    
    /**
   	 * 修改资产编号
   	 * @param priNumber
   	 * @return
   	 */
    int updateAsset(@Param(value="oldNumber")String oldNumber,@Param(value="newNumber")String newNumber);
    
    /**
   	 * 删除资产信息
   	 * @param priNumber
   	 * @return
   	 */
    int deleteAsset(String Number);
    
    /**
     * 根据条件查询资产属性
     * @param machineName
     * @return
     */
    List<AssetNumber> selectByDynamic(@Param(value="serviceArea")String serviceArea,
    		@Param(value="custArea")String custArea,
    		@Param(value="assetAttr")String assetAttr,
    		@Param(value="page")Integer page,
    		@Param(value="limit")Integer limit,
    		@Param(value="identifier")String identifier);
}