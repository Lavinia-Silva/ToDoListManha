package br.senai.sp.controller;

import java.net.URI;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.dao.SubtarefaDao;
import br.senai.sp.model.Subtarefa;

@RestController
public class SubTarefaController {
	@Autowired
	private SubtarefaDao dao;
	
	@RequestMapping(value = "/tarefa/{id}/subtarefa", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Subtarefa> addSubTarefa(@PathVariable("id") Long idTarefa,@RequestBody Subtarefa subtarefa){
		try {
			dao.criarSubTarefa(idTarefa, subtarefa);
			return ResponseEntity.created(new URI("/subtarefa/" + subtarefa.getId())).body(subtarefa);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<Subtarefa>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Subtarefa>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/subtarefa/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Subtarefa buscarSubtarefa(@PathVariable Long id) {
		return dao.buscar(id);
	}
	
	@RequestMapping(value = "/subtarefa/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> marcarFeito(@PathVariable Long id, @RequestBody Subtarefa subtarefa) {
		try {
			dao.marcarFeito(id, subtarefa.isFeita());
			HttpHeaders header = new HttpHeaders();
			header.setLocation(URI.create("/subtarefa/" + id));
			return new ResponseEntity<Void>(header, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/subtarefa/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		dao.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
