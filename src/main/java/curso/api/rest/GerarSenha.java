package curso.api.rest;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarSenha {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123"));

	}

}
