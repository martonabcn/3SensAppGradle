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

	public InfoUserFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		//le paso la id del archivo de vista al inflater.retorna una view llamada v.
		View v = inflater.inflate(R.layout.fragment_infouser, container, false);
        return v;
    }
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        //Asocio cada elemento del xml al elemento de tipo TextView
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
			nametv.setText(getString(R.string.iu_name) +c.getString(c.getColumnIndex(Helper.User.KEY_NAME)));
			surnametv.setText(getString(R.string.iu_surname) +c.getString(c.getColumnIndex(Helper.User.KEY_SURNAME)));
			agetv.setText(getString(R.string.iu_age) +c.getString(c.getColumnIndex(Helper.User.KEY_AGE)));
			addresstv.setText(getString(R.string.iu_address) +c.getString(c.getColumnIndex(Helper.User.KEY_ADDRESS)));
			phonetv.setText(getString(R.string.iu_phone) +c.getString(c.getColumnIndex(Helper.User.KEY_PHONE)));
			emailtv.setText(getString(R.string.iu_email) +c.getString(c.getColumnIndex(Helper.User.KEY_EMAIL)));
			notestv.setText(getString(R.string.iu_notes) +c.getString(c.getColumnIndex(Helper.User.KEY_NOTES)));
		}
		bd.close();
		c.close();	
		ImageView battery = (ImageView)getActivity().findViewById(R.id.bateriauser);
		Cursor cursor =getActivity().getContentResolver().query(SensorsProvider.URI_MOBILESENSOR, null, null, null, null);
		cursor.moveToFirst();
		
		
		if( cursor.getCount() > 0){
			String bateria = cursor.getString(cursor.getColumnIndex(Helper.Sensors.KEY_BATERIA));
			bateria =bateria.substring(0,bateria.length()-1); //elimino el % que retorna el php
		
		cursor.close();	

		
		 int perbat = Integer.parseInt(bateria);
			int imagebateria = R.drawable.bateria5;
			
			Double n = (double)perbat/25;
			int p = n.intValue(); //solo parte entera, antes de los decimales
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

