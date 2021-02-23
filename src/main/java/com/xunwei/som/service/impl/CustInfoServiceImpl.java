package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.CustInfoMapper;
import com.xunwei.som.pojo.CustInfo;
import com.xunwei.som.service.CustInfoService;
import com.xunwei.som.util.SqlTools;

public class CustInfoServiceImpl implements CustInfoService{

	/**
	 * 方法：根据客户名称查找客户Id
	 */
	@Override
	public Integer selectCusIdByName(String name) {
		SqlSession session = SqlTools.getSession();
		CustInfoMapper mapper = session.getMapper(CustInfoMapper.class);
		try {
			Integer result = mapper.selectCusIdByName(name);
			if(result!=null){
				System.out.println(result);
				session.commit();
				return result;
			}else{
				System.out.println("没有该用户");
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return -1;
	}
	
	/**
	 * 添加客户
	 */
	@Override
	public int insert(CustInfo custInfo) {
		SqlSession session = SqlTools.getSession();
		CustInfoMapper mapper = session.getMapper(CustInfoMapper.class);
		try {
			int result = mapper.insert(custInfo);
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
	public CustInfo selectCustById(Integer id) {
		SqlSession session = SqlTools.getSession();
		CustInfoMapper mapper = session.getMapper(CustInfoMapper.class);
		try {
			CustInfo custInfo = mapper.selectCustById(id);
			session.commit();
			return custInfo;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<CustInfo> selectCustByBaseInfo(String custName,String serviceArea,Integer page,Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		CustInfoMapper mapper = session.getMapper(CustInfoMapper.class);
		try {
			List<CustInfo> custInfos = mapper.selectCustByBaseInfo(custName,serviceArea,page,limit,identifier);
			session.commit();
			return custInfos;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();  
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int update(CustInfo customer) {
		SqlSession session = SqlTools.getSession();
		CustInfoMapper mapper = session.getMapper(CustInfoMapper.class);
		try {
			int result = mapper.update(customer);
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
	public boolean deleteCustById(Integer custId,String display) {
		SqlSession session = SqlTools.getSession();
		CustInfoMapper mapper = session.getMapper(CustInfoMapper.class);
		try {
			boolean result = mapper.deleteCustById(custId,display);
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
}
