package com.bv.kafkaui.config;

import org.springframework.kafka.core.ConsumerFactory;

public class ConsumerGroup {

	private String groupId;
	private ConsumerFactory<Integer, String> kafkaConsumerFactory;
	private boolean inUse;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ConsumerFactory<Integer, String> getKafkaConsumerFactory() {
		return kafkaConsumerFactory;
	}

	public void setKafkaConsumerFactory(ConsumerFactory<Integer, String> kafkaConsumerFactory) {
		this.kafkaConsumerFactory = kafkaConsumerFactory;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(this.getClass() != obj.getClass())
			return false;
		ConsumerGroup cg = (ConsumerGroup) obj;
		
		if(this.getGroupId().equals(cg.getGroupId()))
			return true;
		
		return false;
	}	
	
}