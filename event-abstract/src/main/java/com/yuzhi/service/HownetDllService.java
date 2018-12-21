package com.yuzhi.service;

import java.util.List;


public interface HownetDllService {
	/**
	 * 事件抽取
	 * @param input
	 * @return
	 */
	public List<Object> eventAbstract(String id, String input);
	/**
	 * 健康测试接口
	 * @return
	 */
	public String keepAlived();
}
