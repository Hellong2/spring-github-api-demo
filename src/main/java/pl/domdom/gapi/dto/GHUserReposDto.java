package pl.domdom.gapi.dto;

import java.util.List;

public record GHUserReposDto(String repoName, String repoOwner, List<GHReposDetails> reposDetails) {

    public record GHReposDetails(String branchName, String lastCommitSha) {}

}
