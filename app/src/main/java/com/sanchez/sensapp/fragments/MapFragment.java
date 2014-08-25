package com.sanchez.sensapp.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.sanchez.sensapp.R;
import com.sanchez.sensapp.basedades.Helper.Sensors;
import com.sanchez.sensapp.basedades.SensorsProvider;

public class MapFragment extends SherlockFragment {
	private Cursor cursor;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map,null);
	}

		private LoaderManager.LoaderCallbacks<Cursor> callback;
		//double pixelMetroX;
		//double pixelMetroY;
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
					
			//cursorloader
			callback = new	miCursorLoaderCallback();
			getActivity().getSupportLoaderManager().initLoader(1, null, callback);
			RelativeLayout image = (RelativeLayout)getActivity().findViewById(R.id.contenedorMapa);
			
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
				PosicionarSensors(cursor,R.drawable.punt_negre);
				Cursor c = getActivity().getContentResolver().query(SensorsProvider.URI_MOBILESENSOR, null, null, null, null);
				PosicionarSensors(c,R.drawable.punt);
			}

			@Override
			public void onLoaderReset(Loader<Cursor> arg0) {
				cursor = null;
				PosicionarSensors(null,R.drawable.punt_negre);
			}
			
			
		}
		
		private void PosicionarSensors(Cursor cursor,int resource_image){
			//si el cursor es buit o nol
			if (cursor!=null){
				RelativeLayout contenedor = (RelativeLayout)getActivity().findViewById(R.id.contenedorMapa);
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
						
						Double left = (Double.parseDouble(pos[0]) /13.6); //segons el planol, dona primer la X amplada i despres la y alçada
						Double top = (Double.parseDouble(pos[1]) /5.6);
						
						Double altura = (contenedor.getHeight()-20)*top;
						Double amplada = (contenedor.getWidth()-20)*left;
						
						params.leftMargin =  amplada.intValue() ;
						params.topMargin = altura.intValue()-5;
						contenedor.addView(punt, params);
						//Haurem de posar els atributs de com posicionar X e Y
					}
				}
			
			}else{
				RelativeLayout contenedor = (RelativeLayout)getActivity().findViewById(R.id.contenedorMapa);
				contenedor.removeAllViews();
			}
		}
		//aqui connectarem a BBDD i recuperarme la info dls sensors itb de ladreça
	

}

