package com.example.demo.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Retrieves an entity by its email.
     * @param email must not be null or a empty string.
     * @return the entity with the given email or Optional#empty() if none found.
     */
    Optional<Student> findStudentByEmail(String email);

    /**
     * Returns whether an entity with the given email exists.
     * @param email must not be null or a empty string.
     * @return true if an entity with the given email exists, false otherwise.
     */
    boolean existsStudentByEmail(String email);
}
