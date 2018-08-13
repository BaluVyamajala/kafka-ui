package com.bv.kafkaui.model;

import java.util.Map;

public class TopicContent {

	private String consumerInstanceId;

	private Map<Integer, Content> partitionContent;
	
	public String getConsumerInstanceId() {
		return consumerInstanceId;
	}
	public void setConsumerInstanceId(String consumerInstanceId) {
		this.consumerInstanceId = consumerInstanceId;
	}
	public Map<Integer, Content> getPartitionContent() {
		return partitionContent;
	}
	public void setPartitionContent(Map<Integer, Content> partitionContent) {
		this.partitionContent = partitionContent;
	}

	
}
