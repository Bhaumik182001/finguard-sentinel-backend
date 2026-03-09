package com.bhaumik18.finguard.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of("SYSTEM_INIT");
	}
}
