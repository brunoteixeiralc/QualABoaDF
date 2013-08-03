package br.com.qualABoaDF.negocial;

import java.io.Serializable;

import br.com.qualABoaDF.utils.TipoInformacao;

public class Festa implements Serializable {

	private static final long serialVersionUID = 2113330334087538251L;
	
	public static final String KEY = "informacoesFesta";
	private int id;
	private String nomeFesta;
	private String dataFesta;
	private String horaFesta;
	private String localFesta;
	private String imagemFesta;
	private String maisInformacoesURL;
	private TipoInformacao tipoInformacao;
	
	public Festa(){
		
		tipoInformacao = TipoInformacao.INFORMACOES_FESTA;
	}

	public String getNomeFesta() {
		return nomeFesta;
	}
	public void setNomeFesta(String nomeFesta) {
		this.nomeFesta = nomeFesta;
	}

	public String getDataFesta() {
		return dataFesta;
	}
	public void setDataFesta(String dataFesta) {
		this.dataFesta = dataFesta;
	}
	
	public String getHoraFesta() {
		return horaFesta;
	}
	public void setHoraFesta(String horaFesta) {
		this.horaFesta = horaFesta;
	}
	public String getLocalFesta() {
		return localFesta;
	}
	public void setLocalFesta(String localFesta) {
		this.localFesta = localFesta;
	}
	public String getImagemFesta() {
		return imagemFesta;
	}
	public void setImagemFesta(String imagemFesta) {
		this.imagemFesta = imagemFesta;
	}

	public String getMaisInformacoesURL() {
		return maisInformacoesURL;
	}

	public void setMaisInformacoesURL(String maisInformacoesURL) {
		this.maisInformacoesURL = maisInformacoesURL;
	}

	public TipoInformacao getTipoInformacao() {
		return tipoInformacao;
	}

	public void setTipoInformacao(TipoInformacao tipoInformacao) {
		this.tipoInformacao = tipoInformacao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
