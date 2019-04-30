package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.base.model.staffInfo;

public interface StaffInfoService {

	
	public boolean insertStaff(staffInfo staff);
	
	public List<staffInfo> getStaffByDynamic(String name,String compName,String post);
	
}
