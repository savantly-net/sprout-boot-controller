package net.savantly.sprout.boot.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller(HomeController.BEAN_NAME)
public class HomeController {
	protected static final String BEAN_NAME = "sproutBootHomeController";
    static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private SproutBootControllerConfiguration controllerConfig;
    @Value("${info.app.buildNumber:0}")
    private String buildNumber;
    

    @RequestMapping({"/"})
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String modulePath = controllerConfig.getResourcePath() + "/modules";
        final String generatedPath = controllerConfig.getResourcePath() + "/generated";
        
    	// Client Settings
    	model.addAttribute("clientConfig", controllerConfig.getConfig());

        ServletRequest req = (ServletRequest) request;
        ServletResponse resp = (ServletResponse) response;
        FilterInvocation filterInvocation = new FilterInvocation(req, resp, new FilterChain() {
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                throw new UnsupportedOperationException();
            }
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                ((User) authentication.getPrincipal()).eraseCredentials();
            }
            WebSecurityExpressionRoot sec = new WebSecurityExpressionRoot(authentication, filterInvocation);
            sec.setTrustResolver(new AuthenticationTrustResolverImpl());

            ClientSecurityContext clientSecurityContext = new ClientSecurityContext();
            clientSecurityContext.setAnonymous(sec.isAnonymous());
            clientSecurityContext.setAuthenticated(sec.isAuthenticated());
            clientSecurityContext.setFullyAuthenticated(sec.isFullyAuthenticated());
            clientSecurityContext.setPrincipal(sec.getPrincipal());
            clientSecurityContext.setRememberMe(sec.isRememberMe());
            clientSecurityContext.setAuthorities(sec.getAuthentication().getAuthorities());

            String securityContextString = objectMapper.writeValueAsString(clientSecurityContext);

            model.addAttribute("security", securityContextString);

        }


    	List<String> jsLibResourceArray = new ArrayList<String>();
        for (String jsLib : controllerConfig.getJsLibs()) {
			getResourcePaths(jsLib, jsLibResourceArray);
		}
        model.addAttribute("jsLibResources", jsLibResourceArray);

        List<String> jsResourceArray = new ArrayList<String>();
        getResourcePaths(modulePath + "/*/*.js", jsResourceArray);
        getResourcePaths(modulePath + "/*/config/*.js", jsResourceArray);
        getResourcePaths(modulePath + "/*/controllers/*.js", jsResourceArray);
        getResourcePaths(modulePath + "/*/services/*.js", jsResourceArray);
        getResourcePaths(modulePath + "/*/directives/*.js", jsResourceArray);
        getResourcePaths(modulePath + "/*/filters/*.js", jsResourceArray);

        model.addAttribute("moduleJsResources", jsResourceArray);

        List<String> cssLibResourceArray = new ArrayList<String>();
        
        for (String cssLib : controllerConfig.getCssLibs()) {
        	getResourcePaths(cssLib, cssLibResourceArray);
		}
        model.addAttribute("cssLibResources", cssLibResourceArray);
        
        List<String> cssResourceArray = new ArrayList<String>();
        getResourcePaths(modulePath + "/*/css/*.css", cssResourceArray);
        getResourcePaths(generatedPath + "/*.css", cssResourceArray);
        model.addAttribute("moduleCssResources", cssResourceArray);

        return "/sprout/index";
    }

    private List<String> getResourcePaths(String pattern, List<String> resourceArray) {
        log.info(String.format("Finding embedded resource paths for: %s", pattern));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] moduleResources = resolver.getResources(pattern);
            for (Resource resource : moduleResources) {
                log.debug(String.format("Processing resource: %s", resource));

                URL resourceURL = resource.getURL();
                log.debug(String.format("Found resource URL: %s", resourceURL));
                if(resourceURL.getProtocol() == "file"){
                	resourceArray.add(truncateBeginningOfPath(resourceURL.getPath() + "?v=" + buildNumber, "/static/"));
                } else {
                	resourceArray.add(resourceURL.toString());
                }
                
            }
        } catch (IOException e) {
            log.error(String.format("Error processing resources for pattern: %s", pattern), e);
        }
        return resourceArray;
    }

    private String truncateBeginningOfPath(String fullPath, String stringToMatch) {
        if (fullPath == null || fullPath.length() == 0) {
            throw new RuntimeException("fullPath is null or empty.");
        }
        if (stringToMatch == null || stringToMatch.length() == 0) {
            throw new RuntimeException("stringToMatch is null or empty.");
        }
        int matchIndex = fullPath.indexOf(stringToMatch);
        int splitIndex = matchIndex + stringToMatch.length();
        if (matchIndex == -1) {
            return fullPath;
        } else {
            return fullPath.substring(splitIndex);
        }
    }

}