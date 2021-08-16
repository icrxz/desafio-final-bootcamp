package com.mercadolibre.frescos_api_grupo_2_w2;

import com.mercadolibre.frescos_api_grupo_2_w2.config.SpringConfig;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.util.ScopeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication()
public class Application {
	private final SupervisorRepository supervisorRepository;

	@Autowired
	public Application(SupervisorRepository supervisorRepository) {
		this.supervisorRepository = supervisorRepository;

		if (supervisorRepository.findAll().size() <= 0)
			createAdminUser();
	}

	private void createAdminUser() {
		Supervisor supervisor = new Supervisor();
		supervisor.setEmail("admin@admin.com");
		supervisor.setPassword("$2a$10$JHzsz1ZmVNNYtxiiaSlyl.M8rwfyPPfePg4FRUkPmKMM4T3c2/cFi");
		supervisor.setRole("SUPERVISOR");

		supervisorRepository.save(supervisor);
	}

	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(SpringConfig.class).registerShutdownHook(true).run(args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
