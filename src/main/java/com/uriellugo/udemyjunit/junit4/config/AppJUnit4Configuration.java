package com.uriellugo.udemyjunit.junit4.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.uriellugo.udemyjunit.junit4")
@PropertySource(value = "classpath:junit4.properties")
public class AppJUnit4Configuration {

}
