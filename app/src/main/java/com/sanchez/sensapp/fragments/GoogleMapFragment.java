package com.sanchez.sensapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sanchez.sensapp.R;

public class GoogleMapFragment extends SupportMapFragment{
	private GoogleMap mapa;
	private LatLng gps;
	public GoogleMapFragment(){
		gps = new LatLng(0, 0);
	}
	

	public LatLng getGps() {
		return gps;
	}


	public void setGps(LatLng gps) {
		this.gps = gps;
    }


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if (status == ConnectionResult.SUCCESS){
			mapa = getMap();
			mapa.getUiSettings().setZoomControlsEnabled(true);
			mapa.getUiSettings().setCompassEnabled(true);
			mapa.getUiSettings().setMyLocationButtonEnabled(true);
			mapa.getUiSettings().setAllGesturesEnabled(true);
			
			mapa.setOnMapLongClickListener(new listenerMap());
			//mapa.setInfoWindowAdapter(new listenerMap());
			mapa.setOnInfoWindowClickListener(new listenerMap());
			
			CameraUpdate camUpdt = CameraUpdateFactory.newLatLngZoom(gps, 15);
			mapa.moveCamera(camUpdt);
			
			MarkerOptions myMarker = new MarkerOptions();
			myMarker.position(gps);
			myMarker.title("Casa sensors");
			myMarker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
			mapa.addMarker(myMarker);



		}
	}

	private class listenerMap implements InfoWindowAdapter, OnMapLongClickListener, OnInfoWindowClickListener{

		@Override
		public void onInfoWindowClick(Marker arg0) {
			// TODO Auto-generated method stub
			//per a quan clickem la finestra de informacio q surt al clicar un marcador. 
		}

		@Override
		public void onMapLongClick(LatLng point) {
			// TODO Auto-generated method stub
			//que far� quan clickem una estona al mapa, podriem fer q afegis un marcador.p.ex
			
		}

		//metodes de la window q apareixal clicar un marcador:
		@Override
		public View getInfoContents(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getInfoWindow(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public void onResume(){
		super.onResume();
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if (status != ConnectionResult.SUCCESS){
			 GooglePlayServicesUtil.getErrorDialog(status,getActivity(),1);
		}
	}


}