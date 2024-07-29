package pl.domdom.spring_github_api.rest;


public class GithubUserNotFoundException extends RuntimeException {

    public GithubUserNotFoundException(String message) {
        super(message);
    }

    public GithubUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GithubUserNotFoundException(Throwable cause) {
        super(cause);
    }
}
