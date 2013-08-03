package br.com.qualABoaDF.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.negocial.FestaComentarios;
import br.com.qualABoaDF.negocial.FestaDetalhes;

public class CommentsFragment extends Fragment{

	private FestaDetalhes outrasInformacoes;
	
	private LinearLayout ll;
	private View separator;
	
			@SuppressWarnings("deprecation")
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				
				View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
				ll = (LinearLayout)rootView.findViewById(R.id.llComments);

				if(this.getArguments() != null){
			    	 Bundle bundle = this.getArguments();
			    	 outrasInformacoes = (FestaDetalhes) bundle.getSerializable(FestaDetalhes.KEY);
			    	 
			    	 for (FestaComentarios comentarios : outrasInformacoes.getComentarioFesta()) {
			    		 					    		 
								TextView textView = new TextView(getActivity());
								textView.setText("\"" + comentarios.getComentario() + "\"");
								textView.setEllipsize(TruncateAt.END);
								textView.setTypeface(Typeface.SERIF);
								textView.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
								textView.setTextSize(17);
								textView.setPadding(0, 30, 0, 30);
								ll.addView(textView);
								
								separator = new View(getActivity());
								separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1));
								separator.setBackgroundColor(Color.BLACK);
								ll.addView(separator);
								
							}

				}
				
				return rootView;
			}		
}
	

