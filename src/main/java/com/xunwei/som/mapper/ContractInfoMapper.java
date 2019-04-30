package com.xunwei.som.mapper;

import com.xunwei.som.pojo.ContractInfo;

public interface ContractInfoMapper {

	/**
	 * 插入合同信息
	 * @param contractInfo
	 * @return
	 */
	public boolean insert(ContractInfo contractInfo);
	
}