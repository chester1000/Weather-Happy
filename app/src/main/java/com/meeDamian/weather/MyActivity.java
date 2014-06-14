package com.meeDamian.weather;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MyActivity extends Activity {

	private String title;
	private String desc;

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

	    Parse.initialize(this, "dMeWN03ARR2f0OuFaytujYpdbjnTqRXpw8k1DRdN", "vOlFqHHbrNaW4urMuPT9jOQKcZYVsTWLjl91tobF");

	    ParseQuery<ParseObject> query = ParseQuery.getQuery("CurrentWeather");
	    query.include("base");
	    query.getFirstInBackground(new GetCallback<ParseObject>() {
		    @Override
		    public void done(ParseObject weatherObject, ParseException e) {
		    if( e==null ) {

			    setTitle( weatherObject.getString("title") );
			    setDescription( weatherObject.getString("description") );

			    ParseObject baseWeather = weatherObject.getParseObject("base");
			    if( baseWeather!=null ) {

				    setTitle( baseWeather.getString("defaultTitle") );
				    setDescription( baseWeather.getString("defaultDescription") );

				    baseWeather.getParseFile("image").getDataInBackground(new GetDataCallback() {
					    @Override
					    public void done(byte[] bytes, ParseException e) {
					    if( e==null ) iconView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
				    }
			    });
			    }
		    }
		    }
	    });
    }

	private String setTitle(String val) {
		if( title==null && val!=null && !val.isEmpty() ) titleView.setText(title = val);
		return val;
	}

	private String setDescription(String val) {
		if( desc==null && val!=null && !val.isEmpty() ) descriptionView.setText(desc = val);
		return val;
	}

	public static class ControlsFragment extends Fragment {

	}
}
