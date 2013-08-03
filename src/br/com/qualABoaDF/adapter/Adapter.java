package br.com.qualABoaDF.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.negocial.Festa;
import br.com.qualABoaDF.utils.ImageLoader;



public class Adapter extends BaseAdapter {
    
    private Activity activity;
    private List<Festa> dataFestas;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private Festa festa;
    private ArrayList<Festa> arrayList;

  	static class ViewHolder{
  		
  		TextView evento;
  		TextView data;
  		TextView hora;
  		TextView local;
  		ImageView list_image;

  	}
    
    public Adapter(Activity a, List<Festa> d) {
       
    	activity = a;
        dataFestas=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        arrayList = new ArrayList<Festa>();
        arrayList.addAll(dataFestas);
    
    }

	public int getCount() {
        return dataFestas.size();
    }

    public Object getItem(int position) {
        return dataFestas != null ? dataFestas.get(position) : null;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
    
    	ViewHolder holder = null;
    	
        if(convertView==null){
        	
        	 holder = new ViewHolder();
        	 
        	 convertView = inflater.inflate(R.layout.fragment_list_row, null);
        	 convertView.setTag(holder);
        	 
             holder.evento = (TextView)convertView.findViewById(R.id.evento);
             holder.data = (TextView)convertView.findViewById(R.id.data);
             holder.hora = (TextView)convertView.findViewById(R.id.hora); 
             holder.local = (TextView) convertView.findViewById(R.id.local);
             holder.list_image = (ImageView)convertView.findViewById(R.id.list_image); 
        	 
        }else{
        	
        	holder = (ViewHolder) convertView.getTag();
        }
       
        
        festa = new Festa();
        
        festa = dataFestas.get(position);

        holder.evento.setText(festa.getNomeFesta());
        holder.data.setText("Data: " + festa.getDataFesta());
        holder.hora.setText("Hora: " + festa.getHoraFesta());
        holder.local.setText("Local: " + festa.getLocalFesta());
        imageLoader.DisplayImage(festa.getImagemFesta(), holder.list_image);
        
        return convertView;
    }
    
    public void filter(String charText){
    	charText = charText.toLowerCase(Locale.getDefault());
    	dataFestas.clear();
    	if(charText.length() == 0){
    		dataFestas.addAll(arrayList);
    	}else{
    		for (Festa festa : arrayList) {
				if(festa.getNomeFesta() != null && festa.getNomeFesta().toLowerCase(Locale.getDefault()).contains(charText)){
					dataFestas.add(festa);
				}
			}
    	}
    	
    	notifyDataSetChanged();
    	
    }
}