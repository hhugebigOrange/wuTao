package com.xunwei.som.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xunwei.som.base.controller.BaseController;


/***
 * @author 工具类：导出Excel表单
 */
public class ExcelUtils extends BaseController {

	// 日期格式1：不带时分秒
	public static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	// 日期格式2：带时分秒
	public static SimpleDateFormat fmtOne = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 日期格式3：时分秒
	public static SimpleDateFormat fmtHms = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 方法：生成Excel文件
	 * 
	 * @param res
	 * @param Titles
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public static HSSFWorkbook exportOrder(HttpServletResponse res, String[] Titles, String tableName)
			throws IOException {

		/**
		 * 以下为生成Excel操作
		 */
		// 1.创建一个workbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 2.在workbook中添加一个sheet，对应Excel中的一个sheet
		HSSFSheet sheet = wb.createSheet(tableName);
		// 3.在sheet中添加表头第0行，老版本poi对excel行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 4.创建单元格，设置值表头，设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		// 居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 转换日期格式
		// 设置表头第一列为NO，用户计数
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		// 根据传递过来的标题集合设置表头，列从1开始
		for (int j = 1; j <= Titles.length; j++) {
			cell = row.createCell(j);
			cell.setCellValue(Titles[j - 1]);
			cell.setCellStyle(style);
		}
		return wb;
	}

	/**
	 * 方法：弹出下载框
	 * 
	 * @throws IOException
	 */
	public static void download(HttpServletResponse res, HSSFWorkbook wb, String tableName) throws IOException {
		// 弹出下载框
		Date date = new Date();
		String fileName = fmt.format(date) + tableName;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		wb.write(os);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		res.reset();
		res.setContentType("application/vnd.ms-excel;charset=utf-8");
		res.setHeader("Content-Disposition",
				"attachment;filename=" + new String((fileName + ".xls").getBytes("UTF-8"), "iso-8859-1"));
		ServletOutputStream out = res.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	
	/**
	 * 方法：弹出下载框
	 * 
	 * @throws IOException
	 */
	public static void download2007(HttpServletResponse res, XSSFWorkbook wb, String tableName) throws IOException {
		// 弹出下载框
		Date date = new Date();
		String fileName = fmt.format(date) + tableName;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		wb.write(os);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		res.reset();
		res.setContentType("application/vnd.ms-excel;charset=utf-8");
		res.setHeader("Content-Disposition",
				"attachment;filename=" + new String((fileName + ".xls").getBytes("UTF-8"), "iso-8859-1"));
		ServletOutputStream out = res.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	

	/**
	 * 判断是否有用某个权限
	 * 
	 * @param Permissions
	 *            权限列表
	 * @param Permission
	 *            权限
	 * @return
	 */
	public static boolean havePermissions(String[] Permissions, String Permission) {
		for (String string : Permissions) {
			if (string.equals(Permission)) {
				return true;
			}
		}

		return false;
	}

	/**
	 *复制EXCEL文件
	 * @param fromexcel
	 */
	public static HSSFWorkbook copyExcel(String fromexcel) {  
        HSSFWorkbook wb = null;
        FileInputStream fis =null;
        FileOutputStream fos = null;
        try {  
        	fis = new FileInputStream(fromexcel);
            wb = new HSSFWorkbook(fis);  
            /*HSSFSheet fromsheet = wb.getSheetAt(0);
            Row row = fromsheet.getRow(1);
            Cell cell = row.getCell(2);
            cell.setCellValue("");*/
            fis.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{
			try {
				if(fis != null)
					fis.close();
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wb;
    }
	
	/**
	 *复制EXCEL文件
	 * @param fromexcel
	 */
	public static XSSFWorkbook copyExcel2007(String fromexcel) {  
        XSSFWorkbook wb = null;
        FileInputStream fis =null;
        FileOutputStream fos = null;
        try {  
        	fis = new FileInputStream(fromexcel);
            wb = new XSSFWorkbook(fis);  
            /*HSSFSheet fromsheet = wb.getSheetAt(0);
            Row row = fromsheet.getRow(1);
            Cell cell = row.getCell(2);
            cell.setCellValue("");*/
            fis.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{
			try {
				if(fis != null)
					fis.close();
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wb;
    }
}