package br.com.qualABoaDF.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import br.com.qualABoaDF.MainActivityDetail;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.adapter.Adapter;
import br.com.qualABoaDF.adapter.MenuListAdapter;
import br.com.qualABoaDF.banco.RepositorioDataSource;
import br.com.qualABoaDF.jersey.json.FestasJSONParser;
import br.com.qualABoaDF.negocial.Festa;
import br.com.qualABoaDF.negocial.FestaComentarios;
import br.com.qualABoaDF.negocial.FestaDetalhes;
import br.com.qualABoaDF.negocial.FestaList;
import br.com.qualABoaDF.negocial.FestaMapa;
import br.com.qualABoaDF.spider.Spider;
import br.com.qualABoaDF.transacao.Transacao;
import br.com.qualABoaDF.transacao.TransacaoTask;
import br.com.qualABoaDF.utils.TipoInformacao;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;

@SuppressWarnings("unused")
public final class PartyListFragment extends SherlockFragmentActivity implements OnItemClickListener, Transacao {

	
	private String mContent;
	private ListView list;
	private List<Festa> listFestas = new ArrayList<Festa>();
	private FestaDetalhes outrasInformacoesFesta;
	private FestaMapa mapaFesta;
	private Festa informacoesFesta;
	private FestaComentarios comentarioFesta;
	private Spider spider;
	private TipoInformacao tipoInformacao;
	private TransacaoTask transacaoTask;
	private Intent intent;
	private View mHeader;
	private TextView bottomText;
	private EditText editSearch;
	private Adapter adapter;
	private RepositorioDataSource repositorioDataSource;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mItemsLeftMenu;
	private String[] title;
	private int[] icon;
	private MenuListAdapter menuListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_list);
		
		 title = new String[] { "Eventos em Brasília", "Seus Favoritos",
         "Configurações" };

		 icon = new int[] { R.drawable.ic_collections_go_to_today,R.drawable.ic_rating_important , R.drawable.ic_settings};
			
		 mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		 mDrawerList = (ListView)findViewById(R.id.left_drawer);
		 mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		 menuListAdapter = new MenuListAdapter(this, title, icon);
		 mDrawerList.setAdapter(menuListAdapter);
		 mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		 mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
		 	public void onDrawerClosed(View view) {
		         // TODO Auto-generated method stub
		         super.onDrawerClosed(view);
		     }
		
		     public void onDrawerOpened(View drawerView) {
		         // TODO Auto-generated method stub
		         super.onDrawerOpened(drawerView);
		     }
		 };
		 
		 mDrawerLayout.setDrawerListener(mDrawerToggle);
		 
		 getSherlock().getActionBar().setHomeButtonEnabled(true);
		 getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);
		 getSherlock().getActionBar().setTitle("Eventos em Brasília");		
				
		list = (ListView)findViewById(R.id.list);
		list.setOnItemClickListener(this);
		informacoesFesta = new Festa();
		spider = new Spider();
		
		if(listFestas.size() < 1 && savedInstanceState != null){
			FestaList listSavedInstance  = (FestaList) savedInstanceState.getSerializable(FestaList.KEY);
			listFestas = listSavedInstance.getFestas(); 
			adapter = new Adapter(this, listFestas);
			list.setAdapter(adapter);
		}
		else{
			transacaoTask = new TransacaoTask(this, this, R.string.aguarde);
			transacaoTask.execute(informacoesFesta.getTipoInformacao());
		}
		
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
	}
	
	private void selectItem(int position) {
		
		intent = null;
		switch (position) {
		case 0:
			if(listFestas.size() < 1)
				intent = new Intent(this, PartyListFragment.class);
			break;
		case 1:
			intent = new Intent(this, FavoriteFragment.class);
			break;
		case 2:
			intent = new Intent(this, SettingPreferenceActivity.class);
			break;
		}
		
		if(intent != null){
			startActivity(intent);
		}
		
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
		
    }
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		 mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(FestaList.KEY, new FestaList(listFestas));
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
		
		switch (tipoInformacao) {
		
			case INFORMACOES_FESTA:
				//listFestas = spider.pegarInformacoesFesta("http://brasilia.deboa.com/categoria/baladas/festas-e-shows");
				listFestas = getPartys();
				break;
				
			 case OUTRAS_INFORMACOES_FESTA:
				try {
					outrasInformacoesFesta = spider.outrasInformacoes(informacoesFesta.getMaisInformacoesURL());					
					intent = new Intent(this, MainActivityDetail.class);
					intent.putExtra(Festa.KEY, informacoesFesta);
					intent.putExtra(FestaDetalhes.KEY, outrasInformacoesFesta);
					startActivity(intent);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}

	}

	@Override
	public void atualizarView() {
		if(listFestas != null){
			adapter = new Adapter(this, listFestas);
			list.setAdapter(adapter);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		   inflater.inflate(R.menu.menu_party_list, menu);
		editSearch = (EditText) menu.findItem(R.id.menu_search).getActionView();
		editSearch.addTextChangedListener(textWatcher);
		MenuItem menuSearch = menu.findItem(R.id.menu_search);
		menuSearch.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				editSearch.requestFocus();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				editSearch.setText("");
				editSearch.clearFocus();
				return false;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				listFestas.clear();
				transacaoTask = new TransacaoTask(this, this, R.string.aguarde);
				transacaoTask.execute(informacoesFesta.getTipoInformacao());
				return true;
			case android.R.id.home:
				if(mDrawerLayout.isDrawerOpen(mDrawerList)){
					mDrawerLayout.closeDrawer(mDrawerList);
				}else{
					mDrawerLayout.openDrawer(mDrawerList);
				}
				return true;
			default:	
			return super.onOptionsItemSelected(item);
		}
	}
	
	private List<Festa> getPartys(){
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://android2party2push.appspot.com/resources/festa/json");
			HttpResponse response = client.execute(request);
	        String jsonString = EntityUtils.toString(response.getEntity());
	        // Parse the response
	        FestasJSONParser parser = new FestasJSONParser(jsonString);
	        return parser.getPartys();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
			adapter.filter(text);
			
		}
	};

}
