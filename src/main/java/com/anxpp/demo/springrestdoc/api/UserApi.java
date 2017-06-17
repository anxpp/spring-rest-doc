package com.anxpp.demo.springrestdoc.api;

import com.anxpp.demo.springrestdoc.common.SimpleResponse;
import com.anxpp.demo.springrestdoc.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 测试
 * Created by yangtao on 2017/6/15.
 */
@RestController
@RequestMapping("/user")
public class UserApi {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private UserService userService;

    @GetMapping
    public SimpleResponse index() {
        HashMap<String, String> map = new HashMap<>();
        map.put("add", "添加用户");
        map.put("list", "用户列表");
        map.put("find", "查找用户");
        return SimpleResponse.create(map);
    }

    @PostMapping("/add")
    public SimpleResponse add(String name, Integer sex) {
        log.info("test:add:" + name);
        return SimpleResponse.create(userService.add(name, sex));
    }

    @GetMapping("/list")
    public SimpleResponse list(Integer pageNumber, Integer pageSize) {
        return SimpleResponse.create(userService.list(pageNumber, pageSize));
    }

    @GetMapping("/find")
    public SimpleResponse find(Integer pageNumber, Integer pageSize, String name, Integer sex) {
        return SimpleResponse.create(userService.find(pageNumber, pageSize, name, sex));
    }
}
