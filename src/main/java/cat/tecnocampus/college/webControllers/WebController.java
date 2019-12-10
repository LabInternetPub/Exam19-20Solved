package cat.tecnocampus.college.webControllers;

import cat.tecnocampus.college.domainController.CollegeController;
import domain.College;
import domain.Lecturer;
import domain.Student;
import domain.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class WebController {
    CollegeController college;

    public WebController(CollegeController college) {
        this.college = college;
    }

    @GetMapping("studentList")
    public String showAllStudents(Model model) {
        model.addAttribute("students", college.getAllStudents());

        return "studentList";
    }

    @GetMapping({"subjectList", "/"})
    public String showSubjects(Model model) {
        model.addAttribute("subjects", college.getSubjects());

        return "subjectList";
    }

    @GetMapping("lastRegistration")
    public String currentRegistration(Model model, Principal principal) {
        model.addAttribute("registration", college.getCurrentRegistration(principal.getName()));
        return "currentRegistration";
    }

    @GetMapping("academicRecord")
    public String allUserRegistrations(Model model, Principal principal) {
        model.addAttribute("registrations", college.getAllStudentRegistrations(principal.getName()));
        return "academicRecord";
    }

    @GetMapping({"registerSubjects", "registerSubjects/{errorMsg}"})
    public String registerSubjects(@PathVariable Optional<String> errorMsg, Model model, Principal principal) {
        if (errorMsg.isPresent()) {
            model.addAttribute("errorMsg", errorMsg.get());
        } else model.addAttribute("errorMsg", "");

        Map<College.Level, List<Subject>> subjects = college.getSubjectsStillToRegister(principal.getName());
        model.addAttribute("subjectsToDo", subjects);

        return "registerSubjects";
    }

    @PostMapping("registerSubjects")
    public String registerSubjectsPost(@RequestParam List<String> subjects, Model model, Principal principal) {
        college.registerSubjects(principal.getName(), subjects);

        return "redirect:lastRegistration";
    }

    @GetMapping("registerStudent")
    public String registerStudentGet(Model model) {
        model.addAttribute("student", new Student());

        return "registerStudent";
    }

    @PostMapping("registerStudent")
    public String registerStudentPost(@Valid Student student, Errors errors) {
        if (errors.hasErrors()) {
            return "registerStudent";
        }

        college.registerStudent(student);

        return "redirect:login";
    }

    @GetMapping("registerLecturer")
    public String registerLecturerGet(Model model) {
        model.addAttribute("lecturer", new Lecturer());
        model.addAttribute("subjects", college.getSubjects());

        return "registerLecturer";
    }

    /*
    TODO  5
     When we register a new lecturer we need the very same information as for a student (they both are users) but also
     the subjects they are teaching. Observe in the following method that we pass a list of all the subjects in the system
     to the form. Wee need the form to show to the user the list of the subjects so that she can select the ones she is
     going to teach.
     If the form is done correctly the spring will be able to fill in all the Lecturer values including the list of current
     (teaching) subjects. That is to say a list with Java objects of the Subject class. In fact, we need the subjects
     with only their code filled in. Note that the Lecturers' subjects list is called CurrentSubjects
     In this exercise you only need to complete the "registerLecturer.html" form. Go to todo_5.1
     */

    /*
    TODO 8
     Complete the following method so that Spring validates the lecturer and returns the form to the user when it is
     not properly filled in (you can see that the User class has the appropriate annotations).
     */
    @PostMapping("registerLecturer")
    public String registerLecturerPost(@Valid Lecturer lecturer, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("subjects", college.getSubjects());
            return "registerLecturer";
        }

        college.registerLecturer(lecturer);
        return "redirect:login";
    }


    @GetMapping("/markStudents")
    public String markStudents(Model model) {
        model.addAttribute("subjects", college.getSubjects());

        return "markStudents";
    }

    @GetMapping("lecturerList")
    public String listLecturers(Model model) {
        model.addAttribute("lecturers", college.getAllLecturers());
        return "lecturerList";
    }

}
