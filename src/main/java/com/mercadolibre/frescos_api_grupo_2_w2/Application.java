package com.mercadolibre.frescos_api_grupo_2_w2;

import com.mercadolibre.frescos_api_grupo_2_w2.config.SpringConfig;
import com.mercadolibre.frescos_api_grupo_2_w2.util.ScopeUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(SpringConfig.class).registerShutdownHook(true)
				.run(args);
	}
}
