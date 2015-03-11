package com.jeans.tinyitsm.service.cloud;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.CloudTreeNode;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.PropertyGridRow;

public interface CloudService {

	/**
	 * 获取基本的树结构，即四个根节点的一棵空树
	 * 
	 * @return
	 */
	public List<CloudTreeNode> getTreeOutline();

	/**
	 * 获取指定用户的某一资料库节点的子节点
	 * 
	 * @param id
	 *            资料库父节点所指向的实际资源的id
	 * @param type
	 *            资料库父节点类型(由CloudConstants定义)
	 * @param user
	 *            指定用户
	 * @return
	 */
	public List<CloudTreeNode> getChildren(long id, byte type, User user);

	/**
	 * 获取一个节点的属性列表
	 * 
	 * @param id
	 *            节点Id
	 * @param type
	 *            节点类型
	 * @return
	 */
	public Grid<PropertyGridRow> getProperties(long id, byte type);

	/**
	 * 获取一个文件的简介信息
	 * 
	 * @param id
	 *            文件id
	 * @return
	 */
	public String getBrief(long id);

	/**
	 * 获取一个文件或栏目的标签，多个标签用空格分隔
	 * 
	 * @param id
	 *            文件或栏目的id
	 * @param type
	 *            类型
	 * @return
	 */
	public String getTags(long id, byte type);

	/**
	 * 创建一个新栏目
	 * 
	 * @param owner
	 *            栏目所有者
	 * @param name
	 *            栏目名称
	 * @param tags
	 *            栏目标签，字符串形式，标签之间用空格隔开
	 * @param isPrivate
	 *            是否为非公开的栏目
	 * @param permittedReaderIds
	 *            公开栏目的访问权限，空集合表示所有IT人员可见
	 * @return
	 */
	public CloudTreeNode createList(User owner, String name, String tags, boolean isPrivate, Set<Long> permittedReaderIds);

	/**
	 * 创建一个新收藏夹
	 * 
	 * @param owner
	 *            收藏夹所有者
	 * @param name
	 *            收藏夹名称
	 * @return
	 */
	public CloudTreeNode createFavorList(User owner, String name);

	/**
	 * 删除一个栏目，如栏目有用户正在订阅则会触发事件(type == SubscribedListRemoved)<br>
	 * 删除栏目时是否同时删除其中文件，还是有文件则不能删除的业务逻辑由Action层控制，本方法只负责删除数据库中的栏目记录<br>
	 * 删除栏目后需要删除该栏目的所有订阅和所有推送
	 * 
	 * @param id
	 *            栏目id
	 * @return
	 */
	public boolean deleteList(long id);

	/**
	 * 删除一个收藏夹，可以删除非空收藏夹，但其中所有文件都将取消收藏，文件的收藏计数减1
	 * 
	 * @param id
	 *            收藏夹id
	 * @return
	 */
	public boolean deleteFavorList(long id);

	/**
	 * 停止订阅，栏目订阅计数减1
	 * 
	 * @param id
	 *            Subscirbe的id
	 * @return
	 */
	public boolean deleteRssList(long id);

	/**
	 * 取消收藏，文件的收藏计数减1
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteFavorite(long id);

	/**
	 * 删除文件，删除文件时需同时删除相关的所有推送和收藏，如果栏目有订阅则需要触发事件发送订阅更新消息
	 * 
	 * @param id
	 *            文件id
	 * @param rootPath
	 *            文件存放的根路径，{contextRoot}/{cloudRoot}，cloudRoot在配置文件中设置
	 * @param silent
	 *            是否静默删除，即不触发事件
	 * @return
	 * @throws IOException
	 */
	public boolean deleteFile(long id, String rootPath, boolean silent) throws IOException;

	/**
	 * 推送一个栏目或一个文件，触发事件(type == ListPushed 或 FilePushed)，推送时会检查用户的访问权限，不具备该资源访问权限的用户不会收到推送
	 * 
	 * @param type
	 *            推送资源的类型，CloudConstants.CLOUD_FILE或CloudConstants.CLOUD_LIST
	 * @param id
	 *            资源id
	 * @param userIds
	 *            目标用户ids
	 * @param ownerId
	 *            推送人id
	 * @return 收到推送的用户数
	 */
	public int push(byte type, long id, Set<Long> userIds, long ownerId);

	/**
	 * 订阅一个栏目，订阅前需检查访问权限，订阅后栏目的订阅计数加1
	 * 
	 * @param id
	 *            栏目id
	 * @return
	 */
	public CloudTreeNode subscribe(long id, User subscriber);

	/**
	 * 上传一个文件，文件上传时默认与其所在List的权限相同
	 * 
	 * @param file
	 *            要上传的文件的java.io.File对象
	 * @param listId
	 *            所在栏目id
	 * @param filename
	 *            实际文件名
	 * @param contentType
	 *            文件的ContentType
	 * @param rootPath
	 *            存储文件的根路径
	 * @return 文件的视图模型对象，用户前端显示
	 * @throws IOException
	 */
	public CloudTreeNode upload(File file, long listId, String filename, String contentType, String rootPath) throws IOException;

	/**
	 * 新文件上传后检索和发布订阅消息
	 * 
	 * @param files
	 *            新上传成功的文件节点列表
	 * @param listId
	 *            所属栏目id
	 */
	public void uploadRss(List<CloudTreeNode> files, long listId);

	/**
	 * 收藏一个文件，文件的收藏计数加1
	 * 
	 * @param id
	 *            文件id
	 * @param favorListId
	 *            所在收藏夹id
	 * @param user
	 *            当前用户
	 * @return
	 */
	public CloudTreeNode favor(long id, long favorListId, User user);

	/**
	 * 拒绝一个推送，如果接受推送的用户列表清空，则删除推送记录
	 * 
	 * @param id
	 *            推送信息的id，不是具体推送的内容的id
	 * @param user
	 *            接受待删除推送的用户
	 * @return
	 */
	public boolean refusePush(long id, User user);

	/**
	 * 移动一个文件/收藏的文件，返回新的节点，当源/目标不存在或源等于目标时，不执行移动，返回null<br>
	 * 移动文件时要根据目标栏目来重新设定访问权限，设定规则为取两者的交集，具体情况有： <li>目标文件夹为私有，则文件访问权限无论原先是什么都一律设为私有 <li>目标文件夹是全公开的，则保持文件原访问权限不变 <li>目标文件夹是部分访问权限的，而文件原本是私有的，那么保持私有不变 <li>
	 * 目标文件夹是部分访问权限的，而文件原本是全公开权限，那么照抄目标文件夹的权限 <li>剩余情况，即目标文件夹和文件原本都是部分访问权限，那么取二者的交集
	 * 
	 * @param id
	 *            源id(CloudFile.id/Favorite.id)
	 * @param targetId
	 *            目标id(CloudList.id/FavorList.id)
	 * @param type
	 *            移动类型(CloudConstants.FILE/CloudConstants.FAVOR_LIST_LINK)
	 * @return
	 */
	public CloudTreeNode move(long id, long targetId, byte type);

	/**
	 * 改变一个文件或栏目的访问权限，如果栏目权限的改变引起订阅变化则触发CloudEvent
	 * 
	 * @param owner
	 *            文件或栏目的所有人
	 * @param id
	 *            文件或栏目的id
	 * @param type
	 *            区分文件和栏目的类型
	 * @param isPrivate
	 *            目标权限，是否私有
	 * @param permittedReaderIds
	 *            目标权限，可访问的用户id集合，空集合表示完全公开
	 */
	public void changePermissions(User owner, long id, byte type, boolean isPrivate, Set<Long> permittedReaderIds);

	/**
	 * 保存一个文件的简介
	 * 
	 * @param id
	 *            文件id
	 * @param brief
	 *            简介，截断至不超过512个字
	 */
	public void saveBrief(long id, String brief);

	/**
	 * 保存一个文件或栏目的标签，如果已经有标签则进行合并
	 * 
	 * @param id
	 *            文件或栏目id
	 * @param type
	 *            区分文件或栏目的类型
	 * @param tags
	 *            标签，多个标签用空格分隔
	 */
	public void saveTags(long id, byte type, String tags);

	/**
	 * 文件、栏目、收藏夹重命名
	 * 
	 * @param id
	 *            文件、栏目或收藏夹的id
	 * @param type
	 *            区分类型
	 * @param name
	 *            新的名称，截断至不超过256个字
	 */
	public CloudTreeNode rename(long id, byte type, String name);

	/**
	 * 获取文件的版本，返回的版本信息放在一个Map中，关键字分别为majorVersion, minorVersion, versionType, version(已经字符串化的版本串)
	 * 
	 * @param id
	 *            文件id
	 * @return
	 */
	public Map<String, Object> getVersion(long id);

	/**
	 * 保存文件的版本
	 * 
	 * @param id
	 *            文件id
	 * @param majorVersion
	 *            主版本
	 * @param minorVersion
	 *            次版本
	 * @param versionType
	 *            版本类型
	 */
	public void saveVersion(long id, byte majorVersion, double minorVersion, String versionType);

	/**
	 * 准备单个文件下载，获取要下载的文件对象，返回用户可见的文件名<br>
	 * 获取的File对象必须经过存在性验证，不能返回null对象或不存在物理文件的对象，获取之前应检查文件的访问权限，返回的文件其下载计数加一
	 * 
	 * @param id
	 *            文件id
	 * @param rootPath
	 *            文件存储根目录
	 * @param isIE
	 *            前端是IE浏览器
	 * @param file
	 *            获取到的有权下载的实际存在的物理文件对象
	 * @param downloader
	 *            下载人员，用于测试是否有权限下载
	 * @return 下载文件名
	 * @throws UnsupportedEncodingException 
	 */
	public String prepareDownload(long id, String rootPath, boolean isIE, List<File> files, User downloader) throws UnsupportedEncodingException;

	/**
	 * 准备栏目或收藏夹下载，文件名即为栏目/收藏夹名称.zip
	 * 
	 * @param id
	 *            要下载的栏目或收藏夹的id
	 * @param type
	 *            id的类型(栏目或收藏夹)
	 * @param rootPath
	 *            文件存储根目录
	 * @param isIE
	 *            前端是IE浏览器
	 * @param entries
	 *            获取到的有权下载的实际存在的物理文件对象的Map，关键字为可见文件名
	 * @param downloader
	 *            下载人员，用于测试是否有权限下载
	 * @return 下载文件名
	 * @throws UnsupportedEncodingException
	 */
	public String prepareListDownload(long id, byte type, String rootPath, boolean isIE, Map<String, File> entries, User downloader) throws UnsupportedEncodingException;

	/**
	 * 准备下载，获取要下载的文件对象集合，返回文件名"多个文件打包下载.zip"作为下载文件名<br>
	 * 获取的File对象必须经过存在性验证，不能返回null对象或不存在物理文件的对象，获取之前应检查文件的访问权限，返回的文件其下载计数加一
	 * 
	 * @param ids
	 *            要下载的文件的ids
	 * @param rootPath
	 *            文件存储根目录
	 * @param isIE
	 *            前端是IE浏览器
	 * @param entries
	 *            获取到的有权下载的实际存在的物理文件对象的Map，关键字为可见文件名
	 * @param downloader
	 *            下载人员，用于测试是否有权限下载
	 * @return 下载文件名
	 * @throws UnsupportedEncodingException
	 */
	public String prepareMultiDownload(List<Long> ids, String rootPath, boolean isIE, Map<String, File> entries, User downloader) throws UnsupportedEncodingException;
}
