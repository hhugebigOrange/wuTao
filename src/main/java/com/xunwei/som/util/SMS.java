package com.xunwei.som.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class SMS {

	public static final int COMPLETION = 2114; // 完工模板短信
	public static final int SERVICE_ACCEPTANCE = 2113; // 客服受理工单模板短信
	public static final int ENGINNER_ACCEPTANCE = 2112; // 工程师接单模板短信

	/**
	 * 发送短信
	 * 
	 * @param template_id
	 *            //模板ID
	 * @param phone
	 *            //要发送人的电话号码
	 * @param name
	 *            //要发送人的姓名
	 * @param woNumber
	 *            //要发送的工单号码
	 * @throws IOException
	 */
	public static void senMessage(int template_id, String phone, String name, String woNumber) {
		try {
			URL url = new URL("https://sms-api.upyun.com/api/messages");
			// 设置 JSON 参数
			JSONObject object = new JSONObject();
			object.put("template_id", template_id);
			object.put("mobile", phone);
			object.put("vars", name + "|" + woNumber);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置必要参数
			conn.setConnectTimeout(10000);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Authorization", "nCWRaTlHe2wz1KsnTF3olqQUjisrYo");
			conn.setRequestProperty("Content-type", "application/json");

			// 创建链接
			conn.connect();
			OutputStream os = conn.getOutputStream();
			os.write(object.toString().getBytes("UTF-8"));

			// Gets the status code from an HTTP response message
			int code = conn.getResponseCode();

			InputStreamReader reader = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(reader);
			char[] chars = new char[1024];
			int length = 0;
			StringBuilder result = new StringBuilder();
			while ((length = br.read(chars)) != -1) {
				result.append(chars, 0, length);
			}
			System.out.println("code:" + code + "::" + result.toString());
		} catch (Exception e) {
			
		}

	}
}
