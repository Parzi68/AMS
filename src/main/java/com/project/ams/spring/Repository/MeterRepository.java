package com.project.ams.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ams.spring.model.Meterdata;

@Repository
public interface MeterRepository extends JpaRepository<Meterdata, Long>{
	
}
