package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.pojo.CustInfo;

public interface CustInfoService {

	/**
	 * 根据客户名字查找客户ID
	 * @param name
	 * @return
	 */
	public Integer selectCusIdByName(String name);
	
	
	/**
	 * 添加客户
	 */
	public int insert(CustInfo custInfo);
	
	/**
	 * 根据客户ID查找客户
	 * @param id
	 * @return
	 */
	public CustInfo selectCustById(Integer id);
	
	/**
	 * 根据名字查找相应客户
	 * @param custName
	 * @return
	 */
	public List<CustInfo> selectCustByBaseInfo(String custName,String serviceArea,Integer page,Integer limit,String identifier);
	
	/**
	 * 修改客户信息
	 * @param customer
	 * @return
	 */
	public int update(CustInfo customer);
	
	/**
	 * 删除客户
	 * @param custId
	 * @return
	 */
	boolean deleteCustById(Integer custId,String display);
	
}
