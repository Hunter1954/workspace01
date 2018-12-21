package com.yuzhi.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 分句解析句子及日志信息
 * @author Administrator
 *
 */
public class CurrentSentenceDTO {
	@JSONField(ordinal = 1)
	private String currentSentence;//当前句子
	@JSONField(ordinal = 2)
	private List<KeenageDataDTO> nodes;//分词结果
	@JSONField(ordinal = 3)
	private List<RulesDTO> rulesEntities;//日志信息
	
	//封装属性
	public List<KeenageDataDTO> getNodes() {
		return nodes;
	}
	public void setNodes(List<KeenageDataDTO> nodes) {
		this.nodes = nodes;
	}
	//无参构造
	public CurrentSentenceDTO() {
		super();
	}
	public String getCurrentSentence() {
		return currentSentence;
	}
	public void setCurrentSentence(String currentSentence) {
		this.currentSentence = currentSentence;
	}
	
	public List<RulesDTO> getRulesEntities() {
		return rulesEntities;
	}
	public void setRulesEntities(List<RulesDTO> rulesEntities) {
		this.rulesEntities = rulesEntities;
	}
	public CurrentSentenceDTO(String currentSentence, List<KeenageDataDTO> nodes, List<RulesDTO> rulesEntities) {
		super();
		this.currentSentence = currentSentence;
		this.nodes = nodes;
		this.rulesEntities = rulesEntities;
	}
	@Override
	public String toString() {
		return "CurrentSentenceDTO [currentSentence=" + currentSentence + ", nodes=" + nodes + ", rulesEntities="
				+ rulesEntities + "]";
	}
	
	
	
	
	
	
	
	
	
	
}
