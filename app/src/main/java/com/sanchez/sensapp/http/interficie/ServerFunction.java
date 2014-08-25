package com.sanchez.sensapp.http.interficie;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
/**
 * Interficie en la que es declaren els m�todes que permeten definir l'adre�a del servidor,
 * el tractament de les dades que retorna el php del servidor i passar par�metres. 
 * S'implementen en cada classe de tipus ServerHttp
 */

public interface ServerFunction {
	public String getURL(String url);
	public void treatData(Context context, String data);
	public List<NameValuePair> getParams();
}
