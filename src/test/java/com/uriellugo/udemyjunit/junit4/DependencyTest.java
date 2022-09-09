package com.uriellugo.udemyjunit.junit4;

import com.uriellugo.udemyjunit.junit4.config.AppJUnit4Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {AppJUnit4Configuration.class})
@RunWith(SpringRunner.class)
public class DependencyTest {

    @Autowired
    private Dependency dependency;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testDependency(){
        assertEquals(dependency.getClass().getSimpleName(), dependency.getClassName());
    }

    @Test
    public void testAddTwo(){
        assertEquals(3, dependency.addTwo(1));
    }

    @Test
    public void testSubdependency(){
        assertEquals(SubDependency.class.getSimpleName(), dependency.getSubdependencyClassName());
    }

    @Test
    public void testUrl(){
        assertTrue(dependency.getUrl().contains("tutorial-spring-testing-junit-4"), "Se esperaba: 'tutorial-spring-testing-junit-4' pero fue: '" + dependency.getUrl() + "'");
    }

    @Test
    public void testBeanNames() {
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
    }
}