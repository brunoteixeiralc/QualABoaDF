package br.com.qualABoaDF;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import br.com.qualABoaDF.banco.RepositorioDataSource;
import br.com.qualABoaDF.fragment.CommentsFragment;
import br.com.qualABoaDF.fragment.DetailFragment;
import br.com.qualABoaDF.fragment.MapsFragment;
import br.com.qualABoaDF.negocial.Festa;
import br.com.qualABoaDF.negocial.FestaDetalhes;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

@SuppressWarnings("unused")
public class MainActivityDetail extends FragmentActivity implements TabListener {

	private Festa festa;
	private FestaDetalhes festaDetalhes;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private static final String TAG = "Facebook Publish";
	private RepositorioDataSource repositorioDataSource;
	private ShareActionProvider shareActionProvider;
	protected static Bundle args;
	
	android.support.v4.view.ViewPager mViewPager;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_detail);
		
		if(festa == null && festaDetalhes == null && savedInstanceState != null){
			festa = (Festa) savedInstanceState.getSerializable(Festa.KEY);
			festaDetalhes = (FestaDetalhes) savedInstanceState.getSerializable(FestaDetalhes.KEY);
		}else{
			festa = (Festa)this.getIntent().getSerializableExtra(Festa.KEY);
			festaDetalhes = (FestaDetalhes) this.getIntent().getSerializableExtra(FestaDetalhes.KEY);
		}

		args = new Bundle();
		args.putSerializable(Festa.KEY, festa);
		args.putSerializable(FestaDetalhes.KEY, festaDetalhes);
		
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        final android.app.ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.SimpleOnPageChangeListener(){
        	@Override
        	public void onPageSelected(int position) {
        		 actionBar.setSelectedNavigationItem(position);
        	}
        });

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Informações").setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Como chegar").setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Comentários").setTabListener(this));
                       

	}

	 public static class AppSectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

	        public AppSectionsPagerAdapter(FragmentManager fm) {
	            
	        	super(fm);
	        }

	        @Override
	        public Fragment getItem(int i) {
	            switch (i) {
	                case 0:
	                   
	                	 Fragment detailFragment = new DetailFragment();
	                	 detailFragment.setArguments(args);
	                     return detailFragment;
	                 
	                case 1:
	          
	                	Fragment mapFragment = new MapsFragment();
	                	mapFragment.setArguments(args);
	                    return mapFragment;
	                 
	                case 2:
	                   
	                	Fragment commentsFragment = new CommentsFragment();
	                	commentsFragment.setArguments(args);
	                    return commentsFragment;
	               
	                default:
	            	    
	                	Fragment detailFragment2 = new DetailFragment();
	                	detailFragment2.setArguments(args);
	                    return detailFragment2;
	            }
	        }

	        @Override
	        public int getCount() {
	            return 3;
	        }

	    }

	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 	
		 getMenuInflater().inflate(R.menu.menu_party_detail, menu);
		 android.view.MenuItem item = menu.findItem(R.id.menu_favorite);
			
			if(festa.getId() != 0){
				item.setIcon(R.drawable.ic_rating_important);
				return super.onCreateOptionsMenu(menu);
			}
			
			repositorioDataSource = new RepositorioDataSource(this);
			repositorioDataSource.open();
			festa.setId(repositorioDataSource.getFesta(festa.getNomeFesta()) != 0 ? repositorioDataSource.getFesta(festa.getNomeFesta()) : 0);
			if(festa.getId() != 0)
				item.setIcon(R.drawable.ic_rating_important);
			repositorioDataSource.close();
			
			android.view.MenuItem itemShare = menu.findItem(R.id.menu_item_share);
			shareActionProvider = (ShareActionProvider) itemShare.getActionProvider();
			shareActionProvider.setShareIntent(getDefaultIntent());
			
			return super.onCreateOptionsMenu(menu);
	}
	
	private Intent getDefaultIntent() {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_TEXT,festa.getNomeFesta() + "," + festa.getDataFesta() + "," + festa.getHoraFesta() + "," + festa.getMaisInformacoesURL());
	    return intent;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_favorite:
				repositorioDataSource = new RepositorioDataSource(this);
				repositorioDataSource.open();
				if(item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_rating_important).getConstantState())){
					repositorioDataSource.deleteFavorite(festa);
					item.setIcon(R.drawable.ic_rating_not_important);
				}else{
					repositorioDataSource.createFavorite(festa);
					item.setIcon(R.drawable.ic_rating_important);
				}
				repositorioDataSource.close();
			default:
		       return super.onOptionsItemSelected(item);
			}
	
	}

	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Festa.KEY, festa);
		outState.putSerializable(FestaDetalhes.KEY, festaDetalhes);
	}

	@Override
	public void onTabReselected(android.app.ActionBar.Tab tab,
			android.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(android.app.ActionBar.Tab tab,
			android.app.FragmentTransaction ft) {
		
		mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(android.app.ActionBar.Tab tab,
			android.app.FragmentTransaction ft) {
		
	}

	
}
