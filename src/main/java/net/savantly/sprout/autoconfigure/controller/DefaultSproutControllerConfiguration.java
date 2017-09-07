package net.savantly.sprout.autoconfigure.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration(DefaultSproutControllerConfiguration.BEAN_NAME)
@ConfigurationProperties("savantly.client")
@ConditionalOnClass(SpringTemplateEngine.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class DefaultSproutControllerConfiguration implements SproutControllerConfiguration {
	
	protected static final String BEAN_NAME = "sproutControllerConfiguration";

	private Map<String, Object> config;
	private String resourcePath = "classpath:/static";
	private List<String> jsLibs = new ArrayList<>();
	private List<String> cssLibs = new ArrayList<>();
	
	public DefaultSproutControllerConfiguration() {
		config = new HashMap<>();
	}
	
    @Bean
    public SproutTemplateResolver templateResolver() {
    	SproutTemplateResolver resolver = new SproutTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
    
    @Bean
    @ConditionalOnMissingBean(SpringTemplateEngine.class)
    public SpringTemplateEngine templateEngineBean(Set<ITemplateResolver> resolverBeans) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolvers(resolverBeans);
        return templateEngine;
    }
    
    @ConditionalOnBean(SpringTemplateEngine.class)
    public void configureSpringTemplateEngine(SpringTemplateEngine templateEngine, SproutTemplateResolver sproutTemplateResolver){
    	templateEngine.getTemplateResolvers().add(sproutTemplateResolver);
    }

    @Bean
    @ConditionalOnMissingBean(ThymeleafViewResolver.class)
    public ThymeleafViewResolver thymeleafViewResolverBean(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setOrder(1);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
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
