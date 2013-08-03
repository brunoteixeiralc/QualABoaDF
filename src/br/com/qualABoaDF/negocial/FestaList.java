package br.com.qualABoaDF.negocial;

import java.io.Serializable;
import java.util.List;

public class FestaList implements Serializable {

	private static final long serialVersionUID = 7552717352879972249L;
	public static final String KEY = "festaList";
	private List<Festa> festas;
	public FestaList(List<Festa> festas){
		this.setFestas(festas);
	}
	public List<Festa> getFestas() {
		return festas;
	}
	public void setFestas(List<Festa> festas) {
		this.festas = festas;
	}

}
