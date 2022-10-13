package com.uriellugo.udemyjunit.testing;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SystemTest {

    @Test
    void test_systemProperties() {
        Properties properties = System.getProperties();
        assertNotNull(properties, "System properties is null");
        properties.entrySet().stream()
                .map(c -> c.getKey() + " = " + c.getValue())
                .sorted()
                .forEach(System.out::println);
    }

    @Test
    void test_systemProperties2() {
        Properties properties = System.getProperties();
        assertNotNull(properties, "System properties is null");
        properties.list(System.out);
    }

    @Test
    void test_whenGetSecurityManage_thenIsNull() {
        SecurityManager securityManager = System.getSecurityManager();
        assertNull(securityManager, "Hay un SecurityManager");
    }

    @Test
    void test_systemEnvironment() {
        Map<String, String> environment = System.getenv();
        assertNotNull(environment);
        environment.entrySet().stream()
                //.sorted((c1, c2) -> c1.getKey().compareTo(c2.getKey()))
                //.sorted(Comparator.comparing(Map.Entry::getKey))
                .sorted(Map.Entry.comparingByKey())
                .forEach(c -> System.out.println(c.getKey() + " = " + c.getValue()));
    }

    @Test
    void test_systemLineSeparator() {
        assertEquals(System.getProperty("line.separator"), System.lineSeparator(), "El lineSeparator no es el mismo");
    }
}
