package com.automation.server.repository;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;

public interface GitRepository extends Serializable{
    void create() throws IOException;
    void open(String userName, String password) throws IOException, GitExecutionException;
    String getLocalPath();
    void close();
    void addRemoteRepository(String remoteUrl);
    void clone(String remoteRepositoryUrl) throws URISyntaxException, IOException, GitExecutionException;
    void add(String filePatter) throws GitExecutionException;
    void commit(String commitMessage) throws GitExecutionException;
    void push() throws GitExecutionException;
    List<String> getDiff(int numberOfCommit) throws IOException;
}
