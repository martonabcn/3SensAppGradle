package com.sanchez.sensapp.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.widget.Toast;

import com.sanchez.sensapp.R;
import com.sanchez.sensapp.http.interficie.ServerFunction;

public class Acceshttp {
//no hereda de nadie, debo darle el contexto:
	private Context context;
    //parte de la URL comun en todos los php
	private static final String myurl = "http://158.109.64.44/3sense";
	
	//constructor de la clase, le passo el contexto, viene del SyncService //
	public Acceshttp (Context cont){
        this.context = cont;
	}

	//creo metodo para llamar al server, como parámetros un objeto tipo ServerHttpXXX y 1get/ 2post
	public void CallServer (ServerFunction serverobject,int type){
        //creo un instancia HttpClient: hacee peticions al server
		HttpClient httpclient = new DefaultHttpClient();
		HttpRequestBase base = null;  
		HttpPost httppost = null;
		HttpGet httpget = null;

		switch (type) {
		case 1: // GET con la direccion completa, segun el ServerHttpXXX que le haya pasado al Callserver
			httpget =new HttpGet(serverobject.getURL(myurl));
			base = httpget; 
			 break;
		case 2: //POST
			httppost = new HttpPost(serverobject.getURL(myurl));			
			try { 
				//le meto como pares url-encodados la pareja de nombre/valor (id/2 o  usuario/Alberto pass/Moral)
				//que devuelve mi mEtodo getParams(), definido en la interficie ServerFunction y implementado en cada uno de las clases ServerHttpXXX
				UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(serverobject.getParams());
				httppost.setEntity(postEntity);
				base = httppost;  
			} catch (UnsupportedEncodingException  e1) {
				e1.printStackTrace();
			}
			
		default:			
			break;
		}

		try {
			HttpResponse response = httpclient.execute(base);
			HttpEntity entity = response.getEntity();
			//Entity es la informacion q proviene del server, en el formato establecido
			String s = EntityUtils.toString(entity);
			//La transformo en string y se la paso al metodo treatData del objecte ServerHttpXXX que
			// haya llamado al CallServer
			serverobject.treatData(context, s);
			
		} catch (ClientProtocolException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
            e3.printStackTrace();
		}
		
		
	}
}
