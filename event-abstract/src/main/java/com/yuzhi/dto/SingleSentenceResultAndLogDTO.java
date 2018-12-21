package com.yuzhi.dto;

import java.util.List;


/**
 * 单句解析结果及日志信息
 * 此实体类为com.yuzhi.service.impl.ApiRequestServiceImpl下的getReturnJsonResultAndPrintLog()方法专用
 * @author Administrator
 *
 */
public class SingleSentenceResultAndLogDTO {
	//解析结果列表
	private List<ReturnJsonDTO> returnJsonEntities;
	//解析日志列表
	private List<RulesDTO> rulesEntities;
	//分词列表
	private List<KeenageDataDTO> nodes;
	
	
	
	

	


	public SingleSentenceResultAndLogDTO(List<ReturnJsonDTO> returnJsonEntities, List<RulesDTO> rulesEntities,
			List<KeenageDataDTO> nodes) {
		super();
		this.returnJsonEntities = returnJsonEntities;
		this.rulesEntities = rulesEntities;
		this.nodes = nodes;
	}




	public SingleSentenceResultAndLogDTO() {
		super();
	}
	
	@Override
	public String toString() {
		return "SingleSentenceResultAndLog [returnJsonEntities=" + returnJsonEntities + ", rulesEntities="
				+ rulesEntities + ", nodes=" + nodes + "]";
	}

	public List<ReturnJsonDTO> getReturnJsonEntities() {
		return returnJsonEntities;
	}

	public void setReturnJsonEntities(List<ReturnJsonDTO> returnJsonEntities) {
		this.returnJsonEntities = returnJsonEntities;
	}

	public List<RulesDTO> getRulesEntities() {
		return rulesEntities;
	}
	public void setRulesEntities(List<RulesDTO> rulesEntities) {
		this.rulesEntities = rulesEntities;
	}
	public List<KeenageDataDTO> getNodes() {
		return nodes;
	}
	public void setNodes(List<KeenageDataDTO> nodes) {
		this.nodes = nodes;
	}
	
	
}
