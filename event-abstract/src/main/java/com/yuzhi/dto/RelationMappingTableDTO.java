package com.yuzhi.dto;
/**
 * 触发词关系映射表实体类
 * @author Administrator
 *
 */
public class RelationMappingTableDTO {
	private String eventType;//触发词类型
	private String eventKey;//对应触发词key值
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public RelationMappingTableDTO(String eventType, String eventKey) {
		super();
		this.eventType = eventType;
		this.eventKey = eventKey;
	}
	public RelationMappingTableDTO() {
		super();
	}
	@Override
	public String toString() {
		return "RelationMappingTable [eventType=" + eventType + ", eventKey=" + eventKey + "]";
	}
	
	
}
