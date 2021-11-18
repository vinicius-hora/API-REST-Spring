package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	//tempo de validade do tokem em milisegundos, abaixo está 2 dias
	private static final long EXPIRATION_TIME = 172800000;
	
	// uma senha unica para compor a autenticação
	private static final String SECRET = "SenhaSecreta";
	
	//Prefixo padrão de  token jwt
	private static final String TOKEN_PREFIX = "Bearer";
	
	//retorno do metodo
	private static final String HEADER_STRING = "Authorization";
	
	//gerando token de autenticação e adicionado resposta
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		//motagem do token
		//add usuari
		String JWT = Jwts.builder().setSubject(username)
					//tempo de expiração
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					//compactação e geração de senha
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		//junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + JWT;
		
		//adiciona no cabeçalho http
		response.addHeader(HEADER_STRING, token);
		
		//Escreve token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
		
		
		
	}
	
	//Retorna o usuário validado com token ou caso não seja válido, reotrna null
	public Authentication getAuthentication(HttpServletRequest request) {
		
		//pega o token enviado no cabeçanho http
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			//faz a validação do token do usuário na requisição
			String user = Jwts.parser().setSigningKey(SECRET)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody().getSubject(); 
			
			if(user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
								.getBean(UsuarioRepository.class)
								.findUuserByLogin(user);
				//verifica se o usuário existe e faz o retorno
				if( usuario != null) {
					
					return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(), 
								usuario.getSenha(), 
								usuario.getAuthorities());
					
				}
				
			}
		}
		// nao autorizado
		return null;
		
		
	}
	
	
	
	
}