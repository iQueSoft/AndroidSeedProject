package net.iquesoft.project.seed.domain.exception;

/**
 * Created by andre on 22-Jun-16.
 */
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidPasswordException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidPasswordException(Throwable throwable) {
        super(throwable);
    }
}
