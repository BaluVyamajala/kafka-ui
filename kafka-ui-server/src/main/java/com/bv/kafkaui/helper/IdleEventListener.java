package com.bv.kafkaui.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Component;

@Component
public class IdleEventListener {

	Logger logger = LoggerFactory.getLogger(TopicEventHelper.class);

	@EventListener
	public void eventHandler(ListenerContainerIdleEvent event) {

		logger.info("Event Listener:" + event.toString());
		
	}
}