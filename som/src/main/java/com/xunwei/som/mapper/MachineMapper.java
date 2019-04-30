package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xunwei.som.base.model.Machine;

public interface MachineMapper {

	/*
	 * 查询所有工单
	 */
	public List<Machine> selectAllMachine() throws Exception;
	
	/*
	 * 根据条件查询工单
	 */
	public List<Machine> selectMachineByDynamic(@Param(value="machineName")String machineName) throws Exception;
	
}
