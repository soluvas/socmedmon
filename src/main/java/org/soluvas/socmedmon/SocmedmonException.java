package org.soluvas.socmedmon;

/**
 * Created by ceefour on 27/10/2015.
 */
public class SocmedmonException extends RuntimeException {

    public SocmedmonException() {
    }

    public SocmedmonException(String message) {
        super(message);
    }

    public SocmedmonException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocmedmonException(Throwable cause) {
        super(cause);
    }

    public SocmedmonException(Throwable cause, String format, Object... params) {
        super(String.format(format, params), cause);
    }

    public SocmedmonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
