package com.bv.kafkaui.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.Topic;
import com.bv.kafkaui.model.enums.BeginningOrEnding;

@Component
public class TopicHelper {

	@Autowired
	KafkaMessageListener kafkaMessageListener;

	@Autowired
	private AdminClient client;
	
	Logger logger = LoggerFactory.getLogger(TopicHelper.class);

	public List<Topic> getParitionInfo() {

		logger.debug("Get all Topics");
		List<Topic> kafkaTopics = new ArrayList<Topic>();

		Map<String, List<PartitionInfo>> topics = kafkaMessageListener.getParitionInfo();
		topics.forEach((k, v) -> {
			Topic kt = new Topic();
			kt.setTopic(k);
			List<Integer> partitions = new ArrayList<Integer>();
			v.forEach(part -> partitions.add(part.partition()));
			kt.setPartitions(partitions);
			kafkaTopics.add(kt);
		});
		return kafkaTopics;
	}

	public List<PartitionOffset> getAllTopicOffsets(Topic kafkaTopic, BeginningOrEnding bgngOrEdng) {

		List<TopicPartition> partitions = new ArrayList<TopicPartition>();

		for (Integer paritition : kafkaTopic.getPartitions()) {
			partitions.add(new TopicPartition(kafkaTopic.getTopic(), paritition));
		}

		List<PartitionOffset> partitionOffsets = kafkaMessageListener.getTopicOffsets(kafkaTopic.getTopic(), partitions,
				bgngOrEdng);

		return partitionOffsets;
	}

	public void createTopic(String topicName, Integer partitions) {
		int numPartitions = 9;
		if(partitions != null)
			numPartitions = partitions.intValue();

		NewTopic newTopic = new NewTopic(topicName, numPartitions, (short) 3);
		try {
			final CreateTopicsResult createTopicsResult = client.createTopics(Collections.singletonList(newTopic));
			 createTopicsResult.values().get(topicName).get();
		}catch( Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void deleteTopic(String topicName) {
		NewTopic newTopic = new NewTopic(topicName, 12, (short) 3);
		client.createTopics(Collections.singletonList(newTopic));
		client.deleteTopics(Collections.singletonList(topicName));
	}

	public Topic getParitionInfoForATopic(String topicName) {
		
		List<PartitionInfo> paritionInfo = kafkaMessageListener.getParitionInfoForATopic(topicName);

		Topic kt = new Topic();
		kt.setTopic(topicName);
		List<Integer> partitions = new ArrayList<Integer>();
		paritionInfo.forEach(part -> partitions.add(part.partition()));
		kt.setPartitions(partitions);
		
		return kt;
	}

}