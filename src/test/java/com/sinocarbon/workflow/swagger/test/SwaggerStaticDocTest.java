//package com.sinocarbon.workflow.swagger.test;
//
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import javax.annotation.Resource;
//
//import org.junit.After;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import io.github.robwin.markup.builder.MarkupLanguage;
//import io.github.robwin.swagger2markup.GroupBy;
//import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
//import com.sinocarbon.workflow.MavenDemoApplication;
//import com.sinocarbon.workflow.swagger.test.handler.MySwaggerResultHandler;
//
//@AutoConfigureMockMvc
//@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = MavenDemoApplication.class)
//@WebAppConfiguration
//public class SwaggerStaticDocTest {
//
//	private String snippetDir = "target/generated-snippets";
//	private String outputDir = "target/asciidoc";
//	@Autowired
//	private MockMvc mockMvc;
//	@Resource
//	private ObjectMapper objectMapper;
//
//	@After
//	public void Test() throws Exception {
//		// 得到swagger.json,写入outputDir目录中
//		mockMvc.perform(get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
//				.andDo(MySwaggerResultHandler.outputDirectory(outputDir).build()).andExpect(status().isOk())
//				.andReturn();
//
//		// 读取上一步生成的swagger.json转成asciiDoc,写入到outputDir
//		// 这个outputDir必须和插件里面<generated></generated>标签配置一致
//		Swagger2MarkupConverter.from(outputDir + "/swagger.json").withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
//				.withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
//				.withExamples(snippetDir).build().intoFolder(outputDir);// 输出
//
//	}
//
//	@Test
//	public void exampleApi() throws Exception {
//
//		mockMvc.perform(get("/example/test?username=superadmin&tenantId=webapp")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(MockMvcRestDocumentation.document("findUserInfoByUsername", preprocessResponse(prettyPrint())));
//	}
//
//}
