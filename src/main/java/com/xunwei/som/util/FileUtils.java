package com.xunwei.som.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.xunwei.som.base.model.ValueResult;

public class FileUtils {
	// 只日期格式
	public static final String DATE = "yyyy-MM-dd";
	// 只时间格式
	public static final String TIME = "HH:mm:ss";
	// 日期和时间格式
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	// 每个sheet显示的数据行数
	private static final int RECORDCOUNT = 60000;
    /**
     * 将时间转化为字符串
     * @param date
     * @param type
     * @return
     */
	public static String dateToString(Date date, int type) {
		if (SOMUtils.isNull(date)) return "";
		String format = "";
		switch (type) {
		case 1:
			format = "yyyy-MM-dd HH:mm:ss";
			break;
		case 2:
			format = "yyyy-MM-dd";
			break;
		case 3:
			format = "HH:mm:ss";
			break;
		case 4:
			format = "yyyy_MM_dd_HH_mm_ss";
			break;
		default:
			format = "yyyy-MM-dd HH:mm:ss";
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 按照传入的格式返回当前时间字符串
	 * @param format
	 * @return
	 */
	public static String getDateString (String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date());
	}
	/**
	 * 将传入的时间按照传入的格式格式化成字符串
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getDateString(String format, Date date) {
		if (SOMUtils.isNull(date)) return null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 截取显示错误信息的主要内容
	 */
	public static String getErrorMessage (Exception e) {
		String errorStr = "";
		String error = "";
		// 获取报错信息
		try {
			errorStr = e.getCause().getCause().getMessage();
		} catch (Exception e2) {
			// TODO: handle exception
			error = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage();
			return error;
		}
		// 截取主要报错信息
		int start = errorStr.indexOf("###");
		if (start > 0) {
			errorStr = errorStr.replaceFirst("###", "");
			int end = errorStr.indexOf("###");
			if (end > 0 && end > start) error = errorStr.substring(start, end).trim();
		}
		// 返回错误信息
		return error;
	}
	

	/**
	 * 编写错误页面信息
	 * @param str
	 * @param response
	 */
	public static void writeErrorPage(String str, HttpServletResponse response) {
		if (response == null)
			return;
		PrintWriter pw = null;
		try {
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("<script>alert('" + str + "');</script>");
			pw.write(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	
	
	/**
	 * 将字符串转为日期
	 * @param format			时间格式
	 * @param dateStr		时间字符串
	 * @return
	 */
	public static Date stringToDate (String format, String dateStr) {
		// 如果传入的时间文本为空则直接返回
		if (SOMUtils.isNull(dateStr)) return null;
		// 如果格式文本为空则设置默认格式
		if (SOMUtils.isNull(format)) format = DATETIME;
		// 设置时间格式
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			// 将时间文本转为日期
			Date date = simpleDateFormat.parse(dateStr);
			return date;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**
	 * 使用xml格式导出excel表格
	 * @param response
	 * @param fileName
	 * @param data
	 */
	public static void exportExcelByXml (HttpServletResponse response, String fileName, Class<?> clazz, List<?> list) {
		fileName += ".xls";
		String cntentType = "application/vnd.ms-excel";
		try {
			// 解决文件中文名乱码
			fileName = new String(fileName.getBytes("UTF-8"), "ISO_8859_1");
			// 告诉浏览器用什么软件可以打开此文件
			response.setHeader("Content-Type", cntentType);
			// 下载文件的默认名称
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			DataOutputStream rafs = new DataOutputStream(response.getOutputStream());
			// 创建表格数据xml
			StringBuffer data = createExcelXml(clazz, list, rafs);
			// 导出表格
			rafs.write(data.toString().getBytes()); 
			rafs.flush(); 
			rafs.close(); 
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 拼接创建表格xml
	 * @param clazz
	 * @param list
	 * @return
	 * @throws IOException 
	 */
	private static StringBuffer createExcelXml (Class<?> clazz, List<?> list, DataOutputStream rafs) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<?xml version=\"1.0\"?><?mso-application progid=\"Excel.Sheet\"?>"
        		+ "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" "
        		+ "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
        		+ "xmlns:x=\"urn:schemas-microsoft-com:office:excel\" "
        		+ "xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" "
        		+ "xmlns:html=\"http://www.w3.org/TR/REC-html40\">"
        		+ "<Styles><Style ss:ID=\"Default\" ss:Name=\"Normal\">"
        		+ "<Alignment ss:Vertical=\"Center\"/><Borders/>"
        		+ "<Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"12\"/>"
        		+ "<Interior/><NumberFormat/><Protection/></Style></Styles>");
		// 获取列数和表头
		Map<String, Object> baseMap = getColumnAndTitle(clazz);
		int column = (int) baseMap.get("column");
		List<String> titleList = (List<String>) baseMap.get("titleList");
		// 标记当前循环了多少行数据
		int count = 0;
		// 数据总数
		int total = list.size();
		// sheet个数
		int sheetNo = total % RECORDCOUNT == 0 ? total / RECORDCOUNT : total / RECORDCOUNT + 1;
		if (total == 0) sheetNo = 1;
		for (int i = 0; i < sheetNo; i ++) {
			int index = 0;
			stringBuffer.append("<Worksheet ss:Name=\"sheet"+i+"\">");
			stringBuffer.append("<Table ss:ExpandedColumnCount=\"" + column
	                + "\" ss:ExpandedRowCount=\"" + RECORDCOUNT + 1
	                + "\" x:FullColumns=\"1\" x:FullRows=\"1\">");
			// 添加表头
			stringBuffer.append("<Row>");
			for (int t = 0; t < column; t ++) {
				stringBuffer.append("<Cell><Data ss:Type=\"String\">" + titleList.get(t) + "</Data></Cell>");
			}
			stringBuffer.append("</Row>");
			for (int j = count; j < total; j ++) {
				stringBuffer.append("<Row>");
				// 保存表格数据
				List<Object> dataList = new ArrayList<Object>();
				getProperty(list.get(j), clazz, dataList);
				// 此时循环遍历表格数据
				for (int d = 0; d < column; d ++) {
					stringBuffer.append("<Cell><Data ss:Type=\"String\">" + dataList.get(d) +"</Data></Cell>");
				}
				stringBuffer.append("</Row>");
				index ++;
				count ++;
				if (index > RECORDCOUNT || index == RECORDCOUNT) break;
				rafs.write(stringBuffer.toString().getBytes());
				rafs.flush();
				stringBuffer.setLength(0);
			}
			stringBuffer.append("</Table><WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">"
					+ "<ProtectObjects>False</ProtectObjects><ProtectScenarios>False</ProtectScenarios>"
					+ "</WorksheetOptions></Worksheet>");
			// 每一个工作簿输出一次，释放资源，防止内存溢出
			rafs.write(stringBuffer.toString().getBytes());
			rafs.flush();
			stringBuffer.setLength(0);
		}
		stringBuffer.append("</Workbook>");
		return stringBuffer;
	}
	/**
	 * 获取实体类中包含excel注解的个数，作为表格列数，同时获取每列的表头
	 * @param clazz
	 * @return
	 */
	private static Map<String, Object> getColumnAndTitle (Class<?> clazz) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 保存列数
		int column = 0;
		// 保存表头数据
		List<String> list = new ArrayList<String>();
		Field [] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Excel.class)) {
				// 获取当前属性的注解值
				Annotation anno = field.getDeclaredAnnotation(Excel.class);
				// 转为Excel类
				Excel excel = (Excel) anno;
				// 列数+1
				column ++;
				// 保存表头
				list.add(excel.name());
			}
		}
		map.put("column", column);
		map.put("titleList", list);
		return map;
	}
	/**
	 * 获取传入的对象包含注解属性的值
	 * @param obj
	 * @param clazz
	 * @param dataList
	 */
	private static void getProperty (Object obj, Class<?> clazz, List<Object> dataList) {
		// 获取实体类中所有的属性
		Field [] fields = clazz.getDeclaredFields();
		// 遍历属性
		for (Field field : fields) {
			// 获取带有导出注解的属性
			if (field.isAnnotationPresent(Excel.class)) {
				// 创建一个字符数组，保存列名和值
				// 获取当前属性的注解值
				Annotation anno = field.getDeclaredAnnotation(Excel.class);
				// 转为Excel类
				Excel excel = (Excel) anno;
				// 获取当前属性
				String property = field.getName();
				// 拼接获取值方法
				String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
				try {
					// 调用方法获取值
					Method method = clazz.getDeclaredMethod(methodName);
					// 获取当前值
					Object data = method.invoke(obj);
					// 如果此时是日期格式
					String format = excel.exportFormat();
					// 如果此时需要值的替换
					String [] replace = excel.replace();
					if (! SOMUtils.isNull(format)) {
						// 说明此时是日期格式，按照日期格式导出
						if (! SOMUtils.isNull(data)) {
							dataList.add(DateUtils.getDate(new SimpleDateFormat(format), (Date) data));
						} else {
							dataList.add("");
						}
					} else if (replace.length > 0) {
						// 此时需要值的替换
						dataList.add(isReplace(replace, data + ""));
					} else {
						dataList.add(SOMUtils.isNull(data) ? "" : data);
					}
				} catch (NoSuchMethodException e) {
					// TODO: handle exception
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 将id替换成指定的值
	 * @param replace
	 * @param id
	 * @return
	 */
	private static String isReplace (String [] replace, String id) {
		String result = id;
		for (int i = 0; i < replace.length; i ++) {
			String str = replace[i];
			// 拆解配置
			String [] strs = str.split("_");
			if (id.equals(strs[1])) {
				result = strs[0];
			}
		}
		return result;
	}
	/**
	 * 将返回给erp的报文中的同步成功的出库单保存封装成字符串
	 * @param list
	 * @return
	 */
	public static String resultToString (List<ValueResult<String>> list) {
		StringBuffer result = new StringBuffer();
		for (ValueResult<String> valueResult : list) {
			if (valueResult.getCode() == 0) {
				result.append(valueResult.getResult());
			}
			result.append(",");
		}
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}
}
