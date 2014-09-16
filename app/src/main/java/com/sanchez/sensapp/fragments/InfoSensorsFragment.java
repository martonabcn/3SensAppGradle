package com.sanchez.sensapp.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanchez.sensapp.R;
import com.sanchez.sensapp.basedades.Helper;
import com.sanchez.sensapp.basedades.Helper.Sensors;
import com.sanchez.sensapp.basedades.SensorsProvider;

public class InfoSensorsFragment extends Fragment {
	private Cursor cursor;
	private SensorAdapter adapter;
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		//li passo la id de larxiu de vista al inflater .retorna una view i lanomeno v.
		View v = inflater.inflate(R.layout.fragment_infosensors, container, false);
        return v;
    }
	
	private LoaderManager.LoaderCallbacks<Cursor> callback;
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListView lista =(ListView)getActivity().findViewById(R.id.listSensors);
		Cursor c = null;
		adapter = new SensorAdapter(getActivity(), c);
		lista.setAdapter(adapter);
		
		//cursorloader
		callback = new	miCursorLoaderCallback();
		getActivity().getSupportLoaderManager().initLoader(0, null, callback);
		//a cada loader li dono una id diferente, pq cada fragment te un loader propi, i es diferencien per aquesta id
		//el loader de iNFOsENSORS sera id 0
	}
	
	private class miCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{

		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			String[] columnes = new String []{Sensors.KEY_ID,Sensors.KEY_Name,Sensors.KEY_BATERIA,Sensors.KEY_GAS,Sensors.KEY_AGUA,Sensors.KEY_LUZ,Sensors.KEY_TEMPERATURA,Sensors.KEY_POSICIO};
			return new CursorLoader(getActivity(), SensorsProvider.URI_SENSORS,
					columnes, null, null, null);	
		
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
			adapter.swapCursor(arg1);
			cursor = arg1;
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			adapter.swapCursor(null);
			cursor = null;

		}

	}
	
	
	
	private class SensorAdapter extends CursorAdapter{
		private LayoutInflater mInflater;
		public SensorAdapter(Context context, Cursor c) {
			super(context, c,true);
			mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//inflo el xml amb una fucnio q crida a cada cop per crear una view de cada item
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			//dona les dades a la part view (grafica)
			ViewHolder holder;
			if(arg0.getTag().equals(null)){
				holder = new ViewHolder();
				holder.bateria =(ImageView) arg0.findViewById(R.id.bateria5);
				holder.cuantogas =(TextView) arg0.findViewById(R.id.cuantogas);
				holder.cuantatemperatura =(TextView) arg0.findViewById(R.id.temperatura);
				holder.cuantaagua =(TextView) arg0.findViewById(R.id.agua);
				holder.cuantaluz =(TextView) arg0.findViewById(R.id.luz);
				holder.numerosensor =(TextView) arg0.findViewById(R.id.numerosensor);
				holder.porcentajebateria =(TextView) arg0.findViewById(R.id.porcentaje_bateria);
				holder.checkgas =(ImageView) arg0.findViewById(R.id.checkgas);
				holder.checkagua =(ImageView) arg0.findViewById(R.id.checkagua);
				arg0.setTag(holder);
			}else{
				holder =(ViewHolder) arg0.getTag();
			}
			

			
			String percentatge_bateria = arg2.getString(arg2.getColumnIndex(Helper.Sensors.KEY_BATERIA));
			String gasok = arg2.getString(arg2.getColumnIndex(Helper.Sensors.KEY_GAS));
			String cantidadluz = arg2.getString(arg2.getColumnIndex(Helper.Sensors.KEY_LUZ));
			String cantidadtemperatura = arg2.getString(arg2.getColumnIndex(Helper.Sensors.KEY_TEMPERATURA));
			String aguaok = arg2.getString(arg2.getColumnIndex(Helper.Sensors.KEY_AGUA));
			String sensornumero = arg2.getString(arg2.getColumnIndex(Helper.Sensors.KEY_Name));
			
			
			//dedes get of cursor
			holder.porcentajebateria.setText(percentatge_bateria + " %");

			int perbat = Integer.parseInt(percentatge_bateria);
			int imagebateria = R.drawable.bateria5;
			
			Double n = (double)perbat/25;
			int p = n.intValue(); //agafo nomes la part sencera, abans dels decimals
			switch (p) {
			case 0:
				imagebateria=R.drawable.bateria1;
				break;
			case 1:
				imagebateria=R.drawable.bateria2;
				break;
			case 2:
				imagebateria=R.drawable.bateria3;
				break;
			case 3:
				imagebateria=R.drawable.bateria4;
				break;
				
			default:
				break;
			}
			
			holder.bateria.setImageDrawable(getResources().getDrawable(imagebateria));
			
			int imagecheckgas = R.drawable.ok;
			int imagecheckagua = R.drawable.ok;

			if(!Boolean.parseBoolean(gasok)){
				imagecheckgas=R.drawable.wrong;
			}
			holder.checkgas.setImageDrawable(getResources().getDrawable(imagecheckgas));
			if(!Boolean.parseBoolean(aguaok)){
				imagecheckagua=R.drawable.wrong;
			}
			holder.checkagua.setImageDrawable(getResources().getDrawable(imagecheckagua));
		
			holder.cuantaagua.setText(aguaok);
			holder.cuantogas.setText(gasok);
			holder.cuantaluz.setText(cantidadluz);
			holder.cuantatemperatura.setText(cantidadtemperatura);
			holder.numerosensor.setText(sensornumero);
			
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			//com ser� la vista?
			View view = mInflater.inflate(R.layout.item_sensors, arg2,false); 
			
			return view;
		}
		
		private class ViewHolder{ //encapsulo els objectes visuals. nomes calcula un cop. aixi no haig de sobreposar imatger
			ImageView bateria;
			TextView cuantogas;
			TextView cuantatemperatura;
			TextView cuantaagua;
			TextView cuantaluz;
			TextView numerosensor;
			TextView porcentajebateria;
			ImageView checkgas ;
			ImageView checkagua ;
		}
		
	}
	

}

