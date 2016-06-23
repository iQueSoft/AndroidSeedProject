package net.iquesoft.project.seed.domain.exception;

/**
 * Created by andre on 22-Jun-16.
 */
public class InvalidEmailException extends Exception {
    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidEmailException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidEmailException(Throwable throwable) {
        super(throwable);
    }
}
