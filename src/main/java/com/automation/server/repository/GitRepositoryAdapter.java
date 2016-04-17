package com.automation.server.repository;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GitRepositoryAdapter implements GitRepository {
    private String localPath;
    private String remoteUrl;
    private Repository repository;
    private Git git;
    private CredentialsProvider creditentialsProvider;

    public GitRepositoryAdapter(String localPath, String userName, String password) {
        this.localPath = localPath;
        this.creditentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
    }

    @Override
    public void create() throws IOException {
        repository = FileRepositoryBuilder.create(new File(localPath));

    }

    @Override
    public void open(String userName, String password) throws IOException, GitExecutionException {
        creditentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
        try {
            pull();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new GitExecutionException("Exception when executing 'open'. Remote URL: " +
                    remoteUrl + ", local path:" + localPath, e);
        }
        repository = new FileRepositoryBuilder()
                .setGitDir(new File(localPath))
                .build();
        git = new Git(repository);
    }

    @Override
    public void close() {
        git.close();
        repository.close();
        creditentialsProvider = null;
    }

    @Override
    public void addRemoteRepository(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    @Override
    public void clone(String remoteRepositoryUrl) throws URISyntaxException, IOException, GitExecutionException {
        remoteUrl = remoteRepositoryUrl;
        try {
            git = Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(new File(localPath))
                    .setCredentialsProvider(creditentialsProvider)
                    .call();
            repository = git.getRepository();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new GitExecutionException("Exception when executing 'clone'. Remote URL: " + remoteUrl, e);
        }
    }

    @Override
    public void add(String filePattern) throws GitExecutionException {
        if(git == null) {
            throw new IllegalStateException();
        }
        AddCommand add = git.add();
        try {
            add.addFilepattern(filePattern).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new GitExecutionException("Exception when executing 'add'. File pattern: " + filePattern, e);
        }
    }

    @Override
    public void commit(String commitMessage) throws GitExecutionException {
        if(git == null) {
            throw new IllegalStateException();
        }
        CommitCommand commit = git.commit();
        try {
            commit.setMessage(commitMessage).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new GitExecutionException("Exception when executing 'commit'. Commit message: " + commitMessage, e);
        }
    }

    @Override
    public void push() throws GitExecutionException {
        PushCommand push = git.push().setCredentialsProvider(creditentialsProvider);
        try {
            push.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new GitExecutionException("Exception when executing 'push'.", e);
        }
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    private void pull() throws GitAPIException {
        PullCommand pull = git.pull().setCredentialsProvider(creditentialsProvider);
        pull.call();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(remoteUrl);
        stream.writeObject(localPath);
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        this.remoteUrl = (String) stream.readObject();
        this.localPath = (String) stream.readObject();
    }
}
