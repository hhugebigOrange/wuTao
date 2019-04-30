package com.xunwei.som.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;



/**
 * 二位码工具类
 * @author 
 * 
 *<div ></div>
 */
public class ZXingUtils {
																									//,String logo
		//加密,文字->二维码(图片)
	public static void encodeImt(String imgpath,String format,String content,int width,int height) throws WriterException, IOException{
	
		
		
		//放置加密设置的一些参数
		Hashtable<EncodeHintType,Object> hints=new Hashtable<EncodeHintType,Object>();
		//排错率
		hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
		//编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		//外边距,margin
		hints.put(EncodeHintType.MARGIN, 1);
		/**
		 * content: 需要加密的文字
		 * BarcodeFormat.QR_CODE:需要解析的类型(二维码)
		 * hints:加密涉及的一些参数,编码、排错率
		 */
		//判断true还是false的BitMatrix(判断二维码里面的字符,与输入的字符是否一样,一样就是true,不一样就是false)
		BitMatrix bitMatrix=new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, width, height,hints);
		
		
		//内存中生成的一张图片,此时的图片是一个二维码->需要一个boolean[][]的数组->BitMatrix
		BufferedImage img =new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				img.setRGB(x, y, (bitMatrix.get(x, y)?255255255:000));
				
			}
			
		}
		
		//画logo
		//img=LogUtil.logoMatrix(img, logo);
		
		
		
		//根据一个字符串String->产生一个File
		File file =new File(imgpath);
		
		
		//生成图片
		ImageIO.write(img, format, file);
	}
		
	/**
	 * 解密,二维码->文字
	 * @param file
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static void decodeImg(File file) throws IOException, NotFoundException{
		if(!file.exists()) return;
	
		//file->内存中的一张图片
		BufferedImage imge =ImageIO.read(file);
		MultiFormatReader formatreader=new MultiFormatReader();
		LuminanceSource source =new BufferedImageLuminanceSource(imge);
		Binarizer binarizer=new HybridBinarizer(source);
		BinaryBitmap binaryBitmap=new BinaryBitmap(binarizer);
		//将图片的->result
		Map map =new HashMap();
		map.put(EncodeHintType.CHARACTER_SET, "utf-8");
		Result result=formatreader.decode(binaryBitmap,map);
		
		System.out.println("解析结果"+result.toString());
		
		
	}

	/**
	 * 
	 * @param 要生成二维码的参数
	 * @param 生成二维码图片保存的路径
	 */
	public static void qrCode(String content,String path) {
        int width = 300;
        int height = 300;
        String format = "png";
        // 定义二维码参数
        HashMap hints = new HashMap<String,String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符集
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 纠错级别
        hints.put(EncodeHintType.MARGIN, 2);// 空白
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            Path file = new File(path).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	/**
	 * 下载二维码到桌面
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public static void downPicture(String machineCode) throws Exception{
		 URL url=new URL(SOMUtils.qrAddr+machineCode+".png");
		  HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		  conn.setRequestMethod("GET");
		  conn.setConnectTimeout(1000);//超时提示1秒=1000毫秒
		  InputStream inStream=conn.getInputStream();//获取输出流
		  byte[] data=readInputStream(inStream);
		  File file=new File("C:/Users/Administrator/Desktop/"+machineCode+".png");
		  FileOutputStream outStream=new FileOutputStream(file);
		  outStream.write(data);
		  outStream.close();
		}
		 //readInputStream方法--------------------------------------------------
	    public static byte[] readInputStream(InputStream inStream) throws Exception{
		  ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		  byte[] buffer=new byte[1024];//转换为二进制
		  int len=0;
		  while((len =inStream.read(buffer))!=-1){
		outStream.write(buffer,0,len);
		  }
		 return  outStream.toByteArray();
		 }
	    
	    public static void downloadFile(String filePath,String fileName,HttpServletResponse response) throws Exception{
	        //URI uri =  this.getClass().getClassLoader().getResource(filePath).toURI();
	        File file = new File(filePath);
	        // 清空response
	        response.reset();
	        // 设置response的Header
	        response.addHeader("Content-Disposition", "attachment;filename="
	                + new String(fileName.getBytes("gbk"), "iso-8859-1")); // 转码之后下载的文件不会出现中文乱码
	        response.addHeader("Content-Length", "" + file.length());
	        // 以流的形式下载文件
	        InputStream fis = new BufferedInputStream(new FileInputStream(filePath)); 
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	        OutputStream toClient = new BufferedOutputStream( response.getOutputStream());
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
	       }
}