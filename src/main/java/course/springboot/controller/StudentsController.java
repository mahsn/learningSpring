package course.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import course.springboot.model.Phone;
import course.springboot.model.Students;
import course.springboot.repository.GraduationCoursesRepository;
import course.springboot.repository.PhoneRepository;
import course.springboot.repository.StudentsRepository;

@Controller
public class StudentsController {
	
	@Autowired
	private StudentsRepository studentsRepository;
	
	@Autowired
	private PhoneRepository phoneRepository;
	
	@Autowired
	private GraduationCoursesRepository graduationCoursesRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@RequestMapping(method = RequestMethod.GET, value="/students")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("save/students");
		modelAndView.addObject("studentObj", new Students());
		modelAndView.addObject("students", studentsRepository.findAll(PageRequest.of(0, 5)));
		modelAndView.addObject("graduationCourses", graduationCoursesRepository.findAll());
		return modelAndView;
	}
	
	@GetMapping("/pageable")
	public ModelAndView showingPagenable(@PageableDefault(size = 5) Pageable pageable,
			ModelAndView modelAndView) {
		Page<Students> page = studentsRepository.findAll(pageable);
		modelAndView.addObject("students", page);
		modelAndView.addObject("studentsObj", new Students());
		modelAndView.setViewName("save/students");
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="**/savedata", consumes = {"multipart/form-data"})
	public ModelAndView saveDatabase(@Valid Students students, BindingResult bindingResult, final MultipartFile file)
		throws IOException {
		
		students.setPhones(phoneRepository.getPhones(students.getId()));
		if (bindingResult.hasErrors()) {
			ModelAndView andView = new ModelAndView("save/students");
			andView.addObject("students", studentsRepository.findAll(PageRequest.of(0, 5)));
			andView.addObject("studentObj", students);
			andView.addObject("graduationCourses", graduationCoursesRepository.findAll());
			List<String> msg = new ArrayList<String>();
			
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			andView.addObject("msg", msg);
			return andView;
		}
		if (file.getSize() > 0) {
			students.setFiles(file.getBytes());
			students.setFileType(file.getContentType());
			students.setFileName(file.getOriginalFilename());
		}else {
			if (students.getId() != null && students.getId() > 0) {
				Students filesTemp = studentsRepository.findById(students.getId()).get();
				students.setFiles(filesTemp.getFiles());
				students.setFileType(filesTemp.getFileType());
				students.setFileName(filesTemp.getFileName());
			}
		}
		
		studentsRepository.save(students);
		ModelAndView andView = new ModelAndView("save/students");
		andView.addObject("students", studentsRepository.findAll(PageRequest.of(0, 5)));
		andView.addObject("studentObj", new Students());
		andView.addObject("graduationCourses", graduationCoursesRepository.findAll());
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/liststudents")
	public ModelAndView listStudents() {
		ModelAndView andView = new ModelAndView("save/students");
		andView.addObject("students", studentsRepository.findAll(PageRequest.of(0, 5)));
		andView.addObject("studentObj", new Students());
		andView.addObject("graduationCourses", graduationCoursesRepository.findAll());
		return andView;
	}
	
	@GetMapping("/edit/{studentId}")
	public ModelAndView edit(@PathVariable("studentId") Long studentId) {
		ModelAndView modelAndView = new ModelAndView("save/students");
		Optional<Students> student = studentsRepository.findById(studentId);
		modelAndView.addObject("studentObj", student.get());
		modelAndView.addObject("graduationCourses", graduationCoursesRepository.findAll());
		return modelAndView;
	}
	
	@GetMapping("/remove/{studentId}")
	public ModelAndView remove(@PathVariable("studentId") Long studentId) {
		studentsRepository.deleteById(studentId);
		ModelAndView modelAndView = new ModelAndView("save/students");
		modelAndView.addObject("students", studentsRepository.findAll(PageRequest.of(0, 5)));
		modelAndView.addObject("studentObj", new Students());
		modelAndView.addObject("graduationCourses", graduationCoursesRepository.findAll());
		return modelAndView;
	}
	
	@PostMapping("**/search")
	public ModelAndView searchStudent(@RequestParam("first_name") String first_name ) {
		ModelAndView andView = new ModelAndView("save/students");
		andView.addObject("students", studentsRepository.findByName(first_name));
		andView.addObject("studentObj", new Students());
		andView.addObject("graduationCourses", graduationCoursesRepository.findAll());
		return andView;
	}
	
	@GetMapping("**/search")
	public void downloadPdf(@RequestParam("first_name") String first_name,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Students> studentsList = new ArrayList<Students>();
		
		if (studentsList != null && !studentsList.isEmpty()) {
			studentsList = studentsRepository.findByName(first_name);
		} else {
			Iterable<Students> iterable = studentsRepository.findAll();
			for (Students student : iterable) {
				studentsList.add(student);
			}
		}
		
		byte [] pdf = reportUtil.generateReport(studentsList, "students", request.getServletContext());
		response.setContentLength(pdf.length);
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "report.pdf");
		response.setHeader(headerKey, headerValue);
		response.getOutputStream().write(pdf);
	}
	
	@GetMapping("**/downloadfile/{studentId}")
	public void downloadFiles(@PathVariable("studentId")  Long studentId, 
			HttpServletResponse response) throws IOException {
		Students students = studentsRepository.findById(studentId).get();
		if (students.getFileName() != null) {
			response.setContentLength(students.getFileName().length());
			response.setContentType(students.getFileName());
	
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", students.getFileName());
			response.setHeader(headerKey, headerValue);
			response.getOutputStream().write(students.getFiles());
		}
	}
	
	@GetMapping("/phones/{studentId}")
	public ModelAndView telephones(@PathVariable("studentId") Long studentId) {
		ModelAndView modelAndView = new ModelAndView("save/phones");
		Optional<Students> student = studentsRepository.findById(studentId);
		modelAndView.addObject("studentObj", student.get());
		modelAndView.addObject("phones", phoneRepository.getPhones(studentId));
		return modelAndView;
	}
	
	@PostMapping("**/phonesAdd/{studentId}")
	public ModelAndView phonesAdd(Phone phone, 
			@PathVariable("studentId") Long studentId) {
		ModelAndView modelAndView = new ModelAndView("save/phones");
		Students student = studentsRepository.findById(studentId).get();
		if (phone != null && (phone.getPhoneNumber() == null && phone.getPhoneNumber().isEmpty())) {
			ModelAndView andView = new ModelAndView("save/phones");
			andView.addObject("studentObj", student);
			andView.addObject("phones", phoneRepository.getPhones(studentId));
			List<String> msg = new ArrayList<String>();
			msg.add("Phone number must be informed");
			andView.addObject("msg", msg);
			
			return andView;
		}
		
		phone.setStudents(student);
		phoneRepository.save(phone);
		modelAndView.addObject("studentObj", student);
		modelAndView.addObject("phones", phoneRepository.getPhones(studentId));
		return modelAndView;
	}
	
	@GetMapping("/removePhone/{phoneId}")
	public ModelAndView removePhone(@PathVariable("phoneId") Long phoneId) {
		Students students = phoneRepository.findById(phoneId).get().getStudents();
		
		phoneRepository.deleteById(phoneId);
		ModelAndView modelAndView = new ModelAndView("save/phones");
		modelAndView.addObject("studentObj", students);
		modelAndView.addObject("phones", phoneRepository.getPhones(students.getId()));
		return modelAndView;
	}
}
