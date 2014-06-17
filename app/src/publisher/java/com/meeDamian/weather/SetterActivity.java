package com.meeDamian.weather;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.micromobs.android.floatlabel.FloatLabelEditText;
import com.parse.ParseException;
import com.parse.ParseObject;

public class SetterActivity extends Activity {

	private ActionBar ab;
	private WeathersAdapter wa;

	private String baseWeatherId;

	private GridView weathers;
	private MenuItem saveButton;

	private EditText titleView;
	private EditText descView;

	private int currentSelection = -1;

	@SuppressLint("AppCompatMethod")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setter);

		ab = getActionBar();

		titleView = getEditText(R.id.customTitle);
		descView = getEditText(R.id.customDescription);

		Bundle extras = getIntent().getExtras();
		if( extras!=null ) {
			baseWeatherId = extras.getString("baseId");
			ParseHelper.getCurrentWeather(null, new ParseHelper.OnCurrentWeather() {
				@Override
				public void onDataAvailable(String weatherId, String baseWeatherId, String title, String desc, Bitmap image) {
				ab.setIcon(new BitmapDrawable(getResources(), image));
				setName(title);
				setDesc(desc);

				TextWatcher tw = new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						acknowledgeChanges();
					}

					@Override
					public void afterTextChanged(Editable s) { }
				};

				titleView.addTextChangedListener(tw);
				descView.addTextChangedListener(tw);

				if( saveButton!=null ) saveButton.setVisible(true);
				}
			});
		}

		weathers = (GridView) findViewById(R.id.weathersList);
		wa = new WeathersAdapter(this, baseWeatherId, new WeathersAdapter.OnWeathersReady() {
			@Override
			public void onPositionFound(int position) {
				checkImage(position);
			}
		});
		weathers.setAdapter(wa);


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

	private EditText getEditText(int id) {
		FloatLabelEditText floLab = (FloatLabelEditText) findViewById(id);
		return floLab!=null ? floLab.getEditText() : null;
	}

	private void checkImage(int position) {
		currentSelection = position;
		weathers.setItemChecked(position, true);
		weathers.smoothScrollToPosition(position);
	}

	private void setName(String title) {
		if( titleView!=null ) titleView.setText( title!=null ? title : "" );
	}
	private void setDesc(String desc) {
		if( descView!=null ) descView.setText( desc!=null ? desc : "" );
	}

	private void updateView(int position, String title, String desc) {
		checkImage( position );
		setName( title );
		setDesc( desc );
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
		saveButton.setEnabled(true);
	}

}
