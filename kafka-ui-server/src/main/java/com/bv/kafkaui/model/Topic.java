package com.bv.kafkaui.model;

import java.util.List;

public class Topic {

	private String topic;
	private List<Integer> partitions;

	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public List<Integer> getPartitions() {
		return partitions;
	}
	public void setPartitions(List<Integer> partitions) {
		this.partitions = partitions;
	}
	@Override
	public String toString() {
		return "KafkaTopic [topic=" + topic + ", partitions=" + partitions + "]";
	}
	

}