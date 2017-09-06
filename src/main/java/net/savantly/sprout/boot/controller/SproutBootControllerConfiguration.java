package net.savantly.sprout.boot.controller;

import java.util.List;
import java.util.Map;

public interface SproutBootControllerConfiguration {

	Map<String, Object> getConfig();

	String getResourcePath();

	List<String> getCssLibs();

	List<String> getJsLibs();

}
