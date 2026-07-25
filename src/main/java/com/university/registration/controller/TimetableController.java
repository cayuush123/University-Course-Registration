package com.university.registration.controller;

import com.university.registration.entity.Timetable;
import com.university.registration.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@CrossOrigin(origins = "*")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public List<Timetable> getAllTimetables() {
        return timetableService.getAllTimetables();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public Timetable getTimetableById(@PathVariable Long id) {
        return timetableService.getTimetableById(id);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public List<Timetable> getTimetablesByCourseId(@PathVariable Long courseId) {
        return timetableService.getTimetablesByCourseId(courseId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Timetable createTimetable(@RequestBody Timetable timetable) {
        return timetableService.saveTimetable(timetable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Timetable updateTimetable(@PathVariable Long id,
                                     @RequestBody Timetable timetable) {
        return timetableService.updateTimetable(id, timetable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
    }
}