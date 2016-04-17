package com.automation.server.controller;

import com.automation.server.feature.FeatureFile;
import com.automation.server.feature.FeatureFileRepository;
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
        System.out.println(files.values().toArray()[0]);
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

    @Override
    public Resource<FeatureFile> toResource(FeatureFile entity) {
        return new Resource<>(entity);
    }

    @Override
    public Resource<FeatureFile> process(Resource<FeatureFile> resource) {
        resource.add(linkTo(FeatureController.class).slash(resource.getContent().getFileName()).withSelfRel());
        resource.add(linkTo(FeatureController.class).slash("{featureFileName}").withSelfRel());
        return resource;
    }

}
