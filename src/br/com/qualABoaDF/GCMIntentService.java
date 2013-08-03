package br.com.qualABoaDF;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import br.com.qualABoaDF.pushNotification.Constants;
import br.com.qualABoaDF.pushNotification.ServerUtilities;
import br.com.qualABoaDF.utils.ActivityStackUtils;
import br.com.qualABoaDF.utils.NotificationUtil;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService(){
		
		super(Constants.PROJECT_NUMBER);
	}

	@Override
	protected void onError(Context context, String regId) {
		
		log("Erro: " + regId);
	}

	@Override
	protected void onMessage(Context context, Intent regId) {
		
		String msg = regId.getStringExtra("msg");
		log(msg);
		
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		
		ServerUtilities.register(getApplicationContext(), regId);
		
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		
	    if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
	            ServerUtilities.unregister(getApplicationContext(), regId);
	    }
	}
	
	private void log(String msg){
		
		Log.i("GCMIntentService", msg);
		
		if(ActivityStackUtils.isMyApplicationTaskOnTop(getApplicationContext())){
			
			Intent intent = new Intent("RECEIVER_QUE_VAI_RECEBER_ESTA_MSG");
			intent.putExtra("msg", msg);
			sendBroadcast(intent);
		}else{
			
			Intent intent2 = new Intent(this, MainActivity.class);
			intent2.putExtra("msg", msg);
			NotificationUtil.generateNotification(getApplicationContext(), msg , intent2);
		}
		
		
		
	}

}
