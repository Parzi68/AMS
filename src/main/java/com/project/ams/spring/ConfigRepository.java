package com.project.ams.spring;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Details, Long> {
    @Query("SELECT MAX(d.source_id) FROM Details d")
    Long findMaxId();
}
