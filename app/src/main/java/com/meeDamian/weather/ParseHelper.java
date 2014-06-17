package com.meeDamian.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseHelper {

	private static ParseHelper instance;

	private ParseHelper(Context c ) {
		Parse.initialize(c, "dMeWN03ARR2f0OuFaytujYpdbjnTqRXpw8k1DRdN", "vOlFqHHbrNaW4urMuPT9jOQKcZYVsTWLjl91tobF");
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

						ParseHelper.setImage(baseWeather, new GetDataCallback() {
							@Override
							public void done(byte[] bytes, ParseException e) {
							Bitmap image = ( e==null ) ? BitmapFactory.decodeByteArray(bytes, 0, bytes.length) : null;
							callback.onDataAvailable(myId, baseId, title, desc, image);
							}
						});
					}
				}
			}
		});
	}



	public static void getWeatherById(String id, GetCallback<ParseObject> callback) {
		ParseQuery.getQuery("Weather").getInBackground(id, callback);
	}

	public static ParseQuery<ParseObject> getAllWeathers(FindCallback<ParseObject> callback) {
		ParseQuery<ParseObject> weatherQuery = ParseQuery.getQuery("Weather");
		weatherQuery.orderByDescending("timesUsed");
		weatherQuery.findInBackground(callback);

		return weatherQuery;
	}

	public static void setImage(ParseObject currentWeather, GetDataCallback callback) {
		currentWeather.getParseFile("image").getDataInBackground(callback);
	}

	public static ParseHelper getInstance(Context c) {
		return instance!=null ? instance : (instance = new ParseHelper(c));
	}

	public interface OnCurrentWeather {
		public void onDataAvailable(String weatherId, String baseWeatherId, String title, String desc, Bitmap image);
	}
}
