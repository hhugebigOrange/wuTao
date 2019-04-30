package com.xunwei.som.base.model;

public class Machine {

	private int id;
	
	private String machineName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	@Override
	public String toString() {
		return "Machine [id=" + id + ", machineName=" + machineName + "]";
	}

}
