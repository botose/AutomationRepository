package com.automation.server.repository;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;


public class GitRepositoryAdapter implements GitRepository {
    private String remoteUrl;
    private String localPath;
    private Repository repository;
    private Git git;
    private CredentialsProvider creditentialsProvider;

    public GitRepositoryAdapter(String localPath, String userName, String password) {
        this.localPath = localPath;
        creditentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
    }

    @Override
    public void create() throws IOException {
        repository = FileRepositoryBuilder.create(new File(localPath));

    }

    @Override
    public void open() throws IOException {
        repository = new FileRepositoryBuilder()
                .setGitDir(new File(localPath))
                .build();
        git = new Git(repository);
    }

    @Override
    public String getLocalPath() {
        return localPath;
    }

    @Override
    public void close() {
        repository.close();
    }

    @Override
    public void addRemoteRepository(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    @Override
    public void clone(String remoteRepositoryUrl) throws URISyntaxException, IOException, GitExecutionException {
        remoteUrl = remoteRepositoryUrl;
        open();
        git.remoteSetUrl().setUri(new URIish(remoteUrl));
        try {
            Collection<Ref> remoteRefs = git.lsRemote()
                    .setCredentialsProvider(creditentialsProvider)
                    .setRemote("origin")
                    .setTags(true)
                    .setHeads(false)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new GitExecutionException("Exception when executing 'clone'. Remote URL: " + remoteRepositoryUrl, e);
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
        PushCommand push = git.push();
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

    public Repository getRepository() {
        return repository;
    }

    public Git getGit() {
        return git;
    }

    public CredentialsProvider getCreditentialsProvider() {
        return creditentialsProvider;
    }
}
