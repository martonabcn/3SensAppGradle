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

import com.sanchez.sensapp.http.interficie.ServerFunction;

public class Acceshttp {
//com no hereda de ningu, li dono el contexte:
	private Context context;
	private static final String myurl = "http://158.109.64.44/3sense";
	
	//constructor de la classe li passo el contexte, li ve del service SyncService // TODO SEGURRRRR???????
	public Acceshttp (Context cont){
		this.context = cont;
	}
	
	//creo funcio per cridar al server
	public void CallServer (ServerFunction serverobject,int type){
		HttpClient httpclient = new DefaultHttpClient(); //creo un objecte HttpClient: demana peticions al server
		HttpRequestBase base = null;  
		HttpPost httppost = null;
		HttpGet httpget = null;
		
		switch (type) {
		case 1: // GET amb l'adreça completa: concatena l'inici amb el final que corrspon a l'objecte ServerHttpXXX que m'hagi cridat el CallServer
			httpget =new HttpGet(serverobject.getURL(myurl)); 
			base = httpget; 
			 break;
		case 2: //POST
			httppost = new HttpPost(serverobject.getURL(myurl));			
			try { 
				//le meto como pares url-encodados la pareja de nombre/valor (id/2 o  usuario/Alberto pass/Moral) 
				//que devuelve mi método getParams(), definido en la interficie ServerFunction y implementado en cada uno de las clases ServerHttpXXX
				UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(serverobject.getParams());
				httppost.setEntity(postEntity);
				base = httppost;  
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block ??????????????????????????
				e1.printStackTrace();
			}
			
		default:			
			break;
		}
		
		try {
			HttpResponse response = httpclient.execute(base);
			HttpEntity entity = response.getEntity();
			//Entity es la informacio q prove del server, en el format establert
			String s = EntityUtils.toString(entity);
			//La transformo en string i se la passo al metode treatData del objecte ServerHttpXXX que m'hagi cridat el CallServer
			serverobject.treatData(context, s);
			
		} catch (ClientProtocolException e2) {
			// TODO Auto-generated catch block ???????????????????
			e2.printStackTrace();
		} catch (IOException e3) {
			// TODO Auto-generated catch block ?????????????????
			e3.printStackTrace();
		}
		
		
	}
}
