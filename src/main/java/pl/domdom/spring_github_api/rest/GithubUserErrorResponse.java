package pl.domdom.spring_github_api.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GithubUserErrorResponse {
    private int status;
    private String message;
}
