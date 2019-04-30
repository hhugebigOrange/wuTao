package com.xunwei.som.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class CodeUtil {
	private static int width = 70;// 定义图片的width
	private static int height = 30;// 定义图片的height
	private static int codeCount = 4;// 定义图片上显示验证码的个数
	private static int xx = 12;
	private static int fontHeight = 18;
	private static int codeY = 22;
	private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * 生成一个map集合 code为生成的验证码 codePic为生成的验证码BufferedImage对象
	 * 
	 * @return
	 */
	public static Map<String, Object> generateCodeAndPic() {
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// Graphics2D gd = buffImg.createGraphics();
		// Graphics2D gd = (Graphics2D) buffImg.getGraphics();
		Graphics gd = buffImg.getGraphics();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		// 设置字体。
		gd.setFont(font);

		// 画边框。
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		// 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 30; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String code = String.valueOf(codeSequence[random.nextInt(36)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			// 用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(000000));
			gd.drawString(code, (i + 1) * xx, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(code);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 存放验证码
		map.put("code", randomCode);
		// 存放生成的验证码BufferedImage对象
		map.put("codePic", buffImg);
		return map;
	}

	public static void main(String[] args) throws Exception {
		int BLACK = 0xFF000000;
		int WHITE = 0xFFFFFFFF;
		String text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf31e52205cafba8a&redirect_uri=http%3a%2f%2fsolutionyun.com%3fmachCode%3d"
				+ "GZZ0041"+"&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect"; // 这里是URL ，扫描之后就跳转到这个界面
		String path = "D:/"; // 图片生成的位置
		int width = 900;
		int height = 900;
		// 二维码图片格式
		String format = "png";
		// 设置编码，防止中文乱码
		Hashtable<EncodeHintType, Object> ht = new Hashtable<EncodeHintType, Object>();
		ht.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置二维码参数(编码内容，编码类型，图片宽度，图片高度,格式)
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, ht);
		// 生成二维码(定义二维码输出服务器路径)
		File outputFile = new File(path);
		if (!outputFile.exists()) {
			// 创建文件夹
			outputFile.mkdir();
		}
		int b_width = bitMatrix.getWidth();
		int b_height = bitMatrix.getHeight();
		// 建立图像缓冲器
		BufferedImage image = new BufferedImage(b_width, b_height, BufferedImage.TYPE_3BYTE_BGR);
		for (int x = 0; x < b_width; x++) {
			for (int y = 0; y < b_height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
			}
		}
		// 生成二维码
		ImageIO.write(image, format, new File(path + "/跳转." + format)); // 二维码的名称
																				// 是
																				// erweima.sgif
	}

	/**
	 * 校验验证码
	 * 
	 * @param code(用户输入的验证码)
	 * @param sessionCode(验证码)
	 * @return
	 */
	public static String checkCode(String code, String sessionCode) {
		if (code == null || "".equals(code)) {
			return "输入的验证码不能为空";
		}

		if (code.equals(sessionCode)) {
			return "验证码相同";
		} else {
			return "输入的验证码不对";
		}
	}

}
