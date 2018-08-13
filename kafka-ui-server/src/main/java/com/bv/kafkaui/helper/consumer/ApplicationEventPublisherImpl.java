package com.bv.kafkaui.helper.consumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.event.ConsumerPausedEvent;
import org.springframework.kafka.event.ListenerContainerIdleEvent;

import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.enums.ConsumerStatus;

public class ApplicationEventPublisherImpl implements ApplicationEventPublisher {

	Logger logger = LoggerFactory.getLogger(ApplicationEventPublisherImpl.class);

	private Map<Integer, ConsumerStatus> partitionStatus;

	public ApplicationEventPublisherImpl(List<PartitionOffset> partitionOffsets) {
		super();
		partitionStatus = new HashMap<Integer, ConsumerStatus>();
		for (PartitionOffset partitionOffset : partitionOffsets) {
			partitionStatus.put(partitionOffset.getPartition(), ConsumerStatus.RUN);
		}
	}

	public void resumeAllParitionConsumers() {

		for (Map.Entry<Integer, ConsumerStatus> anEntry : partitionStatus.entrySet()) {
			anEntry.setValue(ConsumerStatus.RESUME);
		}
	}
	
	public void pauseAllParitionOfIdleConsumers() {

		for (Map.Entry<Integer, ConsumerStatus> anEntry : partitionStatus.entrySet()) {
			anEntry.setValue(ConsumerStatus.PAUSE);
		}
	}
	@Override
	public void publishEvent(Object event) {

		if (event.getClass() == ConsumerPausedEvent.class) {
			ConsumerPausedEvent cpe = (ConsumerPausedEvent) event;
			logger.trace("ConsumerPausedEvent:" + cpe.toString());
		}

		if (event.getClass() == ListenerContainerIdleEvent.class) {
			ListenerContainerIdleEvent lcie = (ListenerContainerIdleEvent) event;
			logger.trace("ListenerContainerIdleEvent:" + lcie.toString());

			for (TopicPartition topicPartition : lcie.getTopicPartitions()) {
				logger.trace("Checking to see if Parition is ready for release" + lcie.toString());
				if (this.partitionStatus.get(topicPartition.partition()) == ConsumerStatus.RESUME) {
					logger.info("yes Releasing the parition" + topicPartition.toString());
					this.partitionStatus.put(topicPartition.partition(), ConsumerStatus.RUN);
					((ListenerContainerIdleEvent) event).getConsumer().resume(Arrays.asList(topicPartition));
				}
				if ((this.partitionStatus.get(topicPartition.partition()) == ConsumerStatus.PAUSE && lcie.getIdleTime() >= 5000) ||
						this.partitionStatus.get(topicPartition.partition()) == ConsumerStatus.PAUSE_NOW ) {
					logger.info("yes Pausing the parition" + topicPartition.toString());
					this.partitionStatus.put(topicPartition.partition(), ConsumerStatus.PAUSED);
					((ListenerContainerIdleEvent) event).getConsumer().pause(Arrays.asList(topicPartition));
				}

			}

		}
	}

	public Map<Integer, ConsumerStatus> getPartitionStatus() {
		return partitionStatus;
	}	

}