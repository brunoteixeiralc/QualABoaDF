package br.com.qualABoaDF.negocial;

import java.io.Serializable;

public class FestaComentarios implements Serializable {

	private static final long serialVersionUID = 8689489657737989154L;
	public static final String KEY = "comentarioFesta";
	
	private String data;
	private String nome;
	private String comentario;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

}
