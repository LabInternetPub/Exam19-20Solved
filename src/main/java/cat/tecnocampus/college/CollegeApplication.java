package cat.tecnocampus.college;

import cat.tecnocampus.college.domainController.CollegeController;
import cat.tecnocampus.college.persistence.RegistrationDAO;
import cat.tecnocampus.college.persistence.StudentDAO;
import cat.tecnocampus.college.persistence.SubjectDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CollegeApplication {
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private RegistrationDAO registrationDAO;
    private CollegeController college;

    public CollegeApplication(StudentDAO studentDAO, SubjectDAO subjectDAO, RegistrationDAO registrationDAO, CollegeController college) {
        this.studentDAO = studentDAO;
        this.subjectDAO = subjectDAO;
        this.registrationDAO = registrationDAO;
        this.college = college;
    }

    public static void main(String[] args) {
        SpringApplication.run(CollegeApplication.class, args);
    }

}
