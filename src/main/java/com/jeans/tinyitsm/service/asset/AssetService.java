package com.jeans.tinyitsm.service.asset;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.AssetValidateResult;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.model.view.HardwareItem;

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
	 * 根据id集合和类型获取资产列表
	 * 
	 * @param ids
	 *            资产id集合
	 * @param type
	 *            资产类型，硬件或软件
	 * @return
	 */
	public List<Asset> loadAssets(Set<Long> ids, byte type);

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
	 * 获取所有指定类型的资产
	 * 
	 * @param companyId
	 *            公司id
	 * @param catalog
	 *            资产类型或分类
	 * @return
	 */
	public List<AssetItem> loadAssets(long companyId, byte catalog);

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

	/**
	 * 更新资产的属性值（除companyId, ownerId, state外）
	 * 
	 * @param ids
	 *            资产id集合
	 * @param type
	 *            资产类型
	 * @param props
	 *            新属性值Map
	 * @return 实际更新数量
	 */
	public int saveProps(Set<Long> ids, byte type, Map<String, Object> props);

	/**
	 * 检查指定公司下的硬件类资产数据一致性
	 * 
	 * @param companyId
	 *            公司id
	 * @return
	 */
	public List<AssetValidateResult> validate(long companyId);

	/**
	 * 检查指定资产可以转移为的下一个状态，多项资产一起检查时取各项交集，可能为空
	 * 
	 * @param ids
	 *            资产id集合
	 * @param type
	 *            资产类型
	 * @return
	 */
	public Set<Byte> checkNextStates(Set<Long> ids, byte type);

	/**
	 * 批量调整资产的使用状态
	 * 
	 * @param ids
	 *            资产id集合
	 * @param type
	 *            资产类型，硬件/软件
	 * @param newState
	 *            新的使用状态
	 * @param ownerId
	 *            责任人id（仅硬件类资产）
	 * @param keepOldOwner
	 *            是否保留原有的责任人（多个硬件类资产批量调整时）
	 * @return
	 */
	public int changeState(Set<Long> ids, byte type, byte newState, long ownerId, boolean keepOldOwner);

	/**
	 * 根据校验结果调整一项硬件类资产的数据
	 * 
	 * @param id
	 *            资产id
	 * @param adjustType
	 *            调整方式：0=调整责任人(需要提交owner属性); 1=回收资产; 2=根据责任人调整所属公司
	 * @param ownerId
	 *            责任人id
	 * @return
	 */
	public int adjust(long id, byte adjustType, long ownerId);

	/**
	 * 添加新的资产，资产的属性和记录数除所属公司id外全部通过props传入
	 * 
	 * @param props
	 *            资产的属性和记录数
	 * @param companyId
	 *            所属公司id
	 * @return
	 */
	public int createNewAssets(Map<String, Object> props, long companyId);

	/**
	 * 获取某个用户所配属的硬件设备类资产
	 * 
	 * @param owner
	 *            责任人的HRUnit对象
	 * @return
	 */
	public List<HardwareItem> loadEquipmentsByOwner(HRUnit owner);
}
