package com.csci588.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PlayerActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_screen_layout);
		
		positions = new String[5];
		positions[0] = "QB";
		positions[1] = "WR";
		positions[2] = "RB";
		positions[3] = "TE";
		positions[4] = "K";
		
		final String p_id = getIntent().getStringExtra("player_id");
		
		String teamQuery = "select rosters.manager_id from rosters where (qb = "+p_id + 
				" or rb1 = " + p_id +
				" or rb2 = " + p_id +
				" or wr1 = " + p_id +
				" or wr2 = " + p_id +
				" or wrt = " + p_id +
				" or te = " +  p_id +
				" or k = " +   p_id +
				" or bn1 = " +   p_id +
				" or bn2 = " +   p_id +
				" or bn3 = " +   p_id +
				" or bn4 = " +   p_id +
				" or bn5 = " +   p_id +
				") AND week = 9";
		String ownerQuery = "select managers.username from managers where managers._id = (" + teamQuery + ")";
		Cursor managerInfo = GamedayActivity.getDbHelp().getQuery(teamQuery);
		Cursor playerInfo = GamedayActivity.getDbHelp().getQuery("SELECT * from nfl_players where _id = " + p_id);
		Cursor teamInfo = GamedayActivity.getDbHelp().getQuery(ownerQuery);
		
		/*determines the buttons for the player, based on thier team status */
		if (teamInfo.moveToFirst()) {
			managerInfo.moveToFirst();
			if (managerInfo.getInt(0) != 1) {
				LinearLayout ll = (LinearLayout) this
						.findViewById(R.id.buttonLayout);
				ll.setOrientation(LinearLayout.VERTICAL);
				Button cmp = new Button(this);
				cmp.setText("Compare");
				cmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				ll.addView(cmp);

				Button trd = new Button(this);
				trd.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				trd.setText("Trade For");
				ll.addView(trd);
			} else {
				String teamStateQuery = "select (qb = " + p_id + " OR wr1 = "
						+ p_id + " or wr2 = " + p_id + " or wrt = " + p_id
						+ " or rb1 = " + p_id + " or rb2 = " + p_id
						+ " or k = " + p_id + " or te = " + p_id
						+ ") from rosters where manager_id = "
						+ managerInfo.getString(0) + " and week = " + "9";
				Cursor teamStateInfo = GamedayActivity.getDbHelp().getQuery(
						teamStateQuery);
				teamStateInfo.moveToFirst();
				if (teamStateInfo.moveToFirst()) {
					if (teamStateInfo.getInt(0) == 1) {
						LinearLayout ll = (LinearLayout) this
								.findViewById(R.id.buttonLayout);
						ll.setOrientation(LinearLayout.VERTICAL);
						Button cmp = new Button(this);
						cmp.setText("Compare");
						cmp.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						ll.addView(cmp);

						Button add = new Button(this);
						add.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						add.setText("Bench");
						ll.addView(add);

						Button drop = new Button(this);
						drop.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						drop.setText("Drop");
						drop.setOnClickListener(new View.OnClickListener() {
				             public void onClick(View v) {
				                 // Perform action on click
				            	 PlayerActions.dropPlayer(p_id, "1", "9");
				             }
				         });
						ll.addView(drop);
					} else {
						LinearLayout ll = (LinearLayout) this
								.findViewById(R.id.buttonLayout);
						ll.setOrientation(LinearLayout.VERTICAL);
						Button cmp = new Button(this);
						cmp.setText("Compare");
						cmp.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						ll.addView(cmp);

						Button strt = new Button(this);
						strt.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						strt.setText("Start");
						ll.addView(strt);

						Button drop = new Button(this);
						drop.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						drop.setText("Drop");
						drop.setOnClickListener(new View.OnClickListener() {
				             public void onClick(View v) {
				                 // Perform action on click
				            	 PlayerActions.dropPlayer(p_id, "1", "9");
				             }
				         });
						ll.addView(drop);
					}
				}
				teamStateInfo.close();
			}
		}else{
			LinearLayout ll = (LinearLayout) this.findViewById(R.id.buttonLayout);
			ll.setOrientation(LinearLayout.VERTICAL);
			Button cmp = new Button(this);
			cmp.setText("Compare");
			cmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ll.addView(cmp);
			
			Button add = new Button(this);
			add.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			add.setText("Add");
			add.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 PlayerActions.addPlayer(p_id, "1", "9");
	             }
	         });
			ll.addView(add);
		}
		setFantasyTeamName(teamInfo);
		setPositionTeam(playerInfo);
		setPlayerName(playerInfo);
		setByeWeek(playerInfo);
		setNextGame(playerInfo);
		playerInfo.close();
		teamInfo.close();
		managerInfo.close();
	}
	
	private void setPlayerName(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_name);
		if(cursor.moveToFirst()){
			tv.setText(cursor.getString(1) + " " + cursor.getString(2));
		}
	}
		
	private void setFantasyTeamName(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_fantasy);
		if(cursor.moveToFirst()){
			tv.setText("Owned by: " + cursor.getString(0));
		}
		else{
			tv.setText("Free Agent");
		}
	}
	
	private void setPositionTeam(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_posteam);
		String textBox = "";
		if(cursor.moveToFirst()){
			textBox += positions[cursor.getInt(4)];
			String teamQuery = "select nfl_teams.abbreviation from nfl_teams where _id = " + cursor.getString(3);
			Cursor teamAbv =  GamedayActivity.getDbHelp().getQuery(teamQuery);
			teamAbv.moveToFirst();
			textBox += " ( " + teamAbv.getString(0) + " )";
			teamAbv.close();
		}
		tv.setText(textBox);
	}
	
	private void setByeWeek(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_bye);
		if(cursor.moveToFirst()){
			String teamQuery = "select nfl_teams.bye from nfl_teams where _id = " + cursor.getString(3);
			Cursor teamBye =  GamedayActivity.getDbHelp().getQuery(teamQuery);
			teamBye.moveToFirst();
			tv.setText("Bye Week: " + teamBye.getString(0));
			teamBye.close();
		}
	}
	
	
	private void setNextGame(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_nextgame);
		if(cursor.moveToFirst()){
			String gameQuery = "SELECT away_team, home_team from nfl_schedule where week = 9 AND ( home_team = " + cursor.getString(3) +
					" OR away_team = " + cursor.getString(3) + ")";
			Cursor game =  GamedayActivity.getDbHelp().getQuery(gameQuery);
			game.moveToFirst();
			String gamesTeamQuery = "SELECT nfl_teams.abbreviation from nfl_teams where nfl_teams._id IN("
					+ game.getString(0) + " ," + game.getString(1) + ")";
			Cursor teams = GamedayActivity.getDbHelp().getQuery(gamesTeamQuery);
			teams.moveToFirst();
			String matchup = "Next Game: " + teams.getString(0) + " @ ";
			teams.moveToNext();
			matchup += teams.getString(0);
			teams.close();
			game.close();
			tv.setText(matchup);
		}
	}
	
	private String[] positions;
}
