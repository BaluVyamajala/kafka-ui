package com.bv.kafkaui.model;

public class PartitionContent {

	private Integer partition;
	private Content content;
		
	public PartitionContent(Integer partition, Content content) {
		super();
		this.partition = partition;
		this.content = content;
	}
	public Integer getPartition() {
		return partition;
	}
	public void setPartition(Integer partition) {
		this.partition = partition;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	
}