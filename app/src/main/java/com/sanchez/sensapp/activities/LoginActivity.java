package com.sanchez.sensapp.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sanchez.sensapp.R;
import com.sanchez.sensapp.app.App;
import com.sanchez.sensapp.http.Acceshttp;
import com.sanchez.sensapp.http.object.ServerHttpLogin;
/**
 * Infla la interficie d'usuari amb la pantalla de login.  
 * 
 *
 */
public class LoginActivity extends Activity {
	EditText txtUser;
	EditText txtPassword;
	Button btnLogin;
	TextView txtviewMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		 txtUser = (EditText)findViewById(R.id.edittext_user);
		 txtPassword = (EditText)findViewById(R.id.edittext_password);
		 btnLogin = (Button)findViewById(R.id.buttonlogin);
		 txtviewMessage = (TextView)findViewById(R.id.txtview_message);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//quan clico, guardo les dades que he introduit als camps Usuari i Contrasenya
				//i les comprovo 
				String userin = txtUser.getText().toString();
				String passwordin = txtPassword.getText().toString();
				if(!userin.equals("") || !passwordin.equals("")){
					MiAsynctask myasynctask = new MiAsynctask();
					if(((App)getApplication()).verificaConexion()){
						myasynctask.execute(userin, passwordin);
					}else{
						Toast.makeText(getBaseContext(), "No Internet connection", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}
		
				
				//asinctask
	private class MiAsynctask extends AsyncTask<String, Void, ServerHttpLogin> { //ejecuta en 2o plano  
												//insert, update, return
		@Override
		protected ServerHttpLogin doInBackground(String... params) {
			ServerHttpLogin mylogin = new ServerHttpLogin(); //creo un objeto pato de MI clase anteriormente
			mylogin.setUser(params[0]);
			mylogin.setPassword(params[1]);
			Acceshttp acces = new Acceshttp(getBaseContext());
			acces.CallServer(mylogin, 2); // 2 es post
			//li passo el mylogin(conte user i pass) al metode callserver, i aquest retorna la info del servido al mylogin, el mylogin ja conte la info retornada pel server
			
			return mylogin;
		}

		@Override
		protected void onPostExecute(ServerHttpLogin mylogin) {
			if (mylogin.isLogin()==true){		
				
				SharedPreferences sp =getSharedPreferences("Systema", Activity.MODE_MULTI_PROCESS);
				SharedPreferences.Editor ed =sp.edit();
				ed.putInt("IDUSUARIO", mylogin.getIDuser());
				ed.commit();
				
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("IDUSUARIO", mylogin.getIDuser());
				startActivity(intent);
			}
			else{
				setMessage("Login incorrecte");
			}
		}
		
		
	}
				
	 public void setMessage(String msg)
		{
			txtviewMessage.setText(msg);
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
