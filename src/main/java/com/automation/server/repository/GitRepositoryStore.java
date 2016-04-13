package com.automation.server.repository;


import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Component("GitRepositoryStore")
public class GitRepositoryStore {
    static private Map<String, GitRepositoryAdapter> gitRepositories = new HashMap<>();
    static private GitRepositoryAdapter activeRepository;

    public GitRepositoryAdapter getActiveRepository() {
        return activeRepository;
    }

    public GitRepositoryAdapter openRepository(String remoteRepositoryUrl, String userName, String password) throws URISyntaxException, GitExecutionException {
        GitRepositoryAdapter gitRepositoryAdapter = gitRepositories.get(remoteRepositoryUrl);
        if(gitRepositoryAdapter == null) {
            String[] splittedUrl = remoteRepositoryUrl.split("/");
            String localRepositoryPath = "gitRepos/" + splittedUrl[splittedUrl.length-1].split(".")[0];
            gitRepositoryAdapter = new GitRepositoryAdapter(localRepositoryPath, userName, password);
            try {
                gitRepositoryAdapter.clone(remoteRepositoryUrl);
                gitRepositories.put(remoteRepositoryUrl, gitRepositoryAdapter);
                activeRepository = gitRepositoryAdapter;
            } catch (GitExecutionException e) {
                e.printStackTrace();
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gitRepositoryAdapter;
    }
}
