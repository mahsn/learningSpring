package course.springboot.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class Students implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty(message = "Firts name cannot be empty")
	private String first_name;
	@NotEmpty(message = "Last name cannot be empty")
	private String last_name;
	@NotEmpty(message = "Login cannot be empty")
	private String student_login;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	@NotEmpty(message = "Email cannot be empty")
	private String email;
	
	@OneToMany(mappedBy =  "students", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Phone> phones;
	
	@ManyToOne
	private GraduationCourses graduationCourses;
	
	@Enumerated(EnumType.STRING)
	private Levels studentLevels;
	
	@Lob
	private byte[] files;
	
	private String fileName;
	private String fileType;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getStudent_login() {
		return student_login;
	}
	public void setStudent_login(String student_login) {
		this.student_login = student_login;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	
	public List<Phone> getPhones() {
		return phones;
	}
	
	public GraduationCourses getGraduationCourses() {
		return graduationCourses;
	}
	
	public void setGraduationCourses(GraduationCourses graduationCourses) {
		this.graduationCourses = graduationCourses;
	}
	
	public void setStudentLevels(Levels studentLevels) {
		this.studentLevels = studentLevels;
	}
	
	public Levels getStudentLevels() {
		return studentLevels;
	}
	
	public void setFiles(byte[] files) {
		this.files = files;
	}
	
	public byte[] getFiles() {
		return files;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
