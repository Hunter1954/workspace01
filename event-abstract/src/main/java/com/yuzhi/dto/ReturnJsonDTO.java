package com.yuzhi.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 返回的json对象实体
 * @author Administrator
 *
 */
public class ReturnJsonDTO {
	@JSONField(ordinal = 1)
	private String type;//触发词类型
	@JSONField(ordinal = 2)
	private List<String> trigger;//触发词
	@JSONField(ordinal = 3)
	private List<List<Integer>> trigger_position;//触发词在句子中的位置
	@JSONField(ordinal = 4)
	private List<String> sub;//主体
	@JSONField(ordinal = 5)
	private List<String> obj;//客体
	@JSONField(ordinal = 6)
	private List<String> date;//时间
	@JSONField(ordinal = 7)
	private List<String> amount;//程度
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getTrigger() {
		return trigger;
	}
	public void setTrigger(List<String> trigger) {
		this.trigger = trigger;
	}
	
	public List<List<Integer>> getTrigger_position() {
		return trigger_position;
	}
	public void setTrigger_position(List<List<Integer>> trigger_position) {
		this.trigger_position = trigger_position;
	}
	public List<String> getSub() {
		return sub;
	}
	public void setSub(List<String> sub) {
		this.sub = sub;
	}
	public List<String> getObj() {
		return obj;
	}
	public void setObj(List<String> obj) {
		this.obj = obj;
	}
	public List<String> getDate() {
		return date;
	}
	public void setDate(List<String> date) {
		this.date = date;
	}
	public List<String> getAmount() {
		return amount;
	}
	public void setAmount(List<String> amount) {
		this.amount = amount;
	}
	
	
	
	
	public ReturnJsonDTO(String type, List<String> trigger, List<List<Integer>> trigger_position, List<String> sub,
			List<String> obj, List<String> date, List<String> amount) {
		super();
		this.type = type;
		this.trigger = trigger;
		this.trigger_position = trigger_position;
		this.sub = sub;
		this.obj = obj;
		this.date = date;
		this.amount = amount;
	}
	public ReturnJsonDTO() {
		super();
	}
	@Override
	public String toString() {
		return "ReturnJsonEntity [type=" + type + ", trigger=" + trigger + ", trigger_position=" + trigger_position
				+ ", sub=" + sub + ", obj=" + obj + ", date=" + date + ", amount=" + amount + "]";
	}
	
	
	
	
	
	
	
	
}
