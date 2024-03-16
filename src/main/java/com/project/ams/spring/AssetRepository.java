package com.project.ams.spring;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vaadin.flow.component.textfield.TextField;

@Repository
public interface AssetRepository extends JpaRepository<Asset , Long> {

    // Custom query to find the maximum ID in the Asset table
    @Query("SELECT MAX(t.source_id) FROM Asset t")
    Long findMaxId();
    
    @Query(value="SELECT * FROM asset t WHERE t.id= :id", nativeQuery = true)
    public List<Asset> asset_list(long id);
    
    @Query(value = "SELECT COUNT(*) > 0 FROM asset t WHERE t.source_name = :source_name", nativeQuery = true)
    Boolean check_source(@Param("source_name") String source_name);

}

