package com.meeDamian.weather;

import android.view.View;
import android.view.ViewGroup;

public class ViewGroupUtils {

	public static ViewGroup getParent(View v) {
		return (ViewGroup) v.getParent();
	}

	public static void removeView(View v) {
		ViewGroup p = getParent(v);
		if( p!=null ) p.removeView(v);
	}

	public static void replaceView(View cv, View nv) {
		ViewGroup p = getParent(cv);
		if( p==null ) return;

		final int idx = p.indexOfChild(cv);
		removeView(cv);
		removeView(nv);
		p.addView(nv, idx);
	}
}
