package curso.api.rest.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import curso.api.rest.model.Telefone;
import curso.api.rest.model.Usuario;

public class UsuarioDTO implements Serializable {

	static final long serialVersionUID = 1L;
	
	private String userLogin;
	
	private String userNome;
	
	private List<Telefone> telefone = new ArrayList<>();

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserNome() {
		return userNome;
	}

	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}

	public List<Telefone> getTelefone() {
		return telefone;
	}

	public void setTelefone(List<Telefone> telefone) {
		this.telefone = telefone;
	}
	

	public UsuarioDTO(Usuario usuario) {
		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
		this.telefone = usuario.getTelefone();
	}

	
	
	

}
