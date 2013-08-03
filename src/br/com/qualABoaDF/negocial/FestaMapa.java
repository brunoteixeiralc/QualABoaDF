package br.com.qualABoaDF.negocial;

import java.io.Serializable;

public class FestaMapa implements Serializable {

	private static final long serialVersionUID = -3999522013314465050L;

	public static final String KEY = "mapaFesta";
	
	private String latitude;
	
	private String longitude;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
