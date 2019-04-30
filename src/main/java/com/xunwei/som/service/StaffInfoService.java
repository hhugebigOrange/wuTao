package com.xunwei.som.service;

import java.util.List;
import com.xunwei.som.pojo.StaffInfo;
public interface StaffInfoService {

	/**
	 * 插入用户
	 * @param staff
	 * @return
	 */
	public boolean insertStaff(StaffInfo staff);
	
	
	/**
	 * 根据条件查询用户
	 * @param name
	 * @param compName
	 * @param post
	 * @return
	 */
	public List<StaffInfo> getStaffByDynamic(String name,String compName,String post,String phone,Integer page,Integer limit,String workCond,String identifier);
	
	/**
	 * 根据用户名查找所属公司的Id
	 * @param name
	 * @return
	 */
	public Integer selectCompIdByName(String name);
	
	/**
	 * 查找所有用户
	 * @return
	 */
	public List<StaffInfo> selectAllStaff();
	
	 /**
     * 根据用户ID查询用户
     * @param staff
     * @return
     */
    StaffInfo selectStaffByNum(String staffNo);
	
    
    /**
     * 根据传递过来的对象修改相应属性
     * @param staffInfo
     * @return
     */
    int updateStaff(StaffInfo staffInfo);
    
    
    /**
     * 根据主键，清空开始时间，结束时间，事由原因
     * @param staffId
     * @return
     */
    int updateDateByStaffId(String staffId);
    
    /**
     * 查询出已完成工单里的所有工程师的姓名
     */
    List<StaffInfo> selectStaffByOrder();
    
    /**
     * 查询出已完成工单里的所有坐席的姓名
     */
    List<StaffInfo> selectStaffBySeat();
    
    /**
     * 删除用户
     */
    int deleteStaffById(String staffId);
}
