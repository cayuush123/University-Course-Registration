package com.university.registration.dto;

import com.university.registration.enums.Gender;
import com.university.registration.enums.LecturerRank;

public class LecturerRequestDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LecturerRank rank;
    private String phoneNumber;
    private Long departmentId;
    private UserRequestDTO user;

    public LecturerRequestDTO() {
    }

    public LecturerRequestDTO(String firstName, String lastName, Gender gender, LecturerRank rank, String phoneNumber, Long departmentId, UserRequestDTO user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.rank = rank;
        this.phoneNumber = phoneNumber;
        this.departmentId = departmentId;
        this.user = user;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public UserRequestDTO getUser() {
        return user;
    }

    public void setUser(UserRequestDTO user) {
        this.user = user;
    }
}
