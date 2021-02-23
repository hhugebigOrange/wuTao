package com.xunwei.som.calendar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.xunwei.som.calendar.impl.CalendarServiceImpl;
import com.xunwei.som.util.ExcelUtils;

/**
 * 日历工具类
 * 
 * @author Administrator
 *
 */

public class CalendarTool {

	// 日历集合
	public static List<Calendars> Calendars = new ArrayList<>();
	// 假期集合
	public static List<String> weekDays = new ArrayList<>();
	// 补班集合
	public static List<String> needWordDays = new ArrayList<>();

	static CalendarService calendarService = new CalendarServiceImpl();
	
	static {
		Calendars = calendarService.selectAllCalendar();
		for (com.xunwei.som.calendar.Calendars Calendar : Calendars) {
			if (Calendar.getSign() == 1) {
				weekDays.add(ExcelUtils.fmt.format(Calendar.getCalendar()));
			} else {
				needWordDays.add(ExcelUtils.fmt.format(Calendar.getCalendar()));
			}

		}
	}

	/**
	 * 方法：判断输入的日期是否是周末或节假日，不是则肯定上班，日期格式为：yyyy-MM-dd
	 * 
	 * @return
	 * @throws ParseException
	 */
	/*
	 * public static boolean isWeekend(String bDate) throws ParseException {
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); Date bdate
	 * = format.parse(bDate); Calendar cal = Calendar.getInstance();
	 * cal.setTime(bdate); if (cal.get(Calendar.DAY_OF_WEEK) ==
	 * Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	 * return true; } else { return false; }
	 * 
	 * }
	 */
	public static boolean isWeekend(String bDate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate = format.parse(bDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(bdate);
		// 先判断是否是节假日
		if (weekDays.contains(bDate)) {
			return true;
		}
		// 在判断是否是补班
		if (needWordDays.contains(bDate)) {
			return false;
		}
		// 判断是否是假期
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 两个日期之前的相隔天数。 日期格式为：yyyy-MM-dd(注：不能带小时数，否则算出来的天数不对),例如：2018-2-7 到
	 * 2018-2-9。相隔日期为2天。
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static int dateSplit(Date start, Date end) throws Exception {
		// 如果是同一天，则间隔为0
		if (ExcelUtils.fmt.format(start).equals(ExcelUtils.fmt.format(end))) {
			return 0;
		}
		Long spi = end.getTime() - start.getTime();
		int step = (int) (spi / (24 * 60 * 60 * 1000));// 相隔天数
		return step;
	}

	/**
	 * 获取法定节假日集合：日期格式为：yyyy-MM-dd。当前年份为2018年。
	 * 
	 * @return
	 */
	public static List<String> getHolidays() {
		List<String> holidays = new ArrayList<String>();
		holidays.add("2018-01-01");// 元旦
		holidays.add("2018-02-15");// 春节
		holidays.add("2018-02-15");
		holidays.add("2018-02-16");
		holidays.add("2018-02-17");
		holidays.add("2018-02-18");
		holidays.add("2018-02-19");
		holidays.add("2018-02-20");
		holidays.add("2018-02-21");
		holidays.add("2018-04-05");// 清明节
		holidays.add("2018-04-06");
		holidays.add("2018-04-07");
		holidays.add("2018-04-29");// 劳动节
		holidays.add("2018-04-30");
		holidays.add("2018-05-01");
		holidays.add("2018-06-16");// 端午节
		holidays.add("2018-06-17");
		holidays.add("2018-06-18");
		holidays.add("2018-09-22");// 中秋节
		holidays.add("2018-09-23");
		holidays.add("2018-09-24");
		holidays.add("2018-10-01");// 国庆节
		holidays.add("2018-10-02");
		holidays.add("2018-10-03");
		holidays.add("2018-10-04");
		holidays.add("2018-10-05");
		holidays.add("2018-10-06");
		holidays.add("2018-10-07");
		return holidays;
	}

	/**
	 * 获取补班上班日集合：日期格式为：yyyy-MM-dd。当前年份为2018年。
	 * 
	 * @return
	 */
	public static List<String> getWorkDays() {
		List<String> workDays = new ArrayList<String>();
		workDays.add("2018-02-11");// 补班
		workDays.add("2018-02-24");// 补班
		workDays.add("2018-04-08");// 补班
		workDays.add("2018-04-28");// 补班
		workDays.add("2018-09-29");// 补班
		workDays.add("2018-09-30");// 补班
		return workDays;
	}

	/**
	 * 获得传入日期的下一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date dateTemp = cal.getTime();
		return dateTemp;
	}

	/**
	 * 获得传入日期的前一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date dateTemp = cal.getTime();
		dateTemp = cal.getTime();
		return dateTemp;
	}

	/**
	 * 方法：获取每天的工作时间（h/小时）
	 * 
	 * @param workTime(上班时间)：hh:ss:mm
	 * @param offWorkTime(下班时间)：hh:ss:mm
	 * @return
	 */
	public static Double getOneDayWorkTime(Date workTime, Date offWorkTime) {
		long number = offWorkTime.getTime() - workTime.getTime();
		long nm = 1000 * 60;
		// 计算差多少分钟
		long min = number / nm;
		Double worktime = (double) min;
		return worktime;
	}

	/**
	 * 方法：计算有效的停机时间。上下班时间依照客户输入的时间为准。时间精确到Min/分钟
	 * 
	 * @param startDate(报修时间)：日期格式yyyy-mm-dd
	 *            hh:ss:mm
	 * @param endDate(完成时间)：日期格式yyyy-mm-dd
	 *            hh:ss:mm
	 * @param workTime(客户上班时间)：日期格式
	 *            hh:ss:mm
	 * @param offWorkTime(客户下班时间)：日期格式
	 *            hh:ss:mm
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static Double getDownTime(Date startDate, Date endDate, Date
	 * workTime, Date offWorkTime) throws Exception { if
	 * (!workTime.before(offWorkTime)) { throw new Exception("下班时间应该在上班时间之后"); }
	 * // 开始时间的年月日 Date startYMD =
	 * ExcelUtils.fmt.parse(ExcelUtils.fmtOne.format(startDate).substring(0,
	 * 11)); // 结束时间的年月日 Date endYMD =
	 * ExcelUtils.fmt.parse(ExcelUtils.fmtOne.format(endDate).substring(0, 11));
	 * // 开始时间的时分秒 Date startHMS =
	 * ExcelUtils.fmtHms.parse(ExcelUtils.fmtOne.format(startDate).substring(11)
	 * ); // 结束时间的时分秒 Date endHMS =
	 * ExcelUtils.fmtHms.parse(ExcelUtils.fmtOne.format(endDate).substring(11));
	 * // 先算出每天正常的工作时间 Double oneDayDownTime = getOneDayWorkTime(workTime,
	 * offWorkTime); // 日历集合 List<Calendars> Calendars =
	 * calendarService.selectAllCalendar();
	 * 
	 *//**
		 * 先判断开始时间和结束时间是否为同一天
		 */
	/*
	 * // A:如果是同一天 if
	 * (ExcelUtils.fmt.format(startDate).equals(ExcelUtils.fmt.format(endDate)))
	 * { // 先判断当天是否为节假日 for (Calendars calendar : Calendars) { if
	 * (ExcelUtils.fmt.format(startDate).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 1) { return 0.0; } } //
	 * 再判断当天是否是周末。 if (isWeekend(ExcelUtils.fmt.format(startDate))) { //
	 * 如果是周末，在判断当天是否为补班，如果两者都不是，则直接返回0.0 for (Calendars calendar : Calendars) {
	 * if
	 * (!ExcelUtils.fmt.format(startDate).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar()))) { return 0.0; } } } // 既不是节假日，也不是周末。则说明在工作区间内，则分三种情况
	 *//**
		 * 1：如果开始时间在下班时间之后或下班时间当时，则当天有效工作时间肯定为0.0
		 */
	/*
	 * if (startHMS.after(offWorkTime)) { return 0.0; }
	 *//**
		 * 2：如果开始时间在上班时间之前或上班当时，在判断结束时间。
		 */
	/*
	 * if (startHMS.before(workTime)) { // 如果结束时间在下班时间之后 if
	 * (endHMS.after(offWorkTime)) { return oneDayDownTime; } // 如果结束时间在上班时间之前
	 * if (endHMS.before(workTime)) { return 0.0; } // 其他情况则说明结束时间落在工作区间 return
	 * getOneDayWorkTime(workTime, endHMS); }
	 *//**
		 * 3：其他情况则开始时间一定在有效工作时限内，在判断结束时间
		 */
	/*
	 * // 如果结束时间在下班时间之后 if (endHMS.after(offWorkTime)) { return
	 * getOneDayWorkTime(startHMS, offWorkTime); } return
	 * getOneDayWorkTime(startHMS, endHMS); }
	 * 
	 *//**
		 * 开始时间和结束时间不是同一天
		 */
	/*
	 * // 用于实际计算的开始时间，默认与传递过来的参数一致。 Date reallyStartTime = startDate; //
	 * 用于实际计算的结束时间 Date reallyEndTime = endDate; Calendar time =
	 * Calendar.getInstance(); boolean result = true; // 先判断两者是否是同一个节假日中 boolean
	 * s1 = false; boolean e1 = false; // 两个日期间隔的天数 int step1 =
	 * dateSplit(ExcelUtils.fmt.parse(ExcelUtils.fmt.format(startDate)),
	 * ExcelUtils.fmt.parse(ExcelUtils.fmt.format(endDate))); Date date2 =
	 * startDate; // 判断开始时间和结束时间是否在同一个节假日内 for (Calendars calendar : Calendars)
	 * { if
	 * (ExcelUtils.fmt.format(startDate).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 1) { s1 = true; } if
	 * (ExcelUtils.fmt.format(endDate).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 1) { e1 = true; } //
	 * 如果同为节假日，在看他们中间间隔的天数，是否每天都是节假日，若是，则证明两者处于同一个节假期内，返回0.0 if (s1 && e1) { if
	 * (step1 == 1) { return 0.0; } int j = 1; for (int i = 1; i < step1; i++) {
	 * time.setTime(date2); time.add(Calendar.DAY_OF_MONTH, 1); date2 =
	 * time.getTime(); // 判断是否是节假日 for (Calendars calendar1 : Calendars) { //
	 * 如果是节假日，则说明+1天后不属于工作日，停机时间不增加。 if
	 * (ExcelUtils.fmt.format(date2).equals(ExcelUtils.fmt.format(calendar1.
	 * getCalendar())) && calendar.getSign() == 1) { j++; } } if (j == step1) {
	 * return 0.0; } } } }
	 * 
	 * // 判断开始时间和结束时间是否在同一个周末内 s1 = false; e1 = false; // 两个日期间隔的天数 step1 =
	 * dateSplit(ExcelUtils.fmt.parse(ExcelUtils.fmt.format(startDate)),
	 * ExcelUtils.fmt.parse(ExcelUtils.fmt.format(endDate)));
	 * 
	 * // 判断开始时间是否是周末，且不是补班 if (isWeekend(ExcelUtils.fmt.format(startDate))) {
	 * startHMS = ExcelUtils.fmtHms.parse("08:30:30"); // 如果是周末，则在判断当天是否为补班。 for
	 * (Calendars calendar : Calendars) { // 如果是补班，则时间不用变，直接返回 if
	 * (ExcelUtils.fmt.format(startDate).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 0) { s1 = true; break; } } } //
	 * 判断结束时间是否是周末，且不是补班 if (isWeekend(ExcelUtils.fmt.format(endDate))) { //
	 * 如果是周末，则在判断当天是否为补班。 for (Calendars calendar : Calendars) { //
	 * 如果是补班，则时间不用变，直接返回 if
	 * (ExcelUtils.fmt.format(endDate).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 0) { e1 = true; break; } } }
	 * 
	 * if (s1 == false && e1 == false && step1 == 1) { return 0.0; }
	 * 
	 *//**
		 * 找出用于计算的真正的开始时间 判断开始时间是否是节假日。
		 */
	/*
	 * w1: while (result) { int i = 0; time.setTime(reallyStartTime); f1: for
	 * (Calendars calendar : Calendars) { // 如果是节假日，则天数增加1天，在判断一次是否为节假日。 if
	 * (ExcelUtils.fmt.format(reallyStartTime).equals(ExcelUtils.fmt.format(
	 * calendar.getCalendar())) && calendar.getSign() == 1) { startHMS =
	 * ExcelUtils.fmtHms.parse("08:30:30"); i++; break f1; } } if (i != 0) {
	 * time.add(Calendar.DAY_OF_MONTH, 1); reallyStartTime = time.getTime();
	 * continue; } if (i == 0) { break w1; } }
	 * 
	 * // 经过上一个循环，reallyStartTime的日期必定不会是节假日。再判断这个日期是否是周末。 while (result) { int
	 * i = 0; time.setTime(reallyStartTime); if
	 * (isWeekend(ExcelUtils.fmt.format(reallyStartTime))) { i++; //
	 * 如果是周末，则在判断当天是否为补班。 for (Calendars calendar : Calendars) { //
	 * 如果是补班，则时间不用变，直接返回 if
	 * (ExcelUtils.fmt.format(reallyStartTime).equals(ExcelUtils.fmt.format(
	 * calendar.getCalendar())) && calendar.getSign() == 0) { i++; break; } } }
	 * if (i == 1) { // i==1，则说明当天是周末，且当天不是补班，则天数在+1，进行下次循环While循环
	 * time.add(Calendar.DAY_OF_MONTH, 1); reallyStartTime = time.getTime();
	 * continue; } if (i == 2) { // i==2，则说明当天虽然是周末，但是补班，reallyStartTime日期不必改变
	 * break; } if (i == 0) { // i==0，则说明当天不是周末，也不是节假日，reallyStartTime日期不改变
	 * break; } }
	 * 
	 *//**
		 * 找出用于计算的真正的结束时间 判断结束时间是否是节假日。
		 */
	/*
	 * while (result) { int i = 0; time.setTime(reallyEndTime); for (Calendars
	 * calendar : Calendars) { // 如果是节假日，则天数增加1天，在判断一次是否为节假日。 if
	 * (ExcelUtils.fmt.format(reallyEndTime).equals(ExcelUtils.fmt.format(
	 * calendar.getCalendar())) && calendar.getSign() == 1) { startHMS =
	 * ExcelUtils.fmtHms.parse("08:30:30"); i++; break; } } if (i != 0) {
	 * time.add(Calendar.DAY_OF_MONTH, 1); reallyEndTime = time.getTime();
	 * continue; } if (i == 0) { break; } }
	 * 
	 * // 经过上一个循环，reallyStartTime的日期必定不会是节假日。再判断这个日期是否是周末。 while (result) { int
	 * i = 0; time.setTime(reallyEndTime); if
	 * (isWeekend(ExcelUtils.fmt.format(reallyEndTime))) { i++; //
	 * 如果是周末，则在判断当天是否为补班。 for (Calendars calendar : Calendars) { //
	 * 如果是补班，则时间不用变，直接返回 if
	 * (ExcelUtils.fmt.format(reallyEndTime).equals(ExcelUtils.fmt.format(
	 * calendar.getCalendar())) && calendar.getSign() == 0) { i++; break; } } }
	 * if (i == 1) { // i==1，则说明当天是周末，且当天不是补班，则天数在+1，进行下次循环While循环
	 * time.add(Calendar.DAY_OF_MONTH, 1); reallyEndTime = time.getTime();
	 * startHMS = ExcelUtils.fmtHms.parse("08:30:30"); continue; } if (i == 2) {
	 * // i==2，则说明当天虽然是周末，但是补班，reallyStartTime日期不必改变 break; } if (i == 0) { //
	 * i==0，则说明当天不是周末，也不是节假日，reallyStartTime日期不改变 break; } }
	 * 
	 * // 两个日期间隔的天数 int step =
	 * dateSplit(ExcelUtils.fmt.parse(ExcelUtils.fmt.format(reallyStartTime)),
	 * ExcelUtils.fmt.parse(ExcelUtils.fmt.format(reallyEndTime)));
	 * 
	 * System.out.println("真正开始时间：" + ExcelUtils.fmt.format(reallyStartTime));
	 * System.out.println("真正结束时间：" + ExcelUtils.fmt.format(reallyEndTime)); //
	 * 则证明换算后，两个日期还是处在同一天 if (step == 0) { if (startHMS.after(offWorkTime)) {
	 * return 0.0; }
	 *//**
		 * 2：如果开始时间在上班时间之前或上班当时，在判断结束时间。
		 */
	/*
	 * if (startHMS.before(workTime)) { // 如果结束时间在下班时间之后 if
	 * (endHMS.after(offWorkTime)) { return oneDayDownTime; } // 如果结束时间在上班时间之前
	 * if (endHMS.before(workTime)) { return 0.0; } // 其他情况则说明结束时间落在工作区间 return
	 * getOneDayWorkTime(workTime, endHMS); }
	 *//**
		 * 3：其他情况则开始时间一定在有效工作时限内，在判断结束时间
		 */
	/*
	 * // 如果结束时间在下班时间之后 if (endHMS.after(offWorkTime)) { return
	 * getOneDayWorkTime(startHMS, offWorkTime); } return
	 * getOneDayWorkTime(startHMS, endHMS); }
	 * 
	 * // 相隔一天 if (step == 1) {
	 *//**
		 * 1：如果开始时间在下班时间之后或下班时间当时，再看结束时间
		 */
	/*
	 * if (startHMS.after(offWorkTime)) { // 如果结束时间在下班时间之后 if
	 * (endHMS.after(offWorkTime)) { return oneDayDownTime; } // 如果结束时间在上班时间之前
	 * if (endHMS.before(workTime)) { return 0.0; } // 其他情况则说明结束时间落在工作区间 return
	 * getOneDayWorkTime(workTime, endHMS); }
	 *//**
		 * 2：如果开始时间在上班时间之前或上班当时，在判断结束时间。
		 */
	/*
	 * if (startHMS.before(workTime)) { // 如果结束时间在下班时间之后 if
	 * (endHMS.after(offWorkTime)) { return oneDayDownTime * 2; } //
	 * 如果结束时间在上班时间之前 if (endHMS.before(workTime)) { return oneDayDownTime; } //
	 * 其他情况则说明结束时间落在工作区间 return oneDayDownTime + getOneDayWorkTime(workTime,
	 * endHMS); }
	 *//**
		 * 3：其他情况则开始时间一定在有效工作时限内，在判断结束时间
		 */
	/*
	 * // 如果结束时间在下班时间之后 if (endHMS.after(offWorkTime)) { return oneDayDownTime +
	 * getOneDayWorkTime(startHMS, offWorkTime); } // 如果结束时间在上班时间之前 if
	 * (endHMS.before(workTime)) { return getOneDayWorkTime(startHMS,
	 * offWorkTime); } // 其他情况则说明结束时间落在工作区间 return getOneDayWorkTime(startHMS,
	 * offWorkTime) + getOneDayWorkTime(workTime, endHMS); }
	 * 
	 * Double stepTime = 0.0; // 其他情况则说明相差了不止一天，先判断中间隔的天数的总时间 Date date1 =
	 * reallyStartTime; for (int i = 1; i < step; i++) { int j = 0;
	 * time.setTime(date1); time.add(Calendar.DAY_OF_MONTH, 1); date1 =
	 * time.getTime(); // 判断是否是节假日 for (Calendars calendar : Calendars) { //
	 * 如果是节假日，则说明+1天后不属于工作日，停机时间不增加。 if
	 * (ExcelUtils.fmt.format(date1).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 1) { j++; break; } } if (j == 1)
	 * { continue; }
	 * 
	 * // 如果不是节假日，再判断是否是补班 for (Calendars calendar : Calendars) { //
	 * 如果是补班，则时间不用变，直接返回 if
	 * (ExcelUtils.fmt.format(date1).equals(ExcelUtils.fmt.format(calendar.
	 * getCalendar())) && calendar.getSign() == 0) { j++; break; } }
	 * 
	 * if (j == 1) { stepTime = stepTime + oneDayDownTime; continue; } //
	 * 如果既不是节假日，也不是补班，再判断是否是周末。 if (isWeekend(ExcelUtils.fmt.format(date1))) {
	 * continue; } // 其他情况，则说明肯定在上班期间，工作时间加一天。 stepTime = stepTime +
	 * oneDayDownTime; }
	 * 
	 *//**
		 * 1：判断了天数后，在判断详细的小时数。 如果开始时间在下班时间之后或下班时间当时，再看结束时间
		 */
	/*
	 * if (startHMS.after(offWorkTime)) { // 如果结束时间在下班时间之后 if
	 * (endHMS.after(offWorkTime)) { return oneDayDownTime + stepTime; } //
	 * 如果结束时间在上班时间之前 if (endHMS.before(workTime)) { return stepTime; } //
	 * 其他情况则说明结束时间落在工作区间 return getOneDayWorkTime(workTime, endHMS) + stepTime;
	 * }
	 *//**
		 * 2：如果开始时间在上班时间之前或上班当时，在判断结束时间。
		 */

	/*
	 * if (startHMS.before(workTime)) { // 如果结束时间在下班时间之后 if
	 * (endHMS.after(offWorkTime)) { return oneDayDownTime * 2 + stepTime; } //
	 * 如果结束时间在上班时间之前 if (endHMS.before(workTime)) { return oneDayDownTime +
	 * stepTime; } // 其他情况则说明结束时间落在工作区间 return oneDayDownTime +
	 * getOneDayWorkTime(workTime, endHMS) + stepTime; }
	 *//**
		 * 3：其他情况则开始时间一定在有效工作时限内，在判断结束时间
		 *//*
		 * // 如果结束时间在下班时间之后 if (endHMS.after(offWorkTime)) { return
		 * oneDayDownTime + getOneDayWorkTime(startHMS, offWorkTime) + stepTime;
		 * } // 如果结束时间在上班时间之前 if (endHMS.before(workTime)) { return
		 * getOneDayWorkTime(startHMS, offWorkTime) + stepTime; } //
		 * 其他情况则说明结束时间落在工作区间 return getOneDayWorkTime(startHMS, offWorkTime) +
		 * getOneDayWorkTime(workTime, endHMS) + stepTime;
		 * 
		 * }
		 */

	/**
	 * 计算两个日期间实际的工作时间
	 * 
	 * @param startDate
	 * @param endDate
	 * @param workTime
	 * @param offWorkTime
	 * @return
	 * @throws Exception
	 */
	public static Double getDownTime(Date startDate, Date endDate, Date workTime, Date offWorkTime) throws Exception {
		/*if (!workTime.before(offWorkTime)) {
			throw new Exception("下班时间应该在上班时间之后");
		}*/
		// 开始日期和结束日期的年月日
		Date sDate = ExcelUtils.fmt.parse(ExcelUtils.fmtOne.format(startDate).substring(0, 11));
		Date eDate = ExcelUtils.fmt.parse(ExcelUtils.fmtOne.format(endDate).substring(0, 11));
		// 如果开始日期和结束日期是同一天
		if (sDate.compareTo(eDate) == 0) {
			// 如果是假期或节假日
			if (weekDays.contains(ExcelUtils.fmt.format(sDate)) || isWeekend(ExcelUtils.fmt.format(sDate))) {
				return 0.0;
			}
			return getOneDayDownTime(startDate, endDate);
		}
		// 如果不是同一天，先判断中间相隔多少天
		int day = dateSplit(sDate, eDate);
		// 如果相隔一天
		if (day == 1) {
			return getTwoDayDownTime(startDate, endDate);
		} else {
			Date nextDay = sDate;
			double dayWorkTime = 0.0;
			for (int i = 1; i < day; i++) {
				nextDay = getNextDay(nextDay);
				// 如果中间当天上班，则时间加1天
				if (!isWeekend(ExcelUtils.fmt.format(nextDay))) {
					dayWorkTime = dayWorkTime + 480.0;
				}
			}
			return getTwoDayDownTime(startDate, endDate) + dayWorkTime;
		}
	}

	/**
	 * 计算同一天内工作的有效时间,默认上班时间8:30:00，中午12:30:00午休。下午13:30:00上班，17:30:30下班。精确到分钟
	 * 
	 * @param 开始时间
	 * @param 结束时间
	 * @return
	 * @throws Exception
	 */
	public static Double getOneDayDownTime(Date startDate, Date endDate) throws Exception {
		Date sDate = ExcelUtils.fmtHms.parse(ExcelUtils.fmtOne.format(startDate).substring(11));
		Date eDate = ExcelUtils.fmtHms.parse(ExcelUtils.fmtOne.format(endDate).substring(11));
		Date morningSdate = ExcelUtils.fmtHms.parse("08:30:00");
		Date morningEdate = ExcelUtils.fmtHms.parse("12:30:00");
		Date afternoonSdate = ExcelUtils.fmtHms.parse("13:30:00");
		Date afternoonEdate = ExcelUtils.fmtHms.parse("17:30:00");
		// 1.如果开始时间和结束时间都在上午上班时间之前，则说明用时为0
		if (sDate.compareTo(morningSdate) <= 0 && eDate.compareTo(morningSdate) <= 0) {
			return 0.0;
		}
		// 2.如果开始时间和结束时间都在下午下班时间之后，则说明用时为0
		if (sDate.compareTo(afternoonEdate) >= 0 && eDate.compareTo(afternoonEdate) >= 0) {
			return 0.0;
		}
		// 3.如果开始时间和结束时间落在休息期间，则说明用时为0
		if (sDate.compareTo(morningEdate) >= 0 && eDate.compareTo(afternoonSdate) <= 0) {
			return 0.0;
		}
		// 4.如果开始时间在上班之前，结束时间在下班之后，则说明用时正好为1天。
		if (sDate.compareTo(morningSdate) <= 0 && eDate.compareTo(afternoonEdate) >= 0) {
			return 480.0;
		}
		// 5.如果开始时间在上班之前，且结束时间落在休息区间，则说明用时为半天。
		if (sDate.compareTo(morningSdate) <= 0 && eDate.compareTo(morningEdate) >= 0
				&& eDate.compareTo(afternoonSdate) <= 0) {
			return 240.0;
		}
		// 6.如果开始时间落在休息区间，且结束时间在下班时候，则说明用时为半天。
		if (sDate.compareTo(morningEdate) >= 0 && sDate.compareTo(afternoonSdate) <= 0
				&& eDate.compareTo(afternoonEdate) >= 0) {
			return 240.0;
		}
		// 7.如果开始时间在上午上班之前，结束时间在上午上班区间，则计算用时
		if (sDate.compareTo(morningSdate) <= 0 && eDate.compareTo(morningSdate) >= 0
				&& eDate.compareTo(morningEdate) <= 0) {
			return getOneDayWorkTime(morningSdate, eDate);
		}
		// 8.如果开始时间在上午上班之前，结束时间在下午上班区间，则计算用时，减去休息的1个小时
		if (sDate.compareTo(morningSdate) <= 0 && eDate.compareTo(afternoonSdate) >= 0
				&& eDate.compareTo(afternoonEdate) <= 0) {
			return getOneDayWorkTime(morningSdate, eDate) - 60;
		}
		// 9.如果开始时间在上午上班区间，且结束时间在上午上班区间，则计算用时
		if (sDate.compareTo(morningSdate) >= 0 && sDate.compareTo(morningEdate) <= 0
				&& eDate.compareTo(morningSdate) >= 0 && eDate.compareTo(morningEdate) <= 0) {
			return getOneDayWorkTime(sDate, eDate);
		}
		// 10.如果开始时间在上午上班区间，结束时间落在休息区间，则计算用时
		if (sDate.compareTo(morningSdate) >= 0 && sDate.compareTo(morningEdate) <= 0
				&& eDate.compareTo(morningEdate) >= 0 && eDate.compareTo(afternoonSdate) <= 0) {
			return getOneDayWorkTime(sDate, morningEdate);
		}
		// 11.如果开始时间在上午上班区间，结束时间落在下午上班区间，则计算用时
		if (sDate.compareTo(morningSdate) >= 0 && sDate.compareTo(morningEdate) <= 0
				&& eDate.compareTo(afternoonSdate) >= 0 && eDate.compareTo(afternoonEdate) <= 0) {
			return getOneDayWorkTime(sDate, eDate) - 60;
		}
		// 12.如果开始时间在上午上班区间，结束时间在下班时间之后，则计算用时
		if (sDate.compareTo(morningSdate) >= 0 && sDate.compareTo(morningEdate) <= 0
				&& eDate.compareTo(afternoonEdate) >= 0) {
			return getOneDayWorkTime(sDate, morningEdate) + 240;
		}
		// 13.如果开始时间落在休息区间，结束时间在下午上班区间，则计算用时
		if (sDate.compareTo(morningEdate) >= 0 && sDate.compareTo(afternoonSdate) <= 0
				&& eDate.compareTo(afternoonSdate) >= 0 && eDate.compareTo(afternoonEdate) <= 0) {
			return getOneDayWorkTime(afternoonSdate, eDate);
		}
		// 13.如果开始时间落在下午上班区间，结束时间也落在下午上班时间，则计算用时
		if (sDate.compareTo(afternoonSdate) >= 0 && sDate.compareTo(afternoonEdate) <= 0
				&& eDate.compareTo(afternoonSdate) >= 0 && eDate.compareTo(afternoonEdate) <= 0) {
			return getOneDayWorkTime(sDate, eDate);
		}
		// 14.如果开始时间落在下午上班区间，结束时间也在下午下班之后，则计算用时
		if (sDate.compareTo(afternoonSdate) >= 0 && sDate.compareTo(afternoonEdate) <= 0
				&& eDate.compareTo(afternoonEdate) >= 0) {
			return getOneDayWorkTime(sDate, afternoonEdate);
		}
		return -1.0;
	}

	/**
	 * 计算相邻两天内的有效时间,考虑节假日、周末和补班，默认上班时间8:30:00，中午12:30:00午休。下午13:30:00上班，17:30:30下班。精确到分钟
	 * 
	 * @param 开始时间
	 * @param 结束时间
	 * @return
	 * @throws Exception
	 */
	public static Double getTwoDayDownTime(Date startDate, Date endDate) throws Exception {
		// 开始日期和结束日期的年月日
		Date sDate = ExcelUtils.fmt.parse(ExcelUtils.fmtOne.format(startDate).substring(0, 11));
		Date eDate = ExcelUtils.fmt.parse(ExcelUtils.fmtOne.format(endDate).substring(0, 11));
		Date sDate1 = ExcelUtils.fmtHms.parse(ExcelUtils.fmtOne.format(startDate).substring(11));
		Date eDate1 = ExcelUtils.fmtHms.parse(ExcelUtils.fmtOne.format(endDate).substring(11));
		Date morningSdate = ExcelUtils.fmtHms.parse("08:30:00");
		Date morningEdate = ExcelUtils.fmtHms.parse("12:30:00");
		Date afternoonSdate = ExcelUtils.fmtHms.parse("13:30:00");
		Date afternoonEdate = ExcelUtils.fmtHms.parse("17:30:00");
		// 如果相邻两天都是假期或者节假日，则用时为0
		if (isWeekend(ExcelUtils.fmt.format(sDate)) && isWeekend(ExcelUtils.fmt.format(eDate))) {
			return 0.0;
		}
		// 如果前一天为假期或节假日
		if (isWeekend(ExcelUtils.fmt.format(sDate)) && !isWeekend(ExcelUtils.fmt.format(eDate))) {
			if (eDate1.compareTo(morningSdate) <= 0) {
				return 0.0;
			}
			// 如果时间落在上午区间内
			if (eDate1.compareTo(morningSdate) > 0 && eDate1.compareTo(morningEdate) < 0) {
				return getOneDayDownTime(ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(endDate) + " 08:30:00"),
						endDate);
			}
			// 如果时间落在休息区间内
			if (eDate1.compareTo(morningEdate) >= 0 && eDate1.compareTo(afternoonSdate) <= 0) {
				return 240.0;
			}
			// 如果时间落在下午区间内
			if (eDate1.compareTo(afternoonSdate) > 0 && eDate1.compareTo(afternoonEdate) < 0) {
				return getOneDayDownTime(ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(endDate) + " 08:30:00"),
						endDate);
			}
			// 如果时间落在下午下班后
			if (eDate1.compareTo(afternoonEdate) >= 0) {
				return 480.0;
			}
		}
		// 如果后一天为假期或节假日
		if (!isWeekend(ExcelUtils.fmt.format(sDate)) && isWeekend(ExcelUtils.fmt.format(eDate))) {
			if (sDate1.compareTo(morningSdate) <= 0) {
				return 480.0;
			}
			// 如果时间落在上午区间内
			if (sDate1.compareTo(morningSdate) > 0 && sDate1.compareTo(morningEdate) < 0) {
				return getOneDayDownTime(sDate1, ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(sDate1) + " 17:30:00"));
			}
			// 如果时间落在休息区间内
			if (sDate1.compareTo(morningEdate) >= 0 && sDate1.compareTo(afternoonSdate) <= 0) {
				return 240.0;
			}
			// 如果时间落在下午区间内
			if (sDate1.compareTo(afternoonSdate) > 0 && sDate1.compareTo(afternoonEdate) < 0) {
				return getOneDayDownTime(sDate1, ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(sDate1) + " 17:30:00"));
			}
			// 如果时间落在下午下班后
			if (sDate1.compareTo(afternoonEdate) >= 0) {
				return 0.0;
			}
		}
		// 如果两天都上班
		if (!isWeekend(ExcelUtils.fmt.format(sDate)) && !isWeekend(ExcelUtils.fmt.format(eDate))) {
			double time = 0.0;
			if (eDate1.compareTo(morningSdate) <= 0) {
				time = 0.0;
			} else
			// 如果时间落在上午区间内
			if (eDate1.compareTo(morningSdate) > 0 && eDate1.compareTo(morningEdate) < 0) {
				time = getOneDayDownTime(ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(endDate) + " 08:30:00"),
						endDate);
			} else
			// 如果时间落在休息区间内
			if (eDate1.compareTo(morningEdate) >= 0 && eDate1.compareTo(afternoonSdate) <= 0) {
				time = 240.0;
			} else
			// 如果时间落在下午区间内
			if (eDate1.compareTo(afternoonSdate) > 0 && eDate1.compareTo(afternoonEdate) < 0) {
				time = getOneDayDownTime(ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(endDate) + " 08:30:00"),
						endDate);
			} else
			// 如果时间落在下午下班后
			if (eDate1.compareTo(afternoonEdate) >= 0) {
				time = 480.0;
			}
			if (sDate1.compareTo(morningSdate) <= 0) {
				time = time + 480.0;
			} else
			// 如果时间落在上午区间内
			if (sDate1.compareTo(morningSdate) > 0 && sDate1.compareTo(morningEdate) < 0) {
				time = time + getOneDayDownTime(sDate1,
						ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(sDate1) + " 17:30:00"));
			} else
			// 如果时间落在休息区间内
			if (sDate1.compareTo(morningEdate) >= 0 && sDate1.compareTo(afternoonSdate) <= 0) {
				time = time + 240.0;
			} else
			// 如果时间落在下午区间内
			if (sDate1.compareTo(afternoonSdate) > 0 && sDate1.compareTo(afternoonEdate) < 0) {
				time = time + getOneDayDownTime(sDate1,
						ExcelUtils.fmtOne.parse(ExcelUtils.fmt.format(sDate1) + " 17:30:00"));
			} else
			// 如果时间落在下午下班后
			if (sDate1.compareTo(afternoonEdate) >= 0) {
				time = time + 0.0;
			}
			return time;
		}
		return -1.0;
	}


}
