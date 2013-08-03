package br.com.qualABoaDF.banco;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.qualABoaDF.negocial.Festa;

public class RepositorioDataSource {


	  // Database fields
	  private SQLiteDatabase database;
	  private RepositorioHelper dbHelper;
	  private String[] allColumns = {RepositorioHelper.COLUMN_ID,RepositorioHelper.COLUMN_PARTY_DATE,RepositorioHelper.COLUMN_PARTY_IMAGE,
			  RepositorioHelper.COLUMN_PARTY_LOCALE, RepositorioHelper.COLUMN_PARTY_MORTE_INFORMATION, RepositorioHelper.COLUMN_PARTY_NAME,
			  RepositorioHelper.COLUMN_PARTY_TIME};

	  public RepositorioDataSource(Context context) {
	    dbHelper = new RepositorioHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public void createFavorite(Festa festa) {
	    ContentValues values = new ContentValues();
	    values.put(RepositorioHelper.COLUMN_PARTY_DATE, festa.getDataFesta());
	    values.put(RepositorioHelper.COLUMN_PARTY_IMAGE, festa.getImagemFesta());
	    values.put(RepositorioHelper.COLUMN_PARTY_LOCALE, festa.getLocalFesta());
	    values.put(RepositorioHelper.COLUMN_PARTY_MORTE_INFORMATION, festa.getMaisInformacoesURL());
	    values.put(RepositorioHelper.COLUMN_PARTY_NAME, festa.getNomeFesta());
	    values.put(RepositorioHelper.COLUMN_PARTY_TIME, festa.getHoraFesta());
	    
	    database.insert(RepositorioHelper.TABLE_FAVORITE, null,values);

	  }

	  public void deleteFavorite(Festa festa) {
	    long id = festa.getId();
	    database.delete(RepositorioHelper.TABLE_FAVORITE, RepositorioHelper.COLUMN_ID.toString()+ " =?", new String[]{String.valueOf(id)});
	  }

	  public List<Festa> getAllFavoritePartys() {
	    
		List<Festa> festas = new ArrayList<Festa>();
	    Cursor cursor = database.query(RepositorioHelper.TABLE_FAVORITE,
	        allColumns, null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Festa festa = cursorToFesta(cursor);
	      festas.add(festa);
	      cursor.moveToNext();
	    }
	    
	    cursor.close();
	    return festas;
	  }
	  
	  public int getFesta(String nomeFesta){
		  
		  int idFesta = 0;
		  Cursor cursor = database.query(RepositorioHelper.TABLE_FAVORITE, allColumns, RepositorioHelper.COLUMN_PARTY_NAME.toString() + "=?", 
				  new String[]{nomeFesta}, 
				  null, null, null, null);
		  if(cursor.moveToFirst())
			  idFesta = cursor.getInt(0);
		  
		  return idFesta;
	  }

	  private Festa cursorToFesta(Cursor cursor) {
	    Festa festa = new Festa();
	    festa.setId(cursor.getInt(0));
	    festa.setDataFesta(cursor.getString(1));
	    festa.setImagemFesta(cursor.getString(2));
	    festa.setLocalFesta(cursor.getString(3));
	    festa.setMaisInformacoesURL(cursor.getString(4));
	    festa.setNomeFesta(cursor.getString(5));
	    festa.setHoraFesta(cursor.getString(6));
	    return festa;
	  }
	} 

