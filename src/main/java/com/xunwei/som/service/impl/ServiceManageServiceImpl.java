package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.OrderInfoMapper;
import com.xunwei.som.mapper.OrderPartsMapper;
import com.xunwei.som.mapper.ServiceInfoMapper;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.OrderParts;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.service.ServiceManageService;
import com.xunwei.som.util.SqlTools;

public class ServiceManageServiceImpl implements ServiceManageService{

	/**
	 * 方法：添加工单信息
	 */
	@Override
	public boolean insertOrder(OrderInfo orderInfo) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			boolean result = mapper.insert(orderInfo);
			System.out.println(result?"添加成功":"添加失败");
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return false;
	}

	
	/**
	 * 方法：获得所有待处理工单的集合
	 */
	@Override
	public List<OrderInfo> getOrderByProcessed(String woStatus) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> result = mapper.getOrderByProcessed(woStatus);
			System.out.println(result==null?"查询失败":"查询成功");
			session.commit();
			for (OrderInfo orderInfo : result) {
				System.out.println(orderInfo);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 方法：获得所有待处理工单的个数
	 */
	@Override
	public int getOrderByWoStatus(String woStatus) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			int result = mapper.getOrderByWoStatus(woStatus);
			System.out.println(result>0?"查询成功":"查询失败");
			session.commit();
			System.out.println("未处理个数为:"+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return -1;
	}


	/**
	 * 方法：根据工单号修改工单状态
	 */
	@Override
	public int updateWoStatus(String woNumber, String woStatus) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			int result = mapper.updateWoStatus(woNumber, woStatus);
			System.out.println(result>0?"修改成功":"修改失败");
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


	/**
	 * 方法：查询所有工单
	 */
	@Override
	public List<OrderInfo> selectOrderAll() {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> result = mapper.selectOrderAll();
			System.out.println(result==null?"查询失败":"查询成功");
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}


	/**
	 * 方法：根据条件查询相应工单
	 */
	@Override
	public List<OrderInfo> selectOrderByDynamic(String custName, String machCode, String woNumber,String startDate, String endDate,String type,String woStatus) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> result = mapper.selectOrderByDynamic(custName, machCode, woNumber,startDate, endDate,type,woStatus);
			System.out.println(result==null?"查询失败":"查询成功");
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}
	
	/**
	 * 方法：根据条件查询相应工单2.0
	 */
	@Override
	public List<OrderInfo> selectOrderByOrder(String custName, String serviceArea, String startDate, String endDate,
			String workState, String[] faultType,String conver,Integer page,Integer limit) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> result = mapper.selectOrderByOrder(custName, serviceArea, startDate, endDate, workState, faultType,conver,page,
					limit);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}


	@Override
	public OrderInfo selectOrderByOrderNum(String orderNum) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			OrderInfo result = mapper.selectOrderByOrderNum(orderNum);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}


	@Override
	public int updateFaultType(String woNumber, String faultType) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			int result = mapper.updateFaultType(woNumber, faultType);
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
	public String selectLastOrderNumber(String compName) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			String result = mapper.selectLastOrderNumber(compName);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updateWoNumber(String woNumber, String newWoNumber) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			int result = mapper.updateWoNumber(woNumber, newWoNumber);
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
	public boolean updateOrder(OrderInfo record) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			boolean result = mapper.updateOrder(record);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return false;
	}


	@Override
	public List<OrderInfo> selectOrderByParts(OrderInfo record) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> result = mapper.selectOrderByParts(record);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}


	@Override
	public int insertSelective(ServiceInfo record) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			int result = mapper.insertSelective(record);
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
	public int insertSelective(OrderParts record) {
		SqlSession session = SqlTools.getSession();
		OrderPartsMapper mapper = session.getMapper(OrderPartsMapper.class);
		try {
			int result = mapper.insertSelective(record);
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
	public int updateSelective(OrderParts record) {
		SqlSession session = SqlTools.getSession();
		OrderPartsMapper mapper = session.getMapper(OrderPartsMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(record);
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
	public OrderParts selectSelective(OrderParts record) {
		SqlSession session = SqlTools.getSession();
		OrderPartsMapper mapper = session.getMapper(OrderPartsMapper.class);
		try {
			OrderParts result = mapper.selectByPrimaryKey(record.getWoNumber());
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

}
