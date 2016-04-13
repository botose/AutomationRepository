package com.automation.server.repository;

import java.io.IOException;
import java.net.URISyntaxException;

public interface GitRepository {
    void create() throws IOException;
    void open() throws IOException;
    String getLocalPath();
    void close();
    void addRemoteRepository(String remoteUrl);
    void clone(String remoteRepositoryUrl) throws URISyntaxException, IOException, GitExecutionException;
    void add(String filePatter) throws GitExecutionException;
    void commit(String commitMessage) throws GitExecutionException;
    void push() throws GitExecutionException;
}
