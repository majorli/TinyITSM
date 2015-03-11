package com.jeans.tinyitsm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.jeans.tinyitsm.model.cloud.Tag;
import com.jeans.tinyitsm.model.portal.User;

/**
 * 资料云中的两类资源（文件或栏目）的统一接口，均为公共属性的Getter方法
 * 
 * @author Majorli
 *
 */
public interface CloudUnit extends Serializable {

	/**
	 * 资源Id
	 * 
	 * @return
	 */
	public long getId();

	/**
	 * 资源名称（文件名或栏目名）
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 资源的所有者，即文件的上传人或栏目的创建人，也是唯一拥有写权限（文件版本更新、文件删除、栏目删除、重命名、移动、修改读权限、修改标签、私有化/公共化、推送）
	 * 
	 * @return
	 */
	public User getOwner();

	/**
	 * 拥有资源读权限（列表、下载、预览）的用户集合，当资源为私有资源时，集合为空
	 * 
	 * @return
	 */
	public Set<User> getPermittedReaders();

	/**
	 * 是否为私有资源
	 * 
	 * @return
	 */
	public boolean isPrivateUnit();

	/**
	 * 资源的关联标签集合
	 * 
	 * @return
	 */
	public Set<Tag> getTags();

	/**
	 * 资源的创建时间，即文件的第一次上传时间或栏目的第一次创建时间
	 * 
	 * @return
	 */
	public Date getCreateTime();
	
	/**
	 * 资源的最后修改时间（文件版本更新、文件/栏目重命名）
	 * 
	 * @return
	 */
	public Date getLastUpdateTime();
}
