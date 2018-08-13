package com.bv.kafkaui.helper.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

public class RecordFilterStrategyImpl<K, V> implements RecordFilterStrategy<K, V> {

	Logger logger = LoggerFactory.getLogger(RecordFilterStrategyImpl.class);

	List<String> arrayOfKeys = new ArrayList<String>();

	@Override
	public boolean filter(ConsumerRecord<K, V> consumerRecord) {

		if (arrayOfKeys.isEmpty())
			return false;

		for (String aKey : arrayOfKeys) {
			if (consumerRecord.key().toString().contains(aKey))
				return false;
		}
		logger.info("Filter Method called");

		// Yes - Discard the record
		return true;
	}

}