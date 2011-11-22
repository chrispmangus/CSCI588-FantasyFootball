package com.csci588.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchCursorAdapter extends CursorAdapter {

	private LayoutInflater lf;
	
	public SearchCursorAdapter(Context context, Cursor c) {
		super(context, c);
		lf = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		final String p_id = cursor.getString(0);
		view.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
				myIntent.putExtra("player_id", p_id);
				v.getContext().startActivity(myIntent);
			}
		});
		ImageView iV = (ImageView) view.findViewById(R.id.player_pic);
		iV.setBackgroundResource(PictureActions.pickPicture(cursor.getInt(0)));
		
		TextView text = (TextView) view.findViewById(R.id.playerName);
		text.setText(cursor.getString(1) + " " + cursor.getString(2));
	 	text = (TextView) view.findViewById(R.id.teamName);
	 	text.setText(cursor.getString(1) + " " + cursor.getString(2));
	 	text = (TextView) view.findViewById(R.id.realPoints);
	 	text.setText("Points: " + cursor.getString(3));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = lf.inflate(R.layout.roster_item,parent,false);
		return v;
	}

	
}
