package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.OrderInfoMapper;
import com.xunwei.som.mapper.ServiceInfoMapper;
import com.xunwei.som.pojo.OrderInfo;
import com.xunwei.som.pojo.ServiceInfo;
import com.xunwei.som.pojo.StaffInfo;
import com.xunwei.som.service.ServiceInfoService;
import com.xunwei.som.util.SqlTools;

public class ServiceInfoServiceImpl implements ServiceInfoService{

	/**
	 * 根据服务信息里面的工单号查找相应工单信息
	 */
	@Override
	public List<OrderInfo> selectOrderByNum() {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<OrderInfo> orders = mapper.selectOrderByNum();
			session.commit();
			return orders;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<ServiceInfo> selectAllServiceInfo() {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> staffs = mapper.selectAllServiceInfo();
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}



	@Override
	public List<StaffInfo> selectStaffByNum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceInfo> selectServiceInfByDynamic(String serviceArea, String custName, String startDate,
			String endDate, String workState,String custScore,String machCode,String custScore3,String custScore5,String repairType,String[] faultType,Integer page,Integer limit,String ProcessingState,String enginnerName,String identifier) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> staffs = mapper.selectServiceInfByDynamic(serviceArea, custName, startDate, endDate, workState,custScore,machCode,custScore3,custScore5,repairType,faultType,page,limit,ProcessingState,enginnerName,identifier);
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 根据工单号查找相应的服务评价
	 */
	@Override
	public ServiceInfo selectServiceInfByDyWoNumber(String woNumber) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			ServiceInfo staff = mapper.selectServiceInfByDyWoNumber(woNumber);
			session.commit();
			return staff;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 根据工单号修改相应的服务评价
	 */
	@Override
	public int update(String woNumber, String custSat, String custEva, Integer custScore) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			int result = mapper.update(woNumber, custSat, custEva, custScore);
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
	public List<ServiceInfo> selectServiceInfByDynamic2(String compName, String woNumber, String sendTime) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> staffs = mapper.selectServiceInfByDynamic2(compName, woNumber, sendTime);
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<ServiceInfo> selectServiceInfoByengineerKpi(String staffId,String custName,String serviceArea,String startDate,String endDate,String[] faultType,Integer page,Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> staffs = mapper.selectServiceInfoByengineerKpi(staffId, custName,serviceArea,startDate, endDate,faultType,page,limit,identifier);
			session.commit();
			return staffs;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<OrderInfo> selectOrderInfoByKpi() {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> orderInfos = mapper.selectOrderInfoByKpi();
			session.commit();
			return orderInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<ServiceInfo> selectServiceInfoBySeatServiceKpi(String staffId, String startDate, String endDate,String[] faultType) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> serviceInfo = mapper.selectServiceInfoBySeatServiceKpi(staffId, startDate, endDate,faultType);
			session.commit();
			return serviceInfo;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<OrderInfo> selectOrderInfoBySeat() {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			List<OrderInfo> orderInfos = mapper.selectOrderInfoBySeat();
			session.commit();
			return orderInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<ServiceInfo> selectServiceInfoByOrder(String custName, String woNumber, String faultType,
			String startDate, String endDate) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> serviceInfos = mapper.selectServiceInfoByOrder(custName, woNumber, faultType, startDate, endDate);
			session.commit();
			return serviceInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updateServiceInfo(String woNumber, String woStatus) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			int result = mapper.updateServiceInfo(woNumber, woStatus);
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
	public boolean upDateServiceInfo(ServiceInfo serviceInfo) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			boolean result = mapper.upDateServiceInfo(serviceInfo);
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
	public List<ServiceInfo> selectOrderByWeChat(ServiceInfo record) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> result = mapper.selectOrderByWeChat(record);
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
	public List<ServiceInfo> selectOrderByWeChatCustomer(OrderInfo record) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> result = mapper.selectOrderByWeChatCustomer(record);
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
	public List<ServiceInfo> selectOrderByWeChatTechnical(ServiceInfo record) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> result = mapper.selectOrderByWeChatTechnical(record);
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
	public boolean deleteServiceInfo(String woNumber) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			boolean result = mapper.deleteServiceInfo(woNumber);
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
	public List<ServiceInfo> selectServiceInfByWoStatus(String serviceArea, String custName, String startDate,
			String endDate, Integer page, Integer limit, String ProcessingState, String identifier) {
		SqlSession session = SqlTools.getSession();
		ServiceInfoMapper mapper = session.getMapper(ServiceInfoMapper.class);
		try {
			List<ServiceInfo> result = mapper.selectServiceInfByWoStatus(serviceArea, custName, startDate, endDate, page, limit, ProcessingState, identifier);
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
