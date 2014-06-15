package com.meeDamian.weather;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class WeathersAdapter extends BaseAdapter {

	private Context mContext;
	private List<ParseObject> weathers;
	private LayoutInflater inflater;

	public WeathersAdapter(Context c) {
		mContext = c;

		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ParseQuery.getQuery("Weather").findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
			weathers = parseObjects;
			notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getCount() {
		return weathers!=null ? weathers.size() : 0;
	}

	@Override
	public ParseObject getItem(int position) {
		return weathers!=null ? weathers.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public String getItemName(int position) {
		return getItem(position).getString("defaultTitle");
	}

	public String getItemDesc(int position) {
		return getItem(position).getString("defaultDescription");
	}

	public void getItemPicture(int position, GetDataCallback callback) {
		getItem(position).getParseFile("image").getDataInBackground(callback);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if( convertView!=null ) view = convertView;
		else {
			view = inflater.inflate(R.layout.weather_grid_item, parent, false);

			int height = parent.getHeight();
			if( height>0 ) {
				ViewGroup.LayoutParams params = view.getLayoutParams();
				params.height = height/3;
			}
		}

		((TextView) view.findViewById(R.id.title)).setText(getItemName(position));

		final ImageView image = (ImageView) view.findViewById(R.id.image);
		getItemPicture(position, new GetDataCallback() {
			@Override
			public void done(byte[] bytes, ParseException e) {
			image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
			}
		});

		return view;
	}
}