package com.uriellugo.udemyjunit.junit4;

import org.springframework.stereotype.Component;

/**
 * Clase de prueba para correr JUnit4 con Spring
 * @See https://danielme.com/2017/07/17/tutorial-spring-testing-junit-4/
 *
 */
@Component
public class SubDependency {

    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
