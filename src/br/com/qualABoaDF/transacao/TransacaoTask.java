package br.com.qualABoaDF.transacao;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gcm.GCMRegistrar;
import br.com.qualABoaDF.utils.TipoInformacao;
import br.com.qualABoaDF.utils.Utils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class TransacaoTask extends AsyncTask<TipoInformacao, Void, Map<String, Object>>{

	
	private ProgressDialog progresso;
	private Context context;
	private int aguardeMsg;
	private final Transacao transacao;
	private Throwable exceptionErro;
	private boolean alertNotificacao ;
	private Map<String, Object> mapResult;
	private Editor editor;
	private SharedPreferences sp;
	
	public TransacaoTask(Context context, Transacao transacao, int aguardeMsg) {
		
		this.context = context;
		this.aguardeMsg = aguardeMsg;
		this.transacao = transacao;

	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		
		mapResult  = new HashMap<String, Object>();
		
		abrirProcesso();
		
	}
	

	@Override
	protected Map<String, Object> doInBackground(TipoInformacao... params) {
		
		try {
			
			transacao.executar(params[0]);
			mapResult.put("tipoInformacao", params[0].name());
			
		} catch (Exception e) {
			
			Log.e("Erro", e.getMessage());
			mapResult.put("result", false);
			return mapResult;
			
		}finally{
			
			try {
				
				fecharProcesso();
				
				
			} catch (Exception e2) {
				Log.e("Erro", e2.getMessage());
			}
		}
		mapResult.put("result", true);
		return mapResult;
	}
	
	@Override
	protected void onPostExecute(Map<String, Object> result) {
		
		if(result.get("result").equals(true) && result.get("tipoInformacao").equals(TipoInformacao.INFORMACOES_FESTA.toString())){
			transacao.atualizarView();
			loadPrefs();
			
			if(alertNotificacao){
				AlertDialogNotificationPush();
				savePrefs();
			}
			
		}else if(result.get("result").equals(false)){
			
			Utils.alertDialog(context, "Erro: " + exceptionErro.getMessage());
			
		}
		
	super.onPostExecute(result);
	
	}

	private void fecharProcesso() {
		
		try {
			
		if(progresso != null)
			progresso.dismiss();
			
			
			
		} catch (Exception e) {
			
			Log.e("Erro", e.getMessage());
		}
		
	}
	
	private void abrirProcesso() {
		
		try {
			
			progresso = ProgressDialog.show(context, "", context.getString(aguardeMsg));
			
		} catch (Exception e) {
			
			Log.e("Erro", e.getMessage());
		}
		
		
	}
	
	private void AlertDialogNotificationPush(){
		
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		editor = sp.edit();
		
		AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
		aBuilder.setMessage("Deseja receber notificação sobre novas festas?").setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				editor.putBoolean("NOTIFICATION", true);
				editor.commit();
				registerDevice();
				
			}
		});
		
		aBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				editor.putBoolean("NOTIFICATION", false);
				editor.commit();
				dialog.cancel();
				
			}
		});
		
		AlertDialog alertDialog = aBuilder.create();
		alertDialog.show();
		
		
	}
	
	private void registerDevice(){
		
		GCMRegistrar.checkDevice(context);
		
		GCMRegistrar.checkManifest(context);
		
		final String regId = GCMRegistrar.getRegistrationId(context);
		
		if(regId.equals("")){
			
			GCMRegistrar.register(context, br.com.qualABoaDF.pushNotification.Constants.PROJECT_NUMBER);
			
			Toast toast = Toast.makeText(context, "Notificação ativa", Toast.LENGTH_LONG);
			toast.show();
			
		}else{
			
			Toast toast = Toast.makeText(context, "Aparelho já registrado", Toast.LENGTH_LONG);
			toast.show();
			
		}
	}
	
	
	private void loadPrefs(){
		
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		alertNotificacao = sp.getBoolean("ALERT_NOTIFICATION", true);
		
	}
	
	private void savePrefs(){
		
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		editor = sp.edit();
		editor.putBoolean("ALERT_NOTIFICATION", false);
		editor.commit();
	}
	
}
