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

    /*
    TODO 4:
     The aim of this exercise is, when a new student is being created with an email that already exists in the system, to
     return to the registration form "registerStudent" completed with the data introduced by the user. Note that this kind
     of error can't be detected by the @Valid annotation since it's not a syntax problem but a business one.
     We need to make several steps. You'll have points for each one:
     1.- Catch the appropriate exception (duplicated key). To do so, create a new student and see the exceptions risen by the system. See
     that there is more than one. Take the most general one (do NOT catch the one fetched by the H2 database).
     Once you have the correct exception handler the method will return a "standard" error page.
     2.- This exception can be risen by any duplicated key. You need to return the "registerStudent" form only when the exception if risen
     by a student duplicated page (and the "standard" error otherwise). You'll need to pass an empty student to the thymeleaf form and also
     an attribute named "studentExists" with content "exists" (see the form registerStudent.html)
     HINT: The information you need is not in the exception
     3.- Now we want to pass the student provided by the user to the form. We can't receive this information through the model (unfortunately).
     You'll need to create the student with the information provided by one of the already existing method parameters. Guess which and how
     to do so. Better create a private method that returns the student filled in.
     */
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public String duplicatedKey(HttpServletRequest request, Exception ex, Model model) {
        logger.error("Request: " + request.getRequestURL() + " raised " + ex);

        if (request.getRequestURL().toString().contains("registerStudent")) {
            //Could have done:
            //Student s = (Student) request.getSession().getAttribute("student");
            Student student = createStudentFromRequest(request);
            model.addAttribute("student", student);
            model.addAttribute("studentExists", "exists");
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
