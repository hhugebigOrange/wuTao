package com.xunwei.som.pojo;

public class AssetNumber {
	
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
    
    private Device device;
    
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

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
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

	public AssetNumber(String assetNumber, Integer quantity, Float unitPirce, Float origValue, Float accDep,
			Float netValue, Float realNumber, Float money) {
		super();
		this.assetNumber = assetNumber;
		this.quantity = quantity;
		this.unitPirce = unitPirce;
		this.origValue = origValue;
		this.accDep = accDep;
		this.netValue = netValue;
		this.realNumber = realNumber;
		this.money = money;
	}

	public AssetNumber() {
		super();
	}
    
    
}