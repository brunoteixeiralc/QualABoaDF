package br.com.qualABoaDF.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.utils.ImageLoader;
import com.fima.cardsui.objects.Card;

public class CardWithImage extends Card {
	
	private ImageLoader imageLoader;
	private ImageView imagemFesta;
	
	public CardWithImage(String title, String desc, int image, String urlImage){
		super(title, desc, image, urlImage);
	}
	
	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_with_image, null);
		
		imageLoader = new ImageLoader(context);
		imagemFesta = (ImageView) view.findViewById(R.id.imageView1);
		imageLoader.DisplayImage(urlImage, imagemFesta);
		
		((TextView) view.findViewById(R.id.title)).setText(title);
		view.findViewById(R.id.imageView1);
		
		return view;
	}

	
	
	
}
