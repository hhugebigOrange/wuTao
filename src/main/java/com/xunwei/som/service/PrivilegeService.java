package com.xunwei.som.service;

import java.util.List;

import com.xunwei.som.pojo.PrivilegeTable;
import org.springframework.stereotype.Service;

@Service
public interface PrivilegeService {

	/**
	 * 根据所有用户
	 */
	List<PrivilegeTable> selectAllPrivilege();
	
	 /**
     * 根据用户名查找密码
     * @param username
     * @return
     */
	PrivilegeTable selectPasswordByUser(String userName);
    
    /**
     * 根据用户名修改密码
     * @param userName
     * @return
     */
    int updatePrivile(String userName,String password);
	
}
