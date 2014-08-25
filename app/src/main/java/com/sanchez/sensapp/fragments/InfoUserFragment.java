package com.sanchez.sensapp.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanchez.sensapp.R;
import com.sanchez.sensapp.basedades.BBDD;
import com.sanchez.sensapp.basedades.Helper;
import com.sanchez.sensapp.basedades.Helper.User;
import com.sanchez.sensapp.basedades.SensorsProvider;

public class InfoUserFragment extends Fragment {
	TextView nametv;
	TextView surnametv;
	TextView agetv;
	TextView addresstv;
	TextView phonetv;
	TextView emailtv;
	TextView notestv;
	
	
	User userdades;
	public InfoUserFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		//li passo la id de larxiu de vista al inflater .retorna una view i lanomeno v.
		View v = inflater.inflate(R.layout.fragment_infouser, container, false);
        return v;
    }
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		nametv = (TextView) getActivity().findViewById(R.id.name);
		surnametv = (TextView) getActivity().findViewById(R.id.surname);
		agetv = (TextView) getActivity().findViewById(R.id.age);
		addresstv = (TextView)getActivity().findViewById(R.id.address);
		phonetv = (TextView) getActivity().findViewById(R.id.phone);
		emailtv = (TextView) getActivity().findViewById(R.id.email);
		notestv = (TextView) getActivity().findViewById(R.id.notes);
		
		BBDD bd = new BBDD(getActivity());
		bd.open();
		Cursor c = bd.getUser();
		
		c.moveToFirst();
		if( c.getCount() != 0){
			nametv.setText("Nom: " +c.getString(c.getColumnIndex(Helper.User.KEY_NAME)));
			surnametv.setText("Cognom: " +c.getString(c.getColumnIndex(Helper.User.KEY_SURNAME)));
			agetv.setText("Edat: " +c.getString(c.getColumnIndex(Helper.User.KEY_AGE)));
			addresstv.setText("Adreça: " +c.getString(c.getColumnIndex(Helper.User.KEY_ADDRESS)));
			phonetv.setText("Telefon: " +c.getString(c.getColumnIndex(Helper.User.KEY_PHONE)));
			emailtv.setText("Email: " +c.getString(c.getColumnIndex(Helper.User.KEY_EMAIL)));
			notestv.setText("Notes: " +c.getString(c.getColumnIndex(Helper.User.KEY_NOTES)));
		}
		bd.close();
		c.close();	
		ImageView battery = (ImageView)getActivity().findViewById(R.id.bateriauser);
		Cursor cursor =getActivity().getContentResolver().query(SensorsProvider.URI_MOBILESENSOR, null, null, null, null);
		cursor.moveToFirst();
		
		
		if( cursor.getCount() > 0){ //PQ aqui es mes gran i al anterior diferent??
			String bateria = cursor.getString(cursor.getColumnIndex(Helper.Sensors.KEY_BATERIA));
			bateria =bateria.substring(0,bateria.length()-1); //per treure el unpercent que retorna el php
		
		cursor.close();	

		
		 int perbat = Integer.parseInt(bateria);
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
			
			battery.setImageDrawable(getResources().getDrawable(imagebateria));
		 
		}
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}
	
	
	
	

}

