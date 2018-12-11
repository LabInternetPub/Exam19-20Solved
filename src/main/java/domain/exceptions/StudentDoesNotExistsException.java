package domain.exceptions;

public class StudentDoesNotExistsException extends RuntimeException {
    public StudentDoesNotExistsException(String email) {
        super("Student " + email + " does not exist");
    }
}
