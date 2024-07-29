package pl.domdom.spring_github_api.webService;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GithubWebService {

    private final GitHub githubConnector;
    private final JwtConfig jwtConfig;
    private final GHAppInstallationToken appInstallationToken;

    @Autowired
    public GithubWebService(@Value("${github.api.app.id}") final long githubAppId, JwtConfig jwtConfig, @Value("${github.api.app.installationId:-1}") long installationId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.jwtConfig = jwtConfig;
        GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtConfig.getJwtToken(String.valueOf(githubAppId))).build();
        if (installationId == -1 && gitHubApp.getApp().listInstallations().iterator().hasNext()) {
            installationId = gitHubApp.getApp().listInstallations().iterator().next().getId();
        } else {
            throw new RuntimeException("Invalid installation ID");
        }
        GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationById(installationId); // Installation Id
        this.appInstallationToken = appInstallation.createToken().create();
        appInstallationToken.getExpiresAt();
        this.githubConnector = new GitHubBuilder()
                .withAppInstallationToken(appInstallationToken.getToken())
                .build();

    }

}
