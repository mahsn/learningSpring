package course.springboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import course.springboot.model.GraduationCourses;

@Repository
@Transactional
public interface GraduationCoursesRepository extends CrudRepository<GraduationCourses, Long> {

}
