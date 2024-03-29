package com.project.ams.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Mappingdata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "serial")
	private Long id;
	
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "serial")
	private int source_id;
	private String reg_name;
	private int reg_address;
	private int reg_length;
	private String reg_type;
	private int multiplier;
	private String element_name;
	private String point_type;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSource_id() {
		return source_id;
	}
	public void setSource_id(int source_id) {
		this.source_id = source_id;
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
	public int getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	public String getElement_name() {
		return element_name;
	}
	public void setElement_name(String element_name) {
		this.element_name = element_name;
	}
	public String getPoint_type() {
		return point_type;
	}
	public void setPoint_type(String point_type) {
		this.point_type = point_type;
	}
	
	

}
