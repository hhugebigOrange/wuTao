package com.xunwei.som.base.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xunwei.som.pojo.AssetNumber;
import com.xunwei.som.pojo.front.AssetManage;
import com.xunwei.som.service.AsAetNumberService;
import com.xunwei.som.util.ExcelUtils;
import com.xunwei.som.util.SOMUtils;

/**
 * 资产管理
 * 
 * @author Administrator
 *
 */
@Controller
public class AssetManageController extends BaseController {

	@Autowired
	private AsAetNumberService asSetNumberService;

	// 用来保存每次查询的资产属性结果集
	private List<AssetNumber> assetNumbers;

	// 用于保存每个用户的查询记录
	private Map<String, Object> export = new HashMap<>();

	/**
	 * 接口：匹配资产管理页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/assetManage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> assetManage(ModelAndView modelAndView) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> json = new HashMap<>();
		// 从前端接受数据
		String serviceArea = request.getParameter("serviceArea");
		String custName = request.getParameter("custName");
		List<AssetNumber> selectAssetNumbers = new ArrayList<>();
		// 判断是用户还是公司账号
		String identifier = null;
		if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
			json.put("code", 1);
			json.put("msg", "请先登录");
			return json;
		}
		// 分页
		Integer page = null; // 页数
		Integer limit = null; // 每页显示的条目数
		Integer[] para = SOMUtils.pageWithLimit(request, page, limit);
		if (para[0] == null) {
			// 默认显示第一页数据
			page = 0;
			limit = 10;
		} else {
			page = para[0];
			limit = para[1];
		}
		if (SOMUtils.getCompName(request).get("role").equals("客户")) {
			custName = (String) SOMUtils.getCompName(request).get("compname");
		} else if (!(SOMUtils.getCompName(request).get("role").equals("运维总监")
				|| SOMUtils.getCompName(request).get("role").equals("总部客服")
				|| SOMUtils.getCompName(request).get("role").equals("优质运维专员")
				|| SOMUtils.getCompName(request).get("role").equals("运维管理人员"))) {
			serviceArea = (String) SOMUtils.getCompName(request).get("compname");
		}
		if (serviceArea != null) {
			if (serviceArea.equals("广州乐派数码科技有限公司") || serviceArea.equals("系统推进部") || serviceArea.equals("行业客户部")) {
				serviceArea = request.getParameter("serviceArea");
				identifier = "1";
			}
		}
		assetNumbers = asSetNumberService.selectByDynamic(serviceArea, custName, "迅维", null, null, identifier);
		selectAssetNumbers = asSetNumberService.selectByDynamic(serviceArea, custName, "迅维", page, limit, identifier);
		List<AssetManage> assetManages = new ArrayList<>();
		for (AssetNumber assetNumber : selectAssetNumbers) {
			if (assetNumber.getDevice() == null) {
				continue;
			}
			AssetManage assetManage = new AssetManage(assetNumber.getDevice().getServiceArea(),
					assetNumber.getDevice().getCustArea(), assetNumber.getDevice().getAssetAttr(),
					assetNumber.getDevice().getHoldDepartment(), assetNumber.getDevice().getHoldMan(),
					assetNumber.getDevice().getDevName(), assetNumber.getDevice().getAssetClass(),
					assetNumber.getDevice().getUnitType(), assetNumber.getAssetNumber(), assetNumber.getQuantity(),
					assetNumber.getUnitPirce(), assetNumber.getOrigValue(), assetNumber.getAccDep(),
					assetNumber.getNetValue(), assetNumber.getRealNumber(), assetNumber.getMoney(),
					assetNumber.getUnit(), assetNumber.getNumber());
			assetManages.add(assetManage);
		}
		export.put(request.getParameter("username") + "assetManage", assetNumbers);
		json.put("code", 0);
		json.put("msg", "资产属性数据");
		json.put("data", assetManages);
		json.put("count", assetNumbers.size());
		return json;
	}

	/**
	 * 导出资产管理页面excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportAssetManage", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportAssetManage(ModelAndView model, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "资产管理表";
		System.out.println(request.getParameter("username"));
		List<AssetNumber> assetNumbers = (List<AssetNumber>) export
				.get(request.getParameter("username") + "assetManage");
		// 导出Excel
		HSSFWorkbook wb = ExcelUtils.copyExcel(SOMUtils.qrAddr + "kpi/asset.xls");
		HSSFSheet sheet = wb.getSheet(tableName);
		HSSFRow row;
		int i = 2;
		// 循环将数据写入Excel
		for (AssetNumber assetNumber : assetNumbers) {
			if (assetNumber.getDevice() == null) {
				continue;
			}
			row = sheet.createRow(i);
			// 创建单元格，设置值
			row.createCell(0).setCellValue(i++-1);
			row.createCell(1).setCellValue(assetNumber.getDevice().getCustArea());
			row.createCell(2).setCellValue(assetNumber.getDevice().getServiceArea());
			row.createCell(3).setCellValue(assetNumber.getDevice().getAssetAttr());
			row.createCell(4).setCellValue(assetNumber.getDevice().getHoldDepartment());
			row.createCell(5).setCellValue(assetNumber.getDevice().getHoldMan());
			row.createCell(6).setCellValue(assetNumber.getDevice().getDeviceType());
			row.createCell(7).setCellValue(assetNumber.getDevice().getAssetNumber());
			row.createCell(8).setCellValue(assetNumber.getDevice().getAssetClass());
			row.createCell(9).setCellValue(assetNumber.getDevice().getUnitType());
			row.createCell(10).setCellValue(assetNumber.getUnit());
			row.createCell(11).setCellValue(assetNumber.getQuantity() == null ? 0 : assetNumber.getQuantity());
			row.createCell(12).setCellValue(assetNumber.getUnitPirce() == null ? 0 : assetNumber.getUnitPirce());
			row.createCell(13).setCellValue(assetNumber.getOrigValue() == null ? 0 : assetNumber.getOrigValue());
			row.createCell(14).setCellValue(assetNumber.getAccDep() == null ? 0 : assetNumber.getAccDep());
			row.createCell(15).setCellValue(assetNumber.getNetValue() == null ? 0 : assetNumber.getNetValue());
			row.createCell(16).setCellValue(assetNumber.getRealNumber() == null ? 0 : assetNumber.getRealNumber());
			row.createCell(17).setCellValue(assetNumber.getNumber() == null ? 0 : assetNumber.getNumber());
			row.createCell(18).setCellValue(assetNumber.getMoney() == null ? 0 : assetNumber.getMoney());
			row.createCell(19).setCellValue("");
		}
		ExcelUtils.download(res, wb, tableName);
		json.put("code", 0);
		json.put("msg", "导出成功");
		return json;
	}

	/**
	 * 导出资产管理页面excel
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportExcel", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> exportExcel(ModelAndView model, HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		String tableName = "资产管理表";
		HSSFWorkbook wb = ExcelUtils.copyExcel(SOMUtils.qrAddr + "kpi/asset.xls");
		ExcelUtils.download(res, wb, tableName);
		return json;
	}

	/**
	 * 方法：资产属性修改
	 * 
	 * @param modelAndView
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/changeAsset", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> changeAsset(ModelAndView modelAndView) throws ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 查看登陆人是否有权限
		Map<String, Object> json = new HashMap<>();
		// 从前端接收资产编码
		String assetNumber = request.getParameter("assetNumber"); // 资产编码
		// 接受修改参数
		String unit = request.getParameter("unit"); // 单位
		Integer quantity = null;
		Float unitPirce = null;
		Float origValue = null;
		Float accDep = null;
		Float netValue = null;
		Float realNumber = null;
		Float number = null;
		Float money = null;
		try {
			quantity = Integer.valueOf(request.getParameter("quantity")); // 数量
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "数量只能是整数");
			return json;
		}
		try {
			unitPirce = Float.valueOf(request.getParameter("unitPirce")); // 单价
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "单价只能是数字");
			return json;
		}
		try {
			origValue = Float.valueOf(request.getParameter("origValue")); // 原值
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "原值只能是数字");
			return json;
		}
		try {
			accDep = Float.valueOf(request.getParameter("accDep")); // 累计折旧
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "累计折旧只能是数字");
			return json;
		}
		try {
			netValue = Float.valueOf(request.getParameter("netValue")); // 净值
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "净值只能是数字");
			return json;
		}
		try {
			realNumber = Float.valueOf(request.getParameter("realNumber")); // 实盘数
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "实盘数只能是数字");
			return json;
		}
		try {
			number = Float.valueOf(request.getParameter("number")); // 盘亏、盘盈数
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "盘亏、盘盈数只能是数字");
			return json;
		}
		try {
			money = Float.valueOf(request.getParameter("money")); // 盘亏、盘盈金额
		} catch (NumberFormatException e) {
			json.put("code", 1);
			json.put("msg", "盘亏、盘盈金额只能是数字");
			return json;
		}
		// 判断输入参数是否有空
		Map<String, Object> args = new HashMap<>();
		args.put("资产编码", assetNumber);
		args.put("单位", unit);
		args.put("数量", quantity);
		args.put("单价", unitPirce);
		args.put("原值", origValue);
		args.put("累计折旧", accDep);
		args.put("净值", netValue);
		args.put("实盘数", realNumber);
		args.put("实盘数量", number);
		args.put("金额", money);
		String param = SOMUtils.paramsIsNull(args);
		if (!param.equals("")) {
			json.put("code", 1);
			json.put("msg", param + "不能为空");
			return json;
		}
		// 执行添加方法
		AssetNumber newAssetNumber = new AssetNumber(assetNumber, quantity, unitPirce, origValue, accDep, netValue,
				realNumber, money);
		newAssetNumber.setUnit(unit);
		newAssetNumber.setNumber(number);
		// 执行变动方法
		boolean result = asSetNumberService.updateSelective(newAssetNumber);
		if (result) {
			json.put("code", 0);
			json.put("msg", "修改成功");
			return json;
		}
		json.put("code", 1);
		json.put("msg", "修改失败");
		return json;
	}

}
