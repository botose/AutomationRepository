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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@BasePathAwareController
@RequestMapping(value = "/git")
public class GitController implements ResourceProcessor<Resource<GitRepository>>, ResourceAssembler<GitRepository, Resource<GitRepository>> {
    @Autowired
    private GitRepositoryStore repositories;

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public ResponseEntity<Resource<GitRepository>> add() throws GitExecutionException {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }

        gitRepository.add(".");
        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.OK);
    }

    @RequestMapping(value = "/commit",method = RequestMethod.GET)
    public ResponseEntity<Resource<GitRepository>> commit(@RequestParam String commitMessage) throws GitExecutionException {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }

        gitRepository.commit(commitMessage);
        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.OK);
    }

    @RequestMapping(value = "/push",method = RequestMethod.GET)
    public ResponseEntity<Resource<GitRepository>> push() throws GitExecutionException {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }

        gitRepository.push();
        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.OK);
    }

    @RequestMapping(value = "/addCommitPush",method = RequestMethod.GET)
    public ResponseEntity<Resource<GitRepository>> acp(@RequestParam String commitMessage) throws GitExecutionException {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }

        gitRepository.add(".");
        gitRepository.commit(commitMessage);
        gitRepository.push();
        return new ResponseEntity<>(toResource(gitRepository), HttpStatus.OK);
    }

    @RequestMapping(value = "/history",method = RequestMethod.GET)
    public ResponseEntity<List<String>> history(@RequestParam(defaultValue = "0") Integer numberOfCommit) throws GitExecutionException, IOException {
        GitRepository gitRepository = repositories.getActiveRepository();
        if(gitRepository == null) {
            throw new IllegalStateException("No active git repository, open one with RequestMethod.POST");
        }

        List<String> changes = gitRepository.getDiff(numberOfCommit);
        return new ResponseEntity<>(changes, HttpStatus.OK);
    }

    @Override
    public Resource<GitRepository> toResource(GitRepository entity) {
        Resource<GitRepository> resource = new Resource<>(entity);
        return resource;
    }

    @Override
    public Resource<GitRepository> process(Resource<GitRepository> resource) {
        resource.add(linkTo(GitController.class).slash("add").withSelfRel());
        resource.add(linkTo(GitController.class).slash("commit?commitMessage=").withSelfRel());
        resource.add(linkTo(GitController.class).slash("push").withSelfRel());
        resource.add(linkTo(GitController.class).slash("history?numberOfCommit=").withSelfRel());
        resource.add(linkTo(GitController.class).slash("addCommitPush?commitMessage=").withSelfRel());
        return resource;
    }
}
