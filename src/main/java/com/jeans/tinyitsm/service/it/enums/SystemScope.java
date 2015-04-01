package com.jeans.tinyitsm.service.it.enums;

import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.util.HrUtil;

public enum SystemScope implements EnumTitle {
	Private, PrivateAndInferiors, PrivateAndDirectInferiors, DirectInferiors, Inferiors, Custom;

	private String[] titles = new String[] { "本公司", "本公司及全体下属公司", "本公司及全体直属公司", "全体直属公司", "全体下属公司", "指定公司" };
	private String[] titles_prov = new String[] { "省公司本级", "全省各公司", "省市两级", "全体市公司", "全省各地区", "指定公司" };
	private String[] titles_city = new String[] { "市公司本级", "全地区", "全地区", "全体分公司", "全体分公司", "指定公司" };

	@Override
	public String getTitle() {
		return titles[this.ordinal()];
	}

	public String getTitle(Organization company) {
		byte type = HrUtil.getCompanyLevel(company);
		if (type < 0) {
			return "";
		}
		if (type == HRConstants.PROVINCE) {
			return titles_prov[this.ordinal()];
		}
		if (type == HRConstants.CITY) {
			return titles_city[this.ordinal()];
		}
		return "本公司";
	}
}
