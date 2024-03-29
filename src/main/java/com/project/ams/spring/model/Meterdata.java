package com.project.ams.spring.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Meterdata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Timestamp time;
	private String VR;
	private String VB;
	private String VY;
	private String VN;
	private String IR;
	private String IB;
	private String IY;
	private String IN;
	private String QR;
	private String QB;
	private String QY;
	private String QN;
	private String KR;
	private String KB;
	private String KY;
	private String KN;
	private String HZ;
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp timestamp) {
		time = timestamp;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVR() {
		return VR;
	}
	public void setVR(String vR) {
		VR = vR;
	}
	public String getVB() {
		return VB;
	}
	public void setVB(String vB) {
		VB = vB;
	}
	public String getVY() {
		return VY;
	}
	public void setVY(String vY) {
		VY = vY;
	}
	public String getVN() {
		return VN;
	}
	public void setVN(String vN) {
		VN = vN;
	}
	public String getIR() {
		return IR;
	}
	public void setIR(String iR) {
		IR = iR;
	}
	public String getIB() {
		return IB;
	}
	public void setIB(String iB) {
		IB = iB;
	}
	public String getIY() {
		return IY;
	}
	public void setIY(String iY) {
		IY = iY;
	}
	public String getIN() {
		return IN;
	}
	public void setIN(String iN) {
		IN = iN;
	}
	public String getQR() {
		return QR;
	}
	public void setQR(String qR) {
		QR = qR;
	}
	public String getQB() {
		return QB;
	}
	public void setQB(String qB) {
		QB = qB;
	}
	public String getQY() {
		return QY;
	}
	public void setQY(String qY) {
		QY = qY;
	}
	public String getQN() {
		return QN;
	}
	public void setQN(String qN) {
		QN = qN;
	}
	public String getKR() {
		return KR;
	}
	public void setKR(String kR) {
		KR = kR;
	}
	public String getKB() {
		return KB;
	}
	public void setKB(String kB) {
		KB = kB;
	}
	public String getKY() {
		return KY;
	}
	public void setKY(String kY) {
		KY = kY;
	}
	public String getKN() {
		return KN;
	}
	public void setKN(String kN) {
		KN = kN;
	}
	public String getHZ() {
		return HZ;
	}
	public void setHZ(String hZ) {
		HZ = hZ;
	}
	
}
