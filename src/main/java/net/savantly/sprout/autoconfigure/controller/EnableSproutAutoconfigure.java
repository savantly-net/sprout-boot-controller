package net.savantly.sprout.autoconfigure.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ComponentScan(basePackageClasses={EnableSproutAutoconfigure.class})
public @interface EnableSproutAutoconfigure {

}
