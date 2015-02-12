package com.abinaya.business;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TabHost tabHost = getTabHost();

		Intent intentListCards = new Intent().setClass(this,
				AllCardsActivity.class);
		TabSpec listSpec = tabHost.newTabSpec("ListCards")
				.setIndicator("List Cards").setContent(intentListCards);

		Intent intentMyCard = new Intent().setClass(this, MyCardActivity.class);
		TabSpec myCardSpec = tabHost.newTabSpec("MyCard")
				.setIndicator("My Card").setContent(intentMyCard);

		tabHost.addTab(listSpec);
		tabHost.addTab(myCardSpec);
		tabHost.setBackgroundColor(Color.parseColor("#DCDCDC"));
		tabHost.setCurrentTab(0);
	}

}