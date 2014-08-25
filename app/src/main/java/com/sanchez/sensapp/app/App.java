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
				//cada quant? cada 1 minut*60segons*1000 milisegons
				1000*60*1, pendingIntent);
		
	}
	/**
	 * Mètode que comprova la connexió a internet. L'utilitzo per comprovar en el SyncService (per si puc fer el CallServer) 
	 * i en ActivityLogin(per a que em mostri un toast)
	 * AFEGIRLO AMB TOAST A CADA ACTIVITY???????????????????????
	 * @return
	 */
	public boolean verificaConexion() {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		// No només wifi, també GPRS
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		
		for (int i = 0; i < redes.length; i++) {
		// tenim connexió? posem a true
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
		}

}
