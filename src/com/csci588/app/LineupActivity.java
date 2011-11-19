package com.csci588.app;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LineupActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylineup);
		
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
		
		LinearLayout llR = new LinearLayout(this);
		llR.setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout llB = new LinearLayout(this);
		llB.setOrientation(LinearLayout.HORIZONTAL);
				
		Cursor cursor = gatherRoster(1,9);
		fillNames(llR,cursor);
		fillLastWeek(llR,cursor);
		fillProjected(llR,cursor);
		fillProjected(llR,cursor);
		fillProjected(llR,cursor);
		HorizontalScrollView hsvR = (HorizontalScrollView) this.findViewById(R.id.roster_lineup);
		hsvR.addView(llR);
		//Cursor c = gatherBench(1, 9);
		//HorizontalScrollView hsvB = (HorizontalScrollView) this.findViewById(R.id.bench_lineup);
		//fillBenchPos(llB,c);
	//	hsvB.addView(llB);
		
		cursor.close();
		//if(c != null)
		//	c.close();
		
	}
		
	private void fillNames(LinearLayout mainLL, Cursor cursor){
		Log.i("HERE?", "NOW?");
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Name");
		ll.addView(tv);
		//int count = 0;//do take account for blank spots
		if(cursor.moveToFirst()){
			do{
				Log.i("HERE?", "NOW?!");
				tv = new TextView(this);
			//	if(rosterBlanks[count] == true){
			//		tv.setText("EMPTY");
		//		}else
					tv.setText(cursor.getString(0) + " " + cursor.getString(1));
				ll.addView(tv);
			//	count++;
			}while(cursor.moveToNext());
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		Log.i("HERE?", "WHERE?");
		layoutParams.setMargins(20, 0, 20, 0);
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillLastWeek(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Last Week");
		ll.addView(tv);
		for(int i = 0; i < 7; i++){
			tv = new TextView(this);
			tv.setText("22.2");
			ll.addView(tv);
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(20, 0, 20, 0);
		mainLL.addView(ll,layoutParams);
	}
	
	private void fillProjected(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Proj.");
		ll.addView(tv);
		for(int i = 0; i < 7; i++){
			tv = new TextView(this);
			tv.setText("25.2");
			ll.addView(tv);
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(20, 0, 20, 0);
		mainLL.addView(ll,layoutParams);
	}
	
	private Cursor gatherRosterList(int teamID, int weekID){
		Log.i("WHY","HEY");
		String q1 = "SELECT ROSTERS.qb, ROSTERS.rb1, ROSTERS.rb2, ROSTERS.wr1, ROSTERS.wr2, ROSTERS.te, ROSTERS.k FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ teamID + " and week = " + weekID;
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private Cursor gatherRoster(int teamID, int weekID){
		Log.i("WHY","NO");
		Cursor tempCursor = gatherRosterList(teamID, weekID);
		checkRosterBlanks(tempCursor);
		Log.i("WHY","NO!");
		Log.i("null",""+rosterBlanks[3]);
		String q1 = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName from NFL_PLAYERS where NFL_PLAYERS._id IN (";
		if(tempCursor.moveToFirst()){
			for(int i = 0; i < 7; i++){
				Log.i("sup"," " + i);
				if(rosterBlanks[i]){
					continue;
				}else{
				Log.i("SUP?", i +"  "+ tempCursor.getString(i));
					if(i < 6)
						q1 += tempCursor.getString(i) + ",";
					else
						q1 += tempCursor.getString(i);
				}
					
			}
			q1+=")";
			Log.i("q",q1);
			q1+=" ORDER BY CASE NFL_PLAYERS.position_id WHEN 0 THEN 0 WHEN 2 THEN 1 WHEN 1 THEN 2 WHEN 3 THEN 3 WHEN 4 THEN 4 END";
		}
		else{
			Log.i("TACOBELL?", "YEP");
			return null;
		}
		Log.i("WHY","null?");
		tempCursor.close();
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private Cursor gatherBench(int teamID, int weekID){
		Cursor tempCursor = gatherBenchList(teamID, weekID);
		checkBenchBlanks(tempCursor);
		String q1 = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS.position_id from NFL_PLAYERS where NFL_PLAYERS._id IN (";
		if(tempCursor.moveToFirst() && !benchAllBlanks()){
			for(int i = 0; i < 5; i++){
				if(i != 0 && !benchBlanks[i])
					q1 +=",";
				if(!benchBlanks[i])
					q1 += tempCursor.getString(i);
			}
			q1+=")";
			q1+=" ORDER BY CASE NFL_PLAYERS.position_id WHEN 0 THEN 0 WHEN 2 THEN 1 WHEN 1 THEN 2 WHEN 3 THEN 3 WHEN 4 THEN 4 END";
		}
		else{
			Log.i("CURSOR", "NOT NULL?");
			tempCursor.close();
			return null;
		}
		tempCursor.close();
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	private void fillBenchPos(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		if(cursor !=null){
			if(cursor.moveToFirst()){
				for(int i =0; i< 5; i++){
					tv = new TextView(this);
					if(benchBlanks[i])
						tv.setText("BN");
					else
						tv.setText(positions[cursor.getInt(2)]);
					ll.addView(tv);		
				}
			}
		}else{
			for(int i = 0; i < 5; i++){
				tv = new TextView(this);
				tv.setText("BN");
				ll.addView(tv);
			}
		}
		mainLL.addView(ll);
	}
	
	private void fillBenchNames(LinearLayout mainLL, Cursor cursor){
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView tv;
		tv = new TextView(this);
		tv.setText("Name");
		ll.addView(tv);
		if(cursor.moveToFirst()){
			do{
				tv = new TextView(this);
				tv.setText(cursor.getString(0) + " " + cursor.getString(1));
				ll.addView(tv);
			}while(cursor.moveToNext());
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(20, 0, 20, 0);
		mainLL.addView(ll,layoutParams);
	}
	
	private Cursor gatherBenchList(int teamID, int weekID){
		String q1 = "SELECT ROSTERS.bn1, ROSTERS.bn2, ROSTERS.bn3, ROSTERS.bn4, ROSTERS.bn5 FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ teamID + " and week = " + weekID;
		return GamedayActivity.getDbHelp().getQuery(q1);
	}
	
	
	private void checkRosterBlanks(Cursor cursor){
		if(cursor.moveToFirst()){
			for(int i = 0; i < 7; i++){
				if(cursor.getString(i) == null)
					rosterBlanks[i] = true;
			}
		}
		
	}
	
	private void checkBenchBlanks(Cursor cursor){
		if(cursor.moveToFirst()){
			for(int i = 0; i < 5; i++){
				if(cursor.getString(i) == null)
					benchBlanks[i] = true;
			}
		}
	}
	
	private boolean allBlanksAhead(int i){
		for(int j = i+1; j < rosterBlanks.length; i++){
			if(rosterBlanks[j] == false){
				return false;
			}
		}
		return true;
	}
	
	private boolean benchAllBlanks(){
		for(int i= 0; i < benchBlanks.length; i++){
			if(benchBlanks[i] == false){
				return false;
			}
		}
		return true;
	}
	
	private String[] positions;
	private boolean[] rosterBlanks;
	private boolean[] benchBlanks;
}
