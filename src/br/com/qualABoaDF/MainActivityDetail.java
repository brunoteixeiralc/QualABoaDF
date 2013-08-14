package br.com.qualABoaDF;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
@SuppressWarnings("unused")
public class MainActivityDetail extends SherlockFragmentActivity{

	private Festa festa;
	private FestaDetalhes festaDetalhes;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private static final String TAG = "Facebook Publish";
	private RepositorioDataSource repositorioDataSource;
	private com.actionbarsherlock.widget.ShareActionProvider shareActionProvider;

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
		
		DetailFragment fragDetail = new DetailFragment();
		MapsFragment fragMaps = new MapsFragment();
		CommentsFragment fragComments = new CommentsFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(Festa.KEY, festa);
		args.putSerializable(FestaDetalhes.KEY, festaDetalhes);
		
		fragDetail.setArguments(args);
		fragMaps.setArguments(args);
		fragComments.setArguments(args);

		final ActionBar ab = this.getSupportActionBar();

		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayUseLogoEnabled(false);
        ab.setTitle(festa.getNomeFesta());
        
        ab.addTab(ab.newTab().setText("Informações").setTabListener((com.actionbarsherlock.app.ActionBar.TabListener) new ListenerTab(fragDetail)));
        ab.addTab(ab.newTab().setText("Como chegar").setTabListener((com.actionbarsherlock.app.ActionBar.TabListener) new ListenerTab(fragMaps)));
        ab.addTab(ab.newTab().setText("Comentários").setTabListener((com.actionbarsherlock.app.ActionBar.TabListener) new ListenerTab(fragComments)));
        showTabsNav();

	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		 
		
		getSupportMenuInflater().inflate(R.menu.menu_party_detail, menu);
		MenuItem item = menu.findItem(R.id.menu_favorite);
		
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
		
		MenuItem itemShare = menu.findItem(R.id.menu_item_share);
		shareActionProvider = (com.actionbarsherlock.widget.ShareActionProvider) itemShare.getActionProvider();
		shareActionProvider.setShareIntent(getDefaultIntent());
		
		return super.onCreateOptionsMenu(menu);
	}
	
	private Intent getDefaultIntent() {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    //intent.setType("image/*");
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_TEXT,festa.getNomeFesta() + "," + festa.getDataFesta() + "," + festa.getHoraFesta() + "," + festa.getMaisInformacoesURL());
	    //Uri uri = Uri.fromFile(new File(getFilesDir(), festa.getImagemFesta()));
	    //intent.putExtra(Intent.EXTRA_STREAM, uri.toString());
	    return intent;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
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
	
	private class ListenerTab implements com.actionbarsherlock.app.ActionBar.TabListener{
		
		private Fragment frag;

		public ListenerTab(Fragment frag){
			this.frag = frag;
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.content_frame, frag, null);
		}
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {	
			ft.remove(frag);
		}
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {	
		}
	}

	private void showTabsNav() {
		 
	        ActionBar ab = getSupportActionBar();
	        if (ab.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
	            ab.setDisplayShowTitleEnabled(false);
	            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	      }
	 }

	
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "**** Qual a boa DF ****");
	       // postParams.putString("caption", festa.getNomeFesta());
	       // postParams.putString("description", "Data: " + festa.getDataFesta() +"\n"+ "Hora: " + festa.getHoraFesta());
	        postParams.putString("link", festa.getMaisInformacoesURL());
	       // postParams.putString("picture", festa.getImagemFesta());

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getApplicationContext()
	                         .getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getApplicationContext()
	                             .getApplicationContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
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

}
