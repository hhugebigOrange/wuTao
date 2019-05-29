package com.xunwei.som.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.xunwei.som.pojo.Contract;

public interface ContractMapper {
    int deleteByPrimaryKey(String contractNo);

    int insert(Contract record);

    int insertSelective(Contract record);

    Contract selectByPrimaryKey(String contractNo);
    
    Contract selectByID(Integer ID);

    /**
     * 根据客户名称修改合同信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(@Param("record")Contract record,@Param("custName")String custName);

    int updateByPrimaryKey(Contract record);
    
    /**
     * 根据公司名字查询名下合同
     * @return
     */
    List<Contract> selectByComp(@Param("compName")String compName);
    
    /**
     * 根据客户名称,区域查询相应合同。另有两个条件，若timeout=1,查询过期合同，dueTo=1,查询一年内到期合同
     */
    List<Contract> selectByCust(@Param("custName")String custName,@Param("compName")String compName,@Param("timeout")String timeout,
    		@Param("dueTo")String dueTo,
    		@Param("page")Integer page,
    		@Param("limit")Integer limit,
    		@Param("contractNature")String contractNature,
    		@Param("order")String order,
    		@Param("identifier")String identifier,
    		@Param("assetAscription")String assetAscription);
    
    /**
     * 根据条件，统计总客户数
     */
    int countCustNumber(@Param("custName")String custName);
    
    
    /**
     * 根据报表条件查询合同
     */
    List<Contract> selectByKpi(@Param("contractNo")String contractNo,@Param("custName")String custName,@Param("serviceArea")String serviceArea,@Param("page")Integer page,@Param("limit")Integer limit,@Param("identifier")String identifier);
    
    
}