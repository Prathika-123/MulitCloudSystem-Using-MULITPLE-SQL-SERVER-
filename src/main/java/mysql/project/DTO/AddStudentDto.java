package mysql.project.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentDto {
   private String rollNo;
   private String name;
   private String gender;
   private String email;
   private String subject;
   private String lecturer;
   private String section;
   private int mark;
   private String grade;
}
