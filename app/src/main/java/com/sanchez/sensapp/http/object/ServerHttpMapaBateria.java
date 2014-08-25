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

import com.sanchez.sensapp.basedades.SensorsProvider;
import com.sanchez.sensapp.basedades.Helper.Sensors;
import com.sanchez.sensapp.http.Acceshttp;
import com.sanchez.sensapp.http.interficie.ServerFunction;

public class ServerHttpMapaBateria implements ServerFunction{
	private final String url = "/Iphone_datosSensorUser.php";
	//cuando le pasamos la id del usuario, este php devuelve la bateria del sensor móvil
	
	private String iduser;
	public ServerHttpMapaBateria(String iduser) {
		this.iduser = iduser;
	}

	@Override
	public String getURL(String url) {
		return url+this.url;
	}

	@Override
	public void treatData(Context context, String data) {
		if(!data.equals("")){ //si no hi ha res no fa res
			ContentResolver cr =context.getContentResolver();

			//miro si hiha algo en aquesta columna, per,sino hi ha res, posar-lo
			Cursor c = cr.query(SensorsProvider.URI_MOBILESENSOR, null, null, null, null);
			if (c.getCount()==0){
				//inserta
				Uri entityUri = Uri.withAppendedPath(SensorsProvider.URI_MOBILESENSOR, 0+"");
				ContentValues values = new ContentValues();
				values.put(Sensors.KEY_BATERIA, data);
				cr.insert(entityUri, values);
			}
			else{
				//update
				c.moveToFirst();
				int id = c.getInt(c.getColumnIndex(Sensors.KEY_ID));
				Uri entityUri = Uri.withAppendedPath(SensorsProvider.URI_MOBILESENSOR, id+"");//al final de ladresa posam la ID  
				ContentValues values = new ContentValues();
				values.put(Sensors.KEY_BATERIA, data);
				cr.update(entityUri, values, null, null);
			}
			}
		ServerHttpMapaPosicio shmapapos = new ServerHttpMapaPosicio(iduser); //el fico aqui per a que no em cridin ala bdd tots dos alhora, sino un i despres laltre
		Acceshttp acces = new Acceshttp(context);
		acces.CallServer(shmapapos, 2);
	}

	@Override
	public List<NameValuePair> getParams() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);  	
		nameValuePairs.add(new BasicNameValuePair("id",iduser)); //li haig de pasar la id del usuari al PHP sota el  nom"id" 
		
		return nameValuePairs;
	}

}
