package com.automation.server.repository;


import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.Map;

public class GitRepositoryStore {
    static private Map<String, GitRepositoryAdapter> gitRepositories;
    static private GitRepositoryAdapter activeRepository;

    public GitRepositoryAdapter getActiveRepository() {
        return null;
    }

    public GitRepositoryAdapter openRepository(String remoteRepositoryUrl, String userName, String password) {
        GitRepositoryAdapter gitRepositoryAdapter = gitRepositories.get(remoteRepositoryUrl);
        if(gitRepositoryAdapter == null) {
            String[] splittedUrl = remoteRepositoryUrl.split("/");
            String repoName = "gitRepos/" + splittedUrl[splittedUrl.length-1].split(".")[0];
            gitRepositoryAdapter = new GitRepositoryAdapter(repoName);
            try {
                gitRepositoryAdapter.clone(remoteRepositoryUrl, userName, password);
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addRepository() {

    }
}
