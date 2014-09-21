package com.sanchez.sensapp.basedades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Classe que determina l'estructura de les taules de la BD interna. Obre la BDinterna si existeix.
 * Sino existeix, la crea (oncCreate) i la update. Ajuda al SensorsProvider a fer el contacte amb la BDinterna, evitant errors
 */
public class Helper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1; 
	
	private Helper(Context context) {
		super(context, "SensDataBase.bd", null, DB_VERSION);
	}
	
	public static Helper getInstance(Context c){
		return new Helper(c);
	}

	/**
	 constants amb els noms de les columnes de les dades on situare la info proporcionada pel meu
	 content provider. Per afegir les meves columnes al CP, faig servir les columnes standard 
	 ja definides a la classe BaseColumns
	 */
	public static final String DB_TABLE_USER = "user";
	public static final class User implements BaseColumns {
		private User () {}
		public static final String KEY_ID = "_id"; //SEMPRE AQUEST ID PER A LA BBDD
		public static final String KEY_NAME = "name"; // NOMS DIFERENTS COLUMNES 
		public static final String KEY_SURNAME = "surname";
		public static final String KEY_AGE = "age";
		public static final String KEY_ADDRESS = "address";
		public static final String KEY_PHONE = "phone";
		public static final String KEY_EMAIL = "email";
		public static final String KEY_NOTES = "notes";
	}
	
    //Sentencia SQL para crear la tabla de USerss
	private static final String DATABASE_CREATE_User = 
			"create table " + DB_TABLE_USER + "("
			+ User.KEY_ID +" integer primary key AUTOINCREMENT, "
			+ User.KEY_NAME + " text not null, "
			+ User.KEY_SURNAME + " text not null, "
			+ User.KEY_AGE + " text not null, "
			+ User.KEY_ADDRESS + " text not null, "
			+ User.KEY_PHONE + " text not null, "
			+ User.KEY_EMAIL + " text not null, "			
			+ User.KEY_NOTES + " text not null);";
	
	public static final String DB_TABLE_SENSORS = "sensors";
	public static final class Sensors implements BaseColumns {
		private Sensors () {}
		public static final String KEY_ID = "_id"; //SEMPRE AQUEST ID PER A LA BBDD
		public static final String KEY_BATERIA = "bateria"; // NOMS DIFERENTS COLUMNES
		public static final String KEY_GAS = "gas";
		public static final String KEY_AGUA = "agua"; 
		public static final String KEY_LUZ = "luz";
		public static final String KEY_TEMPERATURA = "temperatura";
		public static final String KEY_POSICIO = "posicio";
		public static final String KEY_Name = "name";
		
	}
    //Sentencia SQL per a crear la taula de Sensors
	private static final String DATABASE_CREATE_Sensors = 
			"create table " + DB_TABLE_SENSORS + "("
			+ Sensors.KEY_ID +" integer primary key AUTOINCREMENT, "
			+ Sensors.KEY_Name + " text , "
			+ Sensors.KEY_BATERIA + " text, "
			+ Sensors.KEY_GAS + " text, "
			+ Sensors.KEY_AGUA + " text, "
			+ Sensors.KEY_LUZ + " text , "
			+ Sensors.KEY_TEMPERATURA + " text, "
			+ Sensors.KEY_POSICIO + " text);";
	
	

		public static final String DB_TABLE_MOBILESENSOR = "mobilesensor";
	   //Sentencia SQL per a crear la taula de MobileSensors
		private static final String DATABASE_CREATE_MobileSensor = 
				"create table " + DB_TABLE_MOBILESENSOR + "("
				+ Sensors.KEY_ID +" integer primary key AUTOINCREMENT, "
				+ Sensors.KEY_BATERIA + " text, "
				+ Sensors.KEY_POSICIO + " text);"; //nomes son noms, no adreces
	

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		//Se ejecuta la sentencia SQL de creacion de la tabla
		arg0.execSQL(DATABASE_CREATE_User);
		arg0.execSQL(DATABASE_CREATE_Sensors);
		arg0.execSQL(DATABASE_CREATE_MobileSensor);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase dbtoupgrade, int oldVersion, int newVersion) {
		// fer!! AIXI ESTa Be?????????????????????
		dbtoupgrade.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_USER);
		dbtoupgrade.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_SENSORS);
		dbtoupgrade.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_MOBILESENSOR);
	}
	
}

