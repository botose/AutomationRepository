package com.automation.server.controller;

import com.automation.server.model.ScenarioFile;
import com.automation.server.repository.GitRepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@BasePathAwareController
@RestController
@RequestMapping(value = "/localRepo")
public class ScenarioController extends ResourceProcessor<Resource<ScenarioFile>>, ResourceAssembler<ScenarioFile, Resource<ScenarioFile>> {

    @Autowired
    GitRepositoryStore gitRepositoryStore;

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<ScenarioFile> getAll() {

        return null;
    }

    @RequestMapping(value = "/{ScenarioFile}", method = RequestMethod.GET)
    public Resource<ScenarioFile> get(@PathVariable String fileName) {

        return null;
    }

    @Override
    public Resource<ScenarioFile> toResource(ScenarioFile entity) {
        return null;
    }

    @Override
    public Resource<ScenarioFile> process(Resource<ScenarioFile> resource) {
        return null;

    }

}
