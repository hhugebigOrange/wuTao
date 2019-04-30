package com.xunwei.som.service.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.xunwei.som.mapper.CustomerFeedbackMapper;
import com.xunwei.som.pojo.CustomerFeedback;
import com.xunwei.som.service.CustomerFeedbackService;
import com.xunwei.som.util.SqlTools;

public class CustomerFeedbackServiceImpl implements CustomerFeedbackService{

	@Override
	public int insertSelective(CustomerFeedback record) {
		SqlSession session = SqlTools.getSession();
		CustomerFeedbackMapper mapper = session.getMapper(CustomerFeedbackMapper.class);
		try {
			int result = mapper.insertSelective(record);
			System.out.println(result>0?"添加成功":"添加失败");
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return -1;
	}

	@Override
	public List<CustomerFeedback> selectAllFeedback() {
		SqlSession session = SqlTools.getSession();
		CustomerFeedbackMapper mapper = session.getMapper(CustomerFeedbackMapper.class);
		try {
			List<CustomerFeedback> CustomerFeedbacks = mapper.selectAllFeedback();
			session.commit();
			return CustomerFeedbacks;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

}
