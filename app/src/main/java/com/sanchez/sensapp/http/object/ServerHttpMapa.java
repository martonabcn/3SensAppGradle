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

import com.sanchez.sensapp.basedades.Helper.Sensors;
import com.sanchez.sensapp.basedades.SensorsProvider;
import com.sanchez.sensapp.http.Acceshttp;
import com.sanchez.sensapp.http.interficie.ServerFunction;

public class ServerHttpMapa implements ServerFunction{
	private final String url = "/Iphone_sensorXY.php";
	
	private String iduser;
	public ServerHttpMapa(String iduser) {
		this.iduser = iduser;
	}

	@Override
	public String getURL(String url) {
		return url+this.url;
	}

	/**
	 * mètode que fa el tractament de les dades que obté del php del servidor
	 * Aquest php torna la informació  dels sensors en la forma: 
	 * NumeroMaximDeSensors <br>Sensor1-posicion/Sensor2-posicion/Sensor3-posicion/
	 * 
	 */
	@Override
	public void treatData(Context context, String data) {

		if(!data.equals("<br>")){ //si nomes es br, no fa res
			String[] itemSensor = data.split("<br>"); //coloca a posicio0 del string el nummaxsensors i a la posicio1 la resta de info
			ContentResolver cr =context.getContentResolver();
			for(String item : itemSensor[1].split("/")){ //Em fa un item de cada sensor
				String[] datosCol = item.split("-"); // i em separa per columnes, a la 0 anira la ID i a la 1 la posicio
				
				/**com el SensorProvider em dona una unica o totes les sensors.Per un insert, no em cal la id,
				 *  pq me la crea la BBDD. Poso un element pq es un unic element
				 *  Un insert agafa automaticament la ultima posicio disponible, pero a un update li has de dir
				 *  primer faig la query: Consulta SQL: donam els registres de la columna name on el contingut 
				 *  sigui igual al que mha tornat elPHP Sensors.KEY_Name + " = '" + datosCol[0] + "'"
				**/
				
				String selection = (Sensors.KEY_Name + " = '" + datosCol[0] + "'"); 
				Cursor c = cr.query(SensorsProvider.URI_SENSORS, null, selection, null, null);
				//AQUI NO CAL FER C.MOVETOFIRST???????? PQQQQ????????????
				//si tinc la BBDD buida, haig de inserir el sensor
				if (c.getCount()==0){
					//inserta : ha de ser a posicio 0
					Uri entityUri = Uri.withAppendedPath(SensorsProvider.URI_SENSORS, 0+"");
					ContentValues values = new ContentValues();
					values.put(Sensors.KEY_POSICIO, datosCol[1]);
					cr.insert(entityUri, values); //
				}
				else{
					//update :ha de ser a la posicio del cursor (a la posicio correspoenent a la ID)
					c.moveToFirst();
					int id = c.getInt(c.getColumnIndex(Sensors.KEY_ID));
					//String where2 = (Sensors.KEY_ID + " = " + id); TB es pot fer aixi en comptes de amb les URIs
					Uri entityUri = Uri.withAppendedPath(SensorsProvider.URI_SENSORS, id+"");//al final de l'adreça posam la ID  
					ContentValues values = new ContentValues();
					values.put(Sensors.KEY_POSICIO, datosCol[1]);
					cr.update(entityUri, values, null, null); 

				}
							
			}
			ServerHttpMapaBateria shmapabateria = new ServerHttpMapaBateria(iduser); //el fico aqui per a que no em cridin ala bdd tots dos alhora, sino un i despres laltre
			Acceshttp acces = new Acceshttp(context);
			acces.CallServer(shmapabateria, 2);
		}
		
	}

	@Override
	public List<NameValuePair> getParams() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);  	
		nameValuePairs.add(new BasicNameValuePair("id",iduser)); //li haig de pasar la id del usuari al PHP sota el  nom"id" 
		
		return nameValuePairs;
	}

}


