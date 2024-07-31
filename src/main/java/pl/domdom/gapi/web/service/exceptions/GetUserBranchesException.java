package pl.domdom.gapi.web.service.exceptions;

public class GetUserBranchesException extends RuntimeException {
    public GetUserBranchesException(String message) {
        super(message);
    }

    public GetUserBranchesException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetUserBranchesException(Throwable cause) {
        super(cause);
    }
}
