package projectBlog.customBlog;

public class TempException extends RuntimeException {

    public TempException() {
        super();
    }

    public TempException(String message) {
        super(message);
    }

    public TempException(String message, Throwable cause) {
        super(message, cause);
    }

    public TempException(Throwable cause) {
        super(cause);
    }
}
