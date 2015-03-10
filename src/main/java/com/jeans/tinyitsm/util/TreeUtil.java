package com.jeans.tinyitsm.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jeans.tinyitsm.model.TreeNode;

public class TreeUtil {

	/**
	 * return null if not found
	 * 
	 * @param id
	 * @param list
	 * @return
	 */
	public static TreeNode seek(long id, List<? extends TreeNode> list) {
		if (list == null)
			return null;

		Iterator<? extends TreeNode> it = list.iterator();
		while (it.hasNext()) {
			TreeNode u = it.next();
			if (u.getId() == id)
				return u;
			else {
				TreeNode su = seek(id, u.getChildren());
				if (su != null)
					return su;
			}
		}
		return null;
	}

	/**
	 * return empty list if not found
	 * 
	 * @param name
	 * @param list
	 * @return
	 */
	public static List<TreeNode> seek(String keyword, List<? extends TreeNode> list) {
		List<TreeNode> ret = new ArrayList<TreeNode>();
		if (list == null)
			return ret;
		Iterator<? extends TreeNode> it = list.iterator();
		while (it.hasNext()) {
			TreeNode u = it.next();
			if (u.getName().indexOf(keyword) != -1 || u.getAlias().indexOf(keyword) != -1)
				ret.add(u);
			ret.addAll(seek(keyword, u.getChildren()));
		}
		return ret;
	}

	/**
	 * expandLevel: 0..n
	 * 
	 * @param tree
	 * @param expandLevel
	 */
	public static void expandOrgTree(List<? extends TreeNode> tree, int expandLevel) {
		expand(tree, expandLevel, 0);
	}

	private static void expand(List<? extends TreeNode> nodes, int expandLevel, int currentLevel) {
		Iterator<? extends TreeNode> it = nodes.iterator();
		while (it.hasNext()) {
			TreeNode n = it.next();
			if (currentLevel <= expandLevel)
				n.expand();
			else
				n.collapse();
			expand(n.getChildren(), expandLevel, currentLevel + 1);
		}
	}
}
