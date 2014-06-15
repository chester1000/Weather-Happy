package com.meeDamian.weather;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class MyActivity extends BaseActivity {

	@Override
	void handleControls() {
		ImageButton launchSettings = (ImageButton) findViewById(R.id.controls);

		if( launchSettings!=null ) {
			launchSettings.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), SetterActivity.class));
				}
			});
		}

	}

}
