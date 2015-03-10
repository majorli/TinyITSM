package com.jeans.tinyitsm.service.cloud;

import java.util.List;

import com.jeans.tinyitsm.model.EasyuiTreeNode;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.CloudGridRow;
import com.jeans.tinyitsm.model.view.Grid;

public interface CloudViewService {
	/**
	 * 根据指定的资料分类来获取某一用户的资料列表，数据分页
	 * 
	 * @param docType
	 * @param page
	 * @param rows
	 * @param user
	 * @return
	 */
	public Grid<CloudGridRow> loadFiles(int docType, int page, int rows, User user);

	/**
	 * 获取指定用户可见的资料库TOP10动态树
	 * 
	 * @param user
	 * @return
	 */
	public List<EasyuiTreeNode> loadTopTens(User user);

	/**
	 * 检查Top10中的某项资料是否可以收藏，已经收藏的和用户自身的项目不能收藏
	 * 
	 * @param id
	 * @param user
	 * @return 1=可以收藏，0=用户自己的资料，-1=已经收藏过的资料
	 */
	public int topTenFavoriteCheck(long id, User user);
	
	/**
	 * 检查Top10中的某个栏目是否可以订阅，已经订阅的和用户自身的栏目不能订阅
	 * 
	 * @param id
	 * @param user
	 * @return 1=可以订阅，0=用户自己的栏目，-1=已经订阅过的栏目
	 */
	public int topTenSubscribeCheck(long id, User user);
}
