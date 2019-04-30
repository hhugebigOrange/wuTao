package com.xunwei.som.mapper;

import com.xunwei.som.pojo.Slogan;

public interface SloganMapper {
	
	/**
	 * 插入口号
	 * @param record
	 * @return
	 */
    int insert(Slogan record);
    
    int updateSlogan(String slogan);

    int insertSelective(Slogan record);
    
    /**
     * 查询口号
     * @return
     */
    String selectSlogan();
}