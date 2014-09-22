package com.sanchez.sensapp.http.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sanchez.sensapp.basedades.Helper;
import com.sanchez.sensapp.basedades.SensorsProvider;
import com.sanchez.sensapp.http.Acceshttp;
import com.sanchez.sensapp.http.interficie.ServerFunction;

public class ServerHttpInfoSensors implements ServerFunction{
	private final String url = "/Iphone_datosSensores.php";


    //metodo que concatena la pagina del php de datos sensores con el resto de la direccion url
	@Override
	public String getURL(String url) {
		return url+this.url;
	}
	
	private String iduser;
	public ServerHttpInfoSensors(String iduser) {
		this.iduser = iduser;
	}
	
	/**
	 * metode que fa el tractament de les dades que obte del php del servidor
	 * Aquest php torna la informacio  dels sensors en la forma:
	 * NumeroMaximDeSensors <br>Sensor1-43-328.0-7.409149-False-False/Sensor2-......
	 * 
	 */
	@Override
	public void treatData(Context context, String data) {
		
		if(!data.equals("<br>")){ //si nomes es br, no fa res
			String[] itemSensor = data.split("<br>"); // separo les dades. En la posicio 0 em queda el nummaxdesensors, en la 1 la resta de informacio
			
			ContentResolver cr = context.getContentResolver(); 
			Cursor c = cr.query(SensorsProvider.URI_SENSORS, null, null, null, null); //pregunto si ja hi ha algo a la Base de Dades interna ???????????????
		//	cursor.moveToFirst(); NO HI ERA. HAURIA D'ESTAR???????		
			if(c.getCount() == 0){ //si no hi ha res, (primera vegada que accedeixo)l'haure de crear
				
				for(String item : itemSensor[1].split("/")){ //fesme un item de cada sensor, separat del seguent per /
					String[] dCol = item.split("-"); //coloca a una posicio del string cada dada del sensor
					Uri entityUri = Uri.withAppendedPath(SensorsProvider.URI_SENSORS, 0+"");
					//Afegeixo un 0 ala adresa de memoria interna
					ContentValues values = new ContentValues();
					values.put(Helper.Sensors.KEY_Name, dCol[0]); //coloca cada dada al camp corresponent CR
					values.put(Helper.Sensors.KEY_BATERIA, dCol[1]);
					values.put(Helper.Sensors.KEY_LUZ, dCol[2]);
					values.put(Helper.Sensors.KEY_TEMPERATURA, dCol[3]);
					values.put(Helper.Sensors.KEY_AGUA, dCol[4]);
					values.put(Helper.Sensors.KEY_GAS, dCol[5]);
					cr.insert(entityUri, values); //inserta la Uri i les dades al CR
				}
				
			}else{ //si ja existeixen dades inserides a la BD interna, situara la info a la posicio de memoria del sensor corresponent
				for(String item : itemSensor[1].split("/")){ 
					String[] dCol = item.split("-"); 
					
					//?????????????????????????????????????????????
					String[] projection = new String[]{Helper.Sensors.KEY_ID}; //tornam la columna ID
					String selection = Helper.Sensors.KEY_Name + " = '"+dCol[0]+"'"; // de les files en que coincideixi el nom guardat i el que arriba del server
					Cursor cursor = cr.query(SensorsProvider.URI_SENSORS, projection, selection, null, null);
					cursor.moveToFirst();
					
					int idSensor = cursor.getInt(cursor.getColumnIndex(Helper.Sensors.KEY_ID)); //guardo com a sencer la columna ID
					Uri entityUri = Uri.withAppendedPath(SensorsProvider.URI_SENSORS, idSensor+""); //i l'afegeixo a la adresa URI
					ContentValues values = new ContentValues();
					values.put(Helper.Sensors.KEY_Name, dCol[0]); 
					values.put(Helper.Sensors.KEY_BATERIA, dCol[1]);
					values.put(Helper.Sensors.KEY_LUZ, dCol[2]);
					values.put(Helper.Sensors.KEY_TEMPERATURA, dCol[3]);
					values.put(Helper.Sensors.KEY_AGUA, dCol[4]);
					values.put(Helper.Sensors.KEY_GAS, dCol[5]);
					cr.update(entityUri, values,null,null); //actualitza les dades i la Uri al CR
				}
			}
		}
		/**
		 * Una vegada ?????? creo un objecte de la classe ServerHttpMapa, que em permetra
		 *  connectar amb el servidor. 
		 * Se situa aqui per a que no ataquin al servidor tots dos alhora, sino primer un i despres laltre
		 */
		
		ServerHttpMapa SHMapa = new ServerHttpMapa(iduser);
		Acceshttp acces = new Acceshttp(context);
		acces.CallServer(SHMapa, 2);
	}

	@Override
	public List<NameValuePair> getParams() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);  	
		nameValuePairs.add(new BasicNameValuePair("id",iduser)); //li haig de pasar la id del usuari al PHP sota el  nom"id" 
		
		return nameValuePairs;
	}

}
