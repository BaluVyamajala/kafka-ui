package com.bv.kafkaui.helper.consumer;

import java.util.Optional;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

public class ConsumerInstance {

	private Optional<String> instaceId;
	private Optional<String> consumerGroup;
	private MessageListenerImpl<String, String> customMessageListener;
	private ConcurrentMessageListenerContainer<String, String> container;
	private ConsumerFactory<String, String> kafkaConsumerFactory;
	private DateTime lastUsedAt;

	public ConsumerInstance(Optional<String> instaceId, Optional<String> consumerGroup) {
		super();
		this.instaceId = instaceId;
		this.consumerGroup = consumerGroup;
	}

	public ConsumerInstance() {
		super();
		this.instaceId = Optional.of(UUID.randomUUID().toString());
	}

	public MessageListenerImpl<String, String> getCustomMessageListener() {
		return customMessageListener;
	}

	public void setCustomMessageListener(MessageListenerImpl<String, String> customMessageListener) {
		this.customMessageListener = customMessageListener;
	}

	public ConcurrentMessageListenerContainer<String, String> getContainer() {
		return container;
	}

	public void setContainer(ConcurrentMessageListenerContainer<String, String> container) {
		this.container = container;
	}

	public ConsumerFactory<String, String> getKafkaConsumerFactory() {
		return kafkaConsumerFactory;
	}

	public void setKafkaConsumerFactory(ConsumerFactory<String, String> kafkaConsumerFactory) {
		this.kafkaConsumerFactory = kafkaConsumerFactory;
	}

	public Optional<String> getInstaceId() {
		return instaceId;
	}

	public void setInstaceId(Optional<String> instaceId) {
		this.instaceId = instaceId;
	}

	public Optional<String> getConsumerGroup() {
		return consumerGroup;
	}

	public void setConsumerGroup(Optional<String> consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public DateTime getLastUsedAt() {
		return lastUsedAt;
	}


	public void markAsUsed() {
		this.lastUsedAt = DateTime.now();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;

		ConsumerInstance ci = (ConsumerInstance) obj;

		if (ci.getInstaceId().isPresent() && this.getInstaceId().isPresent()
				&& ci.getInstaceId().get().equals(this.getInstaceId().get()))
			return true;

		if (ci.getConsumerGroup().isPresent() && this.getConsumerGroup().isPresent()
				&& ci.getConsumerGroup().get().equals(this.getConsumerGroup().get()))
			return true;

		return false;
	}

	@Override
	public String toString() {
		return "ConsumerInstance [instaceId=" + instaceId + ", consumerGroup=" + consumerGroup
				+ ", customMessageListener=" + customMessageListener + ", container=" + container
				+ ", kafkaConsumerFactory=" + kafkaConsumerFactory + ", lastUsedAt=" + lastUsedAt + "]";
	}
	
}