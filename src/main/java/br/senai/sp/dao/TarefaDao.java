package br.senai.sp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.model.Tarefa;
@Repository
public class TarefaDao implements InterfaceDao<Tarefa> {
	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	public void inserir(Tarefa tarefa) {
		manager.persist(tarefa);
	}

	public Tarefa buscar(Long id) {
		return manager.find(Tarefa.class, id);
	}

	public List<Tarefa> listar() {
		TypedQuery<Tarefa> query = manager.createQuery(
				"SELECT t FROM Tarefa t ", Tarefa.class);
		return query.getResultList();
	}

	@Transactional
	public void excluir(Long id) {
		try {
			Tarefa tarefa = manager.find(Tarefa.class, id);
			manager.remove(tarefa);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
