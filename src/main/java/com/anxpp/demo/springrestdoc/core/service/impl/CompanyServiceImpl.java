package com.anxpp.demo.springrestdoc.core.service.impl;

import com.anxpp.demo.springrestdoc.core.repo.CompanyRepo;
import com.anxpp.demo.springrestdoc.common.SimplePage;
import com.anxpp.demo.springrestdoc.core.entity.Company;
import com.anxpp.demo.springrestdoc.core.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service 实现
 * Created by yangtao on 2017/6/15.
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyRepo companyRepo;

    @Override
    public Company add(String name, String address, String phone) {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setPhone(phone);
        return companyRepo.save(company);
    }

    @Override
    public Page<Company> list(Integer pageNumber, Integer pageSize) {
        return companyRepo.findAll(SimplePage.build(pageNumber, pageSize));
    }

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
    @Override
    public Page<Company> find(Integer pageNumber, Integer pageSize, String name, String address, String phone) {
        return companyRepo.findAll(where(name, address, phone), SimplePage.build(pageNumber, pageSize));
    }

    /**
     * 条件查询
     *
     * @param name    公司名称模糊匹配
     * @param address 公司地址模糊匹配
     * @param phone   公司电话精确匹配
     * @return 条件
     */
    private Specification<Company> where(final String name, final String address, final String phone) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //名字
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            //名字
            if (StringUtils.hasText(address)) {
                predicates.add(cb.like(root.get("address"), "%" + address + "%"));
            }
            //电话
            if (StringUtils.hasText(phone))
                predicates.add(cb.equal(root.<String>get("phone"), phone));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
