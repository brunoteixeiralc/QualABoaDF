package br.com.qualABoaDF.negocial;

import java.io.Serializable;
import java.util.Set;

import br.com.qualABoaDF.utils.TipoInformacao;

public class FestaDetalhes implements Serializable{

	private static final long serialVersionUID = 290741085770358100L;
	
	public static final String KEY = "detalhesFesta";
	private Set<String> ingressos;
	private Set<String> atracoes;
	private String maiSobreFesta;
	private Set<FestaComentarios> comentarioFesta;
	private FestaMapa mapaFesta;
	private TipoInformacao tipoInformacao;
	private Set<String> maisInformacoes;
	
	public FestaDetalhes(){
		
		tipoInformacao = TipoInformacao.OUTRAS_INFORMACOES_FESTA;
		
	}
	
	public String getMaiSobreFesta() {
		return maiSobreFesta;
	}
	public void setMaiSobreFesta(String maiSobreFesta) {
		this.maiSobreFesta = maiSobreFesta;
	}
	public Set<String> getAtracoes() {
		return atracoes;
	}
	public void setAtracoes(Set<String> atracoes) {
		this.atracoes = atracoes;
	}
	public Set<String> getIngressos() {
		return ingressos;
	}
	public void setIngressos(Set<String> ingressos) {
		this.ingressos = ingressos;
	}
	public Set<FestaComentarios> getComentarioFesta() {
		return comentarioFesta;
	}
	public void setComentarioFesta(Set<FestaComentarios> comentarioFesta) {
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

	public Set<String> getMaisInformacoes() {
		return maisInformacoes;
	}

	public void setMaisInformacoes(Set<String> maisInformacoes) {
		this.maisInformacoes = maisInformacoes;
	}
	
}
