package com.plasmidEditor.sputnik;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootTest
class SputnikApplicationTests {
	@Value("${spring.application.name}")
	private transient String appName;

	@Test
	void applicationPropertiesLoads() {
		Assertions.assertEquals("sputnik", appName, "application.properties wasn't read");
	}

}
