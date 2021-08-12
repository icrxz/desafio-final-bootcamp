package com.mercadolibre.frescos_api_grupo_2_w2;

import com.mercadolibre.frescos_api_grupo_2_w2.config.SpringConfig;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SupervisorForm;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.ScopeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication()
public class Application {

	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(SpringConfig.class).registerShutdownHook(true).run(args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
