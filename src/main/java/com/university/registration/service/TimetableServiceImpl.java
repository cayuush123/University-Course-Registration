package com.university.registration.service;

import com.university.registration.entity.Timetable;
import com.university.registration.entity.Course;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.TimetableRepository;
import com.university.registration.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }

    @Override
    public Timetable getTimetableById(Long id) {
        return timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found with id: " + id));
    }

    @Override
    public List<Timetable> getTimetablesByCourseId(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
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
