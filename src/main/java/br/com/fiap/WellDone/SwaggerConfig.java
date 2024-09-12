package br.com.fiap.WellDone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
	OpenAPI customAPI() {
		return new OpenAPI().info(new Info().title("Projeto WellDone")
				.description("Este projeto é referente a empresa PluSoft" + " WellDone").version("1.1"));
	}

}
