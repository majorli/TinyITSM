package com.jeans.tinyitsm.event.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.event.CloudEvent;
import com.jeans.tinyitsm.event.CloudEventType;
import com.jeans.tinyitsm.event.itsm.EventListener;
import com.jeans.tinyitsm.model.CloudUnit;
import com.jeans.tinyitsm.model.cloud.CloudFile;
import com.jeans.tinyitsm.model.portal.Rss;
import com.jeans.tinyitsm.model.portal.Subscription;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.service.portal.MessageConstants;

@Component
public class CloudEventHandler implements EventListener<CloudEvent> {

	private BaseDao<Subscription> subDao;
	private BaseDao<Rss> rssDao;

	@Autowired
	public void setSubDao(BaseDao<Subscription> subDao) {
		this.subDao = subDao;
	}

	@Autowired
	public void setRssDao(BaseDao<Rss> rssDao) {
		this.rssDao = rssDao;
	}

	/**
	 * 处理CloudEvent，根据事件类型发布订阅消息，记录log日志<br>
	 * <li>ListPushed: 栏目推送
	 * <li>FilePushed: 文件推送
	 * <li>NewFileUploaded: 订阅更新(新文件上传)
	 * <li>FileRemovedInSubscribedList: 订阅更新(文件被删除)
	 * <li>SubscribedListRemoved：订阅失效(栏目已删除，订阅记录被删除)
	 * <li>SubscribedListPermissionRemoved：订阅失效(失去栏目访问权限，订阅记录仍然保留)
	 * 
	 * @param event
	 *            事件对象
	 */
	@Override
	@Transactional
	public void fired(CloudEvent event) {
		if (event.getUsers().size() == 0) {
			return;
		}
		CloudUnit unit = (CloudUnit) event.getTarget();
		CloudEventType type = event.getType();
		byte rssType = MessageConstants.RSS_SUBSCRIPTION;
		StringBuilder builder = new StringBuilder(type.getTitle());
		builder.append("，");
		switch (type) {
		case ListPushed:
			rssType = MessageConstants.RSS_LIST_PUSH;
			builder.append(unit.getOwner().getUsername()).append("向你推送了栏目\"").append(unit.getName()).append("\"");
			break;
		case FilePushed:
			rssType = MessageConstants.RSS_FILE_PUSH;
			builder.append(unit.getOwner().getUsername()).append("向你推送了文件\"").append(unit.getName()).append("\"");
			break;
		case NewFileUploaded:
			CloudFile file = (CloudFile) unit;
			builder.append("订阅的栏目\"").append(file.getList().getName()).append("\"上传了").append(event.getCount()).append("个新文件：\"").append(file.getName())
					.append("\"");
			if (event.getCount() > 1) {
				builder.append("等");
			}
			break;
		case FileRemovedInSubscribedList:
			builder.append("订阅的栏目\"").append(((CloudFile) unit).getList().getName()).append("\"中的文件\"").append(unit.getName()).append("\"已被删除");
			break;
		case SubscribedListRemoved:
			builder.append("订阅栏目\"").append(unit.getName()).append("\"已经失效，原因：栏目被删除");
			break;
		case SubscribedListPermissionRemoved:
			builder.append("订阅栏目\"").append(unit.getName()).append("\"已经失效，原因：失去访问权限");
			break;
		}
		Rss rss = new Rss();
		rss.setTargetId(unit.getId());
		rss.setText(builder.toString());
		rss.setType(rssType);
		rss.setPublishTime(new Date());
		rssDao.save(rss);
		for (User receiver : event.getUsers()) {
			Subscription sub = new Subscription();
			sub.setRss(rss);
			sub.setReceiver(receiver);
			sub.setNewRss(true);
			subDao.save(sub);
		}
	}
}
