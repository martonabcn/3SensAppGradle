package com.sanchez.sensapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sanchez.sensapp.R;
import com.sanchez.sensapp.fragments.BrowserFragment;
import com.sanchez.sensapp.fragments.InfoSensorsFragment;
import com.sanchez.sensapp.fragments.InfoUserFragment;
import com.sanchez.sensapp.fragments.MapFragment;

public class MainActivity extends SherlockFragmentActivity {

	private Cursor cursor;
	private SimpleCursorAdapter adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int iduser = getIntent().getIntExtra("IDUSUARIO", -1);


//ACTIONBAR i FRAGMENTS
      //Obtenemos una referencia a la actionbar
      		ActionBar actBar = getSupportActionBar();
      		//Establecemos el modo de navegacion por pestanyas
      		actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

      		//Creamos las pestayas
      		ActionBar.Tab tabuser =	actBar.newTab().setText(getString(R.string.frag_user));
      		ActionBar.Tab tabsensors = actBar.newTab().setText(getString(R.string.frag_sensors));
      		ActionBar.Tab tabmap = actBar.newTab().setText(getString(R.string.frag_map));

      		//Asociamos los listener a las pestanyas
      		tabuser.setTabListener(new MyTabListener());
      		tabsensors.setTabListener(new MyTabListener());
      		tabmap.setTabListener(new MyTabListener());
      		

      		//Anyadimos las pestanyas a la action bar
      		actBar.addTab(tabuser);
      		actBar.addTab(tabsensors);
      		actBar.addTab(tabmap);
      		
      		
      		
          		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	      		ActionBar.Tab tabweb = actBar.newTab().setText(getString(R.string.frag_browser));
	      		tabweb.setTabListener(new MyTabListener());
	      		actBar.addTab(tabweb);
      		}

    } 

    public class MyTabListener implements TabListener {
        Fragment fragment = null;

    	@Override
    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
            //Creamos los fragments de cada pestanya cuando se clickan

    		if (ft != null) {
                if(tab.getText().equals(getString(R.string.frag_user))){
                    fragment = new InfoUserFragment();
                }else if(tab.getText().equals(getString(R.string.frag_sensors))){
                    fragment = new InfoSensorsFragment();
                }else if(tab.getText().equals(getString(R.string.frag_map))){
                    fragment = new MapFragment();
                }else if(tab.getText().equals(getString(R.string.frag_browser))){
                    fragment = new BrowserFragment();
                }
                ft.replace(R.id.contenidor, fragment); //contenidor es el id del layout xml del activity_main
            }
        }	

    	@Override
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (ft != null) {
                ft.remove(fragment);
            }
    	}
    	
    	@Override
    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
            if (ft != null) {
                if(tab.getText().equals(getString(R.string.frag_user))){
                    fragment = new InfoUserFragment();
                }else if(tab.getText().equals(getString(R.string.frag_sensors))){
                    fragment = new InfoSensorsFragment();
                }else if(tab.getText().equals(getString(R.string.frag_map))){
                    fragment = new MapFragment();
                }else if(tab.getText().equals(getString(R.string.frag_browser))){
                    fragment = new BrowserFragment();
                }
                ft.replace(R.id.contenidor, fragment); //contenedor es el id del layout xml del activity_main
            }
    	}
    /**
    }   
    //LOADERS
    private class DatabaseCursorLoaderCallback implements LoaderCallbacks<Cursor>{

		@Override
		public android.support.v4.content.Loader<Cursor> onCreateLoader( //inicialitza els loaders, de moment en tinc 1
				int arg0, Bundle arg1) {
		
			CursorLoader cl = new CursorLoader(getApplication(), PatologiaProvider.URI_USER,
					new String[]{User._ID,User.KEY_NAME,User.KEY_SURNAME,User.KEY_AGE,User.KEY_ADDRESS,User.KEY_PHONE, User.KEY_EMAIL,User.KEY_NOTES},
					null, null, null);

			return cl;
		}

		@Override
		public void onLoadFinished( //quan sha acabat la lectura de la BBDD
				android.support.v4.content.Loader<Cursor> arg0, Cursor arg1) { //em retorna un cursor
			adapter.swapCursor(arg1);//actualitza el cursor del adapter, fa q ladapter es mostri en pantalla
		
		}
		@Override
		public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) { //quan torna a comen�ar
		//elimino el cursor antic
		adapter.swapCursor(null);
		}
		**/
    }

  
}
