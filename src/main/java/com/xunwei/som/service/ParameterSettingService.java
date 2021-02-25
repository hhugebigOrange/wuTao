package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.pojo.permissions.ParameterSetting;
import org.springframework.stereotype.Service;

/**
 * 参数接口
 * @author Administrator
 *
 */
@Service
public interface ParameterSettingService {

	 /**
	  * 根据参数名查找相应参数列表
	  * @param parameterName
	  * @return
	  */
	 List<ParameterSetting> selectByPrimaryKey();
	 
	 /**
	  * 根据参数名修改响应参数列表
	  * @param record
	  * @return
	  */
	 int updateByPrimaryKey(ParameterSetting record);
	 
	 int insertSelective(ParameterSetting record);
	 
	
}
