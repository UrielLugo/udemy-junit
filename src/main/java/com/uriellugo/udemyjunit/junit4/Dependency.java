package com.uriellugo.udemyjunit.junit4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Clase de prueba para correr JUnit4 con Spring
 * @See https://danielme.com/2017/07/17/tutorial-spring-testing-junit-4/
 *
 */
@Component
public class Dependency {

    private final SubDependency subDependency;

    @Value("${url:google.com}")
    private String url;

    public Dependency(SubDependency subDependency) {
        super();
        this.subDependency = subDependency;
    }

    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    public String getSubdependencyClassName() {
        return subDependency.getClassName();
    }

    public int addTwo(int i) {
        return i + 2;
    }

    public String getUrl() {
        return url;
    }

}