package com.xunwei.som.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.AssetNumber;
import org.springframework.stereotype.Service;

/**
 * 资产管理接口
 * @author Administrator
 *
 */
@Service
public interface AsAetNumberService {

	/**
	 * 插入一条资产信息
	 * @param assetNumber
	 * @return
	 */
	boolean insert(AssetNumber assetNumber);
	
	/**
     * 根据条件查询资产属性
     * @param machineName
     * @return
     */
    List<AssetNumber> selectByDynamic(String serviceArea,String custArea,String assetAttr,Integer page,Integer limit,String identifier);
	
    /**
   	 * 根据输入的条件修改相应的资产盘点信息
   	 * @param priNumber
   	 * @return
   	 */
    boolean updateSelective(AssetNumber record);
    
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
}
