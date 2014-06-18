package com.meeDamian.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseHelper {

	private static ParseHelper instance;

	private ParseHelper(Context c ) {
		Parse.initialize(c, "dMeWN03ARR2f0OuFaytujYpdbjnTqRXpw8k1DRdN", BuildConfig.PARSE_KEY);
	}

	public static void getCurrentWeather(final String currentId, final OnCurrentWeather callback) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("CurrentWeather");
		query.include("base");
		query.orderByDescending("createdAt");
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject weatherObject, ParseException e) {
			if( e==null && (currentId==null || !weatherObject.getObjectId().equals(currentId)) ) {

				final String myId = weatherObject.getObjectId();
				String tmpTitle = weatherObject.getString("title");
				String tmpDesc = weatherObject.getString("description");

				ParseObject baseWeather = weatherObject.getParseObject("base");
				if( baseWeather!=null ) {

					final String baseId = baseWeather.getObjectId();
					final String title = ( tmpTitle==null ) ? baseWeather.getString("defaultTitle") : tmpTitle;
					final String desc = ( tmpDesc==null ) ? baseWeather.getString("defaultDescription") : tmpDesc;

					baseWeather.getParseFile("image").getDataInBackground(new GetDataCallback() {
						@Override
						public void done(byte[] bytes, ParseException e) {
						Bitmap image = (e==null) ? BitmapFactory.decodeByteArray(bytes, 0, bytes.length) : null;
						callback.onDataAvailable(myId, baseId, title, desc, image);
						}
					});
				}
			}
			}
		});
	}

	public static ParseHelper getInstance(Context c) {
		return instance!=null ? instance : (instance = new ParseHelper(c));
	}

	public interface OnCurrentWeather {
		public void onDataAvailable(String weatherId, String baseWeatherId, String title, String desc, Bitmap image);
	}
}
