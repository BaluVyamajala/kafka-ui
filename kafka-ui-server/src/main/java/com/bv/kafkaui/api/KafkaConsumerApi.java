package com.bv.kafkaui.api;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bv.kafkaui.helper.TopicEventHelper;
import com.bv.kafkaui.helper.consumer.ConsumerInstance;
import com.bv.kafkaui.helper.consumer.ConsumerInstanceFactory;
import com.bv.kafkaui.model.ApiGenericResponse;
import com.bv.kafkaui.model.PartitionContent;
import com.bv.kafkaui.model.PartitionOffset;
import com.bv.kafkaui.model.enums.Position;

@RestController
public class KafkaConsumerApi {

	@Autowired
	ConsumerInstanceFactory consumerInstaceFactory;

	Logger logger = LoggerFactory.getLogger(KafkaConsumerApi.class);

	@RequestMapping(path = "/api/consumer/keepAlive", method = RequestMethod.POST)
	public void keepAlive(@RequestParam(required = true) String topicName,
			@RequestParam(required = true) String consumerGroup) {

		Optional<ConsumerInstance> consumerInstance = consumerInstaceFactory.getConsumerInstance(Optional.empty(),
				Optional.of(consumerGroup));
		if (consumerInstance.isPresent()) {
			consumerInstance.get().markAsUsed();
		} else
			throw new RuntimeException("Consumer Instance not found, consider re initializing");
	}

	@RequestMapping(path = "/api/consumer/init", method = RequestMethod.POST)
	public void initializeConsumerInstance(@RequestBody List<PartitionOffset> partitionOffset,
			@RequestParam(required = true) String topicName, @RequestParam(required = true) String consumerGroup) {

		Optional<ConsumerInstance> consumerInstance = consumerInstaceFactory.getConsumerInstance(Optional.empty(),
				Optional.of(consumerGroup));

		TopicEventHelper topicEventHelper = new TopicEventHelper(topicName, partitionOffset, consumerGroup,
				Optional.empty(), Position.LATEST, consumerInstance, null);

		if (consumerInstance.isPresent()) {
			// Consumer Instance is already present - Pause it
			topicEventHelper.pauseAllContainerThreadsFromIdleEvent();
		} else {
			// Consumer Instance is not present - create it and Pause it
			topicEventHelper.startContainer();
		}

		return;
	}

	@RequestMapping(path = "/api/consumer/fetch", method = RequestMethod.POST)
	public ResponseEntity<Object> getTopicContent(@RequestBody List<PartitionOffset> partitionOffset,
			@RequestParam(required = true) String topicName, @RequestParam(required = true) String consumerGroup,
			@RequestParam(required = true) Position position,
			@RequestParam(required = false) Optional<Integer> noOfRecordsPerPartition,
			@RequestParam(required = false) List<String> searchKeys) {

		Optional<ConsumerInstance> consumerInstance = consumerInstaceFactory.getConsumerInstance(Optional.empty(),
				Optional.of(consumerGroup));

		TopicEventHelper topicEventHelper = new TopicEventHelper(topicName, partitionOffset, consumerGroup,
				noOfRecordsPerPartition, position, consumerInstance, searchKeys);

		if (consumerInstance.isPresent())
			topicEventHelper.resumeContainer();
		else {
			ApiGenericResponse response = new ApiGenericResponse(true,
					"Consumer Instance not found for Consumer Group");
			return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<PartitionContent> partitionContent = topicEventHelper.getPartitionContents();
		return new ResponseEntity<Object>(partitionContent, HttpStatus.OK);
	}

}