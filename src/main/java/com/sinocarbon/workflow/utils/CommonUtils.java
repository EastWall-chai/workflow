package com.sinocarbon.workflow.utils;

import java.util.List;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;

public class CommonUtils {

	/**
	 * 将字符串集合 转换成 字符串，用,隔开
	 * [1, 2, 3] => "1,2,3"
	 * 空集合 返回 空串
	 * @param list
	 * @return
	 */
	public static String praseListToString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		if(CollectionUtils.isEmpty(list)) {
			return "";
		}
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if(i != list.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
