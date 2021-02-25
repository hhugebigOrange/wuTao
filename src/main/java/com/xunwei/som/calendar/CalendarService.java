package com.xunwei.som.calendar;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CalendarService {

	/**
	 * 插入一条日期记录
	 * @param record
	 * @return
	 */
    int insert(Calendars record);
    
    /**
     * 查询所有日历信息
     * @return
     */
    List<Calendars> selectAllCalendar();
	
}
