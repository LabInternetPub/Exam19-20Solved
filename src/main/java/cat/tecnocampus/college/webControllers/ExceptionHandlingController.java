package cat.tecnocampus.college.webControllers;

import domain.Student;
import domain.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sun.jvm.hotspot.ci.ciReceiverTypeData;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(RegisterPreviousLevelSubjectsFirst.class)
    public String handlePreviousSubjectsNotSelected(HttpServletRequest request, Exception ex) {

        logger.error("Request: " + request.getRequestURL() + " raised " + ex);

        String errorMsg = "You must register all subjects of precedent levels!!!";
        return "redirect:/registerSubjects/"+errorMsg;
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public String handleNoSubjectSelected(HttpServletRequest request, Exception ex) {

        logger.error("Request: " + request.getRequestURL() + " raised " + ex);

        String errorMsg = "You must choose at least one subject!!!";
        return "redirect:/registerSubjects/"+errorMsg;
    }

    @ExceptionHandler({StudentDoesNotExistsException.class, StudentWithNoRegistrations.class, SubjectAlreadyRegisteredException.class, UnRegisteredSubjectException.class})
    public String handleManyErrors(Model model, HttpServletRequest request, Exception ex) {
        String url = request.getRequestURL().toString();

        logger.error("Request: " + url + " raised " + ex);

        model.addAttribute("errorMsg", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public String duplicatedKey(HttpServletRequest request, Exception ex, Model model) {
        logger.error("Request: " + request.getRequestURL() + " raised " + ex);

        if (request.getRequestURL().toString().contains("registerStudent")) {
            Student student = createStudentFromRequest(request);
            model.addAttribute("student", student);
            model.addAttribute("userExists", "exists");
            return "registerStudent";
        }

        model.addAttribute("errorMsg", ex.getMessage());
        return "errorPage";
    }

    private Student createStudentFromRequest(HttpServletRequest request) {
        Student student = new Student();
        student.setName(request.getParameter("name"));
        student.setSecondName(request.getParameter("secondName"));
        student.setEmail(request.getParameter("email"));

        return student;
    }

}
