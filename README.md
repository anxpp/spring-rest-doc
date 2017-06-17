# Spring REST Docs 简易教程


-------------------

- *本文相当于官方文档的部分翻译版，Demo中还做了一些扩展，以使整个文档生成工作更加顺利，完整文档请参考[Spring REST Docs 官方文档][1]。*

- *文章参考：*

    *1、[http://blog.anxpp.com/index.php/archives/1071/][7]*

    *2、[http://blog.csdn.net/anxpp/article/details/73330832][6]*

-------------------

## 目录

[TOC]

-------------------

## 简介
> Spring REST Docs 可以生成准确可读的RESTful Service文档（当然一般的API文档生成也依然得心应手）

    网上随便找了找，中文资料几乎没有，只能硬着头皮带着跛脚的英语看起了官方文档，并分享如下...
    文章写得很渣，直接下载文末的 Demo 通常会上手更快!

Spring 官方文档都是用 Spring REST Docs 生成的，其简洁性和可读性也是大家都认可的，不过 Spring REST Docs 的优势远不止于此：

 - 代码无污染：Spring REST Docs 基于单元测试生成文档片段（snippets），不会侵入到源码中，所以就不会使得源码变得越来越臃肿。
 - 单元测试：因为文档的生成是依赖单元测试的，以此可以矫正一些不爱写单元测试的程序员小哥哥。
 - 支持 markdown：修改一行配置代码即可支持生成 MarkDown 语法的文档片段（不过要生成html文档目前官方只支持adoc文档）。
 - 文档自动更新：文档自动更新？文档自动更新！默认的，在构建的时候，会首先运行单元测试，此时便生成了文档片段，然后在打包时，通过添加 asciidoctor-maven-plugin 插件即可生成最终的文档，只要是规范的开发过程，文档都会随版本的每次发布而自动更新！
 - 可读性高：Spring 官方文档就是个例子。
 - ......

![Spring官方文档示例](http://img.blog.csdn.net/20170616141334191?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYW54cHA=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

我不是针对谁，我是说 Spring 全家桶的每一个组件，都堪称极品。

-------------------

## 整合 Spring REST Docs

Spring REST Docs 同时支持Maven和Gradle，不过个人偏爱Maven，一下仅介绍Maven下的整合，Gradle请查看[官方文档][1]。
生成文档片段的单元测试同时支持 Spring MVC Test 和 REST Assured 2/3，对比了一下，Spring MVC Test的代码更加简洁，
所以本文也使用 Spring MVC Test 来做单元测试，如果有兴趣使用 Assured 的，依然还是参考[官方文档][1]吧。

### Build configuration

在pom.xml文件中添加依赖和构建插件：

 - spring-restdocs-mockmvc用于编写单元测试
 - 添加Asciidoctor插件
 - prepare-package 参数允许文档被添加到程序包中

```
<dependency> 
	<groupId>org.springframework.restdocs</groupId>
	<artifactId>spring-restdocs-mockmvc</artifactId>
	<version>1.2.1.RELEASE</version>
	<scope>test</scope>
</dependency>

<build>
	<plugins>
		<plugin> 
			<groupId>org.asciidoctor</groupId>
			<artifactId>asciidoctor-maven-plugin</artifactId>
			<version>1.5.3</version>
			<executions>
				<execution>
					<id>generate-docs</id>
					<phase>prepare-package</phase> 
					<goals>
						<goal>process-asciidoc</goal>
					</goals>
					<configuration>
						<backend>html</backend>
						<doctype>book</doctype>
					</configuration>
				</execution>
			</executions>
			<dependencies>
				<dependency> 
					<groupId>org.springframework.restdocs</groupId>
					<artifactId>spring-restdocs-asciidoctor</artifactId>
					<version>1.2.1.RELEASE</version>
				</dependency>
			</dependencies>
		</plugin>
	</plugins>
</build>
```
这样，在单元测试时，就能在对应的输出目录里生成对应的文档片段（adoc文档片段或者MarkDown文档片段）。

#### 将文档打包到jar中

如果希望生成的jar中直接包含文档，可以在 maven-resources 插件中配置将文档添加到 jar 的指定目录中，如下是放到 static/docs 中：

```
<plugin> 
	<artifactId>maven-resources-plugin</artifactId>
	<version>2.7</version>
	<executions>
		<execution>
			<id>copy-resources</id>
			<phase>prepare-package</phase>
			<goals>
				<goal>copy-resources</goal>
			</goals>
			<configuration> 
				<outputDirectory>
					${project.build.outputDirectory}/static/docs
				</outputDirectory>
				<resources>
					<resource>
						<directory>
							${project.build.directory}/generated-docs
						</directory>
					</resource>
				</resources>
			</configuration>
		</execution>
	</executions>
</plugin>
```

### 打包时自动生成文档片段

文档片段是运行单元测试时自动生成的，Maven直接打包时一般是不会运行单元测试的（Gradle 在构建时默认是会运行单元测试的），不过做过全网回归的，基本都用过 maven 的 surefire 插件，生成文档的单元测试 .java 文件统一使用特定的后缀，然后每次打包前运行这些单元测试即可，配置如下：

```
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<configuration>
		<includes>
			<include>**/*Documentation.java</include>
		</includes>
	</configuration>
</plugin>
```

### 生成文档片段

Spring REST Docs  通过 Spring’s MVC Test 向 service 发起请求来生成请求和响应相关信息的文档片段。

**配置单元测试**

首先需要声明一个 public 的 JUnitRestDocumentation ，并且加上 @Rule 注解，JUnitRestDocumentation 会按约定自动完成配置（约定大于配置），在 Maven 下默认文档片段输出目录为：target/generated-snippets

```
@Rule
public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
```

其中 JUnitRestDocumentation 提供一个带 String 参数的构造函数，用于指定自定义文档片段输出目录。

**配置 MockMvc**

```
private MockMvc mockMvc;

@Autowired
private WebApplicationContext context;

@Before
public void setUp() {
	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(this.restDocumentation)) 
			.build();
}
```

此处使用了默认配置，不过 documentationConfiguration 也提供了api用于自定义配置，这在后面的配置一节中会介绍。

**测试RESTful service**

以上完成配置后，就可以开始使用 MockMvc 请求 RESTful service 并记录请求和响应的相关信息了：

```
this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON)) 
	.andExpect(status().isOk()) 
	.andDo(document("index")); 
```

    第一行请求根路径，并接受一个 application/json 的响应消息。
    第二三行先是断言会得到一个预期的响应，并在配置的代码片段输出目录中新建一个 index 的目录，并在其中生成默认的文档片段。
    
**使用文档片段**

我们需要编写 .adoc 后缀的将文档片段整合起来，目录为：	
src/main/asciidoc/*.adoc，之后在打包是会自动生成最终的 html 文档，目录为：target/generated-docs/*.html

可以根据文末提供的 Demo 编写 adoc 文档，最好是直接参考 asciidoctor [官方文档][2]

-------------------

## 为 API 编写文档

本节介绍如何使用 Spring REST Docs 为API 编写文档。

### 链接

为当前路径添加子路径的相关信息，感觉是专门为 RESTFul Service 提供的 API，使用方式为：

```
this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
	.andExpect(status().isOk())
	.andDo(document("index", links( 
			linkWithRel("alpha").description("Link to the alpha resource"), 
			linkWithRel("bravo").description("Link to the bravo resource")))); 
```

此处必须将每一个子路径都添加进来，否则单元测试会失败，这样其实也是为了更加规范，如果有的 link 不想添加进来，使用使用 ignored() 方法将其忽略掉，在文档片段中不会生成被忽略的 link 的相关信息。

如果实在不想把所有的可能的 link 都添加进来，可以使用 relaxedLinks 代替 links 从而不会导致单元测试失败。

 > **类似 links 和 relaxedLinks，后面介绍的每一种文档记录方式都支持这两种方法(即严格模式和 relaxed 模式)，此处一次说明，后续小节不再赘述。**

#### 格式化链接

默认的超链接格式有两种：

 - Atom – links are expected to be in an array named links. Used by default when the content type of the response is compatible with application/json.
 - HAL – links are expected to be in a map named _links. Used by default when the content type of the response is compatible with application/hal+json.

使用方式如下：

```
.andDo(document("index", links(halLinks(), 
		linkWithRel("alpha").description("Link to the alpha resource"),
		linkWithRel("bravo").description("Link to the bravo resource"))));
```

如果 API 返回的链接格式是其他格式的，可以自己实现 LinkExtractor 以支持对应的格式。

#### 忽略相同的链接

```
public static LinksSnippet links(LinkDescriptor... descriptors) {
	return HypermediaDocumentation.links(linkWithRel("_self").ignored().optional(),
			linkWithRel("curies").ignored()).and(descriptors);
}
```

### Request and response payloads

此处指的是消息体内的数据，虽然平常不管用 GET 还是 POST ，也不管参数是在 URL 后面拼接后传送的还是编码到消息主体内发送的，我们都统称为“参数”，
不过在记录 API 的时候，还是得加以区分，而且对于 RESTFul 来说，这是很重要的。

根据Spring REST Doc官方文档，把 URL 后面拼接的参数叫做 parameter ，而编码到消息主体里的参数叫做 fields ，下面就分开介绍如何记录到文档中。

在请求完成后，默认会自动生成两个文档：request-body.adoc 和 response-body.adoc ，用来记录请求和响应。

#### Request fields

对于 Request fields 主要针对的是非 GET 方式提交时的参数，生成文档的方式如下：

```
.andDo(document("restful-user-add",
        requestFields(
                fieldWithPath("name").description("用户姓名"),
                fieldWithPath("sex").description("用户性别，0=女，1=男"))
));
```

此处同样支持使用 relaxedRequestFields 。

默认生成的文档片段文件名为：request-fields.adoc

#### Response fields

    本文中所有数据默认都已Json的方式传输。

对于响应消息中的信息，生成文档的方式与上面的类似：

```
.andDo(document("restful-user-list",
        responseFields(
                subsectionWithPath("_links").description("<<resources-restful-user-index,Links>> user resources"),
                subsectionWithPath("_embedded.user").description("用户列表").type("User对象数组"),
                subsectionWithPath("page").description("分页信息").type("Object"))));
```

同样提供 relaxedResponseFields 支持。

默认生成的文档片段文件名为：response-fields.adoc

#### Request parameters

请求参数通常包含在 GET 请求的 URL 中：

```
this.mockMvc.perform(post("/users").param("username", "Tester")) 
	.andExpect(status().isCreated())
	.andDo(document("create-user", requestParameters(
			parameterWithName("username").description("The user's username")
	)));
```

请求参数也可以作为表单数据包含在POST请求中：

```
this.mockMvc.perform(post("/users").param("username", "Tester")) 
	.andExpect(status().isCreated())
	.andDo(document("create-user", requestParameters(
			parameterWithName("username").description("The user's username")
	)));
```

relaxedRequestParameters 同样可用。

生成的文档片段文件名为：request-parameters.adoc

#### Path parameters

RESTFul 中大多数 API 都是将参数放到路径中的，这在一般的 API 中也常常这么多：

```
this.mockMvc.perform(get("/locations/{latitude}/{longitude}", 51.5072, 0.1275)) 
	.andExpect(status().isOk())
	.andDo(document("locations", pathParameters( 
			parameterWithName("latitude").description("The location's latitude"), 
			parameterWithName("longitude").description("The location's longitude") 
	)));
```

提供 relaxedPathParameters 

生成的文档片段为：path-parameters.adoc



### HTTP 头

HTTP 请求头和相应头记录方式如下：

```
this.mockMvc
	.perform(get("/people").header("Authorization", "Basic dXNlcjpzZWNyZXQ=")) 
	.andExpect(status().isOk())
	.andDo(document("headers",
			requestHeaders( 
					headerWithName("Authorization").description(
							"Basic auth credentials")), 
			responseHeaders( 
					headerWithName("X-RateLimit-Limit").description(
							"The total number of requests permitted per period"),
					headerWithName("X-RateLimit-Remaining").description(
							"Remaining requests permitted in current period"),
					headerWithName("X-RateLimit-Reset").description(
							"Time at which the rate limit period will reset"))));
```

生成的文档片段分别为：request-headers.adoc 和 response-headers.adoc

### 文档片段复用

很多参数、链接描述等其实是一样的，如果在每个接口里都写一遍难免比较麻烦，不过还是有办法可以复用这些代码的。比如分页中的链接可以预先定义好：

```
protected final LinksSnippet pagingLinks = links(
		linkWithRel("first").optional().description("The first page of results"),
		linkWithRel("last").optional().description("The last page of results"),
		linkWithRel("next").optional().description("The next page of results"),
		linkWithRel("prev").optional().description("The previous page of results"));
```

然后使用：

```
this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
	.andExpect(status().isOk())
	.andDo(document("example", this.pagingLinks.and( 
			linkWithRel("alpha").description("Link to the alpha resource"),
			linkWithRel("bravo").description("Link to the bravo resource"))));
```

-------------------

## 配置

### API 地址相关信息

```
this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
		.apply(documentationConfiguration(this.restDocumentation).uris()
				.withScheme("https")
				.withHost("example.com")
				.withPort(443))
		.build();
```

这样，文档中出现的 HTTP 请求等的 Host 等信息就会以上面的配置为准了。

### 文档片段编码配置

默认为UTF-8，所以一般不需要配置，不过有特殊要求的话，可以如下配置：

```
this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
		.apply(documentationConfiguration(this.restDocumentation)
				.snippets().withEncoding("ISO-8859-1"))
		.build();
```

### 文档片段模板

文档片段支持生成两个格式，默认为 Asciidoctor ，也可以配置生成 MarkDown 格式的文档：

```
this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
		.apply(documentationConfiguration(this.restDocumentation)
				.snippets().withTemplateFormat(TemplateFormats.markdown()))
		.build();
```

### 默认的文档片段

默认生成的文档片段有 6 个：

- curl-request
- http-request
- http-response
- httpie-request
- request-body
- response-body

如果需要修改可以如下配置：

```
this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
		.apply(documentationConfiguration(this.restDocumentation).snippets()
				.withDefaults(curlRequest()))
		.build();
```

## Asciidoctor

Asciidoctor 其实跟 MarkDown 挺像的，表达能力也比较强，但使用起来却很简单。

官方快速参考：[http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/][5]

官方完整文档：[http://asciidoctor.org/docs/user-manual/][2]

## Demo 源码

 - GitHub 地址：[GitHub][3]

 - GitLab 私服：[http://git.anxpp.com/open-source-unclassified-projects/spring-rest-doc-demo.git][4]

[1]: http://math.stackexchange.com/
[2]: http://asciidoctor.org/docs/user-manual/
[3]: http://git.anxpp.com/open-source-unclassified-projects/spring-rest-doc-demo.git
[4]: http://git.anxpp.com/open-source-unclassified-projects/spring-rest-doc-demo.git
[5]: http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/
[6]: http://blog.csdn.net/anxpp/article/details/73330832
[7]: http://blog.anxpp.com/index.php/archives/1071/