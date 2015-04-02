package com.jeans.tinyitsm.event;

import com.jeans.tinyitsm.event.itsm.EventType;

/**
 * 信息化项目事件类型枚举
 * <ul>
 * <li>NewSystemCreated: 新增信息系统，向信息系统所有Branch所属的公司发布公告，告知新增信息系统的名称、SystemStage和对应项目的ProjectStage
 * <li>ProjectConstructed: 项目终验，系统整体验收完成，进入正常运行使用阶段，向系统所有Branch所属的公司发布公告
 * <li>BranchConstructed: 单点验收，系统的一个Branch完成单点验收，向Branch所属公司和系统owner发布公告
 * <li>ProjectStageChanged: 阶段性进展，项目建设阶段发生改变时，向项目对应的信息系统所有Branch所属的公司发布公告
 * <li>ProjectMilestone: 里程碑，项目建设里程碑抵达，向项目对应的信息系统所有Branch所属的公司发布公告
 * <br><br>
 * @author Majorli
 *
 */
public enum ProjectEventType implements EventType {
	NewSystemCreated,
	ProjectConstructed,
	BranchConstructed,
	ProjectStageChanged,
	ProjectMilestone;

	@Override
	public String getTitle() {
		String title = null;
		switch(this) {
		case NewSystemCreated:
			title = "新增系统";
			break;
		case ProjectConstructed:
			title = "项目终验";
			break;
		case BranchConstructed:
			title = "单点验收";
			break;
		case ProjectStageChanged:
			title = "阶段性进展";
			break;
		case ProjectMilestone:
			title = "里程碑";
		}
		return title;
	}

}
