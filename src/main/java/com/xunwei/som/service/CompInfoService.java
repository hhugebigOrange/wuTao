package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.pojo.CompInfo;
import org.springframework.stereotype.Service;

/**
 * 分公司服务接口
 * @author Administrator
 *
 */
@Service
public interface CompInfoService {

	/**
	 * 查看所有分公司信息
	 * @return
	 */
	List<CompInfo> selectAllComp();
	
	/**
     * 根据输入条件更新公司信息
     * @param priNumber
     * @return
     */
    int updateByPrimaryKeySelective(CompInfo record);
    
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
	
}
