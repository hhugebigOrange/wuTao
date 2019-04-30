package com.xunwei.som.base.controller;

import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.pojo.permissions.User;
import com.xunwei.som.pojo.permissions.UserRole;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.service.UserService;
import com.xunwei.som.service.impl.CustInfoServiceImpl;
import com.xunwei.som.service.impl.UserServiceImpl;
import com.xunwei.som.util.CodeUtil;
import com.xunwei.som.util.SOMUtils;
import com.xunwei.som.util.WeChatUtils;

import net.sf.json.JSONObject;

/**
 * 微信端：接口
 * 
 * @author Administrator
 *
 */
@Controller
public class WeChatController extends BaseController {

	private UserService userService = new UserServiceImpl();

	private CustInfoService custInfoService = new CustInfoServiceImpl();

	private Map<String, Object> codeList = new HashMap<>();
	
	boolean registeredUser=true;

	/**
	 * 微信：验证登陆
	 * @param modelAndView
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/WeChatDologin", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> WeChatDologin(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 接收请求参数
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		// 如果账号或密码为空，则返回登陆页面
		if (SOMUtils.isNull(userName) || SOMUtils.isNull(password)) {
			json.put("code", 1);
			json.put("msg", "用户名或密码不能为空");
			return json;
		}
		// 根据用户选择的角色，查找出相应的客户群体
		List<User> users = userService.selectUserByRole(null, null, null);
		for (User user : users) {
			// 比对账号和密码,成功跳到首页
			if (userName.equals(user.getUserId()) && password.equals(user.getPassword())) {
				user.setLastLoginTime(new Date());
				if(user.getOpenId()==null){
					user.setOpenId(user.getUserId());
				}
				/**/
				// 获取登陆客户所属的公司名称
				userService.updateByPrimaryKeySelective(user);
				UserRole userRole = userService.selectByPrimaryKey(userName);
				int a=0;
				// 判断拥有哪些权限
				switch (userRole.getRoleId()) {
				case "客户":
					CustInfo cust=custInfoService.selectCustById(custInfoService.selectCusIdByName(user.getCustName()));
					user.setCustName(cust.getSignComp());
					json.put("userRole", "customer");
					break;
				case "工程师":
					json.put("userRole", "engineer");
					break;
				case "技术主管":
					json.put("userRole", "competent");
					break;
				case "运维经理":
					json.put("userRole", "competent");
					break;
				default:
					a++;
				}
				if(a==1){
					json.put("code", 1);
					json.put("msg", "对不起"+userRole.getRoleId()+"尚不能登陆手机端");
					return json;
				}
				json.put("code", 0);
				json.put("user", userName);
				json.put("name", user.getUserName());
				json.put("userName", user.getUserId());
				json.put("password", user.getPassword());
				json.put("serviceArea", user.getCustName());
				json.put("msg", "登陆成功");
				json.put("openId", user.getOpenId());
				return json;
			}
		}
		json.put("code", 1);
		json.put("msg", "账号或密码错误，请重新输入");
		return json;
	}

	/**
	 * 获得验证码
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/getCode", produces = "application/json; charset=utf-8")
	@ResponseBody
	public void getCode() {
		Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();
		// 将四位数字的验证码保存到Session中。
		String coDe = codeMap.get("code").toString();
		session.setAttribute("coDe", coDe);
		System.out.println(codeMap.get("code").toString());
		// 禁止图像缓存。
		codeList.put(request.getRemoteAddr(), coDe);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", -1);
		response.setContentType("image/jpeg");
		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos;
		try {
			sos = response.getOutputStream();
			ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
			sos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取openId
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getNowOpenId", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> isLogin() throws IOException {
		// 从前端接受数据
		Map<String, Object> json = new HashMap<>();
		String openCode = request.getParameter("code"); // code
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", openCode);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
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
		String openid = jsonObject.getString("openid");
		json.put("code", 0);
		json.put("openid", openid);
		return json;
	}

	/**
	 * 客户扫一扫注册
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/registeredUser", produces = "application/json; charset=utf-8")
	public ModelAndView registeredUser(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 获取openId
		String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		// 从前端接受数据
		String code = request.getParameter("code"); // code
		// 根据code获取openId;
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", "wxf31e52205cafba8a")
				.replace("SECRET", "60e5bd27b2f49825d70b96deb99f2ff2").replace("CODE", code);
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
		String openid = jsonObject.getString("openid");
		// 从前端接收参数
		String phone = request.getParameter("phone"); // 手机号
		String password = request.getParameter("password"); // 密码
		String name = request.getParameter("name"); // 姓名
		Integer custId = Integer.valueOf(request.getParameter("custId")); // 所属公司
		String custName=custInfoService.selectCustById(custId).getCustName();
		// 创建用户对象
		User user = new User();
		user.setUserId(phone);
		user.setOpenId(openid);
		user.setUserName(name);
		user.setPassword(password);
		user.setCustName(custName);
		user.setCreateTimed(new Date());
		userService.insertSelective(user);
		// 创建用户-权限对象
		UserRole userRole = new UserRole();
		userRole.setRoleId("客户");
		userRole.setUserId(phone);
		userService.insertSelective(userRole);
		modelAndView.setViewName("redirect:http://solutionyun.com/som/weChat/resSuccess.html");
		return modelAndView;
	}

	/**
	 * 客户扫一扫注册
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/registeredUserReally", produces = "application/json; charset=utf-8")
	public ModelAndView registeredUserReally(ModelAndView modelAndView) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 从前端接收参数
		String phone = request.getParameter("phone"); // 手机号
		String password = request.getParameter("password"); // 密码
		String newPassword = request.getParameter("newPassword"); // 确认密码
		String name = request.getParameter("name"); // 姓名
		String custName = request.getParameter("custName"); // 所属公司
		String url = "http://solutionyun.com/registeredUser?" + "phone=" + phone + "&password=" + password
				+ "&newPassword=" + newPassword + "&name=" + name + "&custName=" + custName;
		String enCodeUrl = URLEncoder.encode(url, "UTF-8");
		modelAndView.setViewName(
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf31e52205cafba8a&redirect_uri=" + enCodeUrl
						+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
		return modelAndView;
	}

	@Test
	public void test() throws UnsupportedEncodingException {
		String url = "http://solutionyun.com/registeredUser?" + "phone=" + "123" + "&password=" + "123"
				+ "&newPassword=" + "123" + "&name=" + "张" + "&custName=" + "里";
		System.out.println(URLEncoder.encode(url, "UTF-8"));
	}

	/**
	 * 获取签名
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getSign", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> getSign() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接受参数
		String url = request.getParameter("url");
		if (url == null || url.equals("")) {
			json.put("code", 1);
			json.put("msg", "url不能为空");
			return json;
		}
		json = WeChatUtils.getSign(request, url);
		return json;
	}

	/**
	 * 定时刷新微信端的AccessToken和JsapiTiket(频率，每一个半小时)
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 1000 * 60 * 45 * 2) // 服务器启动时执行一次，之后每隔一个半小时。
	public void updateWx() throws Exception {
		WeChatUtils.getAccess();
		WeChatUtils.getJsapiTicket();
	}

	/**
	 * 判断参数是否有误
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/registeredCustomer", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> registeredCustomer() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		synchronized (this) {
			Map<String, Object> json = new HashMap<>();
			// 从前端接收参数
			String phone = request.getParameter("phone"); // 手机号
			String password = request.getParameter("password"); // 密码
			String newPassword = request.getParameter("newPassword"); // 确认密码
			String name = request.getParameter("name"); // 姓名
			Integer custId = Integer.valueOf(request.getParameter("custId")); // 所属公司
			String custName=custInfoService.selectCustById(custId).getCustName();
			// 参数非空判断
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("手机号", phone);
			params.put("密码", password);
			params.put("确认密码", newPassword);
			params.put("姓名", name);
			params.put("所属公司", custName);
			// 判断其他栏目是否存在空值
			String param = SOMUtils.paramsIsNull(params);
			if (!param.equals("")) {
				json.put("code", 1);
				json.put("msg", param + "不能为空");
				return json;
			}
			// 判断密码和确认密码是否相等
			if (!password.equals(newPassword)) {
				json.put("code", 1);
				json.put("msg", "确认密码和密码输入不一致");
				return json;
			}
			// 判断该手机号是和openId是否注册过
			for (User user : userService.selectAllUser()) {
				if (user.getOpenId() == null) {
					continue;
				}
				if (user.getUserId().equals(phone)) {
					json.put("code", 1);
					json.put("msg", "该手机号已经注册过");
					return json;
				}
			}
			// 判断所属公司是否存在，如果存在，则新增
			if (custInfoService.selectCusIdByName(custName) <= 0) {
				json.put("code", 1);
				json.put("msg", "对不起，该公司尚未与本系统合作，望您谅解");
				return json;
			}
			registeredUser=true;
			json.put("code", 0);
			json.put("msg", "参数输入无误");
			return json;
		}
	}
	
	/**
	 * 根据openId获取账号密码
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUserAccount", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> getUserAccount() throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接收参数
		String openId = request.getParameter("openId"); // openId
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", openId);
		// 判断其他栏目是否存在空值
		String param = SOMUtils.paramsIsNull(params);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		User newUser=new User();
		// 判断该手机号是和openId是否注册过
		for (User user : userService.selectAllUser()) {
			if (user.getOpenId() == null) {
				continue;
			}
			if (user.getOpenId().equals(openId)) {
				newUser=user;
			}
		}
		if(newUser.getUserId()==null){
			json.put("code", 1);
			json.put("msg", "对不起，该openId不存在");
			return json;
		}
		json.put("userName", newUser.getUserName());
		json.put("password", newUser.getPassword());
		json.put("phone", newUser.getUserId());
		return json;
	}
}
