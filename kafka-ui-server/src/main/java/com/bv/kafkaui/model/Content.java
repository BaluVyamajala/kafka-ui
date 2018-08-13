package com.bv.kafkaui.model;

import java.util.ArrayList;
import java.util.List;

public class Content {

	private List<Record> records = new ArrayList<Record>();

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> topicEvents) {
		this.records = topicEvents;
	}	
	
	
}
