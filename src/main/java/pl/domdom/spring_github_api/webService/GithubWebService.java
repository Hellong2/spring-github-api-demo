package pl.domdom.spring_github_api.webService;

import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import pl.domdom.spring_github_api.dto.GHReposDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GithubWebService {

    private final GitHub githubConnector;

    @Value("${github.api.app.repos.fork.exclude:true}")
    private boolean appReposExcluded;


    @Autowired
    public GithubWebService(@Value("${github.api.app.id}") final long githubAppId, JwtConfig jwtConfig, @Value("${github.api.app.installationId:-1}") long installationId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtConfig.getJwtToken(String.valueOf(githubAppId))).build();
        if (installationId == -1 && gitHubApp.getApp().listInstallations().iterator().hasNext()) {
            installationId = gitHubApp.getApp().listInstallations().iterator().next().getId();
        } else {
            throw new RuntimeException("Invalid installation ID");
        }
        GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationById(installationId); // Installation Id
        GHAppInstallationToken appInstallationToken = appInstallation.createToken().create();
        this.githubConnector = new GitHubBuilder()
                .withAppInstallationToken(appInstallationToken.getToken())
                .build();

    }

    private static GHReposDto convertToDto(Map.Entry<String, GHRepository> repo) {
        GHReposDto reposDto = new GHReposDto();
        reposDto.setRepoName(repo.getKey());
        reposDto.setRepoOwner(repo.getValue().getOwnerName());
        reposDto.setReposDetails(new ArrayList<>());
        try {
            for (Map.Entry<String, GHBranch> branch : repo.getValue().getBranches().entrySet()) {
                GHReposDto.GHReposDetails details = new GHReposDto.GHReposDetails();
                details.setBranchName(branch.getKey());
                details.setLastCommitSha(branch.getValue().getSHA1());
                reposDto.getReposDetails().add(details);
            }
        } catch (IOException e) {
            throw new RuntimeException("");
        }
        return reposDto;
    }

    // there is sometimes a problem with establishing connection, which is why we are trying again.
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
