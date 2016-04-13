package com.automation.server.controller;

import com.automation.server.repository.GitExecutionException;
import com.automation.server.repository.GitRepository;
import com.automation.server.repository.GitRepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@BasePathAwareController
@RestController
@RequestMapping(value = "/repositories")
public class GitRepositoryConnectionController implements ResourceProcessor<Resource<GitRepository>>, ResourceAssembler<GitRepository, Resource<GitRepository>> {
    @Autowired
    private GitRepositoryStore repositories;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<GitRepository>> openRepo(@RequestBody RepositoryData repositoryData) throws URISyntaxException, GitExecutionException, IOException {
        GitRepository gitRepository = null;
        gitRepository = repositories.openRepository(
                repositoryData.remoteRepositoryUrl,
                repositoryData.userName,
                repositoryData.password);
        System.out.println("Repository successfully opened - " + repositoryData.getRemoteRepositoryUrl());

        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resource<GitRepository>> getRepo() {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }

        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Resource<GitRepository>> closeRepo() {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }
        gitRepository.close();

        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.OK);
    }

    @Override
    public Resource<GitRepository> toResource(GitRepository entity) {
        Resource<GitRepository> resource = new Resource<>(entity);
        return resource;
    }

    @Override
    public Resource<GitRepository> process(Resource<GitRepository> resource) {
       resource.add(linkTo(GitRepositoryConnectionController.class).slash("upload").withRel("upload"));
       return resource;
    }

    static public class RepositoryData {
        private String remoteRepositoryUrl;
        private String userName;
        private String password;

        public String getRemoteRepositoryUrl() {
            return remoteRepositoryUrl;
        }

        public void setRemoteRepositoryUrl(String remoteRepositoryUrl) {
            this.remoteRepositoryUrl = remoteRepositoryUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
