package course.springboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import course.springboot.model.LoginUsers;

@Repository
@Transactional
public interface LoginUsersRepository  extends CrudRepository<LoginUsers, Long>{

	@Query("select u from LoginUsers u where u.username = ?1")
	public LoginUsers findUsersByUsername(String username);
	
}
