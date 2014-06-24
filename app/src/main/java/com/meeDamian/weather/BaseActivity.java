package com.meeDamian.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public abstract class BaseActivity extends Activity {

	protected String baseWeatherId;

	private ImageView iconView;
	private TextView titleView;
	private TextView descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

	    iconView = (ImageView) findViewById(R.id.weatherIcon);
	    titleView = (TextView) findViewById(R.id.weatherTitle);
	    descriptionView = (TextView) findViewById(R.id.weatherDescription);

	    handleControls();

	    ParseHelper.getInstance(this);
    }

	@Override
	protected void onResume() {
		super.onResume();
		ParseHelper.getCurrentWeather(null, new ParseHelper.OnCurrentWeather() {
			@Override
			public void onDataAvailable(String weatherId, String baseId, String title, String desc, Bitmap image) {
			baseWeatherId = baseId;
			iconView.setImageBitmap(image);
			iconView.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
			setTitle(title);
			setDescription(desc);
			}
		});
	}

	private String setTitle(String val) {
		if( titleView!=null && val!=null ) titleView.setText(val);
		return val;
	}

	private String setDescription(String val) {
		if( descriptionView!=null && val!=null ) descriptionView.setText(val);
		return val;
	}

	abstract void handleControls();
}
