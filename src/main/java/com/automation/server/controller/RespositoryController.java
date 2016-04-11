package com.automation.server.controller;

import com.automation.server.repository.GitRepositoryAdapter;
import com.automation.server.repository.GitRepositoryStore;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@BasePathAwareController
public class RespositoryController implements ResourceProcessor<Resource<GitRepositoryAdapter>>, ResourceAssembler<GitRepositoryAdapter, Resource<GitRepositoryAdapter>> {
    private GitRepositoryStore repositories;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Resource<GitRepositoryAdapter>> openRepo(String remoteRepositoryUrl, String userName, String password) {
        GitRepositoryAdapter gitRepositoryAdapter = repositories.openRepository(remoteRepositoryUrl, userName, password);

        Resource<GitRepositoryAdapter> resource = toResource(gitRepositoryAdapter);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Override
    public Resource<GitRepositoryAdapter> toResource(GitRepositoryAdapter entity) {
        Resource<GitRepositoryAdapter> resource = new Resource<>(entity);
        return resource;
    }

    @Override
    public Resource<GitRepositoryAdapter> process(Resource<GitRepositoryAdapter> resource) {
       resource.add(linkTo(RespositoryController.class).slash("upload").withRel("upload"));
       return resource;
    }
}
