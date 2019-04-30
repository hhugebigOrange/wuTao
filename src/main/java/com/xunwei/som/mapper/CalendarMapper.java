package com.xunwei.som.mapper;

import java.util.List;

import com.xunwei.som.calendar.Calendars;

public interface CalendarMapper {
	
	/**
	 * 插入一条日期记录
	 * @param record
	 * @return
	 */
    int insert(Calendars record);

    int insertSelective(Calendars record);
    
    /**
     * 查询所有日历信息
     * @return
     */
    List<Calendars> selectAllCalendar();
    
    
}