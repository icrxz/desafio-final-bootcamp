package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.mercadolibre.restclient.mock.RequestMockHolder;
import com.mercadolibre.frescos_api_grupo_2_w2.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "SCOPE_SUFFIX = integration_test" })
public abstract class IntegrationTest {

	@AfterEach
	protected void afterEach() {
		RequestMockHolder.clear();
	}
}
