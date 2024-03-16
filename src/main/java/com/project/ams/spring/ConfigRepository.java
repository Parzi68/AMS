package com.project.ams.spring;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Rtuconfig, Long> {
//    @Query("SELECT MAX(d.source_id) FROM Rtuconfig d")
//    Long findMaxId();
    
	
    @Query(value="SELECT * FROM rtuconfig d where d.id= :id", nativeQuery = true)
    public List<Rtuconfig> details_list(long id);
    
    @Query(value = "SELECT COUNT(*) > 0 FROM rtuconfig t WHERE t.slave_id = :slave_id", nativeQuery = true)
    Boolean check_source(@Param("slave_id") int slave_id);
//    
//    @Query("SELECT com_port FROM rtuconfig d WHERE d.com_port = :com_port")
//    String comPort(@Param("com_port") String string);
//    
//    @Query("SELECT baud_rate FROM rtuconfig d WHERE d.baud_rate = :baud_rate")
//    String baudRate(@Param("baud_rate") String string);
//    
//    @Query("SELECT parity FROM rtuconfig d WHERE d.parity = :parity")
//    String parity(@Param("parity") String parity);
//    
//    @Query("SELECT data_bits FROM rtuconfig d WHERE d.data_bits = :data_bits")
//    String dataBits(@Param("data_bits") String data_bits);
//    
//    @Query("SELECT stop_bits FROM rtuconfig d WHERE d.stop_bits = :stop_bits")
//    String stopBits(@Param("stop_bits") String string);
}
