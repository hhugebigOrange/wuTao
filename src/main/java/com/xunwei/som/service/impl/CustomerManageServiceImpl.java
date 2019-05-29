package com.xunwei.som.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.xunwei.som.mapper.ContractMapper;
import com.xunwei.som.mapper.DeviceChangeMapper;
import com.xunwei.som.mapper.DeviceMapper;
import com.xunwei.som.mapper.OrderInfoMapper;
import com.xunwei.som.pojo.Contract;
import com.xunwei.som.pojo.Device;
import com.xunwei.som.pojo.DeviceChange;
import com.xunwei.som.service.CustomerManageService;
import com.xunwei.som.util.SqlTools;

public class CustomerManageServiceImpl implements CustomerManageService {

	/**
	 * 新增合同
	 */
	@Override
	public int insert(Contract contract) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			int result = mapper.insertSelective(contract);
			System.out.println(result > 0 ? "添加成功" : "添加失败");
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
	 * 根据公司名字查询合同
	 */
	@Override
	public List<Contract> selectByComp(String compName) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			List<Contract> contracts = mapper.selectByComp(compName);
			session.commit();
			return contracts;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 新增设备
	 */
	@Override
	public int insert(Device device) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			int result = mapper.insertSelective(device);
			System.out.println(result > 0 ? "添加成功" : "添加失败");
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
	 * 根据条件查找设备
	 */
	@Override
	public List<Device> selectByDynamic(String serviceArea, String custArea, String assetAttr,String machCode,Integer page,
			Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			List<Device> devices = mapper.selectByDynamic(serviceArea, custArea, assetAttr,machCode,page,limit,identifier);
			for (Device device : devices) {
				System.out.println(device);
			}
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 根据条件查询设备2.0
	 */
	@Override
	public List<Device> selectByDevice(String custName, String serviceArea, String unitType, String assetAttr,Integer page,Integer limit) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			List<Device> devices = mapper.selectByDevice(custName, serviceArea, unitType, assetAttr,page,limit);
			for (Device device : devices) {
				System.out.println(device);
			}
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 根据客户名称查找相应合同
	 */
	@Override
	public List<Contract> selectByCust(String custName, String compName, String timeout, String dueTo,Integer page,Integer limit,String contractNature,String order,String identifier,String assetAscription) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			List<Contract> contracts = mapper.selectByCust(custName, compName, timeout, dueTo,page,limit,contractNature,order,identifier,assetAscription);
			session.commit();
			return contracts;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	/**
	 * 根据条件统计客户总数
	 */
	@Override
	public int countCustNumber(String custName) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			int result = mapper.countCustNumber(custName);
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
	 * 根据合同编码查询合同
	 */
	@Override
	public Contract selectByPrimaryKey(String contractNo) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			Contract result = mapper.selectByPrimaryKey(contractNo);
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

	@Test
	public void selectByCust1() {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			Device devices = mapper.selectDeviceById("BAK0001");
			session.commit();
			System.out.println(devices);
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
	}

	@Override
	public Device selectDeviceById(String id) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			Device devices = mapper.selectDeviceById(id);
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<Device> selectDeviceNumByCustName(String custName) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			List<Device> devices = mapper.selectDeviceNumByCustName(custName);
			for (Device device : devices) {
				System.out.println(device);
			}
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<Device> selectMaintNumByCustName(String custName) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			List<Device> devices = mapper.selectMaintNumByCustName(custName);
			for (Device device : devices) {
				System.out.println(device);
			}
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<Device> selectByDeviceKpi(String serviceArea, String custArea, String contartctNo,Integer page,Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			List<Device> devices = mapper.selectByKpi(serviceArea, custArea, contartctNo,page,limit,identifier);
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public List<Contract> selectByKpi(String contractNo, String custName,String serviceArea, Integer page, Integer limit,String identifier) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			List<Contract> contracts = mapper.selectByKpi(contractNo, custName,serviceArea, page, limit,identifier);
			session.commit();
			return contracts;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(Contract record, String custName) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(record, custName);
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
	public int updateByPrimaryKeySelective(Device device) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			int result = mapper.updateByPrimaryKeySelective(device);
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
	public List<Device> selectDeviceByAsSetNumber(String asSetNumber) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			List<Device> devices = mapper.selectDeviceByAsSetNumber(asSetNumber);
			session.commit();
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}
		return null;
	}

	@Override
	public int insertSelective(DeviceChange record) {
		SqlSession session = SqlTools.getSession();
		DeviceChangeMapper mapper = session.getMapper(DeviceChangeMapper.class);
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
	public int cleanDeviceAttribute(String machCode) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			int result = mapper.cleanDeviceAttribute(machCode);
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
	public Device selectDeviceById(Integer id) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			Device result = mapper.selectDeviceByID(id);
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
	public Contract selectByID(Integer ID) {
		SqlSession session = SqlTools.getSession();
		ContractMapper mapper = session.getMapper(ContractMapper.class);
		try {
			Contract result = mapper.selectByID(ID);
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
	public Device selectByCode(String machCode) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			Device result = mapper.selectByPrimaryKey(machCode);
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
	public int updateDeviceServiceArea(String oldServiceArea, String newServiceArea) {
		SqlSession session = SqlTools.getSession();
		DeviceMapper mapper = session.getMapper(DeviceMapper.class);
		try {
			int result = mapper.updateDeviceServiceArea(oldServiceArea, newServiceArea);
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
	public boolean deleteOrder(String woNumber) {
		SqlSession session = SqlTools.getSession();
		OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
		try {
			boolean result = mapper.deleteOrder(woNumber);
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
