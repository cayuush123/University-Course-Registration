package com.university.registration.service;

import com.university.registration.entity.Lecturer;
import java.util.List;

public interface LecturerService {
    List<Lecturer> getAllLecturers();
    Lecturer getLecturerById(Long id);
    Lecturer saveLecturer(Lecturer lecturer);
    Lecturer updateLecturer(Long id, Lecturer lecturer);
    void deleteLecturer(Long id);
}
