package br.com.qualABoaDF;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import br.com.qualABoaDF.fragment.PartyListFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class MainActivity extends SherlockFragmentActivity {

	 private UiLifecycleHelper uiHelper;
	 private Intent intent;
	 private Session.StatusCallback callback = new Session.StatusCallback() {
	        @Override
	        public void call(final Session session, final SessionState state, final Exception exception) {
	            onSessionStateChange(session, state, exception);
	        }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this,callback );
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		LoginButton button = (LoginButton) findViewById(R.id.login_button);
		button.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public void onError(FacebookException error) {
				 Log.i("Erro FB", "Error " + error.getMessage());

			}
		});
		
		button.setReadPermissions(Arrays.asList("basic_info","email"));
		
		button.setSessionStatusCallback(new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				if(session.isOpened()){
					Log.i("FB","Access Token"+ session.getAccessToken());
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
	                               intent = new Intent(getApplicationContext(), PartyListFragment.class);
	                               startActivity(intent);
							 }
						}
					});
				}
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}

	}
	
	@Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
	
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
	
	 private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		   	if (state.isOpened()) {
		   		//intent = null;
		        //intent = new Intent(getApplicationContext(), PartyListFragment.class);
		        //startActivity(intent);
		   		Log.i("out", "Logged in...");
		     } else if (state.isClosed()) {
		       	Log.i("out", "Logged out...");
		       }
		   }
}
