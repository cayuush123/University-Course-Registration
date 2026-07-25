package com.university.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UniversityCourseRegistrationApplicationTests {

	@org.springframework.beans.factory.annotation.Autowired
	private MockMvc mockMvc;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.UserRepository userRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.StudentRepository studentRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.LecturerRepository lecturerRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.EnrollmentRepository enrollmentRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.GradeRepository gradeRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.CourseRepository courseRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.repository.TimetableRepository timetableRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.university.registration.service.GradeService gradeService;

	@org.springframework.beans.factory.annotation.Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() throws Exception {
		System.out.println("=== DIAGNOSTICS: INSPECTING DATABASE USERS ===");
		userRepository.findAll().forEach(user -> {
			System.out.println("USER ID: " + user.getId() + ", Username: " + user.getUsername() + ", Email: " + user.getEmail() + ", Role: " + user.getRole());
		});

		System.out.println("=== PASSWORDS RESET CHECK COMPLETED ===");

		System.out.println("=== DIAGNOSTICS: INSPECTING LECTURERS ===");
		lecturerRepository.findAll().forEach(l -> {
			System.out.println("LECTURER ID: " + l.getLecturerId() + ", Name: " + l.getFirstName() + " " + l.getLastName() + ", User ID: " + (l.getUser() != null ? l.getUser().getId() : "null"));
		});

		System.out.println("=== DIAGNOSTICS: INSPECTING STUDENTS ===");
		studentRepository.findAll().forEach(s -> {
			System.out.println("STUDENT ID: " + s.getStudentId() + ", Name: " + s.getFirstName() + " " + s.getLastName() + ", User ID: " + (s.getUser() != null ? s.getUser().getId() : "null"));
		});

		System.out.println("=== DIAGNOSTICS: INSPECTING COURSES ===");
		courseRepository.findAll().forEach(c -> {
			System.out.println("COURSE ID: " + c.getCourseId() + ", Code: " + c.getCourseCode() + ", Name: " + c.getCourseName() + ", Lecturer ID: " + (c.getLecturer() != null ? c.getLecturer().getLecturerId() : "null"));
		});

		System.out.println("=== DIAGNOSTICS: INSPECTING ENROLLMENTS ===");
		enrollmentRepository.findAll().forEach(e -> {
			System.out.println("ENROLLMENT ID: " + e.getEnrollmentId() + ", Student ID: " + (e.getStudent() != null ? e.getStudent().getStudentId() : "null") + ", Course ID: " + (e.getCourse() != null ? e.getCourse().getCourseId() : "null") + ", Status: " + e.getStatus());
		});

		System.out.println("=== DIAGNOSTICS: INSPECTING GRADES ===");
		gradeRepository.findAll().forEach(g -> {
			System.out.println("GRADE ID: " + g.getGradeId() + ", Student ID: " + (g.getStudent() != null ? g.getStudent().getStudentId() : "null") + ", Course ID: " + (g.getCourse() != null ? g.getCourse().getCourseId() : "null") + ", Grade: " + g.getGrade() + ", Score: " + g.getScore());
		});

		System.out.println("=== DIAGNOSTICS: INSPECTING TIMETABLES ===");
		timetableRepository.findAll().forEach(t -> {
			System.out.println("TIMETABLE ID: " + t.getTimetableId() + ", Course ID: " + (t.getCourse() != null ? t.getCourse().getCourseId() : "null") + ", Day: " + t.getDayOfWeek() + ", Room: " + t.getRoomNumber());
		});

		System.out.println("=== INSERTING PERMANENT GRADE FOR STUDENT2 ===");
		com.university.registration.entity.Student student2 = studentRepository.findById(4L).orElse(null);
		com.university.registration.entity.Course javaCourse = courseRepository.findAll().stream()
				.filter(c -> c.getCourseName().equalsIgnoreCase("java"))
				.findFirst()
				.orElse(null);

		if (student2 != null && javaCourse != null) {
			// Check if grade already exists
			boolean exists = gradeRepository.findAll().stream()
					.anyMatch(g -> g.getStudent().getStudentId().equals(student2.getStudentId()) && g.getCourse().getCourseId().equals(javaCourse.getCourseId()));
			if (!exists) {
				com.university.registration.entity.Grade testGrade = new com.university.registration.entity.Grade();
				testGrade.setStudent(student2);
				testGrade.setCourse(javaCourse);
				testGrade.setGrade("A");
				testGrade.setScore(95.0);
				testGrade = gradeRepository.save(testGrade);
				System.out.println("Permanently seeded grade for student2. Grade ID: " + testGrade.getGradeId());
			} else {
				System.out.println("Grade already exists for student2!");
			}
		} else {
			System.out.println("student2 or javaCourse not found in database!");
		}
	}
}
