package br.com.qualABoaDF.cards;

import java.util.Set;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.qualABoaDF.R;

import com.fima.cardsui.objects.Card;

public class CardSimpleRaw extends Card {

	
	public CardSimpleRaw(String title, Set<String> outrasInformacoes){
		super(title,outrasInformacoes);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_simple_raw, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		
		LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
		
		if(outrasInformacoes.size() == 0 ){
			
			TextView textView = new TextView(context);
			textView.setText("Sem informação");
			textView.setEllipsize(TruncateAt.END);
			textView.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
			LinearLayout.LayoutParams lpTextnew = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lpTextnew.setMargins(8, 0, 0, 0);
			textView.setLayoutParams(lpTextnew);
			ll.addView(textView);
			
			return view;
		}
			
		for (String outraInformacao : outrasInformacoes) {
			if(!outraInformacao.equals("")){
				TextView textView = new TextView(context);
				textView.setText(outraInformacao);
				textView.setEllipsize(TruncateAt.END);
				textView.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
				LinearLayout.LayoutParams lpTextnew = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				lpTextnew.setMargins(8, 0, 0, 0);
				textView.setLayoutParams(lpTextnew);
				ll.addView(textView);
			}
		}
       
		return view;
	}

	
	
	
}
