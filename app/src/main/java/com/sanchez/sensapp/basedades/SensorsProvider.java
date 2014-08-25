package com.sanchez.sensapp.basedades;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


/**
 * Classe que fa la gestió i el tractament de dades. Cadascun dels elements "SENSORS" o "MOBILESENSOR"
 *  té la seva URIassociada amb la que s'accedeix des dels altres components de la app(per exemple quan es 
 *  fa el treatdata des de qualsevol ServerHttpObject.
 */
public class SensorsProvider extends ContentProvider {

	private static final String SENSORS_URI = "content://com.sanchez.sensapp/sensors"; 
	public static final Uri URI_SENSORS = Uri.parse(SENSORS_URI);	//s'encapsula en un objecte de tipus Uri 
	private static final String MOBILESENSOR_URI = "content://com.sanchez.sensapp/mobilesensor"; 
	public static final Uri URI_MOBILESENSOR = Uri.parse(MOBILESENSOR_URI);	//s'encapsula en un objecte de tipus Uri 

	//Creo 4 constants, amb diferents valors segons si em refereixo a tota la taula o a un sensor concret
	private static final int ALL_SENSORS = 1; 
	private static final int SINGLE_SENSOR = 2;
	private static final int ALL_SENSOR_MOBILE = 3; 
	private static final int SINGLE_SENSOR_MOBILE = 4;
	
	/**  un objecte de la classe UriMatcher permet interpretar patrons en una URI. Sabré si la URI
	 *  fa referencia a una tabla genèrica o a un registre concret a través de la seva ID.
	 *   Inicialitzarem l'objecte UriMatcher dient-li el format dels 4 tipus de URI, de forma que
	 *   pugui diferenciar-los i tornar-nos el seu tipus (una de les 4 constants creades). Amb el 
	 *   addURI indiquem la authority de la nostra URI, el format de la entitat que demanem, i el tipus 
	 * 	amb el que volem identificar aquest format. Quan la cridi em tornarà si es 1 2 3 o 4**/
	
	private static final UriMatcher mUriMatcher; 
	static{
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI("com.sanchez.sensapp", "sensors", ALL_SENSORS); 
		mUriMatcher.addURI("com.sanchez.sensapp", "sensors/#", SINGLE_SENSOR); 
		mUriMatcher.addURI("com.sanchez.sensapp", "mobilesensor", ALL_SENSOR_MOBILE); 
		mUriMatcher.addURI("com.sanchez.sensapp", "mobilesensor/#", SINGLE_SENSOR_MOBILE); 		
	}



	
	private SQLiteDatabase myDatabase;//no té constructor pq tu no el crides, el crida el sistema
	/** a l'oncreate: inicialitzar la nostra base de dades, a través del seu nom i versió, pel qual utilitzem 
	* la classe Helper creada i a través del context de Helper 
	*/
	@Override
	public boolean onCreate() { //s'inicializa el CP al arranque 
		Context context = getContext();
		Helper dbHelper = Helper.getInstance(context);
		myDatabase = dbHelper.getWritableDatabase(); //AQUI ES DONDE SE SUPONE QUE NO DEBO LLAMARLA????????
		return (myDatabase == null) ? false : true;  //si content provider no va, em torna false; si funciona, torna true, i continuo
	}
	
	/**
	 * mètode query rep com a params:una URI, una llista de noms de columna, un criteri de selecció,
	 * una llista de valors per les variables utilitzadas en el criteri anterior, i un criteri d'ordenació
	 * Retorna un cursor
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch (mUriMatcher.match(uri)){
		//faig match amb la uri que li passo al query amb el meu objecte mUriMatcher definit més a dalt
		case SINGLE_SENSOR: //si el tipus tornat és SINGLE,substitueixo el criteri de selecció 
			qb.setTables(Helper.DB_TABLE_SENSORS); 
			//ensenyam,de la taula de sensors, només la que tingui la id de la uri q minteressa
			qb.appendWhere(Helper.Sensors._ID + " = " + uri.getPathSegments().get(1)); 
			break;
		case ALL_SENSORS: //tb pq podem CONSULTAR mes dun
			qb.setTables(Helper.DB_TABLE_SENSORS); //ensenyam la taula sensors
			break;
		case ALL_SENSOR_MOBILE:
			qb.setTables(Helper.DB_TABLE_MOBILESENSOR); 
			break;
		case SINGLE_SENSOR_MOBILE: 
			qb.setTables(Helper.DB_TABLE_MOBILESENSOR); 
			qb.appendWhere(Helper.Sensors._ID + " = " + uri.getPathSegments().get(1));
		}
		Cursor cursor = qb.query(myDatabase, projection, selection, selectionArgs, null, null, sortOrder);
		//AQUINOPONER????????cursor.movetoFirst(); ?????????????????
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		//dins d'aquest contexte, donam el content resolver?????????????, i fesme notificacio quan hagi canvis a la uri
		return cursor;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String taula = "";
		String where = selection;

		switch (mUriMatcher.match(uri)){
		case SINGLE_SENSOR:
			taula = Helper.DB_TABLE_SENSORS; 
			where += Helper.Sensors._ID + " = " + uri.getPathSegments().get(1);
		break;
		case ALL_SENSORS:
			taula = Helper.DB_TABLE_SENSORS;
		break;
		case SINGLE_SENSOR_MOBILE:
			taula = Helper.DB_TABLE_MOBILESENSOR; 
			where += Helper.Sensors._ID + " = " + uri.getPathSegments().get(1);
		break;
		case ALL_SENSOR_MOBILE:
			taula = Helper.DB_TABLE_MOBILESENSOR;
		}
		//borra dins de la taula, el registre q vull
		return myDatabase.delete(taula, where, selectionArgs);
	}

	
	/**
	 *identificar el tipus de dades que retorna el content provider. Aquest tipus de dades s'expresarà
	 * com un MIME Type, per a determinar el tipus de dades que estan rebent després d'una petició al
	 *  servidor. Això ajudarà a determinar quines aplicacions son capaces de processar aquestes dades.
	 */
	@Override
	public String getType(Uri uri) {
		switch (mUriMatcher.match(uri)){
		case ALL_SENSORS:
			return "vnd.android.cursor.dir/vnd.com.sanchez.sensapp.sensors"; 
		case SINGLE_SENSOR:
			return "vnd.android.cursor.item/vnd.com.sanchez.sensapp.sensors";
		case ALL_SENSOR_MOBILE:
			return "vnd.android.cursor.dir/vnd.com.sanchez.sensapp.mobilesensor";
		case SINGLE_SENSOR_MOBILE:
			return "vnd.android.cursor.item/vnd.com.sanchez.sensapp.mobilesensor"; 
		}
		throw new UnsupportedOperationException("Not yet implemented");
	}

	
	@Override
	public Uri insert(Uri uri, ContentValues values) { //CV:Parells clau/valor
		String nomtable ="";
		Log.e("insert ","insert "+uri );
		switch (mUriMatcher.match(uri)){
		case SINGLE_SENSOR: //nomes single pq insereixo UN
			nomtable = Helper.DB_TABLE_SENSORS;
			break;
		case SINGLE_SENSOR_MOBILE:
			nomtable = Helper.DB_TABLE_MOBILESENSOR;
			break;
		}
		//inserta els valors values a la taula nomtable i em retorna la id del registre, la qual guardo sota el nom idreg
		long idreg = myDatabase.insert(nomtable, null, values); 
		// genero una nova uri per al nou registre que he afegit
		Uri newUri = null;
		switch (mUriMatcher.match(uri)){
		case SINGLE_SENSOR:
			newUri = ContentUris.withAppendedId(URI_SENSORS, idreg);
			break;
		case SINGLE_SENSOR_MOBILE:
			newUri = ContentUris.withAppendedId(URI_MOBILESENSOR, idreg);
			break;
		}
		return newUri;
	}
	

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String taula = "";
		String where = selection == null ? "" :selection;
		
		switch (mUriMatcher.match(uri)){
		case SINGLE_SENSOR:
			taula = Helper.DB_TABLE_SENSORS; 
			where += Helper.Sensors._ID + " = " + uri.getPathSegments().get(1);
			break;
		case SINGLE_SENSOR_MOBILE:
			taula = Helper.DB_TABLE_MOBILESENSOR; 
			where += Helper.Sensors._ID + " = " + uri.getPathSegments().get(1);
			break;
		}
		return myDatabase.update(taula, values, where, selectionArgs);
	}
	
}
