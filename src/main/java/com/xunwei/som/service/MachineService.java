package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.base.model.Machine;
import org.springframework.stereotype.Service;

@Service
public interface MachineService {

	public List<Machine> selectAllMachine() throws Exception;
	
	/*
	 * 根据条件查询工单
	 */
	public List<Machine> selectMachineByDynamic(String machineName) throws Exception;

}
