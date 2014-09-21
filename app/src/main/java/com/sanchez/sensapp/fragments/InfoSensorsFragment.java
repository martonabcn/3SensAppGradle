package com.sanchez.sensapp.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
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
	
	// Adaptador que hace de puente entre los datos que se le pasan por el cursor y las vistas del ListView
	
	private class SensorAdapter extends CursorAdapter{
		private LayoutInflater mInflater;
		public SensorAdapter(Context context, Cursor c) {
            // llamamos al constructor indicando contexto, cursor con la consulta y booleano que indica que la consulta ha de ser regenerada
			super(context, c,true);
			mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//inflo el xml amb una fucnio q crida a cada cop per crear una view de cada item
		}

        @Override
        public View newView(Context ctxto, Cursor mcursor, ViewGroup mviewgroup) {
            //Indicamos como sera la vista
            View view = mInflater.inflate(R.layout.item_sensors, mviewgroup,false);

            return view;
        }
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.

        private class ViewHolder{ //encapsulo los objetos visuals. solo calcula uns vez. asi no he de sobreponer imagenes
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
        //bindView Rellena con los datos la parte view (grafica)que se nos pasa de la llamada anterior y el cursor que apunta al elemento actual.

		@Override
		public void bindView(View myview, Context mycontext, Cursor mycursor) {

            // Creates a ViewHolder and store references to the children views we want to bind data to.
            ViewHolder holder;
            // When myview is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the myView supplied
            // by ListView is null.
			if(myview.getTag()==null){
				holder = new ViewHolder();
				holder.bateria =(ImageView) myview.findViewById(R.id.bateria5);
				holder.cuantogas =(TextView) myview.findViewById(R.id.cuantogas);
				holder.cuantatemperatura =(TextView) myview.findViewById(R.id.temperatura);
				holder.cuantaagua =(TextView) myview.findViewById(R.id.agua);
				holder.cuantaluz =(TextView) myview.findViewById(R.id.luz);
				holder.numerosensor =(TextView) myview.findViewById(R.id.numerosensor);
				holder.porcentajebateria =(TextView) myview.findViewById(R.id.porcentaje_bateria);
				holder.checkgas =(ImageView) myview.findViewById(R.id.checkgas);
				holder.checkagua =(ImageView) myview.findViewById(R.id.checkagua);
				myview.setTag(holder);
			}else{
                // Get the ViewHolder back to get fast access to the 6 TextView
                // and the 3ImageView.
				holder =(ViewHolder) myview.getTag();
			}
			

			//Cojo los datos del Helper para cada campo, y los guardo en un String
			String percentatge_bateria = mycursor.getString(mycursor.getColumnIndex(Helper.Sensors.KEY_BATERIA));
			String gasok = mycursor.getString(mycursor.getColumnIndex(Helper.Sensors.KEY_GAS));
			String cantidadluz = mycursor.getString(mycursor.getColumnIndex(Helper.Sensors.KEY_LUZ));
			String cantidadtemperatura = mycursor.getString(mycursor.getColumnIndex(Helper.Sensors.KEY_TEMPERATURA));
			String aguaok = mycursor.getString(mycursor.getColumnIndex(Helper.Sensors.KEY_AGUA));
			String sensornumero = mycursor.getString(mycursor.getColumnIndex(Helper.Sensors.KEY_Name));

            Log.e("milogbat", percentatge_bateria);
            Log.e("milogagua", aguaok);
            Log.e("miloggas", gasok);
			//A cada campo child del holder, le pongo el texto o la imagen correspondiente
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


		
	}
	

}

