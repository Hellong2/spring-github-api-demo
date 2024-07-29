package pl.domdom.gapi.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.domdom.gapi.dto.GHReposDto;
import pl.domdom.gapi.web.service.GithubWebService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final GithubWebService githubWebService;

    @GetMapping("/{userName}")
    public ResponseEntity<List<GHReposDto>> getRepos(@PathVariable String userName) {
        try {
            return new ResponseEntity<>(githubWebService.getUserRepos(userName), HttpStatus.OK);
        } catch (IOException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }
    }
