package br.com.qualABoaDF.fragment;


import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.locationSource.GetRouteDirection;
import br.com.qualABoaDF.negocial.Festa;
import br.com.qualABoaDF.negocial.FestaDetalhes;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public final class MapsFragment extends Fragment implements LocationListener {

	private View view;
	private GoogleMap map;
	private Location locationNetwork,locationGPS;
	private LocationManager locationManager;
	private FestaDetalhes detalhesFesta;
	private Festa informacoesFesta;
	private LatLng latLng;
	private LatLng from;
	private LatLng to;
	private Document document;
	private GetRouteDirection v2GetRouteDirection;
	private TextView duracaoDistancia;
	private boolean opcaoProviderEscolhido;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	    	view = inflater.inflate(R.layout.fragment_maps, null);
	    	duracaoDistancia = (TextView) view.findViewById(R.id.distdur);
	    	map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);
			map.setTrafficEnabled(true);
	    	map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    	 if(this.getArguments() != null){
		    	 Bundle bundle = this.getArguments();
		    	 detalhesFesta = (FestaDetalhes) bundle.getSerializable(FestaDetalhes.KEY);
		    	 informacoesFesta = (Festa) bundle.getSerializable(Festa.KEY);
	    	 }
	    	latLng = new LatLng(Double.parseDouble(detalhesFesta.getMapaFesta().getLatitude()), Double.parseDouble(detalhesFesta.getMapaFesta().getLongitude()));
	    	CameraUpdate position = CameraUpdateFactory.newLatLngZoom(latLng, 13);
			map.moveCamera(position);
			map.animateCamera(position, 1000, new CancelableCallback() {
				
				@Override
				public void onFinish() {
					Toast.makeText(getActivity(), informacoesFesta.getNomeFesta() +" achado", Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onCancel() {
					Toast.makeText(getActivity(), informacoesFesta.getNomeFesta()+" n‹o encontrado", Toast.LENGTH_LONG).show();
				}
			});
			
	    }catch (InflateException e) {
	     }
	    return view;
	}
	
	private void adicionarMarcador(GoogleMap map , LatLng from, LatLng to){

		MarkerOptions markerOptionsFrom = new MarkerOptions();
		markerOptionsFrom.position(from).title("Estou aqui!!");
		map.addMarker(markerOptionsFrom);
		MarkerOptions markerOptionsTo = new MarkerOptions();
		markerOptionsTo.position(to).title(informacoesFesta.getNomeFesta()).snippet(informacoesFesta.getLocalFesta());
		map.addMarker(markerOptionsTo);
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker marker) {
				
				LinearLayout linear = (LinearLayout) this.getInfoContents(marker);
				linear.setBackgroundResource(R.drawable.janela_marker);
				
				return linear;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				
				LinearLayout linear = new LinearLayout(getActivity());
				linear.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				linear.setOrientation(LinearLayout.VERTICAL);

				TextView t = new TextView(getActivity());
				t.setText(marker.getTitle());
				t.setTextColor(Color. parseColor("#000000"));
				t.setGravity(Gravity.CENTER);
				linear.addView(t);
				
				TextView tSnippet = new TextView(getActivity());
				tSnippet.setText(marker.getSnippet());
				tSnippet.setTextColor(Color.parseColor("#407865"));
				linear.addView(tSnippet);

				return linear;
			}
		});
	}

	@Override
	public void onResume() {
		
		super.onResume();
		
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(locationGPS == null)
				calcularRota(locationNetwork);
			else
				calcularRota(locationGPS);

		}else if(!opcaoProviderEscolhido){		
			
			AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
			aBuilder.setMessage("O GPS est‡ desligado, deseja ligar?").setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent callGPSSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(callGPSSettings);
				}
			});
			
			aBuilder.setNegativeButton("N‹o", new DialogInterface.OnClickListener() {
				
			@Override
					public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					calcularRota(locationNetwork);
			}
		});
			
			AlertDialog alertDialog = aBuilder.create();
			alertDialog.show();
			opcaoProviderEscolhido = true;
			
		}else{
			calcularRota(locationNetwork);
		}

	}
	
	@Override
	public void onPause() {
		
		super.onPause();
		//Desliga GPS
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {		
		 if (location != null)
		    {
		        Log.i("SuperMap", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
		    } 
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private void calcularRota(Location location){
		
//		GpsLocationTracker gpsLocationTracker = new GpsLocationTracker(this.getActivity());
//		
//		if (gpsLocationTracker.canGetLocation()) 
//        {
//			from = new LatLng(gpsLocationTracker.getLatitude(), gpsLocationTracker.getLongitude());
//        } 
		
		v2GetRouteDirection = new GetRouteDirection();
		from = new LatLng(location.getLatitude(), location.getLongitude());
		to = new LatLng(Double.parseDouble(detalhesFesta.getMapaFesta().getLatitude()), Double.parseDouble(detalhesFesta.getMapaFesta().getLongitude()));
		GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();
	}
	
	private class GetRouteTask extends AsyncTask<String, Void, String> {
         
         private ProgressDialog Dialog;
         String response = "";
         @Override
         protected void onPreExecute() {
               Dialog = new ProgressDialog(getActivity());
               Dialog.setMessage("Calculando a rota...");
               Dialog.show();
         }

         @Override
         protected String doInBackground(String... urls) {
               document = v2GetRouteDirection.getDocument(from, to, GetRouteDirection.MODE_DRIVING);
               response = "Success";
         return response;

         }

         @Override
         protected void onPostExecute(String result) {
               map.clear();
               if(response.equalsIgnoreCase("Success")){
               ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
               
               PolylineOptions rectLine = new PolylineOptions().width(10).color(
                           Color.RED);

               for (int i = 0; i < directionPoint.size(); i++) {
                     rectLine.add(directionPoint.get(i));

               }
               map.addPolyline(rectLine);
               adicionarMarcador(map, from, to);
               duracaoDistancia.setText("Dist‰ncia de " + v2GetRouteDirection.distance + "km e " + "chegar‡ em " + v2GetRouteDirection.duration + "min");
               }
              
               Dialog.dismiss();
         }
   }
}
	

