package com.automation.server.repository;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Component("GitRepositoryStore")
public class GitRepositoryStore implements Serializable {
    public static final String GIT_REPOS_BASE_FOLDER = "gitRepos/";
    public static final String GIT_FOLDER = "/.git";
    static private Map<String, GitRepositoryAdapter> gitRepositories = new HashMap<>();
    static private GitRepositoryAdapter activeRepository;

    public GitRepositoryAdapter getActiveRepository() {
        if(activeRepository == null) {
            throw new IllegalStateException("No active Git repo, please open one first.");
        }
        return activeRepository;
    }

    public GitRepositoryAdapter openRepository(String remoteRepositoryUrl, String userName, String password) throws URISyntaxException, GitExecutionException, IOException {
        GitRepositoryAdapter gitRepositoryAdapter = gitRepositories.get(getRepoNameFromURL(remoteRepositoryUrl));

        if(activeRepository != gitRepositoryAdapter) {
            activeRepository.close();
        }

        if(gitRepositoryAdapter == null) {
            createAndOpenRepository(remoteRepositoryUrl, userName, password);
        } else {
            openExistingRepository(userName, password, gitRepositoryAdapter);
        }

        return activeRepository;
    }

    private void openExistingRepository(String userName, String password, GitRepositoryAdapter gitRepositoryAdapter) throws GitExecutionException, IOException {
        try {
            gitRepositoryAdapter.open(userName, password);
            activeRepository = gitRepositoryAdapter;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void createAndOpenRepository(String remoteRepositoryUrl, String userName, String password) throws URISyntaxException, GitExecutionException, IOException {
        GitRepositoryAdapter gitRepositoryAdapter;
        String repoName = getRepoNameFromURL(remoteRepositoryUrl);
        String localPath = GIT_REPOS_BASE_FOLDER + repoName;
        gitRepositoryAdapter = new GitRepositoryAdapter(localPath, userName, password);
        try {
            if(new File(localPath + GIT_FOLDER).exists()) {
                gitRepositoryAdapter.open(userName, password);
            } else {
                gitRepositoryAdapter.clone(remoteRepositoryUrl);
            }
            gitRepositories.put(repoName, gitRepositoryAdapter);
            activeRepository = gitRepositoryAdapter;
        } catch (GitExecutionException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getRepoNameFromURL(String remoteRepositoryUrl) {
        String[] splittedUrl = remoteRepositoryUrl.split("/");
        return splittedUrl[splittedUrl.length-1];
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(gitRepositories);
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        this.gitRepositories = (Map<String, GitRepositoryAdapter>) stream.readObject();
    }
}
