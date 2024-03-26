package com.project.ams.spring.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Meterdata {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Timestamp Created;
	private int source_id;
	private String value;
	public Timestamp getCreated() {
		return Created;
	}
	public void setCreated(Timestamp timestamp) {
		Created = timestamp;
	}
	public int getSource_id() {
		return source_id;
	}
	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
