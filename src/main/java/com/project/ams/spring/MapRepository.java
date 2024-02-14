package com.project.ams.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<MappingData, Long> {
	@Query("SELECT MAX(m.source_id) FROM MappingData m")
    Long findMaxId();

}
