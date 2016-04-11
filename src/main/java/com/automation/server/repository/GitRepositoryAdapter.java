package com.automation.server.repository;

import com.sun.jndi.toolkit.url.Uri;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;


public class GitRepositoryAdapter {
    private String path;
    private Repository repository;
    private Git git = new Git(repository);

    public GitRepositoryAdapter(String path) {
        this.path = path;
    }

    public void create() throws IOException {
        repository = FileRepositoryBuilder.create(new File(path));
    }

    public void open() throws IOException {
        repository = new FileRepositoryBuilder()
                .setGitDir(new File(path))
                .build();
    }

    public String getPath() {
        return path;
    }

    public void close() {
        repository.close();
    }

    public void clone(String remoteRepositoryUrl, String userName, String password) throws GitAPIException, URISyntaxException {
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(userName, password);
        git.remoteSetUrl().setUri(new URIish(remoteRepositoryUrl));
        Collection<Ref> remoteRefs = git.lsRemote()
                .setCredentialsProvider(cp)
                .setRemote("origin")
                .setTags(true)
                .setHeads(false)
                .call();
    }
}
