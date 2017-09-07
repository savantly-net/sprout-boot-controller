package net.savantly.sprout.autoconfigure.controller;

import java.util.List;
import java.util.Map;

public interface SproutControllerConfiguration {

	Map<String, Object> getConfig();

	String getResourcePath();

	List<String> getCssLibs();

	List<String> getJsLibs();

}
