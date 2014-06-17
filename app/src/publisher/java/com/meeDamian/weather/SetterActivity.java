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

public class SetterActivity extends Activity implements WeathersAdapter.OnStateChanged {

	private ActionBar ab;
	private WeathersAdapter wa;

	private String currentWeatherId;

	private GridView weathers;
	private MenuItem saveButton;

	private EditText titleView;
	private EditText descView;

	private boolean unsavedChanges = false;
	private int currentSelection = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setter);

		ab = getActionBar();

		titleView = getEditText(R.id.customTitle);
		descView = getEditText(R.id.customDescription);

		Bundle extras = getIntent().getExtras();
		if( extras!=null ) {

			currentWeatherId = extras.getString("weatherId");

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


		weathers = (GridView) findViewById(R.id.weathersList);
		weathers.setAdapter(wa = new WeathersAdapter(this, currentWeatherId));
		wa.registerOnLoadCallback(this);

		weathers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			updateView(
				position,
				wa.getItemName(position),
				wa.getItemDesc(position)
			);
			acknowledgeChanges();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setter, menu);
		saveButton = menu.findItem(R.id.action_save);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId()==R.id.action_save ) {
			updateWeather();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	public void onAllItemsLoaded(int position, String title, String desc) {
		if( saveButton!=null ) saveButton.setVisible(true);
		updateView(position, title, desc);
	}

	private EditText getEditText(int id) {
		FloatLabelEditText floLab = (FloatLabelEditText) findViewById(id);
		return floLab!=null ? floLab.getEditText() : null;
	}

	private void setText(EditText et, String val) {
		if( et!=null && val!=null ) et.setText(val);
	}

	private void updateView(int position, String title, String desc) {
		currentSelection = position;

		weathers.setItemChecked(position, true);
		weathers.smoothScrollToPosition(position);

		setText( titleView, title );
		setText( descView, desc );
	}

	private void updateWeather() {
		ParseObject updatedWeather = new ParseObject("CurrentWeather");

		ParseObject baseWeather = wa.getItem(currentSelection);
		baseWeather.increment("timesUsed");
		baseWeather.saveInBackground();

		updatedWeather.put("base", wa.getItem(currentSelection));

		if( !titleView.getText().toString().equals(baseWeather.getString("defaultTitle")) ) {
			updatedWeather.put("title", titleView.getText().toString());
		}

		if( !descView.getText().toString().equals(baseWeather.getString("defaultDescription")) ) {
			updatedWeather.put("description", descView.getText().toString());
		}

		try {
			updatedWeather.save();
		} catch(ParseException ignored) {}
		finish();
	}

	private void acknowledgeChanges() {
		unsavedChanges = true;
		saveButton.setEnabled(true);
	}

}
