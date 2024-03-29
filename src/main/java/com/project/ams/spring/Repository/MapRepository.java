package com.project.ams.spring.Repository;

import java.util.List;
import java.util.Vector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.ams.spring.model.Mappingdata;

@Repository
public interface MapRepository extends JpaRepository<Mappingdata, Long> {
//	@Query("SELECT MAX(m.source_id) FROM MappingData m")
//	Long findMaxId();

	@Query(value = "SELECT * FROM mappingdata t where t.id= :id", nativeQuery = true)
	public List<Mappingdata> tag_list(long id);

	@Query(value = "SELECT COUNT(*) > 0 FROM mappingdata t WHERE t.reg_address = :reg_address", nativeQuery = true)
	Boolean check_source(@Param("reg_address") int reg_address);

	@Query(value = "SELECT reg_name from mappingdata t where t.source_id =:source_id", nativeQuery = true)
	Vector get_RegName(@Param("source_id") Integer source_id);

	@Query(value = "SELECT reg_address, reg_length, reg_type, multiplier, point_type from mappingdata t where t.source_id =:source_id AND t.reg_name =:reg_name", nativeQuery = true)
	public String getAlltags(Integer source_id, String reg_name);

//	@Query(value = "SELECT COUNT(*) > 0 FROM mappingdata t WHERE t.reg_name = :reg_name AND t.source_id =:source_id")
//	public boolean existsBySourceIdAndRegName(@Param("source_id") int source_id,@Param("reg_name") String reg_name);

//	@Query("SELECT reg_address FROM MappingData m WHERE m.reg_address = :reg_address")
//	int RegAddress(@Param("reg_address") int reg_address);
//	
//	@Query("SELECT reg_length FROM MappingData m WHERE m.reg_length = :reg_length")
//	int RegLength(@Param("reg_length") int reg_length);
}
