package com.uriellugo.udemyjunit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.StreamSupport;

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

	@Test
	void testEnvironment_activeProfiles() {
		assertNotNull(environment);
		Arrays.stream(environment.getActiveProfiles()).forEach(System.out::println);
	}

	@Test
	void testEnvironment_defaultProfiles() {
		assertNotNull(environment);
		Arrays.stream(environment.getDefaultProfiles()).forEach(System.out::println);
	}

	@Test
	void test_resourcePropertySources() {
		assertNotNull(environment);
		Properties properties = new Properties();
		MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
		StreamSupport.stream(propSrcs.spliterator(), false)
				.filter(ps -> ps instanceof ResourcePropertySource)
				.map(ps -> ((ResourcePropertySource) ps).getPropertyNames())
				.flatMap(Arrays::stream)
				.forEach(propName -> properties.setProperty(propName, environment.getProperty(propName)));
		properties.forEach((key, value) -> System.out.println(key + " : " + value));
	}

	@Test
	void test_ymlPropertySource() {
		assertNotNull(environment);
		MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
		StreamSupport.stream(propSrcs.spliterator(), false)
				.filter(ps -> ps instanceof ResourcePropertySource)
				.map(ps -> {
					YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
					yaml.setResources(new ClassPathResource("application.yml"));
					return yaml.getObject();
				})
				.filter(Objects::nonNull)
				.forEach(prop -> prop.forEach((key, value) -> System.out.println(key + " : " + value)));
	}
}