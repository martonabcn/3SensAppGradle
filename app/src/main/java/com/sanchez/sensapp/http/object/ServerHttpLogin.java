package com.sanchez.sensapp.http.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.sanchez.sensapp.http.interficie.ServerFunction;

public class ServerHttpLogin implements ServerFunction {
	private final String url = "/Iphone_login.php";

	
	//metodo que concatena la pagina del php de login con el resto de la direccion url
	@Override
	public String getURL(String urlBase) {
		String address = (urlBase + this.url);
		return address;
	}

	
	//metodos para recuperar las variables que provienen del servidor
	private int IDuser;
	public int getIDuser() {
		return IDuser;
	}

	private Boolean islogin;
	public Boolean isLogin() {		
		return islogin;
	}

	/**
	 * metode que fa el tractament de les dades que obte del php del servidor
	 * les dades em venen separades per <br>. en primera posicio obtinc 0 si el login es incorrecte, i 1 si es correcte.
	 * en la segona posicio obtinc la ID del usuari
	 */

	@Override
	public void treatData(Context context, String data) { //TODO ve de la Classe Acceshttp a la comanda function.treatData(context, s);
		String[] d = data.split("<br>"); //separo les dades per <br>
		if(d[0].equals("1")){ //si login es correcte
			islogin=true; //guardo el valor true a variable islogin
			IDuser = Integer.parseInt(d[1]); // guardo com a Integer a la variable IDuser el numero de ID de usuari que em torna el php
		}
		else{
			islogin=false; //si login es incorrecte la variable islogin valdra false
		}
	}
		
	
	private String user; 
	//faig gets i setters
		public String getUser() { //OI QUE??????????????? sera utilitzada a l'activitat de login per guardar el user introduit per l'usuari
			return user;
		}
		public void setUser(String user) {
			this.user = user; //ho fico a la variable user abans definida.
		}

		
		private String password;
	//faig gets i setters
		public String getPassword() {//sera utilitzada a l'activitat de login per guardar el password introduit per l'usuari
			return password;
		}
		public void setPassword(String password) {

            this.password = password;
		}
		

/**metode que crea una llista de noms+valors i associa el nom de la variable que demana el php amb el
 * valor que sera l'introduit per l'usuari a la pantalla de login
 */
	@Override
	public List<NameValuePair> getParams() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2); 
		nameValuePairs.add(new BasicNameValuePair("usuario", getUser())); //"usuario" i "pass" es el nom que demana el php
		nameValuePairs.add(new BasicNameValuePair("pass", getPassword()));
		
		return nameValuePairs;
	}

}
