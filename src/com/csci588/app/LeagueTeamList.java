package com.csci588.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeagueTeamList {
	
	private Context context;
	private DatabaseHelper db;
	
	LeagueTeamList(Context context, DatabaseHelper db){
		this.context = context;
		this.db = db;
	}
	
	
	// Fill list of teams
	public ViewGroup createTeamList(ViewGroup ll, String query){
		Cursor dataCursor = db.getQuery(query);
		if(dataCursor.moveToFirst()){
			int i = 1;
			do {
				ll.addView(itemCreator(Integer.toString(i), dataCursor));
				++i;
			}
			while(dataCursor.moveToNext());
		}
		dataCursor.close();
		return ll;		
	}
	
	// Fill team entry
	private View itemCreator(String rank, Cursor cursor){
		LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lf.inflate(R.layout.league_team, null);
		TextView text = (TextView) view.findViewById(R.id.teamRank);
		text.setText(rank);
	 	text = (TextView) view.findViewById(R.id.leagueTeamName);
	 	text.setText(cursor.getString(0));
	 	text = (TextView) view.findViewById(R.id.teamRecord);
	 	text.setText(cursor.getString(1) + "-" + cursor.getString(2) + "-" + cursor.getString(3));
	 	final String p_id = cursor.getString(4);
	 	text = (TextView) view.findViewById(R.id.teamPoints);
	 	String totalPoints =  "select sum(real_stats) from nfl_fantasy_stats, rosters " +
				"where manager_id = "+ cursor.getString(4) + " and rosters.week = 9 and " +
				"( rosters.qb= _id or rosters.rb1 = _id or rosters.rb2 = _id " +
				"or rosters.wr1 = _id or rosters.wr2 = _id or rosters.te = _id or rosters.k = _id)";
	 	Cursor ptCursor = db.getQuery(totalPoints);
	 	ptCursor.moveToFirst();
	 	text.setText(Float.toString(ptCursor.getFloat(0)));
	 		 	
	 	view.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class); // need new activity for leagues
				myIntent.putExtra("team_id", p_id); // get team_id using maagnerId
				v.getContext().startActivity(myIntent);
			}
		});
	 	
	 	return view;
	}

	
}
