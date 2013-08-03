package br.com.qualABoaDF.transacao;

import br.com.qualABoaDF.utils.TipoInformacao;

public interface Transacao {

	public void executar(TipoInformacao tipoInformacao);
	
	public void atualizarView();
	
}
