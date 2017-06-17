package com.anxpp.demo.springrestdoc.core.entity;

import com.anxpp.demo.springrestdoc.core.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 测试实体
 * Created by yangtao on 2017/6/15.
 */
@Entity
public class User extends BaseEntity {


    //姓名
    @Column(length = 32)
    private String name;

    //性别
    @Column(length = 1)
    private Integer sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
