package com.bv.kafkaui.model;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class Record {

	private long offset;
	private String key;
	@JsonRawValue
	private String value;

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Record(long offset, String key, String value) {
		super();
		this.offset = offset;
		this.key = key;
		this.value = value;
	}

	public Record() {

	}

}