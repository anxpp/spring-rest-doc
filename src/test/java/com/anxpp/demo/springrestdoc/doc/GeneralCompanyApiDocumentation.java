package com.anxpp.demo.springrestdoc.doc;

import com.anxpp.demo.springrestdoc.SpringRestDocApplicationTests;
import com.anxpp.demo.springrestdoc.core.repo.CompanyRepo;
import com.anxpp.demo.springrestdoc.core.entity.Company;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Random;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GeneralCompanyApiDocumentation extends SpringRestDocApplicationTests {

    @Autowired
    private CompanyRepo companyRepo;

    @Test
    public void companyAdd() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "yupaopao");
        params.add("address", "上海市徐汇区");
        params.add("phone", "12345678");
        this.mockMvc.perform(
                post("/company/add").params(params))
                .andExpect(status().isOk())
                .andDo(document("company-add",
                        requestParameters(
                                parameterWithName("name").description("公司名称"),
                                parameterWithName("address").description("公司地址"),
                                parameterWithName("phone").description("公司电话")),
                        relaxedResponseFields(
                                fieldWithPath("data.id").type("Integer").description("添加公司的ID"),
                                fieldWithPath("data.name").type("data.name").description("添加公司的名称"),
                                fieldWithPath("data.address").type("data.name").description("添加公司的地址"),
                                fieldWithPath("data.phone").type("Integer").description("添加公司的联系电话"))));
    }

    @Test
    public void companyList() throws Exception {
        add(50);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "4");
        this.mockMvc.perform(
                get("/company/list").params(params))
                .andExpect(status().isOk())
                .andDo(document("company-list",
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
    public void companyFind() throws Exception {
        add(30);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "4");
        params.add("name", "0");
        params.add("address", "");
        params.add("phone", "");
        this.mockMvc.perform(
                get("/company/find").params(params))
                .andExpect(status().isOk())
                .andDo(document("company-find",
                        requestParameters(
                                REQUEST_PARAM_PAGE,
                                REQUEST_PARAM_SIZE,
                                parameterWithName("name").description("公司名称模糊匹配"),
                                parameterWithName("address").description("公司地址模糊匹配"),
                                parameterWithName("phone").description("公司电话精确匹配")),
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
     * 添加公司
     */
    private void add(Integer count) {
        final ArrayList<Company> companyList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Company company = new Company();
            company.setName("company" + i + " " + new Random().nextInt(9999));
            company.setAddress("address" + i);
            company.setPhone(String.valueOf(new Random().nextInt(89999999) + 10000000));
            companyList.add(company);
        }
        companyRepo.save(companyList);
    }

}
