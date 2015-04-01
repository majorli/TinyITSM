package com.jeans.tinyitsm.util;

import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.service.hr.HRConstants;

public class HrUtil {

	/**
	 * 获取公司层级
	 * 
	 * @param company
	 *            公司对象
	 * @return 0=省公司; 1=市公司; 2=分公司; -1=不是公司
	 */
	public static byte getCompanyLevel(Organization company) {
		if (company.getType() == HRConstants.COMPANY) {
			if (company.getId() == 1L) {
				return HRConstants.PROVINCE;
			} else {
				if (company.getSuperior().getId() == 1L) {
					return HRConstants.CITY;
				} else {
					return HRConstants.BRANCH;
				}
			}
		} else {
			return -1;
		}
	}
}
