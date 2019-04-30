package com.xunwei.som.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.xunwei.som.pojo.StaffInfo;

public interface StaffInfoMapper {

    /**
     * 新增用户
     * @param staff
     * @return
     */
    boolean insert(StaffInfo staff);

    /**
     * 根据条件查询用户
     * @param staff
     * @return
     */
    List<StaffInfo> selectStaffByDynamic(@Param("name")String name,@Param("compName")String compName,@Param("post")String post,@Param("phone")String phone,
    		@Param("page")Integer page,
    		@Param("limit")Integer limit,@Param("workCond")String workCond,@Param("identifier")String identifier);
    
    /**
     * 根据所属的公司名查询所属的公司ID
     */
    
    Integer selectCompIdByName(String name);
    
    /**
     * 查询所有用户
     * @param staff
     * @return
     */
    List<StaffInfo> selectAllStaff();
    
    /**
     * 根据用户ID查询用户
     * @param staff
     * @return
     */
    StaffInfo selectStaffByNum(@Param("staffNo")String staffNo);
    
    /**
     * 根据传递过来的对象修改相应参数
     */
    int update(StaffInfo staffInfo);
    
    
    /**
     * 根据主键，清空开始时间，结束时间，事由原因
     * @param staffId
     * @return
     */
    int updateDateByStaffId(@Param("staffId")String staffId);
    
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