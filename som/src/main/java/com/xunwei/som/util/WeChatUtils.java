package com.xunwei.som.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.codehaus.xfire.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微信工具类
 * @author admin
 *
 */

public class WeChatUtils {
	
	private static String serviceappid = "wx5a8748c30b6c9ce5";// oms微信服务号appid
	private static String servicesecret = "1905f72908deb695186dfafb858d103f";// oms微信服务号secret
	// ACCESSTOKEN
	private static String ACCESSTOKEN = "";
	// 保存的时间
	private static long EXPIRES_IN;
	// 获取ACCESSTOKEN的时间
	private static long GET_ACCESSTOKEN_TIME;
	// 获取配置文件内容工具类

	static{
		serviceappid = SOMUtils.getValue("serviceAppid");
		servicesecret = SOMUtils.getValue("serviceSecret");
	}

	
	/**
	 * 微信网页授权验证
	 * @return
	 * @throws IOException
	 */
	public static String getWechatServiceAuthorize() throws IOException {
		String authUrl = SOMUtils.getValue("wechatServiceAuthorize");
		String redirectUrl = SOMUtils.getValue("redirectUrl");
		authUrl = authUrl.replace("{0}", serviceappid).replace("{1}", URLEncoder.encode(redirectUrl)).replace("{2}", "");
		return  RequestUtil.get(authUrl);
	}
	/**
	 * 微信网页授权验证
	 * @return
	 * @throws IOException
	 */
	public static String getWechatServiceAccessToken(String code) throws IOException {
		String accessTokenUrl = SOMUtils.getValue("wechatServiceAccessTokenUrl");
		accessTokenUrl = accessTokenUrl.replace("{0}", serviceappid).replace("{1}", servicesecret).replace("{2}", code);
		return  RequestUtil.get(accessTokenUrl);
	}
	

	/**
	 * 获取微信服务号登录的相关信息
	 * @throws IOException 
	 */
	public static Object getWeChatServiceUserInfoMsg(String accessToken,String openid) throws IOException {
		String url = SOMUtils.getValue("wechatServiceLoginUrl").replace("{0}", accessToken).replace("{1}",openid);
		// 接收反馈的结果
		return RequestUtil.get(url);
	}
	
	/**
	 * 解密用户敏感数据获取用户信息
	 * @param lat
	 * @param lng
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	 public static String getUserInfo(String encryptedData,String sessionKey,String iv){
	        // 被加密的数据
	        byte[] dataByte = Base64.decode(encryptedData);
	        // 加密秘钥
	        byte[] keyByte = Base64.decode(sessionKey);
	        // 偏移量
	        byte[] ivByte = Base64.decode(iv);
	        try {
	               // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
	            int base = 16;
	            if (keyByte.length % base != 0) {
	                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
	                byte[] temp = new byte[groups * base];
	                Arrays.fill(temp, (byte) 0);
	                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
	                keyByte = temp;
	            }
	            // 初始化
	            Security.addProvider(new BouncyCastleProvider());
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
	            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
	            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
	            parameters.init(new IvParameterSpec(ivByte));
	            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
	            byte[] resultByte = cipher.doFinal(dataByte);
	            if (null != resultByte && resultByte.length > 0) {
	                String result = new String(resultByte, "UTF-8");
	                return result;
	            }
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
	            e.printStackTrace();
	        } catch (InvalidParameterSpecException e) {
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            e.printStackTrace();
	        } catch (InvalidAlgorithmParameterException e) {
	            e.printStackTrace();
	        } catch (NoSuchProviderException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	
	public static String coordinateToAddress(double lat,double lng)throws IOException, JSONException{
		String address = "";
		String url = SOMUtils.getValue("coorToAddressUrl");
		String key = SOMUtils.getValue("qqMapKey");
		url = url + lat + "," + lng + "&key="+key+"&get_poi=1";
		String backStr = RequestUtil.get(url);
		JSONObject json = new JSONObject(backStr);
		int status = json.getInt("status");
		if(status==0){
			JSONObject resultJson = new JSONObject(json.getString("result"));
			address = resultJson.getString("address");
		}
		return address;
	}
	/**
	 * 取值
	 * @return
	 */
	public static String ACCESS_TOKEN() {
		// 获取当前的时间
		long nowTime = System.currentTimeMillis();
		// 判断时间是否过期
		if (nowTime - GET_ACCESSTOKEN_TIME >= EXPIRES_IN || ACCESSTOKEN == "") {
			getACCESS_TOKEN();
		}
		return ACCESSTOKEN;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	public static void getACCESS_TOKEN() {
		String access_token = "";
		String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + serviceappid + "&secret=" + servicesecret + "";
		String result = "";
		try {
			result = RequestUtil.get(getTokenUrl);
			JSONObject jsonResult = new JSONObject(result);
			//int code = jsonResult.getInt("errcode");
			if (jsonResult.has("access_token")) {
				ACCESSTOKEN = jsonResult.getString("access_token");
				// 获取保存的时间
				GET_ACCESSTOKEN_TIME = System.currentTimeMillis();
				// 获取保存的时间
				EXPIRES_IN = jsonResult.getInt("expires_in") * 1000;
			} else {
				// 此时出现错误，将错误信息存入日志库
				Object errmsg = jsonResult.get("errmsg");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void test() {
		
	}

	/**
	 * map:
	 * openid(String) 		需要发送的用户的openid
	 * typeid(int)     		发送消息的类型，调用哪个模板
	 * title(String) 		消息模板的标题
	 * flowList(List) 		具体消息内容，封装成集合
	 * remark(String) 		备注信息
	 * @param inputData
	 * @return
	 */
	public static String formatData(Map<String, Object> inputData) {
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("touser", inputData.get("openid").toString()); // 需要发送的用户
		request.put("template_id", sendType(Integer.valueOf(inputData.get("typeid").toString()))); // 调用哪个模板
		/* +++++++++++++为空的部分+++++++++++++ */
		request.put("url", "");
		Map<String, String> miniprogram = new HashMap<String, String>();
		miniprogram.put("appid", SOMUtils.isNull(inputData.get("appid")) ? "" : inputData.get("appid").toString());
		miniprogram.put("pagepath", SOMUtils.isNull(inputData.get("pagepath")) ? "" : inputData.get("pagepath").toString());
		request.put("miniprogram", miniprogram);
		/* +++++++++++++为空的部分+++++++++++++ */
		// 主数据集合
		Map<String, Object> data = new HashMap<String, Object>();
		// 标题
		Map<String, String> title = new HashMap<String, String>();
		// 消息标题
		title.put("value", inputData.get("title").toString());
		title.put("color", "#000000");
		data.put("first", title);
		List<String> flowList = (List<String>) inputData.get("flowList");
		for (int i = 0; i < flowList.size(); i++) {
			Map<String, String> flowMap = new HashMap<String, String>();
			flowMap.put("value", flowList.get(i));
			flowMap.put("color", "#000000");
			data.put("keyword" + (i + 1), flowMap);
		}
		// 备注
		Map<String, String> remark = new HashMap<String, String>();
		remark.put("value", inputData.get("remark").toString());
		remark.put("color", "#000000");
		data.put("remark", remark);
		request.put("data", data);
		JSONObject jsonObject = new JSONObject(request);
		return jsonObject.toString();
	}
	
	
	/**
	 * 1.商品到货通知   D_xbtfvQbtrTNukp3DKmbOC2OQSUcvHrdRplD4JtQEc
	 * 2.订单异常提醒   Fq5y7dGxpNptvC8nj9EAtShwcAAIK8tY95RDCKjtCe4
	 * 3.订单拒收提醒   kntH9h4VcAcE9as300lJQy5gcDu-PAcPrI0yS3Tz4rI
	 * 4.绑定成功通知   uKnw2e2nC0QnmBQkFzzc5JPwWE3Tei0g-g3EqXcfxKw
	 * 5.绑定失败通知   7VqQD9mdx1u1soNcy0k39UBNI5oBCQaHf1c13pt6RNM
	 * 6.商品接收通知   jBCTwzRNeJ7HdEwMX_81jEGmyo2HW_kQBq7dFK0T-E0
	 * @param typeid
	 * @return
	 */
	private static String sendType(int typeid) {
		switch (typeid) {
		case 1:
			return SOMUtils.getValue("TEMPLATE_1");
		case 2:
			return SOMUtils.getValue("TEMPLATE_2");
		case 3:
			return SOMUtils.getValue("TEMPLATE_3");
		case 4:
			return SOMUtils.getValue("TEMPLATE_4");
		case 5:
			return SOMUtils.getValue("TEMPLATE_5");
		case 6:
			return SOMUtils.getValue("TEMPLATE_6");
		case 7:
			return SOMUtils.getValue("TEMPLATE_7");
		case 8:
			return SOMUtils.getValue("TEMPLATE_8");
		default:
			return null;
		}
	}
	
	/**
	 * 发送模板消息
	 * @param jsonData
	 * @return
	 */
	public static String sendWXMessage(Map<String, Object> inputData) {
		String jsonData = formatData(inputData);
		String sendURL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + ACCESS_TOKEN();
		try {
			return RequestUtil.post(sendURL, jsonData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws ParseException, JSONException, IOException {
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("openid", "owOm30kNP7W0N5Mmv13Mi2_0TUYs");
		map.put("typeid", 1);
		map.put("title", "尊敬的用户,你的订单已经到货了,请注意查收。");
		List<String> list = new ArrayList<String>();
		list.add("268263");
		list.add("湖北省荆州市沙市区");
		list.add("2017-11-20 09:59:01");
		map.put("flowList", list);
		map.put("remark", "如有疑问请咨询客服。");*/
		
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("openid", "owOm30kNP7W0N5Mmv13Mi2_0TUYs");
		map.put("typeid", 5);
		map.put("title", "绑定用户失败！");
		List<String> list = new ArrayList<String>();
		list.add("咖喱粑粑");
		list.add("该用户已经绑定过了。");
		map.put("flowList", list);
		map.put("remark", "如有疑问请联系客服。");*/
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openid", "owOm30vMG_I1OFntvB8Ra63is2ds");
		map.put("typeid", 4);
		map.put("title", "绑定用户成功！");
		map.put("pagepath", "pages/MyOrder/MyOrder?id=1642006");
		map.put("appid", "wx14257869dc51058c");
		List<String> list = new ArrayList<String>();
		list.add("咖喱粑粑");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		list.add(sdf.format(new Date()));
		map.put("flowList", list);
		map.put("remark", "现在您可以收到推送消息，感谢您的使用。");
		String str = sendWXMessage(map);
		System.out.println(str);
		
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("openid", "owOm30kNP7W0N5Mmv13Mi2_0TUYs");
//		map.put("typeid", 3);
//		map.put("title", "您所关联的的订单在XXXX被拒收了。");
//		List<String> list = new ArrayList<String>();
//		list.add("330912-1630441"); // 订单号
//		list.add("1712001W"); // 批次
//		list.add("运输延误,运输物品时间过长."); // 原因
//		list.add("承运商原因"); // 责任归类
//		map.put("flowList", list);
//		map.put("remark", "如有疑问请咨询客服。");
//		WeChatUtils.sendWXMessage(map);
		
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("openid", "owOm30kNP7W0N5Mmv13Mi2_0TUYs");
//		map.put("typeid", 1);
//		map.put("title", "尊敬的用户,你的订单已经到货了,请注意查收。");
//		List<String> list = new ArrayList<String>();
//		list.add("1630441"); // 出库单号
//		list.add("四川省乐山市市中区嘉定南路205号二楼药剂科库房"); // 到达地址
//		list.add(FileUtils.dateToString(new Date(), 1)); // 到达时间
//		map.put("flowList", list);
//		map.put("remark", "如有疑问请咨询客服。");
//		WeChatUtils.sendWXMessage(map);
		
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("openid", "owOm30kNP7W0N5Mmv13Mi2_0TUYs");
//		map.put("typeid", 2);
//		map.put("title", "您所关联的订单有异常情况。");
//		List<String> list = new ArrayList<String>();
//		list.add("成都市新都区第二人民医院"); // 收货人
//		list.add("330912-1630441"); // 订单号
//		list.add("四川省乐山市市中区嘉定南路205号二楼药剂科库房"); // 所在地址
//		list.add("出车祸了"); // 原因
//		list.add("无"); // 订单信息
//		map.put("flowList", list);
//		map.put("remark", "如有疑问请咨询客服。");
//		WeChatUtils.sendWXMessage(map);
		
	}
}
