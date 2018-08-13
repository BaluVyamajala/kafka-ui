package com.bv.kafkaui.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bv.kafkaui.helper.consumer.ConsumerInstanceFactory;

@Component
public class ScheduledTasks {	
	
	@Autowired
	ConsumerInstanceFactory consumerInstanceFactory;
	
	Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	//Runs every 2 minutes
    @Scheduled(fixedRate = 120000)
    public void reportCurrentTime() {
    	
    	consumerInstanceFactory.removeInactiveConsumers();
    	
    }

	//Runs every 1 minute
    @Scheduled(fixedRate = 60000)
    public void printActiveConsumerGroups() {

    	consumerInstanceFactory.printActiveConsumers();
    	
    }

}
