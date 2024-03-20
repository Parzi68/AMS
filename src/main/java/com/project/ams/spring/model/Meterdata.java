package com.project.ams.spring.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Meterdata {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private LocalDate Created;
	private int source_id;
	private String value;
	public LocalDate getCreated() {
		return Created;
	}
	public void setCreated(LocalDate created) {
		Created = created;
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
