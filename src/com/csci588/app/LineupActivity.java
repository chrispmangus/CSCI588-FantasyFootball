package com.csci588.app;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LineupActivity extends Activity{
	
	@Override
	protected void onRestart(){
		super.onRestart();
		Intent intent = getIntent();
   	 finish();
   	 startActivity(intent);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylineup);
		
		final int team_id = Integer.parseInt(getIntent().getStringExtra("team_id"));
		
		benchBlanks = new boolean[5];
		Arrays.fill(benchBlanks, false);
		
		rosterBlanks = new boolean[7];
		Arrays.fill(rosterBlanks, false);
		
		positions = new String[5];
		positions[0] = "QB";
		positions[1] = "RB";
		positions[2] = "WR";
		positions[3] = "TE";
		positions[4] = "K";
		
		String teamNameQuery = "select username from managers where _id = " + team_id;
		String teamScoreQuery = "select sum(real_stats) from nfl_fantasy_stats, rosters " +
				"where manager_id = "+ team_id+ " and rosters.week = " + 9 + " and " +
				"( rosters.qb= _id or rosters.rb1 = _id or rosters.rb2 = _id " +
				"or rosters.wr1 = _id or rosters.wr2 = _id or rosters.te = _id or rosters.k = _id)";
		String recordQuery = "select wins, losses, ties from records where _id = " + team_id;
		Cursor recordInfo = GamedayActivity.getDbHelp().getQuery(recordQuery);
		Cursor teamNameInfo = GamedayActivity.getDbHelp().getQuery(teamNameQuery);
		Cursor teamScoreInfo = GamedayActivity.getDbHelp().getQuery(teamScoreQuery);
		
		teamNameInfo.moveToFirst();
		teamScoreInfo.moveToFirst();
		recordInfo.moveToFirst();
		
		TextView tV = (TextView) this.findViewById(R.id.teamName);
		tV.setText(teamNameInfo.getString(0) + "'s team");
		
		tV = (TextView) this.findViewById(R.id.weekNum);
		tV.setText("Week 9 score: ");
		
		tV = (TextView) this.findViewById(R.id.weekScore);
		tV.setText(teamScoreInfo.getString(0));
		
		tV = (TextView) this.findViewById(R.id.currentRank);
		tV.setText(recordInfo.getString(0) + "-" + recordInfo.getString(1) + "-" + recordInfo.getString(2));
		
		LinearLayout llR = new LinearLayout(this);
		llR.setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout llB = new LinearLayout(this);
		llB.setOrientation(LinearLayout.HORIZONTAL);
				
		Cursor cursor = gatherRoster(team_id,9);
		Cursor c = gatherBench(team_id, 9);
		
		String rosterStatsQuery = "select real_stats, proj_stats, avgpts from nfl_fantasy_stats, nfl_player_stats " +
				"where nfl_fantasy_stats._id = nfl_player_stats._id and nfl_player_stats._id IN"
				+"("+ rosters+")";
		String benchesStatsQuery = "select real_stats, proj_stats, avgpts from nfl_fantasy_stats, nfl_player_stats " +
				"where nfl_fantasy_stats._id = nfl_player_stats._id and nfl_player_stats._id IN"
				+"("+ benches+")";
		Cursor rosterStatsInfo = GamedayActivity.getDbHelp().getQuery(rosterStatsQuery);
		Cursor benchStatsInfo = GamedayActivity.getDbHelp().getQuery(benchesStatsQuery);
		fillRosterNames(llR,cursor);
		llR.addView(getDivider(this),getDividerParams());
		fillBenchPos(llB);
		fillBenchNames(llB, c);
		fillRealStats(llR,rosterStatsInfo);
		llR.addView(getDivider(this),getDividerParams());
		fillProjected(llR,rosterStatsInfo);
		llR.addView(getDivider(this),getDividerParams());
		fillAverage(llR,rosterStatsInfo);
		
		fillBRealStats(llB,benchStatsInfo);
		llB.addView(getDivider(this),getDividerParams());
		fillBProjected(llB,benchStatsInfo);
		llB.addView(getDivider(this),getDividerParams());
		fillBAverage(llB,benchStatsInfo);
		HorizontalScrollView hsvR = (HorizontalScrollView) this.findViewById(R.id.roster_lineup);
		hsvR.addView(llR);
		
		HorizontalScrollView hsvB = (HorizontalScrollView) this.findViewById(R.id.bench_lineup);
		//fillBenchPos(llB,c);
		hsvB.addView(llB);
		
		teamNameInfo.close();
		teamScoreInfo.close();
		rosterStatsInfo.close();
		benchStatsInfo.close();
		recordInfo.close();
		cursor.close();
		if(c != null)
			c.close();
		
	}
		
	private void fillRosterNames(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Name");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < rosterBlanks.length; i++){
				
				tv = new TextView(this);
				if(rosterBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(0) + " " + cursor.getString(1));
					tv.setTextSize(16);
					final String p_id = cursor.getString(2);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					tv.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							
							Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
							myIntent.putExtra("player_id", p_id);
							v.getContext().startActivity(myIntent);
						}
					});
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(15, 0, 20, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillBenchNames(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Name");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor != null){
		
			cursor.moveToFirst();
			for(int i = 0; i < benchBlanks.length; i++){
				tv = new TextView(this);
				if(benchBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(0) + " " + cursor.getString(1));
					tv.setTextSize(16);
					final String p_id = cursor.getString(2);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					tv.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							
							Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
							myIntent.putExtra("player_id", p_id);
							v.getContext().startActivity(myIntent);
						}
					});
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}else{
			for(int i = 0; i < benchBlanks.length; i++){
				tv = new TextView(this);
				tv.setText("");
				tv.setTextSize(16);
				tv.setBackgroundResource(R.color.rosterTextBg);
				tv.setTextColor(getResources().getColor(R.color.mainScreenText));
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(15, 0, 20, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	
	
	private void fillRealStats(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Real Stats");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < rosterBlanks.length; i++){
				
				tv = new TextView(this);
				if(rosterBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(0));
					tv.setTextSize(16);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillProjected(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Proj. Stats");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < rosterBlanks.length; i++){
				
				tv = new TextView(this);
				if(rosterBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(1));
					tv.setTextSize(16);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillAverage(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Avg. Stats");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < rosterBlanks.length; i++){
				
				tv = new TextView(this);
				if(rosterBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(2));
					tv.setTextSize(16);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		
		mainLL.addView(ll,layoutParams);
	}

	private void fillBRealStats(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Real Stats");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < benchBlanks.length; i++){
				
				tv = new TextView(this);
				if(benchBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(0));
					tv.setTextSize(16);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillBProjected(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Proj. Stats");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < benchBlanks.length; i++){
				
				tv = new TextView(this);
				if(benchBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(1));
					tv.setTextSize(16);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillBAverage(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.tab_unselected);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Avg. Stats");
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		if(cursor.moveToFirst()){
		
			
			for(int i = 0; i < benchBlanks.length; i++){
				
				tv = new TextView(this);
				if(benchBlanks[i] == true){
					tv.setText("");
					tv.setTextSize(16);
				}
				else{
					tv.setText(cursor.getString(2));
					tv.setTextSize(16);
					tv.setBackgroundResource(R.color.rosterTextBg);
					tv.setTextColor(getResources().getColor(R.color.mainScreenText));
					
					cursor.moveToNext();
				}
				ll.addView(tv);
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		
		mainLL.addView(ll,layoutParams);
	}
	
	
	private Cursor gatherRoster(int teamID, int weekID){
		Cursor tempCursor = gatherRosterList(teamID, weekID);
		checkBlanks(tempCursor, rosterBlanks);
		String q1 = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id from NFL_PLAYERS where NFL_PLAYERS._id IN (";
		if(tempCursor.moveToFirst()){
			for(int i = 0; i < 7; i++){
				if(rosterBlanks[i]){
					continue;
				}else{
					if(i < 6 && !allBlanksAhead(rosterBlanks,i)){
						q1 += tempCursor.getString(i) + ",";
						rosters += tempCursor.getString(i) + ",";
					}
					else{
						q1 += tempCursor.getString(i);
						rosters += tempCursor.getString(i);
					}
				}
					
			}
			q1+=")";
			q1+=" ORDER BY CASE NFL_PLAYERS.position_id WHEN 0 THEN 0 WHEN 2 THEN 1 WHEN 1 THEN 2 WHEN 3 THEN 3 WHEN 4 THEN 4 END";
		}
		else{
			return null;
		}
		tempCursor.close();
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private Cursor gatherBench(int teamID, int weekID){
		Cursor tempCursor = gatherBenchList(teamID, weekID);
		checkBlanks(tempCursor, benchBlanks);
		String q1 = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id, NFL_PLAYERS.position_id from NFL_PLAYERS where NFL_PLAYERS._id IN (";
		if(tempCursor.moveToFirst() && !allBlanks(benchBlanks)){
			for(int i = 0; i < 5; i++){
				if(benchBlanks[i]){
					continue;
				}else{
					if(i < 6 && !allBlanksAhead(benchBlanks,i)){
						q1 += tempCursor.getString(i) + ",";
						benches += tempCursor.getString(i) + ",";
					}
					else{
						q1 += tempCursor.getString(i);
						benches += tempCursor.getString(i);
					}
				}
			}
			q1+=")";
			q1+=" ORDER BY CASE NFL_PLAYERS.position_id WHEN 0 THEN 0 WHEN 2 THEN 1 WHEN 1 THEN 2 WHEN 3 THEN 3 WHEN 4 THEN 4 END";
		}
		else{
			tempCursor.close();
			return null;
		}
		tempCursor.close();
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private void fillBenchPos(LinearLayout mainLL){
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
	
		tv = new TextView(this);
		tv.setText("Pos.");
		tv.setTextSize(16);
		tv.setBackgroundResource(R.color.pos_col);
		tv.setTextColor(Color.parseColor("#E8E8E8"));
		ll.addView(tv);
		for(int i = 0; i < 5; i++){
			tv = new TextView(this);
			tv.setText("BN" + (i+1));
			tv.setTextSize(16);
			tv.setBackgroundResource(R.color.type_bg_col);
			ll.addView(tv);
		}
		
		mainLL.addView(ll);
	}
	
	
	private Cursor gatherRosterList(int teamID, int weekID){
		String q1 = "SELECT ROSTERS.qb, ROSTERS.rb1, ROSTERS.rb2, ROSTERS.wr1, ROSTERS.wr2, ROSTERS.te, ROSTERS.k FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ teamID + " and week = " + weekID;
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private Cursor gatherBenchList(int teamID, int weekID){
		String q1 = "SELECT ROSTERS.bn1, ROSTERS.bn2, ROSTERS.bn3, ROSTERS.bn4, ROSTERS.bn5 FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ teamID + " and week = " + weekID;
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private static void checkBlanks(Cursor cursor, boolean[] blanks){
		if(cursor.moveToFirst()){
			for(int i = 0; i < blanks.length; i++){
				if(cursor.getString(i) == null || cursor.getString(i).equals(""))
					blanks[i] = true;
			}
		}
	}
	
	private static int firstBlank(boolean[] blanks){
		for(int i = 0; i< blanks.length; i++){
			if(blanks[i] == true)
				return i;
		}
		return -1;
	}

	
	private boolean allBlanksAhead(boolean[] blanks, int i){
		for(int j = i+1; j < blanks.length; j++){
			if(blanks[j] == false){
				return false;
			}
		}
		return true;
	}
	
	private boolean allBlanks(boolean[] blanks){
		for(int i = 0; i < blanks.length; i++){
			if(blanks[i] == false){
				return false;
			}
		}
		return true;
	}
	
	public static View getDivider(Context context){
		View v = new View(context);
		
		v.setBackgroundColor(0xFFFFFFFF);
		return v;
	}
	
	public static LinearLayout.LayoutParams getDividerParams(){
		LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		vParams.width = 2;
		return vParams;
	}
	
	private String rosters = "";
	private String benches = "";
	

	private String[] positions;
	private boolean[] rosterBlanks;
	private boolean[] benchBlanks;
}
