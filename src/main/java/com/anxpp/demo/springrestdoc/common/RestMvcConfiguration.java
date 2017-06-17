package com.anxpp.demo.springrestdoc.common;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * restful 配置
 * Created by anxpp.com on 2017/6/16.
 */
@Component
public class RestMvcConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/restful").getCorsRegistry().addMapping("/restful/**");
    }
}