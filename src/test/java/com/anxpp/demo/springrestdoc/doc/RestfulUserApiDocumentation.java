package com.anxpp.demo.springrestdoc.doc;

import com.anxpp.demo.springrestdoc.SpringRestDocApplicationTests;
import com.anxpp.demo.springrestdoc.core.entity.User;
import com.anxpp.demo.springrestdoc.core.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.relaxedLinks;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestfulUserApiDocumentation extends SpringRestDocApplicationTests {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void restfulUserProfile() throws Exception {
        this.mockMvc.perform(
                get("/restful/profile/user"))
                .andExpect(status().isOk())
                .andDo(document("restful-user-profile",
                        responseFields(
                                subsectionWithPath("alps.version").description("接口版本"),
                                subsectionWithPath("alps.descriptors").description("接口详细描述")
                        )
                ));
    }

    @Test
    public void restfulUserList() throws Exception {
        empty();
        add(3);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "2");
        this.mockMvc.perform(
                get("/restful/user").params(params))
                .andExpect(status().isOk())
.andDo(document("restful-user-list",
        requestParameters(
                parameterWithName("page").description("分页页码"),
                parameterWithName("size").description("分页页长")
        ),
        relaxedLinks(
                linkWithRel("self").description("The <<resources-restful-user-index,user resource>>"),
                linkWithRel("first").description("第一页"),
                linkWithRel("next").description("下一页"),
                linkWithRel("last").description("最后一页"),
                linkWithRel("profile").description("The ALPS profile for the service")),
        responseFields(
                subsectionWithPath("_links").description("<<resources-restful-user-index,Links>> user resources"),
                subsectionWithPath("_embedded.user").description("用户列表").type("User对象数组"),
                subsectionWithPath("page").description("分页信息").type("Object"))));
    }

    @Test
    public void restfulUserAdd() throws Exception {

        Map<String, Object> user = new HashMap<>();
        user.put("name", "testAdd");
        user.put("sex", 1);

        this.mockMvc.perform(
                post("/restful/user").contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(user))).andExpect(
                status().isCreated())
                .andDo(document("restful-user-add",
                        requestFields(
                                fieldWithPath("name").description("用户姓名"),
                                fieldWithPath("sex").description("用户性别，0=女，1=男"))
                ));
    }

    @Test
    public void restfulUserUpdate() throws Exception {

        Map<String, Object> user = new HashMap<>();
        user.put("name", "testUpdate");
        user.put("sex", 1);

        String userLocation = this.mockMvc
                .perform(
                        post("/restful/user").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("name", "testUpdateComplete");

        this.mockMvc.perform(
                patch(userLocation).contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isNoContent())
                .andDo(document("restful-user-update",
                        requestFields(
                                fieldWithPath("name").description("用户姓名").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("sex").description("用户性别").type(JsonFieldType.STRING).optional())));
    }

    @Test
    public void restfulUserFindBySex() throws Exception {
        empty();
        add(100);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sex", "1");
        params.add("page", "0");
        params.add("size", "2");

        this.mockMvc.perform(
                get("/restful/user/search/sex").params(params))
                .andExpect(status().isOk())
                .andDo(document("restful-user-find-sex",
                        requestParameters(
                                parameterWithName("sex").description("用户性别"),
                                parameterWithName("page").description("分页页码"),
                                parameterWithName("size").description("分页页长")
                        )));
    }

    @Test
    public void restfulUserFindByNameLike() throws Exception {
        empty();
        add(200);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "%15%");
        params.add("page", "0");
        params.add("size", "2");

        this.mockMvc.perform(
                get("/restful/user/search/name").params(params))
                .andExpect(status().isOk())
                .andDo(document("restful-user-find-name",
                        requestParameters(
                                parameterWithName("name").description("用户姓名，如果需要模糊匹配，请在参数前后按需添加占位符如：%key%"),
                                parameterWithName("page").description("分页页码"),
                                parameterWithName("size").description("分页页长")
                        )));
    }

    @Test
    public void restfulUserDelete() throws Exception {
        empty();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "testDelete");
        user.put("sex", 1);

        long count = userRepo.count();

        String userLocation = this.mockMvc
                .perform(
                        post("/restful/user").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Assert.assertEquals(count + 1, userRepo.count());

        this.mockMvc.perform(
                delete(userLocation))
                .andExpect(status().isNoContent())
                .andDo(document("restful-user-delete"));

        Assert.assertEquals(count, userRepo.count());
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
            add("user" + i, new Random().nextInt(2));
    }

    private void empty() {
        userRepo.deleteAll();
    }

}
