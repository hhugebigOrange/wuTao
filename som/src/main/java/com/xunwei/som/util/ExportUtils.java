package com.xunwei.som.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;

public abstract class ExportUtils {
	public static void exportExcel(HttpServletResponse response, Class<?> clazz, List<?> list, ExportParams params,
			String fileName) {
		setResponseHeader(response, fileName);
		OutputStream out = null;
		try {
			Workbook workbook = ExcelExportUtil.exportExcel(params, clazz, list);
			out = response.getOutputStream();
			workbook.write(out);
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	protected static void setResponseHeader(HttpServletResponse response, String fileName) {
		fileName += ".xls";
		String cntentType = "application/vnd.ms-excel";
		try {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO_8859_1");// 解决文件中文名乱码
		} catch (Throwable e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Type", cntentType);// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 下载文件的默认名称
	}

}
