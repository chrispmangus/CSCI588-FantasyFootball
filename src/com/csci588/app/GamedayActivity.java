package com.csci588.app;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class GamedayActivity extends Activity {
	
	/*called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameday);

		dbHelp = new DatabaseHelper(this);
		try {
			 
        	dbHelp.createDataBase();
 
	 	} catch (IOException ioe) {
	 
	 		throw new Error("Unable to create database");
	 
	 	}
	 
	 	try {
	 
	 		dbHelp.openDataBase();
	 
	 	}catch(SQLException sqle){
	 
	 		throw sqle;
	 
	 	}
		
		final int our_manager_id = 1;
		int week = 9;
		
		/* Necessary for underlining TOP PERFORMER textview */
		TextView tV = (TextView) this.findViewById(R.id.topPerformers);
		SpannableString content = new SpannableString("TOP PERFORMERS");
		content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
		tV.setText(content);
		
		setupView(our_manager_id,week,tV, true);
		
		
		/* TODO LEAGUE IMAGE CLICK
		iV = (ImageView) this.findViewById(R.id.myTeamImg);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", ""+our_manager_id);
				startActivityForResult(myIntent,0);
			}
		});*/
		
		
		final Button myLineup = (Button) findViewById(R.id.viewLineup);
		myLineup.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", "1");
				startActivityForResult(myIntent,0);
			}
		});
		
		final Button myMatchup = (Button) findViewById(R.id.viewMatchup);
		myMatchup.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), MidweekActivity.class);
				startActivityForResult(myIntent,0);
			}
		});
		
		
		
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		setupView(1,9,new TextView(this), false);
	}
	
	
	private void setupView(final int our_manager_id, int week, TextView tV, boolean firstFlag){
		String teamNameQuery = "select username from managers where _id = " + our_manager_id;
		String teamScoreQuery = "select sum(real_stats) from nfl_fantasy_stats, rosters " +
				"where manager_id = "+ our_manager_id+ " and rosters.week = " + week + " and " +
				"( rosters.qb= _id or rosters.rb1 = _id or rosters.rb2 = _id " +
				"or rosters.wr1 = _id or rosters.wr2 = _id or rosters.te = _id or rosters.k = _id)";
		Cursor teamNameInfo = dbHelp.getQuery(teamNameQuery);
		Cursor teamScoreInfo = dbHelp.getQuery(teamScoreQuery);
		
		String awayTeamQuery = "select manager_1_id, manager_2_id from match_ups " +
				"where week = " + week + " and (manager_1_id = " +
				+ our_manager_id +" or manager_2_id = " +
				+ our_manager_id + ")";
		Cursor awayTeamInfo =  GamedayActivity.getDbHelp().getQuery(awayTeamQuery);
		
		teamNameInfo.moveToFirst();
		teamScoreInfo.moveToFirst();
		awayTeamInfo.moveToFirst();
		
		int team1 = awayTeamInfo.getInt(0);
		int team2 = awayTeamInfo.getInt(1);
		final int awayTeam = (team1 == our_manager_id) ? team2 : team1;
		awayTeamInfo.close();
		
		String awayNameQuery = "select username from managers where _id = " + awayTeam;
		String awayScoreQuery = "select sum(real_stats) from nfl_fantasy_stats, rosters " +
				"where manager_id = "+ awayTeam+ " and rosters.week = " + week + " and " +
				"( rosters.qb= _id or rosters.rb1 = _id or rosters.rb2 = _id " +
				"or rosters.wr1 = _id or rosters.wr2 = _id or rosters.te = _id or rosters.k = _id)";
		Cursor awayNameInfo = dbHelp.getQuery(awayNameQuery);
		Cursor awayScoreInfo = dbHelp.getQuery(awayScoreQuery);
		
		awayNameInfo.moveToFirst();
		awayScoreInfo.moveToFirst();
		
		tV = (TextView) this.findViewById(R.id.leagueName);
		tV.setText(new SpannableString("Fantasy Football League"));
		
		tV = (TextView) this.findViewById(R.id.myTeamName);
		tV.setText(teamNameInfo.getString(0) + "'s team");
		tV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", ""+our_manager_id);
				startActivityForResult(myIntent,0);
			}
		});
		
		ImageView iV = (ImageView) this.findViewById(R.id.myTeamImg);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", ""+our_manager_id);
				startActivityForResult(myIntent,0);
			}
		});
		
		tV = (TextView) this.findViewById(R.id.myTeamScore);
		tV.setText(teamScoreInfo.getString(0));
		
		tV = (TextView) this.findViewById(R.id.myTeamTime);
		tV.setText(new SpannableString("20 min"));
		
		tV = (TextView) this.findViewById(R.id.vsTeamName);
		tV.setText(awayNameInfo.getString(0) + "'s team");
		tV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", ""+awayTeam);
				startActivityForResult(myIntent,0);
			}
		});
		
		iV = (ImageView) this.findViewById(R.id.vsTeamImg);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", ""+awayTeam);
				startActivityForResult(myIntent,0);
			}
		});
		
		tV = (TextView) this.findViewById(R.id.vsTeamScore);
		tV.setText(awayScoreInfo.getString(0));
		
		tV = (TextView) this.findViewById(R.id.vsTeamTime);
		tV.setText(new SpannableString("60 min"));
		
		//TOP PERFORMERS
		
		String myTopPerformer = "select fName,lName, real_stats, nfl_players._id from nfl_players, nfl_fantasy_stats, rosters " +
				"where nfl_players._id = nfl_fantasy_stats._id and " +
				"manager_id = " + our_manager_id + " and rosters.week = 9 and " +
				"( rosters.qb= nfl_fantasy_stats._id or rosters.rb1 = nfl_fantasy_stats._id or rosters.rb2 = nfl_fantasy_stats._id " +
				"or rosters.wr1 = nfl_fantasy_stats._id or rosters.wr2 = nfl_fantasy_stats._id or rosters.te = nfl_fantasy_stats._id " +
				"or rosters.k = nfl_fantasy_stats._id) order by real_stats desc";
		String awayTopPerformer = "select fName,lName, real_stats, nfl_players._id from nfl_players, nfl_fantasy_stats, rosters " +
				"where nfl_players._id = nfl_fantasy_stats._id and " +
				"manager_id = " + awayTeam + " and rosters.week = 9 and " +
				"( rosters.qb= nfl_fantasy_stats._id or rosters.rb1 = nfl_fantasy_stats._id or rosters.rb2 = nfl_fantasy_stats._id " +
				"or rosters.wr1 = nfl_fantasy_stats._id or rosters.wr2 = nfl_fantasy_stats._id or rosters.te = nfl_fantasy_stats._id " +
				"or rosters.k = nfl_fantasy_stats._id) order by real_stats desc";
		Cursor myTopInfo = dbHelp.getQuery(myTopPerformer);
		Cursor awayTopInfo = dbHelp.getQuery(awayTopPerformer);
		
		myTopInfo.moveToFirst();
		awayTopInfo.moveToFirst();
		
		tV = (TextView) this.findViewById(R.id.myTopName);
		tV.setText(myTopInfo.getString(0) + " " + myTopInfo.getString(1));
		tV = (TextView) this.findViewById(R.id.myTopScore);
		tV.setText(myTopInfo.getString(2));
		tV = (TextView) this.findViewById(R.id.myTopTime);
		tV.setText("10 min");
		final int topId = myTopInfo.getInt(3);
		iV = (ImageView) this.findViewById(R.id.myTopPerform);
		iV.setBackgroundResource(PictureActions.pickPicture(topId));
		
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
				myIntent.putExtra("player_id", ""+topId);
				v.getContext().startActivity(myIntent);
			}
		});
		
		tV = (TextView) this.findViewById(R.id.vsTopName);
		tV.setText(awayTopInfo.getString(0) + " " + awayTopInfo.getString(1));
		tV = (TextView) this.findViewById(R.id.vsTopScore);
		tV.setText(awayTopInfo.getString(2));
		tV = (TextView) this.findViewById(R.id.vsTopTime);
		tV.setText("20 min");
		final int awayId = awayTopInfo.getInt(3);
		iV = (ImageView) this.findViewById(R.id.vsTopPerform);
		iV.setBackgroundResource(PictureActions.pickPicture(awayId));
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
				myIntent.putExtra("player_id", ""+awayId);
				v.getContext().startActivity(myIntent);
			}
		});
		
		myTopInfo.close();
		awayTopInfo.close();
		
		tabs = (TabHost)this.findViewById(R.id.stat_tabhost);
		if(firstFlag)
			tabs.setup();
		else{
			tabs.setCurrentTab(0);
			tabs.clearAllTabs();
		}

	 	TopPerformerList tpl = new TopPerformerList(this);
	 	String query = "select fName, lName,nfl_players._id from nfl_players, nfl_fantasy_stats " +
				"where nfl_players._id = nfl_fantasy_stats._id order by real_stats DESC";
	 	setupTab(tpl.createPerformerList(query, dbHelp), "ALL");
	 	query = "select fName, lName,nfl_players._id from nfl_players, nfl_fantasy_stats " +
				"where nfl_players._id = nfl_fantasy_stats._id and position_id = 0 order by real_stats DESC";
		setupTab(tpl.createPerformerList(query, dbHelp), "QB");
		query = "select fName, lName,nfl_players._id from nfl_players, nfl_fantasy_stats " +
				"where nfl_players._id = nfl_fantasy_stats._id and position_id = 2 order by real_stats DESC";
		setupTab(tpl.createPerformerList(query, dbHelp), "RB");
		query = "select fName, lName,nfl_players._id from nfl_players, nfl_fantasy_stats " +
				"where nfl_players._id = nfl_fantasy_stats._id and position_id = 1 order by real_stats DESC";
		setupTab(tpl.createPerformerList(query, dbHelp), "WR");
		query = "select fName, lName,nfl_players._id from nfl_players, nfl_fantasy_stats " +
				"where nfl_players._id = nfl_fantasy_stats._id and position_id = 3 order by real_stats DESC";
		setupTab(tpl.createPerformerList(query, dbHelp), "TE");
		query = "select fName, lName,nfl_players._id from nfl_players, nfl_fantasy_stats " +
				"where nfl_players._id = nfl_fantasy_stats._id and position_id = 4 order by real_stats DESC";
		setupTab(tpl.createPerformerList(query, dbHelp), "K");	
		
		final ScrollView sv = (ScrollView) this.findViewById(R.id.scrollViewGameDay);
		sv.post(new Runnable() {            
		    public void run() {
		           sv.fullScroll(View.FOCUS_UP);              
		    }
		});
		
		iV = (ImageView) this.findViewById(R.id.lineupimg);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				myIntent.putExtra("team_id", ""+our_manager_id);
				startActivityForResult(myIntent,0);
			}
		});
		
		iV = (ImageView) this.findViewById(R.id.matchupimage);
		iV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), MidweekActivity.class);
				myIntent.putExtra("team_id", ""+our_manager_id);
				startActivityForResult(myIntent,0);
			}
		});
		
		awayNameInfo.close();
		awayScoreInfo.close();
		teamScoreInfo.close();
		teamNameInfo.close();
	}
	
	
	public static DatabaseHelper getDbHelp(){
		return dbHelp;
	}
	
	private void setupTab(final View view, final String tag){
		View tabview = createTabView(tabs.getContext(), tag);
		TabSpec setContent = tabs.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory(){
			public View createTabContent(String tag){ return view;}
		});
		tabs.addTab(setContent);
	}
	
	private static View createTabView(final Context context, final String text){
		View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.tabText);
		tv.setText(text);
		return view;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelp != null) {
			dbHelp.close();
		}
	}

	private static DatabaseHelper dbHelp;
	private TabHost tabs;
}