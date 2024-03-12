package com.project.ams.spring;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "SourceManagement")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private Long id;

    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id", columnDefinition = "serial")
	private int source_id;

    @Column(name = "source_name")
    private String source_name;
    @Column(name = "application_name")
    private String application_name;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "location_name")
    private String location_name;
    @Column(name = "protocol_type")
    private String protocol_type;
    @Column(name = "install_date")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate install_date;
    @Column(name = "modified_date")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate modified_date;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    public LocalDate getInstall_date() {
        return install_date;
    }

    public void setInstall_date(LocalDate install_date) {
        this.install_date = install_date;
    }

    public LocalDate getModified_date() {
        return modified_date;
    }

    public void setModified_date(LocalDate modified_date) {
        this.modified_date = modified_date;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(String protocol_type) {
        this.protocol_type = protocol_type;
    }

    public void setSource_id(int sourceId) {
        this.source_id = sourceId;
    }

    public int getSource_id() {
        return source_id;
    }
}
