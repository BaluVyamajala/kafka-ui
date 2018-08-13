package com.bv.kafkaui.helper.consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import com.bv.kafkaui.model.Content;
import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.Record;
import com.bv.kafkaui.model.enums.ConsumerStatus;
import com.bv.kafkaui.model.enums.Position;

public class MessageListenerImpl<K, V> implements ConsumerAwareMessageListener<String, String>, ConsumerSeekAware,
		RecordFilterStrategy<String, String> {

	Logger logger = LoggerFactory.getLogger(MessageListenerImpl.class);

	private String topicName;
	private Integer noOfRecordsPerPartition;
	public Map<Integer, Content> partitionContents;
	private Map<Integer, ConsumerStatus> partitionStatus;
	public Map<Integer, Long> partitionOffsetsSeek = new HashMap<Integer, Long>();
	// private Position position;
	public ConsumerStatus partitionIdleStatus = ConsumerStatus.RUN;
	List<String> searchKeys = new ArrayList<String>();

	public int counter = 0;

	public MessageListenerImpl(String topicName, List<PartitionOffset> partitionOffsets,
			Integer noOfRecordsPerPartition, List<String> searchKeys) {
		logger.trace("Message Listener Created");
		initialize(topicName, partitionOffsets, noOfRecordsPerPartition, searchKeys);
	}

	private void initialize(String topicName, List<PartitionOffset> partitionOffsets, Integer noOfRecordsPerPartition,
			List<String> searchKeys) {
		this.topicName = topicName;
		this.noOfRecordsPerPartition = noOfRecordsPerPartition;

		this.partitionContents = new HashMap<Integer, Content>();
		this.partitionStatus = new HashMap<Integer, ConsumerStatus>();
		this.searchKeys = searchKeys;

		for (PartitionOffset partitionOffset : partitionOffsets) {
			partitionContents.put(partitionOffset.getPartition(), new Content());
			partitionStatus.put(partitionOffset.getPartition(), ConsumerStatus.RUN);
		}
	}

	public void reInitializePartitionContents(String topicName, List<PartitionOffset> partitionOffsets,
			Integer noOfRecordsPerPartition, Position position, List<String> searchKeys) {

		partitionContents.clear();
		initialize(topicName, partitionOffsets, noOfRecordsPerPartition, searchKeys);
		// this.position = position;
		if (position == Position.OFFSET) {
			for (PartitionOffset partitionOffset : partitionOffsets) {
				if (partitionOffset.getOffset() != null)
					partitionOffsetsSeek.put(partitionOffset.getPartition(), partitionOffset.getOffset());
			}
		} else {
			for (PartitionOffset partitionOffset : partitionOffsets) {
				if (partitionOffset.getOffset() != null)
					partitionOffsetsSeek.put(partitionOffset.getPartition(), 0L);
			}
		}

	}

	@Override
	public void onMessage(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {

		if (filter(record)) {
			return;
		}

		if (partitionContents.get(record.partition()).getRecords().size() >= noOfRecordsPerPartition) {
			if (this.partitionStatus.get(record.partition()) != ConsumerStatus.PAUSED) {
				TopicPartition topicPartion = new TopicPartition(this.topicName, record.partition());
				Collection<TopicPartition> topicPartitions = new ArrayList<TopicPartition>();
				topicPartitions.add(topicPartion);
				logger.debug("Consumer Pause requested from Message Listener:" + record.partition());
				consumer.pause(topicPartitions);
				this.partitionStatus.put(record.partition(), ConsumerStatus.PAUSED);
			}
		} else {
			partitionContents.get(record.partition()).getRecords()
					.add(new Record(record.offset(), record.key(), record.value()));
			logger.debug("Thread {} partition: {} offset: {} key: {} value: {} ", Thread.currentThread().getId(),
					record.partition(), record.offset(), record.key(), record.value());
		}

	}

	@Override
	public void registerSeekCallback(ConsumerSeekCallback callback) {
		logger.debug("registerSeekCallback" + callback.toString());

	}

	@Override
	public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		logger.debug("onPartitionsAssigned" + assignments.toString());
	}

	@Override
	public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

		logger.trace("onIdleContainer" + assignments.toString());
		Long offset = null;
		for (Map.Entry<TopicPartition, Long> anEntry : assignments.entrySet()) {
			offset = partitionOffsetsSeek.get(anEntry.getKey().partition());
			if (offset != null) {
				if (offset != 0) {
					logger.trace("Seeking consumer parition {} to offset {} ", anEntry.getKey().partition(), offset);
					callback.seek(anEntry.getKey().topic(), anEntry.getKey().partition(), offset);
				} else {
					callback.seekToBeginning(anEntry.getKey().topic(), anEntry.getKey().partition());
				}
				partitionOffsetsSeek.remove(anEntry.getKey().partition());

			} else
				logger.trace("offset for Parition {} is null", anEntry.getKey().partition());

		}

	}

	public Map<Integer, ConsumerStatus> getPartitionStatus() {
		return partitionStatus;
	}

	@Override
	public boolean filter(ConsumerRecord<String, String> consumerRecord) {

		if (searchKeys == null || searchKeys.isEmpty())
			return false;

		for (String aKey : searchKeys) {
			logger.trace("Comparing {} of {} {} with {}", consumerRecord.key().toString(), consumerRecord.partition(),
					consumerRecord.offset(), aKey);
			if (consumerRecord.key().toString().contains(aKey)) {
				logger.trace("Matching record found");
				return false;
			}
		}
		logger.trace("Filter Method called");

		// Yes - Discard the record
		return true;
	}

}