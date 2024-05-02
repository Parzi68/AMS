package com.project.ams.spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.ams.spring.model.Userdetails;

@Repository
public interface UserRepository extends JpaRepository<Userdetails, Long>{
	
	 // Custom query to find the maximum ID in the Asset table
    @Query("SELECT MAX(t.id) FROM Userdetails t")
    Long findMaxId();
	
    @Query(value="select * from userdetails where username=:username", nativeQuery = true)
	public Userdetails getDetails(String username);
    
	@Query(value = "SELECT * FROM user t where t.id= :id", nativeQuery = true)
	public List<Userdetails> user_list(long id);
	
	@Query(value = "SELECT COUNT(*) > 0 FROM userdetails t WHERE t.username = :username", nativeQuery = true)
	Boolean check_source(@Param("username") String username);

//	@Query(value = "SELECT * FROM userdetails u WHERE u.username = :username", nativeQuery = true)
//	User findByUsername(@Param("username") String username);
	
	@Query(value = "select CASE WHEN Count(c) > 0 then true else false end from userdetails c where username = :user", nativeQuery = true)
	public boolean checkIfUserExist(String user);
	
	@Query(value="select role from userdetails where username=:username", nativeQuery = true)
	public String getrole(String username);

	
}
