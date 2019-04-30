package com.xunwei.som.calendar.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.calendar.Calendars;
import com.xunwei.som.calendar.CalendarService;
import com.xunwei.som.mapper.CalendarMapper;
import com.xunwei.som.util.SqlTools;

public class CalendarServiceImpl implements CalendarService{

	@Override
	public int insert(Calendars record) {
		SqlSession session = SqlTools.getSession();
		CalendarMapper mapper = session.getMapper(CalendarMapper.class);
		try {
			int result= mapper.insert(record);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		session.close();
		return -1;
	}

	@Override
	public List<Calendars> selectAllCalendar() {
		SqlSession session = SqlTools.getSession();
		CalendarMapper mapper = session.getMapper(CalendarMapper.class);
		try {
			List<Calendars> Calendars= mapper.selectAllCalendar();
			session.commit();
			return Calendars;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		session.close();
		return null;
	}

}
