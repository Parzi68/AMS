package com.project.ams.spring;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset , Long> {

    // Custom query to find the maximum ID in the Asset table
    @Query("SELECT MAX(t.source_id) FROM Asset t")
    Long findMaxId();
    
    @Query(value="SELECT * FROM Asset where id= :id", nativeQuery = true)
    public List<Asset> asset_list(long id);
}

