package mysql.project.primary.repo;

import mysql.project.primaryEntity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimaryLecturerRepository extends JpaRepository<Lecturer,Integer> {
    List<Lecturer> findBySection(String studentClass);

    List<Lecturer> findByEmail(String email);

}
