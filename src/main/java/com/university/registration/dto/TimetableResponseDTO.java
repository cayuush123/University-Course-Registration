package com.university.registration.dto;

import java.time.LocalTime;

public class TimetableResponseDTO {
    private Long timetableId;
    private CourseResponseDTO course;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;

    public TimetableResponseDTO() {
    }

    public TimetableResponseDTO(Long timetableId, CourseResponseDTO course, String dayOfWeek, LocalTime startTime, LocalTime endTime, String roomNumber) {
        this.timetableId = timetableId;
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
    }

    public Long getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(Long timetableId) {
        this.timetableId = timetableId;
    }

    public CourseResponseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseResponseDTO course) {
        this.course = course;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
