package com.project.ams.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset , Long> {

    // Custom query to find the maximum ID in the Asset table
    @Query("SELECT MAX(t.source_id) FROM Asset t")
    Long findMaxId();
}

