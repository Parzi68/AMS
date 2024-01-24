package com.project.ams.spring;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "testing")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long source_id;

    @Column(name = "source_name")
    private String source_name;
    @Column(name = "application_name")
    private String application_name;
    @Column(name = "longitude")
    private int longitude;
    @Column(name = "latitude")
    private int latitude;
    @Column(name = "location_name")
    private String location_name;
    @Column(name = "protocol_type")
    private String protocol_type;
    @Column(name = "install_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate install_date;
    @Column(name = "modified_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate modified_date;

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

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
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







    public void setSource_id(Long sourceId) {
        this.source_id = sourceId;
    }

    public Long getSource_id() {
        return source_id;
    }
}
