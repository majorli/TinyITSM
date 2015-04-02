package com.jeans.tinyitsm.service.it.enums;

import com.jeans.tinyitsm.service.EnumTitle;

public enum PhaseState implements EnumTitle {
	WaitingForStart, Started, Finished, OverdueToStart, OverdueToFinish;

	private String[] titles = new String[] { "等待", "执行", "完成", "推迟", "延期" };

	@Override
	public String getTitle() {
		return titles[this.ordinal()];
	}

}
