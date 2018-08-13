package com.bv.kafkaui.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bv.kafkaui.helper.TopicHelper;
import com.bv.kafkaui.helper.consumer.ConsumerInstanceFactory;
import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.Topic;
import com.bv.kafkaui.model.enums.BeginningOrEnding;

@RestController
public class KafkaTopicApi {

	@Autowired
	TopicHelper topicHelper;

	@Autowired
	ConsumerInstanceFactory consumerInstaceFactory;

	Logger logger = LoggerFactory.getLogger(KafkaTopicApi.class);

	@RequestMapping(path = "/api/topic/getAll")
	public List<Topic> getTopics() {
		return topicHelper.getParitionInfo();
	}

	@RequestMapping(path = "/api/topic", method = RequestMethod.GET)
	public Topic getTopic(String topicName) {
		return topicHelper.getParitionInfoForATopic(topicName);
	}

	@RequestMapping(path = "/api/topic", method = RequestMethod.POST)
	public void createTopic(String topicName) {
		topicHelper.createTopic(topicName);
	}

	@RequestMapping(path = "/api/topic", method = RequestMethod.DELETE)
	public void deleteTopic(String topicName) {
		topicHelper.deleteTopic(topicName);
	}

	@RequestMapping(path = "/api/topic/offsets", method = RequestMethod.POST)
	public List<PartitionOffset> getTopicOffsets(@RequestBody Topic kafkaTopic,
			@RequestParam BeginningOrEnding bgngOrEdng) {

		return topicHelper.getAllTopicOffsets(kafkaTopic, bgngOrEdng);

	}

}