package com.university.registration.repository;

import com.university.registration.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

    @Query("SELECT l FROM Lecturer l WHERE l.user.email = :email")
    Optional<Lecturer> findByUserEmail(@Param("email") String email);
}
