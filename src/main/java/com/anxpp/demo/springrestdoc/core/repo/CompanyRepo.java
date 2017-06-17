package com.anxpp.demo.springrestdoc.core.repo;

import com.anxpp.demo.springrestdoc.core.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * 数据访问
 * <p>同时支持 RESTFul Service 和普通的Service调用
 * Created by yangtao on 2017/6/15.
 */
@RepositoryRestResource(collectionResourceRel = "company", path = "company")
public interface CompanyRepo extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {
}
