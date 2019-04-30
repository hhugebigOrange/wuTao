package com.xunwei.som.mapper;

import com.xunwei.som.pojo.ConstractExec;

public interface ConstractExecMapper {
	 /**
   	 * 增加合同执行信息
   	 * @param priNumber
   	 * @return
   	 */
    int insert(ConstractExec record);

    /**
   	 * 根据输入的条件增加相应的合同执行信息
   	 * @param priNumber
   	 * @return
   	 */
    int insertSelective(ConstractExec record);
}