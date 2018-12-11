package domain.exceptions;

public class UnRegisteredSubjectException extends RuntimeException {
    public UnRegisteredSubjectException(String s) {
        super(s);
    }
}
