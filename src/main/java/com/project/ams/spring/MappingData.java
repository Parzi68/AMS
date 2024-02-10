package com.project.ams.spring;

import jakarta.persistence.Entity;

@Entity
public class MappingData {
	private long id;
	private String reg_name;
	private int reg_address;
	private int reg_length;
	private String reg_type;
	private String multiplier;
	private String element_name;
	private int point_type;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getReg_name() {
		return reg_name;
	}
	public void setReg_name(String reg_name) {
		this.reg_name = reg_name;
	}
	public int getReg_address() {
		return reg_address;
	}
	public void setReg_address(int reg_address) {
		this.reg_address = reg_address;
	}
	public int getReg_length() {
		return reg_length;
	}
	public void setReg_length(int reg_length) {
		this.reg_length = reg_length;
	}
	public String getReg_type() {
		return reg_type;
	}
	public void setReg_type(String reg_type) {
		this.reg_type = reg_type;
	}
	public String getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(String multiplier) {
		this.multiplier = multiplier;
	}
	public String getElement_name() {
		return element_name;
	}
	public void setElement_name(String element_name) {
		this.element_name = element_name;
	}
	public int getPoint_type() {
		return point_type;
	}
	public void setPoint_type(int point_type) {
		this.point_type = point_type;
	}
	
	

}
