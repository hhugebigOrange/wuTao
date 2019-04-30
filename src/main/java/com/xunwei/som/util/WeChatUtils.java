package com.xunwei.som.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;

import com.xunwei.som.base.controller.BaseController;
import com.xunwei.som.pojo.WeChatConfig;
import com.xunwei.som.service.WeChatConfigService;
import com.xunwei.som.service.impl.WeChatConfigServiceImpl;

import net.sf.json.JSONObject;

/**
 * 微信工具类
 * 
 * @author admin
 *
 */

public class WeChatUtils extends BaseController {

	private static String serviceappid = "wxf31e52205cafba8a";// som微信服务号appid
	private static String servicesecret = "60e5bd27b2f49825d70b96deb99f2ff2";// som微信服务号secret
	// ACCESSTOKEN
	private static String ACCESSTOKEN = "";
	// JSAPI_TICKET
	private static String JSAPI_TICKET = "";
	// 保存的时间
	private static long EXPIRES_IN;
	// 获取ACCESSTOKEN的时间
	private static long GET_ACCESSTOKEN_TIME;
	// 获取配置文件内容工具类

	/**
	 * 刷新access_token
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getAccess() throws IOException {
		String GETOPENID = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a").replace("APPSECRET",
				"60e5bd27b2f49825d70b96deb99f2ff2");
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		System.out.println(jsonObject + "12313123");
		String accessToken = jsonObject.getString("access_token");
		WeChatUtils.setValue("access_token", accessToken);
		return accessToken;
	}

	/**
	 * 刷新jsapi_ticket
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getJsapiTicket() throws IOException {
		String GETOPENID = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("ACCESS_TOKEN", WeChatUtils.getValue("access_token"));
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		System.out.println(jsonObject + "112312");
		String ticket = jsonObject.getString("ticket");
		WeChatUtils.setValue("jsapi_ticket", ticket);
		return ticket;
	}

	/**
	 * 从数据库中拿取相应的值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		WeChatConfigService weChatConfigService = new WeChatConfigServiceImpl();
		String value = weChatConfigService.selectById(key).getValue();
		return value;
	}

	/**
	 * 从数据库中写入相应的值
	 * 
	 * @param key
	 * @return
	 */
	public static void setValue(String key, String value) {
		WeChatConfigService weChatConfigService = new WeChatConfigServiceImpl();
		WeChatConfig weChatConfig = new WeChatConfig();
		weChatConfig.setName(key);
		weChatConfig.setValue(value);
		weChatConfigService.updateById(weChatConfig);
	}

	// 算出签名
	public static Map<String, Object> getSign(HttpServletRequest request,String url) throws IOException {
		Map<String, Object> json = new HashMap<>();
		String noncestr = WeChatUtils.getRandomChar(11);// 随机字符串
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
		// 5、将参数排序并拼接字符串
		String str = "jsapi_ticket=" + WeChatUtils.getValue("jsapi_ticket") + "&noncestr=" + noncestr + "&timestamp="
				+ timestamp + "&url=" + url;
		System.out.println(str);
		json.put("appId", WeChatUtils.serviceappid);
		json.put("timestamp", timestamp);
		json.put("noncestr", noncestr);
		json.put("signature", DigestUtils.sha1Hex(str));
		return json;
	}
	
	// 生成随机字符串
	public static String getRandomChar(int length) {
		char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(36)]);
		}
		return buffer.toString();
	}

	@Test
	public void test() throws IOException {
		System.out.println(DigestUtils.sha1Hex("jsapi_ticket=HoagFKDcsGMVCIY2vOjf9uZdyRFQI_Pa_p7AxV1BNtIIa7hXxy97--bQTS9buNPbN4NKD07In1GsBMAbJFJo_Q&noncestr=X8W27HIX5EZ&timestamp=1548120761&url=http://solutionyun.com/accidentScan"));
	}

	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			public void run() {
				// task to run goes here
				System.out.println("Hello !!");
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.HOURS);
	}
}
