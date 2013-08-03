package br.com.qualABoaDF.pushNotification;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import com.google.android.gcm.GCMRegistrar;
import android.content.Context;
import android.util.Log;

@SuppressWarnings("unused")
public final class ServerUtilities {
	
	private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    private static final String SERVER_URL = "http://android2push.appspot.com";
    private static final String TAG = "Android2Push";
    
    public static boolean register(final Context context, final String regId) {
    	
    	Log.i(TAG, "registrando o aparelho (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
    	
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Tentativas #" + i + " para registrar");
            try {
            	
                post(serverUrl, params);
                GCMRegistrar.setRegisteredOnServer(context, true);
                
				String message = "Aparelho registrado!!";
                
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Falha ao registro na tentativa " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = "Erro para registrar o aparelho, depois de " + MAX_ATTEMPTS;
        return false;
    }
    
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
       
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Postando '" + body + "' para " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post falhou com o seguinte erro " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
    
    public static void unregister(final Context context, final String regId) {
        Log.i(TAG, "removendo aparelho do GCM (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = "Aparelho removido do GCM com sucesso";
         
        } catch (IOException e) {
            String message ="N‹o conseguiu remover o aparelho do GCM " + e.getMessage();       
        
        }
    }
}
 
 

