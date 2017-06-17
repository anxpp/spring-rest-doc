package com.anxpp.demo.springrestdoc.api;

import com.anxpp.demo.springrestdoc.common.SimpleResponse;
import com.anxpp.demo.springrestdoc.core.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试
 * Created by yangtao on 2017/6/15.
 */
@RestController
@RequestMapping("/company")
public class CompanyApi {

    @Resource
    private CompanyService companyService;

    @PostMapping("/add")
    public SimpleResponse add(String name, String address, String phone) {
        return SimpleResponse.create(companyService.add(name, address, phone));
    }

    @GetMapping("/list")
    public SimpleResponse list(Integer pageNumber, Integer pageSize) {
        return SimpleResponse.create(companyService.list(pageNumber, pageSize));
    }

    @GetMapping("/find")
    public SimpleResponse find(Integer pageNumber, Integer pageSize, String name, String address, String phone) {
        return SimpleResponse.create(companyService.find(pageNumber, pageSize, name, address, phone));
    }
}
