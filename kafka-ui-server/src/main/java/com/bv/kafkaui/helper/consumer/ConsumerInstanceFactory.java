package com.bv.kafkaui.helper.consumer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsumerInstanceFactory {

	private List<ConsumerInstance> consumerInstances = new ArrayList<ConsumerInstance>();
	
	Logger logger = LoggerFactory.getLogger(ConsumerInstanceFactory.class);

	public Optional<ConsumerInstance> getConsumerInstance(Optional<String> consumerInstanceId,
			Optional<String> consumerGroup) {

		if (!consumerInstanceId.isPresent() && !consumerGroup.isPresent())
			return Optional.empty();

		int index = consumerInstances.indexOf(new ConsumerInstance(consumerInstanceId, consumerGroup));

		if (index < 0)
			return Optional.empty();

		return Optional.of(consumerInstances.get(index));
	}

	public List<ConsumerInstance> getConsumerInstances() {
		return consumerInstances;
	}

	public void setConsumerInstances(List<ConsumerInstance> consumerInstances) {
		this.consumerInstances = consumerInstances;
	}

	public void removeInactiveConsumers() {

		Iterator<ConsumerInstance> iterator = consumerInstances.iterator();
		while (iterator.hasNext()) {
			ConsumerInstance consumerInstance = iterator.next();
			if ((new DateTime().getMillis() - consumerInstance.getLastUsedAt().getMillis()) > 180000) {
				consumerInstance.getContainer().stop();
				logger.debug("Consumer Group {} in active for more than 3 minutes, stopping it", consumerInstance.getConsumerGroup());
				iterator.remove();
			}
		}

	}

	public void printActiveConsumers() {
		
		consumerInstances.stream().forEach(entry -> logger.debug(entry.toString()));
		
	}

}