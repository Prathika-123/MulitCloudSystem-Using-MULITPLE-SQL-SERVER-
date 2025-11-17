package mysql.project.Services;

import lombok.NoArgsConstructor;
import mysql.project.DTO.LecturerDetails;
import mysql.project.DTO.StudentInfo;
import mysql.project.primaryEntity.Lecturer;
import mysql.project.rentedEntity.Marks;
import mysql.project.secondaryEntity.Student;
import mysql.project.primary.repo.PrimaryLecturerRepository;
import mysql.project.rented.repo.SecondaryRentMarksRepository;
import mysql.project.secondary.repo.SecondaryStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class StudentService {
    @Autowired
    private  SecondaryStudentRepository studentRepository;

    @Autowired
    private PrimaryLecturerRepository primaryLecturerRepository;

    @Autowired
    private SecondaryRentMarksRepository secondaryRentMarksRepository;

    public Optional<Student> validateStudent(String rollNo, String email){
        return studentRepository.findByRollNoAndEmail(rollNo,email);
    }

    public StudentInfo getStudentById(Integer studentId) {
        Student st= studentRepository.findById(studentId).orElseThrow(()->new IllegalArgumentException("Student not found!!"));

            StudentInfo studentInfo=new StudentInfo();
            studentInfo.setStudentId(studentId);
            studentInfo.setRollNo(st.getRollNo());
            studentInfo.setName(st.getName());
            studentInfo.setGender((st.getGender().equals("M"))?"Male":"Female");
            studentInfo.setSection(st.getStudentClass());
            studentInfo.setEmail(st.getEmail());
            studentInfo.setMarks(secondaryRentMarksRepository.findByStudentId(studentId));
            studentInfo.setLecturers(primaryLecturerRepository.findBySection(st.getStudentClass()));
            return studentInfo;

    }
    public List<Lecturer> validateLecturer(String email){
        return primaryLecturerRepository.findByEmail(email);
    }

    public LecturerDetails lecturerDetails(String email){
        List<Lecturer> lecturerList=primaryLecturerRepository.findByEmail(email);//fetch all lecturer
        Set<String> sections=new HashSet<>();//fetch all unique section of lecturer
        Set<String> subjects=new HashSet<>();

        for(Lecturer lecturer:lecturerList){
            sections.add(lecturer.getSection());
            subjects.add(lecturer.getDepartment());
        }

        Map<String,List<Integer>> map2=new HashMap<>();//section-student id map
        Map<Integer,List<Map<String,Integer>>> map=new HashMap<>();//student id->(subject-Mark) mark
        Map<String,List<Student>> sectionToStudents=new HashMap<>();

        for(String section:sections){
            sectionToStudents.put(section,studentRepository.findByStudentClass(section));
        }

       for(String section:sectionToStudents.keySet()){
           List<Integer> studentIds=sectionToStudents.get(section).stream()
                   .map(Student::getStudentId)
                   .collect(Collectors.toList());
           map2.put(section,studentIds);

           for(Integer id:studentIds)
               map.put(id,new ArrayList<>());
       }

        for(Integer id:map.keySet()){

            for(String subject:subjects){
                Marks marks1=secondaryRentMarksRepository.findByStudentIdAndSubject(id,subject);
                Map<String,Integer> ref=new HashMap<>();

                ref.put(subject,(marks1!=null)?marks1.getMarks():0);
                map.get(id).add(ref);
            }
        }

        Map<Integer,String[]> map1=studentRepository.findAllByStudentIdIn(map.keySet()).stream()
                .collect(Collectors.toMap(
                        student->student.getStudentId(),
                        student->new String[]{student.getName(),student.getRollNo()}
                ));


        Map<String,List<String>> classToSubject=lecturerList.stream()
                .collect(Collectors.groupingBy(
                        Lecturer::getSection,
                        Collectors.mapping(Lecturer::getDepartment,Collectors.toList())
                ));

        LecturerDetails lecturerDetails=new LecturerDetails();
        lecturerDetails.setLecturerList(lecturerList);
        lecturerDetails.setMap(map);
        lecturerDetails.setMap1(map1);
        lecturerDetails.setMap2(map2);
        lecturerDetails.setClassToSubject(classToSubject);

        return lecturerDetails;
    }
}
