package com.university.registration.dto;

import com.university.registration.enums.Gender;
import com.university.registration.enums.LecturerRank;

public class LecturerResponseDTO {
    private Long lecturerId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LecturerRank rank;
    private String phoneNumber;
    private DepartmentResponseDTO department;
    private UserResponseDTO user;

    public LecturerResponseDTO() {
    }

    public LecturerResponseDTO(Long lecturerId, String firstName, String lastName, Gender gender, LecturerRank rank, String phoneNumber, DepartmentResponseDTO department, UserResponseDTO user) {
        this.lecturerId = lecturerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.rank = rank;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.user = user;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LecturerRank getRank() {
        return rank;
    }

    public void setRank(LecturerRank rank) {
        this.rank = rank;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DepartmentResponseDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDTO department) {
        this.department = department;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}
