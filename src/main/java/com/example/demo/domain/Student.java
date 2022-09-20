package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Entity
public class Student {

    @Id
    @GeneratedValue
    @Column(name = "student_id")
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentLecture> myLectures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multi_dept_id")
    private Department multiDept;

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Embedded
    private Credit credit;
    private int grade;
    private String multiMajor;

    // 이성훈이 9/11에 추가함
    private int admissionYear;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<TimeTable> timetables = new ArrayList<>();

    //생성자
    public Student(String name, Department department, int grade, int admissionYear) {
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.admissionYear = admissionYear;
        this.credit = new Credit();
    }

    public Student() {
    }



    /** 양방향 편의 메서드 */
    public void addLectureToStudent(Lecture lecture, String gpa, int takesGrade, int takesSemester) {
        StudentLecture st = new StudentLecture(this,lecture, gpa, takesGrade, takesSemester);

        this.getMyLectures().add(st);
    }


    //==비즈니스 로직==//

    /** 내가 들었던 강의들중 LectureName과 같은게 있다면 requiredLectures에서 삭제함*/
    public void CheckIfListenLectures( Set<String> requiredLectures ){
        Iterator<String> iterate = requiredLectures.iterator();

        while(iterate.hasNext()){
            String lectureName = iterate.next();
            if (myLectures.stream().anyMatch(sl -> (sl.getGpa()!="F" && sl.getGpa()!="NP" // F나 NP가 아닌지 확인
                    && sl.getLecture().getName().equals(lectureName))))
                    iterate.remove();
        }
    }

    /** 내가 들었던 강의들중 LectureName을 포함하는 것이 있다면 requiredLectures에서 LectureName을 포함하는 것들을 모두 삭제함*/
    public void CheckIfListenSimilarLecture(Set<String> requiredLectures, String lectureName){
        if(myLectures.stream().anyMatch(sl -> (sl.getGpa()!="F" && sl.getGpa()!="NP"    // F나 NP가 아닌지 확인
                && sl.getLecture().getName().contains(lectureName))))
            requiredLectures.removeIf(lecture -> lecture.contains(lectureName));
    }

    /**
     * 시간표 삭제
     */
    public void delete(TimeTable timeTable){
        timetables.remove(timeTable);
    }

}
