package com.anxpp.demo.springrestdoc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

/**
 * 父类，用于准备好相关环境
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRestDocApplicationTests {

    //部分字段可以如下统一定义
    //请求时的分页参数
    protected static final ParameterDescriptor REQUEST_PARAM_PAGE = parameterWithName("pageNumber").description("页码，第一页页码为0，不传或传值非法设置为默认值0");
    protected static final ParameterDescriptor REQUEST_PARAM_SIZE = parameterWithName("pageSize").description("页长，不传或传值非法默认设置为4");
    //响应是的分页字段说明
    protected static final FieldDescriptor RESPONSE_PARAM_TOTAL_ELEMENTS = fieldWithPath("data.totalElements").type("Integer").description("总条数");
    protected static final FieldDescriptor RESPONSE_PARAM_TOTAL_PAGES = fieldWithPath("data.totalPages").type("Integer").description("总页数");
    protected static final FieldDescriptor RESPONSE_PARAM_LAST = fieldWithPath("data.last").type("bool").description("是否最后一页");
    protected static final FieldDescriptor RESPONSE_PARAM_FIRST = fieldWithPath("data.first").type("bool").description("是否第一页");
    protected static final FieldDescriptor RESPONSE_PARAM_PAGE = fieldWithPath("data.number").type("Integer").description("第几页");
    protected static final FieldDescriptor RESPONSE_PARAM_ELEMENTS = fieldWithPath("data.numberOfElements").type("Integer").description("当页元素个数");
    protected static final FieldDescriptor RESPONSE_PARAM_SIZE = fieldWithPath("data.size").type("Integer").description("分页长度");

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        //默认生成的文档片段
        Snippet[] defaultSnippets = new Snippet[]{CliDocumentation.curlRequest(), CliDocumentation.httpieRequest(), HttpDocumentation.httpRequest(), HttpDocumentation.httpResponse(), PayloadDocumentation.requestBody(), PayloadDocumentation.responseBody()};
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)
                        //此处也支持生成markdown文档片段，但不能生成html
                        .snippets().withTemplateFormat(TemplateFormats.asciidoctor()).withEncoding("UTF-8")
                        .withDefaults(defaultSnippets)
                        .and()
                        .uris().withScheme("https").withHost("api.yupaopao.com").withPort(443)
                        .and()
                )
                .build();
    }

}
