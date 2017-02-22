package br.senai.sp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.model.Subtarefa;
import br.senai.sp.model.Tarefa;

@Repository
public class SubtarefaDao {
	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	public void criarSubTarefa(Long idTarefa, Subtarefa subtarefa){
		subtarefa.setTarefa(manager.find(Tarefa.class, idTarefa));
		manager.persist(subtarefa);
	}
	
	public Subtarefa buscar(Long id) {
		return manager.find(Subtarefa.class, id);
	}
	
	@Transactional
	public void marcarFeito(Long idSubtarefa, boolean valor){
		Subtarefa subtarefa = buscar(idSubtarefa);
		subtarefa.setFeita(valor);
		manager.merge(subtarefa);
	}
	
	@Transactional
	public void excluir(Long idSubtarefa){
		Subtarefa subtarefa = buscar(idSubtarefa);
		Tarefa tarefa = subtarefa.getTarefa();
		tarefa.getSubtarefas().remove(subtarefa);
		manager.merge(tarefa);
	}
}
