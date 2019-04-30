package com.xunwei.som.pojo;

public class OrderStatistics {
	
	private String engineerName;
	
	private Integer engineerWoNumber1;
	
	private Integer engineerWoNumber2;
	
	private Integer engineerWoNumber3;
	
	private Integer turnOrderNumber;
	
	private Integer engineerTotalWoNumber;
	
	public Integer getTurnOrderNumber() {
		return turnOrderNumber;
	}

	public void setTurnOrderNumber(Integer turnOrderNumber) {
		this.turnOrderNumber = turnOrderNumber;
	}

	public String getEngineerName() {
		return engineerName;
	}

	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}

	public Integer getEngineerWoNumber1() {
		return engineerWoNumber1;
	}

	public void setEngineerWoNumber1(Integer engineerWoNumber1) {
		this.engineerWoNumber1 = engineerWoNumber1;
	}

	public Integer getEngineerWoNumber2() {
		return engineerWoNumber2;
	}

	public void setEngineerWoNumber2(Integer engineerWoNumber2) {
		this.engineerWoNumber2 = engineerWoNumber2;
	}

	public Integer getEngineerWoNumber3() {
		return engineerWoNumber3;
	}

	public void setEngineerWoNumber3(Integer engineerWoNumber3) {
		this.engineerWoNumber3 = engineerWoNumber3;
	}

	public Integer getEngineerTotalWoNumber() {
		return engineerTotalWoNumber;
	}

	public void setEngineerTotalWoNumber(Integer engineerTotalWoNumber) {
		this.engineerTotalWoNumber = engineerTotalWoNumber;
	}

	public OrderStatistics(String engineerName, Integer engineerWoNumber1, Integer engineerWoNumber2,
			Integer engineerWoNumber3, Integer engineerTotalWoNumber) {
		super();
		this.engineerName = engineerName;
		this.engineerWoNumber1 = engineerWoNumber1;
		this.engineerWoNumber2 = engineerWoNumber2;
		this.engineerWoNumber3 = engineerWoNumber3;
		this.engineerTotalWoNumber = engineerTotalWoNumber;
	}
}
