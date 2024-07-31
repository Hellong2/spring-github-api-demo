package pl.domdom.gapi.web.service.exceptions;

public class InstallationIdNotFoundException extends RuntimeException {
    public InstallationIdNotFoundException(String message) {
        super(message);
    }

    public InstallationIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstallationIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
