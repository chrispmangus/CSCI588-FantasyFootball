package com.csci588.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MatchupActivity extends Activity{
	@Override
	protected void onRestart() {
		super.onRestart();
		Intent myIntent = getIntent();
		finish();
		startActivity(myIntent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymatchup);
		
		final String team_id = getIntent().getStringExtra("team_id");
		final String oteam_id = getIntent().getStringExtra("oteam_id");
		final String team_name = getIntent().getStringExtra("team_name");
		final String oteam_name = getIntent().getStringExtra("oteam_name");
		final String team_score = getIntent().getStringExtra("team_score");
		final String oteam_score = getIntent().getStringExtra("oteam_score");
		
		ImageView iV = (ImageView) this.findViewById(R.id.myMatchupTeam);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", team_id);
				startActivityForResult(myIntent,0);
			}
		});
		
		iV = (ImageView) this.findViewById(R.id.opMatchupTeam);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", oteam_id);
				startActivityForResult(myIntent,0);
			}
		});
		
		TextView tV = (TextView) this.findViewById(R.id.myMatchupName);
		tV.setText(team_name + "'s team");
		
		tV = (TextView) this.findViewById(R.id.opMatchupName);
		tV.setText(oteam_name + "'s team");
		
		tV = (TextView) this.findViewById(R.id.myMatchupScore);
		tV.setText(team_score);
		
		tV = (TextView) this.findViewById(R.id.opMatchupScore);
		tV.setText(oteam_score);
		
		String myQuery = "select fName, lName, real_stats, nfl_players._id from nfl_players, nfl_fantasy_stats, rosters " +
				"where manager_id =" +team_id+" and rosters.week = 9 and " +
				"nfl_players._id IN(qb,rb1,rb2,wr1,wr2,te,k) and nfl_players._id = nfl_fantasy_stats._id";
		String oQuery =  "select fName, lName, real_stats, nfl_players._id from nfl_players, nfl_fantasy_stats, rosters " +
				"where manager_id =2 and rosters.week = 9 and " +
				"nfl_players._id IN(qb,rb1,rb2,wr1,wr2,te,k) and nfl_players._id = nfl_fantasy_stats._id";
		
		Cursor myInfo = GamedayActivity.getDbHelp().getQuery(myQuery);
		Cursor oInfo = GamedayActivity.getDbHelp().getQuery(oQuery);
		
		LayoutInflater lf = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		if(myInfo.moveToFirst()){
			LinearLayout ll = (LinearLayout)this.findViewById(R.id.myLayout);
			do{
				View view = lf.inflate(R.layout.mymatchup_item, null);
				tV = (TextView) view.findViewById(R.id.myFirst);
				final String p_id = myInfo.getString(3);
				tV.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						
						Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
						myIntent.putExtra("player_id", p_id);
						v.getContext().startActivity(myIntent);
					}
				});
				tV.setText(myInfo.getString(0) + " " + myInfo.getString(1));
				//tV = (TextView) view.findViewById(R.id.myLast);
				//tV.setText(myInfo.getString(1));
				tV = (TextView) view.findViewById(R.id.myScore);
				tV.setText(myInfo.getString(2));
				ll.addView(view);
			}while(myInfo.moveToNext());
		}
		
		if(oInfo.moveToFirst()){
			LinearLayout ll = (LinearLayout)this.findViewById(R.id.oLayout);
			do{
				View view = lf.inflate(R.layout.mymatchup_item, null);
				tV = (TextView) view.findViewById(R.id.myFirst);
				final String p_id = oInfo.getString(3);
				tV.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						
						Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
						myIntent.putExtra("player_id", p_id);
						v.getContext().startActivity(myIntent);
					}
				});
				tV.setText(oInfo.getString(0) + " " + oInfo.getString(1));
				//tV = (TextView) view.findViewById(R.id.myLast);
				//tV.setText(myInfo.getString(1));
				tV = (TextView) view.findViewById(R.id.myScore);
				tV.setText(oInfo.getString(2));
				ll.addView(view);
			}while(oInfo.moveToNext());
		}
	}
}
