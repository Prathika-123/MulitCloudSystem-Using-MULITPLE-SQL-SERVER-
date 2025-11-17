package mysql.project.Controller;

import lombok.NoArgsConstructor;
import mysql.project.DTO.*;
import mysql.project.Services.StudentService;
import mysql.project.primaryEntity.Lecturer;
import mysql.project.secondaryEntity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.LdapContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@NoArgsConstructor

public class StudentController {
    @Autowired
    private  StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String rollNo = loginRequest.getRollNo();
        String email = loginRequest.getEmail();
        Optional<Student> student = studentService.validateStudent(rollNo, email);

        if (student.isPresent()) {
            Student st = student.get();
            return ResponseEntity.ok(new LoginResponse(true, st.getStudentId(), "Login Successful"));
        } else {
            return ResponseEntity.status(401).body(new LoginResponse(false, null, "Invalid credentials"));
        }
    }

    @GetMapping("/details/{studentId}")
    public ResponseEntity<?> getStudentDetails(@PathVariable Integer studentId){
        StudentInfo studentInfo=studentService.getStudentById(studentId);

        if(studentInfo!=null){
            return ResponseEntity.ok(studentInfo);
        }

        return ResponseEntity.status(404).body("Student not found");
    }

    @PostMapping("/lecturer/login")
    ResponseEntity<?> login(@RequestBody LecturerRequest lecturerRequest){
        String email=lecturerRequest.getEmail();
        List<Lecturer> lecturerList=studentService.validateLecturer(email);

        if(lecturerList.getFirst()!=null){
            return ResponseEntity.ok(new LecturerResponse(lecturerList.getFirst().getName(),email,"Login successful!!"));
        }

        return ResponseEntity.status(401).body(new LecturerResponse(null,null,"Check the details"));
    }

    @GetMapping("/lecturerDetails/{email}")
    public ResponseEntity<?> getLecturerDetails(@PathVariable String email){
        LecturerDetails details=studentService.lecturerDetails(email);

        return ResponseEntity.ok(details);
    }

}
