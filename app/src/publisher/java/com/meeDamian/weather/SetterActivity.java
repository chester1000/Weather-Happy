package com.meeDamian.weather;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SetterActivity extends Activity {

	private ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setter);

		ab = getActionBar();

		Bundle extras = getIntent().getExtras();
		if( extras!=null ) {
			String currentWeatherId = extras.getString("weatherId");

			ParseQuery.getQuery("Weather").getInBackground(currentWeatherId, new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject currentWeather, ParseException e) {
				if( e==null ) {
					currentWeather.getParseFile("image").getDataInBackground(new GetDataCallback() {
						@Override
						public void done(byte[] bytes, ParseException e) {
						ab.setIcon(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
						}
					});
				}
				}
			});
		}

		GridView weathers = (GridView) findViewById(R.id.weathersList);
		weathers.setAdapter(new WeathersAdapter(this));

		weathers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.d("Item Clicked", "" + position);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if( id==R.id.action_settings ) return true;

		return super.onOptionsItemSelected(item);
	}
}
