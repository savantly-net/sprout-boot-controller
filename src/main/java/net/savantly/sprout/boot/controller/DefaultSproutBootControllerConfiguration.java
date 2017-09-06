package net.savantly.sprout.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(DefaultSproutBootControllerConfiguration.BEAN_NAME)
@ConfigurationProperties("savantly.client")
public class DefaultSproutBootControllerConfiguration implements SproutBootControllerConfiguration {
	
	protected static final String BEAN_NAME = "sproutBootControllerConfiguration";

	private Map<String, Object> config;
	private String resourcePath = "classpath:/static";
	private List<String> jsLibs = new ArrayList<>();
	private List<String> cssLibs = new ArrayList<>();
	
	public DefaultSproutBootControllerConfiguration() {
		config = new HashMap<>();
	}

	@Override
	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	@Override
	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	@Override
	public List<String> getJsLibs() {
		return jsLibs;
	}

	public void setJsLibs(List<String> jsLibs) {
		this.jsLibs = jsLibs;
	}

	@Override
	public List<String> getCssLibs() {
		return cssLibs;
	}

	public void setCssLibs(List<String> cssLibs) {
		this.cssLibs = cssLibs;
	}
}
