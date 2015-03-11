package com.jeans.tinyitsm.action.cloud;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.EasyuiTreeNode;
import com.jeans.tinyitsm.model.view.CloudNoti;
import com.jeans.tinyitsm.service.cloud.CloudService;
import com.jeans.tinyitsm.service.cloud.CloudViewService;
import com.jeans.tinyitsm.service.portal.SubscriptionService;

public class CloudMainAction extends BaseAction<List<CloudNoti>> {

	private SubscriptionService subsService;
	private CloudViewService viewService;
	private CloudService service;

	@Autowired
	public void setSubsService(SubscriptionService subsService) {
		this.subsService = subsService;
	}

	@Autowired
	public void setViewService(CloudViewService viewService) {
		this.viewService = viewService;
	}

	@Autowired
	public void setService(CloudService service) {
		this.service = service;
	}

	/**
	 * 刷新当前用户的订阅消息，如果id有大于0的值传入则刷新至id之前，否则刷新当前最新10条
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "refr-subs", results = { @Result(type = "json", params = { "root", "data" }) })
	public String refreshSubscriptions() throws Exception {
		data = subsService.refresh(id, getCurrentUser());
		return SUCCESS;
	}

	/**
	 * 刷新更多10条当前用户的订阅消息，如果id有大于0的值传入则从id之后开始读取10条，否则读取最新10条
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "more-subs", results = { @Result(type = "json", params = { "root", "data" }) })
	public String moreSubscriptions() throws Exception {
		data = subsService.more(id, getCurrentUser());
		return SUCCESS;
	}

	private boolean newRss;

	public boolean isNewRss() {
		return newRss;
	}

	public void setNewRss(boolean newRss) {
		this.newRss = newRss;
	}

	private List<EasyuiTreeNode> topTenTree;

	public List<EasyuiTreeNode> getTopTenTree() {
		return topTenTree;
	}

	public void setTopTenTree(List<EasyuiTreeNode> topTenTree) {
		this.topTenTree = topTenTree;
	}

	/**
	 * 获取当前用户的资料库最新动态信息树
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "top-tens", results = { @Result(type = "json", params = { "root", "topTenTree" }) })
	public String loadTopTens() throws Exception {
		topTenTree = viewService.loadTopTens(getCurrentUser());
		return SUCCESS;
	}

	private long flId;

	public long getFlId() {
		return flId;
	}

	public void setFlId(long flId) {
		this.flId = flId;
	}

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 收藏一个来自TOP10的资料，操作结果用返回值表示：1=收藏成功；0=用户自身的资料；-1=用户已经收藏过这个资料；-2=资料或收藏夹无效
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "topten-favor", results = { @Result(type = "json", params = { "root", "code" }) })
	public String favor() throws Exception {
		int check = viewService.topTenFavoriteCheck(id, getCurrentUser());
		if (check == 1) {
			if (null != service.favor(id, flId, getCurrentUser())) {
				code = 1;
			} else {
				code = -2;
			}
		} else {
			code = check;
		}
		return SUCCESS;
	}
	
	/**
	 * 订阅一个来自TOP10的栏目，操作结果用返回值表示：1=订阅成功；0=用户自身的栏目；-1=用户已经订阅过这个栏目；-2=栏目无效
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "topten-subscribe", results = { @Result(type = "json", params = { "root", "code" }) })
	public String subscribe() throws Exception {
		int check = viewService.topTenSubscribeCheck(id, getCurrentUser());
		if (check == 1) {
			if (null != service.subscribe(id, getCurrentUser())) {
				code = 1;
			} else {
				code = -2;
			}
		} else {
			code = check;
		}
		return SUCCESS;
	}
}
