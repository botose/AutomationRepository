package com.automation.server.controller;

import com.automation.server.feature.FeatureFile;
import com.automation.server.feature.FeatureFileRepository;
import com.automation.server.feature.Scenario;
import com.automation.server.repository.GitRepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@BasePathAwareController
@RequestMapping(value = "/localRepo/{fileName}/scenarios")
public class ScenarioController implements ResourceProcessor<Resource<Scenario>>, ResourceAssembler<Scenario, Resource<Scenario>> {

    @Autowired
    GitRepositoryStore gitRepositoryStore;

    @Autowired
    FeatureFileRepository featureFileRepository;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Scenario>> getAll(@PathVariable String fileName) throws IOException {
        Resources<Scenario> resources = new Resources<>(featureFileRepository.getFile(fileName).getScenarios());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(value = "/{scenarioTitle}", method = RequestMethod.GET)
    public ResponseEntity<Resource<Scenario>> get(@PathVariable String fileName,
                                  @PathVariable String scenarioTitle) throws NoSuchFileException {
        List<Scenario> scenarios = featureFileRepository.getFile(fileName).getScenarios();
        Resource<Scenario> resource = null;
        for(Scenario scenario : scenarios) {
            if(scenario.getTitle().contains(scenarioTitle)) {
                resource = toResource(scenario);
            }
        }
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "/{scenarioTitle}", method = RequestMethod.POST)
    public ResponseEntity<Resource<Scenario>> get(@PathVariable String fileName,
                                  @PathVariable String scenarioTitle,
                                  @RequestBody Scenario scenario) throws NoSuchFileException {
        FeatureFile featureFile = featureFileRepository.getFile(fileName);
        featureFile.setScenario(scenarioTitle, scenario);
        featureFileRepository.updateFile(fileName);
        return new ResponseEntity<>(toResource(scenario), HttpStatus.OK);
    }

    @RequestMapping(value = "/{scenarioTitle}", method = RequestMethod.DELETE)
    public ResponseEntity<Resource<Scenario>> delete(@PathVariable String fileName,
                                  @PathVariable String scenarioTitle) throws NoSuchFileException {
        FeatureFile featureFile = featureFileRepository.getFile(fileName);
        featureFile.removeScenario(scenarioTitle);
        featureFileRepository.updateFile(fileName);
        Resource<Scenario> scenarioResource = null;
        return new ResponseEntity<>(scenarioResource, HttpStatus.OK);
    }

    @Override
    public Resource<Scenario> toResource(Scenario entity) {
        return new Resource<>(entity);
    }

    @Override
    public Resource<Scenario> process(Resource<Scenario> resource) {
        resource.add(linkTo(FeatureController.class).withSelfRel());
        return resource;
    }

}
