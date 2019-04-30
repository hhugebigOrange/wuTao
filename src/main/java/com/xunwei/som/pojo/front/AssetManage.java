package com.xunwei.som.pojo.front;

public class AssetManage {
	
	private String serviceArea;
	
	private String custName;
	
	private String assetAttr;
	
	private String  holdDepartment;
	
	private String  holdMan;
	
	private String devName;
	
	private String  assetClass;
	
	private String  unitType;

	private String assetNumber;

	private Integer quantity;

	private Float unitPirce;

	private Float origValue;

	private Float accDep;

	private Float netValue;

	private Float realNumber;

	private Float money;

	private String unit;

	private Float number;

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getAssetAttr() {
		return assetAttr;
	}

	public void setAssetAttr(String assetAttr) {
		this.assetAttr = assetAttr;
	}

	public String getHoldDepartment() {
		return holdDepartment;
	}

	public void setHoldDepartment(String holdDepartment) {
		this.holdDepartment = holdDepartment;
	}

	public String getHoldMan() {
		return holdMan;
	}

	public void setHoldMan(String holdMan) {
		this.holdMan = holdMan;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getUnitPirce() {
		return unitPirce;
	}

	public void setUnitPirce(Float unitPirce) {
		this.unitPirce = unitPirce;
	}

	public Float getOrigValue() {
		return origValue;
	}

	public void setOrigValue(Float origValue) {
		this.origValue = origValue;
	}

	public Float getAccDep() {
		return accDep;
	}

	public void setAccDep(Float accDep) {
		this.accDep = accDep;
	}

	public Float getNetValue() {
		return netValue;
	}

	public void setNetValue(Float netValue) {
		this.netValue = netValue;
	}

	public Float getRealNumber() {
		return realNumber;
	}

	public void setRealNumber(Float realNumber) {
		this.realNumber = realNumber;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Float getNumber() {
		return number;
	}

	public void setNumber(Float number) {
		this.number = number;
	}

	public AssetManage(String serviceArea, String custName, String assetAttr, String holdDepartment, String holdMan,
			String devName, String assetClass, String unitType, String assetNumber, Integer quantity, Float unitPirce,
			Float origValue, Float accDep, Float netValue, Float realNumber, Float money, String unit, Float number) {
		super();
		this.serviceArea = serviceArea;
		this.custName = custName;
		this.assetAttr = assetAttr;
		this.holdDepartment = holdDepartment;
		this.holdMan = holdMan;
		this.devName = devName;
		this.assetClass = assetClass;
		this.unitType = unitType;
		this.assetNumber = assetNumber;
		this.quantity = quantity;
		this.unitPirce = unitPirce;
		this.origValue = origValue;
		this.accDep = accDep;
		this.netValue = netValue;
		this.realNumber = realNumber;
		this.money = money;
		this.unit = unit;
		this.number = number;
	}

	public AssetManage() {
		super();
	}
	
}
