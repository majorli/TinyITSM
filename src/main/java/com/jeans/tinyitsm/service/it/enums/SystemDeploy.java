package com.jeans.tinyitsm.service.it.enums;

import com.jeans.tinyitsm.service.EnumTitle;

public enum SystemDeploy implements EnumTitle {
	Nation, NationAndProvince, NationAndProvinceAndCities, Province, ProvinceAndCities, Cities, Outer, Custom;

	private String[] titles = new String[] { "国家局集中", "国省两级分布", "国省市三级分布", "省集中", "省市两级分布", "市集中", "行业外", "其他部署方式" };

	@Override
	public String getTitle() {
		return titles[this.ordinal()];
	}

}
