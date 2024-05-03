package com.project.ams;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class Validation {

	private String string;
	private Set<String> setString;
	private Integer integer;
	private LocalDate fromDate;
	private LocalDate toDate;
	private LocalTime fromTime;
	private LocalTime toTime;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Set<String> getSetString() {
		return setString;
	}

	public void setSetString(Set<String> setString) {
		this.setString = setString;
	}

	public Integer getInteger() {
		return integer;
	}

	public void setInteger(Integer integer) {
		this.integer = integer;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public LocalTime getFromTime() {
		return fromTime;
	}

	public void setFromTime(LocalTime fromTime) {
		this.fromTime = fromTime;
	}

	public LocalTime getToTime() {
		return toTime;
	}

	public void setToTime(LocalTime toTime) {
		this.toTime = toTime;
	}

}
