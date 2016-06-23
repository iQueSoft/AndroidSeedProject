package net.iquesoft.project.seed.domain.exception;

public class InvalidWebUrlException extends Exception {
    public InvalidWebUrlException() {
        super();
    }

    public InvalidWebUrlException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidWebUrlException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidWebUrlException(Throwable throwable) {
        super(throwable);
    }
}
