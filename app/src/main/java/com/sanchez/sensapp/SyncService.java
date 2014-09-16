package com.sanchez.sensapp;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.sanchez.sensapp.app.App;
import com.sanchez.sensapp.http.Acceshttp;
import com.sanchez.sensapp.http.object.ServerHttpInfoUsers;

public class SyncService extends Service {
		
	/**
	 @Override
	public IBinder onBind(Intent intent) {
		 TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
		return mBinder;
	}
	 */

	//ASI????????????????????????????????????????
	IBinder mBinder = new LocalBinder();

 	@Override
 	public IBinder onBind(Intent intent) {
  	return mBinder;
 	}
	
 	public class LocalBinder extends Binder {
 		public SyncService getServerInstance() {
 			return SyncService.this;
 		}
 	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		SharedPreferences sp = getSharedPreferences("Systema", Activity.MODE_PRIVATE);
		int iduser = sp.getInt("IDUSUARIO", 0); //donam la IDdel user, i un 0 si no existeix la preferencia
		if(((App)getApplication()).verificaConexion()){
			new MyAsynctask().execute(iduser+"");
		}
		return Service.START_NOT_STICKY; //no es torna a executar el servei si s'atura
	}
	
	private class MyAsynctask extends AsyncTask<String, Void, Void> { //executa en 2n pla

		@Override
		protected Void doInBackground(String... params) {
			//li passo la IDUSER que em torna al fer Login i que es trobava als contexts
			String iduser = params[0];
			//Creo un objecte httpinfousers i li passo la iduser
			ServerHttpInfoUsers infousers = new ServerHttpInfoUsers(iduser);	
			Acceshttp acces = new Acceshttp(getBaseContext());
			//crido al servidor amb l'objecte infousers i fent un post ( 1 es get, 2 es post)
			acces.CallServer(infousers, 2); 
			// TODO: TANCAR APP ???????? ho tinc apuntat aixi als fulls.....
			return null;
		}
	}
	
	
	
}
