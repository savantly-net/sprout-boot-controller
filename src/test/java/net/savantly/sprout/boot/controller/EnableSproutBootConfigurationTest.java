package net.savantly.sprout.boot.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;



@RunWith(SpringRunner.class)
@WebAppConfiguration
public class EnableSproutBootConfigurationTest {
	
	Logger log = LoggerFactory.getLogger(EnableSproutBootConfigurationTest.class);
	
	@Autowired
	WebApplicationContext ctx;
	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(ctx)
				.build();
	}
	
	@Before
	public void beforeEach(){
		DefaultSproutBootControllerConfiguration controllerConfig = ctx.getBean(DefaultSproutBootControllerConfiguration.class);
		controllerConfig.getJsLibs().clear();
		controllerConfig.getCssLibs().clear();
	}

	@Test
	public void contextLoads() {
		Assert.assertTrue(ctx.containsBean(DefaultSproutBootControllerConfiguration.BEAN_NAME));
		Assert.assertTrue(ctx.containsBean(HomeController.BEAN_NAME));
	}
	
	@Test
	public void loadIndexPage() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk());
	}
	
	@Test
	public void indexPageModelContainsExternalJsResource() throws Exception {
		DefaultSproutBootControllerConfiguration controllerConfig = ctx.getBean(DefaultSproutBootControllerConfiguration.class);
		String testJsUrl = "http://test.js";
		controllerConfig.getJsLibs().add(testJsUrl );
		mvc.perform(get("/")).andExpect(MockMvcResultMatchers.model().attribute("jsLibResources", hasItem(testJsUrl)));
	}
	
	@Test
	public void indexPageModelContainsClasspathJsResource() throws Exception {
		DefaultSproutBootControllerConfiguration controllerConfig = ctx.getBean(DefaultSproutBootControllerConfiguration.class);
		String clientJsPath = "test.js";
		String testJsUrl = "classpath:/static/" + clientJsPath;
		controllerConfig.getJsLibs().add(testJsUrl );
		mvc.perform(get("/")).andExpect(MockMvcResultMatchers.model().attribute("jsLibResources", hasItem(clientJsPath+"?v=0")));
	}
	
	@Test
	public void indexPageMarkupContainsExternalJsResource() throws Exception {
		DefaultSproutBootControllerConfiguration controllerConfig = ctx.getBean(DefaultSproutBootControllerConfiguration.class);
		String testJsUrl = "http://test.js";
		controllerConfig.getJsLibs().add(testJsUrl );
		MvcResult result = mvc.perform(get("/")).andReturn();
		MockHttpServletResponse response = result.getResponse();
		log.debug(response.getContentAsString());
		response.getContentAsString().contains(testJsUrl);
	}
	
	
	@Configuration
	@EnableSproutBootController
	public static class Config{
		
		@Bean
		ObjectMapper objectMapper(){
			return new ObjectMapper();
		}
		
	}

}
