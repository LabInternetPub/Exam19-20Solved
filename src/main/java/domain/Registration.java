package domain;

import domain.exceptions.SubjectAlreadyRegisteredException;
import domain.exceptions.UnRegisteredSubjectException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Registration {
    private static int FIRST_APRIL = 91;
    private static int LAST_DECEMBER = 365;

    private Student student;
    private ArrayList<Subject> subjects;
    private HashMap<String,Double> grades;
    private String academicYear;  /* YYYY/YYYY */
    private LocalDate date;

    //Very very nasty and dirty trick (need to find a better way)
    //For the SimpleMapFlatter to cope with Map<String,Double>
    private String gradeKey = null;
    private Double gradeMark = null;

    public Registration() {
        date = LocalDate.now();
        subjects = new ArrayList<>();
        grades = new HashMap<>();

        if (date.getDayOfYear() >= FIRST_APRIL && date.getDayOfYear() <= LAST_DECEMBER)
            this.academicYear = String.valueOf(date.getYear()) + "/" + String.valueOf(date.getYear()+1);
        this.academicYear = String.valueOf(date.getYear()-1) + "/" + String.valueOf(date.getYear());;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        System.out.println("Registration set subjects called: " + subjects);
        this.subjects.addAll(subjects);
    }

    public void registerSubject(Subject subject) {
        if (grades.containsKey(subject.getCode()))
            throw new SubjectAlreadyRegisteredException("Subject " + subject.getCode() + " already registered");

        grades.put(subject.getCode(), -1.0);
        subjects.add(subject);
    }

    public void grade(Subject subject, double mark) {
        if (subjects.contains(subject)) {
            grades.put(subject.getCode(), mark);
        } else {
            throw new UnRegisteredSubjectException("Subject " + subject.getCode() + " is not registered");
        }
    }

    public double getSubjectMark(String code) {
        if (grades.containsKey(code))
            return grades.get(code);
        throw new UnRegisteredSubjectException("Subject " + code + " is not registered");
    }

    public boolean passedSubject(Subject subject) {
        Double mark = grades.get(subject.getCode());
        if (mark == null)
            return false;
        return mark.doubleValue() >= 5.0;
    }

    public boolean NotEvaluatedSubject(Subject subject) {
        Double mark = grades.get(subject.getCode());
        if (mark == null) {
            return true;
        }
        return mark.doubleValue() < 0.0;
    }

    public List<Subject> getPassedSubjects() {
        return subjects.stream().filter(this::passedSubject).collect(Collectors.toList());
    }

    public List<Subject> getNonEvaluatedSubjects() {
        return subjects.stream().filter(this::NotEvaluatedSubject).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Registration{" +
                "student=" + student +
                ", subjects=" + subjects +
                ", grades=" + grades +
                ", academicYear='" + academicYear + '\'' +
                ", date=" + date +
                '}';
    }

}
