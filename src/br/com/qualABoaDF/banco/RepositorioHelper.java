package br.com.qualABoaDF.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RepositorioHelper extends SQLiteOpenHelper {

	public RepositorioHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS favorite";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PARTY_NAME = "nome_festa";
	public static final String COLUMN_PARTY_DATE = "data_festa";
	public static final String COLUMN_PARTY_TIME = "hora_festa";
	public static final String COLUMN_PARTY_LOCALE = "local_festa";
	public static final String COLUMN_PARTY_IMAGE = "imagem_festa";
	public static final String COLUMN_PARTY_MORTE_INFORMATION = "mais_informacoes_url";
	
	public static final String TABLE_FAVORITE = "favorite";
	private static final String NOME_BANCO = "favorite_party";
	private static final int VERSAO_BANCO = 1;
	
	
	private static final String SCRIPT_DATABASE_CREATE ="CREATE TABLE " + TABLE_FAVORITE + " ( " + 
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_PARTY_NAME + " text null," + 
			COLUMN_PARTY_DATE + " text null, "+ 
			COLUMN_PARTY_TIME + " text null, " +
			COLUMN_PARTY_LOCALE + " text null, " +
			COLUMN_PARTY_IMAGE + " text null, " +
			COLUMN_PARTY_MORTE_INFORMATION + " text null);";

	protected SQLiteDatabase db;
	
	public RepositorioHelper(Context context){
		super(context,NOME_BANCO,null,VERSAO_BANCO);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SCRIPT_DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(RepositorioHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL(SCRIPT_DATABASE_DELETE);
		    onCreate(db);
		
	}
	
			
}
