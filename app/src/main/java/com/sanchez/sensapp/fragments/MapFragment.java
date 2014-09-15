package com.sanchez.sensapp.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.model.LatLng;
import com.sanchez.sensapp.R;
import com.sanchez.sensapp.basedades.BBDD;
import com.sanchez.sensapp.basedades.Helper;
import com.sanchez.sensapp.basedades.Helper.Sensors;
import com.sanchez.sensapp.basedades.SensorsProvider;

import java.io.IOException;
import java.util.ArrayList;

public class MapFragment extends SherlockFragment {
	private Cursor cursor;
    private LoaderManager.LoaderCallbacks<Cursor> callback;
    //double pixelMetroX;
    //double pixelMetroY;
    private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =  inflater.inflate(R.layout.fragment_map,null);
        return view;
	}


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //cursorloader
       // callback = new	miCursorLoaderCallback();
       /// getActivity().getSupportLoaderManager().initLoader(1, null, callback);
      //  RelativeLayout image = (RelativeLayout)getActivity().findViewById(R.id.contenedorMapa);

        //pixelMetroX=(610/(13.6*1000));
        //pixelMetroY=(408/(5.6*1000));

        //a cada loader li dono una id diferente, pq cada fragment te un loader propi, i es diferencien per aquesta id
        //aquest loader sera 15
        callback = new	miCursorLoaderCallback();
        getActivity().getSupportLoaderManager().initLoader(15, null, callback);

    }

    private class miCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
            String[] columnes = new String []{Sensors.KEY_ID,Sensors.KEY_BATERIA,Sensors.KEY_GAS,Sensors.KEY_AGUA,Sensors.KEY_LUZ,Sensors.KEY_TEMPERATURA,Sensors.KEY_POSICIO};
            return new android.support.v4.content.CursorLoader(getActivity(), SensorsProvider.URI_SENSORS,
                    columnes, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) { //arg1 es el resultat de  SensorsProvider.URI_SENSORS , es a dir el contingut de la BD
            cursor = arg1;
            positions();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            cursor = null;
        }


    }

    private void positions(){
        PosicionarSensors(cursor, R.drawable.punt_negre);
        Cursor c = getActivity().getContentResolver().query(SensorsProvider.URI_MOBILESENSOR, null, null, null, null);
        PosicionarSensors(c, R.drawable.punt);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(cursor != null) {
            positions();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        GoogleMapFragment f = new GoogleMapFragment();
        BBDD bbdd = new BBDD(getActivity());
        bbdd.open();
        Cursor c = bbdd.getUser();

        c.moveToFirst();

        String address = c.getString(c.getColumnIndex(Helper.User.KEY_ADDRESS));

        Geocoder coder = new Geocoder(getActivity());
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(address, 1);
            for(Address add : adresses){

                double longitude = add.getLongitude();
                double latitude = add.getLatitude();
                LatLng latLng = new LatLng(latitude,longitude);
                f.setGps(latLng);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        bbdd.close();
        ((SherlockFragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment,f).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
       /** if(getActivity()!=null) {
            ((SherlockFragmentActivity) getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, null)
                    .commit();
        }**/
    }

    @Override
    public void onDetach() {
        PosicionarSensors(null,R.drawable.punt_negre);
        PosicionarSensors(null,R.drawable.punt);
        super.onDetach();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void PosicionarSensors(Cursor cursor,int resource_image){

			//si el cursor es buit o nol
			if (cursor!=null){
                cursor.moveToFirst();
				RelativeLayout contenedor = (RelativeLayout)view.findViewById(R.id.contenedorMapa);
				for(int i=0;i < cursor.getCount();i++){
					cursor.moveToPosition(i);
					String position = cursor.getString(cursor.getColumnIndex(Sensors.KEY_POSICIO));
					if(position!=null){
						String[] pos =position.split(" ");

                        ImageView punt = new ImageView(getActivity());
						punt.setImageDrawable(getResources().getDrawable(resource_image));
						
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
						params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
						
						Double left = (Double.parseDouble(pos[0]) /13.6); //segons el planol, dona primer la X amplada i despres la y al�ada
						Double top = (Double.parseDouble(pos[1]) /5.6);
						
						Double altura = (dpToPx(200)-20)*top;

                        Display display = getActivity().getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;


						Double amplada = (width-20)*left;

                        Log.e("position", pos[0] + " - " + pos[1]);
                        Log.e("position", left + " - " + amplada);
                        Log.e("position", top + " - " + altura);
                        Log.e("position", "-------------------------");

						params.leftMargin =  amplada.intValue() ;
						params.topMargin = altura.intValue()-5;
						contenedor.addView(punt,i, params);
						//Haurem de posar els atributs de com posicionar X e Y
					}
				}
			
			}else{
                if(getActivity() != null) {
                    RelativeLayout contenedor = (RelativeLayout) view.findViewById(R.id.contenedorMapa);
                    contenedor.removeAllViews();
                }
			}
		}
		//aqui connectarem a BBDD i recuperarme la info dls sensors itb de ladre�a
	

}

