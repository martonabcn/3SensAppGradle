package com.sanchez.sensapp.basedades;

import com.sanchez.sensapp.basedades.Helper.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Clase que introduce los datos que vienen del servidor a la BD creada por el Helper
 */
public class BBDD {

	private SQLiteDatabase mDatabase;
	Helper helper;
	
	public BBDD(Context c){
		helper = Helper.getInstance(c);
	}
	public void open(){
		mDatabase = helper.getWritableDatabase();
	}
	public void close(){
		mDatabase.close();
		mDatabase = null; 
	}
	public boolean isClosed(){
		return mDatabase == null? false : true;
	}
	
	//metode per recuperar la informacio del usuari
	public Cursor getUser(){
		String[] columns = new String []{User.KEY_ID, User.KEY_NAME,User.KEY_SURNAME ,User.KEY_AGE, User.KEY_ADDRESS,
				User.KEY_PHONE,User.KEY_EMAIL, User.KEY_NOTES};
		return mDatabase.query(Helper.DB_TABLE_USER, columns, null, null, null, null, null);
	}
	//metode per inserir la informacio del usuari al lloc corresponent de la BD
	public void setUser(String[] params){
        Log.e("aaaa", params.toString());
		//Alberto<br>Moral<br>25<br>Paseo Maragall<br>123421982<br>123823238<br>alberto@3sens.es<br>Barcelona<br>nsakjna askdjndsafkjdf sakjflabdnsklfb sadasdkfj nasdkjlfn asf nasdlkn adslfnakñ nasdkjf andfkj a
		ContentValues values = new ContentValues();
		values.put(User.KEY_NAME, params[0]);
		values.put(User.KEY_SURNAME, params[1]);
		values.put(User.KEY_AGE, params[2]);
		values.put(User.KEY_ADDRESS, params[3] + " , " + params[7]); //guardo a la mateixa columna adresa i poblacio
		values.put(User.KEY_PHONE, params[4]);
		values.put(User.KEY_EMAIL, params[6]);
		values.put(User.KEY_NOTES, params[8]);
		mDatabase.delete(Helper.DB_TABLE_USER, null, null);
		long n = mDatabase.insert(Helper.DB_TABLE_USER, null, values);
		System.out.println(n);
	}
	
	
	
}
