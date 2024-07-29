package pl.domdom.gapi.web.service;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import pl.domdom.gapi.dto.GHReposDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GithubWebService {
    static GHReposDto convertToDto(Map.Entry<String, GHRepository> repo) {
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
            throw new GetUserBranchesException("Something went wrong while fetching the user branches", e.getCause());
        }
        return reposDto;
    }

    List<GHReposDto> getUserRepos(String userName) throws IOException;
}
