package course.springboot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.persistence.Id;

import org.hibernate.annotations.ForeignKey;

@Entity
public class Phone   implements Serializable  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty(message = "Phone Number cannot be empty")
	private String phoneNumber;
	@NotEmpty(message = "Phone type cannot be empty")
	private String phoneType;
	
	@ForeignKey(name = "student_id")
	@ManyToOne
	private Students students;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Students getStudents() {
		return students;
	}

	public void setStudents(Students students) {
		this.students = students;
	}
}
