package mysql.project.DTO;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    private String rollNo;
    private String email;

}
