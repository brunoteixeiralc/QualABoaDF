package br.com.qualABoaDF.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import br.com.qualABoaDF.R;
import com.fima.cardsui.objects.Card;

public class CardSimple extends Card {

	
	public CardSimple(String title, String desc){
		super(title,desc);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_simple, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.description)).setText(desc);
		
		return view;
	}

	
	
	
}
