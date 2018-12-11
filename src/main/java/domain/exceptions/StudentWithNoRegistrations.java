package domain.exceptions;

public class StudentWithNoRegistrations extends RuntimeException {
    public StudentWithNoRegistrations(String email) {
        super("User: " + email + "has no registrations!!");
    }
}
