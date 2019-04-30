package com.xunwei.som.mapper;

import java.util.List;

import com.xunwei.som.base.model.staffInfo;

public interface StaffInfoMapper {

    /**
     * 新增用户
     * @param staff
     * @return
     */
    boolean insert(staffInfo staff);

    /**
     * 根据条件查询用户
     * @param staff
     * @return
     */
    List<staffInfo> selectStaffByDynamic(staffInfo staff);
}