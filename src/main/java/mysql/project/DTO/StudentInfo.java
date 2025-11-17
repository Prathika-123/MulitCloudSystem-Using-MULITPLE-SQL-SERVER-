package mysql.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mysql.project.primaryEntity.Lecturer;
import mysql.project.rentedEntity.Marks;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentInfo {
    private Integer studentId;
    private String rollNo;
    private String name;
    private String gender;
    private String section;
    private String email;
    private List<Marks> marks;
    private List<Lecturer> lecturers;
}
