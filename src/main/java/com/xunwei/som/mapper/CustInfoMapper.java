package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.pojo.CustInfo;

public interface CustInfoMapper {

	
	/**
	 * 根据客户名字查找客户ID
	 * @param name
	 * @return
	 */
	public Integer selectCusIdByName(String name);
	
	/**
	 * 添加客户
	 * @param name
	 * @return
	 */
	public int insert(CustInfo customer);
	
	/**
	 * 修改客户信息
	 * @param customer
	 * @return
	 */
	public int update(CustInfo customer);
	
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
	List<CustInfo> selectCustByBaseInfo(@Param(value="custName")String custName,
			@Param(value="serviceArea")String serviceArea,
			@Param(value="page")Integer page,
			@Param(value="limit")Integer limit,@Param(value="identifier")String identifier);
	
	/**
	 * 删除客户
	 * @param custId
	 * @return
	 */
	boolean deleteCustById(@Param(value="custId")Integer custId,@Param(value="display")String display);
	
}