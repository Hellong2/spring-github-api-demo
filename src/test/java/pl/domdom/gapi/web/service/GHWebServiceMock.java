package pl.domdom.gapi.web.service;

import pl.domdom.gapi.dto.GHUserReposDto;
import pl.domdom.gapi.web.service.exceptions.UserNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GHWebServiceMock implements GithubWebService {
    @Override
    public List<GHUserReposDto> getUserRepos(String userName) throws IOException {
        if (userName.equals("throwIOException")) {
            throw new IOException("Test IOException");
        }
        if (!userName.equals("validUser")) {
            throw new UserNotFoundException("User not found");
        }
        List<GHUserReposDto> repos = new ArrayList<>();
        repos.add(createGHReposDto("First", userName + ": 1", 3));
        repos.add(createGHReposDto("Second", userName + ": 2", 1));
        return repos;
    }

    private GHUserReposDto createGHReposDto(String repoName, String repoOwner, int detailsGenerationCounter) {
        ArrayList<GHUserReposDto.GHReposDetails> repos = new ArrayList<>();
        for (int i = 0; i < detailsGenerationCounter; i++) {
            GHUserReposDto.GHReposDetails ghReposDetails = new GHUserReposDto.GHReposDetails("branch: " + i, UUID.randomUUID().toString());
            repos.add(ghReposDetails);
        }
        return new GHUserReposDto(repoName, repoOwner, repos);
    }


}
