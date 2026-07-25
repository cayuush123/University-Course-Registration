package com.university.registration.config;

import com.university.registration.entity.Student;
import com.university.registration.entity.Lecturer;
import com.university.registration.entity.Course;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.repository.StudentRepository;
import com.university.registration.repository.LecturerRepository;
import com.university.registration.repository.UserRepository;
import com.university.registration.repository.CourseRepository;
import com.university.registration.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DatabaseInitializer: Resetting user passwords ===");
        userRepository.findAll().forEach(user -> {
            user.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(user);
            System.out.println("Set password for " + user.getUsername() + " to " + user.getUsername());
        });

        System.out.println("=== DatabaseInitializer: Checking for orphaned students ===");

        List<User> studentUsers = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .toList();

        for (User user : studentUsers) {
            Optional<Student> studentOpt = studentRepository.findByUserEmail(user.getEmail());
            if (studentOpt.isEmpty()) {
                System.out.println("User " + user.getUsername() + " (" + user.getEmail() + ") has no student profile.");
                
                List<Student> orphanedStudents = studentRepository.findAll().stream()
                        .filter(s -> s.getUser() == null)
                        .toList();

                if (!orphanedStudents.isEmpty()) {
                    Student targetStudent = null;
                    if (user.getUsername().equalsIgnoreCase("student1")) {
                        targetStudent = orphanedStudents.stream()
                                .filter(s -> s.getFirstName().equalsIgnoreCase("Asma"))
                                .findFirst()
                                .orElse(null);
                    } else if (user.getUsername().equalsIgnoreCase("student2")) {
                        targetStudent = orphanedStudents.stream()
                                .filter(s -> s.getFirstName().equalsIgnoreCase("Naima") && s.getStudentId() == 4L)
                                .findFirst()
                                .orElse(null);
                    } else if (user.getUsername().equalsIgnoreCase("naima")) {
                        targetStudent = orphanedStudents.stream()
                                .filter(s -> s.getFirstName().equalsIgnoreCase("Naima") && s.getStudentId() == 6L)
                                .findFirst()
                                .orElse(null);
                    } else if (user.getUsername().equalsIgnoreCase("muna")) {
                        targetStudent = orphanedStudents.stream()
                                .filter(s -> s.getFirstName().equalsIgnoreCase("Aisha"))
                                .findFirst()
                                .orElse(null);
                    }

                    if (targetStudent == null) {
                        // Fallback: take the first available orphaned student
                        targetStudent = orphanedStudents.get(0);
                    }

                    System.out.println("Linking user " + user.getUsername() + " to student " + targetStudent.getFirstName() + " " + targetStudent.getLastName() + " (ID: " + targetStudent.getStudentId() + ")");
                    targetStudent.setUser(user);
                    studentRepository.save(targetStudent);
                } else {
                    System.out.println("No orphaned students available to link to user " + user.getUsername());
                }
            }
        }

        System.out.println("=== DatabaseInitializer: Checking for orphaned lecturers ===");

        List<User> lecturerUsers = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.LECTURER)
                .toList();

        for (User user : lecturerUsers) {
            Optional<Lecturer> lecturerOpt = lecturerRepository.findByUserEmail(user.getEmail());
            if (lecturerOpt.isEmpty()) {
                System.out.println("User " + user.getUsername() + " (" + user.getEmail() + ") has no lecturer profile.");

                List<Lecturer> orphanedLecturers = lecturerRepository.findAll().stream()
                        .filter(l -> l.getUser() == null)
                        .toList();

                if (!orphanedLecturers.isEmpty()) {
                    Lecturer targetLecturer = null;
                    if (user.getUsername().equalsIgnoreCase("lecturer1")) {
                        targetLecturer = orphanedLecturers.stream()
                                .filter(l -> l.getFirstName().equalsIgnoreCase("farah"))
                                .findFirst()
                                .orElse(null);
                    }

                    if (targetLecturer == null) {
                        targetLecturer = orphanedLecturers.get(0);
                    }

                    System.out.println("Linking user " + user.getUsername() + " to lecturer " + targetLecturer.getFirstName() + " " + targetLecturer.getLastName() + " (ID: " + targetLecturer.getLecturerId() + ")");
                    targetLecturer.setUser(user);
                    lecturerRepository.save(targetLecturer);
                } else {
                    System.out.println("No orphaned lecturers available to link to user " + user.getUsername());
                }
            }
        }

        // To make sure lecturer1 sees their courses/data, assign Course 2 (java) to Farah Ahmed (ID: 2)
        // if it is currently assigned to Ahmed Ali (ID: 1).
        Course javaCourse = courseRepository.findById(2L).orElse(null);
        Lecturer farahLecturer = lecturerRepository.findById(2L).orElse(null);
        if (javaCourse != null && farahLecturer != null) {
            if (javaCourse.getLecturer() == null || javaCourse.getLecturer().getLecturerId() == 1L) {
                System.out.println("Assigning course " + javaCourse.getCourseName() + " (ID: 2) to Farah Ahmed (ID: 2) for dashboard verification.");
                javaCourse.setLecturer(farahLecturer);
                courseRepository.save(javaCourse);
            }
        }

        // Also assign Timetable 2 to Course 2 (java) so lecturer1 has a timetable to display
        com.university.registration.entity.Timetable timetable2 = timetableRepository.findById(2L).orElse(null);
        if (timetable2 != null && javaCourse != null) {
            if (timetable2.getCourse() == null || timetable2.getCourse().getCourseId() == 1L) {
                System.out.println("Assigning Timetable ID 2 to Course ID 2 (java)");
                timetable2.setCourse(javaCourse);
                timetableRepository.save(timetable2);
            }
        }
    }
}
