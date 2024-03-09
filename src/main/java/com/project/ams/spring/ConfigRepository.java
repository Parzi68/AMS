package com.project.ams.spring;

import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Details, Long> {
    @Query("SELECT MAX(d.source_id) FROM Details d")
    Long findMaxId();
    
//    @Query("SELECT com_port FROM Details d WHERE d.source_id = 28")
//    String comPort();
//    
//    @Query("SELECT baud_rate FROM Details d WHERE d.source_id = 28")
//    String baudRate();
//    
//    @Query("SELECT parity FROM Details d WHERE d.source_id = 28")
//    String parity();
//    
//    @Query("SELECT data_bits FROM Details d WHERE d.source_id = 28")
//    String dataBits();
//    
//    @Query("SELECT stop_bits FROM Details d WHERE d.source_id = 28")
//    String stopBits();
}
