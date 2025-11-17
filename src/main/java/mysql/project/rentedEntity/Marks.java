package mysql.project.rentedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "marks")
public class Marks {

    @Id
    private int id;

    @Column(name = "student_id") // actual column name in DB
    private int studentId;

    @Column(name = "subject")
    private String subject;

    @Column(name="marks")
    private int marks;

    @Column(name="grade")
    private String grade;
}
