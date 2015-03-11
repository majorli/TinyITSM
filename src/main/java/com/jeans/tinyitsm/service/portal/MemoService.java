package com.jeans.tinyitsm.service.portal;

import java.util.Date;

import com.jeans.tinyitsm.model.portal.Memo;
import com.jeans.tinyitsm.model.portal.User;

public interface MemoService {

	/**
	 * 获取一条备忘录消息
	 * 
	 * @param userId
	 *            用户Id
	 * @param memoDate
	 *            备忘日期
	 * @return
	 */
	public Memo getMemo(long userId, Date memoDate);

	/**
	 * 创建一条新备忘消息，如果传来的memo.text == blank则不创建，返回null
	 * 
	 * @param owner
	 * @param text
	 *            文本长度将被自动截取为255个字
	 * @param memoDate
	 * @return 保存失败返回null
	 */
	public Memo newMemo(User owner, String text, Date memoDate);

	/**
	 * 更改一条备忘消息，如果传来的memo.text == blank则保存为空字符串
	 * 
	 * @param owner
	 * @param text
	 *            文本长度将被自动截取为255个字
	 * @param memoDate
	 * 
	 * @return 更改后的备忘消息，保存失败返回null
	 */
	public Memo updateMemo(long id, String text);
}
