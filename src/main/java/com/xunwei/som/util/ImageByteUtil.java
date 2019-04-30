package com.xunwei.som.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;

import com.xunwei.som.base.controller.BaseController;

import net.coobird.thumbnailator.Thumbnails;

//批量导出二维码工具类，压缩为ZIP
public class ImageByteUtil extends BaseController {

	private static float QUALITY = 0.6f;

	public static void compressZip(List<File> listfiles, OutputStream output, String encode, boolean compress,
			String alias) {
		ZipOutputStream zipStream = null;
		try {
			zipStream = new ZipOutputStream(output);
			for (File file : listfiles) {
				compressZip(file, zipStream, compress, alias + "_" + (listfiles.indexOf(file) + 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zipStream != null) {
					zipStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void compressZip(File file, ZipOutputStream zipStream, boolean compress, String alias)
			throws Exception {
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			// zip(input, zipStream, file.getName(), compress);
			zip(input, zipStream, alias + "." + file.getName().substring(file.getName().lastIndexOf(".") + 1),
					compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void zip(InputStream input, ZipOutputStream zipStream, String zipEntryName, boolean compress)
			throws Exception {
		byte[] bytes = null;
		BufferedInputStream bufferStream = null;
		try {
			if (input == null)
				throw new Exception("获取压缩的数据项失败! 数据项名为：" + zipEntryName);
			// 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
			ZipEntry zipEntry = new ZipEntry("图片/" + zipEntryName);
			// 定位到该压缩条目位置，开始写入文件到压缩包中
			zipStream.putNextEntry(zipEntry);
			if (compress) {
				bytes = ImageByteUtil.compressOfQuality(input, 0);
				zipStream.write(bytes, 0, bytes.length);
			} else {
				bytes = new byte[1024 * 5];// 读写缓冲区
				bufferStream = new BufferedInputStream(input);// 输入缓冲流
				int read = 0;
				while ((read = bufferStream.read(bytes)) != -1) {
					zipStream.write(bytes, 0, read);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bufferStream)
					bufferStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] compressOfQuality(File file, float quality) throws Exception {
		byte[] bs = null;
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			bs = compressOfQuality(input, quality);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bs;
	}

	public static byte[] compressOfQuality(InputStream input, float quality) throws Exception {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			if (quality == 0) {
				Thumbnails.of(input).scale(1f).outputQuality(QUALITY).toOutputStream(output);
			} else {
				Thumbnails.of(input).scale(1f).outputQuality(quality).toOutputStream(output);
			}
			return output.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		byte[] buffer = new byte[1024];
		// 生成的ZIP文件名为Demo.zip
		String strZipName = "E:/1.zip";
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipName));
		// 需要同时下载的两个文件result.txt ，source.txt
		File[] file1 = { new File("E:/som/src/main/webapp/qrcode/BAK0001.png"), 
				new File("E:/som/src/main/webapp/qrcode/BAK0002.png"),
				new File("E:/som/src/main/webapp/qrcode/BAK0003.png"), 
				new File("E:/som/src/main/webapp/qrcode/BAK0004.png") };
		for (int i = 0; i < file1.length; i++) {
			FileInputStream fis = new FileInputStream(file1[i]);
			out.putNextEntry(new ZipEntry(file1[i].getName()));
			int len;
			// 读入需要下载的文件的内容，打包到zip文件
			while ((len = fis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.closeEntry();
			fis.close();
		}
		out.close();
	}
}
