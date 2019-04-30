package com.xunwei.som.util;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.shiro.SecurityUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import sun.misc.BASE64Encoder;

import org.apache.shiro.codec.Base64;





import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * 
 */
public  class SOMUtils extends LoggerUtils{
    public static Map<String,String> urlConfigMap;
    public static Map<String,Map<String,String>> elementConfigMap = new HashMap<>();
    protected static String filePath = "/config.properties";
    protected static Properties props;
    static {
        props = new Properties();
        try{
       // InputStream in = new BufferedInputStream(new FileInputStream(SOMUtils.class.getResourceAsStream(filePath)), "UTF-8");
        props.load(new InputStreamReader(new BOMInputStream(SOMUtils.class.getResourceAsStream(filePath)), "UTF-8"));
        }catch(Throwable e){
        	e.printStackTrace();
        }
    }
    //根据key读取value
    public static String getValue(String key) {
        return props.getProperty(key);
    }

    //写入properties信息
    public static void setValue(String key, String value) {
        props.setProperty(key, value);
    }
    
    /**
     * MD5 加密
     *
     * @param plainText 需要加密的内容
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
            //32位加密
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
     * @param obj 判断的Object
     * @return add by lzzzg on 2016-5-25 14:41:04.
     */
    public static boolean isNull(Object obj) {
        if (obj == null) return true;
        if ((obj instanceof Number)) return isNaN((Number) obj);
        return "".equals(obj) || ("null".equals(obj)) || (obj == null);
    }

    /**
     * 判断判断是否为NaN
     *
     * @param n Number类型
     * @return add by lzzzg on 2016-5-25 14:41:19.
     */
    public static boolean isNaN(Number n) {
        if (n == null) return true;
        Class<? extends Number> type = n.getClass();
        if ((Integer.class == type) || (Integer.TYPE == type)) return n.equals(Integer.valueOf(-2147483648));
        if ((Long.class == type) || (Long.TYPE == type)) return n.equals(Long.valueOf(-9223372036854775808L));
        if ((Float.class == type) || (Float.TYPE == type)) return n.equals(Float.valueOf(1.4E-45F));
        if ((Short.class == type) || (Short.TYPE == type)) return n.equals(Short.valueOf((short) -32768));
        if ((Byte.class == type) || (Byte.TYPE == type)) return n.equals(Byte.valueOf((byte) -128));
        if (BigDecimal.class == type) return n.equals(BigDecimal.valueOf(4.9E-324D));
        if (BigInteger.class == type) return n.equals(BigInteger.valueOf(-9223372036854775808L));
        return Double.isNaN(n.doubleValue());
    }


    /**
     * 获取配置文件
     *
     * @param config 配置文件路径+内容字符串
     * @return
     * @throws IOException add by lzzzg 2016-5-30 20:18:03
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
     * @param response
     * @param sucessMsg 提示消息
     * @param id 数据的id
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
        //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
        //Include.Include.ALWAYS 默认
        //Include.NON_DEFAULT 属性为默认值不序列化
        //Include.NON_EMPTY 属性为 空（""）  或者为 NULL 都不序列化
        //Include.NON_NULL 属性为NULL 不序列化
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        //设置将MAP转换为JSON时候只转换值不等于NULL的
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
        mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //设置有属性不能映射成PO时不报错
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //枚举序列化把默认序列name(false)改为toString(true)
        mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING,true);

        return mapper;
    }
    public static ObjectMapper getDefaultObjectMapper(String dateFormat) {
        ObjectMapper mapper = new ObjectMapper();
        //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
        //Include.Include.ALWAYS 默认
        //Include.NON_DEFAULT 属性为默认值不序列化
        //Include.NON_EMPTY 属性为 空（""）  或者为 NULL 都不序列化
        //Include.NON_NULL 属性为NULL 不序列化
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        //设置将MAP转换为JSON时候只转换值不等于NULL的
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
        mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.setDateFormat(new SimpleDateFormat(dateFormat));
        //设置有属性不能映射成PO时不报错
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        //枚举序列化把默认序列name(false)改为toString(true)
        mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING,true);
        return mapper;
    }

    /**
     * 反序列化复杂类型 为泛型的Collection Type
     * @param collectionClass
     * @param elementClasses
     * @return
     */
    public static JavaType getCollectionType(ObjectMapper objectMapper,Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
    /**
     * json格式数据转换为实体对象list
     *
     * @param jsonStr 实体json格式数据
     * @param clazz   实体类
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> List<T> convertToList(String jsonStr, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = getDefaultObjectMapper();
            List<T> list;
            if (!SOMUtils.isNull(jsonStr)) {
                if(!SOMUtils.isNull(clazz)) {
                    JavaType javaType = getCollectionType(objectMapper,ArrayList.class, clazz);
                    list = objectMapper.readValue(jsonStr, javaType); //执行转换
                }else {
                    list = objectMapper.readValue(jsonStr, List.class); //执行转换
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
     * @param jsonStr 实体json格式数据
     * @param clazz   实体类
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> convertToList(String jsonStr, Class<T> clazz,String dateFormat) {
    	try {
    		ObjectMapper objectMapper = getDefaultObjectMapper(dateFormat);
    		List<T> list;
    		if (!SOMUtils.isNull(jsonStr)) {
    			if(!SOMUtils.isNull(clazz)) {
    				JavaType javaType = getCollectionType(objectMapper,ArrayList.class, clazz);
    				list = objectMapper.readValue(jsonStr, javaType); //执行转换
    			}else {
    				list = objectMapper.readValue(jsonStr, List.class); //执行转换
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
	private static <T> T toObject(ObjectMapper objectMapper,String jsonStr,Class<T> clazz){
        try{
            if(!SOMUtils.isNull(jsonStr)) {
                T t = objectMapper.readValue(jsonStr, clazz);
                if (SOMUtils.isNull(t)){
                    t = (T)Class.forName(clazz.getName()).newInstance();
                }
                return t;
            }else{
                throw  new JsonNullException("Json数据为空！");
            }
        }catch (JsonMappingException jsonMappingException) {
            OMS_Logger.error(outputError(jsonMappingException.toString()));
            try {
                return (T) Class.forName(clazz.getName()).newInstance();
            }catch (Exception e){
                return null;
            }
        }catch (IOException ioe){
            OMS_Logger.error(outputError(ioe.toString()));
            return null;
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }
    @SuppressWarnings("rawtypes")
	private static <T> T toObject(ObjectMapper objectMapper,String jsonStr,TypeReference typeReference){
        try{
            if(!SOMUtils.isNull(jsonStr)) {
                T t = objectMapper.readValue(jsonStr, typeReference);
                return t;
            }else{
                throw  new JsonNullException("Json数据为空！");
            }
        }catch (JsonMappingException jsonMappingException) {
            OMS_Logger.error(outputError(jsonMappingException.toString()));
            return null;
        }catch (IOException ioe){
            OMS_Logger.error(outputError(ioe.toString()));
            return null;
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }
    /**
     * json格式数据转换为实体对象
     * @param jsonStr 实体json格式数据
     * @param valueTypeRef TypeReference
     * @param <T>
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static <T> T convertToObject(String jsonStr,TypeReference valueTypeRef) {
        ObjectMapper objectMapper = getDefaultObjectMapper();
        return toObject(objectMapper,jsonStr,valueTypeRef);
    }
    /**
     * json格式数据转换为实体对象
     * @param jsonStr 实体json格式数据
     * @param clazz 实体类
     * @param <T>
     * @return
     */
    public static <T> T convertToObject(String jsonStr,Class<T> clazz) {
        ObjectMapper objectMapper = getDefaultObjectMapper();
        return toObject(objectMapper,jsonStr,clazz);
    }

    /**
     * json格式数据转换为实体对象
     * @param jsonStr 实体json格式数据
     * @param clazz 实体类
     * @param <T>
     * @return
     */
    public static <T> T convertToObject(String jsonStr,Class<T> clazz,String dateFormat) {
        ObjectMapper objectMapper = getDefaultObjectMapper(dateFormat);
        return toObject(objectMapper,jsonStr,clazz);
    }

    /**
     * 自定义异常
     */
    @SuppressWarnings("serial")
	static class  JsonNullException extends Exception{
        public JsonNullException(String msg){
            super(msg);
        }
    }

    private static <T> String toJsonStr(ObjectMapper objectMapper,T t){
        try{
            if(!SOMUtils.isNull(t)) {
                String jsonStr = objectMapper.writeValueAsString(t);
                jsonStr = jsonStr.replace("Null","");
                jsonStr = jsonStr.replace("NaN","");
                jsonStr = jsonStr.replace("Infinity","");
                return jsonStr;
            }else{
                return null;
            }
        }catch (IOException ioe){
            OMS_Logger.error(outputError(ioe.toString()));
            return null;
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }

    /**
     * 对象转为JSON格式
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String objectToJsonStr(T t) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return toJsonStr(objectMapper,t);
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }

    /**
     * 对象转为JSON格式
     * @param t
     * @param dateFormat
     * @param <T>
     * @return
     */
    public static <T> String objectToJsonStr(T t,String dateFormat) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            if(!SOMUtils.isNull(dateFormat))objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
            return toJsonStr(objectMapper,t);
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }
    /**
     * 对象转为JSON格式
     * @param t 对象
     * @param enumsFeature  枚举序列化为调用toString还是name，如果为true则为toString，如果为false则为name
     * @param <T>
     * @return
     */
    public static <T> String objectToJsonStr(T t,boolean enumsFeature) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            //枚举序列化把默认序列name(false)改为toString(true)
            objectMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING,enumsFeature);
            if(!SOMUtils.isNull(t)) {
                String jsonStr = objectMapper.writeValueAsString(t);
                return jsonStr;
            }else{
                return null;
            }
        }catch (IOException ioe){
            OMS_Logger.error(outputError(ioe.toString()));
            return null;
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }
    /**
     * 对象转为JSON格式
     * @param t 对象
     * @param enumsFeature  枚举序列化为调用toString还是name，如果为true则为toString，如果为false则为name
     * @param dateFormat 时间格式
     * @param <T>
     * @return
     */
    public static <T> String objectToJsonStr(T t,boolean enumsFeature,String dateFormat) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            //枚举序列化把默认序列name(false)改为toString(true)
            objectMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING,enumsFeature);
            if(!SOMUtils.isNull(dateFormat))objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
            if(!SOMUtils.isNull(t)) {
                String jsonStr = objectMapper.writeValueAsString(t);
                return jsonStr;
            }else{
                return null;
            }
        }catch (IOException ioe){
            OMS_Logger.error(outputError(ioe.toString()));
            return null;
        }catch (Exception e){
            OMS_Logger.error(outputError(e.toString()));
            return null;
        }
    }

    /**
     * 根据object对象数组输出json字符串
     * */
    public static String funcResultToJsonStr(Object[] objArr)
    {
        String jsonStr = "";
        if ((objArr != null) && (objArr.length > 0)) {
            jsonStr = objectToJsonStr(objArr);
        }
        return jsonStr;
    }


    /**
     * 操作成功或失敗返回前端
     * @param response
     * @param actionResult
     */
    public static void actionResultToClient(HttpServletResponse response, Boolean isSuccess){
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/i18n", (Locale) SecurityUtils.getSubject().getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
        if (isSuccess) {
            SOMUtils.printSucessMsg(response, bundle.getString("zgzt.handleSuccess"), null);
        } else {
            SOMUtils.printFailureMsg(response, bundle.getString("zgzt.handleFail"));
        }
    }


    /**
     * 多个id字符串转换为符合数据库in查询的字符串
     * 234,567 TO '234','567'
     * @param ids
     * @return
     */
    public static String idsToIdsStr(String ids){
        String idsStr = "";
        if(!SOMUtils.isNull(ids)){
            String[] idsAr = ids.split(",");
            for(String id:idsAr){
                idsStr += "'"+id+"'"+",";
            }
            idsStr = idsStr.substring(0,idsStr.length()-1);
        }
        return idsStr;
    }
    /**
     * 多个str字符串以逗号隔开的输出为list
     * 234,567 TO list
     * @param strs
     * @return
     */
    public static List<String> strsToList(String strs){
        List<String> list = new ArrayList<>();
        if(!SOMUtils.isNull(strs)){
            String[] strsAr = strs.split(",");
            for(String str:strsAr){
                list.add(str);
            }
        }
        return list;
    }
    /**
     * 输出登陆失效的json格式数据
     * @param msg
     * @param returnUrl
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getLogoutMsgJsonData(String msg,String returnUrl) {
        try {
            Map map = new HashMap();
            ObjectMapper objectMapper = new ObjectMapper();
            map.put("success", "false");
            map.put("tip", "失败提示");
            map.put("logout", true);
            map.put("msg", SOMUtils.isNull(msg)?"User has logged off, please re login!":msg);
            map.put("url",returnUrl);
            String jsonStr = objectMapper.writeValueAsString(map);
            return jsonStr;
        } catch (Exception e) {
            OMS_Logger.error(outputError(e.toString()));
            return "";
        }
    }


    /**
     * SQL查询转义
     * @param sqlValue
     */
    public static String escape(String sqlValue){
        if(!isNull(sqlValue)){
            if(sqlValue.indexOf("'")>-1||sqlValue.indexOf("\"")>-1) {
                return sqlValue.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
            }else{
                return sqlValue;
            }
        }else{
            return null;
        }
    }

    /**
     * 以逗号(,)分开的字符串组装成符合sql查询条件中IN 或者NOT IN 的值
     * 如 aa,bb  组合成 'aa','bb'
     * @param str
     * @return
     */
    public static String strToSqlStr(String str){
        String returnStr = null;
        if(!isNull(str)){
            if(str.indexOf(",")>-1){
                String[] strs = str.split(",");
                for(String s:strs){
                    returnStr += "'"+s+"',";
                }
                returnStr = returnStr.substring(0,returnStr.length()-1);
            }else{
                returnStr = "'"+str+"'";
            }
        }
        return returnStr;
    }
    /**
     * 以逗号(,)分开的字符串组成list
     * @param str
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List strToList(String str){
        List list = new ArrayList<>();
        if(!isNull(str)){
            if(str.indexOf(",")>-1){
                String[] strs = str.split(",");
                for(String s:strs){
                    list.add(s);
                }
            }else{
                list.add(str);
            }
        }
        return list;
    }


    @SuppressWarnings({"unchecked" })
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
        File targetFile = new File(targetPath,fileName);
        File dir = targetFile.getParentFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(targetFile);
	 }
	/**
	 * 文件拷贝
	 * @param srcPath 源文件
	 * @param destPath 新文件路径
	 * @param newFileName 新文件名称
	 * @param isDel 是否删除原文件
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
  	  return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
  	}
    
    /**
     * base64编码
     * @param string
     * @return
     */
	public static byte[] encode64(byte[] bytes){    
	   return Base64.encode(bytes);    
    }
	
	/**
	 * base64解码
	 * @param str
	 * @return
	 */
	public static byte[] decode64(String str){    
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
				if(fis != null) fis.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public static String md5EncryptAndBase64(String str) {
		return encodeBase64(md5Encrypt(str));
	}

	private static byte[] md5Encrypt(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptStr.getBytes("utf8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String encodeBase64(byte[] b) {
		sun.misc.BASE64Encoder base64Encode = new BASE64Encoder();
		String str = base64Encode.encode(b);
		return str;
	}
    
}
