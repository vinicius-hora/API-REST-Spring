package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.DTO.UsuarioDTO;
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
	public ResponseEntity<UsuarioDTO> init(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		System.out.println("Executando versão 1");
		return  new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()), HttpStatus.OK);
	}
	/*
	//versionamento por cabeçalho
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
	public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		System.out.println("Executando versão 1");
		return  new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	//versionamento de API pela url
	//Serviço RestFull
		//consulta por id
		@GetMapping(value = "v3/{id}", produces = "application/json")
		public ResponseEntity<Usuario> initV2(@PathVariable(value = "id") Long id) {
			
			Optional<Usuario> usuario = usuarioRepository.findById(id);
			
			System.out.println("Executando versão 2");
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
	//cache para carregamento de usuário, supondo que seja um carregamento lento
	//consulta todos
	//@Cacheable("get-all-cache")
	//remove cache parados a muito tempo
	@CacheEvict(value = "cache-get-all", allEntries = true)
	//atualiza quando a alteração no banco
	@CachePut("cache-get-all")
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<UsuarioDTO>> usuario() throws InterruptedException{
		//trava o código por 6 segundos para simular lentidão
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		List<UsuarioDTO> listDTO = new ArrayList<>();
		//convertendo usuários para usuarioDTO
		listDTO = list.stream().map(x -> {
			return new UsuarioDTO(x);
		}).collect(Collectors.toList());
		
		return new ResponseEntity<List<UsuarioDTO>>(listDTO, HttpStatus.OK);
	}
	
	//cadastrar
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> Cadastrar(@RequestBody Usuario usuario){
		
		//permitir salvar telefone no cadastro de usuario
		for (int pos = 0; pos < usuario.getTelefone().size(); pos ++) {
			usuario.getTelefone().get(pos).setUsuario(usuario);
		}
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
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
		
		Usuario userTemp = usuarioRepository.findUuserByLogin(usuario.getLogin());
		
		if(!userTemp.getSenha().equals(usuario.getSenha())) {
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
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














