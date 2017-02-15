package br.senai.sp.model;

import java.util.List;

public class Tarefa {
	private Long id;
	private String titulo;
	private List<SubTarefa> subtarefas;

	public boolean isRealizada(){
		for(SubTarefa subtarefa : subtarefas) {
			if(!subtarefa.isFeita()) {
				return false;
			}
		}
		return true;

	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public List<SubTarefa> getSubtarefas() {
		return subtarefas;
	}
	public void setSubtarefas(List<SubTarefa> subtarefas) {
		this.subtarefas = subtarefas;
	}
	
}
