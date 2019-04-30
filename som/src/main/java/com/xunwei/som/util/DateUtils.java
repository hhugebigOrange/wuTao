package com.xunwei.som.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sun.corba.se.spi.orb.StringPair;

public class DateUtils {
	public static  SimpleDateFormat formatter_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	public static  SimpleDateFormat formatter_HHmmss = new SimpleDateFormat("HH:mm:ss");
	public static  SimpleDateFormat formatter_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static  SimpleDateFormat formatter_yyyymmddhhmmss = new SimpleDateFormat("yyyyMMdd HHmmss");

	
	public static String getDate(SimpleDateFormat simpleDateFormate,Date date){
		return simpleDateFormate.format(date);
	}
	public static String getDate(SimpleDateFormat simpleDateFormate){
		return simpleDateFormate.format(new Date());
	}
	public static String getDate(Date date){
		return formatter_yyyyMMddHHmmss.format(date);
	}
	public static String getDate(){
		return formatter_yyyyMMddHHmmss.format(new Date());
	}
	
	public static String getDateFORyyyyMMddHHmmss(Date date){
		return formatter_yyyyMMddHHmmss.format(date);
	}
	public static String getDateFORyyyyMMddHHmmss(){
		return formatter_yyyyMMddHHmmss.format(new Date());
	}
	public static String getDateFORyyyyMMdd(Date date){
		return formatter_yyyyMMdd.format(date);
	}
	public static String getDateFORyyyyMMdd(){
		return formatter_yyyyMMdd.format(new Date());
	}
	public static String getDateFORHHmmss(Date date){
		return formatter_HHmmss.format(date);
	}
	public static String getDateFORHHmmss(){
		return formatter_HHmmss.format(new Date());
	}
	
	public static Date StringToDate(SimpleDateFormat simpleDateFormate,String dateStr) throws ParseException{
		return simpleDateFormate.parse(dateStr);
	}
	public static Date StringToDateFORyyyyMMdd(String dateStr) throws ParseException{
		return formatter_yyyyMMdd.parse(dateStr);
	}
	
	public static Date StringToDateFORyyyymmddhhmmss(String dateStr) throws ParseException{
		return formatter_yyyymmddhhmmss.parse(dateStr);
	}
	
	/**
	 * 时间比较
	 * @param DATE1 时间1
	 * @param DATE2 时间2
	 * @param formatter 格式
	 * @return  1:时间1在时间2后，-1:时间1在时间2前 0:时间1和时间2一样
	 */
	public static int compareDate(String DATE1, String DATE2,String formatter) {
		SimpleDateFormat df = new SimpleDateFormat(formatter);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	/**
	 * 时间比较
	 * @param DATE1 时间1
	 * @param DATE2 时间2
	 * @return  1:时间1在时间2后，-1:时间1在时间2前 0:时间1和时间2一样
	 */
	public static int compareDate(Date DATE1, Date DATE2) {
		try {
			Date dt1 = DATE1;
			Date dt2 = DATE2;
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取当前月的第一天和最后一天
	 * @return
	 */
	public static Map<String, Date> getTheCurrentMonthDate () {
		Map<String, Date> map = new HashMap<String, Date>();
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		// 获取当前年份
		int year = calendar.get(Calendar.YEAR);
		// 获取当前月份
		int month = calendar.get(Calendar.MONTH);
		// 起始时间
		calendar.clear();
		calendar.set(year, month, 1);
		Date start = calendar.getTime();
		calendar.clear();
		if (month == 11) {
			// 当前月为最后月，年份+1
			calendar.set(year + 1, 1, 1);
		} else {
			// 普通月月份+1
			calendar.set(year, month + 1, 1);
		}
		// 结束时间
		Date end = calendar.getTime();
		map.put("sDate", start);
		map.put("eDate", end);
		return map;
	}
}
