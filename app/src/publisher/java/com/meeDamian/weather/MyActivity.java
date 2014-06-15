package com.meeDamian.weather;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class MyActivity extends BaseActivity {
	@Override
	void handleControls() {
		ImageButton settingsLauncher = (ImageButton) findViewById(R.id.controls);
		if( settingsLauncher!=null ) {
			settingsLauncher.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), SetterActivity.class);
				i.putExtra("weatherId", currentWeatherId);
				startActivity(i);
				}
			});
		}
	}
}
