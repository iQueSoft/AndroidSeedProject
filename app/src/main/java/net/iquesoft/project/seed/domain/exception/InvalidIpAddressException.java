package net.iquesoft.project.seed.domain.exception;

/**
 * Created by andre on 22-Jun-16.
 */
public class InvalidIpAddressException extends Exception {
    public InvalidIpAddressException() {
        super();
    }

    public InvalidIpAddressException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidIpAddressException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidIpAddressException(Throwable throwable) {
        super(throwable);
    }
}
