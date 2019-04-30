package com.xunwei.som.pojo;

public class Kpi {

	private String name;
	
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Kpi [name=" + name + ", value=" + value + "]";
	}

	public Kpi(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

}
