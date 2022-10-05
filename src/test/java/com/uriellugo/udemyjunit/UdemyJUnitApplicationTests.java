package com.uriellugo.udemyjunit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UdemyJUnitApplicationTests {

	@Autowired
	ConfigurableApplicationContext context;

	@Autowired
	Environment environment;

	// https://www.jvt.me/posts/2021/06/25/spring-context-test/
	@Test
	void contextLoads() {
		assertNotNull(context);
	}

	@Test
	void test_environment() {
		String applicationName = environment.getProperty("spring.application.name");
		assertNotNull(applicationName);
		System.out.println("applicationName = " + applicationName);
	}

	@Test
	void test_propertySources() {
		System.out.println("Atributos del applicationContext");
		System.out.println("run.getApplicationName() = " + context.getApplicationName());
		System.out.println("run.getBeanDefinitionCount() = " + context.getBeanDefinitionCount());
		System.out.println("run.getBeanDefinitionNames().length = " + context.getBeanDefinitionNames().length);

		ConfigurableEnvironment environment = context.getEnvironment();
		assertNotNull(environment, "\nEl environment es nulo");
		System.out.println("environment.getSystemEnvironment() = " + environment.getSystemEnvironment());
		System.out.println("environment.getSystemProperties() = " + environment.getSystemProperties());
		System.out.println("environment.getProperty(\"properties.hello-world\") = " + environment.getProperty("properties.hello-world"));
		System.out.println("environment.getActiveProfiles() = " + Arrays.toString(environment.getActiveProfiles()));

		MutablePropertySources propertySources = environment.getPropertySources();
		Iterator<PropertySource<?>> iterator = propertySources.stream().iterator();
		System.out.println("\nConsiguiendo los propertySources del ApplicationContext");
		System.out.println("--------------------------------------------------------\n");
		for(int i=0; iterator.hasNext(); i++) {
			PropertySource<?> next = iterator.next();
			System.out.println("PropertySource" + (i) + ".getName() = " + next.getName());
			System.out.println("PropertySource.getSource() = " + next.getSource());
			System.out.println();
		}
	}
}