package com.bv.kafkaui.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.kafka.support.TopicPartitionInitialOffset.SeekPosition;

import com.bv.kafkaui.config.KafkaConsumerConfig;
import com.bv.kafkaui.helper.consumer.ApplicationEventPublisherImpl;
import com.bv.kafkaui.helper.consumer.ConsumerInstance;
import com.bv.kafkaui.helper.consumer.ConsumerInstanceFactory;
import com.bv.kafkaui.helper.consumer.MessageListenerImpl;
import com.bv.kafkaui.model.Content;
import com.bv.kafkaui.model.PartitionContent;
import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.enums.ConsumerStatus;
import com.bv.kafkaui.model.enums.Position;

public class TopicEventHelper {

	ConsumerInstanceFactory consumerInstanceFactory = BeanUtil.getBean(ConsumerInstanceFactory.class);
	KafkaConsumerConfig kafkaConsumerConfig = BeanUtil.getBean(KafkaConsumerConfig.class);

	protected int idleThreadCount = 0;
	Logger logger = LoggerFactory.getLogger(TopicEventHelper.class);

	private ConsumerInstance consumerInstance;
	private TopicPartitionInitialOffset[] topicPartitionInitialOffsets;

	private String topicName;
	private List<PartitionOffset> partitionOffsets;
	private String consumerGroup;
	private Position position;
	private Integer noOfRecordsPerPartition;
	private List<String> searchKeys;

	public TopicEventHelper(String topicName, List<PartitionOffset> partitionOffset, String consumerGroup,
			Optional<Integer> noOfRecordsPerPartitionOptional, Position position,
			Optional<ConsumerInstance> consumerInstance, List<String> searchKeys) {
		initialize(topicName, partitionOffset, consumerGroup, noOfRecordsPerPartitionOptional, position,
				consumerInstance, searchKeys);
	}

	public void initialize(String topicName, List<PartitionOffset> partitionOffsets, String consumerGroup,
			Optional<Integer> noOfRecordsPerPartitionOptional, Position position,
			Optional<ConsumerInstance> consumerInstance, List<String> searchKeys) {
		this.topicName = topicName;
		this.partitionOffsets = partitionOffsets;
		this.consumerGroup = consumerGroup;
		this.position = position;
		this.noOfRecordsPerPartition = noOfRecordsPerPartitionOptional.orElse(new Integer(0));
		this.consumerInstance = consumerInstance.orElse(new ConsumerInstance());
		this.searchKeys = searchKeys;
		this.consumerInstance.markAsUsed();
	}

	public void startContainer() {
		consumerInstance.setConsumerGroup(Optional.of(this.consumerGroup));
		consumerInstance.setCustomMessageListener(
				new MessageListenerImpl<String, String >(topicName, partitionOffsets, noOfRecordsPerPartition, searchKeys));
		consumerInstance.setKafkaConsumerFactory(buildConsumerFactory());

		buildTopicPartitionInitialOffsets();

		ContainerProperties containerProperties = new ContainerProperties(topicPartitionInitialOffsets);
		containerProperties.setIdleEventInterval(2000L);
		containerProperties.setMessageListener(consumerInstance.getCustomMessageListener());
		consumerInstance.setContainer(new ConcurrentMessageListenerContainer<String, String>(
				consumerInstance.getKafkaConsumerFactory(), containerProperties));
		consumerInstance.getContainer().setConcurrency(partitionOffsets.size());
		consumerInstance.getContainer().setBeanName("TestContainerBeanName");
		consumerInstance.getContainer()
				.setApplicationEventPublisher(new ApplicationEventPublisherImpl(partitionOffsets));
		consumerInstanceFactory.getConsumerInstances().add(consumerInstance);
		consumerInstance.getContainer().start();
		if (this.noOfRecordsPerPartition == 0)
			pauseAllContainerThreadsFromIdleEvent();
		waitForConsumersToGoIdle(120);
	}

	public void resumeContainer() {

		((MessageListenerImpl<String,String>) consumerInstance.getCustomMessageListener())
				.reInitializePartitionContents(topicName, partitionOffsets, noOfRecordsPerPartition, position, searchKeys);
		((ApplicationEventPublisherImpl) consumerInstance.getContainer().getApplicationEventPublisher())
				.resumeAllParitionConsumers();
		//Wait for 10 seconds so that all threads are resumed then Pause all idle containers.
		waitForConsumersToGoIdle(10);
		//Wait for 120 seconds before giving up.
		waitForConsumersToGoIdle(120);
	}

	public void pauseAllContainerThreadsFromIdleEvent() {

		((ApplicationEventPublisherImpl) consumerInstance.getContainer().getApplicationEventPublisher())
				.pauseAllParitionOfIdleConsumers();
	}

	private void waitForConsumersToGoIdle(int loopCount) {
		boolean stopConsumer = false;
		try {
			do {
				Thread.sleep(1000);

				logger.trace("Main thread waiting for 1 second, loopCountDown:" + loopCount);

				if (checkIfAllParitionsPaused())
					stopConsumer = true;

				loopCount = loopCount - 1;
			} while (!stopConsumer && loopCount > 0);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(stopConsumer)
				logger.debug("All paritions are paused - Quitting.");
			else
				logger.debug("I am quitting! Tried enough, hopefully consumer picked up some events.");
		}
		if (!stopConsumer) {
			// Reason could be
			// 1. Consumer taking long time to fetch
			// 2. There is nothing more to consume
			logger.debug("Not all paritions are paused, forcing them to Pause state.");
			pauseAllContainerThreadsFromIdleEvent();
		}

	}

	private ConsumerFactory<String, String> buildConsumerFactory() {

		Map<String, Object> consumerConfigs = kafkaConsumerConfig.consumerConfigs();
		consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
		ConsumerFactory<String, String> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, String>(
				consumerConfigs);
		return kafkaConsumerFactory;
	}

	private void buildTopicPartitionInitialOffsets() {
		topicPartitionInitialOffsets = new TopicPartitionInitialOffset[partitionOffsets.size()];
		int index = 0;
		for (PartitionOffset partitionOffset : partitionOffsets) {
			topicPartitionInitialOffsets[index] = buildTopicPartitionInitialOffset(topicName,
					partitionOffset.getPartition(), noOfRecordsPerPartition);
			index = index + 1;
		}
	}

	private boolean checkIfAllParitionsPaused() {

		logger.debug("checkIfAllParitionsPaused");

		for (Map.Entry<Integer, ConsumerStatus> anEntry : ((MessageListenerImpl<String,String>) consumerInstance
				.getCustomMessageListener()).getPartitionStatus().entrySet()) {

			if (anEntry.getValue() != ConsumerStatus.PAUSED) {
				// Message Listener didn't trigger Pause may be because 1. no content in offset
				// 2. didn't yet start consuming(this may be a problem
				if (((ApplicationEventPublisherImpl) consumerInstance.getContainer().getApplicationEventPublisher())
						.getPartitionStatus().get(anEntry.getKey()) == ConsumerStatus.PAUSED)
					continue;
				return false;
			}
		}
		return true;
	}

	public TopicPartitionInitialOffset buildTopicPartitionInitialOffset(String topic, Integer partition,
			Integer noOfRecordsPerPartition) {

		if (noOfRecordsPerPartition.intValue() == 0)
			noOfRecordsPerPartition = 10;

		if (position == Position.LATEST) {
			noOfRecordsPerPartition = noOfRecordsPerPartition * -1;
			return new TopicPartitionInitialOffset(topic, partition, new Long(noOfRecordsPerPartition), false);
		}

		return new TopicPartitionInitialOffset(topic, partition, SeekPosition.BEGINNING);
	}

	public List<PartitionContent> getPartitionContents() {
		List<PartitionContent> partitionContents = new ArrayList<PartitionContent>();
		
		Map<Integer, Content> contentMap = ((MessageListenerImpl<String,String>) consumerInstance.getCustomMessageListener()).partitionContents;
		
		for (Entry<Integer, Content> partitionOffset : contentMap.entrySet()) {
						
			partitionContents.add(new PartitionContent(partitionOffset.getKey(), partitionOffset.getValue()));
		}
		return partitionContents;
	}
	
	

	public String getConsumerInstanceId() {

		return consumerInstance.getInstaceId().get();

	}

}