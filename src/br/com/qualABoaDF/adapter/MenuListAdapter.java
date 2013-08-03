package br.com.qualABoaDF.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {

	private Context context;
	private String[] title;
	private int[] icon;
	private LayoutInflater layoutInflater;
	
	public MenuListAdapter(Context context, String[] title,
			int[] icon) {
		this.context = context;
		this.title = title;
		this.icon = icon;
	}

	@Override
	public int getCount() {
		return title.length;
	}

	@Override
	public Object getItem(int position) {
		return title[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView txtTitle;
		ImageView imgIcon;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = layoutInflater.inflate(br.com.qualABoaDF.R.layout.drawer_list_item, parent,false);
		txtTitle = (TextView) itemView.findViewById(br.com.qualABoaDF.R.id.title);
        imgIcon = (ImageView) itemView.findViewById(br.com.qualABoaDF.R.id.icon);
        txtTitle.setText(title[position]);
        imgIcon.setImageResource(icon[position]);
        return itemView;
		
	}
    
   
}