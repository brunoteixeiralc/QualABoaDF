package br.com.qualABoaDF.utils;

import java.io.InputStream;
import java.io.OutputStream;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
@SuppressWarnings("deprecation")
public class Utils {
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static boolean isNetworkAvailable(Context context){
    	
    	ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	if(connectivityManager == null){
    		return false;
    	}else{
    		
    		NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
    		if(info != null){
    			
    			for (int i = 0; i < info.length; i++) {
					if(info[i].getState() == State.CONNECTED){
						return true;
					}
				}
    		}
    	}
    	
    	return false;
    }
    
   
	public static void alertDialog(final Context context, final int mensagem){
    	try {
			
    		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Problema =(").setMessage(mensagem).create();
    		dialog.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					return;
					
				}
			});
    		
    		dialog.show();
    		
		} catch (Exception e) {
			Log.e("Erro", e.getMessage());
		}
    }
   
	public static void alertDialog(Context context, String mensagem) {
		try {
			
    		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Problema =(").setMessage(mensagem).create();
    		dialog.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
					
				}
			});
    		
    		dialog.show();
    		
		} catch (Exception e) {
			Log.e("Erro", e.getMessage());
		}
	}

}