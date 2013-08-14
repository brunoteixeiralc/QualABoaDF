package br.com.qualABoaDF.jersey.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.qualABoaDF.negocial.Festa;

public class FestasJSONParser {

	private JSONArray arrayParser;
	
	public FestasJSONParser(String json){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			this.arrayParser = jsonObject.getJSONArray("festa");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	private JSONObject getFestaJSON(int i) {
        try {
            return arrayParser.getJSONObject(i);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
	
	private Festa getParty(int i){
		Festa f = new Festa();
		try {
			JSONObject festaJson = getFestaJSON(i);

			String nome = festaJson.getString("nome");
			String data = festaJson.getString("data");
			String hora = festaJson.getString("hora");
			String local = festaJson.getString("local");
			String imagem = festaJson.getString("imagem");
			String maisInformacoesURL = festaJson.getString("maisInformacoesURL");
			
			imagem = imagem.replaceAll("(á|Á)","%C3%81").replaceAll("(é|É)","%C3%A9").
					replaceAll("(í|Í)","%C3%AD").replaceAll("(ó|Ó)","%C3%B3").
					replaceAll("â|Â", "%C3%A2").replaceAll("ç|Ç", "%C3%A7").replaceAll("ã|Ã", "%C3%A3").
					replaceAll("ê|Ê", "%C3%AA");
			
			f.setNomeFesta(nome);
			f.setDataFesta(data);
			f.setHoraFesta(hora);
			f.setLocalFesta(local);
			f.setImagemFesta(imagem);
			f.setMaisInformacoesURL(maisInformacoesURL);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return f;
		
		
	}
	
	public List<Festa> getPartys(){
		List<Festa> festas = new ArrayList<Festa>();
		for (int i = 0; i < arrayParser.length(); i++) {
            festas.add(getParty(i));
        }
        return festas;
	}
}
