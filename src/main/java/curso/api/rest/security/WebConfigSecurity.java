package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

//mapeia urls, endereços, autoriza ou bloqueia acessos
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	//autenticação swagger
	private static final String[] AUTH_LIST = {
	        // -- swagger ui
	        "**/swagger-ui/**",
	        "**/index.html",
	        "/v2/api-docs",
	        "/webjars/**",
	        "/swagger-ui/**",
	        "webjars",
	        "**/swagger-ui/",
	        "**/swagger-ui/index.html",
	        "/swagger-ui/index/**"
	        
	};
	//lista de permissoes antMatchers
	private static final String[] MATCH_LIST = {
			"/v2/ api-docs" ,
            "/swagger-resources", 
           "/swagger-resources/configuration/ui", 
           "/swagger-resources/configuration/security"
	        
	};
	
	//configura as solicitações de acesso por http
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//AUTORIZANDO ACESSO AO SWAGGER
		http.authorizeRequests ()
        .antMatchers (MATCH_LIST).permitAll();
		http.headers().frameOptions().disable();
		//ativando a proteção contra usuario que não estão validados por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		//ativando a permissão para acesso a página inicial do sistema
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		//permite os clientes fazer as requisições
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/resources/**").permitAll()
		.antMatchers("/apirest/usuario/").permitAll()
		//permir acesso ao swagger /swagger-ui/index.html
		.antMatchers("**/swagger-ui/").permitAll()
		.antMatchers("/swagger-ui/index.html").permitAll()
		.antMatchers(HttpMethod.GET, "**/swagger-ui/").permitAll()
		.antMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
		.antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
		//permitir todos os get
		.antMatchers(HttpMethod.GET, "/usuario/**").permitAll()
		
		.antMatchers("**/resources/").permitAll()
		//URL de logout - redireciona após o user deslogar do sistema 
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		//mapeia URL de logout e invalida o usuário
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		
		//filtra as requisições de login para autenticação
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
								UsernamePasswordAuthenticationFilter.class)
		//filtra demais requisições para verificar presença do token jwt no HEADER http
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//service que ira consultar o usuário no banco
		auth.userDetailsService(implementacaoUserDetailsService)
		
		
		
		//padrão de codificação de senha
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//permite acesso a tudo, independente de ter senha, utilizar para testes
	
	@Override 
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(AUTH_LIST);
		
	}
	
	

}
