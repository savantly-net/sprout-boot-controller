package net.savantly.sprout.autoconfigure.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;

import net.savantly.sprout.autoconfigure.SproutAutoConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ComponentScan(basePackageClasses={SproutAutoConfiguration.class})
public @interface EnableSproutController {

}
