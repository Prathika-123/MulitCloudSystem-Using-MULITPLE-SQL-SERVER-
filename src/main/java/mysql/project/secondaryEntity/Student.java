package mysql.project.secondaryEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id") // actual column name in DB
    private Integer studentId;

    @Column(name = "roll_no")
    private String rollNo;

    @Column(name = "name")
    private String name;

    @Column(name="gender")
    private String gender;

    @Column(name = "class")
    private String studentClass;

    @Column(name = "email")
    private String email;

}
