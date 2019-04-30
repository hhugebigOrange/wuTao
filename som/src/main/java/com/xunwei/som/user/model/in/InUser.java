package com.xunwei.som.user.model.in;

import com.xunwei.som.base.model.PagedIn;



public class InUser extends PagedIn{
	private String name;
	private String tel;
	private String roleId;
	private String querySql;
	private String receivingnumber;
	private String receivingnumberlike;
	private String province;
	private String city;
	private String customername;
	private String company;
	private String bindnumber;
	private String unamelike;

	
	
	public String getUnamelike() {
		return unamelike;
	}

	public void setUnamelike(String unamelike) {
		this.unamelike = unamelike;
	}

	public String getReceivingnumberlike() {
		return receivingnumberlike;
	}

	public void setReceivingnumberlike(String receivingnumberlike) {
		this.receivingnumberlike = receivingnumberlike;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getReceivingnumber() {
		return receivingnumber;
	}

	public void setReceivingnumber(String receivingnumber) {
		this.receivingnumber = receivingnumber;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBindnumber() {
		return bindnumber;
	}

	public void setBindnumber(String bindnumber) {
		this.bindnumber = bindnumber;
	}

}
