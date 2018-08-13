package com.bv.kafkaui.model;

public class PartitionOffset {

	private Integer partition;
	private Long offset;

	public PartitionOffset() {
		super();
	}

	public PartitionOffset(Integer partition, Long offset) {
		super();
		this.partition = partition;
		this.offset = offset;
	}

	public Integer getPartition() {
		return partition;
	}

	public void setPartition(Integer partition) {
		this.partition = partition;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "PartitionOffset [partition=" + partition + ", offset=" + offset + "]";
	}

	
}