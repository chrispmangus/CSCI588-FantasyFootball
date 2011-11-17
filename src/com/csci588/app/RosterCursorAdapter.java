package com.csci588.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RosterCursorAdapter extends CursorAdapter {

	private LayoutInflater lf;
	
	public RosterCursorAdapter(Context context, Cursor c) {
		super(context, c);
		lf = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView text = (TextView) view.findViewById(R.id.playerName);
		text.setText(cursor.getString(1) + " " + cursor.getString(2));
	 	text = (TextView) view.findViewById(R.id.teamName);
	 	text.setText(cursor.getString(1) + " " + cursor.getString(2));
	 	text = (TextView) view.findViewById(R.id.realPoints);
	 	text.setText("32.2");
	 	text = (TextView) view.findViewById(R.id.projPoints);
	 	text.setText("22.1");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = lf.inflate(R.layout.roster_item,parent,false);
		return v;
	}

	
}
