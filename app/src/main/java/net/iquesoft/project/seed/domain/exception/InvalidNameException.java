package net.iquesoft.project.seed.domain.exception;

public class InvalidNameException extends Exception {
    public InvalidNameException() {
        super();
    }

    public InvalidNameException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidNameException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidNameException(Throwable throwable) {
        super(throwable);
    }
}
