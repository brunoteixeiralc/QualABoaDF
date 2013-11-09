package br.com.qualABoaDF.negocial;

import java.io.Serializable;
import java.util.List;

import br.com.qualABoaDF.utils.TipoInformacao;

public class FestaDetalhes implements Serializable{

	private static final long serialVersionUID = 290741085770358100L;
	
	public static final String KEY = "detalhesFesta";
	private List<String> ingressos;
	private List<String> atracoes;
	private String maiSobreFesta;
	private List<FestaComentarios> comentarioFesta;
	private FestaMapa mapaFesta;
	private TipoInformacao tipoInformacao;
	private List<String> maisInformacoes;
	
	public FestaDetalhes(){
		
		setTipoInformacao(TipoInformacao.OUTRAS_INFORMACOES_FESTA);
		
	}

	public List<String> getIngressos() {
		return ingressos;
	}

	public void setIngressos(List<String> ingressos) {
		this.ingressos = ingressos;
	}

	public List<String> getAtracoes() {
		return atracoes;
	}

	public void setAtracoes(List<String> atracoes) {
		this.atracoes = atracoes;
	}

	public String getMaiSobreFesta() {
		return maiSobreFesta;
	}

	public void setMaiSobreFesta(String maiSobreFesta) {
		this.maiSobreFesta = maiSobreFesta;
	}

	public List<FestaComentarios> getComentarioFesta() {
		return comentarioFesta;
	}

	public void setComentarioFesta(List<FestaComentarios> comentarioFesta) {
		this.comentarioFesta = comentarioFesta;
	}

	public FestaMapa getMapaFesta() {
		return mapaFesta;
	}

	public void setMapaFesta(FestaMapa mapaFesta) {
		this.mapaFesta = mapaFesta;
	}

	public TipoInformacao getTipoInformacao() {
		return tipoInformacao;
	}

	public void setTipoInformacao(TipoInformacao tipoInformacao) {
		this.tipoInformacao = tipoInformacao;
	}

	public List<String> getMaisInformacoes() {
		return maisInformacoes;
	}

	public void setMaisInformacoes(List<String> maisInformacoes) {
		this.maisInformacoes = maisInformacoes;
	}
	
	
}
