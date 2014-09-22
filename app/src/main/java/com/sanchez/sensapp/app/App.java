package com.sanchez.sensapp.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;

import com.sanchez.sensapp.SyncService;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Intent intento = new Intent(getApplicationContext(),SyncService.class);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intento, 0);
		AlarmManager myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		myAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime(), 
				//cada 1 minuto*60seg*1000 miliseg
				1000*60*1, pendingIntent);
		
	}
	/**
	 * Metode que comprueba la conexion a internet. Lo uso para comprobar en el SyncService (por si puedo hacer un CallServer)
	 * y en ActivityLogin(para que em muestre un toast)
	 * @return
	 */
	public boolean verificaConexion() {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		// No solo wifi, tambien GPRS
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		
		for (int i = 0; i < redes.length; i++) {
		// si hay connexion ponemos a true
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
		}

}
