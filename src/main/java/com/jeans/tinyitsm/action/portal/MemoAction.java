package com.jeans.tinyitsm.action.portal;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.model.portal.Memo;
import com.jeans.tinyitsm.service.portal.MemoService;

public class MemoAction extends TinyAction {

	private MemoService memoService;

	@Autowired
	public void setMemoService(MemoService memoService) {
		this.memoService = memoService;
	}

	private Date memoDate;
	private Memo memo;

	public Date getMemoDate() {
		return memoDate;
	}

	public void setMemoDate(Date memoDate) {
		this.memoDate = memoDate;
	}

	public Memo getMemo() {
		return memo;
	}

	public void setMemo(Memo memo) {
		this.memo = memo;
	}

	@Action(value = "load-memo", results = { @Result(type = "json", params = { "root", "memo" }) })
	public String loadMemo() throws Exception {
		memo = memoService.getMemo(getCurrentUserId(), memoDate);
		return SUCCESS;
	}

	private long id;
	private String text;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Action(value = "save-memo", results = { @Result(type = "json", params = { "root", "memo" }) })
	public String saveMemo() throws Exception {
		if (id <= 0) {
			memo = memoService.newMemo(getCurrentUser(), text, memoDate);
		} else {
			memo = memoService.updateMemo(id, text);
		}
		return SUCCESS;
	}
}
