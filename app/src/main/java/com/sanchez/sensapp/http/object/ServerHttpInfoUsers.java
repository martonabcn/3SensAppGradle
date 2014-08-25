package com.sanchez.sensapp.http.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.sanchez.sensapp.basedades.BBDD;
import com.sanchez.sensapp.http.Acceshttp;
import com.sanchez.sensapp.http.interficie.ServerFunction;

public class ServerHttpInfoUsers implements ServerFunction{
	private final String url = "/Iphone_datosUser.php";
	
	
	private String iduser;
	public ServerHttpInfoUsers(String iduser) {
		this.iduser = iduser;
	}

	//m�tode que afegeix la �ltima part del string a la adre�a web i la retorna sencera
	@Override
	public String getURL(String url) {
		String address = (url+ this.url);
		return address;
	}

	
	/**
	 * m�tode que fa el tractament de les dades que obt� del php del servidor
	 * Aquest php torna la informaci�  del usuari separada per <br>
	 */
	@Override
	public void treatData(Context context, String data) {
		if(!data.equals("")){ //si no hi ha dades no fa res, si les t�, entra
			BBDD bd = new BBDD(context); //creo una Base de Dades i li passo el contexte
			bd.open();
            Log.e("aaaa",data);
            bd.setUser(data.split("<br>")); //SET USER???????
			bd.close();
		}
		
		/**
		 * Una vegada ?????? creo un objecte de la classe ServerHttpInfoSensors, que em permetr�
		 *  connectar amb el servidor. 
		 * Se situa aqu� per a que no ataquin al servidor tots dos alhora, sino primer un i despres laltre
		 */
		ServerHttpInfoSensors infosensors = new ServerHttpInfoSensors(iduser); 
		Acceshttp acces = new Acceshttp(context);
		acces.CallServer(infosensors, 2); //crido al server amb aquest objecte, i el 2 es POST
	}

	
	
	@Override
	public List<NameValuePair> getParams() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);  	
		nameValuePairs.add(new BasicNameValuePair("id", iduser)); //li haig de pasar la id del usuari al PHP sota el  nom"id" 
		return nameValuePairs;
	}

}
