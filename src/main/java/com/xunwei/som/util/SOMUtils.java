package com.xunwei.som.util;

import org.apache.shiro.SecurityUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.xunwei.som.calendar.CalendarTool;
import com.xunwei.som.pojo.CompInfo;
import com.xunwei.som.pojo.EngineerKpi;
import com.xunwei.som.pojo.Maintenance;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.front.Picture;
import com.xunwei.som.pojo.permissions.ParameterSetting;
import com.xunwei.som.service.CompInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CompInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;

import org.apache.shiro.codec.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class SOMUtils extends LoggerUtils {
	public static Map<String, String> urlConfigMap;
	public static Map<String, Map<String, String>> elementConfigMap = new HashMap<>();
	/*public static String ipAndPort = "10.52.26.3/som/qrcode/";*/ // ip端口地址
	public static String ipAndPort = "47.106.110.201/som/qrcode/";
	/*public static String qrAddr = "E:/som/src/main/webapp/qrcode/";*/ // 二维码文件地址
	public static String qrAddr = "/root/tomcat/apache-tomcat-8.0.53/webapps/som/qrcode/";
	public static String qrAddr1 = "/som/webapp/qrcode/"; // 二维码文件地址
	/*public static String pictureAddr = "E:/som/src/main/webapp/woPicture/";
	public static String pictureAddr1 = "10.52.26.3/som/woPicture/";*/
	public static String pictureAddr = "/root/tomcat/apache-tomcat-8.0.53/webapps/som/woPicture/"; // 工单图片地址
	public static String pictureAddr1 = "47.106.110.201/som/woPicture/";
	protected static String filePath = "/som/src/main/resources/prpo.properties";
	protected static Properties props;
	public static String role = ""; // 返回给前端的角色
	public static List<CompInfo> compInfos = new ArrayList<>();

	// 根据key读取value
	public static String getValue(String key) {
		return props.getProperty(key);
	}

	// 写入properties信息
	public static void setValue(String key, String value) {
		props.setProperty(key, value);
	}

	/**
	 * MD5 加密
	 *
	 * @param plainText
	 *            需要加密的内容
	 * @return
	 */
	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * 判断是否为NULL
	 *
	 * @param obj
	 *            判断的Object
	 * @return add by lzzzg on 2016-5-25 14:41:04.
	 */
	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		if ((obj instanceof Number))
			return isNaN((Number) obj);
		return "".equals(obj) || ("null".equals(obj)) || (obj == null);
	}

	/**
	 * 判断判断是否为NaN
	 *
	 * @param n
	 *            Number类型
	 * @return add by lzzzg on 2016-5-25 14:41:19.
	 */
	public static boolean isNaN(Number n) {
		if (n == null)
			return true;
		Class<? extends Number> type = n.getClass();
		if ((Integer.class == type) || (Integer.TYPE == type))
			return n.equals(Integer.valueOf(-2147483648));
		if ((Long.class == type) || (Long.TYPE == type))
			return n.equals(Long.valueOf(-9223372036854775808L));
		if ((Float.class == type) || (Float.TYPE == type))
			return n.equals(Float.valueOf(1.4E-45F));
		if ((Short.class == type) || (Short.TYPE == type))
			return n.equals(Short.valueOf((short) -32768));
		if ((Byte.class == type) || (Byte.TYPE == type))
			return n.equals(Byte.valueOf((byte) -128));
		if (BigDecimal.class == type)
			return n.equals(BigDecimal.valueOf(4.9E-324D));
		if (BigInteger.class == type)
			return n.equals(BigInteger.valueOf(-9223372036854775808L));
		return Double.isNaN(n.doubleValue());
	}

	/**
	 * 获取配置文件
	 *
	 * @param config
	 *            配置文件路径+内容字符串
	 * @return
	 * @throws IOException
	 *             add by lzzzg 2016-5-30 20:18:03
	 */
	public static Properties getProperties(String config) throws IOException {
		InputStream inputStream = SOMUtils.class.getClassLoader().getResourceAsStream(config);
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException ioE) {
			ioE.printStackTrace();
		} finally {
			inputStream.close();
		}
		return properties;
	}

	/**
	 * json格式数据写入客户端
	 *
	 * @param response
	 * @param jsonStr
	 */
	public static void writeJSONDataToClient(HttpServletResponse response, String jsonStr) {
		PrintWriter out = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			out.write(jsonStr);
			out.close();
		} catch (Exception e) {
			out.close();
			OMS_Logger.error(outputError(e.toString()));
		}
	}

	/**
	 * 字符串消息格式写入客户端
	 *
	 * @param response
	 * @param msgStr
	 */
	public static void writeMsgDataToClient(HttpServletResponse response, String msgStr) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(msgStr);
			out.close();
		} catch (Exception e) {
			out.close();
			OMS_Logger.error(outputError(e.toString()));
		}
	}

	/**
	 * 写入成功消息
	 * 
	 * @param response
	 * @param sucessMsg
	 *            提示消息
	 * @param id
	 *            数据的id
	 */
	public static void printSucessMsg(HttpServletResponse response, String sucessMsg, String id) {

		PrintWriter out = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			HashMap<String, Object> map = new HashMap<>();
			ObjectMapper objectMapper = new ObjectMapper();
			map.put("success", true);
			map.put("tip", "成功提示");
			map.put("msg", sucessMsg);
			if (!SOMUtils.isNull(id)) {
				map.put("id", id);
			}
			String jsonStr = objectMapper.writeValueAsString(map);
			out.write(jsonStr);
			out.close();
		} catch (Exception e) {
			out.close();
			OMS_Logger.error(outputError(e.toString()));
		}
	}

	/**
	 * 输出失败信息
	 *
	 * @param response
	 * @param failureMsg
	 */
	public static void printFailureMsg(HttpServletResponse response, String failureMsg) {
		PrintWriter out = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			HashMap<String, Object> map = new HashMap<>();
			ObjectMapper objectMapper = new ObjectMapper();
			map.put("success", false);
			map.put("tip", "失败提示");
			map.put("msg", failureMsg);
			String jsonStr = objectMapper.writeValueAsString(map);
			out.write(jsonStr);
			out.close();
		} catch (Exception e) {
			out.close();
			OMS_Logger.error(outputError(e.toString()));
		}
	}

	public static ObjectMapper getDefaultObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// 设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
		// Include.Include.ALWAYS 默认
		// Include.NON_DEFAULT 属性为默认值不序列化
		// Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化
		// Include.NON_NULL 属性为NULL 不序列化
		mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
		// 设置将MAP转换为JSON时候只转换值不等于NULL的
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
		mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// 设置有属性不能映射成PO时不报错
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		// 枚举序列化把默认序列name(false)改为toString(true)
		mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, true);

		return mapper;
	}

	public static ObjectMapper getDefaultObjectMapper(String dateFormat) {
		ObjectMapper mapper = new ObjectMapper();
		// 设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
		// Include.Include.ALWAYS 默认
		// Include.NON_DEFAULT 属性为默认值不序列化
		// Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化
		// Include.NON_NULL 属性为NULL 不序列化
		mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
		// 设置将MAP转换为JSON时候只转换值不等于NULL的
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
		mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		mapper.setDateFormat(new SimpleDateFormat(dateFormat));
		// 设置有属性不能映射成PO时不报错
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 枚举序列化把默认序列name(false)改为toString(true)
		mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, true);
		return mapper;
	}

	/**
	 * 反序列化复杂类型 为泛型的Collection Type
	 * 
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	public static JavaType getCollectionType(ObjectMapper objectMapper, Class<?> collectionClass,
			Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * json格式数据转换为实体对象list
	 *
	 * @param jsonStr
	 *            实体json格式数据
	 * @param clazz
	 *            实体类
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertToList(String jsonStr, Class<T> clazz) {
		try {
			ObjectMapper objectMapper = getDefaultObjectMapper();
			List<T> list;
			if (!SOMUtils.isNull(jsonStr)) {
				if (!SOMUtils.isNull(clazz)) {
					JavaType javaType = getCollectionType(objectMapper, ArrayList.class, clazz);
					list = objectMapper.readValue(jsonStr, javaType); // 执行转换
				} else {
					list = objectMapper.readValue(jsonStr, List.class); // 执行转换
				}
				return list;
			} else {
				throw new JsonNullException("Json数据为空！");
			}
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * json格式数据转换为实体对象list
	 *
	 * @param jsonStr
	 *            实体json格式数据
	 * @param clazz
	 *            实体类
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertToList(String jsonStr, Class<T> clazz, String dateFormat) {
		try {
			ObjectMapper objectMapper = getDefaultObjectMapper(dateFormat);
			List<T> list;
			if (!SOMUtils.isNull(jsonStr)) {
				if (!SOMUtils.isNull(clazz)) {
					JavaType javaType = getCollectionType(objectMapper, ArrayList.class, clazz);
					list = objectMapper.readValue(jsonStr, javaType); // 执行转换
				} else {
					list = objectMapper.readValue(jsonStr, List.class); // 执行转换
				}
				return list;
			} else {
				throw new JsonNullException("Json数据为空！");
			}
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T toObject(ObjectMapper objectMapper, String jsonStr, Class<T> clazz) {
		try {
			if (!SOMUtils.isNull(jsonStr)) {
				T t = objectMapper.readValue(jsonStr, clazz);
				if (SOMUtils.isNull(t)) {
					t = (T) Class.forName(clazz.getName()).newInstance();
				}
				return t;
			} else {
				throw new JsonNullException("Json数据为空！");
			}
		} catch (JsonMappingException jsonMappingException) {
			OMS_Logger.error(outputError(jsonMappingException.toString()));
			try {
				return (T) Class.forName(clazz.getName()).newInstance();
			} catch (Exception e) {
				return null;
			}
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private static <T> T toObject(ObjectMapper objectMapper, String jsonStr, TypeReference typeReference) {
		try {
			if (!SOMUtils.isNull(jsonStr)) {
				T t = objectMapper.readValue(jsonStr, typeReference);
				return t;
			} else {
				throw new JsonNullException("Json数据为空！");
			}
		} catch (JsonMappingException jsonMappingException) {
			OMS_Logger.error(outputError(jsonMappingException.toString()));
			return null;
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * json格式数据转换为实体对象
	 * 
	 * @param jsonStr
	 *            实体json格式数据
	 * @param valueTypeRef
	 *            TypeReference
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T convertToObject(String jsonStr, TypeReference valueTypeRef) {
		ObjectMapper objectMapper = getDefaultObjectMapper();
		return toObject(objectMapper, jsonStr, valueTypeRef);
	}

	/**
	 * json格式数据转换为实体对象
	 * 
	 * @param jsonStr
	 *            实体json格式数据
	 * @param clazz
	 *            实体类
	 * @param <T>
	 * @return
	 */
	public static <T> T convertToObject(String jsonStr, Class<T> clazz) {
		ObjectMapper objectMapper = getDefaultObjectMapper();
		return toObject(objectMapper, jsonStr, clazz);
	}

	/**
	 * json格式数据转换为实体对象
	 * 
	 * @param jsonStr
	 *            实体json格式数据
	 * @param clazz
	 *            实体类
	 * @param <T>
	 * @return
	 */
	public static <T> T convertToObject(String jsonStr, Class<T> clazz, String dateFormat) {
		ObjectMapper objectMapper = getDefaultObjectMapper(dateFormat);
		return toObject(objectMapper, jsonStr, clazz);
	}

	/**
	 * 自定义异常
	 */
	@SuppressWarnings("serial")
	static class JsonNullException extends Exception {
		public JsonNullException(String msg) {
			super(msg);
		}
	}

	private static <T> String toJsonStr(ObjectMapper objectMapper, T t) {
		try {
			if (!SOMUtils.isNull(t)) {
				String jsonStr = objectMapper.writeValueAsString(t);
				jsonStr = jsonStr.replace("Null", "");
				jsonStr = jsonStr.replace("NaN", "");
				jsonStr = jsonStr.replace("Infinity", "");
				return jsonStr;
			} else {
				return null;
			}
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * 对象转为JSON格式
	 * 
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> String objectToJsonStr(T t) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return toJsonStr(objectMapper, t);
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * 对象转为JSON格式
	 * 
	 * @param t
	 * @param dateFormat
	 * @param <T>
	 * @return
	 */
	public static <T> String objectToJsonStr(T t, String dateFormat) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			if (!SOMUtils.isNull(dateFormat))
				objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
			return toJsonStr(objectMapper, t);
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * 对象转为JSON格式
	 * 
	 * @param t
	 *            对象
	 * @param enumsFeature
	 *            枚举序列化为调用toString还是name，如果为true则为toString，如果为false则为name
	 * @param <T>
	 * @return
	 */
	public static <T> String objectToJsonStr(T t, boolean enumsFeature) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// 枚举序列化把默认序列name(false)改为toString(true)
			objectMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, enumsFeature);
			if (!SOMUtils.isNull(t)) {
				String jsonStr = objectMapper.writeValueAsString(t);
				return jsonStr;
			} else {
				return null;
			}
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * 对象转为JSON格式
	 * 
	 * @param t
	 *            对象
	 * @param enumsFeature
	 *            枚举序列化为调用toString还是name，如果为true则为toString，如果为false则为name
	 * @param dateFormat
	 *            时间格式
	 * @param <T>
	 * @return
	 */
	public static <T> String objectToJsonStr(T t, boolean enumsFeature, String dateFormat) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// 枚举序列化把默认序列name(false)改为toString(true)
			objectMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, enumsFeature);
			if (!SOMUtils.isNull(dateFormat))
				objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
			if (!SOMUtils.isNull(t)) {
				String jsonStr = objectMapper.writeValueAsString(t);
				return jsonStr;
			} else {
				return null;
			}
		} catch (IOException ioe) {
			OMS_Logger.error(outputError(ioe.toString()));
			return null;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return null;
		}
	}

	/**
	 * 根据object对象数组输出json字符串
	 */
	public static String funcResultToJsonStr(Object[] objArr) {
		String jsonStr = "";
		if ((objArr != null) && (objArr.length > 0)) {
			jsonStr = objectToJsonStr(objArr);
		}
		return jsonStr;
	}

	/**
	 * 操作成功或失敗返回前端
	 * 
	 * @param response
	 * @param actionResult
	 */
	public static void actionResultToClient(HttpServletResponse response, Boolean isSuccess) {
		ResourceBundle bundle = ResourceBundle.getBundle("i18n/i18n", (Locale) SecurityUtils.getSubject().getSession()
				.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
		if (isSuccess) {
			SOMUtils.printSucessMsg(response, bundle.getString("zgzt.handleSuccess"), null);
		} else {
			SOMUtils.printFailureMsg(response, bundle.getString("zgzt.handleFail"));
		}
	}

	/**
	 * 多个id字符串转换为符合数据库in查询的字符串 234,567 TO '234','567'
	 * 
	 * @param ids
	 * @return
	 */
	public static String idsToIdsStr(String ids) {
		String idsStr = "";
		if (!SOMUtils.isNull(ids)) {
			String[] idsAr = ids.split(",");
			for (String id : idsAr) {
				idsStr += "'" + id + "'" + ",";
			}
			idsStr = idsStr.substring(0, idsStr.length() - 1);
		}
		return idsStr;
	}

	/**
	 * 多个str字符串以逗号隔开的输出为list 234,567 TO list
	 * 
	 * @param strs
	 * @return
	 */
	public static List<String> strsToList(String strs) {
		List<String> list = new ArrayList<>();
		if (!SOMUtils.isNull(strs)) {
			String[] strsAr = strs.split(",");
			for (String str : strsAr) {
				list.add(str);
			}
		}
		return list;
	}

	/**
	 * 输出登陆失效的json格式数据
	 * 
	 * @param msg
	 * @param returnUrl
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getLogoutMsgJsonData(String msg, String returnUrl) {
		try {
			Map map = new HashMap();
			ObjectMapper objectMapper = new ObjectMapper();
			map.put("success", "false");
			map.put("tip", "失败提示");
			map.put("logout", true);
			map.put("msg", SOMUtils.isNull(msg) ? "User has logged off, please re login!" : msg);
			map.put("url", returnUrl);
			String jsonStr = objectMapper.writeValueAsString(map);
			return jsonStr;
		} catch (Exception e) {
			OMS_Logger.error(outputError(e.toString()));
			return "";
		}
	}

	/**
	 * SQL查询转义
	 * 
	 * @param sqlValue
	 */
	public static String escape(String sqlValue) {
		if (!isNull(sqlValue)) {
			if (sqlValue.indexOf("'") > -1 || sqlValue.indexOf("\"") > -1) {
				return sqlValue.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
			} else {
				return sqlValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 以逗号(,)分开的字符串组装成符合sql查询条件中IN 或者NOT IN 的值 如 aa,bb 组合成 'aa','bb'
	 * 
	 * @param str
	 * @return
	 */
	public static String strToSqlStr(String str) {
		String returnStr = null;
		if (!isNull(str)) {
			if (str.indexOf(",") > -1) {
				String[] strs = str.split(",");
				for (String s : strs) {
					returnStr += "'" + s + "',";
				}
				returnStr = returnStr.substring(0, returnStr.length() - 1);
			} else {
				returnStr = "'" + str + "'";
			}
		}
		return returnStr;
	}

	/**
	 * 以逗号(,)分开的字符串组成list
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List strToList(String str) {
		List list = new ArrayList<>();
		if (!isNull(str)) {
			if (str.indexOf(",") > -1) {
				String[] strs = str.split(",");
				for (String s : strs) {
					list.add(s);
				}
			} else {
				list.add(str);
			}
		}
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	public static void saveFile(MultipartFile file, String targetPath, String fileName) throws IOException {
		File targetFile = new File(targetPath, fileName);
		File dir = targetFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		file.transferTo(targetFile);
	}

	/**
	 * 文件拷贝
	 * 
	 * @param srcPath
	 *            源文件
	 * @param destPath
	 *            新文件路径
	 * @param newFileName
	 *            新文件名称
	 * @param isDel
	 *            是否删除原文件
	 * @return
	 */
	public static Map<String, String> copyFile(String srcPath, String destPath, String newFileName, boolean isDel) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			File sFile = new File(srcPath);
			String fileName = sFile.getName();
			if (fileName != null) {
				fileName = newFileName + fileName.substring(fileName.lastIndexOf("."), fileName.length());
			}
			resultMap.put("fileName", fileName);
			File mFile = new File(destPath);
			if (!mFile.exists()) {
				(new File(destPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			}
			// 打开输入流
			fis = new FileInputStream(srcPath);
			// 打开输出流
			fos = new FileOutputStream(destPath + "/" + fileName);
			// 读取和写入信息
			int len = 0;
			while ((len = fis.read()) != -1) {
				fos.write(len);
			}
			if (isDel && sFile.isFile()) {
				sFile.delete();
			}
			fos.close(); // 后开先关
			fis.close(); // 先开后关
			resultMap.put("status", "success");
			return resultMap;
		} catch (IOException e) {
			resultMap.put("status", "fail");
			return resultMap;
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeFile(String filePath) {
		File sFile = new File(filePath);
		if (sFile.isFile()) {
			sFile.delete();
		}
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
	}

	/**
	 * base64编码
	 * 
	 * @param string
	 * @return
	 */
	public static byte[] encode64(byte[] bytes) {
		return Base64.encode(bytes);
	}

	/**
	 * base64解码
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] decode64(String str) {
		return Base64.decode(str);
	}

	public static String loadFile(String fileName) {
		InputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			byte[] bs = new byte[fis.available()];
			fis.read(bs);
			String res = new String(bs, "utf8");
			fis.close();
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据分公司名称返回该分公司工单号的前缀
	 * 
	 * @param b
	 * @return
	 */
	public static String orderNumToComp(String compName) {
		for (CompInfo compInfo : SOMUtils.compInfos) {
			if (compName.equals(compInfo.getCompName())) {
				return compInfo.getAbbreviation();
			}
		}
		return "";
	}

	/**
	 * 根据工单号的前缀返回区域名称
	 * 
	 * @param b
	 * @return
	 */
	public static String CompToOrderNumTo(String orderNum) {
		for (CompInfo compInfo : SOMUtils.compInfos) {
			if (orderNum.equals(compInfo.getAbbreviation())) {
				return compInfo.getCompName();
			}
		}
		return "";
	}

	/**
	 * 判断输入对象里是否存在空值
	 * 
	 * @param objects
	 * @return
	 */
	public static String isNull(Map<String, Object> objects) {
		for (Map.Entry<String, Object> entry : objects.entrySet()) {
			if (entry.getValue() == null || "".equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return "";
	}

	@Test
	public void test() throws Exception {
		String startDate = "2019-03-30 10:32:35";
		String endDate = "2019-04-01 09:03:12";
		String workTime = "08:30:00";
		String offWorkTime = "17:30:00";
		ExcelUtils.fmtOne.parse(startDate);
		Double a = CalendarTool.getDownTime(ExcelUtils.fmtOne.parse(startDate), ExcelUtils.fmtOne.parse(endDate),
				ExcelUtils.fmtHms.parse(workTime), ExcelUtils.fmtHms.parse(offWorkTime));
		System.out.println(a);
	}

	/**
	 * 判断用户是否有相应权限
	 * 
	 * @param permissionList
	 * @param permission
	 * @return
	 */
	public static boolean havePermission(String[] permissionList, String permission) {
		for (String string : permissionList) {
			if (string.trim().equals(permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 中文转换为Encoding编码
	 * 
	 * @param content
	 * @return
	 */
	public static String ChineseToEncoding(String content) {
		char[] utfBytes = content.toCharArray();
		String unicodeBytes = "";
		for (int i = 0; i < utfBytes.length; i++) {
			String hexB = Integer.toHexString(utfBytes[i]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	/**
	 * encoding编码转换为中文
	 * 
	 * @param content
	 * @return
	 */
	public static String encodingToChinese(String content) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = content.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = content.substring(start + 2, content.length());
			} else {
				charStr = content.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	/**
	 * 方法：判断是哪一个季度
	 * 
	 * @param month
	 *            月份
	 * @return
	 */
	public static int isWhichQuarter(int month) {
		int quarter = 0;
		switch (month) {
		case 1:
			quarter = 1;
			break;
		case 2:
			quarter = 1;
			break;
		case 3:
			quarter = 1;
			break;
		case 4:
			quarter = 2;
			break;
		case 5:
			quarter = 2;
			break;
		case 6:
			quarter = 2;
			break;
		case 7:
			quarter = 3;
			break;
		case 8:
			quarter = 3;
			break;
		case 9:
			quarter = 3;
			break;
		case 10:
			quarter = 4;
			break;
		case 11:
			quarter = 4;
			break;
		case 12:
			quarter = 4;
			break;
		}
		return quarter;
	}

	/**
	 * 判断前端参数是否有空,如果有空的，则直接返回为空的参数名称
	 * 
	 * @param args
	 * @return
	 */
	public static String paramsIsNull(Map<String, Object> args) {
		for (Map.Entry<String, Object> entry : args.entrySet()) {
			String value = (String) entry.getValue();
			if (entry.getValue() == null || value.trim().equals("")) {
				return entry.getKey();
			}
		}
		return "";
	}

	/**
	 * 
	 * @param permissions
	 *            权限列表
	 * @param json
	 *            返回的MAP值
	 * @return
	 */
	public static Map<String, Object> permissions(String[] permissions, Map<String, Object> json) {
		String para = "";
		for (int i = 0; i < permissions.length; i++) {
			// 判断当前权限
			switch (permissions[i]) {
			case "1":
				para = "homepage";
				break;
			case "1-1":
				para = "workerOrder";
				break;
			case "1-2":
				para = "contract";
				break;
			case "1-3":
				para = "equipment";
				break;
			case "1-4":
				para = "atWork";
				break;
			case "1-5":
				para = "satisfaction";
				break;
			case "2":
				para = "basicMsg";
				break;
			case "2-1":
				para = "custmorMsg";
				break;
			case "2-2":
				para = "userManage";
				break;
			case "2-3":
				para = "calendar";
				break;
			case "3":
				para = "custManage";
				break;
			case "3-1":
				para = "contractMsg";
				break;
			case "3-2":
				para = "contractSatis";
				break;
			case "3-3":
				para = "feedback";
				break;
			case "4":
				para = "serveManage";
				break;
			case "4-1":
				para = "workMsg";
				break;
			case "4-2":
				para = "faultAnalysis";
				break;
			case "5":
				para = "equipAndProperty";
				break;
			case "5-1":
				para = "equipmentManage";
				break;
			case "5-2":
				para = "assets";
				break;
			case "6":
				para = "maintainManage";
				break;
			case "6-1":
				para = "plan";
				break;
			case "6-2":
				para = "implement";
				break;
			case "7":
				para = "reportManage";
				break;
			case "7-1":
				para = "kpiMang";
				break;
			case "7-1-1":
				para = "engineer";
				break;
			case "7-1-2":
				para = "customerMsgr";
				break;
			case "7-1-3":
				para = "branchOffice";
				break;
			case "7-1-4":
				para = "headquarters";
				break;
			case "7-1-5":
				para = "service";
				break;
			case "7-2":
				para = "orderAnalysis";
				break;
			case "7-3":
				para = "repair";
				break;
			case "7-4":
				para = "operationRate";
				break;
			case "7-5":
				para = "maintain";
				break;
			case "7-6":
				para = "equipmentReq";
				break;
			case "7-7":
				para = "contractForm";
				break;
			case "8":
				para = "onGuard";
				break;
			case "8-1":
				para = "engineerWork";
				break;
			case "11":
				para = "system";
				break;
			case "11-1":
				para = "area";
				break;
			case "11-2":
				para = "jurisdiction";
				break;
			case "11-2-1":
				para = "pc";
				break;
			case "11-2-2":
				para = "moble";
				break;
			case "11-2-3":
				para = "account";
				break;
			case "11-3":
				para = "amend";
				break;
			case "11-4":
				para = "parameter";
				break;
			case "11-5":
				para = "orderStat";
				break;
			case "11-6":
				para = "orderPrescription";
				break;
			}
			// 根据当前权限，修改true和false
			for (Map.Entry<String, Object> entry : json.entrySet()) {
				if (entry.getKey().equals(para)) {
					json.put(entry.getKey(), true);
					break;
				}
			}
		}
		return json;
	}

	public static Map<String, Object> permissionsList() {
		Map<String, Object> json = new HashMap<>();
		json.put("homepage", false);
		json.put("workerOrder", false);
		json.put("contract", false);
		json.put("equipment", false);
		json.put("atWork", false);
		json.put("satisfaction", false);
		json.put("basicMsg", false);
		json.put("custmorMsg", false);
		json.put("userManage", false);
		json.put("calendar", false);
		json.put("custManage", false);
		json.put("contractMsg", false);
		json.put("contractSatis", false);
		json.put("feedback", false);
		json.put("serveManage", false);
		json.put("workMsg", false);
		json.put("faultAnalysis", false);
		json.put("equipAndProperty", false);
		json.put("equipmentManage", false);
		json.put("assets", false);
		json.put("maintainManage", false);
		json.put("plan", false);
		json.put("implement", false);
		json.put("reportManage", false);
		json.put("kpiMang", false);
		json.put("engineer", false);
		json.put("customerMsgr", false);
		json.put("branchOffice", false);
		json.put("headquarters", false);
		json.put("service", false);
		json.put("orderAnalysis", false);
		json.put("repair", false);
		json.put("operationRate", false);
		json.put("maintain", false);
		json.put("equipmentReq", false);
		json.put("contractForm", false);
		json.put("onGuard", false);
		json.put("engineerWork", false);
		json.put("system", false);
		json.put("area", false);
		json.put("jurisdiction", false);
		json.put("pc", false);
		json.put("moble", false);
		json.put("account", false);
		json.put("amend", false);
		json.put("parameter", false);
		json.put("orderStat", false);
		json.put("orderPrescription", false);
		return json;
	}

	/**
	 * 根据参数，判断出显示的开始下标和结束下标
	 * 
	 * @param size
	 * @param page
	 * @param limit
	 * @return
	 */
	public static Integer[] pageAndLimit(HttpServletRequest request, List<?> list, Integer page, Integer limit) {
		Integer[] a = new Integer[2];
		if (request.getParameter("page") != null && request.getParameter("limit") != null) {
			if (list != null && list.size() > 0) {
				page = Integer.valueOf(request.getParameter("page"));
				limit = Integer.valueOf(request.getParameter("limit"));
				page = (page - 1) * limit; // 开始下标
				limit = page + limit; // 结束下标
				// 如果开始坐标都比条目数大，则
				if (page > list.size()) {
					a[0] = -1;
					a[1] = -1;
					return a;
				}
				// 如果结束坐标比条目数大，则结束下标就为结束下标。
				if (limit > list.size()) {
					limit = list.size();
				}
				a[0] = page;
				a[1] = limit;
				return a;
			}
		}
		a[0] = null;
		a[1] = null;
		return a;
	}

	/**
	 * 根据参数，判断出显示的开始下标和结束下标
	 * 
	 * @param size
	 * @param page
	 * @param limit
	 * @return
	 */
	public static Integer[] pageWithLimit(HttpServletRequest request, Integer page, Integer limit) {
		Integer[] a = new Integer[2];
		if (request.getParameter("page") != null && request.getParameter("limit") != null) {
			page = Integer.valueOf(request.getParameter("page"));
			limit = Integer.valueOf(request.getParameter("limit"));
			page = (page - 1) * limit; // 开始下标
			a[0] = page;
			a[1] = limit;
			return a;
		}
		a[0] = null;
		a[1] = null;
		return a;
	}

	public static String permissionToNumber(String para) {
		switch (para) {
		case "homepage":
			para = "1";
			break;
		case "workerOrder":
			para = "1-1";
			break;
		case "contract":
			para = "1-2";
			break;
		case "equipment":
			para = "1-3";
			break;
		case "atWork":
			para = "1-4";
			break;
		case "satisfaction":
			para = "1-5";
			break;
		case "basicMsg":
			para = "2";
			break;
		case "custmorMsg":
			para = "2-1";
			break;
		case "userManage":
			para = "2-2";
			break;
		case "calendar":
			para = "2-3";
			break;
		case "custManage":
			para = "3";
			break;
		case "contractMsg":
			para = "3-1";
			break;
		case "contractSatis":
			para = "3-2";
			break;
		case "feedback":
			para = "3-3";
			break;
		case "serveManage":
			para = "4";
			break;
		case "workMsg":
			para = "4-1";
			break;
		case "faultAnalysis":
			para = "4-2";
			break;
		case "equipAndProperty":
			para = "5";
			break;
		case "equipmentManage":
			para = "5-1";
			break;
		case "assets":
			para = "5-2";
			break;
		case "maintainManage":
			para = "6";
			break;
		case "plan":
			para = "6-1";
			break;
		case "implement":
			para = "6-2";
			break;
		case "reportManage":
			para = "7";
			break;
		case "kpiMang":
			para = "7-1";
			break;
		case "engineer":
			para = "7-1-1";
			break;
		case "customerMsgr":
			para = "7-1-2";
			break;
		case "branchOffice":
			para = "7-1-3";
			break;
		case "headquarters":
			para = "7-1-4";
			break;
		case "service":
			para = "7-1-5";
			break;
		case "orderAnalysis":
			para = "7-2";
			break;
		case "repair":
			para = "7-3";
			break;
		case "operationRate":
			para = "7-4";
			break;
		case "maintain":
			para = "7-5";
			break;
		case "equipmentReq":
			para = "7-6";
			break;
		case "contractForm":
			para = "7-7";
			break;
		case "onGuard":
			para = "8";
			break;
		case "engineerWork":
			para = "8-1";
			break;
		case "system":
			para = "11";
			break;
		case "area":
			para = "11-1";
			break;
		case "jurisdiction":
			para = "11-2";
			break;
		case "pc":
			para = "11-2-1";
			break;
		case "moble":
			para = "11-2-2";
			break;
		case "account":
			para = "11-2-3";
			break;
		case "amend":
			para = "11-3";
			break;
		case "parameter":
			para = "11-4";
			break;
		}
		return para;
	}

	public static HttpServletResponse setCookie(HttpServletResponse response, String name, String value, int time) {
		// new一个Cookie对象,键值对为参数
		Cookie cookie = new Cookie(name, value);
		// tomcat下多应用共享
		// 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码

		try {
			URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		cookie.setMaxAge(time);
		// 将Cookie添加到Response中,使之生效
		response.addCookie(cookie); // addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie
		return response;
	}

	/**
	 * 根据登陆账号获取公司名称或分公司名称
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param time
	 * @return
	 */
	public static Map<String, Object> getCompName(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<>();
		UserService userService = new UserServiceImpl();
		String username = request.getParameter("username");
		json.put("role", userService.selectByPrimaryKey(username).getRoleId());
		json.put("compname", userService.selectByUserId(username).getCustName());
		return json;
	}

	/**
	 * 角色转换为english
	 * 
	 * @param request
	 * @return
	 */
	public static String roleToEnglish(String userRole) {
		switch (userRole) {
		case "客户":
			return "shitcustomer";
		case "总部客服":
			return "badhead";
		case "技术主管":
			return "chinken";
		case "工程师":
			return "enginner";
		case "运维助理":
			return "assingest";
		case "驻现场人员":
			return "dogface";
		case "运维经理":
			return "anlahuakeba";
		case "客服经理":
			return "beefnoodle";
		case "运维总监":
			return "uglyman";
		case "优质运维专员":
			return "hqoamc";
		case "运维管理人员":
			return "oamm";
		default:
			return "";
		}
	}

	public static boolean arrayIsNull(String[] array) {
		int a = array.length;
		int b = 0;
		for (String string : array) {
			if (string == null || string.equals("")) {
				b++;
			}
		}
		if (a == b) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当月1号，零点，0分，0秒
	 * 
	 * @return
	 */
	public static Date initDateByMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 判断工单是否超时
	 * 
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public static boolean isOverTime(ServiceInfo service) throws Exception {
		int a = 0;
		// 对每个工程师的kpi指标赋值
		Date workTime = ExcelUtils.fmtHms.parse("08:30:00"); // 上班时间
		Date offWorkTime = ExcelUtils.fmtHms.parse("17:30:00"); // 下班时间
		EngineerKpi engineerKpi = new EngineerKpi();
		// 看派单时间是否达标
		if (service.getOrderInfo().getSendTime() == null) {
			engineerKpi.setSendTimeSlot(SOMUtils.getInt(CalendarTool.getDownTime(service.getOrderInfo().getRepairTime(),
					new Date(), workTime, offWorkTime)));
			if (engineerKpi.getSendTimeSlot() > ParameterSetting.sendTime) {
				a = 1;
			}
		} else if (service.getOrderInfo().getSendTime() != null) {
			engineerKpi.setSendTimeSlot(SOMUtils.getInt(CalendarTool.getDownTime(service.getOrderInfo().getRepairTime(),
					service.getOrderInfo().getSendTime(), workTime, offWorkTime)));
			if (engineerKpi.getSendTimeSlot() > ParameterSetting.sendTime) {
				a = 1;
			}
		}
		// 如果电话响应时间和到达现场时间都为空,则超不超时按最长到达现场时间计算
		if (a == 0) {
			if (service.getOrderInfo().getSendTime() != null && service.getTelRepon() == null
					&& service.getArrTime() == null) {
				engineerKpi.setArrTimeSlot((CalendarTool.getDownTime(service.getOrderInfo().getSendTime(), new Date(),
						workTime, offWorkTime)));
				if (engineerKpi.getArrTimeSlot() > ParameterSetting.arrTime) {
					a = 1;
				}
			}
		}
		// 如果转单时间不为空，看转单前是否超时
		if (a == 0) {
			if (service.getOrderInfo().getTurnOrderTime() != null && service.getOrderInfo().getSendTime() != null) {
				engineerKpi.setArrTimeSlot((CalendarTool.getDownTime(service.getOrderInfo().getSendTime(),
						service.getOrderInfo().getTurnOrderTime(), workTime, offWorkTime)));
				if (engineerKpi.getArrTimeSlot() > ParameterSetting.arrTime) {
					a = 1;
				}
			}
		}
		// 如果电话响应时间不为空，看响应时间是否达标
		/*
		 * if (a == 0) { if (service.getOrderInfo().getSendTime() != null &&
		 * service.getTelRepon() != null) { engineerKpi
		 * .setResponseTime(SOMUtils.getInt(CalendarTool.getDownTime(service.
		 * getOrderInfo().getSendTime(), service.getTelRepon(), workTime,
		 * offWorkTime))); if (engineerKpi.getResponseTime() >
		 * ParameterSetting.telRepon) { a = 1; } } }
		 */
		// 如果到达现场时间不为空，看到达现场时间是否达标
		if (a == 0) {
			if (service.getOrderInfo().getSendTime() != null && service.getArrTime() != null) {
				engineerKpi.setArrTimeSlot(CalendarTool.getDownTime(service.getOrderInfo().getSendTime(),
						service.getArrTime(), workTime, offWorkTime));
				if (engineerKpi.getArrTimeSlot() > ParameterSetting.arrTime) {
					a = 1;
				}
			}
		}
		// 看解决时间是否达标
		if (a == 0) {
			if (service.getOrderInfo().getSendTime() != null && service.getProbSolve() == null) {
				engineerKpi.setProbSolveSlot(CalendarTool.getDownTime(service.getOrderInfo().getSendTime(), new Date(),
						workTime, offWorkTime));
				if (engineerKpi.getProbSolveSlot() > ParameterSetting.probSolve) {
					a = 1;
				}
			} else if (service.getOrderInfo().getSendTime() != null && service.getProbSolve() != null) {
				engineerKpi.setProbSolveSlot(CalendarTool.getDownTime(service.getOrderInfo().getSendTime(),
						service.getProbSolve(), workTime, offWorkTime));
				if (engineerKpi.getProbSolveSlot() > ParameterSetting.probSolve) {
					a = 1;
				}
			}
		}
		if (a == 1) {
			return true;
		}
		return false;
	}

	public static String SendGET(String url, String param) {
		String result = "";// 访问返回结果
		BufferedReader read = null;// 读取访问结果
		try {
			// 创建url
			URL realurl = new URL(
					"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%2fobtainOpenId%3fmachCode%3dGZZ0090&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			// 打开连接
			URLConnection connection = realurl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段，获取到cookies等
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			read = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;// 循环读取
			while ((line = read.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (read != null) {// 关闭流
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * double类型四舍五入去除掉小数
	 */
	public static int getInt(double number) {
		String a = String.valueOf(Math.round(number));
		String b = "";
		for (char ch : a.toCharArray()) {
			if (ch == '.') {
				break;
			}
			b = b + ch;
		}
		int c = Integer.valueOf(b);
		return c;
	}

	/**
	 * double类型保留1位小数
	 */
	public static String getIntOne(double number) {
		String a = String.valueOf(number);
		String b = "";
		int c = 0;
		for (char ch : a.toCharArray()) {
			if (c == 1) {
				b = b + ch;
				return b;
			}
			if (ch == '.') {
				c = 1;
				b = b + ch;
				continue;
			}
			b = b + ch;
		}
		return b;
	}

	/**
	 * double类型保留1位小数
	 */
	public static Double getIntOneToDouble(double number) {
		String a = String.valueOf(number);
		String b = "";
		Double e = 0.0;
		int c = 0;
		for (char ch : a.toCharArray()) {
			if (c == 1) {
				b = b + ch;
				break;
			}
			if (ch == '.') {
				c = 1;
				b = b + ch;
				continue;
			}
			b = b + ch;
		}
		e = Double.valueOf(b);
		return e;
	}

	/**
	 * 根据路径删除文件及文件夹
	 * 
	 * @param dirPath
	 */
	public static void deleteDir(String dirPath) {
		File file = new File(dirPath);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();
			} else {
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
	}

	/**
	 * 根据工单号返回图片路径
	 * 
	 * @param dirPath
	 */
	public static Picture picture(String woNumber) {
		File a = new File(SOMUtils.pictureAddr + woNumber);
		Picture picture = new Picture();
		if (a.exists()) {
			String b = "";
			for (int i = 1; i <= 3; i++) {
				b = SOMUtils.pictureAddr + woNumber + "/" + woNumber + "-" + i + ".png";
				File c = new File(b);
				if (c.exists()) {
					if (i == 1) {
						picture.setPicture1(SOMUtils.pictureAddr1 + woNumber + "/" + woNumber + "-" + i + ".png");
					} else if (i == 2) {
						picture.setPicture2(SOMUtils.pictureAddr1 + woNumber + "/" + woNumber + "-" + i + ".png");
					} else if (i == 3) {
						picture.setPicture3(SOMUtils.pictureAddr1 + woNumber + "/" + woNumber + "-" + i + ".png");
					}
				} else {
					break;
				}
			}

		}
		return picture;
	}

	/**
	 * 根据工单号返回图片路径
	 * 
	 * @param dirPath
	 */
	public static Maintenance setMaintenance(Maintenance maintenance) {
		if (maintenance.getMaterialModel() == null || maintenance.getMaterialModel().equals("")) {
			maintenance.setMaterialModel("");
		} else {
			maintenance.setMaterialModel(
					"型号：" + maintenance.getMaterialModel() + "  " + "数量：" + maintenance.getMaterialNumber());
		}
		return maintenance;
	}

}
