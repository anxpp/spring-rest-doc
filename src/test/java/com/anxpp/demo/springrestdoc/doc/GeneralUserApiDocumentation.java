package com.anxpp.demo.springrestdoc.doc;

import com.anxpp.demo.springrestdoc.SpringRestDocApplicationTests;
import com.anxpp.demo.springrestdoc.core.entity.User;
import com.anxpp.demo.springrestdoc.core.repo.UserRepo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Random;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GeneralUserApiDocumentation extends SpringRestDocApplicationTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void UserIndex() throws Exception {
        this.mockMvc.perform(
                get("/user/"))
                .andExpect(status().isOk())
                .andDo(document("user-index",
                        relaxedResponseFields(
                                fieldWithPath("data").type("Object").description("可用的API"))));
    }

    @Test
    public void UserAdd() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "anxpp");
        params.add("sex", "1");
        this.mockMvc.perform(
                post("/user/add").params(params))
                .andExpect(status().isOk())
                .andDo(document("user-add",
                        requestParameters(
                                parameterWithName("name").description("用户姓名"),
                                parameterWithName("sex").description("性别")),
                        relaxedResponseFields(
                                fieldWithPath("data.id").type("Integer").description("添加用户的ID"),
                                fieldWithPath("data.name").type("String").description("添加用户的姓名"),
                                fieldWithPath("data.sex").type("Integer").description("添加用户的性别"))));
    }

    @Test
    public void UserList() throws Exception {
        add(20);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "4");
        this.mockMvc.perform(
                get("/user/list").params(params))
                .andExpect(status().isOk())
                .andDo(document("user-list",
                        requestParameters(
                                REQUEST_PARAM_PAGE,
                                REQUEST_PARAM_SIZE),
                        relaxedResponseFields(
                                fieldWithPath("data.content").type("对象数组").description("用户列表"),
                                RESPONSE_PARAM_TOTAL_ELEMENTS,
                                RESPONSE_PARAM_TOTAL_PAGES,
                                RESPONSE_PARAM_LAST,
                                RESPONSE_PARAM_FIRST,
                                RESPONSE_PARAM_PAGE,
                                RESPONSE_PARAM_ELEMENTS,
                                RESPONSE_PARAM_SIZE)));
    }

    @Test
    public void UserFind() throws Exception {
        add(30);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "4");
        params.add("name", "0");
        params.add("sex", "0");
        this.mockMvc.perform(
                get("/user/find").params(params))
                .andExpect(status().isOk())
                .andDo(document("user-find",
                        relaxedRequestParameters(
                                REQUEST_PARAM_PAGE,
                                REQUEST_PARAM_SIZE),
                        relaxedResponseFields(
                                fieldWithPath("data.content").type("对象数组").description("用户列表"),
                                RESPONSE_PARAM_TOTAL_ELEMENTS,
                                RESPONSE_PARAM_TOTAL_PAGES,
                                RESPONSE_PARAM_LAST,
                                RESPONSE_PARAM_FIRST,
                                RESPONSE_PARAM_PAGE,
                                RESPONSE_PARAM_ELEMENTS,
                                RESPONSE_PARAM_SIZE)));
    }

    /**
     * 添加用户
     *
     * @param name 用户姓名
     * @param sex  用户性别
     */
    private void add(String name, Integer sex) {
        User user = new User();
        user.setName(name);
        user.setSex(sex);
        userRepo.save(user);
    }

    private void add(Integer count) {
        for (int i = 0; i < count; i++)
            add("user" + i, new Random().nextInt(1));
    }

}
