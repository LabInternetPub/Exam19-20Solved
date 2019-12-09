package cat.tecnocampus.college.domainController;

import cat.tecnocampus.college.persistence.LecturerDAO;
import cat.tecnocampus.college.persistence.RegistrationDAO;
import cat.tecnocampus.college.persistence.StudentDAO;
import cat.tecnocampus.college.persistence.SubjectDAO;
import cat.tecnocampus.college.security.SecurityDAO;
import domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollegeController {
    private RegistrationDAO registrationDAO;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private LecturerDAO lecturerDAO;
    private SecurityDAO securityDAO;
    private College college;
    private PasswordEncoder passwordEncoder;

    public CollegeController(RegistrationDAO registrationDAO, StudentDAO studentDAO, SubjectDAO subjectDAO,
                             LecturerDAO lecturerDAO, SecurityDAO securityDAO, PasswordEncoder passwordEncoder) {
        this.registrationDAO = registrationDAO;
        this.studentDAO = studentDAO;
        this.subjectDAO = subjectDAO;
        this.lecturerDAO = lecturerDAO;
        this.securityDAO = securityDAO;
        this.passwordEncoder = passwordEncoder;

        college = new College();
        college.setSubjects(subjectDAO.getAllSubjects());
    }

    public Student getStudent(String email) {
        return studentDAO.getStudentEager(email);
    }

    public List<Student> getAllStudents() {
        return studentDAO.queryAllStudentsLazy();
    }

    public Registration getCurrentRegistration(String email) {
        return registrationDAO.queryLastRegistrationFromUser(email);
    }

    public List<Registration> getAllStudentRegistrations(String email) {
        return registrationDAO.queryRegistrationsFromUser(email);
    }

    public List<Subject> getSubjects() {
        return college.getSubjects();
    }

    public Registration registerSubjects(String email, List<String> subjects) {
        List<Subject> subjectList;
        subjectList = subjects.stream().map(s -> college.getSubject(s)).collect(Collectors.toList());

        Student student = studentDAO.getStudentEager(email);

        Registration registration = college.registerSubjects(student, subjectList);
        registrationDAO.registerSubjects(registration);

        return registration;
    }

    public Map<College.Level, List<Subject>> getSubjectsStillToRegister(String email) {
        Student student = studentDAO.getStudentEager(email);

        return college.getSubjectsStillToRegister(student);
    }

    public void registerStudent(Student student) {

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentDAO.saveStudent(student);
        securityDAO.createUserRole(student);
    }

    public List<Lecturer> getAllLecturers() {
        return lecturerDAO.getLecturersWithCurrentSubjects();
    }
}
