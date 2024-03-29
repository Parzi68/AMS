//package com.project.ams.spring.Repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.project.ams.spring.model.User;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long>{
//	@Query(value = "SELECT * FROM user t where t.id= :id", nativeQuery = true)
//	public List<User> user_list(long id);
//}
