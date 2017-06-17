package com.anxpp.demo.springrestdoc.core.repo;

import com.anxpp.demo.springrestdoc.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * 数据访问
 * <p>同时支持 RESTFul Service 和普通的Service调用
 * Created by yangtao on 2017/6/15.
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @RestResource(path = "name", rel = "find-by-name")
    Page<User> findByNameLike(@Param("name") String name, Pageable pageable);

    @RestResource(path = "sex", rel = "find-by-sex")
    Page<User> findBySex(@Param("sex") Integer name, Pageable pageable);
}
