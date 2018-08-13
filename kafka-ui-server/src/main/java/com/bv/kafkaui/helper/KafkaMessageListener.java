package com.bv.kafkaui.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bv.kafkaui.config.KafkaConsumerConfig;
import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.enums.BeginningOrEnding;

@Component
public class KafkaMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);

	@Autowired
	KafkaConsumerConfig kafkaConsumerConfig;

	public Map<String, List<PartitionInfo>> getParitionInfo() {

		KafkaConsumer<String, String> defaultConsumer = new KafkaConsumer<String, String>(
				kafkaConsumerConfig.consumerConfigs());

		Map<String, List<PartitionInfo>> topics = defaultConsumer.listTopics();

		defaultConsumer.close();
		return topics;
	}

	public List<PartitionOffset> getTopicOffsets(String topicName, List<TopicPartition> partitions,
			BeginningOrEnding bgngOrEdng) {

		List<PartitionOffset> partitionOffsets = new ArrayList<PartitionOffset>();
		KafkaConsumer<String, String> defaultConsumer = new KafkaConsumer<String, String>(
				kafkaConsumerConfig.consumerConfigs());

		defaultConsumer.assign(partitions);

		if (bgngOrEdng == BeginningOrEnding.Beginning)
			defaultConsumer.seekToBeginning(partitions);
		else
			defaultConsumer.seekToEnd(partitions);

		defaultConsumer.poll(0);
		long offset;
		for (TopicPartition topicPartition : partitions) {
			offset = defaultConsumer.position(topicPartition);
			partitionOffsets
					.add(new PartitionOffset(topicPartition.partition(), new Long(offset)));
			logger.debug("offset:" + offset);
		}
		defaultConsumer.close();
		return partitionOffsets;
	}
	
	public List<PartitionInfo> getParitionInfoForATopic(String topicName) {

		KafkaConsumer<String, String> defaultConsumer = new KafkaConsumer<String, String>(
				kafkaConsumerConfig.consumerConfigs());

		List<PartitionInfo> partitionInfo = defaultConsumer.partitionsFor(topicName);

		defaultConsumer.close();
		return partitionInfo;
	}
}