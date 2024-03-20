package com.project.ams.spring.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.ams.spring.model.Rtuconfig;

@Repository
public interface ConfigRepository extends JpaRepository<Rtuconfig, Long> {
//    @Query("SELECT MAX(d.source_id) FROM Rtuconfig d")
//    Long findMaxId();
    
	
    @Query(value="SELECT * FROM rtuconfig d where d.id= :id", nativeQuery = true)
    public List<Rtuconfig> details_list(long id);
    
    @Query(value = "SELECT COUNT(*) > 0 FROM rtuconfig t WHERE t.slave_id = :slave_id", nativeQuery = true)
    Boolean check_source(@Param("slave_id") int slave_id);
    
	@Query(nativeQuery = true, value ="SELECT t.com_port from rtuconfig t")
    public Vector available_comPort();
    
    @Query(value = "SELECT slave_id from rtuconfig t where t.com_port =:com_port", nativeQuery = true)
    public Vector get_mapped_source(String com_port);
    
    @Query(value = "SELECT source_id from rtuconfig t where t.slave_id =:slave_id", nativeQuery = true)
    Integer get_source_name(int slave_id);
    
    @Query(value = "SELECT baud_rate , data_bits , stop_bits , parity from rtuconfig t where t.slave_id =:slave_id AND t.com_port =:com_port", nativeQuery = true)
    public String modbus_rtu_configuration(@Param("slave_id")int slave_id,@Param("com_port") String com_port);
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
