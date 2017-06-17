package com.anxpp.demo.springrestdoc;

import com.anxpp.demo.springrestdoc.common.SimpleResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class SpringRestDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestDocApplication.class, args);
    }

    @GetMapping("/")
    public SimpleResponse index() {
        return SimpleResponse.create("hello word!");
    }
}
