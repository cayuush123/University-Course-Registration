package com.university.registration.service;

import com.university.registration.entity.Timetable;
import com.university.registration.entity.Course;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.TimetableRepository;
import com.university.registration.repository.CourseRepository;
import com.university.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Timetable> getAllTimetables() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Collections.emptyList();
        }
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole() == Role.ADMIN) {
            return timetableRepository.findAll();
        } else if (user.getRole() == Role.LECTURER) {
            return timetableRepository.findTimetablesByLecturerEmail(email);
        } else if (user.getRole() == Role.STUDENT) {
            return timetableRepository.findTimetablesByStudentEmail(email);
        }
        return Collections.emptyList();
    }

    @Override
    public Timetable getTimetableById(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.LECTURER) {
                    if (timetable.getCourse().getLecturer() == null || !timetable.getCourse().getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this timetable entry.");
                    }
                } else if (user.getRole() == Role.STUDENT) {
                    List<Timetable> myTimetables = timetableRepository.findTimetablesByStudentEmail(email);
                    boolean isMine = myTimetables.stream().anyMatch(t -> t.getTimetableId().equals(id));
                    if (!isMine) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this timetable entry.");
                    }
                }
            }
        }
        return timetable;
    }

    @Override
    public List<Timetable> getTimetablesByCourseId(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.LECTURER) {
                    Course course = courseRepository.findById(courseId).orElse(null);
                    if (course == null || course.getLecturer() == null || !course.getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view timetables for this course.");
                    }
                } else if (user.getRole() == Role.STUDENT) {
                    List<Course> enrolledCourses = courseRepository.findEnrolledCoursesByStudentEmail(email);
                    boolean isEnrolled = enrolledCourses.stream().anyMatch(c -> c.getCourseId().equals(courseId));
                    if (!isEnrolled) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view timetables for this course.");
                    }
                }
            }
        }

        return timetableRepository.findByCourseCourseId(courseId);
    }

    @Override
    public Timetable saveTimetable(Timetable timetable) {
        if (timetable.getCourse() == null || timetable.getCourse().getCourseId() == null) {
            throw new IllegalArgumentException("Course must be specified");
        }

        Course course = courseRepository.findById(timetable.getCourse().getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + timetable.getCourse().getCourseId()));
        timetable.setCourse(course);

        return timetableRepository.save(timetable);
    }

    @Override
    public Timetable updateTimetable(Long id, Timetable timetable) {
        Timetable existing = getTimetableById(id);

        if (timetable.getCourse() != null && timetable.getCourse().getCourseId() != null) {
            Course course = courseRepository.findById(timetable.getCourse().getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + timetable.getCourse().getCourseId()));
            existing.setCourse(course);
        }

        existing.setDayOfWeek(timetable.getDayOfWeek());
        existing.setStartTime(timetable.getStartTime());
        existing.setEndTime(timetable.getEndTime());
        existing.setRoomNumber(timetable.getRoomNumber());

        return timetableRepository.save(existing);
    }

    @Override
    public void deleteTimetable(Long id) {
        Timetable existing = getTimetableById(id);
        timetableRepository.delete(existing);
    }
}
