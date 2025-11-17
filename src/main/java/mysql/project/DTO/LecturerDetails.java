package mysql.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mysql.project.primaryEntity.Lecturer;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LecturerDetails {
    private List<Lecturer> lecturerList;
    private Map<String,List<Integer>> map2;
    private Map<Integer,List<Map<String,Integer>>> map;
    private Map<Integer,String[]> map1;
    private Map<String,List<String>> classToSubject;
}
