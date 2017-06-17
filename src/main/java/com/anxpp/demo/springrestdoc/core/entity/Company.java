package com.anxpp.demo.springrestdoc.core.entity;

import com.anxpp.demo.springrestdoc.core.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 测试实体
 * Created by yangtao on 2017/6/15.
 */
@Entity
public class Company extends BaseEntity {

    @Column(length = 128)
    private String name;

    @Column(length = 128)
    private String address;

    @Column(length = 32)
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
