package br.com.qualABoaDF.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.qualABoaDF.R;
import br.com.qualABoaDF.cards.CardSimple;
import br.com.qualABoaDF.cards.CardSimpleRaw;
import br.com.qualABoaDF.cards.CardWithImage;
import br.com.qualABoaDF.negocial.FestaDetalhes;
import br.com.qualABoaDF.negocial.Festa;

import com.fima.cardsui.views.CardUI;


public final class DetailFragment extends Fragment {
	
	private View view;
	private CardUI mCardView;
	private Festa festa;
	private FestaDetalhes outrasInformacoes;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	    	
	     view = inflater.inflate(R.layout.fragment_detail, null);
	     mCardView = (CardUI) this.view.findViewById(R.id.cardsview);
	     mCardView.setSwipeable(false);

	     if(this.getArguments() != null){
	    	 Bundle bundle = this.getArguments();
	    	 festa =  (Festa) bundle.getSerializable(Festa.KEY);
	    	 outrasInformacoes = (FestaDetalhes) bundle.getSerializable(FestaDetalhes.KEY);
	    	  mCardView.addCard(new CardWithImage("Festa",festa.getNomeFesta() == null ? "Sem informção" : festa.getNomeFesta().toString(), R.id.imageView1, festa.getImagemFesta()));
		   	  mCardView.addCard(new CardSimple("Data",festa.getDataFesta() == null ? "Sem informção" : festa.getDataFesta().toString()));
		   	  mCardView.addCard(new CardSimple("Local",festa.getLocalFesta() == null ? "Sem informção" : festa.getLocalFesta().toString()));
		   	  mCardView.addCard(new CardSimple("Hora",festa.getHoraFesta() == null ? "Sem informção" : festa.getHoraFesta().toString()));
		   	  mCardView.addCard(new CardSimpleRaw("Atrações", outrasInformacoes.getAtracoes()));
		   	  mCardView.addCard(new CardSimpleRaw("Ingressos", outrasInformacoes.getIngressos()));
		   	  mCardView.refresh();

	     }

	    } catch (InflateException e) {
	    
	    }
		return view;
	}
	
}
