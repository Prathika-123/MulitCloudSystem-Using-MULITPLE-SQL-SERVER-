package mysql.project.primaryEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="lecturers_updated")
public class Lecturer {

    @Id
    @Column(name = "lecturer_id") // actual column name in DB
    private int lecturerId;

    @Column(name="name")
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name="email")
    private String email;

    @Column(name = "section")
    private String section;
}
