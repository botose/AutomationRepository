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

import java.net.URISyntaxException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@BasePathAwareController
@RequestMapping(value = "/repositories")
public class RespositoryController implements ResourceProcessor<Resource<GitRepository>>, ResourceAssembler<GitRepository, Resource<GitRepository>> {
    @Autowired
    private GitRepositoryStore repositories;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<GitRepository>> openRepo(@RequestBody RepositoryData repositoryData) {
        GitRepository gitRepository = null;
        try {
            gitRepository = repositories.openRepository(
                    repositoryData.remoteRepositoryUrl,
                    repositoryData.userName,
                    repositoryData.password);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (GitExecutionException e) {
            e.printStackTrace();
        }

        Resource<GitRepository> resource = toResource(gitRepository);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resource<GitRepository>> getRepo() {
        GitRepository gitRepository = repositories.getActiveRepository();

        Resource<GitRepository> resource = toResource(gitRepository);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

        @Override
    public Resource<GitRepository> toResource(GitRepository entity) {
        Resource<GitRepository> resource = new Resource<>(entity);
        return resource;
    }

    @Override
    public Resource<GitRepository> process(Resource<GitRepository> resource) {
       resource.add(linkTo(RespositoryController.class).slash("upload").withRel("upload"));
       return resource;
    }

    static public class RepositoryData {
        String remoteRepositoryUrl;
        String userName;
        String password;
    }
}
