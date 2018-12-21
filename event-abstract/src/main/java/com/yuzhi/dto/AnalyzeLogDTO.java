package com.yuzhi.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 解析日志实体
 * @author Administrator
 *
 */
public class AnalyzeLogDTO {
	@JSONField(ordinal = 1)
	private String articleID;//当前文章ID
	@JSONField(ordinal = 2)
	private List<CurrentSentenceDTO> currentSentenceEntities;//每一个句子
	@Override
	public String toString() {
		return "AnalyzeLogDTO [articleID=" + articleID + ", currentSentenceEntities=" + currentSentenceEntities
				+ "]";
	}
	public AnalyzeLogDTO(String articleID, List<CurrentSentenceDTO> currentSentenceEntities) {
		super();
		this.articleID = articleID;
		this.currentSentenceEntities = currentSentenceEntities;
	}
	public AnalyzeLogDTO() {
		super();
	}
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public List<CurrentSentenceDTO> getCurrentSentenceEntities() {
		return currentSentenceEntities;
	}
	public void setCurrentSentenceEntities(List<CurrentSentenceDTO> currentSentenceEntities) {
		this.currentSentenceEntities = currentSentenceEntities;
	}
	
	
}
