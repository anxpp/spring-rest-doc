package com.anxpp.demo.springrestdoc.core.service.impl;

import com.anxpp.demo.springrestdoc.common.SimplePage;
import com.anxpp.demo.springrestdoc.core.entity.User;
import com.anxpp.demo.springrestdoc.core.repo.UserRepo;
import com.anxpp.demo.springrestdoc.core.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service 实现
 * Created by yangtao on 2017/6/15.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepo userRepo;

    /**
     * 添加用户
     *
     * @param name 用户姓名
     * @param sex  用户性别
     * @return 添加的用户
     */
    @Override
    public User add(String name, Integer sex) {
        User user = new User();
        user.setName(name);
        user.setSex(sex);
        userRepo.save(user);
        return user;
    }

    /**
     * 获取用户列表
     *
     * @param pageNumber 分页页码
     * @param pageSize   分页页长
     * @return 当页用户
     */
    @Override
    public Page<User> list(Integer pageNumber, Integer pageSize) {
        return userRepo.findAll(SimplePage.build(pageNumber, pageSize));
    }

    /**
     * 查找用户
     *
     * @param pageNumber 分页页码
     * @param pageSize   分页页长
     * @param name       姓名
     * @param sex        性别
     * @return 查找结果
     */
    @Override
    public Page<User> find(Integer pageNumber, Integer pageSize, String name, Integer sex) {
        return userRepo.findAll(where(name, sex), SimplePage.build(pageNumber, pageSize));
    }

    /**
     * 条件查询
     *
     * @param name 名字模糊查找
     * @param sex  性别
     * @return 条件
     */
    private Specification<User> where(final String name, final Integer sex) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //名字
            if (name != null && !name.equals("")) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            //类型
            if (sex != null)
                predicates.add(cb.equal(root.<Integer>get("sex"), sex));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
