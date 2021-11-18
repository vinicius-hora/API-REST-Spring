package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

//libera o acesso com o Crossorigin
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Serviço RestFull
	//consulta por id
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		
		return  new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	/*
	//exemplo para baixar relatorio de usuário especifico
	@GetMapping(value = "/{id}/relatoriopdf", produces = "application/pdf")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		//o retorno seria um relatorio, exemplo
		return  new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	*/
	
	//consulta todos
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario(){
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	//cadastrar
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> Cadastrar(@RequestBody Usuario usuario){
		
		//permitir salvar telefone no cadastro de usuario
		for (int pos = 0; pos < usuario.getTelefone().size(); pos ++) {
			usuario.getTelefone().get(pos).setUsuario(usuario);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	
	//atualizar
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> Atualizar(@RequestBody Usuario usuario){
		
		//permitir salvar telefone no cadastro de usuario
		for (int pos = 0; pos < usuario.getTelefone().size(); pos ++) {
			usuario.getTelefone().get(pos).setUsuario(usuario);
		}
				
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	
	//delete
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity<?> Delete(@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return ResponseEntity.noContent().build().ok("ok");
	}
	

}














