package com.jeans.tinyitsm.service.portal.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeans.tinyitsm.dao.FtsDao;
import com.jeans.tinyitsm.model.CloudUnit;
import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.asset.Hardware;
import com.jeans.tinyitsm.model.asset.Software;
import com.jeans.tinyitsm.model.cloud.CloudFile;
import com.jeans.tinyitsm.model.cloud.CloudList;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.FtsResultItem;
import com.jeans.tinyitsm.service.portal.FtsService;
import com.jeans.tinyitsm.service.portal.PortalConstants;
import com.jeans.tinyitsm.util.FileUtil;
import com.jeans.tinyitsm.util.LoggerUtil;

@Service
public class FtsServiceImpl implements FtsService {

	private FtsDao<Hardware> hardwareDao;
	private FtsDao<Software> softwareDao;
	private FtsDao<CloudFile> fileDao;
	private FtsDao<CloudList> listDao;

	@Autowired
	public void setHardwareDao(FtsDao<Hardware> hardwareDao) {
		this.hardwareDao = hardwareDao;
	}

	@Autowired
	public void setSoftwareDao(FtsDao<Software> softwareDao) {
		this.softwareDao = softwareDao;
	}

	@Autowired
	public void setFileDao(FtsDao<CloudFile> fileDao) {
		this.fileDao = fileDao;
	}

	@Autowired
	public void setListDao(FtsDao<CloudList> listDao) {
		this.listDao = listDao;
	}

	@Override
	public void reIndex() throws InterruptedException {
		fileDao.reIndex();
	}

	private List<Asset> assetSearch(String keyword, long companyId) {
		String kw = keyword.trim().replace("\\s+", " ");
		List<Asset> assets = new ArrayList<Asset>();
		try {
			assets.addAll(hardwareDao.query(Hardware.class, new String[] { "fullName", "comment", "location" }, new String[] { "sn", "ip", "code",
					"financialCode" }, kw, "companyId", companyId));
			assets.addAll(softwareDao.query(Software.class, new String[] { "fullName", "comment" }, new String[] { "license" }, kw, "companyId", companyId));
		} catch (Exception e) {
			LoggerUtil.error(e);
		}
		return assets;
	}

	/**
	 * 按关键字搜索CloudFile和CloudList
	 * 
	 * @param keyword
	 * @return
	 */
	private List<CloudUnit> cloudSearch(String keyword) {
		String kw = keyword.trim().replace("\\s+", " ");
		List<CloudUnit> units = new ArrayList<CloudUnit>();
		try {
			units.addAll(fileDao.query(CloudFile.class, new String[] { "versionFilename", "brief", "tags.title" }, kw));
			units.addAll(listDao.query(CloudList.class, new String[] { "name", "tags.title" }, kw));
		} catch (Exception e) {
			LoggerUtil.error(e);
		}
		return units;
	}

	@Override
	public Map<String, List<FtsResultItem>> search(String catalog, String keyword, User user) {
		Map<String, List<FtsResultItem>> results = new LinkedHashMap<String, List<FtsResultItem>>();
		if (StringUtils.contains(catalog, "S")) {
			// 搜索服务类资源
		}
		if (StringUtils.contains(catalog, "A")) {
			// 搜索资产类资源
			List<Asset> assets = assetSearch(keyword, user.getCompanyId());
			if (null != assets && assets.size() > 0) {
				// 转换
				List<FtsResultItem> items = new ArrayList<FtsResultItem>();
				for (Asset asset : assets) {
					FtsResultItem item = new FtsResultItem();
					item.setId(asset.getId());
					item.setTitle(asset.getFullName());
					if (asset instanceof Hardware) {
						item.setType(PortalConstants.FTS_ASSET_HARDWARE);
					} else {
						item.setType(PortalConstants.FTS_ASSET_SOFTWARE);
					}
					items.add(item);
				}
				results.put("信息化资产", items);
			}
		}
		if (StringUtils.contains(catalog, "P")) {
			// 搜索项目类资源
		}
		if (StringUtils.contains(catalog, "M")) {
			// 搜索运维类资源
		}
		if (StringUtils.contains(catalog, "D")) {
			// 搜索文档类资源
			List<CloudUnit> units = cloudSearch(keyword);
			if (null != units && units.size() > 0) {
				List<FtsResultItem> items = new ArrayList<FtsResultItem>();
				for (CloudUnit unit : units) {
					// 转换
					if (FileUtil.checkPermission(unit, user)) {
						FtsResultItem item = new FtsResultItem();
						item.setId(unit.getId());
						if (unit instanceof CloudFile) {
							item.setTitle(unit.getOwner().getUsername() + " - " + ((CloudFile) unit).getVersionFilename());
							item.setType(PortalConstants.FTS_CLOUD_FILE);
						} else {
							item.setTitle(unit.getOwner().getUsername() + " - " + unit.getName());
							item.setType(PortalConstants.FTS_CLOUD_LIST);
						}
						items.add(item);
					}
				}
				results.put("资料库", items);
			}
		}
		return results;
	}

}
