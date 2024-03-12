package com.project.ams.spring;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<MappingData, Long> {
	@Query("SELECT MAX(m.source_id) FROM MappingData m")
	Long findMaxId();

	@Query(value = "SELECT * FROM tag_mapping t where t.id= :id", nativeQuery = true)
	public List<MappingData> tag_list(long id);
	
	@Query(value = "SELECT COUNT(*) > 0 FROM tag_mapping t WHERE t.reg-name = :reg-name", nativeQuery = true)
    Boolean check_source(@Param("reg-name") String reg_name);
//	@Query("SELECT reg_address FROM MappingData m WHERE m.source_id = 15")
//	int RegAddress();
//	
//	@Query("SELECT reg_length FROM MappingData m WHERE m.source_id = 15")
//	int RegLength();
}
