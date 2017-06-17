package com.anxpp.demo.springrestdoc.doc;

import com.anxpp.demo.springrestdoc.SpringRestDocApplicationTests;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestfulIndexApiDocumentation extends SpringRestDocApplicationTests {

    @Test
    public void restfulIndex() throws Exception {
        this.mockMvc.perform(get("/restful"))
                .andExpect(status().isOk())
                .andDo(document("restful-index",
                        links(
                                linkWithRel("user").description("The <<resources-restful-user,user 接口>>"),
                                linkWithRel("company").description("The <<resources-restful-company,company 接口>>"),
                                linkWithRel("profile").description("接口信息描述接口")),
                        responseFields(
                                subsectionWithPath("_links").description("<<resources-restful-index,Links>> 到其他接口"))));
    }

    @Test
    public void restfulProfile() throws Exception {
        this.mockMvc.perform(get("/restful/profile"))
                .andExpect(status().isOk())
                .andDo(document("restful-profile",
                        links(
                                linkWithRel("self").description("当前接口信息描述"),
                                linkWithRel("user").description("The <<resources-restful-user,user 接口描述>>"),
                                linkWithRel("company").description("The <<resources-restful-company,company 接口描述>>")),
                        responseFields(
                                subsectionWithPath("_links").description("<<resources-restful-index,Links>> 到其他接口"))));
    }
}
