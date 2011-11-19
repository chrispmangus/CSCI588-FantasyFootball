package com.csci588.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopPerformerList {

	private Context context;
	
	TopPerformerList(Context context){
		this.context = context;
	}
	
	public LinearLayout createPerformerList(String query, DatabaseHelper db){
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		Cursor dataCursor = db.getQuery(query);
		if(dataCursor.moveToFirst()){
			do{
				ll.addView(itemCreator(dataCursor));
			}
			while(dataCursor.moveToNext());
		}
		dataCursor.close();
		return ll;		
	}
	
	
	private View itemCreator(Cursor cursor){
		LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lf.inflate(R.layout.top_performer, null);
		TextView text = (TextView) view.findViewById(R.id.playerName);
		text.setText(cursor.getString(0) + " " + cursor.getString(1));
	 	text = (TextView) view.findViewById(R.id.teamName);
	 	text.setText(cursor.getString(0) + " " + cursor.getString(1));
	 	text = (TextView) view.findViewById(R.id.realPoints);
	 	text.setText("32.2");
	 	text = (TextView) view.findViewById(R.id.projPoints);
	 	text.setText("22.2");
	 	final String p_id = cursor.getString(2);
	 	view.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
				myIntent.putExtra("player_id", p_id);
				v.getContext().startActivity(myIntent);
			}
		});
	 	
	 	return view;
	}
}
