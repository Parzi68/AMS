package com.project.ams.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<MappingData, Long> {
	@Query("SELECT MAX(m.source_id) FROM MappingData m")
    Long findMaxId();

	@Query("SELECT reg_address FROM MappingData m WHERE m.source_id = 15")
	int RegAddress();
	
	@Query("SELECT reg_length FROM MappingData m WHERE m.source_id = 15")
	int RegLength();
}
