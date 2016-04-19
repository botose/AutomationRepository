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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@BasePathAwareController
@RequestMapping(value = "/localRepo/{fileName}/scenarios")
public class ScenarioController implements ResourceProcessor<Resource<Scenario>>, ResourceAssembler<Scenario, Resource<Scenario>> {

    @Autowired
    GitRepositoryStore gitRepositoryStore;

    @Autowired
    FeatureFileRepository featureFileRepository;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Scenario>> getAll(@PathVariable String fileName) throws IOException {
        ifNoFilesLoadThem();
        Resources<Scenario> resources = new Resources<>(featureFileRepository.getFile(fileName).getScenarios());

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Resource<Scenario>> get(@PathVariable String fileName,
                                                  @RequestParam String scenarioTitle) throws IOException {
        ifNoFilesLoadThem();
        List<Scenario> scenarios = featureFileRepository.getFile(fileName).getScenarios();
        Resource<Scenario> resource = null;
        for(Scenario scenario : scenarios) {
            if(scenario.getTitle().contains(scenarioTitle)) {
                resource = toResource(scenario);
            }
        }
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Resource<Scenario>> get(@PathVariable String fileName,
                                                  @RequestParam String scenarioTitle,
                                                  @RequestBody Scenario scenario) throws IOException {
        ifNoFilesLoadThem();
        FeatureFile featureFile = featureFileRepository.getFile(fileName);
        featureFile.setScenario(scenarioTitle, scenario);
        featureFileRepository.updateFile(fileName);
        return new ResponseEntity<>(toResource(scenario), HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.DELETE)
    public ResponseEntity<Resource<Scenario>> delete(@PathVariable String fileName,
                                                     @RequestParam String scenarioTitle) throws IOException {
        ifNoFilesLoadThem();
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
        return resource;
    }

    private void ifNoFilesLoadThem() throws IOException {
        if(!featureFileRepository.hasFiles()) {
            String path = gitRepositoryStore.getActiveRepository().getLocalPath();
            featureFileRepository.loadFiles(path);
        }
    }

}
