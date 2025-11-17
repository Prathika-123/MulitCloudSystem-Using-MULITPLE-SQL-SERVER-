package mysql.project.rented.repo;

import mysql.project.rentedEntity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.error.Mark;

import java.util.List;

@Repository
public interface SecondaryRentMarksRepository extends JpaRepository<Marks,Integer> {

    List<Marks> findByStudentId(Integer studentId);

    Marks findByStudentIdAndSubject(int id, String subject);
}
