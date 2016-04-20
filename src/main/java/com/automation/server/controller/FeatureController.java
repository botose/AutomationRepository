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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@BasePathAwareController
@RequestMapping(value = "/localRepo")
public class FeatureController implements ResourceProcessor<Resource<FeatureFile>>, ResourceAssembler<FeatureFile, Resource<FeatureFile>> {

    @Autowired
    GitRepositoryStore gitRepositoryStore;

    @Autowired
    FeatureFileRepository featureFileRepository;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<FeatureFile>> getAll() throws IOException {
        Map<String, FeatureFile> files;
        ifNoFilesLoadThem();
        files = featureFileRepository.getFiles();
        Resources<FeatureFile> resources = new Resources<>(files.values());

        resources.add(linkTo(FeatureController.class).withSelfRel());
        resources.add(linkTo(FeatureController.class).slash("reloadFiles").withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(value = "reloadFiles", method = RequestMethod.GET)
    public ResponseEntity<Resources<FeatureFile>> reloadFiles() throws IOException {
        String path = gitRepositoryStore.getActiveRepository().getLocalPath();
        featureFileRepository.loadFiles(path);
        Map<String, FeatureFile> files = featureFileRepository.getFiles();

        Resources<FeatureFile> resources = new Resources<>(files.values());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    private void ifNoFilesLoadThem() throws IOException {
        if(!featureFileRepository.hasFiles()) {
            String path = gitRepositoryStore.getActiveRepository().getLocalPath();
            featureFileRepository.loadFiles(path);
        }
    }

    @RequestMapping(value = "/{featureFileName}", method = RequestMethod.GET)
    public ResponseEntity<Resource<FeatureFile>> get(@PathVariable String featureFileName) throws IOException {
        ifNoFilesLoadThem();
        Resource<FeatureFile> resource = toResource(featureFileRepository.getFile(featureFileName));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "/{featureFileName}", method = RequestMethod.POST)
    public ResponseEntity<Resource<FeatureFile>> write(@PathVariable String featureFileName,
                                       @RequestBody FeatureFile featureFile) throws IOException {
        ifNoFilesLoadThem();
        featureFileRepository.setFile(featureFileName, featureFile);
        featureFileRepository.updateFile(featureFileName);

        Resource<FeatureFile> resource = toResource(featureFileRepository.getFile(featureFileName));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchScenario", method = RequestMethod.GET)
    public ResponseEntity<Resources<ExtendedScenario>> searchForScenario(
            @RequestParam String scenarioTitle) throws IOException {

        ifNoFilesLoadThem();

        List<ExtendedScenario> scenarios = new ArrayList<>();
        for(FeatureFile file : featureFileRepository.getFiles().values()) {
            for(Scenario scenario : file.getScenarios()) {
                if(scenario.getTitle().contains(scenarioTitle)) {
                    scenarios.add(new ExtendedScenario(scenario, file.getFileName()));
                }
            }
        }

        Resources<ExtendedScenario> resource = new Resources<>(scenarios);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Override
    public Resource<FeatureFile> toResource(FeatureFile entity) {
        return new Resource<>(entity);
    }

    @Override
    public Resource<FeatureFile> process(Resource<FeatureFile> resource) {
        resource.add(linkTo(FeatureController.class).slash(resource.getContent().getFileName()).withSelfRel());
        resource.add(linkTo(FeatureController.class).slash("{featureFileName}").withSelfRel());
        resource.add(linkTo(FeatureController.class).slash("reloadFiles").withSelfRel());
        resource.add(linkTo(FeatureController.class).slash(resource.getContent().getFileName()).slash("scenarios").withSelfRel());
        return resource;
    }

    public class ExtendedScenario {
        public Scenario scenario;
        public String fileName;

        public ExtendedScenario(Scenario scenario, String fileName) {
            this.scenario = scenario;
            this.fileName = fileName;
        }
    }

}
