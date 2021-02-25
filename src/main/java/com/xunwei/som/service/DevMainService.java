package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.pojo.DevMain;
import org.springframework.stereotype.Service;

@Service
public interface DevMainService {

    /**
     * 根据条件查询设备执行情况
     * @return
     */
    List<DevMain> selectDevMain(String compName,String custName,String engineerName);
    
    
	
}
