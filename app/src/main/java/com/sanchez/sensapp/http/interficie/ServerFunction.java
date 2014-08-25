package com.sanchez.sensapp.http.interficie;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
/**
 * Interficie en la que es declaren els mètodes que permeten definir l'adreça del servidor,
 * el tractament de les dades que retorna el php del servidor i passar paràmetres. 
 * S'implementen en cada classe de tipus ServerHttp
 */

public interface ServerFunction {
	public String getURL(String url);
	public void treatData(Context context, String data);
	public List<NameValuePair> getParams();
}
