package br.com.qualABoaDF.spider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import br.com.qualABoaDF.negocial.FestaComentarios;
import br.com.qualABoaDF.negocial.FestaDetalhes;
import br.com.qualABoaDF.negocial.Festa;
import br.com.qualABoaDF.negocial.FestaMapa;

public class Spider {

	private FestaDetalhes detalhesFesta;
	private FestaComentarios comentarioFesta;
	private URL URLdeboaUrl,URLdetalhes,URLcomentarios,URLmapa;
	private BufferedReader reader;
	private String line;
	private String titulo,data,hora,local,imgUrl,maisInformacoesUrl;
	private Festa festa;
	private List<Festa> listFestas;
	private boolean pegouNomeEvento,pegarAtracoes,pegarIngressos,pegarMaisInformacoes,pegouData;
	private List<String> atracoes;
	private List<String> ingressos;
	private List<String> maisInformacoes;
	private String patternOutraInformacoes,patternData,patternDataFormat,patternComentario,patternComentarioFormat;
	
	public List<Festa> pegarInformacoesFesta(String urlWeb){

		festa = new Festa();
		listFestas = new ArrayList<Festa>();
		
		pegouNomeEvento = false;
		
		
		try {
			
			URLdeboaUrl = new URL(urlWeb);
			
			reader = new BufferedReader(new InputStreamReader(URLdeboaUrl.openStream()));
	
			while ((line=reader.readLine()) != null) {
				
				if(line.contains("src=http://brasilia.deboa.com/wp-content/uploads/")){
					imgUrl = line.substring(line.lastIndexOf("src=\""),line.lastIndexOf("\" alt")).replace("src=\"","").replace("\" alt", "").replace("’", "%C3%AD");
					imgUrl = imgUrl.replaceAll("(‡|ç)","%C3%81").replaceAll("(Ž|ƒ)","%C3%A9").replaceAll("(’|ê)","%C3%AD").replaceAll("(—|î)","%C3%B3");
					festa.setImagemFesta(imgUrl);
					imgUrl = "";
				}
				
				if(line.contains("href=\"http://brasilia.deboa.com/baladas/") && line.contains("rel=") && line.contains("title=")){
					if(pegouNomeEvento == false){
						titulo = line.substring(line.lastIndexOf("title="),line.lastIndexOf("\">")).replace("&#038;", "&").replace("&#8211;", "-").
								replace("&#8217;", "`").replace("&#8220;", "\"").replace("&#8221;", "\"").replace("title=\"", "");
						festa.setNomeFesta(titulo);
						pegouNomeEvento = true;
						titulo = "";
						continue;
					}
				}
				
				if(line.contains("<b>Data:")){
					line = line.trim().replace("\t", "");
					
					if(line.contains("<b>Hora")){
						data = line.substring(line.lastIndexOf("Data: </b>"),line.lastIndexOf("<b>Hora")).replace("Data: </b>", "").replace("<br />", "");
					}else{
						data = line.substring(line.lastIndexOf("Data: </b>"),line.lastIndexOf("<b>Local")).replace("Data: </b>", "").replace("<br />", "");
					}
					
					festa.setDataFesta(data);
					data = "";
					
				}
				
				if(line.contains("<b>Hora:")){
					hora = line.substring(line.lastIndexOf("Hora: </b>"),line.lastIndexOf("<b>Local")).replace("Hora: </b>", "").replace("<br />", "");
					festa.setHoraFesta(hora);
					hora = "";
					
				}
				
				if(line.contains("<b>Local:")){
					
					local = line.substring(line.lastIndexOf("Local: </b>"),line.lastIndexOf("<br")).replace("Local: </b>", "").replace("</b>", "");
					festa.setLocalFesta(local);
					local = "";
				}
				
				if(line.contains("Veja todas as informa")){
					maisInformacoesUrl = line.substring(line.lastIndexOf("<a href"),line.lastIndexOf("\" rel")).replace("\" rel", "").replace("href=\"", "").replace("<a", "");
					festa.setMaisInformacoesURL(maisInformacoesUrl);
					listFestas.add(festa);
					pegouNomeEvento = false;
					festa = new Festa();
					
					continue;
				}

			}
			
			reader.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listFestas;

	}
	
	public FestaDetalhes outrasInformacoes(String url) throws IOException{
		
		detalhesFesta = new FestaDetalhes();
		detalhesFesta.setAtracoes(new HashSet<String>());
		detalhesFesta.setIngressos(new HashSet<String>());
		detalhesFesta.setMaisInformacoes(new HashSet<String>());
		
		atracoes = new ArrayList<String>();
		ingressos = new ArrayList<String>();
		maisInformacoes = new ArrayList<String>();
		
	    reader = null;
		
		try {
			
			URLdetalhes = new URL(url);
			
			reader = new BufferedReader(new InputStreamReader(URLdetalhes.openStream()));
	
			while ((line = reader.readLine()) != null) {
				
				
				if(line.contains("<h3>Aten") || line.contains("<h3>aten")){
					
					break;
				}
				
				if(line.contains("<h3>Mais Informa") || line.contains("<h3>mais informa")){
					
					pegarAtracoes = false;
					pegarIngressos = false;
					pegarMaisInformacoes = true;
					
					continue;
					
				}
				
				if(line.contains("<h3>Ingressos")|| line.contains("<h3>Ingresso") || line.contains("<h3>ingressos") || line.contains("<h3>ingresso")){
					
					pegarMaisInformacoes = false;
					pegarAtracoes = false;
					pegarIngressos = true;
					
					continue;
					
				}
				
				if(line.contains("<h3>atra") || line.contains("<h3>Atra")){
					
					pegarMaisInformacoes = false;
					pegarIngressos = false;
					pegarAtracoes = true;
					
					continue;
					
				}
				
				if(pegarAtracoes == true){
					
					atracoes.add(line);
					
					continue;
					
					
				}else if(pegarIngressos == true){
					
					ingressos.add(line);
					
					continue;
				
				}else if(pegarMaisInformacoes == true){
					
					maisInformacoes.add(line);
					
					continue;
				}
			
			}
			
			patternOutraInformacoes = "(<)(/*)(\\w+.)(\"*)(/*)(>*)";
			
			for (String at : atracoes) {
				
				at = at.replaceAll(patternOutraInformacoes, "");
				detalhesFesta.getAtracoes().add(at);
			}
	
			for (String in : ingressos) {
							
				in = in.replaceAll(patternOutraInformacoes, "");
				detalhesFesta.getIngressos().add(in);
			}


			for (String mi : maisInformacoes) {
				
				mi = mi.replaceAll(patternOutraInformacoes, "");
				detalhesFesta.getMaisInformacoes().add(mi);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}finally{
			reader.close();
		}
		
			mapa(url);
			
			comentarios(url);
		
		return detalhesFesta;
		
		
	}
	

	private void comentarios(String url) throws IOException {
		
			patternData = "(.+)[:][0-9][0-9](<)(/*)(\\w+.)(\"*)(/*)(>*)(\t*)(<)(/*)(\\w+.)(\"*)(/*)(>*)";
			patternDataFormat = "(<)(/*)(\\w+.)(\"*)(/*)(>*)(\t*)(<)(/*)(\\w+.)(\"*)(/*)(>*)";
			patternComentario = "(.+)(<)(/*)(\\w+.)(\"*)(/*)(>*)";
			patternComentarioFormat = "(<)(/*)(\\w+.)(\"*)(/*)(>*)";
			
			detalhesFesta.setComentarioFesta(new HashSet<FestaComentarios>());
			
			try {
				
				URLcomentarios = new URL(url);
			 
			
			reader = new BufferedReader(new InputStreamReader(URLcomentarios.openStream()));

			while ((line = reader.readLine()) != null) {
				
				if(line.contains("class=\"fn\"") && line.replace("</cite> <span", "</cite><span").contains("</cite><span")){
					
					comentarioFesta = new FestaComentarios();
					comentarioFesta.setNome(line.substring(line.lastIndexOf("\"fn\">"), line.lastIndexOf("</cite>")).replace("\"fn\">", ""));
					continue;
				}
				
				if(line.matches(patternData)){

					comentarioFesta.setData(line.replaceAll(patternDataFormat, ""));
					pegouData = true;
					continue;
				}
				
				if(pegouData == true && line.matches(patternComentario)){
					
					comentarioFesta.setComentario(line.replaceAll(patternComentarioFormat,""));
					detalhesFesta.getComentarioFesta().add(comentarioFesta);
					pegouData = false;
					continue;
					
				}	
			}
			
			}catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
				reader.close();
			}

		}
	
	private void mapa(String url) throws IOException{
		
		detalhesFesta.setMapaFesta(new FestaMapa());
		
		try {
			
			URLmapa = new URL(url);
			reader = new BufferedReader(new InputStreamReader(URLmapa.openStream()));
		
		while ((line = reader.readLine()) != null) {
			
			if(line.contains("&amp;ll")){
				
				line = line.substring(line.lastIndexOf("&amp;ll"),line.lastIndexOf("&amp;spn")).replace("&amp;ll", "");
				String latitude = line.substring(1,line.lastIndexOf(","));
				String longitude = line.substring(line.lastIndexOf(",")).replace(",", "");
				
				detalhesFesta.getMapaFesta().setLatitude(latitude);
				detalhesFesta.getMapaFesta().setLongitude(longitude);
				
			}else if(line.contains("&amp;saddr")){
				
				line = line.substring(line.lastIndexOf("&amp;saddr"),line.lastIndexOf("&amp;daddr")).replace("&amp;saddr", "");
				String latitude = line.substring(1,line.lastIndexOf(","));
				String longitude = line.substring(line.lastIndexOf(",")).replace(",", "");
				
				detalhesFesta.getMapaFesta().setLatitude(latitude);
				detalhesFesta.getMapaFesta().setLongitude(longitude);
			}
		}
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			reader.close();
		}
		
	}

}
