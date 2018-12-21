package com.yuzhi.dto;

import java.util.List;

/**
 * 返回结果的实体类
 * @author Hunter
 *
 */
public class ResultDTO {
	private String sentence;
	private List<ReturnJsonDTO> tags;
	public ResultDTO(String sentence, List<ReturnJsonDTO> tags) {
		super();
		this.sentence = sentence;
		this.tags = tags;
	}
	public ResultDTO() {
		super();
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public List<ReturnJsonDTO> getTags() {
		return tags;
	}
	public void setTags(List<ReturnJsonDTO> tags) {
		this.tags = tags;
	}
	@Override
	public String toString() {
		return "ResultDTO [sentence=" + sentence + ", tags=" + tags + "]";
	}
	
}
