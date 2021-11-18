package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//configurando spring para reconhecer os pacotes
@SpringBootApplication
@EntityScan(basePackages = {"curso.api.rest.model"})
@ComponentScan(basePackages = {"curso.*"})
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})
@EnableTransactionManagement // framework gerencia operações no banco
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class SpringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpringrestapiApplication.class, args);
	}
	/*
	//configuração centralizada para liberação de acessos
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/usuario/**");
		//liberando metodos especificos para servidores especificos
		registry.addMapping("/usuario/**").allowedMethods("GET")
		.allowedOrigins("servidor, servidor2");
		
	}
	*/

}
