package com.meeDamian.weather;

public class MyActivity extends BaseActivity {

	@Override
	void handleControls() {
		ViewGroupUtils.removeView( findViewById(R.id.controls) );
	}

}
