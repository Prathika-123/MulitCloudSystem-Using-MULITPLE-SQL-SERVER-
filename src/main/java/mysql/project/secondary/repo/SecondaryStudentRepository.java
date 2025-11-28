package mysql.project.secondary.repo;

import mysql.project.secondaryEntity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SecondaryStudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByRollNoAndEmail(String rollNo, String email);

    Student findByStudentId(int id);

    List<Student> findByStudentClass(String section);

    List<Student> findAllByStudentIdIn(Set<Integer> studentIds);

    Student findByRollNo(String rollNo);
}
