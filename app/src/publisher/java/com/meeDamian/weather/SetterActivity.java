package com.meeDamian.weather;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.micromobs.android.floatlabel.FloatLabelEditText;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SetterActivity extends Activity {

	private ActionBar ab;
	private WeathersAdapter wa;

	private EditText titleView;
	private EditText descView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setter);

		ab = getActionBar();

		titleView = getEditText(R.id.customTitle);
		descView = getEditText(R.id.customDescription);

		Bundle extras = getIntent().getExtras();
		if( extras!=null ) {
			String currentWeatherId = extras.getString("weatherId");

			ParseQuery.getQuery("Weather").getInBackground(currentWeatherId, new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject currentWeather, ParseException e) {
				if( e==null ) {
					currentWeather
						.getParseFile("image")
						.getDataInBackground(new GetDataCallback() {
							@Override
							public void done(byte[] bytes, ParseException e) {
							ab.setIcon(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
							}
						});
				}
				}
			});
		}

		final GridView weathers = (GridView) findViewById(R.id.weathersList);
		weathers.setAdapter(wa = new WeathersAdapter(this));

		weathers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			view.setSelected(true);

			if( wa!=null ) {
				wa.selectItem(position, new WeathersAdapter.OnItemSelected() {
					@Override
					public void onSelect(String title, String description) {
					if( title!=null && titleView!=null ) titleView.setText(title);
					if( description!=null && descView!=null ) descView.setText(description);
					}
				});
			}
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
		return id==R.id.action_settings || super.onOptionsItemSelected(item);

	}

	private EditText getEditText(int id) {
		FloatLabelEditText floLab = (FloatLabelEditText) findViewById(id);
		return floLab!=null ? floLab.getEditText() : null;
	}
}
