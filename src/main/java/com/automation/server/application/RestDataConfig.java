package com.automation.server.application;

import com.automation.server.controller.GitRepositoryConnectionController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestDataConfig  extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);
        config.setBasePath("/api");
        config.setResourceMappingForDomainType(GitRepositoryConnectionController.class);
    }
}