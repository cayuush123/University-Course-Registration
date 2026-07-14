package com.university.registration.repository;

import com.university.registration.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByCourseCourseId(Long courseId);
}
