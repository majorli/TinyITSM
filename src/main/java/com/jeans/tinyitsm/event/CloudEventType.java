package com.jeans.tinyitsm.event;

import com.jeans.tinyitsm.event.itsm.EventType;

public enum CloudEventType implements EventType {
	ListPushed,
	FilePushed,
	NewFileUploaded,
	FileRemovedInSubscribedList,
	SubscribedListRemoved,
	SubscribedListPermissionRemoved;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case ListPushed:
			title = "栏目推送";
			break;
		case FilePushed:
			title = "文件推送";
			break;
		case NewFileUploaded:
		case FileRemovedInSubscribedList:
			title = "订阅更新";
			break;
		case SubscribedListRemoved:
		case SubscribedListPermissionRemoved:
			title = "订阅失效";
			break;
		default:
			title = "";
		}
		return title;
	}

}
