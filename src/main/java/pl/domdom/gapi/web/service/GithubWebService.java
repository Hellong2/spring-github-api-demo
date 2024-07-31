package pl.domdom.gapi.web.service;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import pl.domdom.gapi.dto.GHUserReposDto;
import pl.domdom.gapi.web.service.exceptions.GetUserBranchesException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GithubWebService {
    static GHUserReposDto convertToDto(Map.Entry<String, GHRepository> repo) {
        List<GHUserReposDto.GHReposDetails> reposDetails = new ArrayList<>();
        try {
            for (Map.Entry<String, GHBranch> branch : repo.getValue().getBranches().entrySet()) {
                GHUserReposDto.GHReposDetails details = new GHUserReposDto.GHReposDetails(branch.getKey(), branch.getValue().getSHA1());
                reposDetails.add(details);
            }
        } catch (IOException e) {
            throw new GetUserBranchesException("Something went wrong while fetching the user branches", e.getCause());
        }
        return new GHUserReposDto(repo.getKey(), repo.getValue().getOwnerName(), reposDetails);
    }

    List<GHUserReposDto> getUserRepos(String userName) throws IOException;
}
