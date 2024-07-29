package pl.domdom.spring_github_api.dto;

import lombok.Data;

import java.util.List;

@Data
public class GHReposDto {
    private String repoName;
    private String repoOwner;
    private List<GHReposDetails> reposDetails;

    @Data
    public static class GHReposDetails {
        private String branchName;
        private String lastCommitSha;
    }

}
