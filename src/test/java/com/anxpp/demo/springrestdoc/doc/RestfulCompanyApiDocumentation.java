package com.anxpp.demo.springrestdoc.doc;

import com.anxpp.demo.springrestdoc.SpringRestDocApplicationTests;
import com.anxpp.demo.springrestdoc.core.entity.Company;
import com.anxpp.demo.springrestdoc.core.repo.CompanyRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.relaxedLinks;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestfulCompanyApiDocumentation extends SpringRestDocApplicationTests {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void restfulCompanyProfile() throws Exception {
        this.mockMvc.perform(
                get("/restful/profile/company"))
                .andExpect(status().isOk())
                .andDo(document("restful-company-profile",
                        responseFields(
                                subsectionWithPath("alps.version").description("接口版本"),
                                subsectionWithPath("alps.descriptors").description("接口详细描述")
                        )
                ));
    }

    @Test
    public void restfulCompanyList() throws Exception {
        empty();
        add(3);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "2");
        this.mockMvc.perform(
                get("/restful/company").params(params))
                .andExpect(status().isOk())
                .andDo(document("restful-company-list",
                        requestParameters(
                                parameterWithName("page").description("分页页码"),
                                parameterWithName("size").description("分页页长")
                        ),
                        relaxedLinks(
                                linkWithRel("self").description("The <<resources-restful-company-index,company resource>>"),
                                linkWithRel("first").description("第一页"),
                                linkWithRel("next").description("下一页"),
                                linkWithRel("last").description("最后一页"),
                                linkWithRel("profile").description("The ALPS profile for the service")),
                        responseFields(
                                subsectionWithPath("_links").description("<<resources-restful-company-index,Links>> company resources"),
                                subsectionWithPath("_embedded.company").description("公司列表").type("Company对象数组"),
                                subsectionWithPath("page").description("分页信息").type("Object"))));
    }

    @Test
    public void restfulCompanyAdd() throws Exception {

        Map<String, Object> company = new HashMap<>();
        company.put("name","testAdd");
        company.put("sex",1);

        this.mockMvc.perform(
                post("/restful/company").contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(company))).andExpect(
                status().isCreated())
                .andDo(document("restful-Company-add",
                        requestFields(
                                fieldWithPath("name").description("用户姓名"),
                                fieldWithPath("sex").description("用户性别，0=女，1=男"))
                ));
    }

    /**
     * 添加Company
     *
     * @param name 名称
     */
    private void add(String name, Integer sex) {
        Company Company = new Company();
        Company.setName(name);
        companyRepo.save(Company);
    }

    private void add(Integer count) {
        for (int i = 0; i < count; i++)
            add("Company" + i, new Random().nextInt(1));
    }

    private void empty(){
        companyRepo.deleteAll();
    }

}
