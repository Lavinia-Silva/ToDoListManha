package br.senai.sp.controller;

import java.net.URI;
import java.util.HashMap;

import javax.validation.ConstraintViolationException;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWTSigner;

import br.senai.sp.dao.UsuarioDao;
import br.senai.sp.model.TokenJWT;
import br.senai.sp.model.Usuario;

@RestController
public class UsuarioController {
	public static final String EMISSOR = "senai";
	public static final String SECRET = "ToDoListSENAIInformatica";
	@Autowired
	private UsuarioDao dao;

	@RequestMapping(value = "/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
		try {
			dao.inserir(usuario);
			HttpHeaders header = new HttpHeaders();
			header.setLocation(URI.create("/usuario/" + usuario.getId()));
			return ResponseEntity.created(new URI("/usuario/" + usuario.getId())).body(usuario);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<Usuario>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<TokenJWT> logar(@RequestBody Usuario usuario) {
		try {
				usuario = dao.logar(usuario);
				if (usuario != null) {
					HashMap<String, Object> claims = new HashMap<String, Object>();
					claims.put("iss", EMISSOR);
					claims.put("is_user", usuario.getId());
					claims.put("nome_user", usuario.getNome());
					// HORA ATUAL EM SEGUNDOS
					long horaAtual = System.currentTimeMillis()/1000;
					// HORA EXPIRAÇÃO (HORA ATUAL + 1 HORA(3600 seg))
					long horaExpiracao =  horaAtual + 3600;
					claims.put("iat", horaAtual);
					claims.put("exp", horaExpiracao);
					JWTSigner signer = new JWTSigner(SECRET);	
					TokenJWT tokenJwT = new TokenJWT();
					tokenJwT.setToken(signer.sign(claims));
					return ResponseEntity.ok(tokenJwT);
				}else{
					return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
				}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<TokenJWT>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
