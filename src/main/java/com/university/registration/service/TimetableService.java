package com.university.registration.service;

import com.university.registration.entity.Timetable;
import java.util.List;

public interface TimetableService {
    List<Timetable> getAllTimetables();
    Timetable getTimetableById(Long id);
    List<Timetable> getTimetablesByCourseId(Long courseId);
    Timetable saveTimetable(Timetable timetable);
    Timetable updateTimetable(Long id, Timetable timetable);
    void deleteTimetable(Long id);
}
