package cat.tecnocampus.college.webControllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorWebController implements ErrorController {

    @GetMapping({"/error"})
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMsg = "";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMsg = "Http Error Code: 400. Bad Request";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorMsg = "Http Error Code: 401. Unauthorized";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMsg = "Http Error Code: 403. Unauthorized";
            }else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMsg = "Http Error Code: 404. Resource not found";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMsg = "Http Error Code: 500. Internal Server Error";
            }
            else errorMsg = "Error number: " + status.toString();
        }
        else
            errorMsg = "Undefined error";

        model.addAttribute("errorMsg", errorMsg);
        return "errorPage";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
