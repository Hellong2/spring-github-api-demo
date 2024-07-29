package pl.domdom.gapi.web.service;

import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import pl.domdom.gapi.dto.GHReposDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Service
public class GithubWebServiceImpl implements GithubWebService {

    private final GitHub githubConnector;

    @Value("${github.api.app.repos.fork.exclude:true}")
    private boolean appReposExcluded;


    @Autowired
    public GithubWebServiceImpl(@Value("${github.api.app.id}") final long githubAppId, JwtConfig jwtConfig, @Value("${github.api.app.installationId:-1}") long installationId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtConfig.getJwtToken(String.valueOf(githubAppId))).build();
        if (installationId == -1) {
            installationId = gitHubApp.getApp().listInstallations().iterator().next().getId();
        } else {
            if (!gitHubApp.getApp().listInstallations().iterator().hasNext()) {
                throw new InstallationIdNotFoundException("Invalid installation ID");
            }
        }
        GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationById(installationId); // Installation Id
        GHAppInstallationToken appInstallationToken = appInstallation.createToken().create();
        this.githubConnector = new GitHubBuilder()
                .withAppInstallationToken(appInstallationToken.getToken())
                .build();

    }

    // there is sometimes a problem with establishing connection, which is why we are trying again.
    @Override
    @Retryable(
            backoff = @Backoff(delay = 2000)
    )
    public List<GHReposDto> getUserRepos(String userName) throws IOException {
        Map<String, GHRepository> userRepos = githubConnector.getUser(userName).getRepositories();
        if (appReposExcluded) {
            return filterRepositories(userRepos);
        }
        return userRepos.entrySet().stream().map(GithubWebService::convertToDto).toList();
    }


    private static List<GHReposDto> filterRepositories(Map<String, GHRepository> repositories) {
        return repositories.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isFork())
                .map(GithubWebService::convertToDto)
                .toList();
    }
}
