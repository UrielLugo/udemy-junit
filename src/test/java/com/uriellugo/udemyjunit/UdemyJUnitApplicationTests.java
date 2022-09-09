package com.uriellugo.udemyjunit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class UdemyJUnitApplicationTests {

	@Autowired
	ApplicationContext context;

	// https://www.jvt.me/posts/2021/06/25/spring-context-test/
	@Test
	void contextLoads() {
		Assertions.assertNotNull(context);
	}

}
