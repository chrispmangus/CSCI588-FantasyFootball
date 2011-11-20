package com.csci588.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeagueTeamList {
	
	private Context context;
	
	LeagueTeamList(Context context){
		this.context = context;
	}
	
	public LinearLayout createTeamList(String query, DatabaseHelper db){
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
		View view = lf.inflate(R.layout.league_team, null);
		TextView text = (TextView) view.findViewById(R.id.teamRank);
		text.setText(cursor.getString(0) + " " + cursor.getString(1));
	 	text = (TextView) view.findViewById(R.id.leagueTeamName);
	 	text.setText(cursor.getString(0) + " " + cursor.getString(1));
	 	text = (TextView) view.findViewById(R.id.teamRecord);
	 	text.setText(cursor.getString(0) + " " + cursor.getString(1));
	 	text = (TextView) view.findViewById(R.id.teamPoints);
	 	text.setText(cursor.getString(0) + " " + cursor.getString(1));
	 	final String p_id = cursor.getString(2);
	 	view.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class); // need new activity for leagues
				myIntent.putExtra("team_id", p_id);
				v.getContext().startActivity(myIntent);
			}
		});
	 	
	 	return view;
	}
	
}
