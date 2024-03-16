package com.project.ams.spring;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Mappingdata, Long> {
//	@Query("SELECT MAX(m.source_id) FROM MappingData m")
//	Long findMaxId();

	@Query(value = "SELECT * FROM mappingdata t where t.id= :id", nativeQuery = true)
	public List<Mappingdata> tag_list(long id);
	
	@Query(value = "SELECT COUNT(*) > 0 FROM mappingdata t WHERE t.reg_name = :reg_name", nativeQuery = true)
    Boolean check_source(@Param("reg_name") String reg_name);
	
//	@Query("SELECT reg_address FROM MappingData m WHERE m.reg_address = :reg_address")
//	int RegAddress(@Param("reg_address") int reg_address);
//	
//	@Query("SELECT reg_length FROM MappingData m WHERE m.reg_length = :reg_length")
//	int RegLength(@Param("reg_length") int reg_length);
}
