package cat.tecnocampus.college.webControllers;

import cat.tecnocampus.college.domainController.CollegeController;
import domain.College;
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
    public String registerStudentPost(@Valid Student student, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "registerStudent";
        }

        redirectAttributes.addFlashAttribute("student", student);
        college.registerStudent(student);

        return "redirect:login";
    }

    @GetMapping("/markStudents")
    public String markStudents(Model model) {
        model.addAttribute("subjects", college.getSubjects());

        return "markStudents";
    }

}
