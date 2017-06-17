package com.anxpp.demo.springrestdoc.core.service;

import com.anxpp.demo.springrestdoc.core.entity.Company;
import org.springframework.data.domain.Page;

/**
 * service
 * Created by yangtao on 2017/6/15.
 */
public interface CompanyService {

    Company add(String name, String address, String phone);

    Page<Company> list(Integer pageNumber, Integer pageSize);

    /**
     * 查找公司
     *
     * @param pageNumber 分页页码
     * @param pageSize   分页页长
     * @param name       公司名称模糊匹配
     * @param address    公司地址模糊匹配
     * @param phone      公司电话精确匹配
     * @return 查找结果
     */
    Page<Company> find(Integer pageNumber, Integer pageSize, String name, String address, String phone);
}
