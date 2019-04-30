package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xunwei.som.mapper.AssetNumberMapper;
import com.xunwei.som.pojo.AssetNumber;
import com.xunwei.som.service.AsAetNumberService;
import com.xunwei.som.util.SqlTools;

public class AsAetNumberServiceImpl implements AsAetNumberService{

	@Override
	public boolean insert(AssetNumber assetNumber) {
		SqlSession session = SqlTools.getSession();
		AssetNumberMapper mapper = session.getMapper(AssetNumberMapper.class);
		try {
			int result= mapper.insert(assetNumber);
			session.commit();
			return result>0?true:false;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally{
			session.close();
		}
		return false;
	}

	@Override
	public List<AssetNumber> selectByDynamic(String serviceArea, String custArea, String assetAttr,Integer page,Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		AssetNumberMapper mapper = session.getMapper(AssetNumberMapper.class);
		try {
			List<AssetNumber> assetNumbers= mapper.selectByDynamic(serviceArea, custArea, assetAttr,page,limit,identifier);
			session.commit();
			return assetNumbers;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		finally{
			session.close();
		}
		return null;
	}

	@Override
	public boolean updateSelective(AssetNumber record) {
		SqlSession session = SqlTools.getSession();
		AssetNumberMapper mapper = session.getMapper(AssetNumberMapper.class);
		try {
			int result= mapper.updateSelective(record);
			session.commit();
			return result>0?true:false;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		finally{
			session.close();
		}
		return false;
	}

	@Override
	public int updateAsset(String oldNumber, String newNumber) {
		SqlSession session = SqlTools.getSession();
		AssetNumberMapper mapper = session.getMapper(AssetNumberMapper.class);
		try {
			int result= mapper.updateAsset(oldNumber, newNumber);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		finally{
			session.close();
		}
		return -1;
	}

	@Override
	public int deleteAsset(String Number) {
		SqlSession session = SqlTools.getSession();
		AssetNumberMapper mapper = session.getMapper(AssetNumberMapper.class);
		try {
			int result= mapper.deleteAsset(Number);
			session.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally{
			session.close();
		}
		return -1;
	}

}
