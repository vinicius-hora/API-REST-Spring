package curso.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

//filtro onde todas as rquisições serão capturadas para autenticação
public class JWTApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// estebelece a autenticação para a requisição
		Authentication authentication = new JWTTokenAutenticacaoService()
										.getAuthentication((HttpServletRequest)request,
										(HttpServletResponse) response);
		
		//coloca o processo de autenticação no spring security
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//continua o processo
		chain.doFilter(request, response);
		
	}

}
