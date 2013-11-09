package br.com.qualABoaDF.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.qualABoaDF.MainActivityDetail;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.adapter.Adapter;
import br.com.qualABoaDF.banco.RepositorioDataSource;
import br.com.qualABoaDF.negocial.Festa;
import br.com.qualABoaDF.negocial.FestaDetalhes;
import br.com.qualABoaDF.negocial.FestaList;
import br.com.qualABoaDF.spider.Spider;
import br.com.qualABoaDF.transacao.Transacao;
import br.com.qualABoaDF.transacao.TransacaoTask;
import br.com.qualABoaDF.utils.TipoInformacao;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;


public class FavoriteFragment extends SherlockFragmentActivity implements Transacao, OnItemClickListener{
	
	private ListView list;
	private Adapter adapter;
	private List<Festa> listFestas = new ArrayList<Festa>();
	private RepositorioDataSource repositorioDataSource;
	private TransacaoTask transacaoTask;
	private Festa informacoesFesta;
	private FestaDetalhes outrasInformacoesFesta;
	private Spider spider;
	private Intent intent;
	private Map<String, Festa> mapListTodasFestas = new HashMap<String, Festa>();
			
			@Override
			public void onSaveInstanceState(Bundle outState) {
				super.onSaveInstanceState(outState);
				outState.putSerializable(FestaList.KEY, new FestaList(listFestas));
			}			

			@Override
			protected void onCreate(Bundle bundle) {
				super.onCreate(bundle);
				
				setContentView(R.layout.fragment_favorite);
				
				for (Festa festa :  PartyListFragment.listFestas) {
					mapListTodasFestas.put(festa.getNomeFesta(), festa);
				}
				
				getSherlock().getActionBar().setHomeButtonEnabled(true);
				getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);
				getSherlock().getActionBar().setTitle("Seus favoritos");
				
				list = (ListView) findViewById(R.id.listFavorite);
				list.setOnItemClickListener(this);
				
				if(listFestas.size() < 1 && bundle != null){
					FestaList listSavedInstance  = (FestaList) bundle.getSerializable(FestaList.KEY);
					listFestas = listSavedInstance.getFestas(); 
					adapter = new Adapter(this, listFestas);
					list.setAdapter(adapter);
				}else{
					repositorioDataSource = new RepositorioDataSource(this);
					repositorioDataSource.open();
					listFestas = repositorioDataSource.getAllFavoritePartys();
					repositorioDataSource.close();
					adapter = new Adapter(this, listFestas);
					list.setAdapter(adapter);
				}
				
				for (Festa festaFavorita : listFestas) {
					if(mapListTodasFestas.get(festaFavorita.getNomeFesta()) == null){
						repositorioDataSource.open();
						repositorioDataSource.deleteFavorite(festaFavorita);
						listFestas.remove(festaFavorita);
						repositorioDataSource.close();
					}
				}
			}
			
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				finish();
				return super.onOptionsItemSelected(item);
			}
			
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int posicao, long id){
				
				informacoesFesta = (Festa) parent.getAdapter().getItem(posicao);
				outrasInformacoesFesta = new FestaDetalhes();
				transacaoTask = new TransacaoTask(this, this, R.string.aguarde);
				transacaoTask.execute(outrasInformacoesFesta.getTipoInformacao());
			}

			@Override
			public void executar(TipoInformacao tipoInformacao) {
				
			try {
				if(tipoInformacao.equals(TipoInformacao.OUTRAS_INFORMACOES_FESTA))
					spider = new Spider();
					outrasInformacoesFesta = spider.outrasInformacoes(informacoesFesta.getMaisInformacoesURL());
				} catch (IOException e) {
					e.printStackTrace();
				  }					
			intent = new Intent(this, MainActivityDetail.class);
			intent.putExtra(Festa.KEY, informacoesFesta);
			intent.putExtra(FestaDetalhes.KEY, outrasInformacoesFesta);
			startActivity(intent);

			}

			@Override
			public void atualizarView() {
				// TODO Auto-generated method stub
				
			}

}
