package com.automation.server.controller;

import com.automation.server.feature.FeatureFile;
import com.automation.server.feature.FeatureFileRepository;
import com.automation.server.repository.GitRepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Map;

@BasePathAwareController
@RequestMapping(value = "/localRepo")
public class FeatureController implements ResourceProcessor<Resource<FeatureFile>>, ResourceAssembler<FeatureFile, Resource<FeatureFile>> {

    @Autowired
    GitRepositoryStore gitRepositoryStore;

    @Autowired
    FeatureFileRepository featureFileRepository;


    @RequestMapping(method = RequestMethod.GET)
    public Resources<FeatureFile> getAll() throws IOException {
        Map<String, FeatureFile> files;
        if(!featureFileRepository.hasFiles()) {
            String path = gitRepositoryStore.getActiveRepository().getLocalPath();
            featureFileRepository.loadFiles(path);
        }
        files = featureFileRepository.getFiles();

        return new Resources<>(files.values());
    }

    @RequestMapping(value = "/{featureFileName}", method = RequestMethod.GET)
    public Resource<FeatureFile> get(@PathVariable String featureFileName) throws NoSuchFileException {
        return toResource(featureFileRepository.getFile(featureFileName));
    }

    @RequestMapping(value = "/{featureFileName}", method = RequestMethod.POST)
    public Resource<FeatureFile> write(@PathVariable String featureFileName,
                                       @RequestBody FeatureFile featureFile) throws NoSuchFileException {
        featureFileRepository.setFile(featureFileName, featureFile);
        featureFileRepository.updateFile(featureFileName);

        return toResource(featureFileRepository.getFile(featureFileName));
    }

    @Override
    public Resource<FeatureFile> toResource(FeatureFile entity) {
        return null;
    }

    @Override
    public Resource<FeatureFile> process(Resource<FeatureFile> resource) {
        return null;

    }

}
