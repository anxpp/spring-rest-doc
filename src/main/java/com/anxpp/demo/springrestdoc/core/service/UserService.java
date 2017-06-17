package com.anxpp.demo.springrestdoc.core.service;

import com.anxpp.demo.springrestdoc.core.entity.User;
import org.springframework.data.domain.Page;

/**
 * service
 * Created by yangtao on 2017/6/15.
 */
public interface UserService {

    /**
     * 添加用户
     *
     * @param name 用户姓名
     * @param sex  用户性别
     * @return 添加的用户
     */
    User add(String name, Integer sex);

    /**
     * 获取用户列表
     *
     * @param pageNumber 分页页码
     * @param pageSize   分页页长
     * @return 当页用户
     */
    Page<User> list(Integer pageNumber, Integer pageSize);

    /**
     * 查找用户
     *
     * @param pageNumber 分页页码
     * @param pageSize   分页页长
     * @param name       姓名
     * @param sex        性别
     * @return 查找结果
     */
    Page<User> find(Integer pageNumber, Integer pageSize, String name, Integer sex);
}
