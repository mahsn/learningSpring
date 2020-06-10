package course.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import course.springboot.model.Phone;

@Repository
@Transactional
public interface PhoneRepository  extends CrudRepository<Phone, Long>{
	
	@Query("Select p from Phone p where p.students.id = ?1")
	public List<Phone> getPhones(Long studentId);
}
