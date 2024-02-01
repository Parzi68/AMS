package com.project.ams.spring;

import jakarta.persistence.*;

@Entity
@Table(name = "testing2")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id",columnDefinition = "serial")
    private Long source_id;

    @Column(name = "com_port")
    private int com_port;

    @Column(name = "baud_rate")
    private int baud_rate;

    @Column(name = "data_bits")
    private int data_bits;

    @Column(name = "stop_bits")
    private int stop_bits;

    @Column(name = "parity")
    private int parity;

    @Column(name = "polling_interval")
    private int polling_interval;

    @Column(name = "time_format")
    private String time_format;

    @Column(name = "report_interval", columnDefinition = "INTEGER USING report_interval::integer")
    private int report_interval;

    @Column(name = "set_time_format")
    private String set_time_format;

    public int getStarting_address() {
        return starting_address;
    }

    public void setStarting_address(int starting_address) {
        this.starting_address = starting_address;
    }

    public int getReq_quantity() {
        return req_quantity;
    }

    public void setReq_quantity(int req_quantity) {
        this.req_quantity = req_quantity;
    }

    private int starting_address;
    private int req_quantity;

    public Long getSource_id() {
        return source_id;
    }

    public void setSource_id(Long source_id) {
        this.source_id = source_id;
    }

    public int getCom_port() {
        return com_port;
    }

    public void setCom_port(int com_port) {
        this.com_port = com_port;
    }

    public int getBaud_rate() {
        return baud_rate;
    }

    public void setBaud_rate(int baud_rate) {
        this.baud_rate = baud_rate;
    }

    public int getData_bits() {
        return data_bits;
    }

    public void setData_bits(int data_bits) {
        this.data_bits = data_bits;
    }

    public int getStop_bits() {
        return stop_bits;
    }

    public void setStop_bits(int stop_bits) {
        this.stop_bits = stop_bits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getPolling_interval() {
        return polling_interval;
    }

    public void setPolling_interval(int polling_interval) {
        this.polling_interval = polling_interval;
    }

    public String getTime_format() {
        return time_format;
    }

    public void setTime_format(String time_format) {
        this.time_format = time_format;
    }

    public int getReport_interval() {
        return report_interval;
    }

    public void setReport_interval(int report_interval) {
        this.report_interval = report_interval;
    }

    public String getSet_time_format() {
        return set_time_format;
    }

    public void setSet_time_format(String set_time_format) {
        this.set_time_format = set_time_format;
    }
}
