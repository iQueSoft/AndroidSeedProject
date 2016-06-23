package net.iquesoft.project.seed.domain.exception;

public class EmptyFieldException extends Exception {

    public EmptyFieldException() {
        super();
    }

    public EmptyFieldException(String detailMessage) {
        super(detailMessage);
    }

    public EmptyFieldException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public EmptyFieldException(Throwable throwable) {
        super(throwable);
    }
}
