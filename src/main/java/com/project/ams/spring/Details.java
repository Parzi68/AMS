package com.project.ams.spring;

import jakarta.persistence.*;

@Entity
@Table(name = "RTUConfig")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",columnDefinition = "serial")
    private Long id;
    
    private int source_id;
    
    private int slave_id;

    @Column(name = "com-port")
	public String com_port;

    @Column(name = "baud-rate")
    public String baud_rate;

    @Column(name = "data-bits")
    public String data_bits;

    @Column(name = "stop-bits")
    private String stop_bits;

    @Column(name = "parity")
    String parity;

    @Column(name = "polling-interval")
    private int polling_interval;

    @Column(name = "time-format")
    private String time_format;

    @Column(name = "report-interval")
    private int report_interval;

    @Column(name = "set-time-format")
    private String set_time_format;
    


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSource_id() {
        return source_id;
    }

    public int getSlave_id() {
		return slave_id;
	}

	public void setSlave_id(int slave_id) {
		this.slave_id = slave_id;
	}

	public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getCom_port() {
        return com_port;
    }

    public void setCom_port(String com_port) {
        this.com_port = com_port;
    }

    public String getBaud_rate() {
        return baud_rate;
    }

    public void setBaud_rate(String baud_rate) {
        this.baud_rate = baud_rate;
    }

    public String getData_bits() {
        return data_bits;
    }

    public void setData_bits(String data_bits) {
        this.data_bits = data_bits;
    }

    public String getStop_bits() {
        return stop_bits;
    }

    public void setStop_bits(String stop_bits) {
        this.stop_bits = stop_bits;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public int getPolling_interval() {
        return polling_interval;
    }

    public String getParity() {
		return parity;
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
