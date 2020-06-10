package course.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import course.springboot.model.Students;

@Repository
@Transactional
public interface StudentsRepository extends JpaRepository<Students, Long> {
	
	@Query("Select s from Students s where s.first_name like %?1%")
	public List<Students> findByName(String name);
}
