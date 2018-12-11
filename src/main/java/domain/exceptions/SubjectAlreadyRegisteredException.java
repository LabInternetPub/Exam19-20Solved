package domain.exceptions;

public class SubjectAlreadyRegisteredException extends RuntimeException {
    public SubjectAlreadyRegisteredException(String s) {
        super(s);
    }
}
