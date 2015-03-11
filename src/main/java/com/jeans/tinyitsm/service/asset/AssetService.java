package com.jeans.tinyitsm.service.asset;

import java.util.Map;

import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.Grid;

public interface AssetService {

	/**
	 * 获取一个Asset
	 * 
	 * @param id
	 *            资产id
	 * @param type
	 *            资产类型，硬件或软件
	 * @return
	 */
	public Asset loadAsset(long id, byte type);

	/**
	 * 分页获取资产
	 * 
	 * @param companyId
	 *            公司id
	 * @param catalog
	 *            资产类型或分类
	 * @param page
	 *            页码
	 * @param rows
	 *            每页记录数
	 * @return
	 */
	public Grid<AssetItem> loadAssets(long companyId, byte catalog, int page, int rows);

	/**
	 * 新增资产，资产类型包括硬件、软件和其他，由AssetConstants中的三个常量定义
	 * 
	 * @param properties
	 *            除id外的其他资产属性键值对
	 * @param type
	 *            资产类型
	 * @return
	 */
	public Asset newAsset(Map<String, Object> properties, byte type);
}
