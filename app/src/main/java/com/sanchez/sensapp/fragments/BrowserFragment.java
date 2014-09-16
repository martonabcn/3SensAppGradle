package com.sanchez.sensapp.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BrowserFragment extends Fragment {
	private WebView web;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		WebSettings webSettings = web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		SharedPreferences sp =getActivity().getSharedPreferences("Systema", Activity.MODE_PRIVATE);
		int iduser =sp.getInt("IDUSUARIO", 0);
		web.loadUrl("http://158.109.64.44/3sense/grafica_posicion.php?id="+iduser);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		web = new WebView(getActivity());
		return web;
	}

}
