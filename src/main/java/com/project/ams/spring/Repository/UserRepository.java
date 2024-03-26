package com.project.ams.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ams.spring.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
}
