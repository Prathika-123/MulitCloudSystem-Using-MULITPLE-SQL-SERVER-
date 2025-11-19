package mysql.project.Services;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import mysql.project.DTO.ChangeMark;
import mysql.project.DTO.LecturerDetails;
import mysql.project.primary.repo.PrimaryLecturerRepository;
import mysql.project.primaryEntity.Lecturer;
import mysql.project.rented.repo.SecondaryRentMarksRepository;
import mysql.project.rentedEntity.Marks;
import mysql.project.secondary.repo.SecondaryStudentRepository;
import mysql.project.secondaryEntity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class LecturerService {
    @Autowired
    private PrimaryLecturerRepository lecturerRepository;

    @Autowired
    private SecondaryRentMarksRepository marksRepository;

    @Autowired
    private SecondaryStudentRepository studentRepository;

    public List<Lecturer> validateLecturer(String email){
        return lecturerRepository.findByEmail(email);
    }

    public LecturerDetails lecturerDetails(String email){
        List<Lecturer> lecturerList=lecturerRepository.findByEmail(email);//fetch all lecturer
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
                Marks marks1=marksRepository.findByStudentIdAndSubject(id,subject);
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

    @Transactional //check once
    public ResponseEntity<?> changeMarks(ChangeMark changeMark){
        Marks marks=marksRepository.findByStudentIdAndSubject(changeMark.getStudentId(),changeMark.getSubject());

        if(marks==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student marks not found");
        }

        if(changeMark.getStudentMark()<0 || changeMark.getStudentMark()>100){
            return ResponseEntity.badRequest().body("Marks is not valid!");
        }
        marks.setMarks(changeMark.getStudentMark());
        Marks marks1=marksRepository.save(marks);
        return ResponseEntity.ok("Marks updated successfully!");
    }
}
